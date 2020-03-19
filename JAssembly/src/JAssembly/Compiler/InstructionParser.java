package JAssembly.Compiler;

import java.util.EnumSet;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;

@SuppressWarnings("unchecked")
public enum InstructionParser {
	HALT(0), 
	MOV(1, EnumSet.of(OperandType.MEMORY, OperandType.MEMORYSHIFT), EnumSet.allOf(OperandType.class)),
	LDR(2, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	PUSH(3, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	JMP(8, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPZ(9, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPL(10, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPG(11, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	ADD(32, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	SUB(33, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	MUL(34, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	DIV(35, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT));

	// Parameters
	short opcode;
	EnumSet<OperandType>[] params;

	// Constructors
	InstructionParser(int opcode, EnumSet<OperandType>... params) {
		this.opcode = (short)opcode;
		this.params = params;
	}

	public short getOpcode() {
		return opcode;
	}
	
	public int getParamCount() {
		return params.length;
	}
	
	public void checkParam(int index, short param, int lineNum) {
		EnumSet<OperandType> paramToCheck = params[index];
		OperandType type = OperandConvertor.getType(param);
		if (!paramToCheck.contains(type)) {
			System.out.println("Warning: OPCode '" + this + "' on line " + lineNum
					+ " is not expecting a constant for operand " + (index + 1));
		}
	}
}
