# FUNC

Performs a jump instruction, but also stores the next instruction in a stack which will be used by the `RET` instruction.

### Format

* `FUNC <register or constant operand>`

### Examples

* `FUNC r0` : Jumps to the memory location given by the value in register 0
* `FUNC 10` : Jumps to the memory location 10
* `FUNC label` : Jumps to the memory location given by the label

### Flags set

* Negative - False
* Zero - False