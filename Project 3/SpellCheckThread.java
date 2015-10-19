import java.util.List;

public class SpellCheckThread extends Thread {

	private WordTree[] dictionary;
	private String bestSuggestion, suggestion, mistake;
	private int distance, bestDistance;
	private String[] suggestions;
	private int mistakeNumber;

	public SpellCheckThread ( WordTree[] dictionary, List<String> mistakes, int mistakeNumber, String[] suggestions )
	{
		this.dictionary = dictionary;
		this.mistake = mistakes.get(mistakeNumber);
		this.mistakeNumber = mistakeNumber;
		this.suggestions = suggestions;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * what each thread is doing
	 */
	public void run()
	{
		bestDistance = 0; //variables that keep track of the best suggestion it finds
		bestSuggestion = "";
		for(int j = 0 ; j < dictionary.length ; j++)
		{
			if(j == 0 && dictionary[j] != null) //sets the first suggestion equal to the best suggestion
			{
				suggestion = dictionary[j].makeSuggestion(mistake);
				bestSuggestion = suggestion;
				bestDistance = levDist(bestSuggestion, mistake);
			}
			if(dictionary[j] != null) //goes through the dictionary, determines a suggestion, and then checks to see if it is the new best suggestion
			{
				suggestion = dictionary[j].makeSuggestion(mistake);
				distance = levDist(suggestion, mistake);
				if(distance < bestDistance)
				{
					bestDistance = distance;
					bestSuggestion = suggestion;
				}
			}
		}
		suggestions[mistakeNumber] = bestSuggestion; //puts all the best suggestions into an array that correlates with the number of mistakes
	}


	/**
	 * reads in two strings and returns the lev distance between them
	 * @param docWord
	 * @param dicWord
	 * @return
	 */
	private int levDist(String docWord, String dicWord)
	{
		int docNum = docWord.length();
		int dicNum = dicWord.length();
		int num1; int num2; int num3;
		int[][] levTable = new int[docNum + 1][dicNum + 1]; //creates the table
		for(int i = 0 ; i < docNum + 1 ; i++)
		{
			for(int j = 0 ; j < dicNum + 1 ; j++)
			{
				if( i == 0)
					levTable[i][j] = j; //we go through the table setting the values to each index to the appropriate number depending on which conditions are met
				else if( j == 0 )
					levTable[i][j] = i;
				else{
					num1 = levTable[i-1][j] + 1;
					num2 = levTable[i][j-1] + 1;
					num3 = levTable[i-1][j-1];
					if( dicWord.charAt(j-1) != docWord.charAt(i-1))
						num3++;
					levTable[i][j] = Math.min(num1 , Math.min(num2, num3)); //sets the index equal to the smallest of the three values
				}
			}
		}
		return levTable[docNum][dicNum]; //return the actual lev distance
	}

}