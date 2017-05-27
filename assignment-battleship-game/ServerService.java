import java.net.*;
import java.io.*;
import java.util.*;

public class ServerService extends Thread
{
	private Socket sock;
	private boolean shouldRun;
	private PrintWriter responseToClient;
	private BufferedReader requestFromClient;
	private Game gameData;
	private NetDaemon nd;
	private Controller c;
	private boolean playerAdded;
	
	public ServerService(Socket s, Game gameData, NetDaemon nd, Controller c)
	{
		shouldRun = true;
		this.gameData = gameData;
		this.c = c;
		this.nd = nd;
		try
		{
			sock = s;
			responseToClient = new PrintWriter(sock.getOutputStream(), true);
			requestFromClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setShouldRun(boolean shouldRun)
	{
		this.shouldRun = shouldRun;
	}
	
	public void closeDown()
	{
		try
		{
			responseToClient.println("endGame");
			sock.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
		
	public void run()
	{
		String response;
		while(shouldRun)
		{
			try
			{
				response = requestFromClient.readLine();
				if(response.equals("requestGameData"))
				{
					responseToClient.println("gamechallenge: Game Edition: " + c.getGameEdition() + " Grid size: " + c.getGridX() + " x " + 
						c.getGridY() + " Aircrafts, Battleships, Submarines, Destroyers, PT-boats " + c.getAircraftCarriers() + " " 
						+ c.getBattleships() + " " + c.getSubmarines() + " " + c.getDestroyers() + " " +
						c.getPtBoats() + " Maximal shots: " + c.getMaximalShots() + " Unlimited Ammo: " + c.usesInfiniteAmmo()
						+ " Aircraft, Battleship, Submarine, Destroyer, PT-boat Ammo: " + c.getAircraftCarrierAmmo() + " " + 
						c.getBattleshipAmmo() + " " + c.getSubmarineAmmo() + " " + c.getDestroyerAmmo() + " " + c.getPtBoatAmmo() + 
						 " Timed Game: " + c.isGameTimed() + " Game Time: " + c.getGameTime() + " Use Blitzkrieg Strategy: " + 
						 c.usingBlitzkriegStrategy() + " Ship relocationing: " + c.getShipRelocations() + 
						" Enemy players: " + c.getGuestPlayers() + " Ally players: " + c.getHostPlayers());
				}
				if(response.equals("serverSidePlayerList")) responseToClient.println("spl" + c.getServerSidePlayerList());
				if(response.equals("opponentSidePlayerList")) responseToClient.println("opl" + c.getOpponentSidePlayerList());
				if(response.equals("endGame")) 
				{
					responseToClient.println("endGame");
					nd.closeDown();
				}
				if(response.startsWith("fire"))
				{
					StringTokenizer st = new StringTokenizer(response);
					String side = null;
					int xCoord = -1;
					int yCoord = -1;
					int i = 0;
					while(st.hasMoreTokens())
					{
						if(i == 0) st.nextToken();
						if(i == 1) side = st.nextToken();
						if(i == 2) xCoord = Integer.parseInt(st.nextToken());
						if(i == 3) yCoord = Integer.parseInt(st.nextToken());
						i++;
					}
					nd.elaborateShootRequest(xCoord, yCoord, side);
				}
				if(response.startsWith("placeShip"))
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
					nd.elaboratePlaceRequest(field, alignment, shipName, xStartCoordinate, yStartCoordinate);
				}
				if(response.startsWith("aosp")) 
				{
					playerAdded = c.addPlayerToOpponentSide(response.substring(4));
					if(!playerAdded) responseToClient.println("sidefull");
					else responseToClient.println("playeradded");
				}
				if(response.startsWith("assp")) 
				{
					playerAdded = c.addPlayerToServerSide(response.substring(4));
					if(!playerAdded) responseToClient.println("sidefull");
					else responseToClient.println("playeradded");
				}
				if(response.startsWith("chat")) 
				{
					nd.sendMessageToAllClients(response);
					nd.parseChatMessage(response);
				}
				if(response.equals("maxShotsExceeded")) { }
				if(response.equals("allShipsDestroyed")) { }
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessageToClients(String message)
	{
		responseToClient.println(message);
	}
	
	public void chat(String message)
	{
		responseToClient.println("chat " + message);
	}
}