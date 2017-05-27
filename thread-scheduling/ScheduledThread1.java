/*
 * Author: Raphael Kimmig
 * Description: User threads of experiments 1, 2, 3 and 4
 */

public class ScheduledThread1 extends ScheduledThread 
{
	public int innerLoop;
	public int i;
	public int max;
	public int io;
	public int id;
	public ThreadBuffer tb;
	public boolean stop;
	
	public ScheduledThread1(int max, int innerLoop1, int id, ThreadBuffer tb) 
	{
		this.max = max;
		this.innerLoop = innerLoop1;
		this.id = id;
		this.tb = tb;
		stop = false;
	}

	public void setTcb(TcbWithIo tcb)
	{
		this.tcb = tcb;
	}
	
	public void run() 
	{
		for(i = 1; i <= max && !stop; i++)   // outer loop 
		{
			this.scheduleLoop();
			for(int j = 1; j < this.innerLoop; j++) work(); // inner loop
			tb.setBuffer(id, i);
		}
		scheduleExit();
	}
	
	// sets thread's stop flag to true
	public void stopThread()
	{
		stop = true;
	}
	
	// returns this thread's identification number
	
	public int getThreadId()
	{
		return id;
	}
	
	// returns this thread's inner loop
	public int getInnerLoop()
	{
		return innerLoop;
	}
	
	// returns this thread's progress
	public int getProgress()
	{
		return i;
	}
	
	
	// returns this thread's outer loop
	public int getMax()
	{
		return max;
	}
	
	// calculates the biggest prime number from 2 to 200
	public void work()
	{
		int max = 2;
		boolean prime;
		for(int c = 3; c < 200; c++)
		{
			prime = true;
			for(int l = 2; l < c; l++)
			{
				if(c%l==0) prime = false;
			}
			if(!prime) max = c;
		}
	}
}

