# Structure

This section will describe the main overview of how to structure the assembly code. It will talk about where to put different things and some basic knowledge about the language

### Main Structure

The structure of the assembly code consists of two parts. The first part is the constant declaration, which is then followed by the main code base. This means that all your constant values need to be defined first, this is due to the way the language is compile in which is does two passes over the code. The first pass is to remove all comments and white spaces as well as replace all constants with their respective value, the second is then used to change the assembly code into byte code.

### Constant Declaration

All constants will be equal to an `<operand>` value, which will be explained later on, and the constant needs to have a name. The name given needs to be all lower case and can only contain alphabetical characters. The format of a constant declaration is: `<name> = <operand>`, there can be as much or as little white space between the `=` and the `<name>` and `<operand>`.

#### Examples

##### Declaration

* `result = r0`

* `x = 5`

* `storage = m0`

##### Usage

* `LDR result 0`

* `ADD r0 x`

* `MOV storage 10`


### Code base

The code base is made up of line seperated instructions, with each instruction being made up of an opcode followed by operands. The number of operands will be determined by the opcode, which will be explain later on. The opcode and operands need to be white space seperated, you can use as much or as little white space as needed. The format of an instruction is: `<opcode> <operand> <operand>`, where the operands are optional.

#### Examples

*  `MOV m0 10`

*  `ADD r0 5`

*  `HALT`

### Commenting

In the `.jasm` file, you are able to comment the code by using `#`. This will comment out everything on the current line after the symbol. Comments can also take up the whole line as well. These can be in any section of the file including both the constant declaration and main code sections.

#### Examples

* `MOV m0 10 #Moves 10 into memory location 0`


* `#This line does nothing`