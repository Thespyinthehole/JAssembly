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
import JAssembly.OperandType;
import JAssembly.SyntaxException;

public class AssemblyCompiler {

	private Map<String, Constant> constants = new HashMap<>();
	private Map<Integer, Integer> memoryToLine = new HashMap<>();
	private boolean constant = true;
	private Map<Integer, Boolean> unusedLines = new HashMap<>();
	private int memloc = 0;
	private OperandConvertor convertor = new OperandConvertor();
	private static Map<String, InstructionParser> opcodes = new HashMap<>();

	static {
		opcodes.put("HALT", new InstructionParser(0, new byte[] {}));
		opcodes.put("MOV", new InstructionParser(1, new byte[] { 6, 15 }));
		opcodes.put("LDR", new InstructionParser(2, new byte[] { 8, 15 }));
		opcodes.put("PUSH", new InstructionParser(3, new byte[] { 8, 15 }));
		opcodes.put("JMP", new InstructionParser(8, new byte[] { 9 }));
		opcodes.put("JMPZ", new InstructionParser(9, new byte[] { 9 }));
		opcodes.put("JMPL", new InstructionParser(10, new byte[] { 9 }));
		opcodes.put("JMPG", new InstructionParser(11, new byte[] { 9 }));
		opcodes.put("ADD", new InstructionParser(32, new byte[] { 8, 9 }));
		opcodes.put("SUB", new InstructionParser(33, new byte[] { 8, 9 }));
		opcodes.put("MUL", new InstructionParser(34, new byte[] { 8, 9 }));
		opcodes.put("DIV", new InstructionParser(35, new byte[] { 8, 9 }));
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

		for (Entry<String, Constant> constant : constants.entrySet()) {
			if (!constant.getValue().isUsed())
				System.out.println("Warning: Constant '" + constant.getKey() + "' is not used");
		}

		findUnreachable(cleaned, 1);

		for (Entry<Integer, Boolean> unused : unusedLines.entrySet()) {
			if (unused.getValue())
				System.out.println("Warning: Possible unreachable line at '" + unused.getKey() + "'");
		}

		writeToBinaryFile(bytecodes.toArray(new Short[0]), file);
	}

	private void findUnreachable(Map<Integer, String> lines, int lineNum) throws SyntaxException {
		String line = lines.get(lineNum);
		if (line == null)
			return;

		if (line.length() == 0) {
			findUnreachable(lines, lineNum + 1);
			return;
		}

		if (!(unusedLines.containsKey(lineNum) && unusedLines.get(lineNum)))
			return;

		String[] values = line.split(" ");
		String opcode = values[0];
		unusedLines.put(lineNum, false);
		switch (opcode) {
		case "HALT":
			return;
		case "JMP":
			short operand = convertor.convertOperand(values[1], constants, lineNum);
			if (convertor.getType(operand) != OperandType.CONSTANT)
				return;
			short value = convertor.extractValue(operand);
			findUnreachable(lines, memoryToLine.get((int) value) + 1);
			return;

		}
		if (opcode.matches("JMP(Z|L|G)")) {
			short operand = convertor.convertOperand(values[1], constants, lineNum);
			if (convertor.getType(operand) != OperandType.CONSTANT)
				return;
			short value = convertor.extractValue(operand);
			findUnreachable(lines, memoryToLine.get((int) value) + 1);
		}
		findUnreachable(lines, lineNum + 1);
	}

	private void writeToBinaryFile(Short[] bytecodes, File file) throws IOException {
		String filename = file.getName();
		int index = filename.indexOf(".");
		filename = filename.substring(0, index) + ".jb";
		File binaryFile = new File(file.getParentFile(), filename);
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
				cleanCode.put(i + 1, "");
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

		if (name.length() == 0)
			throw new SyntaxException(lineNum, "No name for the value '" + value + "' was found");

		if (!name.matches("[a-z]+"))
			throw new SyntaxException(lineNum, "Constant '" + name + "' needs to be in all lower case");

		if (!value.matches("(r|(m[+-]?))?[0-9]+"))
			throw new SyntaxException(lineNum, "Operand '" + value + "' needs to be a operand");

		if (constants.containsKey(name))
			throw new SyntaxException(lineNum, "Constant '" + name + "' declared twice");

		constants.put(name, new Constant(value));
		return "";
	}

	private String extractLineName(String line, int lineNum) throws SyntaxException {
		int index = line.indexOf(":");
		if (index != -1) {
			String lineName = line.substring(0, index);
			if (constants.containsKey(lineName))
				throw new SyntaxException(lineNum, "Constant '" + lineName + "' declared twice");

			if (!lineName.matches("[a-z]+"))
				throw new SyntaxException(lineNum, "Label '" + lineName + "' can only be lower case");

			constants.put(lineName, new Constant(String.valueOf(memloc)));
			line = line.substring(index + 1).trim();
			if (line.length() == 0)
				throw new SyntaxException(lineNum, "Label '" + lineName + "' points to no instruction");
		}
		String[] split = line.split(" ");
		memloc += split.length;
		memoryToLine.put(memloc, lineNum);
		unusedLines.put(lineNum, true);
		return line;
	}

	private List<Short> compileLine(String line, int lineNum) throws SyntaxException {
		if (line.length() == 0)
			return null;
		String[] split = line.split(" ");
		String opcode = split[0];
		InstructionParser params = opcodes.get(opcode);
		if (params == null)
			throw new SyntaxException(lineNum, "Instruction '" + opcode + "' not found");

		if (params.getParamCount() < split.length - 1)
			throw new SyntaxException(lineNum,
					"Instruction '" + opcode + "' does not accept " + (split.length - 1) + " operands");

		List<Short> instruction = new ArrayList<>();
		instruction.add(params.getOpcode());

		for (int i = 1; i < split.length; i++) {
			short operand = convertor.convertOperand(split[i], constants, lineNum);
			params.checkParam(i - 1, operand, lineNum, opcode);
			instruction.add(operand);
		}

		return instruction;
	}

	private String removeComments(String line) {
		int index = line.indexOf("#");
		if (index == -1)
			return line;
		return line.substring(0, index);
	}
}
