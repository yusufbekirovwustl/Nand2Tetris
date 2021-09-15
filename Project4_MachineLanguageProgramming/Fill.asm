// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here

	@fillval
        M=0
	@8192
	D=A
	@SCREEN
	D=D+A
	@screenend
	M=D
    (CHECK)
	@KBD
	D=M
	@FILL
	D;JNE
	@fillval
	M=0
	@SETUP
	0;JMP
    (FILL)
	@fillval
	M=-1
    (SETUP)
	@SCREEN
	D=A
	@addr
	M=D
    (LOOP)
	@addr
	D=M
	@screenend
	D=D-M
	@END
	D;JEQ
	@fillval
	D=M
	@addr
	A=M
	M=D
	@addr
	M=M+1
	@LOOP
	0;JMP
    (END)
	@CHECK
	0;JMP