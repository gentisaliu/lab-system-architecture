/*
 * Author: Raphael Kimmig
 * Description: Thread control block of the ScheduledThread1 thread
 */

public class Tcb
{
    public boolean mayRun=false;
    public boolean done=false;
    public ScheduledThread thread;
	public int time=10; // quantum
	public int io;
}

