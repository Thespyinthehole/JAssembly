# JMPZ

This instruction is a built off `JMP`, it will perform the `JMP` instruction if and only if both the Negative flag is false and Zero flag is true.

### Format

* `JMPZ <register or constant operand>`

### Examples

* `JMPZ r0` : Jumps to the memory location given by the value in register 0
* `JMPZ 10` : Jumps to the memory location 10
* `JMPZ label` : Jumps to the memory location given by the label

### Flags set

* Negative - False
* Zero - False