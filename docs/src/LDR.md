# LDR

Puts data into the a register. 

### Format

* `LDR <register operand> <any operand>`

### Examples

* `LDR r1 r0` : Moves the value of register 0 into register 1
* `LDR r1 10` : Moves the value 10 into register 1
* `LDR r1 m0` : Moves the value of memory location 0 into register 1
* `LDR r1 m+1` : Moves the value of memory offset plus 1 into register 1

### Flags set

* Negative - False
* Zero - False