import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;

//--------------------------------------------------
// show shots,statistics,chat etc
//-------------------------------------------------- 
public class BattleField
{
	private static final long serialVersionUID = 1L;
	public static final int TYPE_PLAYER=0;
	public static final int TYPE_OPPONENT=1;
	private int height=400,width=400;
	protected Graphics g;
	protected BufferedImage whiteCell,redCell,background; 
	private int type;
	protected int cellWidth, cellHeight;
	private int[][] cellStates;
	protected View view;
	protected Game game;
	protected FontMetrics fm;
	private int hCells,vCells;

	protected ArrayList<String[]> myShipsMap = null;
	protected LinkedList<Ship> ships = new LinkedList<Ship>();
	private ImageManager iManage;
	protected Animation explosion,smoke;
	protected LinkedList<Animation.Animator> animations = new LinkedList<Animation.Animator>();
	
	public BattleField(int width, int height, View view,int type) {
		this.view = view;
		this.iManage = view.imageManager;
		this.type = type;
		this.game = view.game;
		this.width= width;
		this.height = height;
		this.hCells = game.getGridX();
		this.vCells = game.getGridY();
		this.cellWidth = width/hCells;
		this.cellHeight = height/vCells;
		if(type == TYPE_PLAYER) {
			cellStates = game.getMyShipsField();
			this.myShipsMap = game.getMyShipsMap();
		} else {
			cellStates = game.getEnemyShipsField();
			this.myShipsMap = game.getEnemyMap();
		}
		explosion = new Animation ("explode",".gif",17,iManage,cellWidth,cellHeight);
		smoke = new Animation ("smoke/s", ".gif",13,iManage,10,10);
		prepareImages();  
		
	}
	
	public void update() {
		
		ships.clear();
		for (int i=0; i< myShipsMap.size(); i++) {
			ships.add(new Ship(myShipsMap.get(i), cellWidth, cellHeight, view));
		}	
		for( Ship s : ships)
			s.act();
	}
	
	public void redraw(Graphics2D g) {
		g.drawImage(background,0,0,null);
		drawCells(g);

		writeAxis(g);

		for (Ship s : ships)
			s.paint(g);
		
		Iterator it = animations.listIterator();
		Animation.Animator a;
		while (it.hasNext()) {
			a = (Animation.Animator)it.next();
			BufferedImage img = a.nextImage();
			if (img != null)
				g.drawImage(img,a.x,a.y,null);
			else it.remove();
		}
	}
	
	public void writeAxis(Graphics2D g) {
		Font font = new Font("Arial",Font.PLAIN,cellHeight/4);
		g.setFont(font);
		fm =  g.getFontMetrics();
		g.setColor(Color.LIGHT_GRAY);
		for(int i=0; i<hCells;i++) {
			g.drawString(""+(i+1),cellWidth*i + (cellWidth)*9/10-fm.stringWidth(""+(i+1)),height-3);
		}
		for(int i=0; i<vCells;i++) {
			g.drawString(""+(char)(i+65),3,width - cellHeight*i - cellHeight*3/4);
		}
	}
	
	
	protected void prepareImages() {
		whiteCell = new BufferedImage(cellWidth-1,cellHeight-1,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = whiteCell.createGraphics();
		
		g.setColor(Color.blue); //FIXME if we got textured background: move it and draw the spec part here
		g.fillRect(0,0,whiteCell.getWidth(),whiteCell.getHeight());
		g.setColor(Color.white);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
		g.fillRect(0,0,whiteCell.getWidth(),whiteCell.getHeight());
		
		redCell = new BufferedImage(cellWidth-1,cellHeight-1,BufferedImage.TYPE_INT_RGB);
		g = redCell.createGraphics();

		g.setColor(Color.blue); 
		g.fillRect(0,0,redCell.getWidth(),redCell.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
		g.setColor(Color.orange);
		g.fillRect(0,0,redCell.getWidth(),redCell.getHeight());
		
		background = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		g = background.createGraphics();
		g.setColor(Color.blue);
		g.fillRect(0,0,width,height);
		g.setColor(Color.white);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.5f));
		for(int i=0;i<width;i++)
			g.drawLine(i*cellWidth,0,i*cellWidth,height); 
		for(int i=0;i<height;i++)
			g.drawLine(0,i*cellHeight,width,i*cellWidth); 
		g.drawLine(0,height-1 , width ,height-1);
		g.drawLine(width-1 , 0, width-1 ,height);

	}
	void drawCells(Graphics2D g) {
		for (int i=0; i<hCells; i++) {
			for (int j=0; j<vCells; j++) {
				switch(cellStates[i][j]) {
				case 2: 
					g.drawImage(whiteCell,i*cellWidth+1, j*cellHeight+1,null);
					break;
				case 3:
					g.drawImage(redCell,i*cellWidth+1, j*cellHeight+1,null);
					break;
				}
			}
		}
	}


//	process a mouse clicked event from view
	public void handleMouseEvent(int x, int y) {
		int hCell,vCell;
	
		hCell = (int) (x/cellWidth);
		vCell = (int) (y/cellHeight);
		if (hCell < hCells && vCell < vCells)
			fieldClickedEvent(hCell,vCell);
	}
	
	public void hitAt(int hCell, int vCell) {
		//if theres something //depending on cellstate:
		explode(hCell,vCell);
		//else do show watersplash
	}
	
	public void explode(int hCell, int vCell) {
		animations.add(explosion.getAnimator(hCell*cellWidth,vCell*cellHeight));
		
	}
	public void smokeShip() {
		animations.add(smoke.getAnimator(cellWidth/4, cellHeight/4)); //let some ship smoke
	}
	
	public void fieldClickedEvent(int hCell,int vCell) {
		view.enqueEvent(new CustomEvent((this.type == TYPE_PLAYER? CustomEvent.PLAYER_FIELD_CLICKED : CustomEvent.OPPONENT_FIELD_CLICKED ),hCell,vCell));
	}
	
	
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}	
}

