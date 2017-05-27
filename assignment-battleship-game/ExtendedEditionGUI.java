import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;

public class ExtendedEditionGUI extends JFrame implements ActionListener, ItemListener
{
	private ButtonGroup ammoButtons;
	private JCheckBox ckbox;
	private JCheckBox unlimited;
	private JCheckBox individual;
	private JPanel movements;
	private JPanel gridSize;
	private JPanel network;
	private JPanel fleet;
	private JPanel ammo;
	private JPanel strategy;
	private JPanel gamePlay;
	private JPanel timedGame;
	private JPanel playerInformation;
	private JPanel multiplayerInformation;
	private JPanel buttonPanel;
	private JPanel contain;
	private JPanel subset;
	private JLabel xCoord;
	private JLabel yCoord;
	private JLabel shipsReplacement;
	private JTextField shipsReplacementField;
	private JTextField xField;
	private JTextField yField;
	private JLabel playerName;
	private JTextField playerNameField;
	private JLabel playersYourSide;
	private JTextField yourSide;
	private JTextField oppositeSide;
	private JLabel playersOpponentSide;
	private JLabel maxShots;
	private JTextField maxShotsField;
	private JLabel aircraft;
	private JLabel battleship;
	private JLabel submarine;
	private JLabel destroyer;
	private JLabel ptboat;
	private JTextField aircraftfield;
	private JTextField battleshipfield;
	private JTextField submarinefield;
	private JTextField destroyerfield;
	private JTextField ptboatfield;
	private JLabel aircraftshots;
	private JLabel battleshipshots;
	private JLabel submarineshots;
	private JLabel destroyershots;
	private JLabel ptboatshots;
	private JTextField aircraftshfield;
	private JTextField battleshipshfield;
	private JTextField submarineshfield;
	private JTextField destroyershfield;
	private JTextField ptboatshfield;
	private JRadioButton blitzkrieg;
	private JRadioButton passivewar;
	private JCheckBox timed;
	private JTextField time;
	private JLabel port;
	private JTextField portfield;
	private JButton waitButton;
	private JButton cancelButton;
	private ButtonGroup strategyButtons;
	private TitledBorder strategyBorder;
	private TitledBorder timedBorder;
	private TitledBorder serverConfig;
	private TitledBorder sidesConfig;
	private TitledBorder playern;
	private TitledBorder fleetBorder;
	private TitledBorder ammoBorder;
	private TitledBorder shotsBorder;
	private TitledBorder gridBorder;
	private TitledBorder movementBorder;
	private String playername;
	private boolean infiniteAmmo;
	private String hostName;
	private int portNumber;
	private Game gameData;
	private Controller cont;
	
	public ExtendedEditionGUI()
	{
		setLayout(new FlowLayout());
		playern = BorderFactory.createTitledBorder("Player Info");
		playerInformation = new JPanel();
		playerInformation.setLayout(new FlowLayout());
		playerName = new JLabel("Player name:");
		playerInformation.add(playerName);
		playerNameField = new JTextField("Player1");
		playerNameField.setPreferredSize(new Dimension(100, 20));
		playerInformation.add(playerNameField);
		playerInformation.setBorder(playern);
		sidesConfig = BorderFactory.createTitledBorder("Configure Sides");
		multiplayerInformation = new JPanel();
		multiplayerInformation.setLayout(new FlowLayout());
		playersYourSide = new JLabel("Nr. Players in your side:");
		playersOpponentSide = new JLabel("Nr. Players in opponent side:");
		yourSide = new JTextField("0");
		yourSide.setPreferredSize(new Dimension(20, 20));
		oppositeSide = new JTextField("1");
		oppositeSide.setPreferredSize(new Dimension(20, 20));
		multiplayerInformation.add(playersYourSide);
		multiplayerInformation.add(yourSide);
		multiplayerInformation.add(playersOpponentSide);		
		multiplayerInformation.add(oppositeSide);
		multiplayerInformation.setBorder(sidesConfig);
		fleetBorder = BorderFactory.createTitledBorder("Fleet Size");
		fleet = new JPanel();
		fleet.setLayout(new FlowLayout());
		aircraft = new JLabel("Aircraft: x");
		battleship = new JLabel("Battleship: x");
		submarine = new JLabel("Submarine: x");
		destroyer = new JLabel("Destroyer: x");
		ptboat = new JLabel("PT boat: x");
		aircraftfield = new JTextField("1");
		aircraftfield.setPreferredSize(new Dimension(20, 20));
		battleshipfield = new JTextField("1");
		battleshipfield.setPreferredSize(new Dimension(20, 20));
		submarinefield = new JTextField("1");
		submarinefield.setPreferredSize(new Dimension(20, 20));
		destroyerfield = new JTextField("1");
		destroyerfield.setPreferredSize(new Dimension(20, 20));
		ptboatfield = new JTextField("1");
		ptboatfield.setPreferredSize(new Dimension(20, 20));
		fleet.add(aircraft);
		fleet.add(aircraftfield);
		fleet.add(battleship);
		fleet.add(battleshipfield);
		fleet.add(submarine);
		fleet.add(submarinefield);
		fleet.add(destroyer);
		fleet.add(destroyerfield);
		fleet.add(ptboat);
		fleet.add(ptboatfield);
		fleet.setBorder(fleetBorder);
		ammo = new JPanel();
		ammo.setLayout(new FlowLayout());
		ammoBorder = BorderFactory.createTitledBorder("Ammo");
		contain = new JPanel();
		contain.setLayout(new BoxLayout(contain, BoxLayout.Y_AXIS));
		subset = new JPanel();
		subset.setLayout(new FlowLayout());
		aircraftshots = new JLabel("Aircraft Ammo:");
		battleshipshots = new JLabel("Battleship Ammo:");
		submarineshots = new JLabel("Submarine Ammo:");
		destroyershots = new JLabel("Destroyer Ammo:");
		ptboatshots = new JLabel("PT-boat Ammo:");
		ammoButtons = new ButtonGroup();
		unlimited = new JCheckBox("Unlimited ammo", true);
		unlimited.addItemListener(this);
		ammoButtons.add(unlimited);
		individual = new JCheckBox("Assign ammo to each battleship");
		individual.addItemListener(this);
		ammoButtons.add(individual);
		aircraftshfield = new JTextField();
		aircraftshfield.setPreferredSize(new Dimension(20, 20));
		aircraftshfield.setEnabled(false);
		battleshipshfield = new JTextField();
		battleshipshfield.setPreferredSize(new Dimension(20, 20));
		battleshipshfield.setEnabled(false);
		submarineshfield = new JTextField();
		submarineshfield.setPreferredSize(new Dimension(20, 20));
		submarineshfield.setEnabled(false);
		destroyershfield = new JTextField();
		destroyershfield.setPreferredSize(new Dimension(20, 20));
		destroyershfield.setEnabled(false);
		ptboatshfield = new JTextField();
		ptboatshfield.setPreferredSize(new Dimension(20, 20));
		ptboatshfield.setEnabled(false);
		ammo.add(aircraftshots);
		ammo.add(aircraftshfield);
		ammo.add(battleshipshots);
		ammo.add(battleshipshfield);
		ammo.add(submarineshots);
		ammo.add(submarineshfield);
		ammo.add(destroyershots);
		ammo.add(destroyershfield);
		ammo.add(ptboatshots);
		ammo.add(ptboatshfield);
		gridBorder = BorderFactory.createTitledBorder("Grid Size");
		gridSize = new JPanel();
		gridSize.setLayout(new GridLayout(1, 4));
		xCoord = new JLabel("X:");
		yCoord = new JLabel("Y:");
		xField = new JTextField("10");
		yField = new JTextField("10");
		gridSize.add(xCoord);
		gridSize.add(xField);
		gridSize.add(Box.createRigidArea(new Dimension(0, 5)));
		gridSize.add(yCoord);
		gridSize.add(yField);
		gridSize.setBorder(gridBorder);
		strategyBorder = BorderFactory.createTitledBorder("Strategy");
		strategy = new JPanel();
		strategy.setLayout(new GridLayout(2, 1));
		strategyButtons = new ButtonGroup();
		blitzkrieg = new JRadioButton("Blitzkrieg", true);
		passivewar = new JRadioButton("Passive War");
		strategyButtons.add(blitzkrieg);
		strategyButtons.add(passivewar);
		strategy.add(blitzkrieg);
		strategy.add(passivewar);
		strategy.setBorder(strategyBorder);
		movementBorder = BorderFactory.createTitledBorder("Max. Relocations");
		movements = new JPanel();
		movements.setLayout(new GridLayout(2, 1));
		shipsReplacement = new JLabel("Ship Relocations/Game");
		movements.add(shipsReplacement);
		shipsReplacementField = new JTextField("0");
		shipsReplacementField.setPreferredSize(new Dimension(40, 20));
		movements.add(shipsReplacementField);
		movements.setBorder(movementBorder);
		timedBorder = BorderFactory.createTitledBorder("Timed");
		timedGame = new JPanel();
		timedGame.setLayout(new GridLayout(2, 1));
		timed = new JCheckBox("Timed Game");
		timed.addItemListener(this);
		time = new JTextField("05:00");
		time.setEnabled(false);
		timedGame.add(timed);
		timedGame.add(time);
		timedGame.setBorder(timedBorder);
		shotsBorder = BorderFactory.createTitledBorder("Shots/Turn");
		gamePlay = new JPanel();
		gamePlay.setLayout(new GridLayout(2, 1));
		maxShots = new JLabel("Max. Shots/Turn:");
		maxShotsField = new JTextField("5");
		maxShotsField.setPreferredSize(new Dimension(30, 20));
		gamePlay.add(maxShots);
		gamePlay.add(maxShotsField);
		gamePlay.setBorder(shotsBorder);
		serverConfig = BorderFactory.createTitledBorder("Server Configuration");
		network = new JPanel();
		network.setLayout(new BoxLayout(network, BoxLayout.X_AXIS));
		port = new JLabel("Port Nr.:");
		portfield = new JTextField("1111");
		portfield.setPreferredSize(new Dimension(100, 20));
		network.add(port);
		network.add(portfield);
		network.setBorder(serverConfig);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		waitButton = new JButton("Wait for connection");
		waitButton.addActionListener(this);
		buttonPanel.add(waitButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		add(playerInformation);
		add(multiplayerInformation);
		add(fleet);
		add(gamePlay);
		ckbox = new JCheckBox("Use game history to determine ammo amount");
		ckbox.addItemListener(this);
		ammoButtons.add(ckbox);
		subset.add(unlimited);
		subset.add(ckbox);
		subset.add(individual);
		contain.add(subset);
		contain.add(ammo);
		contain.setBorder(ammoBorder);
		add(contain);
		add(gridSize);
		add(strategy);
		add(movements);
		add(timedGame);
		add(network);
		add(buttonPanel);
		setTitle("Battleship Options - Extended Edition");
		setSize(660, 380);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void gatherGameData() throws Exception
	{
		portNumber = Integer.parseInt(portfield.getText());
		infiniteAmmo = unlimited.isSelected();
		cont = new Controller(Integer.parseInt(yourSide.getText().trim()) + 1, Integer.parseInt(oppositeSide.getText().trim()), playerNameField.getText().trim(), 
				Integer.parseInt(shipsReplacementField.getText().trim()), Integer.parseInt(xField.getText().trim()), Integer.parseInt(yField.getText().trim()));
		cont.setGameEdition("extended");
		cont.setMaximalShots(Integer.parseInt(maxShotsField.getText().trim()));
		cont.useBlitzkriegStrategy(blitzkrieg.isSelected());
		cont.setGameTimed(timed.isSelected());
		cont.setGameTime(time.getText().trim());
		cont.useInfiniteAmmo(infiniteAmmo);
		gameData = new Game(playerNameField.getText(), Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
		gameData.useUnlimitedAmmo(infiniteAmmo);

		if(!infiniteAmmo) gameData.setDestroyers(Integer.parseInt(destroyerfield.getText().trim()), Integer.parseInt(destroyershfield.getText().trim()));
		else gameData.setDestroyers(Integer.parseInt(destroyerfield.getText().trim()), -1);
		if(!infiniteAmmo) gameData.setSubmarines(Integer.parseInt(submarinefield.getText().trim()), Integer.parseInt(submarineshfield.getText().trim()));
		else gameData.setSubmarines(Integer.parseInt(submarinefield.getText().trim()), -1);
		if(!infiniteAmmo) gameData.setBattleships(Integer.parseInt(battleshipfield.getText().trim()), Integer.parseInt(battleshipshfield.getText().trim()));
		else gameData.setBattleships(Integer.parseInt(battleshipfield.getText().trim()), -1);
		if(!infiniteAmmo) gameData.setAircraftCarriers(Integer.parseInt(aircraftfield.getText().trim()), Integer.parseInt(aircraftshfield.getText().trim()));
		else gameData.setAircraftCarriers(Integer.parseInt(aircraftfield.getText().trim()), -1);
		gameData.setShipRelocations(Integer.parseInt(shipsReplacementField.getText().trim()));
		gameData.setPlayerSide("host");
		gameData.setMaximalShots(Integer.parseInt(maxShotsField.getText().trim()));
		if(!infiniteAmmo)
		{
			if(ckbox.isSelected()) estimateAmmo();
			else
			{		
				cont.setAircraftCarrierAmmo(Integer.parseInt(aircraftshfield.getText()));
				cont.setBattleshipAmmo(Integer.parseInt(battleshipshfield.getText()));
				cont.setSubmarineAmmo(Integer.parseInt(submarineshfield.getText()));
				cont.setDestroyerAmmo(Integer.parseInt(destroyershfield.getText()));
				cont.setPtBoatAmmo(Integer.parseInt(ptboatshfield.getText()));
			}
		}
		cont.setAircraftCarriers(Integer.parseInt(aircraftfield.getText()), 1);
		cont.setBattleships(Integer.parseInt(battleshipfield.getText()), 1);
		cont.setSubmarines(Integer.parseInt(submarinefield.getText()), 1);
		cont.setDestroyers(Integer.parseInt(destroyerfield.getText()), 1);
				/*
				 playerNameField.getText(), "extended", Integer.parseInt(maxShotsField.getText()), Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()), 
				Integer.parseInt(yourSide.getText()), 
				Integer.parseInt(oppositeSide.getText()), blitzkrieg.isSelected(), timed.isSelected(), infiniteAmmo);
		playername = playerNameField.getText();
		gameData.setShipReplacements(Integer.parseInt(shipsReplacementField.getText()));
		if(timed.isSelected()) gameData.setGameTime(time.getText()); */
	}
	
	public void estimateAmmo()
	{
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == waitButton) 
		{
			try
			{
				gatherGameData();
				ConnectionInProgress c = new ConnectionInProgress((JFrame)this, hostName, portNumber, null, true, gameData, cont);
				new Thread(c).run();
			}
			catch(Exception c)
			{
				JOptionPane.showMessageDialog(this, "<html>Verify your input. Make sure you have filled in all the<br>fields, have entered numbers where required or that<br>your server configuration is correct.</html>", "User-input error", JOptionPane.ERROR_MESSAGE);
				c.printStackTrace();
			}
		}
		if(e.getSource() == cancelButton) System.exit(0);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getSource() == timed)
		{
			if(timed.isSelected()) time.setEnabled(true);
			else time.setEnabled(false);
		}
		else
		{
			if(individual.isSelected())
			{
				aircraftshfield.setEnabled(true);
				battleshipshfield.setEnabled(true);
				submarineshfield.setEnabled(true);
				destroyershfield.setEnabled(true);
				ptboatshfield.setEnabled(true);
			}
			else
			{
				aircraftshfield.setEnabled(false);
				battleshipshfield.setEnabled(false);
				submarineshfield.setEnabled(false);
				destroyershfield.setEnabled(false);
				ptboatshfield.setEnabled(false);
			}
		}
	}
}