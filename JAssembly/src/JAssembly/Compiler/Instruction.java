package JAssembly.Compiler;

import java.util.EnumSet;

import JAssembly.Command;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.OPCodes.Arithmetic;
import JAssembly.Interpreter.OPCodes.Jump;
import JAssembly.Interpreter.OPCodes.Memory;

@SuppressWarnings("unchecked")
public enum Instruction {

	HALT((short) 0, null),
	MOV((short) 1, Memory::mov, EnumSet.of(OperandType.MEMORYLOCATION, OperandType.MEMORYOFFSET),
			EnumSet.allOf(OperandType.class)),
	LDR((short) 2, Memory::ldr, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	PUSH((short) 3, Memory::push, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	CMP((short) 8, Jump::compare, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMP((short) 9, Jump::jump, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPZ((short) 10, Jump::jumpZero, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPL((short) 11, Jump::jumpLessThan, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPG((short) 12, Jump::jumpGreaterThan, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	INC((short) 32, Arithmetic::inc, EnumSet.of(OperandType.REGISTER)),
	DEC((short) 33, Arithmetic::dec, EnumSet.of(OperandType.REGISTER)),
	ADD((short) 34, Arithmetic::add, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	SUB((short) 35, Arithmetic::sub, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	MUL((short) 36, Arithmetic::mul, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	DIV((short) 37, Arithmetic::div, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	MOD((short) 38, Arithmetic::mod, EnumSet.of(OperandType.REGISTER),
			EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),;

	// Parameters
	private short opcode;
	private Command<CPU> opcodeCommand;
	private EnumSet<OperandType>[] params;

	// Constructors
	private Instruction(short opcode, Command<CPU> opcodeCommand, EnumSet<OperandType>... params) {
		this.opcode = opcode;
		this.opcodeCommand = opcodeCommand;
		this.params = params;
	}

	public short getOpcode() {
		return opcode;
	}

	public Command<CPU> getOpcodeCommand() {
		return this.opcodeCommand;
	}

	public int getParamCount() {
		return params.length;
	}

	public void checkParam(int index, short param, int lineNum) {
		EnumSet<OperandType> paramToCheck = params[index];
		OperandType type = OperandConvertor.getType(param);
		if (!paramToCheck.contains(type)) {
			System.out.println("Warning: OPCode '" + this + "' on line " + lineNum + " is not expecting a " + type
					+ " for operand " + (index + 1));
		}
	}
}
