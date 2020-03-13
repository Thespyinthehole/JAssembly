enum OperandType {
  CONSTANT, MEMORY, REGISTER, MEMORYSHIFT
}

static class OperandConvertor {
  
  static short convertOperand(String operand, HashMap<String, String> identifiers, int lineNum, int operandCount) throws SyntaxException {
    if (operand.matches("[a-zA-Z]{2,}") || operand.matches("[a-zA-Z]") && operand.length() == 1) {
      String op = identifiers.get(operand);
      if (op == null)
        throw new SyntaxException(lineNum + 1, "Identifier not found for operand " + operandCount);
      short val = convertOperand(op, identifiers, lineNum, operandCount);
      return val;
    }

    if (operand.matches("-?[0-9]+")){
      int start = 0b0000000000000000;
      return (short)(start + convertToBytes(operand));
    }
    
    if (operand.matches("r[0-9]+")) {
      int start = 0b1100000000000000;
      return (short)(start + convertToBytes(operand.substring(1)));
    }

    if (operand.matches("m[0-9]+")) {
      int start = 0b0100000000000000;
      return (short)(start + convertToBytes(operand.substring(1)));
    }

    if (operand.matches("m[+][0-9]+")) {
      int start = 0b1000000000000000;
      return (short)(start + convertToBytes(operand.substring(2)));
    }

    if (operand.matches("m[-][0-9]+")) {
      int start = 0b1000000000000000;
      return (short)(start + convertToBytes(operand.substring(1)));
    }

    throw new SyntaxException(lineNum, "Invalid operand form for operand " + operandCount);
  }

  static OperandType getType(short operand) {
    short memory   = (short) 0b0100000000000000;
    short register = (short) 0b1100000000000000;
    short stripped = (short) (operand & register);

    if (stripped == 0)
      return OperandType.CONSTANT;

    if ((stripped ^ register) == 0)
      return OperandType.REGISTER;

    if ((operand & memory) != 0)
      return OperandType.MEMORY;


    return OperandType.MEMORYSHIFT;
  }

  static short convertToBytes(String data) {
    short value = Short.valueOf(data);
    int val = (int)pow(2, 13);
    if (abs(value) >= val)
      return value;
    short total = 0;
    
    if (value < 0) {
      value += val;
      total += val;
    }
    val /= 2;

    for (int i = 12; i >=0; i--) {
      if (value >= val) {
        value -= val; 
        total += val;
      }
      val /= 2;
    }
    return total;
  }

  static short extractValue(short value) {
    short stripper = (short) 0b0011111111111111;
    short stripped = (short)(value & stripper);
    short total = 0;
    int val = (int)pow(2, 13);

    if (stripped > val) {
      total -= val;
      stripped -= val;
    }

    val /= 2;

    for (int i = 12; i >=0; i--) {
      if (stripped >= val) {
        stripped -= val; 
        total += val;
      } 
      val /= 2;
    }

    return total;
  }
}
