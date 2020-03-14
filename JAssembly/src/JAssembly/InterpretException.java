package JAssembly;

public class InterpretException extends Exception {

	public InterpretException(String message) {
		super(message);
	}

	public InterpretException(int index) {
		super("Intepret error at index: " + index);
	}

	public InterpretException(int index, String message) {
		super("Intepret error at index " + index + ": " + message);
	}
}