# CMP

This instruction compares 2 values and sets the flags based on the outcome. This is most commonly used before a `JMP` instruction, to set the condition of the jump. 

### Format

* `CMP <register operand> <register or constant operand>`

### Examples

* `CMP r0 10` : Compares the value stored in register 0 with the value 10
* `CMP r0 r1` : Compares the value stored in register 0 with the value stored in register 1

### Flags set

* Negative - True if the value of the first operand is less than the value of the second
* Zero - True if the value of the first operand is equal to the value of the second