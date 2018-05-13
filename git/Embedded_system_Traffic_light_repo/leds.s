/*********************************************************************
 *
 *                  Example Assembly Project
 *
 *********************************************************************
 * File Name:      counter_up_led.S
 *
 * Processor:       PIC32MX
 * 
 * Assembler/Compiler/Linker:  MPLAB C32
 *
 * Company:         Microchip Technology, Inc.
 *
 * Software License Agreement
 *
 * 

 ********************************************************************/
#include <p32xxxx.h>

	/* define all global symbols here */
	.global main

	/* define which section (for example "text")
     * does this portion of code resides in. Typically,
     * all your code will reside in .text section as
     * shown below.
    */
	.text

	/* This is important for an assembly programmer. This
     * directive tells the assembler that don't optimize
     * the order of the instructions as well as don't insert
     * 'nop' instructions after jumps and branches.
    */
	.set noreorder      
/*********************************************************************
 * main()
 * This is where the PIC32 start-up code will jump to after initial
 * set-up.
 ********************************************************************/
.ent main /* directive that marks symbol 'main' as function in ELF
           * output
           */
main:
	 nop
        andi       $s0,$s0,0
        sw         $s0,TRISF
        sw         $s0,TRISE
        sw         $s0,TRISD 
        ori         $s0,$s0,0x4
        sw         $s0,PORTF
        li $3,0
      
/*	 endless loop */
endless:
     jal print
     nop
     addi $3,$3,1
     jal delay
     nop
    j    endless
      nop

.end main /* directive that marks end of 'main' function and registers
           * size in ELF output
           */
.ent delay
delay:
    
       andi $s1,$s1,0
       andi $s2,$s2,0
       ori $s2,$s2,0xffff
 x:    addi $s1,$s1,1
        bne $s1,$s2,x
       nop
/* return to caller */
jr    $ra
   nop
.end delay
.ent print
print:
        sw $3,PORTE 
        li    $2,0x10
        sw $2,PORTD
        li    $2,0x00
        sw $2,PORTD 
      	/* return to caller */
	jr	$ra
	nop
.end print

