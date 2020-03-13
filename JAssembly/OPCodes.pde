static class OPCodes {
  
  static HashMap<String, Short[]> opcodes;

  static Instruction[] opcodeFuncs = new Instruction[] {
    null, //0 HALT
    new MemoryFunctions.Mov(), //1
    new MemoryFunctions.Ldr(), //2
    new MemoryFunctions.Push(), //3
    null, //4
    null, //5
    null, //6
    null, //7
    null, //8
    null, //9
    null, //10
    null, //11
    null, //12
    null, //13
    null, //14
    null, //15
    null, //16
    null, //17
    null, //18
    null, //19
    null, //20
    null, //21
    null, //22
    null, //23
    null, //24
    null, //25
    null, //26
    null, //27
    null, //28
    null, //29
    null, //30
    null, //31
    new ArithmeticFunctions.Add2(), //32
    new ArithmeticFunctions.Add3(), //33
    new ArithmeticFunctions.Sub2(), //34
    new ArithmeticFunctions.Sub3(), //35
    new ArithmeticFunctions.Mul2(), //36
    new ArithmeticFunctions.Mul3(), //37
    new ArithmeticFunctions.Div2(), //38
    new ArithmeticFunctions.Div3(), //39
  };

  static Instruction getInstruction(int index) {
    return opcodeFuncs[index];
  }

  static {
    opcodes = new HashMap<String, Short[]>();
    opcodes.put("MOV", new Short[]{null, null, 1});
    opcodes.put("LDR", new Short[]{null, null, 2});
    opcodes.put("PUSH", new Short[]{null, null, 3});
    opcodes.put("ADD", new Short[]{null, null, 32, 33});
    opcodes.put("SUB", new Short[]{null, null, 34, 35});
    opcodes.put("MUL", new Short[]{null, null, 36, 37});
    opcodes.put("DIV", new Short[]{null, null, 38, 39});
  }

  static short getOPCode(String instruction, int operandCount, int lineNum) throws SyntaxException {
    Short[] opcode = opcodes.get(instruction);
    if (opcode == null)
      throw new SyntaxException(lineNum, "Instruction not found");
    if (operandCount >= opcode.length)
      throw new SyntaxException(lineNum, "Incorrect operand count");
    Short finalOPCode = opcode[operandCount];
    if (finalOPCode == null)
      throw new SyntaxException(lineNum, "Incorrect operand count");
    return finalOPCode;
  }
}
