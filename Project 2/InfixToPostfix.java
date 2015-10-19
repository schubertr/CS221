import java.util.Scanner;


public class InfixToPostfix 
{
	public static String error ="";
	/**
	 * Main Method that takes in the input and passes it to InfixToPostfix
	 * @param args
	 * @throws UnbalancedRightParenthesis
	 * @throws InvalidOperator
	 * @throws UnbalancedLeftParenthesis
	 * @throws InvalidOperand
	 * @throws MissingOperand
	 */
	public static void main(String[] args) throws UnbalancedRightParenthesis, InvalidOperator, UnbalancedLeftParenthesis, InvalidOperand, MissingOperand
	{
		System.out.print("Enter infix expression:  ");
		Scanner in = new Scanner (System.in);
		String printedInfix = "";												//main method that calls InfixToPostfix
		printedInfix = in.nextLine();
		InfixToPostfix(printedInfix);
	}

	/**
	 * Takes in the user input and converts it to postfix, also evaluates the postfix expression
	 * @param infix
	 * @throws UnbalancedRightParenthesis
	 * @throws InvalidOperator
	 * @throws UnbalancedLeftParenthesis
	 * @throws InvalidOperand
	 * @throws MissingOperand
	 */
	public static void InfixToPostfix(String infix) throws UnbalancedRightParenthesis, InvalidOperator, UnbalancedLeftParenthesis, InvalidOperand, MissingOperand
	{
		ListQueue<Term> input = new ListQueue();
		ListQueue<Term> output = new ListQueue();			//creating the input queue, output queue, operator stack, and the operand stack.
		ArrayStack<Term> operators = new ArrayStack();
		ArrayStack<Term> operands = new ArrayStack();

		boolean opAnd = true;		//boolean that is true when we are looking for an operand
		String numIn = "";	//string that holds the double values
		int rightParenCount = 0, leftParenCount = 0;		//int values that keep track of the parenthesis

		for (int i = 0; i < infix.length(); i++)		//loops through the infix
		{
			char currentChar = infix.charAt(i);	//current char is the character we are currently looking at 

			if (currentChar != ' ') //will not try to parse anything if we are looking at a space
			{
				if(opAnd)  //will not look for an operand if this is false
				{
					if(currentChar == '(')
					{
						input.enqueue(new Operator(currentChar, Prec(currentChar)));	//if looking for operand and we find a '(' adds it to the input queue as an operator. also sets it's precendence
						opAnd = true;
						leftParenCount++; //ups the left parenthesis count by 1 
					}
					else 
					{ 
						int j = 0 ;
						if(infix.charAt(i) == '+' && opAnd)  //if looking for a operand and we find a plus sign continue reading in the number below and store it as a positive number
						{
							numIn += infix.charAt(i);
							i++;
							for(; j < infix.length()-i && opAnd ; j++) 
							{
								if( Character.isDigit(infix.charAt(i+j)) )
								{
									numIn += infix.charAt(i+j);
								}
								else
								{
									if(infix.charAt(i+j) == '.' && opAnd)
									{
										numIn += infix.charAt(i+j);
									}

									else
										opAnd = false;
								}

							}
						}
						else if(infix.charAt(i) == '-' && opAnd) //if looking for a operand and we find a minus sign continue reading in the number below and store it as a negative number
						{
							numIn += infix.charAt(i);
							i++;
							for(; j < infix.length()-i && opAnd ; j++)
							{
								if( Character.isDigit(infix.charAt(i+j)) )
								{
									numIn += infix.charAt(i+j);
								}
								else
								{
									if(infix.charAt(i+j) == '.' && opAnd)
									{
										numIn += infix.charAt(i+j);
									}

									else
										opAnd = false;
								}

							}
						}
						else if(Character.isDigit(infix.charAt(i))) //if there are no positive or negative signs, just read in the number
						{
							for(; j < infix.length()-i && opAnd ; j++)
							{
								if( Character.isDigit(infix.charAt(i+j)) )
								{
									numIn += infix.charAt(i+j);
								}
								else
								{
									if(infix.charAt(i+j) == '.' && opAnd)
									{
										numIn += infix.charAt(i+j);
									}

									else
										opAnd = false;
								}

							}
						}
						else
						{
							System.out.println("Invalid operand");		//if we get to this point, it's an invalid operand so we throw an exception
							throw new InvalidOperand();
						}
						input.enqueue(new Operand(Double.parseDouble(numIn)));
						i+=numIn.length()-1;
						numIn = "";
						opAnd = false;
					}

				}
				//if the current character is not an operand, we come down here and sort it out
				else if ((currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '(' || currentChar == ')'))
				{
					if(currentChar == ')')
					{
						input.enqueue(new Operator(currentChar, Prec(currentChar)));
						opAnd = false;
						rightParenCount++;
					}
					else
					{
						input.enqueue(new Operator(currentChar, Prec(currentChar)));
						opAnd = true;
					}
				}
				else
				{
					System.out.println("Invalid operator: " + currentChar); //if it gets to here it is an invalid operator and we thrown an exception
					throw new InvalidOperator();
				}


			}
		}
		if(rightParenCount < leftParenCount)
		{ 
			System.out.println("Unbalanced left parenthesis '('");  
			throw new UnbalancedLeftParenthesis();
		}
		else if(rightParenCount > leftParenCount)
		{
			System.out.println("Unbalanced right parenthesis ')'");		//here we make sure the parenthesis are balanced and throw an exception if they are not

			throw new UnbalancedRightParenthesis();
		}
		else if(input.back() instanceof Operator)
		{
			if(input.back().operator != ')')
			{
				System.out.println("Missing operand");	//here we make sure there are a right amount of operands and if there are not we throw an exception
				throw new MissingOperand();
			}
		}
		else
		{
			System.out.print("Standardized infix:  ");		//now we print out the standard infix
			input.print();
		}

		//----------------------Now we start the conversion-----------------------------------------------------------------

		int qsize = input.size();

		for(int i = 0 ; i < qsize ; i++)
		{
			if(input.front() instanceof Operand )
			{
				output.enqueue(input.front());		//if it's an operand add it to the output queue.
				input.dequeue();
			}
			else if(input.front().operator == '(')
			{
				operators.push(input.front()); // if it's a left parenthesis push it onto the operator stack.
				input.dequeue();
			}
			else if(input.front().operator == ')')
			{

				while(!operators.isEmpty() && (operators.peek().operator != '('))
				{
					output.enqueue(operators.pop());		//if it's a right parenthesis pop off everything from the operator stack and add each operator to the output queue, until you hit a left parenthesis. Then pop off the left parenthesis.
				}
				operators.pop();
				input.dequeue();
			}
			else 
			{
				if(operators.isEmpty())		//If the stack is empty, push the new operator onto the stack. 
				{
					operators.push(input.front());
					input.dequeue();
				}
				else //. If the stack is not empty, compare the precedence of the current operator with the precedence of the operator on the top of the stack and handle accordingly
				{
					if(input.front().prec <= operators.peek().prec)
					{
						while(!operators.isEmpty() && (operators.peek().prec > input.front().prec))
						{
							output.enqueue(operators.pop());
						}
					}
					operators.push(input.front()); 
					input.dequeue();
				}
			}

		}

		while(!operators.isEmpty()) //After all the input has been processed, pop whatever is left in the stack and add it to the output queue. 
		{
			output.enqueue(operators.pop());
		}

		System.out.print("Postfix expression:  ");  //Then print the contents of the output queue.
		output.print();

		//-------------------------Evaluating the Postfix expression------------------------------------------

		while(!output.isEmpty()) //Process the postfix expression term by term.
		{
			if(output.front() instanceof Operand)
			{
				operands.push(output.front());  //If the term is an operand, push it onto the stack.
				output.dequeue();
			}
			else if(output.front() instanceof Operator)
			{
				char op = output.front().operator; //If the term is an operator, pop two operands off the stack and apply the operator to them. 

				double first =  operands.pop().operand;
				double second = operands.pop().operand;
				double num = 0;

				switch (op)
				{
				case '+': num = first + second; break;
				case '-': num = second - first; break;
				case '*': num = first * second; break;
				case '/': num = second / first; break; 
				}

				operands.push(new Operand(num)); //Then, push the result back onto the stack.
				output.dequeue();
			}
		}

		System.out.println("Answer: " + operands.peek()); //The final answer should be the only thing left on the stack.
	}
	/**
	 * method that determines the precedence of each operator
	 * @param op
	 * @return
	 */
	public static int Prec(char op)
	{
		if(op == '(')
			return 1;
		if(op == '+' || op == '-')
			return 2;
		if(op == '*' || op == '/')
			return 3;
		else
			return 0;
	}
}
