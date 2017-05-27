import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.net.URL;
import javax.swing.JPanel;


//--------------------------------------------------
// visually represent battlefield + status bar, 
// mouse listener, allow ship placement ,
// send events to controller
// receive events from controller
//-------------------------------------------------- 

public class View extends JPanel implements MouseListener
{
	private static final long serialVersionUID = 1L;
	
	//external
	public ImageManager imageManager;
	protected NetDaemon nd;
	protected Game game;

	
	//components
	protected BattleField player,opponent;
	protected ChatView chat;
	protected ShipList PShipList, OShipList;
	protected Button[] buttons;
	//component format
	private int playerX,playerY;
	private int opponentX,opponentY;
	private int chatX,chatY;
	private int chatHeight,chatWidth;
	private int shipListWidth,shipListHeight;
	private int PShipListX, PShipListY,OShipListX,OShipListY;
	private int bfHeight,bfWidth;
	private int width,height,border;
	private int buttonY;
	private int[] buttonX;
	//placing mode vars
	public boolean placeRotated = false;
	public String shipToPlace = new String();
	public String[] shipToMove;
	protected boolean placing = false;
	protected boolean moving = false;
	private MouseMotionListener shipListener;
	private Point mousePointer = new Point(0,0);
	private BufferedImage mouseImage;
	
	//double buffering	
	protected BufferedImage buffer;
	//misc
	private LinkedList<CustomEvent> events = new LinkedList<CustomEvent>();
	private CustomTimer timer;
	protected Boolean gameOver = false;	
	protected String gameOverMessage;
	protected String[] ammoString;
	//--------------------------------------------------
	// care about mouseinput, add statusbar+battlefield
	//-------------------------------------------------- 
	public View(Game g,NetDaemon nd)
	{
		super(true);
		width= 810;
		border = 10;
		height = 647;
		bfHeight = 400;
		bfWidth = 400;
		
		
		shipListWidth = bfWidth;
		shipListHeight = (int)(shipListWidth*(2./9.));
		PShipListX =  0;
		PShipListY = 2*border;
		
		OShipListX = shipListWidth+border;
		OShipListY = PShipListY;
				
		playerY = PShipListY+shipListHeight;
		playerX = 0;
		
		opponentY = OShipListY+shipListHeight;
		opponentX = playerX + bfWidth + border;
	
		
		buttonY = playerY + bfHeight + border;
		chatY = buttonY + 50 +3*border;
		chatX = 0;
		chatWidth = width;
		chatHeight = 50;
		//-------------
		this.game = g;
		this.nd = nd;

		imageManager = new ImageManager("img/"); 
		
		
		PShipList = new ShipList(shipListWidth,shipListHeight,this,false);
		OShipList = new ShipList(shipListWidth,shipListHeight ,this,true);
		
		player = new BattleField(bfWidth,bfHeight,this,BattleField.TYPE_PLAYER); 
		opponent = new BattleField(bfWidth,bfHeight,this,BattleField.TYPE_OPPONENT); 
		
		if (game.isGameTimed()) {
			this.timer = new CustomTimer(game.getGameTime(),false);
		} else {
			this.timer = new CustomTimer("00:00",true);
		}
			

		buttons = new Button[6];
		buttonX = new int[6];
		
		buttonX[0] = 10;
		buttons[0] = new Button(100,50,this);
		buttonX[1] = 130;
		buttons[1] = new Timer(100,50,this);
		buttonX[2] = 250;
		buttons[2] = new Button(75,50,this,"Quit");
		buttonX[3] = (width-100)/2;
		buttons[3] = new ShotsButton(100,50,this); 
		buttonX[4] = buttonX[3]+130;
		buttons[4] = new Scores(150,50,this,false,nd.getOpponentSidePlayers());
		buttonX[5] = buttonX[4]+170;
		buttons[5] = new Scores(150,50,this,true,nd.getServerSidePlayers());
		chat = new ChatView(chatWidth,chatHeight,4);
		buffer =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		addMouseListener(this);

		timer.start();
		
		ammoString = new String[5];
		ammoString[0] = "Ammunition";
	}
	


	public void explodeSound() {
		try {
		java.applet.Applet.newAudioClip(new URL("file://"+System.getProperty("user.dir")+
					"/sounds/explode.wav")).play();
		} catch (Throwable e) {}
	}

    private void explodeShip(int x, int y, int size , String alignment ,String side) {
        BattleField bf;

        if(side.equals(game.getPlayerSide())) {
            bf = player;
        } else {
            bf = opponent;
        }

        explodeSound();
		if (alignment.equals("horizontal")) {
			for(int i=0; i<size ;i++)
				bf.hitAt(x+i,y);
		} else { //vertical
			for (int i=0;i<size;i++)
				bf.hitAt(x,y+i);
		}

    }
	//--------------------------------------------------
	// controller can send events that way eg	 explosions
	//-------------------------------------------------- 
	public void enqueEvent(CustomEvent e) 
	{
		events.addLast(e);
	}
	
	public void processEvents() 
	{
		while(!events.isEmpty()) {
			CustomEvent e = events.removeFirst();

			switch (e.getType()) {
			case CustomEvent.VIEW_START_PLACING_SHIP:
				if (!moving) {
					placeShip((String)e.getArg(0),(Integer)e.getArg(1),(Integer)e.getArg(2),(Boolean)e.getArg(3));
					placing = true;
				}
				break;
			case CustomEvent.OPPONENT_FIELD_CLICKED:
				nd.shoot((Integer)e.getArg(0),(Integer)e.getArg(1));
				break;
			case CustomEvent.SHIP_FIRED:
				player.smokeShip();
				break;
			case CustomEvent.SHIP_EXPLODE:
				explodeShip((Integer)e.getArg(0),(Integer)e.getArg(1),(Integer)e.getArg(2),(String)e.getArg(3),(String)e.getArg(4));
				break;
			case CustomEvent.GAME_OVER:
				gameOver = true;
				gameOverMessage = (String)e.getArg(0);			
				timer.stop();
				break;
			case CustomEvent.SHIP_PLACED:
				cancelPlaceShip();
				break;	
			case CustomEvent.CHAT_MESSAGE:
				chat.addText((String)e.getArg(0));
				break;
			case CustomEvent.PLAYER_FIELD_CLICKED:
				int x =  (Integer)e.getArg(0);
				int y = (Integer)e.getArg(1);
				
				if(placing == true) {
					if (nd.placeShipsOnField(game.getPlayerSide(),(placeRotated ? "vertical" : "horizontal"), shipToPlace,x,y)) {
						cancelPlaceShip();
					}
				} else if (moving == true) {
					game.deleteShip(shipToMove[0], 
							( shipToMove[1].split("x")[0].equals( shipToMove[2].split("x")[0] )? "vertical" : "horizontal")
							, Integer.parseInt(shipToMove[1].split("x")[0]), Integer.parseInt(shipToMove[1].split("x")[1]));
					if (!game.shipAlreadyPlaced(shipToMove[0],
							( placeRotated ? "vertical" : "horizontal"),x,y))
					{
						nd.placeShipsOnField(game.getPlayerSide(),(placeRotated ? "vertical" : "horizontal"),shipToMove[0],x,y);
						cancelPlaceShip();
					} else {
						
						game.placeShipsOnField((shipToMove[1].split("x")[0].equals( shipToMove[2].split("x")[0] )? "vertical" : "horizontal"),
								shipToMove[0],Integer.parseInt(shipToMove[1].split("x")[0]), Integer.parseInt(shipToMove[1].split("x")[1]));
						
					}
				} else if (game.getShipRelocations() > 0 && game.getShipRepresentation(x,y) != null) {
					shipToMove = game.getShipRepresentation(x,y);
					placeRotated = ( shipToMove[1].split("x")[0].equals(shipToMove[2].split("x")[0]) ? true : false ); 
					placeShip(shipToMove[0], (shipToMove.length - 1) * player.cellWidth, player.cellHeight, placeRotated );
					moving = true;
				}
				break;
			case CustomEvent.OPPONENT_FIELD_HIT:
				explodeSound();
				opponent.hitAt((Integer)e.getArg(0),(Integer)e.getArg(1));
				break;
			case CustomEvent.PLAYER_FIELD_HIT:
				explodeSound();
				player.hitAt((Integer)e.getArg(0),(Integer)e.getArg(1));
				break;
			default:
				System.out.println("Unknown event: "+e);	

			}
		}
	}

//	do nothing
	public void paintComponent(Graphics2D g) {
		;
	}
	//--------------------------------------------------
	// redraw statusbar + battlefield
	//-------------------------------------------------- 
	public void redraw() 
	{
		Graphics2D g = (Graphics2D)buffer.getGraphics();
		
		g.setColor(new Color(87,95,130));
		g.fillRect(0,0,width,height);
		
		drawTeamNames(g);

		g.translate(PShipListX,PShipListY);
		PShipList.redraw(g);
		g.translate(-PShipListX,-PShipListY);
		
		g.translate(OShipListX,OShipListY);
		OShipList.redraw(g);
		g.translate(-OShipListX,-OShipListY);
		
		g.translate(playerX, playerY);
		player.redraw(g);
		g.translate(-playerX, -playerY);
		
		g.translate(opponentX, opponentY);
		opponent.redraw(g);
		g.translate(-opponentX, -opponentY);
		
		for(int i=0;i<6;i++) {
			g.translate(buttonX[i],buttonY);
			buttons[i].redraw(g);
			g.translate(-buttonX[i],-buttonY);
		}

		g.translate(0,buttonY+50);
		drawAmmo(g);
		g.translate(0,-(buttonY + 50));

		g.translate(chatX,chatY);
		chat.redraw(g);
		g.translate(-chatX, -chatY);
		


		if (placing == true || moving == true) {
			double x = Math.min(mouseImage.getWidth(), mouseImage.getHeight());
			g.translate(-x/2,-x/2);
			g.drawImage(mouseImage,mousePointer.x,mousePointer.y,null);
		}
		if(gameOver)
			drawEndMessage(g);
		
		getGraphics().drawImage(buffer,0,0,null);
		
		
	}
	

    public Dimension getMinimumSize() 
    {
        return new Dimension(width,height);
    }
    
    public Dimension getPreferredSize() 
    {
        return new Dimension(width,height);
    }

    public void placeShip(String name,int width,int height,boolean rotate) {
    	if (placing || moving)
    		this.removeMouseMotionListener(shipListener);
    	
    	mouseImage = imageManager.getScaledImage(name+".gif",width,height,rotate);
    	shipToPlace = name;
    	placeRotated = rotate;
        shipListener = new MouseMotionListener() {
        	{}	
        	public void mouseMoved(MouseEvent e) {
        		mousePointer.setLocation(new Point(e.getX(),e.getY()));
        	}
        	public void mouseDragged(MouseEvent e) {}
        };
        this.addMouseMotionListener(shipListener);
    }
    
    public void rotatePlacing() {
    	if (!moving && !placing)
    		return;
    	
    	placeRotated = !placeRotated;
    	if (!placeRotated) { //weve been rotated before so we need to swap x and y 
    		mouseImage = imageManager.getScaledImage(shipToPlace+".gif",mouseImage.getHeight(),mouseImage.getWidth(),placeRotated); 
    	} else {
    	mouseImage = imageManager.getScaledImage(shipToPlace+".gif",mouseImage.getWidth(),mouseImage.getHeight(),placeRotated);
    	}
    }
    
    public void cancelPlaceShip() {
    	this.removeMouseMotionListener(shipListener);
    	moving = placing = false;
    }
    
	public void mouseClicked(MouseEvent e) {
		if (gameOver)
			return;
		int x = e.getX();
		int y = e.getY();
		mousePointer = new Point(x,y); 
		switch(e.getButton()) {
			case 1:
				if (y > PShipListY  && y <  PShipListY + shipListHeight &&
						x > PShipListX &&  x < PShipListX + shipListWidth) 
				{
						PShipList.handleMouseEvent(x - PShipListX, y - PShipListY);
				} 
				else if (y > playerY && y < playerY + bfHeight &&
						x > playerX && x < playerX + bfWidth) 
				{
					player.handleMouseEvent(x - playerX, y - playerY);
				}			
				else if( y > opponentY && y < opponentY + bfWidth &&
						x > opponentX && x < opponentX + bfWidth &&
						placing == false && moving == false )
				{
					opponent.handleMouseEvent(x-opponentX, y-opponentY);
				} else if (y > buttonY && y < buttonY + 50) {
				 if ( x > buttonX[0] && x < buttonX[0]+100 ) {
					cancelPlaceShip();
				} else if ( x > buttonX[2] && x < buttonX[2]+100)
					nd.endGame();

				}
				
				break;
			default:
				if (placing == true || moving == true)
					rotatePlacing();
//				cancel placement
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	
	//--------------------------------------------------
	// process events (eg add an explosion to a bf)
	// update from model data
	// redraw everything
	// sleep (constant frame rate !)
	//-------------------------------------------------- 
	public void gameLoop()
	{
		while (true) {
			updateWorld();
			redraw();
			try { Thread.sleep(30);
			} catch (InterruptedException e) {}
		}
	}

	public void exit() {
		System.exit(0);
	}	
	private void updateWorld() {
		processEvents();
		player.update();
		opponent.update();
	}

	private void drawEndMessage(Graphics2D g) {
	Font font = new Font("Arial",Font.PLAIN,(int)(50));
	g.setColor(Color.white);
	g.setFont(font);
	FontMetrics fm = g.getFontMetrics();
	int slen = fm.stringWidth(gameOverMessage);
	g.drawString(gameOverMessage, (width-slen)/2,(int)(height/2));
	}

	private void drawTeamNames (Graphics2D g) {
		String left,right;
		Font font = new Font("Arial",Font.PLAIN,(int)((3./4.)*border*2));
		if (game.getPlayerSide().equals("host")) {
			left = "Host side ("+game.getPlayerName()+") - "+game.getMyPoints()+" points";
			right = "Guest side - "+game.getEnemyPoints()+" points";
		} else {
			left = "Guest side ("+game.getPlayerName()+") - "+game.getMyPoints()+" points";
			right = "Host side - "+game.getEnemyPoints()+" points";
		}
		g.setColor(Color.lightGray);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int slen = fm.stringWidth(left);
		g.drawString(left,(width/2-slen)/2,(int)((7./8.)*2*border ));
		slen = fm.stringWidth(right);
		g.drawString(right,width/2+(width/2-slen)/2,(int)((7./8.)*2*border ));
	

	}
	private void drawAmmo(Graphics2D g) {
		ammoString[1] = "Submarines: "+(game.getMySubmarineAmmo() != -1 ? game.getMySubmarineAmmo()  : "Inf");
		ammoString[2] = "Destroyers: "+(game.getMyDestroyerAmmo() != -1 ? game.getMyDestroyerAmmo() :  "Inf" );
		ammoString[3] = "Battleships: "+(game.getMyBattleshipAmmo() != -1 ? game.getMyBattleshipAmmo() : "Inf");
		ammoString[4] = "Aircraft carriers :"+(game.getMyAircraftAmmo() != -1 ? game.getMyAircraftAmmo() : "Inf");
		Font font = new Font("Arial",Font.PLAIN,(int)(1.5*border));
		g.setFont(font);
		g.setColor(Color.lightGray);

		FontMetrics fm = g.getFontMetrics();
		int slen;
		for(int i = 0; i< 5; i++) { 
			slen = fm.stringWidth(ammoString[i]);
			g.drawString(ammoString[i],(int)((0.2*width*i + (0.2*width-slen)/2)),(int)(0.75*3*border));
		}
	}


	
	protected class Scores extends Button {
		protected int lines;
		protected Game game;
		protected String[] players;
		protected String side;
		public Scores(int width,int height, View view,boolean host,String[] players) {
			super(width,height,view);
			this.game = view.game;
			this.players = players;
			this.lines = this.players.length+2;
			this.font = new Font("Arial",Font.PLAIN,(int)((1./lines)*height));
			if(host)
				side = "Host side:";
			else 
				side = "Guest side:";
		}
		
		public void redraw(Graphics2D g) {
			g.setColor(new Color(60,162,230));
			g.fillRoundRect(0,0,width,height,height/5,height/5);
			
			g.setColor(Color.lightGray);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int slen = fm.stringWidth(side);
			g.drawString(side,(width-slen)/2,(int)((1./lines)*height));
			double i = 2;
			for (String s : players) {
				slen = fm.stringWidth(s);
				g.drawString(s,(width-slen)/2,(int)((i/lines)*height));
				i++;
			}
			
			
		}
		
	}

	protected class Timer extends Button {
		protected NumberFormat nf;

		public Timer ( int width,int height, View view) {
			super (width,height,view);
			nf = NumberFormat.getInstance();
			nf.setMinimumIntegerDigits(2);
			this.font = new Font("Arial",Font.PLAIN,height/2);
		}
		public void redraw(Graphics2D g) {

				g.setColor(new Color(60,162,230));
				g.fillRoundRect(0,0,width,height,height/5,height/5);
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();
	            int slen = fm.stringWidth(view.timer.getCurrentTime());
	            g.setColor(Color.lightGray);
	            g.drawString(view.timer.getCurrentTime(),(width-slen)/2,(int)(0.75 * height));
		}
	}
	
	protected class ShotsButton extends Button{
		protected Game game;
		public ShotsButton(int width,int height, View view) {
			super(width,height,view);
			this.game = view.game;
			this.font = new Font("Arial",Font.PLAIN,(int)((1./3.)*height));
		}
		
		protected int getShotX() {
			int pos = game.getEnemyShots() - game.getMyShots();
			return (int)(width/2 + pos*((3*width/4.)/game.getMaximalShots())/2.);
		}



		public void redraw(Graphics2D g) {
			g.setColor(new Color(60,162,230));
			g.fillRoundRect(0,0,width,height,height/5,height/5);
			
			g.setColor(Color.lightGray);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			int slen = fm.stringWidth("Shot balance");
			g.drawString("Shot balance",(width-slen)/2,(int)((1./3.)*height));
		
			
			g.fillRect(width/8,height*2/3-1,3*width/4,2);
			g.fillOval(getShotX()-height/10, height*2/3-height/10, height/5, height/5);
		}
		
	}
	
}
