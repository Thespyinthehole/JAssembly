class Line {
  String opcode;
  String[] operands;
  int lineNum;

  Line(String instruction, int lineNum) {
    this.lineNum = lineNum;
    String[] ops = instruction.split(" +"); 
    int len = ops.length;
    opcode = ops[0];
    operands = new String[len-1];
    System.arraycopy(ops, 1, operands, 0, len-1);
  }

  short[] toBinary(HashMap<String, String> identifiers) throws SyntaxException {
    short[] instruction = new short[1+operands.length];
    instruction[0] = OPCodes.getOPCode(opcode, operands.length, lineNum);
    for (int i = 0; i < operands.length; i++) {
      instruction[i+1] = OperandConvertor.convertOperand(operands[i], identifiers, lineNum, i+1);
    }
    return instruction;
  }

  int getMemorySize(){
    return operands.length + 1; 
  }
}
