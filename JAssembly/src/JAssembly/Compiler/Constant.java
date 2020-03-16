package JAssembly.Compiler;

public class Constant {
	private String value;
	private boolean used;

	public Constant(String value) {
		this.value = value;
	}

	public void use() {
		used = true;
	}

	public String getValue() {
		return value;
	}

	public boolean isUsed() {
		return used;
	}

}
