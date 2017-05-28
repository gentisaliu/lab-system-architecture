/*
 * Author: Raphael Kimmig
 * Description: Computational usrethread
 */

public class ScheduledThread extends Thread 
{
	public Tcb tcb; // thread control block
	
	
	// this method checks whether the thread is still allowed to run
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
	        }
	    }
    }

    // this method ends the thread
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

    public void run() 
	{
        scheduleLoop();
		scheduleExit();
    }
}
                                              
