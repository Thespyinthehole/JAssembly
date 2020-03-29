# MOV

Puts data into the a memory location. This location can be either a literal memory location, such as `m0`, or it can be a memory offset, such as `m+1`.

### Format

* `MOV <memory operand> <any operand>`

### Examples

* `MOV m0 r0` : Moves the value of register 0 into memory location 0
* `MOV m0 10` : Moves the value 10 into memory location 0
* `MOV m+1 m0` : Moves the value of memory location 0 into the memory offset of plus 1
* `MOV m+1 m+1` : Moves the value of memory offset plus 1 into memory offset of plus 1

### Flags set

* Negative - False
* Zero - False