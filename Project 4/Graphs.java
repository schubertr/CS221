import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 * Ryan Schubert & Josh Hughes
 * Project 4: Graphic Violence
 * CS221B
 * Dr. Wittman
 */
public class Graphs 
{

	public static void main(String[] args)
	{
		System.out.print("Enter the file name: ");
		Scanner in = new Scanner(System.in);
		String fileName = in.next(); //reads in the file
		File file = new File(fileName);
		Scanner fc;
		Graph graph = null;

		try 
		{
			fc = new Scanner(file);
			graph = new Graph(file, fc); //creates the graph using the file
		} 

		catch (FileNotFoundException e) 
		{
			System.out.print("File not Found!"); //prints out error if file is not found
			e.printStackTrace();
		} 

		boolean menu = true; 
		int choice = 0;
		while(menu) //switch statement that calls the appropriate method based on the user input
		{
			System.out.println("");
			System.out.print("1. Is Connected\n2. Minimum Spanning Tree \n3. Shortest Path\n4. Is Metric\n5. Make Metric\n6. Traveling Salesman Problem\n7. Approximate TSP\n8. Quit\n\nMake your choice (1-8): ");
			choice = in.nextInt();
			System.out.println();
			switch(choice)
			{
			case 1: graph.isConnected(choice);
			break;
			case 2: graph.MST(); 
			break;
			case 3: System.out.println("From which node would you like to find the shortest paths (0 - 5): ");
			int node = in.nextInt(); 
			graph.shortestPath(node, choice);
			break;
			case 4: graph.isMetric(choice);
			break;
			case 5: graph.makeMetric();
			break;
			case 6: graph.travelingSalesmanProblem();
			break;
			case 7: graph.approxTSP();
			break;
			case 8: menu = false;
			break;
			}

		}

	}

}
