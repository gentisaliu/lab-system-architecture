import java.net.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class NetDaemon extends Thread
{	
	private String hostName;
	private int clients;
	private int clientsJoined;
	private ArrayList<ServerService> services;
	private BufferedReader responseFromServer;
	private PrintWriter requestToServer;
	private Socket clientSocket;
	private ServerSocket sSocket;
	private int portNr;
	private int players;
	private boolean shouldRun;
	private boolean isServer;
	private boolean ready;
	private Game gameData;
	private ConnectionInProgress cip;
	private String serverGameData;
	private Controller cont;
	private ArrayList<String> playersServerSide;
	private ArrayList<String> playersOpponentSide;
	private String opl;
	private String spl;
	private boolean opponentResponse;
	private boolean serverResponse;
	private ChooseSide cs;
	private MainWindow mw;
	
	public NetDaemon(boolean isServer, Game gameData, ConnectionInProgress cip, Controller cont)
	{
		setDaemon(true);
		playersServerSide = new ArrayList<String>();
		opponentResponse = false;
		serverResponse = false;
		ready = false;
		this.isServer = isServer;
		this.gameData = gameData;
		this.cont = cont;
		if(isServer) clients = cont.getGuestPlayers() + cont.getHostPlayers() - 1;
		clientsJoined = 0;
		players = clients;
		this.cip = cip;
		setPriority(Thread.MIN_PRIORITY);
		services = new ArrayList<ServerService>();
		responseFromServer = null;
		requestToServer = null;
		sSocket = null;
		mw = null;
		shouldRun = true;
	}
	
	public void run()
	{
		if(isServer)
		{
			try
			{
				sSocket = new ServerSocket(portNr);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			while(shouldRun)
			{
				try
				{
					Socket s = sSocket.accept();
					ServerService c = new ServerService(s, gameData, this, cont);
					services.add(c);
					c.start();
					clientsJoined++;
					System.out.println(clientsJoined + " " + cont.getHostPlayers() + " " + cont.getGuestPlayers());
					if(clientsJoined == cont.getHostPlayers() + cont.getGuestPlayers() - 1) 
					{
						System.out.println("ready");
						ready = true;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			for(ServerService a : services) a.setShouldRun(false);
		}
		else
		{
			while(shouldRun)
			{
				try
				{
					String response = responseFromServer.readLine();
					if(response.startsWith("spl")) 
					{
						spl = response.substring(3);
						serverResponse = true;
					}
					if(response.startsWith("opl"))
					{
						opponentResponse = true;
						opl = response.substring(3);
					}
					if(response.startsWith("gamechallenge")) serverGameData = response;
					if(response.equals("sidefull")) cs.playerNotAdded();
					if(response.equals("playeradded")) cs.playerAdded();
					if(response.startsWith("chat")) 
					{
						parseChatMessage(response);
					}
					if(response.startsWith("water")) 
					{
						waterResponse(response);
					}
					if(response.startsWith("hit")) 
					{
						System.out.println("ship shot");
						hitResponse(response);
					}
					if(response.startsWith("shipSuccessfullyPlaced"))
					{
						shipPlacedResponse(response);
					}
					if(response.startsWith("destroyed")) 
					{
						destroyedResponse(response);
					}
					if(response.equals("endGame"))
					{
						closeDown();
					}
					if(response.startsWith("allShipsDestroyed")) allShipsDestroyed(response);
					response = null;
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void waterResponse(String response)
	{
		StringTokenizer st = new StringTokenizer(response);
		String side = null;
		int k = 0;
		int x = -1;
		int y = -1;
		while(st.hasMoreTokens())
		{
			if(k == 0) st.nextToken();
			if(k == 1) side = st.nextToken();
			if(k == 2) x = Integer.parseInt(st.nextToken());
			if(k == 3) y = Integer.parseInt(st.nextToken());
			k++;
		}
		 gameData.shootAtField(side, x, y, 2, false, null, null, -1, -1);
	}
	
	public void hitResponse(String response)
	{
		StringTokenizer st = new StringTokenizer(response);
		int k = 0;
		String side = null;
		int x = -1;
		int y = -1;
		while(st.hasMoreTokens())
		{
			if(k == 0) st.nextToken();
			if(k == 1) side = st.nextToken();
			if(k == 2) x = Integer.parseInt(st.nextToken());
			if(k == 3) y = Integer.parseInt(st.nextToken());
			k++;
		}
		System.out.println(gameData.getPlayerSide());
		System.out.println(side);
		if(gameData.getPlayerSide().equals(side)) 
		{
			System.out.println("player field hit");
			gameData.shootAtField(side, x, y, 3, false, null, null, -1, -1);
			mw.v.enqueEvent(new CustomEvent(CustomEvent.PLAYER_FIELD_HIT,x,y));
		}
		else  
		{
			System.out.println("enemy field hit");
			gameData.shootAtField(side, x, y, 3, false, null, null, -1, -1);
			mw.v.enqueEvent(new CustomEvent(CustomEvent.OPPONENT_FIELD_HIT,x,y));
		}
	}
	
	public void destroyedResponse(String response)
	{
		StringTokenizer st = new StringTokenizer(response);
		String side = null;
		String shipName = null;
		String alignment = null;
		int size = 0;
		int k = 0;
		int xStartCoord = -1;
		int yStartCoord = -1;
		int hostPoints = -1;
		int guestPoints = -1;
		int x = -1;
		int y = -1;
		while(st.hasMoreTokens())
		{
			if(k == 0) st.nextToken();
			if(k == 1) side = st.nextToken();
			if(k == 2) shipName = st.nextToken();
			if(k == 3) alignment = st.nextToken();
			if(k == 4) xStartCoord = Integer.parseInt(st.nextToken());
			if(k == 5) yStartCoord = Integer.parseInt(st.nextToken());
			if(k == 6) hostPoints = Integer.parseInt(st.nextToken());
			if(k == 7) guestPoints = Integer.parseInt(st.nextToken());
			if(k == 8) x = Integer.parseInt(st.nextToken());
			if(k == 9) y = Integer.parseInt(st.nextToken());
			k++;
		}
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(gameData.getPlayerSide().equals("guest")) 
		{
			gameData.setMyPoints(guestPoints);
			gameData.setEnemyPoints(hostPoints);
		}
		else 
		{
			gameData.setMyPoints(hostPoints);
			gameData.setEnemyPoints(guestPoints);
		}
		gameData.shootAtField(side, x, y, 3, true, shipName, alignment, xStartCoord, yStartCoord);
	    mw.v.enqueEvent(new CustomEvent(CustomEvent.SHIP_EXPLODE,xStartCoord,yStartCoord,size,alignment,side));
	}
	
	public void allShipsDestroyed(String response)
	{
		StringTokenizer st = new StringTokenizer(response);
		st.nextToken();
		String result;
		if((st.nextToken().equals(gameData.getPlayerSide()))) result = "You Lose";
		else result = "You Win!";
		mw.v.enqueEvent(new CustomEvent(CustomEvent.GAME_OVER, result));
		try
		{
			Thread.sleep(10000);
		}
		catch(InterruptedException e){ }
		System.exit(0);
	}
	
	public void setMainWindow(MainWindow mw)
	{
		this.mw = mw;
	}
	
	public void setSideChooser(ChooseSide cs)
	{
		this.cs = cs;
	}
	
	public void addOpponentSidePlayers(String name)
	{
		requestToServer.println("aosp" + name);
	}
	
	public void addServerSidePlayers(String name)
	{
		requestToServer.println("assp" + name);
	}
	
	public String[] getOpponentSidePlayers()
	{
		if(!isServer) 
		{
			requestToServer.println("opponentSidePlayerList");
			while(!opponentResponse) { }
			if(opl == null) return new String[1];
		}
		if(isServer) opl = cont.getOpponentSidePlayerList();
		playersOpponentSide = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(opl);
		while(st.hasMoreTokens()) playersOpponentSide.add(st.nextToken());
		String[] t = new String[playersOpponentSide.size()];
		for(int l = 0; l < playersOpponentSide.size(); l++) t[l] = playersOpponentSide.get(l);
		opponentResponse = false;
		return t;
	}
	
	public String[] getServerSidePlayers()
	{
		if(!isServer) 
		{
			requestToServer.println("serverSidePlayerList");
			while(!serverResponse) { }
			playersServerSide = new ArrayList<String>();
			if(spl == null) return new String[1];
		}
		if(isServer) spl = cont.getServerSidePlayerList();
		StringTokenizer st = new StringTokenizer(spl);
		while(st.hasMoreTokens()) playersServerSide.add(st.nextToken());
		String[] t = new String[playersServerSide.size()];
		for(int l = 0; l < playersServerSide.size(); l++) t[l] = playersServerSide.get(l);
		serverResponse = false;
		return t;
	}
		
	// method is called from a client (non-server) when a certain GUI event is released
	
	public void shoot(int x, int y)
	{
		if(!isServer) requestToServer.println("fire " + gameData.getPlayerSide() + " " + 
				new Integer(x).toString() + " " + new Integer(y).toString());
		if(isServer) elaborateShootRequest(x, y, "host");
	}
	
	// method is called only if NetDaemon is server
	
	public void elaborateShootRequest(int x, int y, String side)
	{
		String controllerQuery = null;
		if(side.equals("guest")) 
		{
			controllerQuery = cont.shootAtHostSide(x, y);
		}
		if(side.equals("host")) 
		{
			controllerQuery = cont.shootAtGuestSide(x, y);
		}
		for(ServerService s: services)
		{
			s.sendMessageToClients(controllerQuery);
		}
		if(controllerQuery.startsWith("water")) waterResponse(controllerQuery);
		if(controllerQuery.startsWith("hit")) hitResponse(controllerQuery);
		if(controllerQuery.startsWith("destroyed")) destroyedResponse(controllerQuery);
		if(controllerQuery.startsWith("allShipsDestroyed")) allShipsDestroyed(controllerQuery);
	}
	
	public void shipPlacedResponse(String response)
	{
		int i = 0;
		StringTokenizer st = new StringTokenizer(response);
		String field = null;
		String alignment = null;
		String shipName = null;
		int xStartCoordinate = -1;
		int yStartCoordinate = -1;
		while(st.hasMoreTokens())
		{
			if(i == 0) st.nextToken();
			if(i == 1) field = st.nextToken();
			if(i == 2) alignment = st.nextToken();
			if(i == 3) shipName = st.nextToken();
			if(i == 4) xStartCoordinate = Integer.parseInt(st.nextToken());
			if(i == 5) yStartCoordinate = Integer.parseInt(st.nextToken());
			i++;
		}
		if(field.equals(gameData.getPlayerSide())) gameData.placeShipsOnField(alignment, shipName, xStartCoordinate, yStartCoordinate);
	}
	
	public void elaboratePlaceRequest(String field, String alignment, String shipName, int xStartCoordinate, int yStartCoordinate)
	{
		boolean controllerResponse = false;
		controllerResponse = cont.addShip(shipName, alignment, field, xStartCoordinate, yStartCoordinate);
		if(controllerResponse)
		{
			for(ServerService s: services)
			{
				s.sendMessageToClients("shipSuccessfullyPlaced " + field + " " + alignment + " " + shipName + " " 
						+ new Integer(xStartCoordinate).toString() + " " + new Integer(yStartCoordinate).toString());
			}
		}
	}
	
	public boolean placeShipsOnField(String field, String alignment, String shipName, int xStartCoordinate, int yStartCoordinate)
	{
		if(!gameData.shipAlreadyPlaced(shipName, alignment, xStartCoordinate, yStartCoordinate))
		{
			if(!isServer) 
			{
				requestToServer.println("placeShip " + field + " " + alignment + " " + shipName + " " + xStartCoordinate + " " + yStartCoordinate);
				return true;
			}
			if(isServer) 
			{
				boolean controllerQuery = cont.addShip(shipName, alignment, field, xStartCoordinate, yStartCoordinate);
				if(controllerQuery) 
				{
					gameData.placeShipsOnField(alignment, shipName, xStartCoordinate, yStartCoordinate);
					for(ServerService s: services)
					{
						s.sendMessageToClients("placeShip " + field + " " + alignment + " " + shipName + " " + xStartCoordinate + " " + yStartCoordinate);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void endGame()
	{
		System.exit(0);
	}
	
	public void sendMessageToAllClients(String message)
	{
		for(ServerService s: services) s.sendMessageToClients(message);
	}
	
	public void chat(String message)
	{
		message = message.substring(5);
		if(isServer) 
		{
			for(ServerService s: services) s.sendMessageToClients("chat " + gameData.getPlayerName() + " " + gameData.getPlayerSide() + " " + message);
			parseChatMessage("chat " + gameData.getPlayerName() + " " + gameData.getPlayerSide() + " " + message);
		}
		if(!isServer) requestToServer.println("chat " + gameData.getPlayerName() + " " + gameData.getPlayerSide() + " " + message);
	}
	
	public void parseChatMessage(String response)
	{
		int counter = 0;
		String side = null;
		String playerName = null;
		String chatMessage = null;
		response = response.substring(5);
		StringTokenizer st = new StringTokenizer(response);
		while(st.hasMoreElements())
		{
			if(counter > 1) break;
			if(counter == 0) playerName = st.nextToken();
			if(counter == 1) side = st.nextToken();
			counter++;
		}
		chatMessage = response.substring(playerName.length() + side.length() + 2);
		if(playerName.equals(gameData.getPlayerName())) playerName = "Me";
		String finalString = playerName + " (" + side + ") says: " + chatMessage;
		mw.v.enqueEvent(new CustomEvent(CustomEvent.CHAT_MESSAGE,finalString));
	}
	
	public String getGameDataFromServer()
	{
		return serverGameData;
	}
	
	public int getClientsJoined()
	{
		return clientsJoined;
	}
	
	public int getNumberOfPlayers()
	{
		return players;
	}
	
	public void setHost(String hostName)
	{
		this.hostName = hostName;
	}
	
	public void setPort(int portNr)
	{
		this.portNr = portNr;
	}
	
	public boolean establishConnection()
	{
		try
		{
			clientSocket = new Socket(hostName, portNr);
			responseFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			requestToServer = new PrintWriter(clientSocket.getOutputStream(), true);
			requestToServer.println("requestGameData");
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Host or port do not exist.", "Connection error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public boolean isReady()
	{
		return ready;
	}
	
	public void setShouldRun(boolean shouldRun)
	{
		this.shouldRun = shouldRun;
	}
	
	public void setGameData(Game gameData)
	{
		this.gameData = gameData;
	}
	
	public void closeDown()
	{
		try
		{
			if(!isServer) clientSocket.close();
			else
			{
				for(ServerService s: services) s.closeDown();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
