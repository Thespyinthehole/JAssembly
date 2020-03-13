enum Flag {
  NEGATIVE, ZERO
}

class CPU {

  short[] memory;
  short[] registers;
  int pc;

  boolean negative = false;
  ;
  boolean zero = false;
  boolean halted = false;

  CPU() {
    this(1024, 8);
  }

  CPU(int memorySize, int registerCount) {
    memory = new short[memorySize];
    registers = new short[registerCount];
  }

  void loadIntoMemory(Program program) throws SyntaxException {
    program.loadIntoMemory(memory);
  }

  void loadIntoMemory(short[] memory) {
    System.arraycopy(memory, 0, this.memory, 0, memory.length);
  }

  void step() {
    if (read(pc) == 0) {
      halted = true;
      setFlag(Flag.NEGATIVE, false);      
      setFlag(Flag.ZERO, false);
      return;
    }
    short val = readNext();
    Instruction func = OPCodes.getInstruction(val);
    try {
      func.action(this);
    } 
    catch (IntepretException e) {
      halted = true;
      println(e);
      return;
    }
  }

  boolean getFlag(Flag flag) {
    switch(flag) {
    case NEGATIVE:
      return negative;
    case ZERO:
      return zero;
    }
    return false;
  }

  void setFlag(Flag flag, boolean val) {
    switch(flag) {
    case NEGATIVE:
      negative = val;
      break;
    case ZERO:
      zero = val;
      break;
    }
  }

  short readNext() {
    return read(pc++);
  }

  short read(int index) {
    return memory[index];
  }

  short getRegister(int index) {
    return registers[index];
  }

  void setRegister(int index, short value) {
    registers[index] = value;
  }

  short getIndex() {
    return (short)(pc - 1);
  }

  short memoryShift(short param) {
    short memloc = (short)(OperandConvertor.extractValue(param) + cpu.getIndex());
    return mod(memloc, (short)memory.length);
  }

  void write(int memloc, short value) {
    memory[memloc] = value;
  }

  void run() {  
    while (!halted)  
      step();
  }

  short mod(short a, short b) {
    return (short)(((a % b) + b) % b);
  }

  String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Registers: ");
    for (short register : cpu.registers) {
      builder.append(register + " ");
    }

    builder.append("\nMemory: ");
    for (int i = 0; i < cpu.memory.length; i++) {
      if (i == cpu.pc)
        builder.append(">");
      builder.append(cpu.memory[i] + " ");
    }
    builder.append("\nPC: " + cpu.pc);
    builder.append("\nFlags:");
    builder.append("\n     Negative: " + cpu.getFlag(Flag.NEGATIVE));
    builder.append("\n     Zero: " + cpu.getFlag(Flag.ZERO));
    builder.append("\n------------------------");
    return builder.toString();
  }
}
