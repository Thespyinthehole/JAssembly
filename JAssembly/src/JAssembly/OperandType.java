package JAssembly;

import java.util.EnumSet;

public enum OperandType {
	CONSTANT, MEMORYLOCATION, REGISTER, MEMORYOFFSET;

	private static EnumSet<OperandType> all = EnumSet.allOf(OperandType.class);
	private static EnumSet<OperandType> register = EnumSet.of(OperandType.REGISTER);
	private static EnumSet<OperandType> storage = EnumSet.of(OperandType.REGISTER, OperandType.MEMORYLOCATION,
			OperandType.MEMORYOFFSET);
	private static EnumSet<OperandType> memory = EnumSet.of(OperandType.MEMORYLOCATION, OperandType.MEMORYOFFSET);
	private static EnumSet<OperandType> accessible = EnumSet.of(OperandType.REGISTER, OperandType.CONSTANT);

	public static EnumSet<OperandType> all() {
		return all;
	}

	public static EnumSet<OperandType> register() {
		return register;
	}

	public static EnumSet<OperandType> storage() {
		return storage;
	}

	public static EnumSet<OperandType> memory() {
		return memory;
	}

	public static EnumSet<OperandType> accessible() {
		return accessible;
	}
}