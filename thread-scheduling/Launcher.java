/*
 * Author: Genti Saliu
 * Description: Gets parameters interactively and starts the experiments
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Launcher extends JFrame implements KeyListener, ActionListener, MouseListener
{
	private JPanel[] userInputPanel; // this panel contains labels, textfields and buttons
	private JPanel buttonPanel; // this panel contains buttons
	private JPanel progressBarPanel; // this panel contains the JProgressBar objects, the thread activity elements
	private JLabel description; // this element contains a brief description of the respective elements
	private JLabel expNr; // experiment number
	private JLabel threads; // thread number
	private JLabel innerLoop; // inner loop
	private JLabel outerLoop; // outer loop
	private JLabel msecs; // milliseconds
	private JTextField experimentNr; // receives the experiment number
	private JTextField threadNumber;
	private JTextField innerloop;
	private JTextField outerloop;
	private JTextField msec;
	private JLabel[] labels; // array contains during runtime dynamically generated label text
	private JProgressBar[] jpb; // progress bar
	private JTextField[] textFields; // array contains during runtime dynamically generated text fields (nr. of threads unknown before runtime etc.)
	private JButton startButton;
	private JButton stopButton;
	private JButton clearButton;
	private JButton aboutButton;
	private int numberOfThreads;
	private int experimentNumber;
	private int elementsInLine; // keeps track of the current graphical elements (labels, text fields) in a line; for proper layout purposes
	private int threadsWithSpecificOL; // number of threads which have specific outer loop numbers
	private int k;
	private int fillProgress; // keeps track how many of the dynamically generated visual elements have been already created
	private int innLoop;
	private int millis;
	private int ioThreads; // interactive threads
	private int minQuantum; // minimal quantum
	private int innerLoopEntries;
	private int msecEntries;
	private int[] quantum;
	private int[] specificOuterLoop;
	private String exp1Description; // contains experiment related information
	private String exp2Description;
	private String exp3Description;
	private String exp4Description;
	private double probability; // Percentage of interactive threads
	private ThreadBuffer tb;
	private Scheduler sched;
	private Scheduler3 sched3;
	private Scheduler4 sched4;
	private ScheduledThread1[] st; // user threads (interactive, computational)
	private JProgressBarUpdater jpbu; // progress bars
	
	
	// constructor; general elements of the UI are drawn, experiment description text is generated, useful arrays for input are created
	
	public Launcher()
	{
		super("Experiment Initialization & Visualization Interface - Versuch 1");
		probability = -1.0;
		exp1Description = "<html>Experiment 1 requires to create up to 10 user threads assigning all of them the same quantum. <br>Each thread should compute according to their quantum, after the quantum expires the scheduler <br>switches to the next thread and so on. Click on a progress bar, when they appear, to receive <br>thread-specific information. This might slow down the thread activity a little bit.</html>";
		exp2Description = "<html>Experiment 2 requires to create up to 10 user threads, each of which has different specific <br>quantum numbers. Click on a progress bar, when they appear, to receive thread-specific information. <br>This might slow down the thread activity a little bit.</html>";
		exp3Description = "<html>Experiment 3 requires to create up to 10 user threads, some of which are interactive and some computing <br>threads. You will be asked to enter the probability of threads being interactive. There is a <br>minimal quantum; all threads are supposed to have a whole multiple quantum of the minimal one. <br>The interactive threads simply print something on the screen. Click on a progress bar, when they appear, <br>to receive thread-specific information. This might slow down the thread activity a little bit.</html>";
		exp4Description = "<html>Experiment 4 requires to create up to 20 user threads. Each thread has a specific minimal time. All threads <br>start as interactive. Should an interactive thread print nothing during its time slice, its status will change <br>to balanced. The balanced threads' quantum is twice as big as the minimal one. Should a balanced thread <br>print nothing during its execution, it will become a computing thread with a quantum 4 times as big as the <br>minimal quantum. Computing threads are executed only then when all interactive and balanced threads <br>have once been executed. Should a balanced or computing thread print on the screen, it becomes <br>interactive. Click on a progress bar, when they appear, to receive thread-specific information. This might <br>slow down the thread activity a little bit.</html>";
		fillProgress = -1;
		innerLoopEntries = 0;
		msecEntries = 0;
		labels = new JLabel[40];
		textFields = new JTextField[40];
		numberOfThreads = 1;
		experimentNumber = 1;
		elementsInLine = 1;
		setSize(750, 900);
		getContentPane().setLayout(new FlowLayout());
		drawUserInteractionArea();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
		setVisible(true);
	}
	
	// This method draws the typical, for each experiment usual, graphical elements (experiment number, inner loop, outer loop etc)
	
	public void drawUserInteractionArea()
	{
		userInputPanel = new JPanel[10];
		progressBarPanel = new JPanel();
		progressBarPanel.setLayout(new BoxLayout(progressBarPanel, BoxLayout.Y_AXIS));
		userInputPanel[0] = new JPanel();
		userInputPanel[0].setLayout(new FlowLayout());
		expNr = new JLabel("Experiment Nr. [1-4]:");
		userInputPanel[0].add(expNr);
		experimentNr = new JTextField();
		experimentNr.addKeyListener(this);
		experimentNr.setPreferredSize(new Dimension(30, 20));
		userInputPanel[0].add(experimentNr);
		threads = new JLabel("Nr. threads:");
		userInputPanel[0].add(threads);
		threadNumber = new JTextField();
		threadNumber.addKeyListener(this);
		threadNumber.setPreferredSize(new Dimension(30, 20));
		threadNumber.setEditable(false);
		userInputPanel[0].add(threadNumber);
		innerLoop = new JLabel("Inner loop:");
		userInputPanel[0].add(innerLoop);
		innerloop = new JTextField();
		innerloop.setPreferredSize(new Dimension(60, 20));
		innerloop.setEditable(false);
		innerloop.addKeyListener(this);
		userInputPanel[0].add(innerloop);
		outerLoop = new JLabel("Nr. threads with specific outer loop nr:");
		userInputPanel[0].add(outerLoop);
		outerloop = new JTextField();
		outerloop.setPreferredSize(new Dimension(40, 20));
		outerloop.addKeyListener(this);
		outerloop.setEditable(false);
		userInputPanel[0].add(outerloop);
		k = 2;
		userInputPanel[1] = new JPanel();
		msecs = new JLabel("Output intervall (millisecs):");
		userInputPanel[1].add(msecs);
		msec = new JTextField();
		msec.setEditable(false);
		msec.addKeyListener(this);
		msec.setPreferredSize(new Dimension(30, 20));
		userInputPanel[1].add(msec);
		description = new JLabel(" ");
		add(description);
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		stopButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		aboutButton = new JButton("About...");
		aboutButton.addActionListener(this);
		buttonPanel = new JPanel();
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(aboutButton);
		add(buttonPanel);
		userInputPanel[k] = new JPanel();
		add(userInputPanel[k]);
		add(userInputPanel[0]);
		add(userInputPanel[1]);
	}
	
	// This method creates dynamically the graphical quantum input elements 
	
	public void queryThreadQuantums()
	{
		try
		{
			experimentNumber = Integer.parseInt(experimentNr.getText().trim());
			numberOfThreads = Integer.parseInt(threadNumber.getText().trim());
		}
		catch(NumberFormatException e)
		{
			printErrorMessage();
		}
		if(((experimentNumber == 1 || experimentNumber == 2 || experimentNumber == 3) && (numberOfThreads > 10 || numberOfThreads < 0)) || ((experimentNumber == 4) && (numberOfThreads < 0 || numberOfThreads > 20)) || (experimentNumber < 1 || experimentNumber > 4)) printErrorMessage();
		else
		{
			k = 1;
			if(experimentNumber == 1)
			{
				fillProgress++;
				elementsInLine++;
				description.setText(exp1Description);
				msec.setEditable(false);
				labels = new JLabel[11];
				textFields = new JTextField[11];
				labels[fillProgress] = new JLabel("Quantum:");
				userInputPanel[1].add(labels[fillProgress]);
				textFields[fillProgress] = new JTextField();
				textFields[fillProgress].setEditable(false);
				textFields[fillProgress].addKeyListener(this);
				textFields[fillProgress].setPreferredSize(new Dimension(30, 20));
				userInputPanel[k].add(textFields[fillProgress]);
				repaint();
				setVisible(true);
			}
			if(experimentNumber == 2 || experimentNumber == 3 || experimentNumber == 4)
			{
				if(experimentNumber == 2) description.setText(exp2Description);
				if(experimentNumber == 3) description.setText(exp3Description);
				if(experimentNumber == 4) description.setText(exp4Description);
				int maxElementsInLine = 5;
				if(experimentNumber == 3)
				{
					fillProgress++;
					labels[fillProgress] = new JLabel("Probability:");
					userInputPanel[k].add(labels[fillProgress]);
					textFields[fillProgress] = new JTextField();
					textFields[fillProgress].addKeyListener(this);
					textFields[fillProgress].setEditable(false);
					textFields[fillProgress].setPreferredSize(new Dimension(30, 20));
					userInputPanel[k].add(textFields[fillProgress]);
					fillProgress++;
					labels[fillProgress] = new JLabel("Min. quantum:");
					userInputPanel[k].add(labels[fillProgress]);
					textFields[fillProgress] = new JTextField();
					textFields[fillProgress].addKeyListener(this);
					textFields[fillProgress].setEditable(false);
					textFields[fillProgress].setPreferredSize(new Dimension(30, 20));
					userInputPanel[k].add(textFields[fillProgress]);
					elementsInLine=4;
					repaint();
					setVisible(true);
				}
				for(int i = 0; i < numberOfThreads; i++)
				{
					fillProgress++;
					if(elementsInLine > maxElementsInLine-1) 
					{
						elementsInLine = 0;
						k++;
						userInputPanel[k] = new JPanel();
						if(experimentNumber == 3) maxElementsInLine = 4;
						else maxElementsInLine = 6;
					}
					if(experimentNumber != 3) labels[fillProgress] = new JLabel("Quantum " + i + ":");
					else labels[fillProgress] = new JLabel("Quantum Multiple " + i + ":");
					userInputPanel[k].add(labels[fillProgress]);
					textFields[fillProgress] = new JTextField();
					textFields[fillProgress].setEditable(false);
					textFields[fillProgress].addKeyListener(this);
					textFields[fillProgress].setPreferredSize(new Dimension(30, 20));
					userInputPanel[k].add(textFields[fillProgress]);
					add(userInputPanel[k]);
					repaint();
					setVisible(true);
					elementsInLine++;
				}
			}
		}
	}
	
//	 This method creates dynamically the graphical outer loop input elements 
	
	public void queryThreadOuterLoops()
	{
		try
		{
			threadsWithSpecificOL = Integer.parseInt(outerloop.getText().trim());
			System.out.println(threadsWithSpecificOL);
		}
		catch(NumberFormatException e)
		{
			printErrorMessage();
		}
		if(threadsWithSpecificOL < 0 || threadsWithSpecificOL > numberOfThreads) printErrorMessage();
		else
		{
			textFields[0].setEditable(true);
			for(int i = 0; i < threadsWithSpecificOL; i++)
			{
				int maxElementsInLine = 5;
				fillProgress++;
				System.out.println(fillProgress);
				if(elementsInLine > maxElementsInLine - 1)
				{
					maxElementsInLine = 7;
					elementsInLine = 0;
					k++;
					userInputPanel[k] = new JPanel();
				}
				labels[fillProgress] = new JLabel("Outer loop " + i + ":");
				userInputPanel[k].add(labels[fillProgress]);
				textFields[fillProgress] = new JTextField();
				textFields[fillProgress].addKeyListener(this);
				textFields[fillProgress].setEditable(false);
				textFields[fillProgress].setPreferredSize(new Dimension(30, 20));
				userInputPanel[k].add(textFields[fillProgress]);
				add(userInputPanel[k]);
				repaint();
				setVisible(true);
				elementsInLine++;
			}
		}
	}
	
	// Error message should the user enter non handable values
	
	public void printErrorMessage()
	{
		JOptionPane.showMessageDialog(this, "Input contains invalid characters or unacceptable values.", "Input error", JOptionPane.ERROR_MESSAGE);
		clearUserInterface();
	}
	
	// Cleans completely the user interface elements, the typical ones are redrawn again
	
	public void clearUserInterface()
	{
		for(int q = 0; q < userInputPanel.length; q++)
		{
			if(userInputPanel[q] == null) continue;
			userInputPanel[q].removeAll();
			remove(userInputPanel[q]);
			userInputPanel[q] = null;
		}
		userInputPanel = null;
		for(int l = 0; l < textFields.length; l++)
		{
			textFields[l] = null;
			labels[l] = null;
		}
		buttonPanel.removeAll();
		tb = null;
		remove(buttonPanel);
		description.setText("");
		k = 0;
		probability = -1.0;
		msecEntries = 0;
		experimentNumber = 0;
		threadsWithSpecificOL = 0;
		specificOuterLoop = null;
		jpbu = null;
		sched = null;
		sched3 = null;
		sched4 = null;
		progressBarPanel.removeAll();
		remove(progressBarPanel);
		progressBarPanel = null;
		jpb = null;
		st = null;
		buttonPanel = null;
		quantum = null;
		innerLoopEntries = 0;
		fillProgress = -1;
		msecEntries = 0;
		drawUserInteractionArea();
		this.repaint();
		setVisible(true);
	}
	
	// the progress bars with the respective properties are generated
	
	public void drawProgressBars()
	{
		jpb = new JProgressBar[numberOfThreads];
		for(int i = 0; i < numberOfThreads; i++) 
		{
			if(i < threadsWithSpecificOL) jpb[i] = new JProgressBar(0, specificOuterLoop[i]);
			else jpb[i] = new JProgressBar(0, 100);
			jpb[i].setPreferredSize(new Dimension(680, 50));
			if(st[i] instanceof ScheduledThreadWithIo) 
			{
				jpb[i].setForeground(Color.green);
				jpb[i].setPreferredSize(new Dimension(680, 100));
			}
			else 
			{
				jpb[i].setForeground(Color.red);
				jpb[i].setStringPainted(true);
			}
			jpb[i].addMouseListener(this);
			progressBarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			progressBarPanel.add(jpb[i]);
		}
		add(progressBarPanel);
		repaint();
		setVisible(true);
		jpbu = new JProgressBarUpdater(jpb, tb, millis);
		if(experimentNumber == 4) sched4.setProgressBarUpdater(jpbu, 4);
		if(experimentNumber == 3) sched3.setProgressBarUpdater(jpbu, 3);
		jpbu.start();
	}
	
	// user input is checked for integrity then parsed
	
	public void verifyAndParseInput()
	{
		try
		{
			innLoop = Integer.parseInt(innerloop.getText());
		}
		catch(NumberFormatException e)
		{
			printErrorMessage();
		}
		if(innLoop < 1) printErrorMessage();
		else
		{
			int c;
			int l;
			if(experimentNumber == 1) quantum = new int[1];
			else quantum = new int[numberOfThreads];
			millis = Integer.parseInt(msec.getText().trim());
			if(experimentNumber == 3)
			{
				c = 2;
				try
				{
					probability = Double.parseDouble(textFields[0].getText().trim());
					minQuantum = Integer.parseInt(textFields[1].getText().trim());
				}
				catch(NumberFormatException e)
				{
					printErrorMessage();
				}
			}
			else c = 0;
			for(l = c; l < numberOfThreads + c; l++) 
			{
				try
				{
					quantum[l-c] = Integer.parseInt(textFields[l].getText().trim());
					if(experimentNumber == 1) break;
				}
				catch(NumberFormatException e)
				{
					printErrorMessage();
				}
			}
			if(experimentNumber != 1) l--;
			specificOuterLoop = new int[numberOfThreads];
			for(c = l; c < threadsWithSpecificOL + l; c++)
			{
				try
				{
					System.out.println(textFields[c].getText() + " " + c);
					specificOuterLoop[c-l] = Integer.parseInt(textFields[c+1].getText().trim());
					if(specificOuterLoop[c-l] < 0) break;
				}
				catch(NumberFormatException e)
				{
					printErrorMessage();
				}
			}
		}
	}
	
	// the wanted experiment with the entered parameters is started, visualization elements are drawn in this phase
	
	public void launchExperiment()
	{
		tb = new ThreadBuffer(new int[numberOfThreads]);
		st = new ScheduledThread1[numberOfThreads];
		if(experimentNumber == 1)
		{
			sched = new Scheduler();
			for(int c = 0; c < numberOfThreads; c++)
			{
				if(c < threadsWithSpecificOL) 
				{
					st[c] = new ScheduledThread1(specificOuterLoop[c], innLoop, c, tb);
				}
				else 
				{
					st[c] = new ScheduledThread1(100, innLoop, c, tb);
				}
				sched.registerThread(st[c], quantum[0]);
			}
			sched.start();
		}
		if(experimentNumber == 2)
		{
			sched = new Scheduler();
			for(int c = 0; c < numberOfThreads; c++)
			{
				if(c < threadsWithSpecificOL) 
				{
					st[c] = new ScheduledThread1(specificOuterLoop[c], innLoop, c, tb);
				}
				else 
				{
					st[c] = new ScheduledThread1(100, innLoop, c, tb);
				}
				sched.registerThread(st[c], quantum[c]);
			}
			sched.start();
		}
		if(experimentNumber == 3)
		{
			sched3 = new Scheduler3();
			sched3.setQuantum(minQuantum);
			for(int c = 0; c < numberOfThreads; c++)
			{
				if(c < threadsWithSpecificOL) 
				{
					st[c] = new ScheduledThreadRandIo(specificOuterLoop[c], 5, probability, c, tb, innLoop);
				}
				else 
				{
					st[c] = new ScheduledThreadRandIo(100, 5, probability, c, tb, innLoop);
				}
				sched3.registerThread(st[c], quantum[c]);
			}
			sched3.start();
		}
		if(experimentNumber == 4)
		{
			sched4 = new Scheduler4();
			for(int c = 0; c < numberOfThreads; c++)
			{
				if(c < threadsWithSpecificOL) 
				{
					st[c] = new ScheduledThreadRandIo(specificOuterLoop[c], 20, 0.1, c, tb, innLoop);
				}
				else 
				{
					st[c] = new ScheduledThreadRandIo(100, 20, 0.5, c, tb, innLoop);
				}
				sched4.registerThread(st[c], quantum[c]);
			}
			sched4.start();
		}
		drawProgressBars();
		startButton.setEnabled(false);
		clearButton.setEnabled(false);
	}
	
	// Sends a stop signal to the working threads and to the scheduler
	
	public void stopThreads()
	{
		for(int i = 0; i < numberOfThreads; i++) st[i].stopThread();
		jpbu.stopThread();
		clearUserInterface();
		JOptionPane.showMessageDialog(this, "Wait for the Scheduler to exit before you enter new parameters.");
	}
	
	// Button pressing actions are handled in this method
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == aboutButton) JOptionPane.showMessageDialog(this, "Versuch 1\nAuthors: Raphael Kimmig, Genti Saliu\nMay 2007\nPractical Work System Architecture, University of Karlsruhe");
		if(e.getSource() == clearButton) clearUserInterface();
		if(e.getSource() == startButton)
		{
			verifyAndParseInput();
			launchExperiment();
			stopButton.setEnabled(true);
		}
		if(e.getSource() == stopButton) 
		{
			stopThreads();
		}
	}
	
	// Guards for a key to be typed
	
	public void keyTyped(KeyEvent evt)
	{
		for(int c = 0; c < textFields.length; c++) 
		{
			if(evt.getSource() == textFields[c]) 
			{
				if(c-1 >= 0) 
				{
					textFields[c-1].setEditable(false);
					textFields[c-1].setEnabled(false);
				}
				if(c+1 >= textFields.length) break;
				if(textFields[c+1] != null) textFields[c+1].setEditable(true);
			}
			if(evt.getSource() == textFields[0]) 
			{
				msec.setEnabled(false);
				msec.setEditable(false);
			}
		}
		if(evt.getSource() == experimentNr) threadNumber.setEditable(true);
		if(evt.getSource() == threadNumber) 
		{
			experimentNr.setEditable(false);
			experimentNr.setEnabled(false);
			innerloop.setEditable(true);
		}
		if(evt.getSource() == innerloop) 
		{
			threadNumber.setEditable(false);
			threadNumber.setEnabled(false);
			if(innerLoopEntries == 0) queryThreadQuantums();
			outerloop.setEditable(true);
			innerLoopEntries++;
		}
		if(evt.getSource() == outerloop) 
		{
			innerloop.setEditable(false);
			innerloop.setEnabled(false);
			msec.setEditable(true);
		}
		if(evt.getSource() == msec)
		{
			if(msecEntries == 0) queryThreadOuterLoops();
			outerloop.setEditable(false);
			outerloop.setEnabled(false);
			msecEntries++;
		}
	}
	
	public void keyPressed(KeyEvent evt)
	{
		
	}
	
	public void keyReleased(KeyEvent evt)
	{
		
	}
	
	// Guards for a click on any area of the progress bars
	
	public void mouseClicked(MouseEvent e)
	{
		for(int i = 0; i < jpb.length; i++)
		{
			if(e.getSource() == jpb[i])
			{
				JOptionPane.showMessageDialog(this, (experimentNumber == 3 && i < ioThreads) ? ("Thread " + i + " is interactive.") : "Thread: " + i + ", Quantum: " + (quantum.length > 1 ? quantum[i] : quantum[0]) + (experimentNumber == 3 ? ", Minimal Quantum " + minQuantum : "") + ", Inner Loop: " + st[i].getInnerLoop() + ", Outer Loop: " + st[i].getMax());;
				break;
			}
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{
		
	}
	
	public static void main(String[] args)
	{
		new Launcher();
	}
}
