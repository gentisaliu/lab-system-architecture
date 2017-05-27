/*
 * Author: Raphael Kimmig
 * Description: Interactive threads, experiment 3
 */

public class ScheduledThreadWithIo extends ScheduledThread1
{
	public TcbWithIo tcb;
	public int maxIo;
	public int id;
	public int io;
	public boolean stop;

	ScheduledThreadWithIo(int max, int maxIo, int id, ThreadBuffer tb) 
	{
		super(max, 1, id, tb);
		this.id = id;
		io = 0;
		this.maxIo = maxIo;
	}
	
	// sets TCB
	
	public void setTcb(TcbWithIo tcb)
	{
		this.tcb = tcb;
	}
	
	// checks whether the thread is still allowed to run and sets the thread to interactive
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
				tcb.io = this.maxIo;
			}
        }
	}
	
	// sets the thread's stop flag to true
	public void stopThread()
	{
		stop = true;
	}
	
	public void setIO()
	{
		io = 0;
	}
	
	// ends thread
	
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
