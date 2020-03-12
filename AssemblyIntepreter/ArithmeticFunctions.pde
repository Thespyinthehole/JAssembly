static class ArithmeticFunctions { 

  static interface BiFunction<T, U, V> {
    V apply(T t, U u) throws IntepretException;
  }

  static void operation(CPU cpu, boolean two, BiFunction<Short, Short, Short> function) throws IntepretException {
    short param1 = cpu.readNext();

    if (OperandConvertor.getType(param1) != OperandType.REGISTER)
      throw new IntepretException(cpu.getIndex(), "Need register parameter");

    short register = OperandConvertor.extractValue(param1);

    short v1 = two ? cpu.getRegister(register) : getNextParam(cpu);
    short v2 = getNextParam(cpu);

    cpu.setRegister(register, function.apply(v1, v2));
  }

  static short getNextParam(CPU cpu) throws IntepretException {
    short param2 = cpu.readNext();
    OperandType type = OperandConvertor.getType(param2);
    short val = 0;
    switch(type) {
    case MEMORY:
    case MEMORYSHIFT:
      throw new IntepretException(cpu.pc - 1, "Cannot directly access memory");
    case REGISTER:
      short p2 = OperandConvertor.extractValue(param2);
      val = cpu.getRegister(p2);
      break;
    case CONSTANT:
      val = OperandConvertor.extractValue(param2);
      break;
    }
    return val;
  }

  static class Add2 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, true, new BiFunction<Short, Short, Short>() {
        public Short apply(Short a, Short b) {
          return (short)(a + b);
        }
      }
      );
    }
  }

  static class Add3 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, false, new BiFunction<Short, Short, Short>() {
        public Short apply(Short a, Short b) {
          return (short)(a + b);
        }
      }
      );
    }
  }

  static class Sub2 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, true, new BiFunction<Short, Short, Short>() {
        public Short apply(Short a, Short b) {
          return (short)(a - b);
        }
      }
      );
    }
  }

  static class Sub3 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, false, new BiFunction<Short, Short, Short>() { 
        public Short apply(Short a, Short b) {
          return (short)(a - b);
        }
      }
      );
    }
  }

  static class Mul2 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, true, new BiFunction<Short, Short, Short>() { 
        public Short apply(Short a, Short b) {
          return (short)(a * b);
        }
      }
      );
    }
  }

  static class Mul3 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      operation(cpu, false, new BiFunction<Short, Short, Short>() { 
        public Short apply(Short a, Short b) {
          return (short)(a * b);
        }
      }
      );
    }
  }

  static class Div2 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      final CPU c = cpu;
      operation(cpu, true, new BiFunction<Short, Short, Short>() {
        public Short apply(Short a, Short b) throws IntepretException {
          if (b == 0)
          throw new IntepretException(c.getIndex()-2, "Division by zero");
          return (short)(a / b);
        }
      }
      );
    }
  }

  static class Div3 implements Instruction<CPU> {
    void action(CPU cpu) throws IntepretException {
      final CPU c = cpu;
      operation(cpu, false, new BiFunction<Short, Short, Short>() { 
        public Short apply(Short a, Short b) throws IntepretException {
          println("Divide " + a + " by " + b);
          if (b == 0)
          throw new IntepretException(c.getIndex()-2, "Division by zero");
          return (short)(a / b);
        }
      }
      );
    }
  }
}
