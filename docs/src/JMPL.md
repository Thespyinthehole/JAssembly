# JMPL

This instruction is a built off `JMP`, it will perform the `JMP` instruction if and only if both the Negative flag is true and Zero flag is false.

### Format

* `JMPL <register or constant operand>`

### Examples

* `JMPL r0` : Jumps to the memory location given by the value in register 0
* `JMPL 10` : Jumps to the memory location 10
* `JMPL label` : Jumps to the memory location given by the label

### Flags set

* Negative - False
* Zero - False