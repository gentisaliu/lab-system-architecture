public class Scheduler4 extends Scheduler3 
{
	private int maxType=0; // 0 = interactive, 1 = balanced, 2 = computational
	private boolean gotOne = true;
	private JProgressBarUpdater jpbu;
	private int experimentNumber;
	
	public void setProgressBarUpdater(JProgressBarUpdater jpbu, int a)
	{
		this.experimentNumber = a;
		this.jpbu = jpbu;
	}

	// creates the thread's thread control block and starts the thread
	
	public void registerThread(ScheduledThreadRandIo t) 
	{
		TcbWithIo tcb = new TcbWithIo();
		tcb.thread=t;
		tcbList.add(tcb);
		t.tcb=tcb;
		t.start();
	}
	
    public void run() 
	{
        try 
		{ 
			Thread.sleep(1000); 
		}
        catch ( Throwable e) 
		{}
        jpbu.setTCBList(tcbList, experimentNumber);
        while(this.done == false) 
		{
            this.done = true;
			if (this.gotOne == false) this.maxType++;
			this.gotOne=false; //set to true in forloop if we got a process with type <=maxtype
			forloop: for ( TcbWithIo tcb : tcbList ) 
			{	
                synchronized(tcb) 
				{
                    if (tcb.done==true) continue forloop;
					if (tcb.type > this.maxType) 
					{
						this.done=false;
						continue forloop;
					}
					if (tcb.type < this.maxType && tcb.io == 0) this.maxType=tcb.type;
					if(tcb.io > 0) 
						tcb.io = tcb.io-1;

					if (tcb.io > 0) {
						this.done = false;
						continue forloop;
					}


					if (tcb.type < this.maxType)
						this.maxType=tcb.type;
					
					System.out.println(tcb.type);
					this.gotOne = true;
                    tcb.mayRun = true;
                    tcb.notify();
                }
                this.done = false;
				for (int i = 0; i < (1 << tcb.type) ; i++) 
				{
                    try 
					{ 
						Thread.sleep(tcb.time); 
					}
                    catch(Throwable e) 
					{}
					if(tcb.io > 0) 
					{
						tcb.type = 0; // io => interaktiv
						break;
					}
				}
				if(tcb.io == 0 && tcb.type < 2) tcb.type = tcb.type+1;
                synchronized(tcb) 
				{
                    if(tcb.mayRun == true) 
					{
                        tcb.mayRun = false;
                        try 
						{ 
							tcb.wait(); 
						}
                        catch(Throwable e) 
						{}
                    }
                }
            }
        }
    }
}

	
