public class Operator extends Term
{
	public Operator(char currentChar, int precedence) {
		this.operator = currentChar;
		prec = precedence;
	}
	
	public String toString()
	{
		return operator + "";
	}
	
	
}
