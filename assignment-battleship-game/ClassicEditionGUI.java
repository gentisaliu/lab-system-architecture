import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ClassicEditionGUI extends JFrame implements ActionListener
{
	private JPanel userInformation;
	private JPanel networkConfiguration;
	private JPanel buttonPanel;
	private JLabel playerName;
	private JTextField playerNameField;
	private JLabel portNr;
	private JTextField portNrField;
	private JButton connectButton;
	private JButton cancelButton;
	private String hostname;
	private String playername;
	private int portNumber;
	private Game gameData;
	private Controller cont;
	
	public ClassicEditionGUI()
	{
		setLayout(new FlowLayout());
		userInformation = new JPanel();
		userInformation.setLayout(new GridLayout(1, 1));
		playerName = new JLabel("Player name:");
		playerNameField = new JTextField("Player1");
		playerNameField.setPreferredSize(new Dimension(100, 20));
		userInformation.add(playerName);
		userInformation.add(playerNameField);
		networkConfiguration = new JPanel();
		networkConfiguration.setLayout(new GridLayout(1, 1));
		portNr = new JLabel("Port Nr.:");
		portNrField = new JTextField("1111");
		portNrField.setPreferredSize(new Dimension(100, 20));
		networkConfiguration.add(portNr);
		networkConfiguration.add(portNrField);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		connectButton = new JButton("Wait for connection");
		connectButton.addActionListener(this);
		buttonPanel.add(connectButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		add(userInformation);
		add(networkConfiguration);
		add(buttonPanel);
		setTitle("Battleship Options - Classic Edition");
		setSize(250, 130);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void elaborateInput()
	{
		playername = playerNameField.getText();
		portNumber = Integer.parseInt(portNrField.getText());
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == connectButton)
		{
			elaborateInput();
			gameData = new Game(playername, 10, 10);
			gameData.setDestroyers(1, -1);
			gameData.setSubmarines(1, -1);
			gameData.setBattleships(1, -1);
			gameData.setAircraftCarriers(1, -1);
			gameData.setShipRelocations(0);
			gameData.setPlayerSide("host");
			gameData.setMaximalShots(5);
			cont = new Controller(1, 1, playerNameField.getText(), 0, 10, 10);
			cont.setDestroyers(1, 1);
			cont.setSubmarines(1, 1);
			cont.setBattleships(1, 1);
			cont.setAircraftCarriers(1, 1);
			cont.useInfiniteAmmo(true);
			cont.useBlitzkriegStrategy(true);
			cont.setGameTimed(false);
			cont.setGameEdition("classical");
			cont.setMaximalShots(5);
			ConnectionInProgress c = new ConnectionInProgress((JFrame) this, " ", portNumber, null, true, gameData, cont);
			new Thread(c).run();
		}
		if(e.getSource() == cancelButton) System.exit(0);
	}
}