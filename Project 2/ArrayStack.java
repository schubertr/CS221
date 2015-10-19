public class ArrayStack<T> 
{
	private int start; 
	private T[] data;
	private int size;
	
	public ArrayStack() 
	{
		start = 0;
		size = 0;
		data = (T[]) new Object[10];
	}

	/**
	 * returns true if the stack is full
	 * @return
	 */
	public boolean isFull() 
	{
		return size == data.length;
	}
/**
 * returns true if the stack is empty
 * @return
 */
	public boolean isEmpty() 
	{
		return size == 0;
	}
/**
 * adds a T to the stack. If the stack is full it makes the stack larger
 * @param t
 */
	public void push(T t) {

		if (isFull())
		{
			T[] newData = (T[]) new Object[data.length*2];
			
			for(int i = 0 ; i < data.length ; i++)
				newData[i] = data[i]; 
			data = newData;
		}
			data[size] = t;
		size++;
	}
/**
 * removes the top T on the stack
 * @return
 */
	public T pop() 
	{
		if (isEmpty()) 
			return null;
		size--;
		return data[size];
	}
/**
 * returns the size of the stack
 * @return
 */
	public int size()
	{
		return size;
	}
	/**
	 * returns the data in top T in the stack
	 * @return
	 */
	public T peek()
	{
		return data[size-1];
	}
	/**
	 * returns the data in the stack of the index entered
	 * @param index
	 * @return
	 */
	public T get(int index)
	{
		return data[index];
	}
}