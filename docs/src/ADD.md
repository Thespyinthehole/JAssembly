# ADD

Adds the value in the second operand to the register from the first operand, storing the result in the register of the first operand.

### Format

* `ADD <register operand> <register or constant operand>`

### Examples

* `ADD r0 10` : Adds 10 to the value in register 0, storing the result in register 0
* `ADD r0 r1` : Adds the value of register 1 to the value in register 0, storing the result in register 0

### Flags set

* Negative - True if the result of the addition is negative
* Zero - True if the result of the addition is zero