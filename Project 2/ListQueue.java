public class ListQueue<T> 
{

	private class Node 
	{
		public T data;
		public Node next;
	}
	
	private Node head = null;
	private Node tail = null;
	private int size = 0;
/**
 * returns true if the queue is empty
 * @return
 */
	public boolean isEmpty() 
	{
		return head == null;
	}
/**
 * adds a T onto the end of the queue
 * @param x
 */
	public void enqueue(T x) 
	{
		Node temp = new Node();
		temp.data = x;
		if(isEmpty())
		{
			head = temp;
			tail = temp;
		}
		else
		{
			tail.next = temp;
			tail = temp;
		}
		size++;
	}
/**
 * removes the first thing in the queue
 */
	public void dequeue() 
	{
		if(size == 0)
		{
			head = null;
			tail = null;
		}
		else
			head = head.next;
		size--;

	}
/**
 * returns that data in the head of the ListQueue
 * @return
 */
	public T front() 
	{
		if( isEmpty() )
			return null;
		return head.data;
	}
	/**
	 * returns the size of the queue
	 * @return
	 */
	public int size()
	{
		return size;
	}
	/**
	 * prints out the contents of the queue
	 */
	public void print()
	{
		Node current = head;
		while(current != null)
		{
			System.out.print(current.data + " ");
			current = current.next;
		}
		System.out.println();
	}
	/**
	 * returns the data in the tail
	 * @return
	 */
	public T back()
	{
		if( isEmpty() )
			return null;
		return tail.data;
	}
}