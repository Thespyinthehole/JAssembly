# DEC

Decrements the value in a register by 1, setting the flags to the correct values.

### Format

* `DEC <register>`

### Examples

* `INC r0` : Decrements the value in register 0 by 1

### Flags set

* Negative - True if the result of the decrement is negative
* Zero - True if the result of the decrement is zero