// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

//this implementation uses a if checker to find lesser of the multiply statement to reduce iterations
//Pseudo-code:
//		i=0
//		sum=0
//		if R1<R0
//		    n=R1
//		    adder=R0
//		else
//		    n=R0
//		    adder=R1
//	    (LOOP)
//		if i=n, goto STOP
//		sum = sum + adder
//              i = i+1
//		goto LOOP
//	    (STOP)
//		goto STOP
//
// Put your code here.

   @R2
   M=0// initialize output to zero
   @i
   M=0 //i = 0
   @sum
   M=0 //sum = 0
   @R1
   D=M
   @R0
   D=D-M
   @R1GREAT
   D;JGE
   @R1
   D=M
   @n
   M=D //n=R1
   @R0
   D=M
   @adder
   M=D //adder = R0
   @LOOP
   0;JMP
(R1GREAT)
   @R0
   D=M
   @n
   M=D //n=R10
   @R1
   D=M
   @adder
   M=D //adder = R1
(LOOP)
   @i
   D=M
   @n
   D=D-M
   @STOP
   D;JEQ
   @sum
   D=M
   @adder
   D=D+M
   @sum
   M=D// sum =sum+adder
   @i
   M= M+1 // i=i+1
   @sum
   D=M
   @R2
   M=D // RAM[2] = sum
   @LOOP
   0;JMP
(STOP)
   @STOP
   0;JMP