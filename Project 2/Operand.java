public class Operand extends Term
{
	public Operand(double num) {
		this.operand = num;
	}
	
	public String toString()
	{
		return operand + "";
	}
}
