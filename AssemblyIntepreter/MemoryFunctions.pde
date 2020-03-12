static class MemoryFunctions {
  static class Mov implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      short param1 = cpu.readNext();
      OperandType type = OperandConvertor.getType(param1);
      Short memloc = null;
      switch(type) {
      case CONSTANT:
      case REGISTER:
        throw new IntepretException(cpu.getIndex(), "Can not move into a constant value or register");
      case MEMORY:
        memloc = OperandConvertor.extractValue(param1);
        break;
      case MEMORYSHIFT:
        memloc = cpu.memoryShift(param1);
        break;
      }
      cpu.write(memloc, read(cpu, cpu.readNext()));
    }
  }

  static class Ldr implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      short param1 = cpu.readNext();
      OperandType type = OperandConvertor.getType(param1);
      switch(type) {
      case CONSTANT:
      case MEMORY:
      case MEMORYSHIFT:
        throw new IntepretException(cpu.getIndex(), "Have to load into register");      
      case REGISTER:
        break;
      }
      short register = OperandConvertor.extractValue(param1);
      short val = cpu.readNext();
      cpu.setRegister(register, read(cpu, val));
    }
  }

  static short read(CPU cpu, short param) {
    OperandType type = OperandConvertor.getType(param);
    Short memloc = null;
    switch(type) {
    case CONSTANT:
      return  OperandConvertor.extractValue(param);
    case MEMORY:
      memloc = OperandConvertor.extractValue(param);
      return cpu.read(memloc);
    case MEMORYSHIFT:
      return cpu.read(cpu.memoryShift(param));
    case REGISTER:
      short register = OperandConvertor.extractValue(param);
      return cpu.getRegister(register);
    }
    return 0;
  }
}
