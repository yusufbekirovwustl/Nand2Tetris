# Nand2Tetris
Work done as part of the Nand2Teris courses in Coursera, done in Summer 2020. This course challenges students to 
work up to developing components that are associated with a "modern computer". The computing platform, called Hack, is
used alongside a high level language called Jack. The student will have to go from designing hardware components all the 
way to implementing a compiler and operating system for the computing platform. In this course, the instructors are the 
computer architects giving design specifications while the student implements those specifications.

Work is divided into the following projects:

### Project 1: Boolean Logic
  From a primitive NAND gate, a generic hardware description language was used to create simple logic elements
  like NOT, AND, OR, XOR, multiplexors and demultiplexors. 
  
### Project 2: Boolean Arithmetic
  From the logic circuits developed in Project 1, I design design half-adders, full-adder, and all the way up to 
  a 16-bit general purpose ALU.
  
###  Project 3: Memory
  From a Flip-Flop primitive, incremental digital design was used to create a 16K register 16-bit Random Access 
  and also a 16-bit Program Counter.
  
### Project 4: Machine Language Programming
  The Hack assembly language is used to write two programs:
    1. Mult.asm - program where a simple register-register multiplication is done, with optimization for reduction in cycle 
       counts by determining the minimum value used in multiplication.
    2. Fill.asm - program where a keyboard I/O input is used to draw a frame buffer completely with white or black pixels.
  
### Project 5: Computer Architecture
  The design of the assembly language is used to create a hardware platform capable of running instructions that satisfy legal 
  Hack assembly commands. A CPU couples with a memory hiarachy is made and joined to create a "computer" that can use instructions
  loaded from memory using a program counter to execute correctly.
  
### Project 6: The Assembler
  An Hack assembly language to Hack machine language Assembler is created. The assembler provides support
  for two instruction types and also includes symbol resolution features. THe assembler takes input to a Hack .asm files and creates
  a .hack file and also checks for exceptions and incorrect file semantics.The Assembler was written in the Java language.
  
### Project 7: Virtual Machine 
  A VMTranslator for a stack based virtual machine languge to Hack assembly is created. Concepts of push/pop, virtual memory segments, 
  arithmetic and logical stack operations, and call/return functionality is implemented in Hack assembly. Main parts of the program are
  seperated into a parser for the VM file commands, and then a CodeWriter for the Assembly commands. The VMTranslator was written in the 
  Java language.
  
### Project 8: Compiler
  Jack, a object-orientated Java-like language is presented for conversion to the Virtual machine language. The compiler created first
  tokenizes the textual input of the Jack language to identify components of the Jack language as defined by a specification. These tokens
  are used in a ComilationEngine to convert tokens to a XML format. This XML file is parsed to make a symbol table and then use that to write
  to a .vm file that can later be translated and assembled for the Hack platform. The compiler generates correct VM language code, but does not
  seek to optimize code written in the Jack language. The compiler was written in the Java language.
  
### Project 9: Operating System
  A simple Operating System is built to provide a abstraction layer between the hardware and the software. Functionality for
  Memory operations, Math, Hardware like Screen and Keyboard, types like Array and String, and a sys program to do OS initialization
  and call main is all implemented. The Operating System is written in the JACK language.
