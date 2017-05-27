/*
 * Author: Genti Saliu
 * Description: Launches Experiments 1 to 3
 * June 2007 
 */
public class Launcher 
{
	public static boolean failed = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int experimentNumber = 0;
		int producers = 0;
		int consumers = 0;
		int cycles = 0;
		int cycleLengthC = 0;
		int cycleLengthP = 0;
		int bufferSize = 0;
		
		// parses user input
		
		for(int i = 0; i < args.length; i++)
		{
			// parses experiment number if word found
			
			if(args[i].equals("-exp"))
			{
				try
				{
					experimentNumber = Integer.parseInt(args[i+1]);
					if(experimentNumber < 1 || experimentNumber > 3) printUsage(experimentNumber);
				}
				catch(NumberFormatException e)
				{
					printUsage(experimentNumber);
				}
			}
			
			// parses cycles number if word found
			
			if(args[i].equals("-c"))
			{
				try
				{
					cycles = Integer.parseInt(args[i+1]);
					if(cycles < 1) printUsage(experimentNumber);
				}
				catch(NumberFormatException e)
				{
					printUsage(experimentNumber);
				}
			}
			
			// parces the buffer size if word found
			
			if(args[i].equals("-b"))
			{
				try
				{
					bufferSize = Integer.parseInt(args[i+1]);
					if(bufferSize < 1) printUsage(experimentNumber);
				}
				catch(NumberFormatException e)
				{
					printUsage(experimentNumber);
				}
			}
			
			// parses producers' cycle time if word found
			
			if(args[i].equals("-tp"))
			{
				try
				{
					cycleLengthP = Integer.parseInt(args[i+1]);
					if(cycleLengthP < 0) printUsage(experimentNumber);
				}
				catch(NumberFormatException e)
				{
					printUsage(experimentNumber);
				}
			}
			
			// parses consumers' if word found
			
			if(args[i].equals("-tc"))
			{
				try
				{
					cycleLengthC = Integer.parseInt(args[i+1]);
					if(cycleLengthC < 0) printUsage(experimentNumber);
				}
				catch(NumberFormatException e)
				{
					printUsage(experimentNumber);
				}
			}
			
			//prints help if word found
			
			if(args[i].equals("-h")) 
			{
				printUsage(experimentNumber);
				failed = true;
			}
		}
		
		// if help wasn't printed the experiment is launched
		
		if(!failed)
		{
			Buffer buffer;
			switch(experimentNumber)
			{
				case 1: buffer = new Buffer(1, experimentNumber); Consumer c = new Consumer(buffer, 0); Producer p = new Producer(buffer, 0); buffer.registerThread(p, cycleLengthP, cycles); buffer.registerThread(c, cycleLengthC); break;
				case 2: buffer = new Buffer(bufferSize, experimentNumber); Consumer c2 = new Consumer(buffer, 0); Producer p2 = new Producer(buffer, 0); buffer.registerThread(p2, cycleLengthP, cycles); buffer.registerThread(c2, cycleLengthC); break;
				case 3: Gui g = new Gui(); g.init(); g.changeBSize(); break;
			}
		}
	}
	
	// prints a description of the required experiment parameters
	
	public static void printUsage(int experimentNumber)
	{
		failed = true;
		switch(experimentNumber)
		{
			case 1: System.out.println("\nDescription: Launcher -exp 1 starts experiment 1"); System.out.println("Usage: Launcher -exp 1 [Experiment Options]\n"); System.out.println("Experiment Options include:"); System.out.println("-c [cycles]\t\tProduction Cycles (Parameter must be positive)"); System.out.println("-tp/-tc [duration]\tProduction/Consuming cycle duration (Parameter must be positive)"); System.out.println("-h\t\t\tPrints usage on screen"); break;
			case 2: System.out.println("\nDescription: Launcher -exp 2 starts experiment 2"); System.out.println("Usage: Launcher -exp 2 [Experiment Options]\n"); System.out.println("Experiment Options include:"); System.out.println("-c [cycles]\t\tProduction Cycles (Parameter must be positive)"); System.out.println("-tp/-tc [duration]\tProduction/Consuming cycle duration (Parameter must be positive)"); System.out.println("-b [buffersize]\t\tSize of circular buffer (Parameter must be positive)"); System.out.println("-h\t\t\tPrints usage on screen"); break;
			case 3: System.out.println("\nDescription: Launcher -exp 3 starts experiment 3"); System.out.println("Usage: Launcher -exp 3\n");
			default: System.out.println("\nDescription: Class Launcher starts 3 different experiments"); System.out.println("Usage: Launcher -exp [1-3] [Experiment Options]\n"); System.out.println("Experiment Options include:"); System.out.println("-c [cycles]\t\t\tProduction Cycles"); System.out.println("-tp/-tc [duration]\t\tProduction/Consuming cycle duration"); System.out.println("-b [buffersize]\t\t\tSize of the circular buffer"); System.out.println("-e [number of producers]\tNumber of producer threads"); System.out.println("-v [number of consumers]\tNumber of consumer threads"); System.out.println("-h\t\t\t\tPrints usage on screen"); System.out.println("\nNote: Not all experiment options listed here apply to each experiment. Refer to experiment usage for details."); break;
		}
	}

}
