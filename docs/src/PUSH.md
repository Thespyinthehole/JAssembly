# PUSH

Push is a very interesting function, as it acts very weirdly with it's second operand. Like `MOV` and `LDR` stores data, however it can put the data in both registers and memory locations. However, with the second operand, it will put the literal value of that operand into the storage location. For example, if you use `r0` as a second operand, it will not store the value location in register 0, instead it will store the value that points to register 0, which would be `1100 0000 0000 0000`.


### Format

* `PUSH <any storage operand> <any operand>`

### Examples

* `PUSH r0 r0` : Moves the pointer for register 0 into register 0
* `PUSH m0 m0` : Moves the pointer for memory location 0 into memory location 0
* `PUSH m+1 m0` : Moves the pointer for memory offset of plus 0 into memory location 0
* `PUSH r0 10` : Moves the value 10 into register 0

### Flags set

* Negative - False
* Zero - False