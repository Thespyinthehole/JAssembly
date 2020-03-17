# Constants

All constants will be equal to an `<operand>` value, which will be explained later on, and the constant needs to have a name. The name given needs to be all lower case and can only contain alphabetical characters. The format of a constant declaration is: `<name> = <operand>`, there can be as much or as little white space between the `=` and the `<name>` and `<operand>`. The names of the constants have to be unique, which means they can not be declared twice.

## Examples

### Declaration

* `result = r0`

* `x = 5`

* `storage = m0`

### Usage

* `LDR result 0`

* `ADD r0 x`

* `MOV storage 10`
