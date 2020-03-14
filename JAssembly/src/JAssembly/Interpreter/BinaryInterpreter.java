package JAssembly.Interpreter;

import java.util.ArrayList;
import java.util.List;

import JAssembly.InterpretException;

public class BinaryInterpreter {

	public void interpret(String[] lines) throws InterpretException {
		String code = extractCode(lines);
		if (!code.matches("[01\\s]*"))
			throw new InterpretException("Only accepts 0 or 1 in the file");
		String[] data = code.split("\\s+");
		List<Short> program = new ArrayList<>();
		for (String datam : data)
			program.add((short) Integer.parseInt(datam, 2));
		CPU cpu = new CPU();
		cpu.loadIntoMemory(program);
		cpu.execute();
	}

	private String extractCode(String[] lines) {
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line + " ");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
