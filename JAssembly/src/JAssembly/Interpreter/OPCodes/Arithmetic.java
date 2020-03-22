package JAssembly.Interpreter.OPCodes;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.Flag;

public class Arithmetic {

	private static boolean operation(CPU cpu, BiFunction<Short, Short, Short> function) throws InterpretException {
		short param = cpu.readNext();

		if (OperandConvertor.getType(param) != OperandType.REGISTER) {
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only load into registers");
			return false;
		}

		short register = OperandConvertor.extractValue(param);

		short v1 = cpu.getRegister(register);
		short v2 = getNextParam(cpu);

		Short output = function.apply(v1, v2);
		if (output == null)
			return false;
		
		cpu.setFlag(Flag.ZERO, output == 0);
		cpu.setFlag(Flag.NEGATIVE, output < 0);
		cpu.setRegister(register, output);
		return true;
	}

	private static boolean operation(CPU cpu, UnaryOperator<Short> function) throws InterpretException {
		short param = cpu.readNext();

		if (OperandConvertor.getType(param) != OperandType.REGISTER) {
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only load into registers");
			return false;
		}

		short register = OperandConvertor.extractValue(param);

		short v1 = cpu.getRegister(register);

		Short output = function.apply(v1);
		if (output == null)
			return false;
		
		cpu.setFlag(Flag.ZERO, output == 0);
		cpu.setFlag(Flag.NEGATIVE, output < 0);
		cpu.setRegister(register, output);
		return true;
	}
	
	private static short getNextParam(CPU cpu) throws InterpretException {
		short param = cpu.readNext();
		switch(OperandConvertor.getType(param)) {
		case CONSTANT:
			return OperandConvertor.extractValue(param);
		case REGISTER:
			return cpu.getRegister(OperandConvertor.extractValue(param));
		default:
			cpu.halt();
			throw new InterpretException("Cannot directly access memory at index: '" + cpu.getIndex() + "'");
		}
	}

	public static boolean add(CPU cpu) throws InterpretException {
		return operation(cpu, (a, b) -> (short) (a + b));
	}

	public static boolean sub(CPU cpu) throws InterpretException {
		return operation(cpu, (a, b) -> (short) (a - b));
	}

	public static boolean mul(CPU cpu) throws InterpretException {
		return operation(cpu, (a, b) -> (short) (a * b));
	}

	public static boolean div(CPU cpu) throws InterpretException {
		return operation(cpu, (a, b) -> {
			if (b == 0) {
				cpu.halt();
				System.err.println("Division by 0 at: '" + cpu.getIndex() + "'");
				return null;
			}
			return (short) (a / b);
		});
	}
	
	public static boolean inc(CPU cpu) throws InterpretException {
		return operation(cpu, a -> (short) (a + 1));
	}
	
	public static boolean dec(CPU cpu) throws InterpretException {
		return operation(cpu, a -> (short) (a - 1));
	}
	
	public static boolean mod(CPU cpu) throws InterpretException {
		return operation(cpu, (a, b) -> (short) (a % b));
	}
}
