# Memory Offset

The memory offset operand is similar to the memory operand in which is gets values from the memory. It can be declared by either `m+` or `m-` depending on which direction the offset should go in. 

The memory offset works by getting the current memory location related to where this operand is stored and then shifts it based on the number given. If `m+` is used, it is shifted to the right.If `m-` is used, it is shifted to the left.

## Examples

This is the memory locations. `*` indicates where the operand is located. `^` indicates which memory location to use.


* `MOV m+2 10`

| 0     | 1         | 2     | 3     | 4 | 5 | 6 |
| -     | -         | -     | -     | - | - | - |
| MOV   | m+2`*`    |  10   | `^`   |   |   |   |

* `LDR r0 m+3`

| 0     | 1         | 2         | 3  | 4 | 5    | 6 |
| -     | -         | -         | -  | - | -    | - |
| LDR   | r0        | m+3`*`    |    |   |  `^` |   |


* `LDR r0 m-1`

| 0     | 1             | 2         | 3  | 4 | 5 | 6 |
| -     | -             | -         | -  | - | - | - |
| LDR   | r0 `^`        | m-1`*`    |    |   |   |   |
