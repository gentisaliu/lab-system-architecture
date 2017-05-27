import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ShipList {
	
	protected int height,width,lineHeight;
	private int lBorder,bBorder,slen;
	protected BufferedImage s2,s3,s4,s5,border;
	protected View view;
	protected ImageManager iManage;
	protected Font font;
	protected Game game;
	protected String[] shipCount;
	protected boolean opponent;

	public ShipList (int width, int height,View view,boolean opponent) {
		this.width = width;
		this.height = height;
		this.lineHeight = height/2;
		this.view = view;
		this.lBorder = (int)((3./4.)*lineHeight);
		this.bBorder = (int)((1./4.)*lineHeight);
		this.iManage = view.imageManager;
		this.font = new Font("Arial",Font.PLAIN,(int)((3./4.)*lineHeight));	
		this.game = view.game;
		this.opponent = opponent;
		this.shipCount = new String[4];

		border = iManage.getScaledImage("bar3.gif", width,height, false);
		
		s2 = iManage.getScaledImage("submarine.gif",lineHeight *2,lineHeight ,false);
		s3 = iManage.getScaledImage("destroyer.gif",lineHeight *3,lineHeight ,false);
		s4 = iManage.getScaledImage("battleship.gif",lineHeight *4,lineHeight ,false);
		s5 = iManage.getScaledImage("aircraft.gif",lineHeight *5,lineHeight ,false);
	}

	protected void readPlayerNumbers() {
		shipCount[0] = ((Integer)game.getMySubmarines()).toString();
		shipCount[1] = ((Integer)game.getMyDestroyers()).toString();
		shipCount[2] = ((Integer)game.getMyBattleships()).toString();
		shipCount[3] = ((Integer)game.getMyAircraftCarriers()).toString();;
		}
	protected void readOpponentNumbers() {
		shipCount[0] = ((Integer)game.getEnemySubmarines()).toString();
		shipCount[1] = ((Integer)game.getEnemyDestroyers()).toString();
		shipCount[2] = ((Integer)game.getEnemyBattleships()).toString();
		shipCount[3] = ((Integer)game.getEnemyAircraftCarriers()).toString();
}
	
	public void redraw(Graphics2D g) {
		g.setColor(new Color(170,202,209));
		g.fillRect(0,0,width,height);
		g.drawImage(border,0,0,null);
		//write num of ships
		g.setColor(Color.black);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		
		if (opponent) {
			readOpponentNumbers();
		} else {
			readPlayerNumbers();
		}
		//first line
		slen = fm.stringWidth(shipCount[0]);
		g.drawString(shipCount[0],slen/2,lineHeight - bBorder);
		slen = fm.stringWidth(shipCount[3]);
		g.drawString(shipCount[3],slen/2+(int)((1./3.)*width),lineHeight - bBorder);
		//2nd line
		slen = fm.stringWidth(shipCount[1]);
		g.drawString(shipCount[1],slen/2,height-bBorder);
		slen = fm.stringWidth(shipCount[2]);
		g.drawString(shipCount[2],slen/2+(int)((4./9.)*width),height-bBorder);
		
		//first line
		g.drawImage(s2,lBorder,0,null);
		g.drawImage(s5,lBorder+(int)((1./3.)*width),0,null);
		//2nd line
		g.drawImage(s3,lBorder,lineHeight,null);
		g.drawImage(s4,lBorder+(int)((4./9.)*width),lineHeight,null);
	}

	
	public void handleMouseEvent(int x, int y) {
		if (y < lineHeight) {
			if(x < (1./3.)*width) {
				if (opponent && game.getEnemySubmarines() > 0 || game.getMySubmarines() > 0)
					view.enqueEvent(new CustomEvent(CustomEvent.VIEW_START_PLACING_SHIP, "submarine", 2*view.player.cellWidth,view.player.cellHeight,view.placeRotated));
			} else {
				if (opponent && game.getEnemyAircraftCarriers() > 0 || game.getMyAircraftCarriers() > 0 )
					view.enqueEvent(new CustomEvent(CustomEvent.VIEW_START_PLACING_SHIP, "aircraft", 5*view.player.cellWidth,view.player.cellHeight,view.placeRotated));
			}
		}
		else {
				if(x < (4./9.)*width) {
					if (opponent && game.getMyDestroyers() >0 || game.getMyDestroyers() > 0 )
						view.enqueEvent(new CustomEvent(CustomEvent.VIEW_START_PLACING_SHIP, "destroyer", 3*view.player.cellWidth,view.player.cellHeight,view.placeRotated));
				} else {
					if (opponent && game.getEnemyBattleships() > 0 || game.getMyBattleships() > 0)
						view.enqueEvent(new CustomEvent(CustomEvent.VIEW_START_PLACING_SHIP, "battleship", 4*view.player.cellWidth,view.player.cellHeight,view.placeRotated));					
				}
			}
		}
	
}
