# MUL

Multiplies the value in the second operand to the register from the first operand, storing the result in the register of the first operand.

### Format

* `MUL <register operand> <register or constant operand>`

### Examples

* `MUL r0 10` : Multiplies 10 by the value in register 0, storing the result in register 0
* `MUL r0 r1` : Multiplies the value of register 1 by the value in register 0, storing the result in register 0

### Flags set

* Negative - True if the result of the multiplication is negative
* Zero - True if the result of the multiplication is zero