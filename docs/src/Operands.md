# Operands

Operands can be of 4 different types. Each instruction can only accept certain types of operands. The first 2 bits of the operand byte code determines what type of operand it is. The following 14 bits is the value of the operand in two's complement.

| Operand Type  | Byte code             |
| ------------- |:--------------------: | 
| Constant      | `00xx xxxx xxxx xxxx` |  
| Memory        | `01xx xxxx xxxx xxxx` | 
| Memory Offset | `10xx xxxx xxxx xxxx` |   
| Register      | `11xx xxxx xxxx xxxx` |