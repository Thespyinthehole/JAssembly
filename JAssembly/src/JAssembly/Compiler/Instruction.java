package JAssembly.Compiler;

import java.util.EnumSet;

import JAssembly.Command;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.OPCodes.Arithmetic;
import JAssembly.Interpreter.OPCodes.Jump;
import JAssembly.Interpreter.OPCodes.Memory;
import JAssembly.Interpreter.OPCodes.IO;

@SuppressWarnings("unchecked")
public enum Instruction {

	HALT(0, null), 
	MOV(1, Memory::mov, OperandType.memory(), OperandType.all()),
	LDR(2, Memory::ldr, OperandType.register(), OperandType.all()),
	PUSH(3, Memory::push, OperandType.storage(), OperandType.all()),
	CMP(8, Jump::compare, OperandType.register(), OperandType.accessible()),
	JMP(9, Jump::jump, OperandType.accessible()),
	JMPZ(10, Jump::jumpZero, OperandType.accessible()),
	JMPL(11, Jump::jumpLessThan, OperandType.accessible()),
	JMPG(12, Jump::jumpGreaterThan, OperandType.accessible()),
	FUNC(13, Jump::function, OperandType.accessible()),
	RET(14, Jump::ret),
	PRT(16, IO::print, OperandType.accessible()),
	INP(17, IO::input, OperandType.register()),
	INC(32, Arithmetic::inc, OperandType.register()),
	DEC(33, Arithmetic::dec, OperandType.register()),
	ADD(34, Arithmetic::add, OperandType.register(), OperandType.accessible()),
	SUB(35, Arithmetic::sub, OperandType.register(), OperandType.accessible()),
	MUL(36, Arithmetic::mul, OperandType.register(), OperandType.accessible()),
	DIV(37, Arithmetic::div, OperandType.register(), OperandType.accessible()),
	MOD(38, Arithmetic::mod, OperandType.register(), OperandType.accessible()),;

	// Parameters
	private short opcode;
	private Command<CPU> opcodeCommand;
	private EnumSet<OperandType>[] params;

	// Constructors
	private Instruction(int opcode, Command<CPU> opcodeCommand, EnumSet<OperandType>... params) {
		this.opcode = (short) opcode;
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
