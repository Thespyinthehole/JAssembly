package JAssembly.Interpreter.OPCodes;

import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.Flag;

public class Jump {

	private static boolean jumpTo(CPU cpu) throws InterpretException {
		short param = cpu.readNext();
		OperandType type = OperandConvertor.getType(param);
		short value = 0;
		switch (type) {
		case MEMORYLOCATION:
		case MEMORYOFFSET:
			cpu.halt();
			throw new InterpretException("Cannot directly access memory at index: '" + cpu.getIndex() + "'");
		case CONSTANT:
			value = OperandConvertor.extractValue(param);
			break;
		case REGISTER:
			value = cpu.getRegister(OperandConvertor.extractValue(param));
		}

		cpu.setPC(value);
		return true;
	}

	public static boolean jump(CPU cpu) throws InterpretException {
		return jumpTo(cpu);
	}

	public static boolean jumpZero(CPU cpu) throws InterpretException {
		if (cpu.getFlag(Flag.ZERO)) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}

	public static boolean jumpLessThan(CPU cpu) throws InterpretException {
		if (cpu.getFlag(Flag.NEGATIVE)) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}

	public static boolean jumpGreaterThan(CPU cpu) throws InterpretException {
		if (!(cpu.getFlag(Flag.NEGATIVE) || cpu.getFlag(Flag.ZERO))) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}

	public static boolean compare(CPU cpu) throws InterpretException {
		short param = cpu.readNext();
		OperandType type = OperandConvertor.getType(param);
		short value = 0;
		switch (type) {
		case MEMORYLOCATION:
		case MEMORYOFFSET:
		case CONSTANT:
			throw new InterpretException("Can only compare a register");
		case REGISTER:
			value = cpu.getRegister(OperandConvertor.extractValue(param));
		}
		param = cpu.readNext();
		type = OperandConvertor.getType(param);
		short value1 = 0;
		switch (type) {
		case MEMORYLOCATION:
		case MEMORYOFFSET:
			throw new InterpretException("Can only compare to a register or a constant");
		case CONSTANT:
			value1 = OperandConvertor.extractValue(param);
			break;
		case REGISTER:
			value1 = cpu.getRegister(OperandConvertor.extractValue(param));
		}
		
		cpu.setFlag(Flag.ZERO, value == value1);
		cpu.setFlag(Flag.NEGATIVE, value < value1);
		return true;
	}
}
