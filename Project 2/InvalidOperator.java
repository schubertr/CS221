public class InvalidOperator extends Exception
{
	public InvalidOperator()
	{
	}
}
/*2.Invalid Operator: If the next token of input should be an operator, but the symbol you find (ignoring spaces, of course) is an invalid operator character, print Invalid operator:  and the invalid character and exit the program.
if(currentChar != supported operator)
   ("Invalid operator: " + currentChar);
   */