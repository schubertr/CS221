
import java.util.LinkedList;
import java.util.List;

/*
 *Authors: Matt Lane, Ryan Schubert
 *WordTree class includes methods that add words to the tree, rotates the nodes
 *for balancing, finds the height, finds the misspelled words in a document, 
 *and gives suggestions for the misspelled words.  
 */

public class WordTree
{

	private static class WordNode
	{
		public String word;
		public int count; //how many times the word shows up in the doc
		public int height;
		public WordNode left;
		public WordNode right;
	}

	WordNode root = null;

	public void add(String word)
	{
		root = addWord(word, root);
		//WordTree.printTree(root);
	}

	private static WordNode addWord(String word, WordNode node )
	{//if the node is equal to null, create a new node and get its height.
		if(node == null)
		{

			WordNode start = new WordNode();
			start.word = word;
			start.height = setHeight(start);
			start.count++;
			return start;
		}
		/*
		 * compares the alphabetical order of each word, and puts the word to the left or right 
		 * depending on its value.
		 */
		int num = word.compareTo(node.word);

		if(num == 0)
		{
			node.count++;
			return node;
		}
		if(num < 0)
		{
			node.left = addWord(word, node.left);
		}

		else if(num > 0)
		{
			node.right = addWord(word, node.right);
		}

		node = balance(node);//balances the node with the balance method
		node.height = setHeight(node);//sets the height
		return node;
	}
	// this method takes the node that is currently being balanced and rotates to the left. 
	private static WordNode rotateLeft(WordNode node)
	{
		WordNode temp = node.left;
		node.left = temp.right;
		temp.right = node;
		node.height = Math.max(node.left.height,node.right.height) + 1;
		temp.height = Math.max(temp.left.height, node.height) + 1;
		return temp;
	}
	// this method takes the node that is currently being balanced and rotates to the right. 
	private static WordNode rotateRight(WordNode node)
	{
		WordNode temp = node.right;
		temp.right = node.left;
		node.left = temp;
		temp.height = Math.max(temp.left.height, temp.right.height) + 1;
		node.height = Math.max(node.right.height, temp.height) + 1;
		return node;
	}
	//in a left right situation of the tree, this method balances the node correctly. 
	private static WordNode rotateLeftRight(WordNode node)
	{
		node.left = rotateRight( node.left );
		return rotateLeft( node );
	}
	//in a right left situation of the tree, this method balances the node correctly.
	private static WordNode rotateRightLeft(WordNode node)
	{
		node.right = rotateLeft(node.right);
		return rotateRight(node);
	}
	//takes all of the different rotate methods and performs them, based on given situations in the tree.
	private static WordNode balance( WordNode node )
	{
		int bValue = bValue(node);
		if(bValue == -2)
		{
			//rotates the node to the right if needed.
			if(node.left.left.height >= node.left.right.height)
			{
				node = rotateRight(node);
			}
			//rotates the node in a left right situation if needed.
			else
				node = rotateLeftRight(node);
		}
		else if( bValue == 2)
		{
			//rotates the node to the left if needed
			if(node.right.right.height >= node.right.left.height)
			{
				node = rotateLeft(node);
			}
			//rotates node in a right left situation if needed. 
			else
				node = rotateRightLeft(node);
		}
		return node; //should we return?
	}
//finds the balance value inside of the tree for each node height.
	public static int bValue(WordNode node)
	{
		if(node.right == null && node.left == null)
			return 0;
		if(node.left == null && node.right != null)
			return 1;
		if(node.right == null && node.left != null)
			return -1;
		return node.right.height - node.left.height;
	}
//sets the height where the current node is based on the children nodes. 
	public static int setHeight(WordNode node)
	{
		if(node == null)
		{
			return -1;
		}
		
		if(node.left == null && node.right == null)
		{
			return 0;
		}
		else if(node.right == null)
			return 1+ setHeight(node.right);

		else if(node.left == null)
			return 1+ setHeight(node.left);

		else
		{
			if(setHeight(node.left) > setHeight(node.right))
			{
				return 1 + setHeight(node.right);
			}
			else
				return 1 + setHeight(node.left);
		}
	}
//recursive method for finding the words inside of the tree.
	private boolean find(String word)
	{
		return search(root, word);
	}
	
	public void findUnmatched(List<String> words, WordTree dictionary)
	{
		findingUnmatched(root, words, dictionary);
	}
	//method to find the unmatched words inside of the tree.
	public void findingUnmatched(WordNode node, List<String> words, WordTree dictionary)
	{
		if(node != null)
		{
			
			if(node.word != null)
			{
				findingUnmatched(node.left, words, dictionary);
				
				if(!dictionary.find(node.word))
					words.add(node.word);


				findingUnmatched(node.right, words, dictionary);

			}
		}
	}
	//this method makes suggestions for the misspelled words that were found inside of the document. 
	public String makeSuggestion(String word)
	{
		List<String> listWords = new LinkedList<String>();
		wordsToList(root, listWords);
		if(listWords.size() > 0)
		{
			//uses the lev distance to find the smallest word
			int smallest = SpellChecker.levDist(word, listWords.get(0));
			int num;
			int index = 0;
			for(int i = 1 ; i < listWords.size() ; i++)
			{
				num = SpellChecker.levDist(word, listWords.get(i));
				//if the num is smaller than the smallest, it sets smallest to num. 
				if(num < smallest)
				{
					smallest = num;
					index = i;
				}
				else if(num == smallest)
				{
					int min = listWords.get(index).compareTo(listWords.get(i));
					if(min > 0)
						index = i;
				}

			}
			return listWords.get(index);
		}
		return " ";
	}
//searches through each word inside of the tree. 
	public boolean search(WordNode node, String word)
	{
		if (node == null)
			return false;

		int num = word.compareTo(node.word);

		if(num == 0)
			return true;
		if(num < 0)
			return search(node.left, word);
		else
			return search(node.right, word);

	}
	//string method that prints the contents of the wordtree with strings and includes the count of the words in the document.
	public String toString()
	{
		return inOrder(root);
	}
	//helps print the words in order
	public String inOrder(WordNode node)
	{
		if (node == null)
			return "";

		String words = "";

		words += inOrder(node.left);
		words += node.word + " " + node.count + "\n" + "#";
		words += inOrder(node.right);
		//Maybe .toString()

		return words;
	}
	//adds the words to the list. 
	public void wordsToList(WordNode node, List<String> words)
	{
		if(node != null)
		{
			if(node.word != null)
			{
				wordsToList(node.left, words);

				words.add(node.word);

				wordsToList(node.right, words);

			}
		}
	}
	//gets the size for the counter
	public int getSize()
	{
		return counter(root);
	}
	//cpunts the amount of times a word is used inside of a document. 
	public int counter(WordNode node)
	{
		if (node == null)
			return 0;

		return 1 + counter(node.left) + counter(node.right);
	}

}