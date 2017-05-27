/*
 * Author: Genti Saliu
 * Description: Thread Control Block for the Producer/Consumer Threads
 * June 2007
 */

class ThreadControlBlock
{
	private Producer thread; // a reference to a Producer thread, to which this ThreadControlBlock might belong to
	private Consumer thread1; // a reference to a Producer thread, to which this ThreadControlBlock might belong to
	private int cycleLength; // the length of a single read/write cycle in milliseconds
	private int cycles; // number of cycles a producer thread should live before ending
	private boolean done; // flags whether the thread is done
	private boolean occupied;  // flags whether thread is active or inactive

	
	/*
	 * Constructor initializes the above variables
	 */
	public ThreadControlBlock(int cycleLength)
	{
		this.cycleLength = cycleLength;
		cycles = 0;
		thread = null;
		thread1 = null;
		done = false;
	}
	
	/*
	 * get and set Methods for the above attributes
	 */
	public void setOccupied(boolean occupied) 
	{
		this.occupied = occupied;
	}

	public boolean getOccupied() 
	{
		return this.occupied;
	}
	public void setProducerCycles(int cycles)
	{
		this.cycles = cycles;
	}
	
	public int getProducerCycles()
	{
		return cycles;
	}
	
	public void setThread(Producer thread)
	{
		this.thread = thread;
	}
	
	public void setThread(Consumer thread1)
	{
		this.thread1 = thread1;
	}
	
	public Thread getThread()
	{
		if(thread != null) return thread;
		else return thread1;
	}
	
	public boolean getDone()
	{
		return done;
	}
	
	public void setDone(boolean done)
	{
		this.done = done;
	}

	public void setCycleLength(int cycleLength) 
	{
		this.cycleLength = cycleLength;
	}
	
	public int getCycleLength()
	{
		return cycleLength;
	}
}
