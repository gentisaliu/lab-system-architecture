/*
 * Author: Raphael Kimmig
 * Description: Thread control block of the ScheduledThreadWithIo and ScheduledThreadRandIo threads
 */

public class TcbWithIo extends Tcb
{
	public int type = 0;
	public int maxIo = 5;
}