package JAssembly.Compiler;

import java.util.EnumSet;
import java.util.function.Predicate;

import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.CPU;
import JAssembly.Interpreter.OPCodes.Arithmetic;
import JAssembly.Interpreter.OPCodes.Jump;
import JAssembly.Interpreter.OPCodes.Memory;

@SuppressWarnings("unchecked")
public enum InstructionParser {
	
	HALT((short) 0, null), 
	MOV((short)  1, Memory::mov, EnumSet.of(OperandType.MEMORY, OperandType.MEMORYSHIFT), EnumSet.allOf(OperandType.class)),
	LDR((short)  2, Memory::ldr, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	PUSH((short) 3, Memory::push, EnumSet.of(OperandType.REGISTER), EnumSet.allOf(OperandType.class)),
	JMP((short)  8, Jump::jump, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPZ((short) 9, Jump::jumpZero, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPL((short) 10, Jump::jumpLessThan, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	JMPG((short) 11, Jump::jumpGreaterThan, EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	ADD((short)  32, Arithmetic::add, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	SUB((short)  33, Arithmetic::sub, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	MUL((short)  34, Arithmetic::mul, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT)),
	DIV((short)  35, Arithmetic::div, EnumSet.of(OperandType.REGISTER), EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT));

	// Parameters
	short opcode;
	Predicate<CPU> opcodePredicate;
	EnumSet<OperandType>[] params;

	// Constructors
	InstructionParser(short opcode, Predicate<CPU> opcodePredicate, EnumSet<OperandType>... params) {
		this.opcode = opcode;
		this.opcodePredicate = opcodePredicate;
		this.params = params;
	}

	public short getOpcode() {
		return opcode;
	}
	
	public Predicate<CPU> getOpcodePredicate() {
		return this.opcodePredicate;
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
