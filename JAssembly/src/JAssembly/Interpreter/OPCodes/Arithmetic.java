package JAssembly.Interpreter.OPCodes;

import java.util.function.BiFunction;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.Flag;

public class Arithmetic {

	private static OperandConvertor convertor = new OperandConvertor();

	private static boolean operation(CPU cpu, BiFunction<Short, Short, Short> function) {
		short param = cpu.readNext();

		if (convertor.getType(param) != OperandType.REGISTER) {
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only load into registers");
			return false;
		}

		short register = convertor.extractValue(param);

		Short v1 = cpu.getRegister(register);
		Short v2 = getNextParam(cpu);

		if (v1 == null || v2 == null)
			return false;

		Short output = function.apply(v1, v2);
		if (output == null)
			return false;
		cpu.setFlag(Flag.ZERO, output == 0);
		cpu.setFlag(Flag.NEGATIVE, output < 0);
		cpu.setRegister(register, output);
		return true;
	}

	private static Short getNextParam(CPU cpu) {
		short param = cpu.readNext();

		OperandType type = convertor.getType(param);

		if (type == OperandType.CONSTANT)
			return convertor.extractValue(param);
		if (type == OperandType.REGISTER)
			return cpu.getRegister(convertor.extractValue(param));

		cpu.halt();
		System.err.println("Cannot directly access memory at index: '" + cpu.getIndex() + "'");
		return null;
	}

	public static boolean add(CPU cpu) {
		return operation(cpu, (a, b) -> {
			return (short) (a + b);
		});
	}

	public static boolean sub(CPU cpu) {
		return operation(cpu, (a, b) -> {
			return (short) (a - b);
		});
	}

	public static boolean mul(CPU cpu) {
		return operation(cpu, (a, b) -> {
			return (short) (a * b);
		});
	}

	public static boolean div(CPU cpu) {
		return operation(cpu, (a, b) -> {
			if (b == 0) {
				cpu.halt();
				System.err.println("Division by 0 at: '" + cpu.getIndex() + "'");
				return null;
			}
			return (short) (a / b);
		});
	}
}
