/*
 * Author: Raphael Kimmig
 * Description: Scheduler for Experiment 3
 */

import java.util.LinkedList;

public class Scheduler3 extends Scheduler 
{
 	public LinkedList<TcbWithIo> tcbList = new LinkedList<TcbWithIo>();
	public int quantum = 10;
	public JProgressBarUpdater jpbu;
	public int experimentNumber;
	
	// set system quantum
	
	public void setQuantum(int quantum) 
	{
		this.quantum = quantum;
	}
	
	public void setProgressBarUpdater(JProgressBarUpdater jpbu, int a)
	{
		this.experimentNumber = a;
		this.jpbu = jpbu;
	}

	// register computational threads, generate TCB, start thread
	
	public void registerThread(ScheduledThread t, int slices) {
		TcbWithIo tcb = new TcbWithIo();
		tcb.thread = t;
		tcb.time = slices;
		((ScheduledThread1) t).setTcb(tcb);
		tcbList.add(tcb);
		t.start();
	}
	
	// register interactive threads, generate TCB, start thread

	public void registerThread(ScheduledThreadRandIo t, int slices) {
		TcbWithIo tcb = new TcbWithIo();
		tcb.thread = t;
		tcb.time = slices;
		tcbList.add(tcb);
		((ScheduledThreadRandIo)t).setTcb(tcb);
		t.start();
	}
	
    public void run() 
	{
        try 
		{ 
			Thread.sleep(1000); 
		}
        catch (Throwable e) 
		{}
        jpbu.setTCBList(tcbList, experimentNumber);
        while(this.done == false) 
		{
            this.done = true;
			forloop: for ( TcbWithIo tcb : tcbList ) // iterate through all threads
			{
                synchronized(tcb) 
				{
                    if (tcb.done==true) continue forloop; // if thread is done, take another thread
					if(tcb.io > 0) // if thread is interactive, do IO, then take new thread
					{
						tcb.io = tcb.io-1;
						this.done = false;
						continue forloop;
					}
                    tcb.mayRun = true; // if thread is computational, call thread to do the calculations
                    tcb.notify();
                }
                this.done = false;
				for (int i = 0; i < tcb.time; i++) 
				{
                    try 
					{ 
						Thread.sleep(this.quantum); // wait time*quantum ms for thread to compute
					}
                    catch ( Throwable e ) {}
					if(tcb.io > 0) break; // if thread is interactive, exit thread
				}
                synchronized(tcb) 
                {
                	if(tcb.mayRun == true) 
                    {
                		tcb.mayRun = false; // thread's mayrun flag is set to false
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
    }
}

	
