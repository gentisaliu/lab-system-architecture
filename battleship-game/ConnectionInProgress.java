import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class ConnectionInProgress extends JDialog implements Runnable
{
	private JProgressBar connecting;
	private String hostName;
	private int portNumber;
	private JLabel text;
	private NetDaemon nd;
	private JPanel objects;
	private boolean connected;
	private boolean isServer;
	private Game gameData;
	private JLabel network;
	private StringTokenizer tokens;
	private ArrayList<String> data;
	private boolean serverDone;
	private ChooseSide cs;
	private Controller c;
	private String playerName;
	
	public ConnectionInProgress(JFrame owner, String hostName, int portNumber, String playerName, boolean isServer, Game gameData, Controller c)
	{
		this.playerName = playerName;
		owner.dispose();
		serverDone = false;
		this.c = c;
		data = new ArrayList<String>();
		text = new JLabel("Connection to the host computer is being established...");
		this.isServer = isServer;
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.gameData = gameData;
		setLayout(new FlowLayout());
		add(text);
		objects = new JPanel();
		objects.setLayout(new GridLayout(2, 1, 0, 15));
		connecting = new JProgressBar();
		connecting.setPreferredSize(new Dimension(300, 20));
		connecting.setIndeterminate(true);
		objects.add(connecting);
		add(objects);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle("Connecting...");
		setSize(350, 90);
		setResizable(true);
		setVisible(true);
	}
	
	public void prepareGameData(String serverResponse)
	{
		tokens = new StringTokenizer(serverResponse);
		while(tokens.hasMoreTokens()) data.add(tokens.nextToken());
		gameData = new Game(playerName, Integer.parseInt(data.get(6)), Integer.parseInt(data.get(8)));
		gameData.useUnlimitedAmmo(new Boolean(data.get(24)).booleanValue());
		gameData.setDestroyers(Integer.parseInt(data.get(17)), Integer.parseInt(data.get(34)));
		gameData.setSubmarines(Integer.parseInt(data.get(16)), Integer.parseInt(data.get(33)));
		gameData.setBattleships(Integer.parseInt(data.get(15)), Integer.parseInt(data.get(32)));
		gameData.setAircraftCarriers(Integer.parseInt(data.get(14)), Integer.parseInt(data.get(31)));
		gameData.setMaximalShots(Integer.parseInt(data.get(21)));
		if(data.get(3).equals("classical"))
		{
			gameData.setGameTimed(new Boolean(data.get(35)).booleanValue());
			gameData.setGameTime(data.get(38));
		}
		else gameData.setGameTimed(false);
		/*
		 data.get(3), Integer.parseInt(data.get(21)), Integer.parseInt(data.get(6)), Integer.parseInt(data.get(8)), 
		Integer.parseInt(data.get(14)), Integer.parseInt(data.get(15)), Integer.parseInt(data.get(16)), Integer.parseInt(data.get(17)), 
		Integer.parseInt(data.get(18)), Integer.parseInt(data.get(54)), Integer.parseInt(data.get(51)), 
		new Boolean(data.get(44)).booleanValue(), new Boolean(data.get(37)).booleanValue(), new Boolean(data.get(24)).booleanValue()); */
		/*
		gameData.setAircraftCarrierAmmo(Integer.parseInt(data.get(31)));
		gameData.setBattleshipAmmo(Integer.parseInt(data.get(32)));
		gameData.setSubmarineAmmo(Integer.parseInt(data.get(33)));
		gameData.setDestroyerAmmo(Integer.parseInt(data.get(34)));
		gameData.setBoatAmmo(Integer.parseInt(data.get(35)));
		gameData.setShipReplacements(Integer.parseInt(data.get(48)));		*/
	}
	
	public void run()
	{
		String response = null;
		int counter = 0;
		nd = new NetDaemon(isServer, gameData, this, c);
		nd.setHost(hostName);
		nd.setPort(portNumber);
		if(!isServer) connected = nd.establishConnection();
		else connected = true;
		nd.start();
		while(!nd.isReady()) 
		{
			if(connected)
			{
				response = null;
				if(!isServer) response = nd.getGameDataFromServer();
				if(response != null) break;
			}
		}
		if(isServer)
		{
			dispose();
			MainWindow m = new MainWindow(gameData, nd);
			new Thread(m).start();
		}
		if(!isServer) 
		{
			System.out.println("Server returned: " + response);
			prepareGameData(response);
			nd.setGameData(gameData);
			dispose();
			cs = new ChooseSide(nd, gameData);
			new Thread(cs).start();
		}
	}
}