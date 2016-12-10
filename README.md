#MicroMips  

This case project, implements a simulator for a simplified MIPS64 processor.  

##Functionalities  
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

##Errors supported
* Invalid instruction
	* The opcode doesn't exist
	* Invalid syntax
* Invalid register
	* Missing registers
	* Too many registers
* Invalid label
	* Label not found
	* Label already exists
* Out of bounds
	* Jump address is out of bounds
	* Data address is out of bounds
	* Goto memory is out of bounds