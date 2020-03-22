# Labels

Labels are a specific type of constant, in which they are differently defined. At the start of each line in the code block, you can set a label for that line by putting a constant name, all lower case alphabetical characters, followed by `:`. This will set up a new constant value which points to the memory location of the start of that line. This is mainly used with the jump command, however you can still use it as a regular constant which is a literal value. Labels can not have the same name as regular constants as they still have the base constant rules applied to them. A label will link to the next instruction in the program.

## Examples

#### Declaration

* `start: LDR r0 0`
* `end: HALT` 
* `loop: ADD r0 10`

#### Usage

* `JMP start`
* `LDR r0 end`
* `JMP loop`