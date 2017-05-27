import java.util.*;

public class Game
{
	private String playerName;
	private String side;
	private ArrayList<String[]> myShipsMap;
	private ArrayList<String[]> destroyedEnemyShipsMap;
	private ArrayList<String[]> myDestroyedShipsMap;
	private int[][] myShipsField;
	private int[][] enemyField;
	private int gridX;
	private int gridY;
	private boolean unlimitedAmmo;
	private boolean timedGame;
	private String gameTime;
	private int shipRelocations;
	private int myPoints;
	private int enemyPoints;
	private int maximalShots;
	private int myShots;
	private int enemyShots;
	private String chatMessage;
	private int chatFlag;
	public boolean canPlay;
	
	/*
	 * my fleet
	 */
	
	private int myAircraftCarriers;
	private int mac;
	private int myBattleships;
	private int mbs;
	private int mySubmarines;
	private int msm;
	private int myDestroyers;
	private int md;
	private int myBoats;
	private int myDestroyedAircraftCarriers;
	private int myDestroyedBattleships;
	private int myDestroyedSubmarines;
	private int myDestroyedDestroyers;
	
	/*
	 * enemy fleet
	 */
	
	private int enemyAircraftCarriers;
	private int enemyBattleships;
	private int enemySubmarines;
	private int enemyDestroyers;
	private int enemyBoats;
	
	/*
	 * my ammo
	 */
	
	private int myAircraftAmmo;
	private int myBattleshipAmmo;
	private int mySubmarineAmmo;
	private int myDestroyerAmmo;
	private int maca;
	private int mbsa;
	private int msma;
	private int mda;
	
	public Game(String playerName, int gridX, int gridY)
	{
		this.playerName = playerName;
		myAircraftAmmo = 0;
		myBattleshipAmmo = 0;
		myDestroyerAmmo = 0;
		mySubmarineAmmo = 0;
		myDestroyedAircraftCarriers = 0;
		myDestroyedBattleships = 0;
		myDestroyedSubmarines = 0;
		myDestroyedDestroyers = 0;
		chatMessage = null;
		myShots = 0;
		enemyShots = 0;
		this.gridX = gridX;
		this.gridY = gridY;
		myShipsField = new int[gridX][gridY];
		enemyField = new int[gridX][gridY];
		myShipsMap = new ArrayList<String[]>();
		destroyedEnemyShipsMap = new ArrayList<String[]>();
		myDestroyedShipsMap = new ArrayList<String[]>();
		myPoints = 0;
		canPlay = false;
		enemyPoints = 0;
		initializeGameFields();
	}
	
	public boolean canPlay()
	{
		return canPlay;
	}
	
	public int getMyPoints()
	{
		return myPoints;
	}
	
	public void setGameTimed(boolean gt)
	{
		timedGame = gt;
	}
	
	public boolean isGameTimed()
	{
		return timedGame;
	}
	
	public void setGameTime(String t)
	{
		gameTime = t;
	}
	
	public String getGameTime()
	{
		return gameTime;
	}
	
	public void setMaximalShots(int shots)
	{
		maximalShots = shots;
		myShots = shots;
		enemyShots = shots;
	}
	
	public int getMaximalShots()
	{
		return maximalShots;
	}
	
	public int getMyShots()
	{
		return myShots;
	}
	
	public int getEnemyShots()
	{
		return enemyShots;
	}
	
	public void setMyPoints(int myPoints)
	{
		this.myPoints = myPoints;
	}
	
	public int getEnemyPoints()
	{
		return enemyPoints;
	}
	
	public void setEnemyPoints(int enemyPoints)
	{
		this.enemyPoints = enemyPoints;
	}
	
	public void setShipRelocations(int relocations)
	{
		this.shipRelocations = relocations;
	}
	
	public int getShipRelocations()
	{
		return shipRelocations;
	}
	
	public ArrayList<String[]> getMyDestroyedShipsMap()
	{
		return myDestroyedShipsMap;
	}
	
	public int[][] getMyShipsField()
	{
		return myShipsField;
	}
	
	public int[][] getEnemyShipsField()
	{
		return enemyField;
	}
	
	public ArrayList<String[]> getMyShipsMap()
	{
		return myShipsMap;
	}
	
	public ArrayList<String[]> getEnemyMap()
	{
		return destroyedEnemyShipsMap;
	}
	
	
	public int getMyAircraftAmmo()
	{
		return myAircraftAmmo;
	}
	
	public int getMyBattleshipAmmo()
	{
		return myBattleshipAmmo;
	}
	
	public int getMySubmarineAmmo()
	{
		return mySubmarineAmmo;
	}
	
	public int getMyDestroyerAmmo()
	{
		return myDestroyerAmmo;
	}
	
	public void useUnlimitedAmmo(boolean ua)
	{
		this.unlimitedAmmo = ua;
	}
	
	public boolean usesUnlimitedAmmo()
	{
		return unlimitedAmmo;
	}
	
	public void setDestroyers(int destroyers, int ammo)
	{
		this.myDestroyers = destroyers;
		this.md = destroyers;
		this.enemyDestroyers = destroyers;
		if(!unlimitedAmmo) 
		{
			this.mda = ammo;
			myDestroyerAmmo = destroyers * ammo;
		}
		else myDestroyerAmmo = -1;
	}
	
	public int getMyDestroyers()
	{
		return myDestroyers;
	}
	
	public int getEnemyDestroyers()
	{
		return enemyDestroyers;
	}
	
	public void setSubmarines(int submarines, int ammo)
	{
		this.mySubmarines = submarines;
		this.msm = submarines;
		this.enemySubmarines = submarines;
		if(!unlimitedAmmo) 
		{
			mySubmarineAmmo = ammo * submarines;
			this.msma = ammo;
		}
		else mySubmarineAmmo = -1;
	}
	
	public int getMySubmarines()
	{
		return mySubmarines;
	}
	
	public int getEnemySubmarines()
	{
		return enemySubmarines;
	}
	
	public void setBattleships(int battleships, int ammo)
	{
		this.myBattleships = battleships;
		this.mbs = battleships;
		this.enemyBattleships = battleships;
		if(!unlimitedAmmo) 
		{
			this.mbsa = ammo;
			myBattleshipAmmo = ammo * battleships;
		}
		else myBattleshipAmmo = -1;
	}
	
	public int getMyBattleships()
	{
		return myBattleships;
	}
	
	public int getEnemyBattleships()
	{
		return enemyBattleships;
	}
	
	public void setAircraftCarriers(int aircrafts, int ammo)
	{
		this.myAircraftCarriers = aircrafts;
		this.mac = aircrafts;
		this.enemyAircraftCarriers = aircrafts;
		if(!unlimitedAmmo) 
		{
			this.maca = ammo;
			myAircraftAmmo = aircrafts * ammo;
		}
		else myAircraftAmmo = -1;
	}
	
	public int getMyAircraftCarriers()
	{
		return myAircraftCarriers;
	}
	
	public int getEnemyAircraftCarriers()
	{
		return enemyAircraftCarriers;
	}
	
	public void initializeGameFields()
	{
		for(int i = 0; i < gridX; i++)
		{
			for(int j = 0; j < gridY; j++)
			{
				myShipsField[i][j] = 0;
				enemyField[i][j] = 0;
			}
		}
	}
	
	public int getGridX()
	{
		return gridX;
	}
	
	public int getGridY()
	{
		return gridY;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public void setPlayerSide(String side)
	{
		this.side = side;
	}
	
	public String getPlayerSide()
	{
		return side;
	}
	
	public void shootAtField(String field, int xCoordinate, int yCoordinate, int shotResult, boolean destroyed, String shipName, String alignment, int xStartCoordinate, int yStartCoordinate)
	{
		if(field.equals(side))
		{
			myShipsField[xCoordinate][yCoordinate] = shotResult;
			myShots++;
		}
		else
		{
			if(!unlimitedAmmo)
			{
				boolean shipAmmoChanged = false;
				while(!shipAmmoChanged)
				{
					int randomNumber = (int) (4*Math.random());
					switch(randomNumber)
					{
						case 0: if(mac - myDestroyedAircraftCarriers > 0) { myAircraftAmmo--; shipAmmoChanged = true; } break;
						case 1: if(mbs - myDestroyedBattleships > 0) { myBattleshipAmmo--; shipAmmoChanged = true; } break;
						case 2: if(msm - myDestroyedSubmarines > 0) { mySubmarineAmmo--; shipAmmoChanged = true; } break;
						case 3: if(md - myDestroyedDestroyers > 0) { myDestroyerAmmo--; shipAmmoChanged = true; } break;
					}
				}
			}
			canPlay = true;
			enemyField[xCoordinate][yCoordinate] = shotResult;
			enemyShots++;
		}
		if(destroyed)
		{
			String[] shipDestroyed;
			int size = 0;
			if(shipName.equals("aircraft")) 
			{
				if(field.equals(side) && !unlimitedAmmo)
				{
					myAircraftAmmo -=  1/(mac - myDestroyedAircraftCarriers)*myAircraftAmmo; 
					myDestroyedAircraftCarriers++;
				}
				size = 5;
			}
			if(shipName.equals("battleship")) 
			{
				if(field.equals(side) && !unlimitedAmmo)
				{
					myBattleshipAmmo -= 1/(mbs - myDestroyedBattleships) * myBattleshipAmmo;
					myDestroyedBattleships++;
				}
				size = 4;
			}
			if(shipName.equals("destroyer")) 
			{
				if(field.equals(side) && !unlimitedAmmo)
				{
					myDestroyerAmmo -= 1/(md - myDestroyedDestroyers) * myDestroyerAmmo;
					myDestroyedDestroyers++;
				}
				size = 3;
			}
			if(shipName.equals("submarine"))
			{
				if(field.equals(side) && !unlimitedAmmo)
				{
					mySubmarineAmmo -= 1/(msm - myDestroyedSubmarines) * mySubmarineAmmo;
					myDestroyedSubmarines++;
				}
				size = 2;
			}
			shipDestroyed = new String[size + 1];
			shipDestroyed[0] = shipName;
			if(alignment.equals("horizontal")) for(int l = 0; l < size; l++) shipDestroyed[l + 1] = new Integer(xStartCoordinate + l).toString() + "x" + new Integer(yStartCoordinate).toString();
			if(alignment.equals("vertical")) for(int l = 0; l < size; l++) shipDestroyed[l + 1] = new Integer(xStartCoordinate).toString() + "x" + new Integer(yStartCoordinate + l).toString();
			if(!field.equals(side)) destroyedEnemyShipsMap.add(shipDestroyed);
			if(field.equals(side)) myDestroyedShipsMap.add(shipDestroyed);
		}
	}
	
	public String[] getShipRepresentation(int x, int y)
	{
		if(myShipsField[x][y] != 1) return null;
		for(String[] theShips: myShipsMap)
		{
			for(int c = 1; c < theShips.length; c++) if((new Integer(x).toString() + "x" + new Integer(y).toString()).equals(theShips[c])) return theShips;
		}
		return null;
	}
	
	public boolean deleteShip(String shipName, String alignment, int xCoordinate, int yCoordinate)
	{
		int size = 0;
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(shipName.equals("boat")) size = 2;
		if(shipAlreadyPlaced(shipName, alignment, xCoordinate, yCoordinate))
		{
			int count = 0;
			for(String[] c : myShipsMap)
			{
				if(c[1].startsWith(new Integer(xCoordinate).toString()) && c[1].endsWith(new Integer(yCoordinate).toString())) 
				{
					myShipsMap.remove(count);
					break;
				}
				count++;
			}
			if(alignment.equals("horizontal"))
			{
				for(int i = 0; i < size; i++) if(myShipsField[xCoordinate + i][yCoordinate] == 1) myShipsField[xCoordinate + i][yCoordinate] = 0;
			}
			if(alignment.equals("vertical"))
			{
				for(int i = 0; i < size; i++) if(myShipsField[xCoordinate][yCoordinate + i] == 1) myShipsField[xCoordinate][yCoordinate + i] = 0;
			}
			return true;
		}
		return false;
	}
	
	public boolean shipAlreadyPlaced(String shipName, String alignment, int xCoordinate, int yCoordinate)
	{
		int size = 0;
		if(shipName.equals("aircraft")) size = 5;
		if(shipName.equals("battleship")) size = 4;
		if(shipName.equals("destroyer")) size = 3;
		if(shipName.equals("submarine")) size = 2;
		if(shipName.equals("boat")) size = 2;
		if(alignment.equals("horizontal")) 
		{
			if(xCoordinate + size - 1 >= gridX || yCoordinate > gridY - 1) return true;
			for(int i = 0; i < size; i++) if(myShipsField[xCoordinate + i][yCoordinate] == 1 || myShipsField[xCoordinate + i][yCoordinate] == 2 || myShipsField[xCoordinate + i][yCoordinate] == 3) return true;
		}
		if(alignment.equals("vertical")) 
		{
			if(yCoordinate + size - 1 >= gridY || xCoordinate > gridX - 1) return true;
			for(int i = 0; i < size; i++)if(myShipsField[xCoordinate][yCoordinate + i] == 1 || myShipsField[xCoordinate][yCoordinate + i] == 2 || myShipsField[xCoordinate][yCoordinate + i] == 3) return true;
		}
		return false;
	}
	
	public synchronized boolean placeShipsOnField(String alignment, String shipName, int xStartCoordinate, int yStartCoordinate)
	{
		if(!shipAlreadyPlaced(shipName, alignment, xStartCoordinate, yStartCoordinate)) 
		{
			int size = 0;
			if(shipName.equals("aircraft")) 
			{
				myAircraftCarriers--;
				size = 5;
			}
			if(shipName.equals("battleship"))
			{
				myBattleships--;
				size = 4;
			}
			if(shipName.equals("destroyer"))
			{
				myDestroyers--;
				size = 3;
			}
			if(shipName.equals("submarine")) 
			{
				mySubmarines--;
				size = 2;
			}
			if(shipName.equals("boat")) 
			{
				myBoats--;
				size = 2;
			}
			String[] newShip = new String[size + 1];
			newShip[0] = shipName;
			if(alignment.equals("horizontal")) 
			{
				for(int i = 0; i < size; i++) 
				{
					myShipsField[xStartCoordinate + i][yStartCoordinate] = 1;
					newShip[i + 1] = (new Integer(xStartCoordinate + i)).toString() + "x" + (new Integer(yStartCoordinate)).toString();
				}
				myShipsMap.add(newShip);
				return true;
			}
			if(alignment.equals("vertical")) 
			{	
				for(int i = 0; i < size; i++) 
				{
					myShipsField[xStartCoordinate][yStartCoordinate + i] = 1;
					newShip[i + 1] = (new Integer(xStartCoordinate)).toString() + "x" + (new Integer(yStartCoordinate + i)).toString();
				}
				myShipsMap.add(newShip);
				return true;
			}
		}
		return false;
	}
}