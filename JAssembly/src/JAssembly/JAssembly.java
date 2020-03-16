package JAssembly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import JAssembly.Compiler.AssemblyCompiler;
import JAssembly.Interpreter.BinaryInterpreter;

public class JAssembly {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			printHelp();
			return;
		}

		String option = args[0];
		String ext = null;
		switch (option) {
		case "-h":
		case "-help":
			printHelp();
			return;
		case "-c":
			if (args.length == 1) {
				System.err.println("No file parameter found. Please refer to the -help option.");
				return;
			}
			ext = ".jasm";
			break;
		case "-r":
			if (args.length == 1) {
				System.err.println("No file parameter found. Please refer to the -help option.");
				return;
			}
			ext = ".jb";
			break;
		default:
			System.err.println("Option not found. Please refer to the -help option.");
			return;
		}
		File f = null;
		f = new File(args[1] + ext);
		if (!f.exists()) {
			System.err.println("No " + ext + " with that name exists in the location.");
			return;
		}
		new JAssembly(f, ext, args);
	}

	public JAssembly(File file, String ext, String[] args) throws IOException {
		String[] lines = readLines(file);
		if (ext.equals(".jasm")) {
			AssemblyCompiler compiler = new AssemblyCompiler();
			try {
				compiler.compile(lines, file);
			} catch (SyntaxException e) {
				System.out.println(e);
			}
		} else {
			BinaryInterpreter interpreter = new BinaryInterpreter();
			int registers = 8;
			int memory = 1024;

			if (args.length > 2) {
				if (args.length % 2 != 0 && args.length <= 6) {
					System.err.println("Invalid interpreter parameters. Please refer to the -help option.");
					return;
				}
				for (int i = 2; i < args.length; i += 2) {
					switch (args[i]) {
					case "-regs":
						try {
							registers = Integer.valueOf(args[i + 1]);
						} catch (NumberFormatException e) {
							System.err.println("The -regs parameter needs a integer value.");
							return;
						}
						break;
					case "-mem":
						try {
							memory = Integer.valueOf(args[i + 1]);
						} catch (NumberFormatException e) {
							System.err.println("The -mem parameter needs a integer value.");
							return;
						}
						break;
					default:
						System.err.println("Invalid interpreter parameters. Please refer to the -help option.");
						return;
					}
				}
			}

			try {
				interpreter.interpret(lines, memory, registers);
			} catch (InterpretException e) {
				System.out.println(e);
			}
		}
	}

	private String[] readLines(File file) throws IOException {
		return Files.readAllLines(file.toPath()).stream().toArray(String[]::new);
	}

	private static void printHelp() {
		System.out.println("This is the JAssembly function");
		System.out.println("It will either compile a .jasm file, or interpret a .jb file");
		System.out.println("\n Options:");
		System.out.println("	-h   : Prints the help menu");
		System.out.println("	-help: Prints the help menu");
		System.out.println("	-c   : Followed by a file to compile,   do not include the extension");
		System.out.println("	-r   : Followed by a file to interpret, do not include the extension");
		System.out.println("			-regs : Followed by an integer to change the register count. Default: 8");
		System.out.println("			-mem  : Followed by an integer to change the memory size.    Default: 1024");
	}
}
