package JAssembly.Compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import JAssembly.OperandConvertor;
import JAssembly.SyntaxException;

public class AssemblyCompiler {

	private Map<String, String> constants = new HashMap<>();
	private boolean constant = true;
	private int memloc = 0;
	private OperandConvertor convertor = new OperandConvertor();
	private boolean lastJmp = false;
	private static Map<String, Short[]> opcodes = new HashMap<String, Short[]>();

	static {
		opcodes.put("HALT", new Short[] { 0 });
		opcodes.put("MOV", new Short[] { null, null, 1 });
		opcodes.put("LDR", new Short[] { null, null, 2 });
		opcodes.put("PUSH", new Short[] { null, null, 3 });
		opcodes.put("JMP", new Short[] { null, 4 });
		opcodes.put("JMPZ", new Short[] { null, 5 });
		opcodes.put("JMPL", new Short[] { null, 6 });
		opcodes.put("JMPG", new Short[] { null, 7 });
		opcodes.put("ADD", new Short[] { null, null, 32 });
		opcodes.put("SUB", new Short[] { null, null, 34 });
		opcodes.put("MUL", new Short[] { null, null, 36 });
		opcodes.put("DIV", new Short[] { null, null, 38 });
	}

	public void compile(String[] lines, File file) throws SyntaxException, IOException {
		Map<Integer, String> cleaned = cleanCodeAndExtractConstants(lines);

		List<Short> bytecodes = new ArrayList<>();

		for (Entry<Integer, String> entry : cleaned.entrySet()) {
			String line = entry.getValue();
			List<Short> bytecode = compileLine(line, entry.getKey());
			if (bytecode != null)
				bytecodes.addAll(bytecode);
		}

		writeToBinaryFile(bytecodes.toArray(new Short[0]), file);
	}

	private void writeToBinaryFile(Short[] bytecodes, File file) throws IOException {
		String filename = file.getName();
		int index = filename.indexOf(".");
		filename = filename.substring(0, index) + ".jb";
		File binaryFile = new File(filename);
		if (binaryFile.exists())
			binaryFile.delete();
		binaryFile.createNewFile();

		StringBuilder builder = new StringBuilder();
		int val = 0;
		for (Short bytecode : bytecodes)
			builder.append(convertor.convertToBinaryString(bytecode) + ((++val % 3 == 0) ? "\n" : " "));
		builder.deleteCharAt(builder.length() - 1);

		FileWriter writer = new FileWriter(binaryFile);
		writer.write(builder.toString());
		writer.flush();
		writer.close();
	}

	private Map<Integer, String> cleanCodeAndExtractConstants(String[] lines) throws SyntaxException {
		Map<Integer, String> cleanCode = new HashMap<>();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			line = cleanLine(line);
			if (line.length() == 0 || line.equals(" ")) {
				continue;
			}
			line = extractConstants(line, i + 1);
			cleanCode.put(i + 1, line);
		}
		return cleanCode;
	}

	private String cleanLine(String line) {
		line = removeComments(line).trim();
		line = line.replaceAll("\\s+", " ");
		return line;
	}

	private String extractConstants(String line, int lineNum) throws SyntaxException {
		if (constant) {
			line = extractConstant(line, lineNum);
		}
		if (!constant) {
			line = extractLineName(line, lineNum);
		}
		return line;
	}

	private String extractConstant(String line, int lineNum) throws SyntaxException {
		int index = line.indexOf("=");
		if (index == -1) {
			constant = false;
			return line;
		}
		String[] split = line.split("=");
		if (split.length != 2)
			throw new SyntaxException(lineNum, "Constants need to be in the form <name> = <operand>");

		String name = split[0].trim();
		String value = split[1].trim();

		if (!name.matches("[a-z]+"))
			throw new SyntaxException(lineNum, "constant '" + name + "' needs to be in all lower case");

		if (!value.matches("(r|(m[+-]?))?[0-9]+"))
			throw new SyntaxException(lineNum, "operand '" + value + "' needs to be a operand");

		if (constants.containsKey(name))
			throw new SyntaxException(lineNum, "Constant '" + name + "' declared twice");

		constants.put(name, value);
		return "";
	}

	private String extractLineName(String line, int lineNum) throws SyntaxException {
		int index = line.indexOf(":");
		if (index != -1) {
			String lineName = line.substring(0, index);
			if (constants.containsKey(lineName))
				throw new SyntaxException(lineNum, "Constant '" + lineName + "' declared twice");

			constants.put(lineName, String.valueOf(memloc));
			line = line.substring(index + 1).trim();
		}
		String[] split = line.split(" ");
		memloc += split.length;
		return line;
	}

	private List<Short> compileLine(String line, int lineNum) throws SyntaxException {
		if (line.length() == 0)
			return null;
		String[] split = line.split(" ");
		String opcode = split[0];
		Short[] params = opcodes.get(opcode);
		if (params == null)
			throw new SyntaxException(lineNum, "Instruction '" + opcode + "' not found");

		if (params.length < split.length - 1)
			throw new SyntaxException(lineNum,
					"Instruction '" + opcode + "' does not accept " + (split.length - 1) + " operands");

		Short bytecode = params[split.length - 1];

		if (bytecode == null)
			throw new SyntaxException(lineNum,
					"Instruction '" + opcode + "' does not accept " + (split.length - 1) + " operands");

		boolean jmp = opcode.equals("JMP");
		
		if (jmp && lastJmp) {
			System.out.println("Warning: Unreachable code on line " + lineNum);
		}
		
		lastJmp = jmp;

		List<Short> instruction = new ArrayList<>();
		instruction.add(bytecode);

		for (int i = 1; i < split.length; i++)
			instruction.add(convertor.convertOperand(split[i], constants, lineNum));
		return instruction;
	}

	private String removeComments(String line) {
		int index = line.indexOf("#");
		if (index == -1)
			return line;
		return line.substring(0, index);
	}
}
