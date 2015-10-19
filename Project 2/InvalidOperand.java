public class InvalidOperand extends Exception
{
	public InvalidOperand()
	{
		
	}
}
/*2.Invalid Operator: If the next token of input should be an operator, but the symbol you find (ignoring spaces, of course) is an invalid operator character, print Invalid operator:  and the invalid character and exit the program.
if(currentChar != supported operator)
   ("Invalid operator: " + currentChar);
   */
/*	1.Invalid Operand: If the next token of input should be an operand, but it is not a properly formatted float literal, print Invalid operand and exit the program.
if(currentChar != double && looking for an operand)
   System.out.println("Invalid operand");
   */