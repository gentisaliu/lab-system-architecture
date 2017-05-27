/*
 * Author: Raphael Kimmig
 * Description: Scheduler for the Experiments 1, 2
 */

import java.util.LinkedList;

public class Scheduler extends Thread
{
	public static boolean done=false;	
	public ThreadBuffer tb;
	public LinkedList<Tcb> tcbList = new LinkedList<Tcb>();
	
	public Scheduler() 
	{
		this.setDaemon(true);
	}

	// creates a thread's control block and starts the thread
	
	public void registerThread(ScheduledThread t, int msecs) 
	{
		Tcb tcb = new Tcb();
		tcb.thread = t;
		tcb.time = msecs;
		tcbList.add(tcb);
		t.tcb = tcb;
		t.start();
	}
	
	// Implementation of Preemptive Round Robin Scheduling
	
	public void run() 
	{
		try 
		{ 
			Thread.sleep(5000); 
		}
		catch(Throwable e) 
		{}
		while(this.done == false) 
		{
			this.done = true;
			forloop: for(Tcb tcb : tcbList ) 
			{
				synchronized(tcb) 
				{
					
					if (tcb.done==true) continue forloop; // if thread is done calculating, ignore this thread
					tcb.mayRun = true; // thread is flagged to run
					tcb.notify(); // thread is notified
					
				}
				
				this.done = false;
				try 
				{ 
					Thread.sleep(tcb.time); // Scheduler sleeps while thread calculates
				}
				catch ( Throwable e ) 
				{}
				
				synchronized(tcb) 
				{
					if(tcb.mayRun == true) // if there was a thread running, set its mayrun flag to false
					{
						tcb.mayRun = false;
						try 
						{ 
							tcb.wait(); 
						}
						catch( Throwable e ) 
						{}
					}
				}
				
			}
		}
		System.out.println("Scheduler exited");
	}
}

