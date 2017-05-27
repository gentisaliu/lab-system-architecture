/*
 * Author: Genti Saliu
 * Description: Reads data from the buffer and displays them graphically
 */

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class JProgressBarUpdater extends Thread {

	private JProgressBar[] jpb; // reference to the progress bars
	private ThreadBuffer tb; // reference to the buffer
	private int msec;
	private boolean stop; // stop flag
	private LinkedList<TcbWithIo> tcbList;
	private int experimentNumber;
	private int counter;
	
	public JProgressBarUpdater(JProgressBar[] jpb, ThreadBuffer tb, int msec)
	{
		experimentNumber = 0;
		counter = 0;
		this.jpb = jpb;
		this.tb = tb;
		this.msec = msec;
		stop = false;
	}
	
	// reference to the thread control block list used in Scheduler4
	
	public void setTCBList(LinkedList<TcbWithIo> tcbList, int c)
	{
		experimentNumber = c;
		this.tcbList = tcbList;
	}
	
	
	// sets the flag to stop
	
	public void stopThread()
	{
		stop = true;
	}
	
	public void run()
	{
		while(!stop)
		{
			try
			{
				Thread.sleep((long) msec);
			}
			catch(InterruptedException ie)
			{
				ie.printStackTrace();
			}
			for(int i = 0; i < jpb.length; i++) jpb[i].setValue(tb.getBuffer(i)); // reads buffer content and visualizes it
			if(experimentNumber == 3)
			{
				counter = 0;
				forloop: for (TcbWithIo tcb : tcbList)
				{
					if(tcb.io > 0) jpb[counter].setForeground(Color.green);
					else
					{
						jpb[counter].setForeground(Color.red);
					}
					counter++;
				}
			}
			if(experimentNumber == 4)
			{
				counter = 0;
				forloop: for (TcbWithIo tcb : tcbList)
				{
					if(tcb.io > 0) jpb[counter].setForeground(Color.gray);
					else
					{
						switch(tcb.type)
						{
							case 0: jpb[counter].setForeground(Color.green); break;
							case 1: jpb[counter].setForeground(Color.yellow); break;
							case 2: jpb[counter].setForeground(Color.red); break;
							default: break;						
						}
					}
					counter++;
				}
			}
		}
	}
}
