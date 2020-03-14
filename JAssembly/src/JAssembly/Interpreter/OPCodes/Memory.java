package JAssembly.Interpreter.OPCodes;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;

public class Memory {

	private static OperandConvertor convertor = new OperandConvertor();

	public static void mov(CPU cpu) {
		short param = cpu.readNext();
		OperandType type = convertor.getType(param);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
		case REGISTER:
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only move into memory");
			return;
		case MEMORY:
			memloc = convertor.extractValue(param);
			break;
		case MEMORYSHIFT:
			memloc = cpu.memoryShift(param);
			break;
		}

		cpu.write(memloc, cpu.readNextValue());
	}

	public static void ldr(CPU cpu) {
		short param = cpu.readNext();
		OperandType type = convertor.getType(param);
		if (type != OperandType.REGISTER) {
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only load into registers");
			return;
		}

		cpu.setRegister(convertor.extractValue(param), cpu.readNextValue());
	}

	public static void push(CPU cpu) {
		short param1 = cpu.readNext();
		OperandType type = convertor.getType(param1);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex()
					+ "': Can only push into a register or memory location");
			return;
		case MEMORY:
			memloc = convertor.extractValue(param1);
			break;
		case MEMORYSHIFT:
			memloc = cpu.memoryShift(param1);
			break;
		case REGISTER:
			short register = convertor.extractValue(param1);
			short val = cpu.readNext();
			cpu.setRegister(register, val);
			return;
		}

		cpu.write(memloc, cpu.readNext());
	}
}
