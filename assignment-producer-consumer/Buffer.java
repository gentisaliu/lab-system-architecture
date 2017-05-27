import java.util.ArrayList;

/*
 * Author: Genti Saliu and Raphael Kimmig
 * Description: The buffer where the producer puts its output and the consumer gets the products
 * June 2007
 */

class Buffer
{
	private ArrayList<Integer> results = new ArrayList<Integer>();	// list of results
	private ArrayList<Producer> producerList = new ArrayList<Producer>();	// list of producers
	private ArrayList<Consumer> consumerList = new ArrayList<Consumer>();	// list of consumers
	private CircularBuffer cb; // a circular buffer
	private int globalCounter; // the global counter
	private int producersCounter;	// number of producers running
	private int experimentNumber;	// experiment number
	
	//	 creates an empty circular buffer if length is larger than 1, otherwise a simple empty buffer is created
	public Buffer(int length, int experimentNumber)
	{
		this.experimentNumber = experimentNumber;
		producersCounter = 0;
		cb = new CircularBuffer(length);
		globalCounter = 1;
	}
	
	
	// stops all producer and consumer threads
	public void stopAll() 
	{
		for(Consumer c : consumerList) if (c != null) c.getThreadControlBlock().setDone(true);
		for (Producer p : producerList) if (p != null) p.getThreadControlBlock().setDone(true);
		synchronized(this) 
		{
			notifyAll();
		}
	}

	// changes cycle time of all consumer threads
	
	public void setConsumerTime(int time) 
	{
		for(Consumer c : consumerList) 
		{
			if (c != null) c.getThreadControlBlock().setCycleLength(time);
		}
	}

	// changes cycle time of all producer threads
	
	public void setProducerTime(int time) 
	{
		for(Producer p : producerList) if(p != null) p.getThreadControlBlock().setCycleLength(time);
	}
	
	// adds a new consumer thread
	public void registerThread(Consumer thread, int cycleLength)
	{
		ThreadControlBlock tcb = new ThreadControlBlock(cycleLength);	// creates thread control block of the new consumer thread
		tcb.setThread(thread);  // registers thread to its thread control block
		thread.setThreadControlBlock(tcb);	// assings the thread control block to the thread
		results.add(0);	// creates a new field to save workload of this thread
		consumerList.add(thread);	// adds this thread to the consumer threads' list
		thread.start();		// starts thread
	}
	
//	 adds a new consumer thread
	public void registerThread(Producer thread, int cycleLength, int cycles)
	{
		ThreadControlBlock tcb = new ThreadControlBlock(cycleLength);	// creates thread control block of the new consumer thread
		tcb.setThread(thread);		// registers thread to its thread control block
		thread.setThreadControlBlock(tcb);		// assings the thread control block to the thread
		producersCounter++;			// increases the number of present producers by 1
		producerList.add(thread);	// adds this thread to the producer threads' list
		tcb.setProducerCycles(cycles);		// assigns production cycles to this thread
		thread.start();		// starts thread
	}
	
	
	// closes thread
	
	public void oneProducerLess()
	{
		producersCounter--;
		synchronized (this) 
		{
			notifyAll();
		}
	}
	
	
	// returns workload of the consumer thread with the overgiven identification number
	
	public int getResult(int id)
	{
		return results.get(id);
	}
	
	// checks whether the producer threads are done
	public synchronized boolean producersDone()
	{
		return producersCounter == 0;
	}
	
	// puts a new product in the circular buffer
	
	public synchronized void put(int value, int id) throws InterruptedException
	{
		while(cb.isFull())	// if the circular buffer is full, wait until it is not
		{
			wait();
		}
		cb.writeBuffer(value);	// write a value to the next free position in the circular buffer
		if(experimentNumber != 3) System.out.println("Producer-Thread " + id + " just sent a product in the warehouse. Warehouse load: " + cb.getNumberOfFullSlots() + "/" + cb.getBufferSize() + " slots of the warehouse are full.\n");		// print confirmation message
		notifyAll();
	}
	
	// removes a new product from the circular buffer
	
	public synchronized int get(int id) throws InterruptedException
	{
		while(cb.isEmpty())		// if the circular buffer is empty notify producers and wait until it is not empty
		{
			if(producersCounter == 0 && cb.isEmpty()) 
			{
				notifyAll();
				return 0;
			}
			wait();
		}			
		int result = cb.readBuffer();		// remove result from the circular buffer
		results.add(id, results.get(id)+result);		//add result to this consumer thread's workload
		if(experimentNumber != 3) System.out.println("Consumer-thread " + id + " took product " + result + " from the warehouse. Overall product value is " + getResult(id) + ". Warehouse load: " + cb.getNumberOfFullSlots() + "/" + cb.getBufferSize() + " slots of the warehouse are full.\n");		// print confirmation message
		notifyAll();
		return result;
	}
	
	// returns the circular buffer
	public CircularBuffer getCircularBuffer()
	{
		return cb;
	}
	
	// returns the counter
	public int getCounter()
	{
		int temp = globalCounter;
		globalCounter++;
		return temp;
	}
	
	// checks whether the circular buffer contains only 0 values
	public boolean isCircularBufferEmpty()
	{
		return cb.isEmpty();
	}
}
