package JAssembly.Interpreter.OPCodes;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.Flag;

public class Jump {

	private static OperandConvertor convertor = new OperandConvertor();

	private static void jumpTo(CPU cpu) {
		short param = cpu.readNext();
		OperandType type = convertor.getType(param);
		short value = 0;
		switch (type) {
		case MEMORY:
		case MEMORYSHIFT:
			cpu.halt();
			System.err.println("Cannot directly access memory at index: '" + cpu.getIndex() + "'");
			return;
		case CONSTANT:
			value = convertor.extractValue(param);
			break;
		case REGISTER:
			value = cpu.getRegister(convertor.extractValue(param));
		}

		cpu.setPC(value);
	}

	public static void jump(CPU cpu) {
		jumpTo(cpu);
	}
	
	public static void jumpZero(CPU cpu) {
		if (cpu.getFlag(Flag.ZERO)) {
			jumpTo(cpu);
		} else {
			cpu.readNext();
		}
	}
	
	public static void jumpLessThen(CPU cpu) {
		if (cpu.getFlag(Flag.NEGATIVE)) {
			jumpTo(cpu);
		} else {
			cpu.readNext();
		}
	}
	
	public static void jumpGreaterThen(CPU cpu) {
		if (!(cpu.getFlag(Flag.NEGATIVE)|| cpu.getFlag(Flag.ZERO))) {
			jumpTo(cpu);
		} else {
			cpu.readNext();
		}
	}
}
