static class IntepretException extends Exception {
  
  IntepretException(int index) {
    super("Intepret error at index: " + index);
  }

  IntepretException(int index, String message) {
    super("Intepret error at index " + index + ": " + message);
  }
}
