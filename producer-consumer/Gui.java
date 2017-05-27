/*
 * Author: Raphael Kimmig
 * Description: GUI
 * June 2007
 */

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.event.*;
import java.awt.event.*; 

public class Gui
{
	static Container visContainer;
	static Container consumerContainer;
	static Container producerContainer;

	static JFrame window;
	
	static Container mainContainer;
	static Container menuContainer;

	static JSlider producerSlider;
	static JSlider consumerSlider;
	
	static JButton prodsButton;
	static JButton consButton;
	static JButton bufButton;
	static JButton stopButton;
	
	static JLabel cycleLabel;
	static JLabel ctimeLabel;
	static JLabel ptimeLabel;
	
	static JTextField numCyclesText;
	static JTextField bufSizeText;
	
	static StorageRep storageRep;

	static Updater updater;
	static Buffer buffer;
	static int numConsumers = 0;
	static int numProducers = 0;
	public Gui() {
		window = new JFrame("Experiment 3");
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	//--------------------------------------------------
	// add a consumer
	//-------------------------------------------------- 
	static private void addConsumer() {
		Consumer con = new Consumer(buffer,numConsumers++);
		JComponent cr = new ConsumerRep(con);
		
		buffer.registerThread(con,consumerSlider.getValue());	
		consumerContainer.add(cr);
		consumerContainer.validate();
		
	}
	//--------------------------------------------------
	// add a producer
	//-------------------------------------------------- 
	static private void addProducer() {
		int cycles = Integer.parseInt(numCyclesText.getText());
		Producer pro = new Producer(buffer,numProducers++);
		JComponent pr = new ProducerRep(pro);

		buffer.registerThread(pro,producerSlider.getValue(),cycles);	
		producerContainer.add(pr);
		producerContainer.validate();
			
	}
	
	static  void changeBSize() {
		int bsize = Integer.parseInt(bufSizeText.getText());
		if(buffer != null) stopAll();
		numConsumers = 0;
		numProducers = 0;
		initVis(new Buffer(bsize, 3));
	}

	static private int getBufferSize() {
	 	return buffer.getCircularBuffer().getBufferSize();
	}
	static private void stopAll() {
		buffer.stopAll();
		updater.stop = true;
		visContainer.removeAll();
	}

	static private void consTimeChanged() {
		int time = consumerSlider.getValue();
		buffer.setConsumerTime(time);
	}

	static private void prodTimeChanged() {
		int time = producerSlider.getValue();
		buffer.setProducerTime(time);
	}


	static void addComponent( Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height, double wx, double wy ) { 
		GridBagConstraints gbc = new GridBagConstraints(); 
		gbc.fill = GridBagConstraints.BOTH; 
		gbc.gridx = x; gbc.gridy = y; 
		gbc.gridwidth = width; gbc.gridheight = height; 
		gbc.weightx = wx; gbc.weighty = wy; 
		gbl.setConstraints( c, gbc ); 
		cont.add( c ); 
	} 

	
	public static void init() {
		mainContainer = window.getContentPane();
		mainContainer.setLayout( new BorderLayout() ); 
		
		menuContainer = new Container();
		GridBagLayout gbl = new GridBagLayout(); 
		menuContainer.setLayout( gbl ); 
		
		//--------------------------------------------------
		// Menu Stuff
		//-------------------------------------------------- 
		
		//--------------------------------------------------
		// producer
		//-------------------------------------------------- 
		prodsButton = new JButton("Add prod");
		prodsButton.addActionListener( new ActionListener() { 
			      public void actionPerformed( ActionEvent e ) { 
					          addProducer(); 
							  } 
		} );

		cycleLabel = new JLabel( "Cycles: " );
		numCyclesText = new JTextField("30");
		//--------------------------------------------------
		// consumer	
		//-------------------------------------------------- 
		consButton = new JButton("Add cons");
		consButton.addActionListener( new ActionListener() { 
			      public void actionPerformed( ActionEvent e ) { 
					         addConsumer(); 
							} 
		} );
		//--------------------------------------------------
		// buffersize
		//-------------------------------------------------- 
		bufButton = new JButton("Clear & resize buffer" );
		bufButton.addActionListener( new ActionListener() { 
			      public void actionPerformed( ActionEvent e ) { 
					         changeBSize(); 
							} 
		} );
		bufSizeText = new JTextField("16");
	
		//--------------------------------------------------
		// time sliders
		//-------------------------------------------------- 
		
		ctimeLabel = new JLabel("Consumer time");
		ptimeLabel = new JLabel("Producer time");

		producerSlider = new JSlider( 100, 10000, 1000 ); 
		producerSlider.addChangeListener( new ChangeListener() { 
			public void stateChanged( ChangeEvent e ) { 
				prodTimeChanged(); 
			} 
		} ); 
		consumerSlider = new JSlider( 100, 10000, 1000 ); 
		consumerSlider.addChangeListener( new ChangeListener() { 
			public void stateChanged( ChangeEvent e ) { 
				consTimeChanged();
			} 
		} ); 
		
		
		//--------------------------------------------------
		// register	
		//-------------------------------------------------- 
		addComponent(menuContainer,gbl,bufButton,0,0,2,1,1,0);
		addComponent(menuContainer,gbl,bufSizeText,2,0,2,1,1,0);
		
		addComponent(menuContainer,gbl,prodsButton,0,1,2,1,1,0);
		addComponent(menuContainer,gbl,consButton,2,1,2,1,1,0);

		addComponent(menuContainer,gbl,cycleLabel,0,2,1,1,0,0);
		addComponent(menuContainer,gbl,numCyclesText,1,2,1,1,0,0);

		
		addComponent(menuContainer,gbl,ptimeLabel,0,3,2,1,1,0);
		addComponent(menuContainer,gbl,ctimeLabel,2,3,2,1,1,0);
		
		addComponent(menuContainer,gbl,producerSlider,0,4,2,1,1,0);
		addComponent(menuContainer,gbl,consumerSlider,2,4,2,1,1,0);
		
		mainContainer.add(menuContainer,BorderLayout.NORTH);
		
		window.setVisible( true );
		window.pack();

		//--------------------------------------------------
		// vis stuff
		//-------------------------------------------------- 
	}
	public static void initVis(Buffer b) {
		buffer = b;
		producerContainer = new Container();
		producerContainer.setLayout(new GridLayout(0,1));
		
		consumerContainer = new Container();
		consumerContainer.setLayout(new GridLayout(0,1));

		storageRep = new StorageRep(buffer);
	
		visContainer = new Container();
		visContainer.setLayout(new GridLayout(0,3));

		visContainer.add(producerContainer);
		visContainer.add(storageRep);
		visContainer.add(consumerContainer);

		mainContainer.add(visContainer, BorderLayout.SOUTH);
		mainContainer.validate();

		//--------------------------------------------------
		// visContainer could be replaced by a nice animation canvas :-)
		//-------------------------------------------------- 
		
		//--------------------------------------------------
		// refresh teh graphics every 50 ms
		//-------------------------------------------------- 
		updater = new Updater(visContainer,50);
		updater.start();
		window.pack();
	}
}

class Updater extends Thread 
{
	private Container toRefresh;
	private int updateInt;
	boolean stop;
	public Updater(Container c, int msecs) {
		this.toRefresh = c;
		this.stop = false;
		this.updateInt = updateInt;
	}

	public void run() {
		while (!stop) {
			toRefresh.repaint();
			try { Thread.sleep(updateInt); }
			catch( Throwable e) {}
		}
	}
}

