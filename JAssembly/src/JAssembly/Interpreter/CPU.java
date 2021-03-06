package JAssembly.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JAssembly.Command;
import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Compiler.Instruction;

public class CPU {

	private short[] memory;
	private short[] registers;
	private short pc = 0;
	private List<Short> returnStack = new ArrayList<Short>();
	private boolean negative = false;
	private boolean zero = false;
	private boolean halted = false;
	private boolean flagged = false;

	@SuppressWarnings("unchecked")
	private final Command<CPU>[] OPCODES = new Command[40];

	public CPU() {
		this(1024, 8);
	}

	public CPU(int memorySize, int registerCount) {
		memory = new short[memorySize];
		registers = new short[registerCount];
		initOPCodes();
	}

	public void loadIntoMemory(List<Short> program) throws InterpretException {
		if (program.size() + 1 > memory.length)
			throw new InterpretException("Program is too big for memory size: " + memory.length + ", needs at least "
					+ (program.size() + 1) + " memory locations");

		for (int i = 0; i < program.size(); i++)
			memory[i] = program.get(i);
	}

	public void execute(boolean debug) throws InterpretException {
		while (!halted)
			step(debug);
	}

	private void initOPCodes() {
		Arrays.stream(Instruction.values()).filter(p -> p.getOpcodeCommand() != null)
				.forEach(p -> OPCODES[p.getOpcode()] = p.getOpcodeCommand());
	}

	private void step(boolean debug) throws InterpretException {
		short val = memory[pc++];
		flagged = false;
		if (val == 0) {
			halted = true;
			return;
		}
		if (val < 0 || val >= OPCODES.length)
			throw new InterpretException(getIndex(), "'" + val + "' is not an instruction");

		Command<CPU> opcode = OPCODES[val];
		if (opcode == null)
			throw new InterpretException(getIndex(), "'" + val + "' is not an instruction");

		boolean success = opcode.execute(this);
		if (!success) {
			halted = true;
			return;
		}

		if (!flagged) {
			zero = false;
			negative = false;
		}
		if (debug)
			System.out.println(this);
	}

	public short readNext() throws InterpretException {
		if (pc + 1 < 0 || pc + 1 >= memory.length)
			throw new InterpretException("Memory location '" + (pc + 1) + "' does not exist");
		return memory[pc++];
	}

	public boolean getFlag(Flag flag) {
		switch (flag) {
		case NEGATIVE:
			return negative;
		case ZERO:
			return zero;
		}
		return false;
	}

	public void setFlag(Flag flag, boolean val) {
		flagged = true;
		switch (flag) {
		case NEGATIVE:
			negative = val;
			break;
		case ZERO:
			zero = val;
			break;
		}
	}

	public void addToReturnStack() {
		returnStack.add(pc);
	}

	public void returnToLocation() {
		setPC(returnStack.remove(returnStack.size() - 1));
	}

	public short readNextValue() throws InterpretException {
		short param = readNext();
		OperandType type = OperandConvertor.getType(param);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
			return param;
		case MEMORYLOCATION:
			memloc = OperandConvertor.extractValue(param);
			return read(memloc);
		case MEMORYOFFSET:
			return read(memoryShift(param));
		case REGISTER:
			short register = OperandConvertor.extractValue(param);
			return getRegister(register);
		}
		return 0;
	}

	public short read(int index) throws InterpretException {
		if (index < 0 || index >= memory.length)
			throw new InterpretException("Memory location '" + index + "' does not exist");
		return memory[index];
	}

	public short getRegister(int index) throws InterpretException {
		if (index < 0 || index >= registers.length)
			throw new InterpretException("Register '" + index + "' does not exist");
		return registers[index];
	}

	public void setRegister(int index, short value) throws InterpretException {
		if (index < 0 || index >= registers.length)
			throw new InterpretException("Register '" + index + "' does not exist");
		registers[index] = value;
	}

	public void setPC(short pc) {
		this.pc = pc;
	}

	public short getIndex() {
		return (short) (pc - 1);
	}

	public short memoryShift(short param) {
		short memloc = (short) (OperandConvertor.extractValue(param) + (pc - 1));
		return mod(memloc, (short) memory.length);
	}

	public void write(int memloc, short value) throws InterpretException {
		if (memloc < 0 || memloc >= memory.length)
			throw new InterpretException("Memory location '" + memloc + "' does not exist");
		memory[memloc] = value;
	}

	public void halt() {
		halted = true;
	}

	private short mod(short a, short b) {
		return (short) (((a % b) + b) % b);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Registers: ");
		for (short register : registers) {
			builder.append(getOperandString(register) + " ");
		}

		builder.append("\nMemory: ");
		for (int i = 0; i < memory.length; i++) {
			if (i == pc)
				builder.append(">");
			builder.append(getOperandString(memory[i]) + " ");
		}
		builder.append("\nPC: " + pc);
		builder.append("\nFlags:");
		builder.append("\n     Negative: " + getFlag(Flag.NEGATIVE));
		builder.append("\n     Zero: " + getFlag(Flag.ZERO));
		builder.append("\n------------------------");
		return builder.toString();
	}

	private String getOperandString(short value) {
		String starter = "";
		OperandType type = OperandConvertor.getType(value);
		short amount = OperandConvertor.extractValue(value);
		switch (type) {
		case MEMORYOFFSET:
			starter = (amount >= 0 ? "+" : "");
		case MEMORYLOCATION:
			starter = "m" + starter;
			break;
		case REGISTER:
			starter = "r";
			break;
		case CONSTANT:
		}
		String literalValue = String.valueOf(amount);
		return (starter + literalValue);
	}
}
