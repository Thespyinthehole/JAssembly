package JAssembly.Compiler;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;

public class InstructionParser {

	private OperandConvertor convertor = new OperandConvertor();

	// Form: 0001 constant, 0010 memory, 0100 memory shift, 1000 register. Can be
	// combined
	private byte[] params;
	private short opcode;

	public InstructionParser(int index, byte[] params) {
		this.params = params;
		this.opcode = (short) index;
	}

	public int getParamCount() {
		return params.length;
	}

	public short getOpcode() {
		return opcode;
	}

	public void checkParam(int index, short param, int lineNum, String opcode) {
		OperandType type = convertor.getType(param);
		switch (type) {
		case CONSTANT:
			if ((params[index] & 1) != 1)
				System.out.println("Warning: OPCode '" + opcode + "' on line " + lineNum
						+ " is not expecting a constant for operand " + index);
			break;
		case MEMORY:
			if ((params[index] & 2) != 2)
				System.out.println("Warning: OPCode '" + opcode + "' on line " + lineNum
						+ " is not expecting a memory address for operand " + index);
			break;
		case MEMORYSHIFT:
			if ((params[index] & 4) != 4)
				System.out.println("Warning: OPCode '" + opcode + "' on line " + lineNum
						+ " is not expecting a memory shift for operand " + index);
			break;
		case REGISTER:
			if ((params[index] & 8) != 8)
				System.out.println("Warning: OPCode '" + opcode + "' on line " + lineNum
						+ " is not expecting a register for operand " + index);
			break;
		}
	}
}
