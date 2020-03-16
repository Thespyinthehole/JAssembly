# Overview

This project consists of both an assembly code compiler and a byte code interpreter. As this uses both, this means it is possible to write in assembly code and byte code. Due to this, this documentation will contain information about both the assembly code and byte code. 

The assembly code and byte code this project uses is of my design and therefore will be explained throughout this book. 

The byte code interpreter uses a system in which is loads the program into shared memory, similar to the [Von Neumann Architecture](https://en.wikipedia.org/wiki/Von_Neumann_architecture), which means that during the code execution the program can be modified. This means that you are able to make [self-modifying code](https://en.wikipedia.org/wiki/Self-modifying_code), however I would only recommend this if you feel confident with the language and how the interpreter works, which I will explain later on.

The byte code uses memory cells of 16 bits, however the format of the operands results in the size being 14 bits. As this uses two's complement, the max range will be from -2<sup>13</sup> to 2<sup>13</sup>-1.

### Setting up a project

To make an assembly file, you will need make a file with the extension `.jasm`. 

To make a binary file, you need to make a file with the extension `.jb`. However if you compile the assembly code, file is automatically created.

### Compiling a file

After downloading the jar file and creating your file, if you run the command `java -jar JAssembly.jar -c <filename>`, where file name is the name of your file excluding the extension, this will compile the assembly to byte code. The result will be in a file name `<filename>.jb`, replacing the current file, or making a new one if needed.

For example, to compile a program called `Program.jasm`:
    
    java -jar JAssembly.jar -c Program

### Running a binary file

This is similar to compiling a file except you use the command `java -jar JAssembly.jar -r <filename>`. 

For example, to run a program called `Program.jb`:
   
    java -jar JAssembly.jar -r Program

This will run the byte code interpreter on the file `Program.jb` using the default memory size of 1024 and the default register count of 8. 

If you want to change the memory size, you need to add the option `-mem` after the command.

For example, to run a program called `Program.jb` with memory size of 64:
    
    java -jar JAssembly.jar -r Program -mem 64

There is a similar process to changing register count, but instead using the `-regs` option.

For example, to run a program called `Program.jb` with register count of 10:

    java -jar JAssembly.jar -r Program -regs 10

You are able to change both the register count and memory size by using both options. They can be used in any order.

For example, to run a program called `Program.jb` with register count of 10 and memory size of 64:
    
    java -jar JAssembly.jar -r Program -mem 64 -regs 10

This can also be done by:

    java -jar JAssembly.jar -r Program -regs 10 -mem 64

### Prerequisites

This project uses Java and therefore you will require at least Java 8, as this project uses some Java 8 features.

### Tested Java Versions

* 1.8.0_241

If you use a Java version that is not listed and it works, I would appreciate you letting me know so I can add it to the list.