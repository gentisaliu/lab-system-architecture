import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//--------------------------------------------------
// represent a ship
//-------------------------------------------------- 
public class Ship 
{
	protected BufferedImage image;
	protected int xpos,ypos,cellWidth,cellHeight,width,height;
	protected int length;
	protected View view;
	protected ImageManager iManage;
	protected String name = "unkown";
	protected boolean vertical;
	
	public Ship (String[] ship,int cellWidth,int cellHeight,View view) {
		
		this.view = view;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		parseShipString(ship);
		this.iManage = view.imageManager;
				
		act();
	}

	public void parseShipString (String [] ship) {
		name = ship[0];
		xpos = Integer.parseInt(ship[1].split("x")[0]);
		ypos = Integer.parseInt(ship[1].split("x")[1]);
//		System.out.println("xpos:"+xpos);
//		System.out.println("ypos:"+ypos);
//		System.out.println("cellWidth: "+cellWidth+", cellHeight:"+cellHeight);
//		System.out.println("xpos:"+xpos);
//		System.out.println("ypos:"+ypos);
		length = ship.length - 1;
//	/	System.out.println(ship[0]+":"+ship[1]+","+ship[2]);
//		System.out.println(xpos+"|"+Integer.parseInt(ship[2].split("x")[0]));
		if (xpos ==  Integer.parseInt(ship[2].split("x")[0]) ) {
			vertical = true;
		} else { 
			vertical = false;
		}
		xpos*=cellWidth;
		ypos*=cellHeight;

	}

	public void sayHello() {
		System.out.println("hi, im a"+name+" and im located at"+xpos+","+ypos);
	}
	//--------------------------------------------------
	// swim up and down little ship :-)
	//-------------------------------------------------- 
	public void act()
	{
		width=length*cellWidth;
		height=cellHeight;
		
		image = iManage.getScaledImage(name+".gif",width,height,vertical);
	}
	//--------------------------------------------------
	// show what you've got
	//-------------------------------------------------- 
	public void paint(Graphics2D g) 
	{
		g.drawImage(image,xpos,ypos,null);
	}
}
