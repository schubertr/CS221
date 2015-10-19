import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class SpellChecker
{

	public static void main(String[] args) throws FileNotFoundException
	{

		Scanner in = new Scanner(System.in);

		System.out.print("Enter a document to spellcheck: ");
		String fName = in.next();

		System.out.print("Enter a dictionary to use: ");	//asks the user for all the information needed
		String dName = in.next();
		
		System.out.print("Enter the number of threads to use: ");
		int totalThreads = in.nextInt();
		System.out.println();
		
		int threadNum = 0;
		
		long start = System.nanoTime();
		
		WordTree[] document = parse(fName); //stores the document into an array of wordtrees
		
		double total1 = (double)(System.nanoTime() - start)/100000000.0;
		System.out.print("Importing the document took ");
		System.out.format("%.3f", total1);		//prints out the time taken to read in and store the document
		System.out.print(" seconds." + "\n");
		
		start = System.nanoTime();
		
		WordTree[] dictionary = parse(dName);  //stores the document into an array of wordtrees
		
		double total2 = (double)(System.nanoTime() - start)/100000000.0;
		System.out.print("Importing the dictionary took ");
		System.out.format("%.3f", total2);    //prints out the time taken to read in and store the dictionary
		System.out.print(" seconds." + "\n");

		start = System.nanoTime();
		
		List<String> mistakes = new LinkedList<String>(); //new list that will store the misspelled words
		
		SpellCheckThread [] thread;
		String[] suggestions, finalSuggestions; //two string arrays for the different suggestions

		for(int i = 0 ; i <  document.length ; i++)
			document[i].findUnmatched(mistakes, dictionary[i]);  //goes through the document finding the misspelled words

		double total3 = (double)(System.nanoTime() - start)/100000000.0;
		System.out.print("Finding misspelled words took ");
		System.out.format("%.3f", total3);		//prints out the time taken to find the misspelled words
		System.out.print(" seconds." + "\n");
		
		start = System.nanoTime();
		
		suggestions = new String[mistakes.size()];
		for(int i = 0 ; i < mistakes.size() ; i++) //this big loop handles the multi threaded makeSuggestions method
		{
			if(threadNum + totalThreads < mistakes.size())
			{
				thread = new SpellCheckThread[totalThreads];
				for(int j = 0; j < totalThreads; j++, threadNum++) 
				{
					thread[j] = new SpellCheckThread(dictionary, mistakes, i, suggestions);
					thread[j].run(); //starts the threads
				}
				for(int j = 0; j < totalThreads; j++)
				{
					try {
						thread[j].join(); //joins threads
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else
			{
				thread = new SpellCheckThread[mistakes.size() % totalThreads];
				for(int j = 0; j < mistakes.size() % totalThreads; j++, threadNum++)
				{
					thread[j] = new SpellCheckThread(dictionary, mistakes, i, suggestions);
					thread[j].run(); //starts threads
				}
				for(int j = 0; j < mistakes.size() % totalThreads; j++)
				{
					try {
						thread[j].join(); //joins the threads
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		finalSuggestions = new String[suggestions.length]; //stores the best of the suggestions into the string array

		double total4 = (double)(System.nanoTime() - start)/100000000.0;
		System.out.print("Suggesting spelling corrections took "); //prints out the time taken to make suggestions
		System.out.format("%.3f", total4);
		System.out.print(" seconds.");
		
		for(int i = 0 ; i < suggestions.length ; i++)
			finalSuggestions[i] = mistakes.get(i) + " - " + suggestions[i];
		
		outputWords("words.txt", document); //outputs the words file
		
		outputSuggestions("suggestions.txt" , suggestions , mistakes); //outputs the suggestions file
	}

	/**
	 * Prints out the words file
	 * @param fileName
	 * @param document
	 */
	public static void outputWords(String fileName, WordTree[] document)
	{
		try {

			//create an print writer for writing to a file
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			for(int i = 0 ; i < document.length ; i++)
			{
				String line = document[i].toString();
				String[] pieces = line.split("#");
				for(int j = 0 ; j < pieces.length ; j++)
				{
					String var = pieces[j];
					if (var != "")
						out.println(var);
				}
			}
			out.close();
		}
		catch(IOException e1) {
			System.out.println("ERROR");
		}

	}
	/**
	 * prints out the suggestions file
	 * @param fileName
	 * @param words
	 * @param mistakes
	 */
	public static void outputSuggestions(String fileName, String[] words, List<String> mistakes)
	{
		try {

			//create an print writer for writing to a file
			PrintWriter out = new PrintWriter(new FileWriter(fileName));
			for(int i = 0 ; i < words.length ; i++)
			{
				out.println(mistakes.get(i) + " - " + words[i]);
			}
			out.close();
		}
		catch(IOException e1) {
			System.out.println("ERROR");
		}

	}
	/**
	 * reads in the file, parses it, and adds it to a word tree
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static WordTree[] parse(String fileName) throws FileNotFoundException
	{
		WordTree[] input = new WordTree[26]; //WordTree array

		for(int i = 0 ; i < input.length ; i++)
			input[i] = new WordTree(); 

		File file = new File(fileName);
		Scanner in = new Scanner(file); 
		String line = "";

		while(in.hasNext()) //until we hit the end of the file, we go through and parse each line
		{
			line = in.next(); 
			line = line.toLowerCase(); //convert all to lower case

			for(int i = 0 ; i < line.length() ; )
			{
				while(i < line.length() && !isLetter(line.charAt(i))) //if it isnt a letter
					i++;

				int startIndex = i;

				while(i < line.length() && isValid(line.charAt(i))) //if it is a letter or "-" or "\'"
					i++;

				int endIndex = i;

				String word = line.substring(startIndex, endIndex); //creates a temp string with the given index

				word = cleanUp(word); //cleans up the word

				if(word.length() != 0)
				{
					input[word.charAt(0) - 'a'].add(word); //adds word to WordTree
				}

			}

		};
		return input;

	}
	/**
	 * checks if character is valid
	 * @param c
	 * @return
	 */
	public static boolean isValid(char c)
	{
		return isLetter(c) || c == '\'' || c =='-';
	}
	/**
	 * checks if it is a / or a -
	 * @param c
	 * @return
	 */
	public static boolean isProper(char c)
	{
		return c == '\'' || c =='-';
	}
	/**
	 * checks to see if the character is a letter
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c)
	{
		return c >= 'a' && c <= 'z';
	}
	/**
	 * cleans up the word
	 * @param word
	 * @return
	 */
	public static String cleanUp(String word)
	{
		while(word.length() > 0 && isProper(word.charAt(0))) 
			word = word.substring(1);
		//gets rid of anything that shouldn't be there
		while(word.length() > 0 && isProper(word.charAt(word.length() - 1)))
			word = word.substring(0, word.length() - 1);
		
		return word; //returns the parsed word
	}
	/**
	 * reads in two strings and returns the lev distance between them
	 * @param docWord
	 * @param dicWord
	 * @return
	 */
	public static int levDist(String docWord, String dicWord)
	{
		int docNum = docWord.length();
		int dicNum = dicWord.length();
		int num1; int num2; int num3;
		int[][] levTable = new int[docNum + 1][dicNum + 1];//creates the table
		for(int i = 0 ; i < docNum + 1 ; i++)
		{
			for(int j = 0 ; j < dicNum + 1 ; j++)
			{
				if( i == 0)
					levTable[i][j] = j;	//we go through the table setting the values to each index to the appropriate number depending on which conditions are met
				else if( j == 0 )
					levTable[i][j] = i;
				else{
					num1 = levTable[i-1][j] + 1;
					num2 = levTable[i][j-1] + 1;
					num3 = levTable[i-1][j-1];
					if( dicWord.charAt(j-1) != docWord.charAt(i-1))
						num3++;
					levTable[i][j] = Math.min(num1 , Math.min(num2, num3));	//sets the index equal to the smallest of the three values
				}
			}
		}
		return levTable[docNum][dicNum]; //return the actual lev distance
	}

}