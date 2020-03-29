# JMP

This instruction sets the `pc` to the given memory location. This means the program flow will jump to another instruction, rather than moving to the next one. 

### Format

* `JMP <register or constant operand>`

### Examples

* `JMP r0` : Jumps to the memory location given by the value in register 0
* `JMP 10` : Jumps to the memory location 10
* `JMP label` : Jumps to the memory location given by the label

### Flags set

* Negative - False
* Zero - False