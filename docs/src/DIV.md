# DIV

Divides the value in the second operand to the register from the first operand, storing the result in the register of the first operand. Will halt execution if a division by 0 occurs. This is an integer division.

### Format

* `DIV <register operand> <register or constant operand>`

### Examples

* `DIV r0 10` : Divides the value in register 0 by 10, storing the result in register 0
* `DIV r0 r1` : Divides the value of register 0 by the value in register 1, storing the result in register 0

### Flags set

* Negative - True if the result of the division is negative
* Zero - True if the result of the division is zero