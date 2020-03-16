package JAssembly.Interpreter.OPCodes;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.Flag;

public class Jump {

	private static OperandConvertor convertor = new OperandConvertor();

	private static boolean jumpTo(CPU cpu) {
		short param = cpu.readNext();
		OperandType type = convertor.getType(param);
		short value = 0;
		switch (type) {
		case MEMORY:
		case MEMORYSHIFT:
			cpu.halt();
			System.err.println("Cannot directly access memory at index: '" + cpu.getIndex() + "'");
			return false;
		case CONSTANT:
			value = convertor.extractValue(param);
			break;
		case REGISTER:
			value = cpu.getRegister(convertor.extractValue(param));
		}

		cpu.setPC(value);
		return true;
	}

	public static boolean jump(CPU cpu) {
		return jumpTo(cpu);
	}

	public static boolean jumpZero(CPU cpu) {
		if (cpu.getFlag(Flag.ZERO)) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}

	public static boolean jumpLessThen(CPU cpu) {
		if (cpu.getFlag(Flag.NEGATIVE)) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}

	public static boolean jumpGreaterThen(CPU cpu) {
		if (!(cpu.getFlag(Flag.NEGATIVE)|| cpu.getFlag(Flag.ZERO))) {
			return jumpTo(cpu);
		} else {
			cpu.readNext();
		}
		return true;
	}
}
