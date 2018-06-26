# MicroMips

This case project implements a simulator for a simplified MIPS64 processor.

## Functionalities
* R-type instructions
	* XOR
	* DSUBU
	* SLT
	* NOP
* I-type instructions
	* BEQC
	* LD
	* SD
	* DADDIU
* J-type instruction
	* BC

## Errors supported
* Invalid instruction
	* The opcode doesn't exist
* Invalid register
	* Missing registers
	* Too many registers
	* Reg>31
* Invalid label
	* Label not found
	* Label already exists
	* Invalid label syntax
* Memory errors (Run time errors)
	* Not aligned to 8 bytes
	* Out of bounds
		* Jump address is out of bounds
		* Data address is out of bounds
		* Goto memory is out of bounds