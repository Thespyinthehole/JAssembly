# Opcodes

A list of instructions that is accepted in the language. After each instruction, some flags are set. These flags are shown in the description of the instruction.
 
|OPCODE| Integer Value | Binary Value |
|-|-|-|
|HALT | 0  | 0000 0000 0000 0000 |
|MOV  | 1  | 0000 0000 0000 0001 |
|LDR  | 2  | 0000 0000 0000 0010 |
|PUSH | 3  | 0000 0000 0000 0011 |
|CMP  | 8  | 0000 0000 0000 1000 |
|JMP  | 9  | 0000 0000 0000 1001 |
|JMPZ | 10 | 0000 0000 0000 1010 |
|JMPL | 11 | 0000 0000 0000 1011 |
|JMPG | 12 | 0000 0000 0000 1100 |
|FUNC | 13 | 0000 0000 0000 1101 |
|RET  | 14 | 0000 0000 0000 1110 |
|PRT  | 16 | 0000 0000 0001 0000 |
|INP  | 17 | 0000 0000 0001 0001 |
|INC  | 32 | 0000 0000 0010 0000 |
|DEC  | 33 | 0000 0000 0010 0001 |
|ADD  | 34 | 0000 0000 0010 0010 |
|SUB  | 35 | 0000 0000 0010 0011 |
|MUL  | 36 | 0000 0000 0010 0100 |
|DIV  | 37 | 0000 0000 0010 0101 |
|MOD  | 38 | 0000 0000 0010 0110 |