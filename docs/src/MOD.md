# MOD

Gets the modulo of the first register operand by the second operand, storing the result in the first register operand. `operand 1 % operand 2`.

### Format

* `MOD <register operand> <register or constant operand>`

### Examples

* `MOD r0 10` : Gets the modulo of r0 % 10, storing the result in register 0
* `MOD r0 r1` : Gets the modulo of r0 % r1, storing the result in register 0

### Flags set

* Negative - True if the result of the modulo is negative
* Zero - True if the result of the modulo is zero