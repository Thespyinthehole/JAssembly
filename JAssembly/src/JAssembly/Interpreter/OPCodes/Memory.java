package JAssembly.Interpreter.OPCodes;

import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;

public class Memory {

	public static boolean mov(CPU cpu) throws InterpretException {
		short param = cpu.readNext();
		OperandType type = OperandConvertor.getType(param);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
		case REGISTER:
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only move into memory");
			return false;
		case MEMORY:
			memloc = OperandConvertor.extractValue(param);
			break;
		case MEMORYSHIFT:
			memloc = cpu.memoryShift(param);
			break;
		}

		cpu.write(memloc, cpu.readNextValue());
		return true;
	}

	public static boolean ldr(CPU cpu) throws InterpretException {
		short param = cpu.readNext();
		OperandType type = OperandConvertor.getType(param);
		if (type != OperandType.REGISTER) {
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex() + "': Can only load into registers");
			return false;
		}

		cpu.setRegister(OperandConvertor.extractValue(param), cpu.readNextValue());
		return true;
	}

	public static boolean push(CPU cpu) {
		short param1 = cpu.readNext();
		OperandType type = OperandConvertor.getType(param1);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
			cpu.halt();
			System.err.println("Intepret error at index '" + cpu.getIndex()
					+ "': Can only push into a register or memory location");
			return false;
		case MEMORY:
			memloc = OperandConvertor.extractValue(param1);
			break;
		case MEMORYSHIFT:
			memloc = cpu.memoryShift(param1);
			break;
		case REGISTER:
			short register = OperandConvertor.extractValue(param1);
			short val = cpu.readNext();
			cpu.setRegister(register, val);
			return true;
		}

		cpu.write(memloc, cpu.readNext());
		return true;
	}
}
