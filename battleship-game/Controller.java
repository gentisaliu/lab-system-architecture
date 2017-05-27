import java.util.*;

public class Controller
{
	private int i;
	private int j;
	
	/*
	 * data
	 */
	
	private String[] playersServerSide;
	private String[] playersOpponentSide;
	private int[][] hostSideGameField;
	private int[][] guestSideGameField;
	private ArrayList<String[]> hostShipsMap;
	private ArrayList<String[]> guestShipsMap;
	private int shipRelocationsHostSide;
	private int shipRelocationsGuestSide;
	private int hostMaximalShots;
	private int guestMaximalShots;
	private int shotsBalance;
	
	/*
	 * game properties
	 */
	
	private int gridX;
	private int gridY;
	private boolean infiniteAmmo;
	private String gameEdition;
	private boolean isGameTimed;
	private boolean blitzkrieg;
	private String gameTime;
	private int shipRelocations;
	private int maximalShots;
	private int guestPlayers;
	private int hostPlayers;
	private int guestPoints;
	private int hostPoints;
	private int destroyedGuestShips;
	private int destroyedHostShips;
	private int totalGuestShips;
	private int totalHostShips;
	
	/*
	 * general fleet attributes
	 */
	
	private int aircraftCarriers;
	private int battleships;
	private int submarines;
	private int destroyers;
	private int ptBoats;
	
	/*
	 * general ammo attributes
	 */
	
	private int aircraftCarrierAmmo;
	private int battleshipAmmo;
	private int submarineAmmo;
	private int destroyerAmmo;
	private int ptboatAmmo;
	
	/*
	 * host fleet state
	 */
	
	private int hostAircraftsCarriers;
	private int hostBattleships;
	private int hostSubmarines;
	private int hostDestroyers;
	private int hostPtBoats;
	
	/*
	 * guest fleet state
	 */
	
	private int guestAircraftCarriers;
	private int guestBattleships;
	private int guestSubmarines;
	private int guestDestroyers;
	private int guestPtBoats;
	
	/*
	 * host ammo state
	 */
	
	private int[] hostAircraftCarrierAmmo;
	private int[] hostBattleshipAmmo;
	private int[] hostSubmarineAmmo;
	private int[] hostDestroyerAmmo;
	private int[] hostPtBoatAmmo;
	
	/*
	 * guest ammo state
	 */
	
	private int[] guestAircraftCarrierAmmo;
	private int[] guestBattleshipAmmo;
	private int[] guestSubmarineAmmo;
	private int[] guestDestroyerAmmo;
	private int[] guestPtBoatAmmo;
	
	public Controller(int serverSidePlayers, int opponentSidePlayers, String hostPlayer, int shipRelocations, int gridX, int gridY)
	{
		destroyedGuestShips = 0;
		destroyedHostShips = 0;
		totalGuestShips = 0;
		totalHostShips = 0;
		hostShipsMap = new ArrayList<String[]>();
		guestShipsMap = new ArrayList<String[]>();
		shipRelocationsHostSide = shipRelocations;
		shipRelocationsGuestSide = shipRelocations;
		hostSideGameField = new int[gridX][gridY];
		guestSideGameField = new int[gridX][gridY];
		this.gridX = gridX;
		this.gridY = gridY;
		shotsBalance = 0;
		this.guestPlayers = opponentSidePlayers;
		this.hostPlayers = serverSidePlayers;
		playersServerSide = new String[serverSidePlayers + 1];
		playersServerSide[0] = hostPlayer;
		playersOpponentSide = new String[opponentSidePlayers];
		playersOpponentSide[0] = "<None>";
		initializeGameFields();
		guestPoints = 0;
		hostPoints = 0;
		i = 1;
		j = 1;
	}
	
	public int getGuestPoints()
	{
		return guestPoints;
	}
	
	public int getHostPoints()
	{
		return hostPoints;
	}
	
	public int getAircraftCarrierAmmo(String side, int aircraft)
	{
		if(side.equals("guestSide"))
		{
			if(aircraft >= guestAircraftCarrierAmmo.length) return -1;
			return guestAircraftCarrierAmmo[aircraft];
		}
		if(side.equals("hostSide"))
		{
			if(aircraft >= hostAircraftCarrierAmmo.length) return -1;
			return hostAircraftCarrierAmmo[aircraft];
		}
		return -1;
	}
	
	public int getBattleshipAmmo(String side, int battleship)
	{
		if(side.equals("guestSide"))
		{
			if(battleship >= guestBattleshipAmmo.length) return -1;
			return guestBattleshipAmmo[battleship];
		}
		if(side.equals("hostSide"))
		{
			if(battleship >= hostBattleshipAmmo.length) return -1;
			return hostBattleshipAmmo[battleship];
		}
		return -1;
	}
	
	public int getSubmarineAmmo(String side, int submarine)
	{
		if(side.equals("guestSide"))
		{
			if(submarine >= guestSubmarineAmmo.length) return -1;
			return guestSubmarineAmmo[submarine];
		}
		if(side.equals("hostSide"))
		{
			if(submarine >= hostSubmarineAmmo.length) return -1;
			return hostSubmarineAmmo[submarine];
		}
		return -1;
	}
	
	public int getDestroyerAmmo(String side, int destroyer)
	{
		if(side.equals("guestSide"))
		{
			if(destroyer >= guestDestroyerAmmo.length) return -1;
			return guestDestroyerAmmo[destroyer];
		}
		if(side.equals("hostSide"))
		{
			if(destroyer >= hostDestroyerAmmo.length) return -1;
			return hostDestroyerAmmo[destroyer];
		}
		return -1;
	}
	
	public int getBoatAmmo(String side, int boat)
	{
		if(side.equals("guestSide"))
		{
			if(boat >= guestPtBoatAmmo.length) return -1;
			return guestPtBoatAmmo[boat];
		}
		if(side.equals("hostSide"))
		{
			if(boat >= hostPtBoatAmmo.length) return -1;
			return hostPtBoatAmmo[boat];
		}
		return -1;
	}
	
	public int getGuestPlayers()
	{
		return guestPlayers;
	}
	
	public int getHostPlayers()
	{
		return hostPlayers;
	}
	
	public int getShipRelocations()
	{
		return shipRelocations;
	}
	
	public void useInfiniteAmmo(boolean ammo)
	{
		this.infiniteAmmo = ammo;
	}
	
	public boolean usesInfiniteAmmo()
	{
		return infiniteAmmo;
	}
	
	public int getGridX()
	{
		return gridX;
	}
	
	public int getGridY()
	{
		return gridY;
	}
	
	public void useBlitzkriegStrategy(boolean blitzkrieg)
	{
		this.blitzkrieg = blitzkrieg;
	}
	
	public boolean usingBlitzkriegStrategy()
	{
		return blitzkrieg;
	}
	
	public void setGameTimed(boolean timed)
	{
		this.isGameTimed = timed;
	}
	
	public void setGameTime(String time)
	{
		this.gameTime = time;
	}
	
	public String getGameTime()
	{
		return gameTime;
	}
	
	public boolean isGameTimed()
	{
		return isGameTimed;
	}
	
	public String getGameEdition()
	{
		return gameEdition;
	}
	
	public void setGameEdition(String edition)
	{
		this.gameEdition = edition;
	}
	
	public void setMaximalShots(int mshots)
	{
		hostMaximalShots = 0;
		guestMaximalShots = 0;
		maximalShots = mshots;
	}
	
	public int getMaximalShots()
	{
		return maximalShots;
	}
	
	public int getPtBoatAmmo()
	{
		return ptboatAmmo;
	}
	
	public void setPtBoatAmmo(int ammo)
	{
		this.ptboatAmmo = ammo;
	}
	
	public void setDestroyerAmmo(int ammo)
	{
		this.destroyerAmmo = ammo;
	}
	
	public int getDestroyerAmmo()
	{
		return destroyerAmmo;
	}
	
	public void setSubmarineAmmo(int ammo)
	{
		this.submarineAmmo = ammo;
	}
	
	public int getSubmarineAmmo()
	{
		return submarineAmmo;
	}
	
	public void setBattleshipAmmo(int ammo)
	{
		this.battleshipAmmo = ammo;
	}
	
	public int getBattleshipAmmo()
	{
		return battleshipAmmo;
	}
	
	public void setAircraftCarrierAmmo(int ammo)
	{
		this.aircraftCarrierAmmo = ammo;
	}
	
	public int getAircraftCarrierAmmo()
	{
		return aircraftCarrierAmmo;
	}
		
	public int getPtBoats()
	{
		return ptBoats;
	}
	
	public void setDestroyers(int destroyers, int initialize)
	{
		this.destroyers = destroyers;
		if(initialize == 1)
		{
			hostDestroyerAmmo = new int[destroyers];
			guestDestroyerAmmo = new int[destroyers];
			hostDestroyers = destroyers;
			guestDestroyers = destroyers;
			totalGuestShips += destroyers;
			totalHostShips += destroyers;
			for(int k = 0; k < destroyers; k++)
			{
				hostDestroyerAmmo[k] = destroyerAmmo;
				guestDestroyerAmmo[k] = destroyerAmmo;
			}
		}
	}
	
	public int getDestroyers()
	{
		return destroyers;
	}
	
	public void setSubmarines(int submarines, int initialize)
	{
		this.submarines = submarines;
		if(initialize == 1)
		{
			hostSubmarineAmmo = new int[submarines];
			guestSubmarineAmmo = new int[submarines];
			hostSubmarines = submarines;
			guestSubmarines = submarines;
			totalGuestShips += submarines;
			totalHostShips += submarines;
			for(int k = 0; k < submarines; k++)
			{
				hostSubmarineAmmo[k] = submarineAmmo;
				guestSubmarineAmmo[k] = submarineAmmo;
			}
		}
	}
	
	public int getSubmarines()
	{
		return submarines;
	}
	
	public void setBattleships(int battleships, int initialize)
	{
		this.battleships = battleships;
		if(initialize == 1)
		{
			hostBattleshipAmmo = new int[battleships];
			guestBattleshipAmmo = new int[battleships];
			hostBattleships = battleships;
			guestBattleships = battleships;
			totalGuestShips += battleships;
			totalHostShips += battleships;
			for(int k = 0; k < battleships; k++)
			{
				hostBattleshipAmmo[k] = battleshipAmmo;
				guestBattleshipAmmo[k] = battleshipAmmo;
			}
		}
	}
	
	public int getBattleships()
	{
		return battleships;
	}
	
	public void setAircraftCarriers(int aircrafts, int initialize)
	{
		this.aircraftCarriers = aircrafts;
		if(initialize == 1)
		{
			hostAircraftCarrierAmmo = new int[aircrafts];
			guestAircraftCarrierAmmo = new int[aircrafts];
			hostAircraftsCarriers = aircrafts;
			guestAircraftCarriers = aircrafts;
			totalGuestShips += aircrafts;
			totalHostShips += aircrafts;
			for(int k = 0; k < aircrafts; k++)
			{
				hostAircraftCarrierAmmo[k] = aircrafts;
				guestAircraftCarrierAmmo[k] = aircrafts;
			}
		}
	}
	
	public int getAircraftCarriers()
	{
		return aircraftCarriers;
	}
	
	public void initializeGameFields()
	{
		for(int i = 0; i < gridX; i++)
		{
			for(int j = 0; j < gridY; j++)
			{
				hostSideGameField[i][j] = 0;
				guestSideGameField[i][j] = 0;
			}
		}
	}
		
	public boolean shipDestroyed(String[] ships, String field)
	{
		for(int c = 1; c < ships.length; c++)
		{
			int l = 0;
			StringTokenizer st = new StringTokenizer(ships[c], "x");
			int[] coord = new int[2];
			while(st.hasMoreTokens())
			{
				coord[l] = Integer.parseInt(st.nextToken());
				l++;
			}
			if(field.equals("guest")) if(guestSideGameField[coord[0]][coord[1]] != 3) return false;
			if(field.equals("host")) if(hostSideGameField[coord[0]][coord[1]] != 3) return false;
		}
		return true;
	}
	
	public synchronized String shootAtHostSide(int x, int y)
	{
		if(shotsBalance > -maximalShots && readyToPlay())
		{
			if(hostSideGameField[x][y] == 1)
			{
				shotsBalance--;
				hostSideGameField[x][y] = 3;
				for(String[] ships : hostShipsMap)
				{
					for(int l = 1; l < ships.length; l++) 
					{
						if(ships[l].startsWith((new Integer(x)).toString()) && ships[l].endsWith((new Integer(y)).toString()))
						{
							int count = 0;
							String[] coordinates = new String[2];
							StringTokenizer st = new StringTokenizer(ships[1], "x");
							while(st.hasMoreTokens())
							{
								coordinates[count] = st.nextToken();
								count++;
							}
							if(shipDestroyed(ships, "host")) 
							{
								destroyedHostShips++;
								if(ships[0].equals("aircraft")) guestPoints += 2;
								if(ships[0].equals("battleship")) guestPoints += 4;
								if(ships[0].equals("destroyer")) guestPoints += 6;
								if(ships[0].equals("submarine")) guestPoints += 8;
								if(ships[0].equals("boat")) guestPoints += 8;
								if(destroyedHostShips == totalHostShips) 
								{
									System.out.println("all host ships destroyed");
									return "allShipsDestroyed host";
								}
								return ("destroyed host " + ships[0] + " " + determineAlignment(ships) + " " + coordinates[0] + " " + coordinates[1] + " " + new Integer(hostPoints).toString() + " " + new Integer(guestPoints).toString() + " " + new Integer(x).toString() + " " + new Integer(y).toString());
							}
							else return ("hit host "  + new Integer(x).toString() + " " + new Integer(y).toString());
						}
					}
				}
			}
			if(hostSideGameField[x][y] == 2) return ("alreadyHit host " + new Integer(x).toString() + " " + new Integer(y).toString());;
			if(hostSideGameField[x][y] == 0) 
			{
				hostSideGameField[x][y] = 2;
				shotsBalance--;
				return ("water host " + new Integer(x).toString() + " " + new Integer(y).toString());
			}
		}
		return "error";
	}
	
	public String determineAlignment(String[] ships)
	{
		StringTokenizer st = new StringTokenizer(ships[1], "x");
		StringTokenizer st1 = new StringTokenizer(ships[2], "x");
		if(st.nextToken().equals(st1.nextToken())) return "vertical";
		else return "horizontal";
	}
	
	public synchronized String shootAtGuestSide(int x, int y)
	{
		if(shotsBalance < maximalShots && readyToPlay())
		{
			if(guestSideGameField[x][y] == 1)
			{
				shotsBalance++;
				guestSideGameField[x][y] = 3;
				for(String[] ships : guestShipsMap)
				{
					for(int l = 1; l < ships.length; l++) 
					{
						if(ships[l].startsWith(new Integer(x).toString()) && ships[l].endsWith(new Integer(y).toString()))
						{
							int count = 0;
							String[] coordinates = new String[2];
							StringTokenizer st = new StringTokenizer(ships[1], "x");
							while(st.hasMoreTokens())
							{
								coordinates[count] = st.nextToken();
								count++;
							}
							if(shipDestroyed(ships, "guest"))
							{
								destroyedGuestShips++;
								if(ships[0].equals("aircraft")) hostPoints += 2;
								if(ships[0].equals("battleship")) hostPoints += 4;
								if(ships[0].equals("destroyer")) hostPoints += 6;
								if(ships[0].equals("submarine")) hostPoints += 8;
								if(ships[0].equals("boat")) hostPoints += 8;
								if(destroyedGuestShips == totalGuestShips) 
								{
									System.out.println("all guest ships destroyed");
									return "allShipsDestroyed guest";
								}
								return ("destroyed guest " + ships[0] + " " + determineAlignment(ships) + " " + coordinates[0] + " " + coordinates[1] + " " + new Integer(hostPoints).toString() + " " + new Integer(guestPoints).toString() + " " + new Integer(x).toString() + " " + new Integer(y).toString());
								}
							else return ("hit guest " + new Integer(x).toString() + " " + new Integer(y).toString());
						}
					}
				}
			}
			if(guestSideGameField[x][y] == 2) return ("alreadyHit guest" + new Integer(x).toString() + " " + new Integer(y).toString());
			if(guestSideGameField[x][y] == 0) 
			{
				guestSideGameField[x][y] = 2;
				shotsBalance++;
				return ("water guest " + new Integer(x).toString() + " " + new Integer(y).toString());
			}
		}
		return "error";
	}
	
	public boolean readyToPlay()
	{
		if(!blitzkrieg) return true;
		if(hostAircraftsCarriers == 0 && hostBattleships == 0 && hostDestroyers == 0 && hostSubmarines == 0 && guestAircraftCarriers == 0
			 && guestBattleships == 0 && guestDestroyers == 0 && guestSubmarines == 0) return true;
		return false;
	}
	
	public boolean fleetExceeded(String field, String sn)
	{
		if(field.equals("host"))
		{
			if(sn.equals("aircraft")) 
			{
				if(hostAircraftsCarriers == 0) return true;
				hostAircraftsCarriers--;
			}
			if(sn.equals("battleship")) 
			{
				if(hostBattleships == 0) return true;
				hostBattleships--;
			}
			if(sn.equals("destroyer")) 
			{
				if(hostDestroyers == 0) return true;
				hostDestroyers--;
			}
			if(sn.equals("submarine")) 
			{
				if(hostSubmarines == 0) return true;
				hostSubmarines--;
			}
			if(sn.equals("boat")) 
			{
				if(hostPtBoats == 0) return true;
				hostPtBoats--;
			}
		}
		if(field.equals("guest"))
		{
			if(sn.equals("aircraft")) 
			{
				if(guestAircraftCarriers == 0) return true;
				guestAircraftCarriers--;
			}
			if(sn.equals("battleship")) 
			{
				if(guestBattleships == 0) return true;
				guestBattleships--;
			}
			if(sn.equals("destroyer")) 
			{
				if(guestDestroyers == 0) return true;
				guestDestroyers--;
			}
			if(sn.equals("submarine")) 
			{
				if(guestSubmarines == 0) return true;
				guestSubmarines--;
			}
			if(sn.equals("boat")) 
			{
				if(guestPtBoats == 0) return true;
				guestPtBoats--;
			}
		}
		return false;
	}
	
	public synchronized boolean addShip(String shipName, String alignment, String field, int xCoordinate, int yCoordinate)
	{
		int size = 0;
		String[] newShip;
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(shipName.equals("boat")) size = 2;
		newShip = new String[size + 1];
		newShip[0] = shipName;
		if(!shipAlreadyPlaced(field, shipName, alignment, xCoordinate, yCoordinate) && !fleetExceeded(field, shipName))
		{
			if(alignment.equals("horizontal")) 
			{
				for(int i = 0; i < size; i++) 
				{
					if(field.equals("host")) hostSideGameField[xCoordinate + i][yCoordinate] = 1;
					if(field.equals("guest")) guestSideGameField[xCoordinate + i][yCoordinate] = 1;
					newShip[i + 1] = new Integer(xCoordinate + i).toString() + "x" + new Integer(yCoordinate).toString();
				}
			}
			if(alignment.equals("vertical")) 
			{
				for(int i = 0; i < size; i++) 
				{
					if(field.equals("host")) hostSideGameField[xCoordinate][yCoordinate + i] = 1;
					if(field.equals("guest")) guestSideGameField[xCoordinate][yCoordinate + i] = 1;
					newShip[i + 1] = (new Integer(xCoordinate)).toString() + "x" + (new Integer(yCoordinate + i)).toString();
				}
			}
			if(field.equals("host")) hostShipsMap.add(newShip);
			if(field.equals("guest")) guestShipsMap.add(newShip);
			return true;
		}
		return false;
	}
		
	public boolean deleteShip(String shipName, String alignment, String field, int xCoordinate, int yCoordinate)
	{
		int size = 0;
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(shipName.equals("boat")) size = 2;
		if(field.equals("friendlyField"))
		{
			int count = 0;
			for(String[] c : hostShipsMap)
			{
				if(c[1].startsWith(new Integer(xCoordinate).toString()) && c[1].endsWith(new Integer(yCoordinate).toString())) 
				{
					if(field.equals("host")) hostShipsMap.remove(count);
					if(field.equals("guest")) guestShipsMap.remove(count);
					break;
				}
				count++;
			}
			if(alignment.equals("horizontal"))
			{
				if(field.equals("host")) for(int i = 0; i < size; i++) if(hostSideGameField[xCoordinate + i][yCoordinate] == 1) hostSideGameField[xCoordinate + i][yCoordinate] = 0;
				if(field.equals("guest")) for(int i = 0; i < size; i++) if(guestSideGameField[xCoordinate + i][yCoordinate] == 1) guestSideGameField[xCoordinate + i][yCoordinate] = 0;
			}
			if(alignment.equals("vertical"))
			{
				if(field.equals("host")) for(int i = 0; i < size; i++) if(hostSideGameField[xCoordinate][yCoordinate + i] == 1) hostSideGameField[xCoordinate][yCoordinate + i] = 0;
				if(field.equals("guest")) for(int i = 0; i < size; i++) if(guestSideGameField[xCoordinate][yCoordinate + i] == 1) guestSideGameField[xCoordinate][yCoordinate + i] = 0;
			}
			return true;
		}
		return false;
	}
	
	public boolean shipAlreadyPlaced(String field, String shipName, String alignment, int xCoordinate, int yCoordinate)
	{
		int size = 0;
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(shipName.equals("boat")) size = 2;
		if(field.equals("guest"))
		{
			if(alignment.equals("horizontal")) 
			{
				if(xCoordinate + size - 1 >= guestSideGameField.length || yCoordinate >= guestSideGameField[xCoordinate].length) return true;
				for(int i = 0; i < size; i++) if(guestSideGameField[xCoordinate + i][yCoordinate] == 1 || guestSideGameField[xCoordinate + i][yCoordinate] == 2 || guestSideGameField[xCoordinate + i][yCoordinate] == 3) return true;
			}
			if(alignment.equals("vertical")) 
			{
				if(xCoordinate > guestSideGameField.length || yCoordinate + size - 1 >= guestSideGameField[xCoordinate].length) return true;
				for(int i = 0; i < size; i++) if(guestSideGameField[xCoordinate][yCoordinate + i] == 1 || guestSideGameField[xCoordinate][yCoordinate + i] == 2 || guestSideGameField[xCoordinate][yCoordinate + i] == 3) return true;
			}
		}
		if(field.equals("host"))
		{
			if(alignment.equals("horizontal")) 
			{
				if(xCoordinate + size - 1 >= hostSideGameField.length || yCoordinate >= hostSideGameField[xCoordinate].length) return true;
				for(int i = 0; i < size; i++) if(hostSideGameField[xCoordinate + i][yCoordinate] == 1 || hostSideGameField[xCoordinate + i][yCoordinate] == 2 || hostSideGameField[xCoordinate + i][yCoordinate] == 3) return true;
			}
			if(alignment.equals("vertical")) 
			{
				if(xCoordinate > hostSideGameField.length - 1 || yCoordinate + size - 1 >= hostSideGameField[xCoordinate].length) return true;
				for(int i = 0; i < size; i++) if(hostSideGameField[xCoordinate][yCoordinate + i] == 1 || hostSideGameField[xCoordinate][yCoordinate + i] == 2 || hostSideGameField[xCoordinate][yCoordinate + i] == 3) return true;
			}
		}
		return false;
	}
	
	public String getServerSidePlayerList()
	{
		StringBuffer b = new StringBuffer();
		for(int k = 0; k < i; k++) b.append(playersServerSide[k] + " ");
		return b.toString();
	}
	
	public String getOpponentSidePlayerList()
	{
		StringBuffer b = new StringBuffer();
		for(int k = 0; k < j; k++) b.append(playersOpponentSide[k] + " ");
		return b.toString();
	}
	
	public synchronized boolean addPlayerToServerSide(String playerName)
	{
		if(i == playersServerSide.length) return false;
		else playersServerSide[i] = playerName;
		i++;
		return true;
	}
	
	public synchronized boolean addPlayerToOpponentSide(String playerName)
	{
		if(playersOpponentSide[0].equals("<None>")) j = 0;
		if(j == playersOpponentSide.length) return false;
		else playersOpponentSide[j] = playerName;
		j++;
		return true;		
	}
}