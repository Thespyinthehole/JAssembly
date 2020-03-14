package JAssembly;

public class SyntaxException extends Exception {
	public SyntaxException(int lineNum) {
		super("Syntax error on line " + lineNum);
	}

	public SyntaxException(int lineNum, String message) {
		super("Syntax error on line " + lineNum + ": " + message);
	}
}