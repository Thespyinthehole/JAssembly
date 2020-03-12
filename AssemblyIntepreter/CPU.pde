class CPU {
  short[] memory;
  short[] registers;
  int pc;
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
  
    print("Registers: ");
    for (short register : registers) {
      print(register + " ");
    }

    print("\nMemory: ");
    for (short mem : memory) {
      print(mem + " ");
    }
    println();
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
    print("Registers: ");
    for (short register : registers) {
      print(register + " ");
    }

    print("\nMemory: ");
    for (short mem : memory) {
      print(mem + " ");
    }
    println();
    while (!halted)  
      step();
  }
  
  short mod(short a, short b){
    return (short)(((a % b) + b) % b); 
  }
}
