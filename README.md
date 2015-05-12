# MiMaSimu
A Simulator for the minimal machine (taught at the kit in Karlsruhe, Germany), with a graphical UI.

## General instructions
The internal memory can be edited by clicking the edit memory button.
Memory images with the extension ".mima" or ".mem" can be load by use of the "load memory" button or by passing the file as command line argument.

double slashes "//" can be used to add command lines to memory files.

commands as well as values can be stored at a specific address by just typing the address before the value:
```
0x00002 42 //value 42 at address 0x2
```

If no address is given the value is stored at the address of the value before incremented by one
```
0x00002 42 // value at address 0x2
43 // value at address 0x3
44 // value at address 0x4
```
addresses as well as data and instructions can ether be written in hex (0x2A), decimal (42) or dual (0b101010).

if no start address is given, the program starts at address 0x00000.

the start address can be explicitly defined as:

```
start 0x100 // program starts at 0x100
```

or can be given as annotation:
```
0x100 0x100042 ;START
0x100 0x100042 //START
0x100 0x100042 start
```
it doesn't matter if lower or upper case is used as well as spaces or tabulators.

The memory address 0x04242 is mapped to the consoles output.

## Architecture of the MIMA (minimal machine)

### Registers

* Acc (Akku): Accumulator
* X: first ALU operand
* Y: second ALU operand
* Z: ALU result
* One (Eins): constant 1
* IAR: instruction address register
* IR: instruction register
* SAR: memory address register
* SDR: memory data register

### ALU Operations

c_2c_1c_0	| Operation
:--------------:|:---------
000		| do nothing (Z --> Z)
001		| X + > --> Z
010		| rotate X to the right --> Z
011		| X AND Y --> Z
100		| X OR Y --> Z
101		| X XOR Y --> Z
110		| one's complement of X --> Z
111		| Z <-- (X == Y)?-1:0

### Instruction set

#### Instruction format

* OpCode < F:  [4 Bit OpCode][20 Bit Address or constant]
* OpCode >= F: [8 Bit OpCode][0x0000]

#### Op-Codes

OpCode 	| mnemonik	| Description
:------:|:--------------|:-----------
0	| LDC c		| c --> Acc
1	| LDV a		| <a> --> Acc
2	| STV a		| Acc --> <a>
3	| ADD a		| Acc + <a> --> Acc
4	| AND a		| Acc AND <a> --> Acc
5	| OR a		| Acc OR <a> --> Acc
6	| XOR a		| Acc XOR <a> --> Acc
7	| EQL a		| if(Acc == <a>){-1 --> Acc} else {0 --> Acc}
8	| JMP a		| Jump to address a
9	| JMN a		| Jump to address a if acc < 0
A	| LDIV a	| <<a>> --> Acc
B	| STIV a	| Acc --> <<a>>
C	| JMS a		| jump subroutine (see below)
D	| JIND a	| jump indirect (see below)
E	|		| free
F0	| HALT		| stops the minimal machine
F1	| NOT		| one's complement(Acc) --> Acc
F2	| RAR		| rotates Acc on the the right --> Acc
F3 - FF	|		| free

##### Notes
* Bits shifted out right within a __RAR__ command will be pushed in at the left.
* The instruction __JMS target__ saves the address of the succeeding instruction (return address) to the address given by target and initiates a jump to target + 1.
* __JIND target__ initiates a jump to the address which is stored at the target address. (Jmp <target>)
