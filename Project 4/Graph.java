import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class Graph 
{
	private Scanner fc; 
	private File graph; 
	private int nodeCount; 
	private int[][] nodeMatrix; 


	public Graph(File file, Scanner scanner)
	{
		graph = file; 
		fc = scanner; 

		nodeCount = fc.nextInt(); 
		nodeMatrix = new int[nodeCount][nodeCount];

		int edges = 0;
		for(int i = 0; i < nodeCount; i++)
		{
			fc.nextLine(); 
			edges = fc.nextInt();//first number at the start of the input line, telling the # of edges
			for(int j = 0; j < edges; j++)
			{
				int node = fc.nextInt(); //first number in the pair
				int edgeLength = fc.nextInt();//second number in the pair
				nodeMatrix[i][node] = edgeLength;

			}
		}
	}


	/**
	 * method that determines the connectivity of the graph
	 * @return
	 */
	public boolean isConnected(int choice)
	{
		boolean[] visitedNodes = breadthFirstTraversal(); //fills an array with a list of which nodes were visited

		for(int i = 0 ; i < visitedNodes.length ; i++)
			if(!visitedNodes[i])
			{
				if(choice == 1)
					System.out.println("Graph is not connected!");//loops through the whole array and if it finds that any node wasn't visited, prints false
				return false;
			}
		if(choice == 1)
			System.out.println("Graph is connected!");//if all the nodes were visited, print true
		return true; 		
	}

	/**
	 * method that performs a breadth first traversal of the graph and returns
	 * an array the shows which nodes were visited and which were not
	 * @return
	 */
	private boolean[] breadthFirstTraversal()
	{
		boolean[] visitedNodes = new boolean[nodeCount];  //boolean array
		Queue<Integer> queue = new LinkedList<Integer>(); //queue that keeps track of the nodes

		queue.add(nodeMatrix[0][0]); //add first node to queue
		visitedNodes[nodeMatrix[0][0]] = true; //set first node to visited

		while( !queue.isEmpty() )
		{
			int current = (queue.peek()).intValue();
			int child = -1; //set child = -1 to start so if something isn't connected we can tell

			for (int i = 0 ; i < nodeCount ; i++ )
				if ( nodeMatrix[current][i] > 0 )		//while the queue is not empty we go through the graph and if not visited and exists we set child = i
					if ( !visitedNodes[i] )
						child = i;

			if ( child != -1 ) //as long as child isn't -1 we set visited to true and add the node to the queue
			{
				visitedNodes[child] = true;
				queue.add(child);
			}
			else
				queue.remove();
		}
		return visitedNodes; //return array of what nodes were visited
	}	

	/**
	 * Performs a depth first traversal of the graph and returns an int array containing the path
	 * @param node
	 * @param matrix
	 * @param path
	 * @param index
	 * @return
	 */
	private int depthFirstTraversal(int node, int[][] matrix, int[] path, int index) 
	{
		path[index] = node; //puts the current node in the array

		for (int i = 0 ; i < nodeCount ; i++) 
			if ((matrix[node][i] != 0) && (!arrayContains(path, i))) 
				index = depthFirstTraversal(i, matrix, path, index + 1); //goes through and recursively does a DFT on the next node
		return index; //returns the array containing the path

	}

	/**
	 * Checks to see if an array contains a certain value
	 * @param array
	 * @param e
	 * @return
	 */
	private boolean arrayContains(int[] array, int e)
	{
		for(int i = 0 ; i < array.length ; i++)
			if(array[i] == e) //goes through the array and if it finds the value, returns true
				return true;
		return false; //otherwise returns false
	}

	/**
	 * Method that finds a minimum spanning tree from a graph following Prim's algorithm and outputs the mst 
	 * when finished
	 */

	public void MST()
	{
		boolean[] set = new boolean[nodeCount]; //If true in set S, if false in set V pertaining to Prim's algorithm
		set[0] = true; //First node in set S 
		int[][] mstMatrix = new int[nodeCount][nodeCount]; //a new matrix to hold our minimum spanning tree
		mstMatrix = minST(set, mstMatrix); 
		System.out.print("MST graph: ");
		standardPrint(mstMatrix);//Output the MST graph in standard format
	}


	private int[][] minST(boolean[] set, int[][] mst) // helper method
	{
		int full = 0; 
		for(int i = 0; i < set.length; i ++)
			if( set[i] == false )
				full++;

		if(full == 0) // base case, if Set S full, we are done
			return mst; 
		else 
		{
			int[] shortest = new int[3]; //holds the shortest distance[0], the node of that distance[1], and what it is connecting from[2]
			shortest[0] = Integer.MAX_VALUE; 
			for(int i = 0; i < set.length; i++)
			{
				if(set[i] == true) //if it is in set S, check for the shortest distance to all other nodes in V
				{
					for(int j = 0; j < nodeCount; j++ )
					{
						if(nodeMatrix[i][j] < shortest[0] && nodeMatrix[i][j] != 0 && set[j] == false)
						{
							shortest[0] = nodeMatrix[i][j]; //update the shortest distance
							shortest[1] = j; // update node
							shortest[2] = i; // update the back pointer

						}
					}

				}

			}
			set[shortest[1]] = true; // put the node in set S
			//set the corresponding values to their respective places in the new mst Matrix, remembering to do it both ways on account of the symmetry of adjacency matrices
			mst[shortest[2]][shortest[1]] = shortest[0]; 
			mst[shortest[1]][shortest[2]] = shortest[0]; 
			return minST(set, mst); // then recurse 

		}

	}
	/**
	 * Method that calculates and prints out the shortest path along with the total distance from one node to all others
	 * @param start
	 */
	public int[] shortestPath(int start, int choice)
	{
		int[] distances = new int[nodeCount]; 
		int[] prev = new int[nodeCount]; 			//declare variables
		boolean[] set = new boolean[nodeCount];
		int full = 0;

		for (int i = 0; i < distances.length; i++)
		{
			distances[i] = Integer.MAX_VALUE; 	//sets all unknown distances to max value
			prev[i] = -1;
		}

		set[start] = true;
		distances[start] = 0;  //sets the distance from the starting node to itself to 0 and puts the starting node in the final set.
		full++;

		while(full != nodeCount)	//while we haven't found the final distance to all the nodes
		{
			int edgeWeight = Integer.MAX_VALUE; 
			int backEdge = start; 
			int nodeU = 0; 

			for(int i = 0; i < nodeCount; i++)	// Find the node u in V that is closest to s
			{
				if(set[i])
				{
					int[] adjacentNodes = adjacentNodes(i); 
					for(int j : adjacentNodes) //go through all the adjacent nodes and find the closest one
					{
						if(!set[j] && nodeMatrix[i][j] < edgeWeight)
						{
							edgeWeight = nodeMatrix[i][j]; 
							backEdge = i; 
							nodeU = j;
						}
					}
				}
			}
			//once the closest is found, we put it in set S
			set[nodeU] = true; 
			distances[nodeU] = distances[backEdge] + nodeMatrix[nodeU][backEdge];  //set the distance so far to the accumulative distance of the whole path
			prev[nodeU] = backEdge; 

			int[] adjacentToV = adjacentNodes(nodeU);
			for(int v : adjacentToV) //go through all the nodes adjacent to the node closest to the start and if it's greater than the distance from start to u plus u to v...
			{						//...we set the closest known distance to v equal to the distance from start to u plus u to v
				if(set[v])
				{
					int temp = distances[v] + nodeMatrix[nodeU][v]; 
					if(temp < distances[nodeU])
					{
						distances[nodeU] = temp; 
						prev[nodeU] = v; 
					}
				}
			}
			full++; 
		}
		if(choice == 3)
			printShortestPath(distances,prev);

		return distances;
	}

	/**
	 * returns a boolean depending on if the graph is metric or not
	 * @return
	 */
	public boolean isMetric(int choice)
	{
		boolean completelyConnected = true; 
		for(int i = 0; i < nodeCount; i ++)
			for(int j = 0; j < nodeCount; j++)
			{
				if(i!=j)//There will be 0 edges between the node and itself
					if(nodeMatrix[i][j]==0)
						completelyConnected = false;
			}
		if(!completelyConnected) //if the graph is not completely connected, print out the error and return false
		{
			if(choice == 4)
				System.out.println("Graph is not metric: Graph is not completely connected.");
			return false;
		}
		else
		{
			if(triInequal()) //if it is completely connected, check to see if it follows the triangle inequality
			{	
				if(choice == 4)
					System.out.println("Graph is metric."); //if yes, print it out and return true
				return true;
			}

			if(choice == 4)
				System.out.println("Graph is not metric: Edges do not obey the triangle inequality."); //if not print out the error and return false
			return false;
		}

	}
	/**
	 * returns a boolean depending on if the graph follows the triangle inequality rule or not
	 * @return
	 */
	private boolean triInequal()
	{
		for(int i = 0 ; i < nodeCount ; i++)
		{
			int[] adjacentNodes = adjacentNodes(i); //goes through each node and creates an array of adjacent nodes

			for(int j = 0 ; j < adjacentNodes.length - 1 ; j++)
				if(nodeMatrix[i][adjacentNodes[j]] + nodeMatrix[i][adjacentNodes[j + 1]] < nodeMatrix[adjacentNodes[j]][adjacentNodes[j + 1]] //checks to see that each of those adjacent nodes...
						|| nodeMatrix[i][adjacentNodes[j + 1]] + nodeMatrix[adjacentNodes[j]][adjacentNodes[j + 1]] < nodeMatrix[i][adjacentNodes[j]] //...follow the triangle inequality rule
								||nodeMatrix[i][adjacentNodes[j]] + nodeMatrix[adjacentNodes[j]][adjacentNodes[j + 1]] < nodeMatrix[i][adjacentNodes[j + 1]])
					return false; //returns false if it does not
		}
		return true; //return true if is does.
	}

	/**
	 * if the graph is not metric, and as long as it is connected, we make it metric and print it out
	 */
	public void makeMetric()
	{
		if(isConnected(-1)) //checks to see if the graph is connected
		{
			int[][] metricMatrix = new int [nodeCount][nodeCount]; //makes a new adjacency matrix

			for(int i = 0 ; i < nodeCount ; i++) //finds all the shortest distances from one node to all the others and sets the direct edge to that distance
			{
				int[] distances = shortestPath(i, -1);

				for(int j = 0 ; j < nodeCount ; j++)
					metricMatrix[i][j] = distances[j];
			}
			standardPrint(metricMatrix); //prints out the new graph
		}
		else
			System.out.println("Error: Graph is not connected.");		//if it is not connected, prints error
	}
	/**
	 * Use brute force to try all possible tours starting at node 0. Print the length of the shortest one and the sequence of nodes it takes.
	 */
	public void travelingSalesmanProblem()
	{
		int[] bestPath = new int[nodeCount + 1];
		int[] path = new int[nodeCount + 1];
		int distance = 0;
		boolean[] visited = new boolean[nodeCount];

		if(!isConnected(-1)) //if it isn't connected, print out the error and return
		{
			System.out.println("Error: Graph is not connected.");
			return;
		}
		bestPath[bestPath.length - 1] = Integer.MAX_VALUE;
		path[path.length - 1] = 0;

		path[0] = 0;

		if(!testAllTours(path, 0, 0, visited, bestPath)) //if it has no tour, print out the error and return
			System.out.println("Error: Graph has no tour.");
		else
		{
			distance = bestPath[bestPath.length - 1]; //otherwise we print out the distance and path
			printPath(distance, bestPath, -1);
		}

	}
	/**
	 * recursive method that tries all tours
	 * @param path
	 * @param cur
	 * @param step
	 * @param visited
	 * @param bestPath
	 * @return
	 */
	private boolean testAllTours(int[] path, int cur, int step, boolean[] visited, int[] bestPath)
	{
		boolean tour = false;

		if(step == visited.length) //check to see if path is better than best path once we know we found a full tour
		{
			if(cur == 0 && path[path.length-1] < bestPath[bestPath.length -1])
			{
				for(int i = 0 ; i < nodeCount + 1 ; i++)
					bestPath[i] = path[i]; //if the distance of path is less than the distance of best path, update best path
				return true; //even if it's not better, return true if we found a tour
			}
			return false; //if we did't find a tour, return false
		}
		if(!visited[cur]) //if we haven't been to the node 'cur' yet, we get it's neighbors
		{
			visited[cur] = true;
			int[] adjacentNodes = adjacentNodes(cur);

			for(int i = 0 ; i < adjacentNodes.length ; i++)
			{
				if( !visited[adjacentNodes[i]] || (i == 0 && step == nodeCount - 1) ) //if we haven't visited a neighbor of cur (unless we are trying to return to 0)
				{
					path[step] = adjacentNodes[i]; //mark the step in the path to the neighbor
					path[path.length - 1] += nodeMatrix[cur][adjacentNodes[i]]; //add the distance from cur to the neighbor to the path distance
					tour = testAllTours(path, adjacentNodes[i], step + 1, visited, bestPath); //recurse with the neighbor as cur, and step + 1
				}
			}
		}
		return tour; //return tour so we know if we found a tour
	}

	/**
	 * Approximates the TSP and prints out the total distance and path
	 */
	public void approxTSP()
	{
		if(isMetric(-1))
		{
			boolean[] set = new boolean[nodeCount]; 
			set[0] = true; 
			int[][] mstMatrix = new int[nodeCount][nodeCount]; //finds the Minimum spanning tree of the graph
			mstMatrix = minST(set, mstMatrix); 

			for(int i = 0 ; i < nodeCount ; i++)
				for(int j = 0 ; j < nodeCount ; j++)	//multiplies everything by 2
					mstMatrix[i][j] *= 2;

			int[] path = new int[nodeCount + 1];
			depthFirstTraversal(0, mstMatrix, path, 0); //does a depth first traversal of the graph

			int distance = 0;

			for(int i = 0 ; i < path.length - 1 ; i++)
				distance += nodeMatrix[path[i]][path[i + 1]]; //adds up the distances for the path

			printPath(distance, path, 1); //prints the path
		}
		else
		{
			System.out.println("Error: Graph is not metric.");
		}
	}

	/**
	 * Prints out a path and the total distance
	 * @param distance
	 * @param path
	 * @param choice
	 */
	private void printPath(int distance, int[] path, int choice)
	{
		System.out.print(distance + ": "); //prints out the distance
		if(choice == 1) //prints out the path including the last index in the array
		{
			System.out.print(path[0] + " -> ");

			for(int i = 1 ; i < path.length; i++) 
			{
				System.out.print(path[i]);
				if(path[i] != path[0]) 
					System.out.print(" -> ");
			}
		}
		if(choice == -1)//prints out the path excluding the last index in the array
		{
			System.out.print(" 0 -> ");
			System.out.print(path[0] + " -> ");
			for(int i = 1 ; i < path.length - 1; i++) 
			{
				System.out.print(path[i]);
				if(path[i] != 0) 
					System.out.print(" -> ");
			}
		}
		System.out.println();
	}

	/**
	 * takes an adjacency matrix and if it needs to, prints it out in the correct format
	 * @param matrix
	 */
	private void standardPrint(int[][] matrix)
	{
		System.out.print("\n" + nodeCount); //output the node count first 

		Queue<Integer> index = new LinkedList<Integer>();
		Queue<Integer> weight = new LinkedList<Integer>(); //queues holding the various values we need to print out
		Queue<Integer> count = new LinkedList<Integer>();

		for( int i = 0; i < nodeCount; i ++) 
		{	
			int temp = 0;
			for(int j = 0; j < nodeCount; j++)
			{
				if(matrix[i][j] != 0 )
				{
					index.add(j);
					weight.add(matrix[i][j]);  //when it finds a location in the adj matrix that has info, it gets the info and stores it in the appropriate queue
					temp++;
				}	
			}
			count.add(temp);
		}

		for(int i = 0 ; i < nodeCount ; i++) //goes through the queues and prints out the info in the correct order
		{
			int num = count.remove();
			System.out.print("\n" + num);
			for(int j = 0 ; j < num ; j++)
				System.out.print(" " + index.remove() + " " + weight.remove());
		}
		System.out.println("\n");

	}
	/**
	 * Print the shortest path
	 * @param distances
	 * @param prev
	 */
	private void printShortestPath(int[] distances, int[] prev)
	{
		for(int i = 0; i < nodeCount; i++) 	//go through all the nodes and print their shortest paths
		{
			System.out.print(i + ": (");
			if(distances[i] == Integer.MAX_VALUE) //if the node is unreachable print out infinity
				System.out.print("Infinity)\t");
			else
			{
				System.out.print(distances[i] + ")\t");

				int cur = i;
				int[] temp = new int[nodeCount];
				for(int index = 0 ; index < temp.length ; index++)
					temp[index] = Integer.MAX_VALUE;
				int num = 0;
				int j = 1;
				temp[0] = cur;

				while(num!= -1) //while it hasn't reached the start node, add it's parent to the temp array
				{
					num = prev[temp[j-1]];

					if(prev[temp[j-1]] != -1)
					{
						temp[j] = num;
						j++;
					}
				}

				for(int k = 1; k <= temp.length; k++)
				{
					if(temp[temp.length - k] != Integer.MAX_VALUE)  //as long as you're at a valid node in the path, print it out
					{
						System.out.print(temp[temp.length - k]);
						if(temp[temp.length - k] != cur) //if there is another node to print, add an arrow
							System.out.print(" -> ");
					}
				}
			}
			System.out.println();
		}
	}

	/**
	 * Outputs the matrix in the correct adjacency format to view for testing purposes
	 */
	private void printAdjacency()
	{
		for(int i = 0; i < nodeCount; i++)  //goes through the matrix and prints each value out
		{
			for(int j = 0; j < nodeCount; j++)
				System.out.print(nodeMatrix[i][j] + " "); 

			System.out.println(); 
		}
	}
	/**
	 * return an array with all the nodes adjacent to the node you read in
	 * @param n
	 * @return
	 */
	private int[] adjacentNodes(int n)
	{
		int count = 0;
		for (int i = 0 ; i < nodeCount ; i++) //if there is an adjacent node, add one to count
		{
			if (nodeMatrix[n][i] != 0)
				count++;
		}

		int[] neighborsOfN = new int[count]; //array size of count
		int index = 0;
		for (int i = 0 ; i < nodeCount ; i++) 
			if (nodeMatrix[n][i] != 0)   //go through the nodes again and add the adjacent ones to the array
				neighborsOfN[index++] = i;

		return neighborsOfN; //return the array or adjacent nodes

	}

}



