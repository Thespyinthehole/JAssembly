static class SyntaxException extends Exception {
  SyntaxException(int lineNum) {
    super("Syntax error on line " + lineNum);
  }

  SyntaxException(int lineNum, String message) {
    super("Syntax error on line " + lineNum + ": " + message);
  }
}
