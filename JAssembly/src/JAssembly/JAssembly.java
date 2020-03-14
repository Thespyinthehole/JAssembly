package JAssembly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import JAssembly.Compiler.AssemblyCompiler;
import JAssembly.Interpreter.BinaryInterpreter;

public class JAssembly {
	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("Please add a filename to parameters");

		String filepath = args[0];
		File f = new File(filepath);
		filepath = f.getName();
		int index = filepath.indexOf(".");
		if (index == -1)
			throw new Exception("Needs to be extension .jasm or .jb");

		String ext = filepath.substring(index);
		if (!(ext.equals(".jasm") || ext.equals(".jb")))
			throw new Exception("Needs to be extension .jasm or .jb");

		new JAssembly(f, ext);
	}

	public JAssembly(File file, String ext) throws SyntaxException, IOException, InterpretException {
		String[] lines = readLines(file);
		String name = file.getName();
		name = name.substring(0, name.indexOf("."));
		if (ext.equals(".jasm")) {
			AssemblyCompiler compiler = new AssemblyCompiler();
			compiler.compile(lines, file);
		} else {
			BinaryInterpreter interpreter = new BinaryInterpreter();
			interpreter.interpret(lines);
		}
	}

	String[] readLines(File file) throws IOException {
		return Files.readAllLines(file.toPath()).stream().toArray(String[]::new);
	}
}
