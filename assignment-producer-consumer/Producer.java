/*
 * Authors: Genti Saliu and Raphael Kimmig
 * Description: Producer Thread, puts data in a common buffer
 * June 2007
 */

class Producer extends Thread
{
	private Buffer buffer;	// reference to the common buffer
	private ThreadControlBlock tcb;	// individual thread control block
	private int cycles;		// production cycles
	private int id;		// identification number
	
	
	/*
	 * Constructor of Producer class
	 */
	public Producer(Buffer buffer, int id)
	{
		this.buffer = buffer;
		this.id = id;
	}
	
	// set thread control block
	public void setThreadControlBlock(ThreadControlBlock tcb)
	{
		this.tcb = tcb;
	}
	
	// get method
	
	public ThreadControlBlock getThreadControlBlock()
	{
		return tcb;
	}
	
	
	// returns producer id
	
	public int getID()
	{
		return id;
	}
	
	// return production cycles
	public int getCycles()
	{
		return cycles;
	}
	
	public void run()
	{
		cycles = tcb.getProducerCycles();		// get production cycles from the thread control block
		// run as long as the production cycles tell
		while(cycles > 0 && !tcb.getDone())
		{
			try
			{
				buffer.put(buffer.getCounter(), id);		// put product in the buffer
				tcb.setOccupied(true);		// mark thread activity
				sleep(tcb.getCycleLength());		// sleep
				tcb.setOccupied(false);		// mark thread inactivity
				cycles--;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		buffer.oneProducerLess();		// diminuishes number of producer threads
		tcb.setDone(true);
	}
}
