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
		case "-d":
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
		try {
			if (ext.equals(".jasm"))
				new AssemblyCompiler().compile(lines, file);
			else
				interpret(lines, args);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void interpret(String[] lines, String[] args) throws Exception {
		int registers = 8;
		int memory = 64;

		if (args.length % 2 != 0 && args.length <= 6)
			throw new Exception("Invalid interpreter parameters. Please refer to the -help option.");

		for (int i = 2; i < args.length; i += 2) {
			switch (args[i]) {
			case "-regs":
				try {
					registers = Integer.valueOf(args[i + 1]);
					if (registers < 0 || registers > 256)
						throw new Exception("The -regs parameter needs to be between 0 and 256.");

				} catch (NumberFormatException e) {
					throw new Exception("The -regs parameter needs a integer value.");
				}
				break;
			case "-mem":
				try {
					memory = Integer.valueOf(args[i + 1]);
					if (memory < 0 || memory > 16384)
						throw new Exception("The -mem parameter needs to be between 0 and 16384.");

				} catch (NumberFormatException e) {
					throw new Exception("The -mem parameter needs a integer value.");
				}
				break;
			default:
				throw new Exception("Invalid interpreter parameters. Please refer to the -help option.");
			}
		}

		new BinaryInterpreter().interpret(lines, memory, registers, args[0].equals("-d"));
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
		System.out.println("			-mem  : Followed by an integer to change the memory size.    Default: 64");
	}
}
