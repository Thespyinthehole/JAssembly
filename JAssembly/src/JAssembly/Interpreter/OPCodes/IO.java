package JAssembly.Interpreter.OPCodes;

import java.io.IOException;

import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;

public class IO {
	public static boolean print(CPU cpu) throws InterpretException {
		short operand = cpu.readNext();
		OperandType type = OperandConvertor.getType(operand);
		int value = 0;

		switch (type) {
		case MEMORYLOCATION:
		case MEMORYOFFSET:
			throw new InterpretException(cpu.getIndex(), "Can not print data from a memory location");
		case CONSTANT:
			value = OperandConvertor.extractValue(operand);
			break;
		case REGISTER:
			value = OperandConvertor.extractValue(operand);
			value = cpu.getRegister(value);
			break;
		}
		
		if (value < 0)
			throw new InterpretException(cpu.getIndex(), "Can not convert a negative value to a character");

		System.out.print((char) value);
		return true;
	}

	public static boolean input(CPU cpu) throws InterpretException {
		short operand = cpu.readNext();
		OperandType type = OperandConvertor.getType(operand);
		int index = 0;

		switch (type) {
		case CONSTANT:
		case MEMORYLOCATION:
		case MEMORYOFFSET:
			throw new InterpretException(cpu.getIndex(), "Can only input into a register");
		case REGISTER:
			index = OperandConvertor.extractValue(operand);
			break;
		}
		try {
			cpu.setRegister(index, (short) System.in.read());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
