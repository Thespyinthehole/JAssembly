package JAssembly.Interpreter;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Consumer;

import JAssembly.InterpretException;
import JAssembly.OperandConvertor;
import JAssembly.OperandType;
import JAssembly.Interpreter.OPCodes.Arithmetic;
import JAssembly.Interpreter.OPCodes.Jump;
import JAssembly.Interpreter.OPCodes.Memory;

public class CPU {

	private short[] memory;
	private short[] registers;
	private int pc = 0;

	private boolean negative = false;
	private boolean zero = false;
	private boolean halted = false;
	private boolean flagged = false;

	private OperandConvertor convertor = new OperandConvertor();

	@SuppressWarnings("unchecked")
	private final Consumer<CPU>[] OPCODES = (Consumer<CPU>[]) Array.newInstance(Consumer.class, 40);

	public CPU() {
		this(1024, 8);
	}

	public CPU(int memorySize, int registerCount) {
		memory = new short[memorySize];
		registers = new short[registerCount];
		initOPCodes();
	}

	public void loadIntoMemory(List<Short> program) throws InterpretException {
		if (program.size() > memory.length)
			throw new InterpretException("Program is too big for memory size: " + memory.length);

		for (int i = 0; i < program.size(); i++)
			memory[i] = program.get(i);
	}

	public void execute() throws InterpretException {
		while (!halted)
			step();
	}

	private void initOPCodes() {
		OPCODES[1] = Memory::mov;
		OPCODES[2] = Memory::ldr;
		OPCODES[3] = Memory::push;
		OPCODES[4] = Jump::jump;
		OPCODES[5] = Jump::jumpZero;
		OPCODES[6] = Jump::jumpLessThen;
		OPCODES[7] = Jump::jumpGreaterThen;
		OPCODES[32] = Arithmetic::add2;
		OPCODES[33] = Arithmetic::add3;
		OPCODES[34] = Arithmetic::sub2;
		OPCODES[35] = Arithmetic::sub3;
		OPCODES[36] = Arithmetic::mul2;
		OPCODES[37] = Arithmetic::mul3;
		OPCODES[38] = Arithmetic::div2;
		OPCODES[39] = Arithmetic::div3;
	}

	private void step() throws InterpretException {
		short val = memory[pc++];
		flagged = false;
		if (val == 0) {
			halted = true;
			return;
		}
		if (val < 0 || val >= OPCODES.length)
			throw new InterpretException(getIndex(), "'" + val + "' is not an instruction");

		Consumer<CPU> opcode = OPCODES[val];
		if (opcode == null)
			throw new InterpretException(getIndex(), "'" + val + "' is not an instruction");

		opcode.accept(this);
		if(!flagged) {
			zero = false;
			negative = false;
		}
		System.out.println(this);
	}

	public short readNext() {
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

	public short readNextValue() {
		short param = readNext();
		OperandType type = convertor.getType(param);
		Short memloc = null;
		switch (type) {
		case CONSTANT:
			return convertor.extractValue(param);
		case MEMORY:
			memloc = convertor.extractValue(param);
			return read(memloc);
		case MEMORYSHIFT:
			return read(memoryShift(param));
		case REGISTER:
			short register = convertor.extractValue(param);
			return getRegister(register);
		}
		return 0;
	}

	public short read(int index) {
		return memory[index];
	}

	public short getRegister(int index) {
		return registers[index];
	}

	public void setRegister(int index, short value) {
		registers[index] = value;
	}

	public void setPC(short pc) {
		this.pc = pc;
	}

	public short getIndex() {
		return (short) (pc - 1);
	}

	public short memoryShift(short param) {
		short memloc = (short) (convertor.extractValue(param) + pc - 1);
		return mod(memloc, (short) memory.length);
	}

	public void write(int memloc, short value) {
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
			builder.append(register + " ");
		}

		builder.append("\nMemory: ");
		for (int i = 0; i < memory.length; i++) {
			if (i == pc)
				builder.append(">");
			builder.append(memory[i] + " ");
		}
		builder.append("\nPC: " + pc);
		builder.append("\nFlags:");
		builder.append("\n     Negative: " + getFlag(Flag.NEGATIVE));
		builder.append("\n     Zero: " + getFlag(Flag.ZERO));
		builder.append("\n------------------------");
		return builder.toString();
	}
}
