package JAssembly;

@FunctionalInterface
public interface Command<T> {
	public boolean execute(T t) throws InterpretException;
}
