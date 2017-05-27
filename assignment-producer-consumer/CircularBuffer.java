/*
 * Author: Genti Saliu
 * June 2007
 * Description: Circular Buffer
 */

public class CircularBuffer 
{
	private int bufferSize; // the fixed buffer size
	private int readPointer; // the next to be read position
	private int writePointer; // the next to be written position
	private StorageRep sr;
	private int[] buffer;
	
	// Constructor: gets the buffer size and creates an empty buffer
	public CircularBuffer(int bufferSize)
	{
		this.bufferSize = bufferSize;
		readPointer = 0;
		writePointer = 0;
		buffer = new int[bufferSize];
		for(int i = 0; i < bufferSize; i++) buffer[i] = 0;
	}
	
	// This method writes the circular buffer
	public synchronized void writeBuffer(int number)
	{
		buffer[writePointer % bufferSize] = number;
		if(sr != null) sr.repaint();
		writePointer++;
	}
	
	// This method reads the circular buffer in the position which readPointer tells
	public synchronized int readBuffer()
	{		
		int content = buffer[readPointer % bufferSize];
		buffer[readPointer % bufferSize] = 0;
		if(sr != null) sr.repaint();
		readPointer++;
		return content;
	}
	
	public void setStorageRep(StorageRep sr)
	{
		this.sr = sr;
	}
	
	// This method checks whether all elements of the circular buffer contain the value 0
	public boolean isEmpty()
	{
		return (writePointer == readPointer);
	}
	
	// returns true if buffer is full
	public boolean isFull()
	{
		return (writePointer - readPointer == bufferSize);
	}
	
	// returns the number of full slots
	public int getNumberOfFullSlots()
	{
		return (writePointer - readPointer);
	}
	
	// returns the buffer size
	public int getBufferSize()
	{
		return bufferSize;
	}
	
	// returns the readPointer
	public int getReadPointer()
	{
		return readPointer;
	}
	
	// returns the writePointer	
	public int getWritePointer()
	{
		return writePointer;
	}
}
