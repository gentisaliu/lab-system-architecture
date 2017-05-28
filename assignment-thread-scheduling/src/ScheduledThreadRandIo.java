/*
 * Author: Raphael Kimmig
 */

import java.util.Random;

public class ScheduledThreadRandIo extends ScheduledThread1
{
	private double ioChance = 0;
	public int maxIo;
	public int innerLoop;
	public int io = 0;
	private Random r = new Random();

	ScheduledThreadRandIo(int max, int maxIo, double ioChance, int id, ThreadBuffer tb, int innerLoop) 
	{
		super(max, innerLoop, id, tb);
		this.ioChance = ioChance;
		this.innerLoop = innerLoop;
	}
	
	public void scheduleLoop() 
	{
		synchronized(tcb) 
		{
			while(tcb.mayRun == false) 
			{
				tcb.notify();
                try 
				{ 
					tcb.wait(); 
				}
                catch(Throwable e) 
				{}
				if(r.nextDouble() < ioChance) 
				{
					maxIo = 5;
					tcb.io = Math.abs(r.nextInt()) % this.maxIo;
					tcb.mayRun = false;
					try { 
						tcb.wait();
					} catch (Throwable e) {}

				}
			}
		}
	}

    public void scheduleExit() 
	{
        synchronized(tcb) 
		{
        	tcb.mayRun = false;
            tcb.done = true;
            tcb.notify();
        }
        System.out.println(this.getName()+": gracefully exited");
	}
}
