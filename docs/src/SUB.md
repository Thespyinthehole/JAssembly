# SUB

Subtracts the value in the second operand to the register from the first operand, storing the result in the register of the first operand.

### Format

* `SUB <register operand> <register or constant operand>`

### Examples

* `SUB r0 10` : Subtracts 10 to the value in register 0, storing the result in register 0
* `SUB r0 r1` : Subtracts the value of register 1 to the value in register 0, storing the result in register 0

### Flags set

* Negative - True if the result of the subtraction is negative
* Zero - True if the result of the subtraction is zero