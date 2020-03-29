# JMPG

This instruction is a built off `JMP`, it will perform the `JMP` instruction if and only if both the Negative and Zero flags are false.

### Format

* `JMPG <register or constant operand>`

### Examples

* `JMPG r0` : Jumps to the memory location given by the value in register 0
* `JMPG 10` : Jumps to the memory location 10
* `JMPG label` : Jumps to the memory location given by the label

### Flags set

* Negative - False
* Zero - False