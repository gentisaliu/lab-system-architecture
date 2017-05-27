/*
 * Authors: Genti Saliu and Raphael Kimmig
 * Description: Consumer Thread, reads from a common buffer
 * June 2007
 */

class Consumer extends Thread
{
	private Buffer buffer;	// reference to the common buffer
	private ThreadControlBlock tcb;	// respective thread control block
	private int id;	// identification number
	private int consumedProduct;
	
	
	/*
	 * Consumer Constructor
	 */
	public Consumer(Buffer buffer, int id)
	{
		this.buffer = buffer;
		this.id = id;
		consumedProduct = 0;
	}
	
	// set method
	
	public void setThreadControlBlock(ThreadControlBlock tcb)
	{
		this.tcb = tcb;
	}
	
	// get method
	
	public ThreadControlBlock getThreadControlBlock()
	{
		return tcb;
	}
	
	// returns consumer id
	
	public int getID()
	{
		return id;
	}
	
	public int getConsumedProduct()
	{
		return consumedProduct;
	}
	
	public void run()
	{
		// run as long as there are producers alive and contents in the buffer
		while((!buffer.producersDone() || !buffer.isCircularBufferEmpty()) && !tcb.getDone())
		{
			try
			{
				consumedProduct = buffer.get(id);		// take product
				tcb.setOccupied(true);		// mark thread activity
				sleep(tcb.getCycleLength());	// sleep
				tcb.setOccupied(false);		// mark thread inactivity
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		tcb.setDone(true);
	}
}
