import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.LinkedList;

public class ChatView {
	protected int width,height,lines,lineHeight,border;
	protected Font font;
	protected LinkedList<String> text;
	public ChatView (int width,int height, int lines) {
		this.width = width;
		this.height = height;
		this.lines = lines;
		this.border = 2;
		this.font = new Font("Arial",Font.PLAIN,12);
		this.lineHeight = (height -2*border)/lines;
		text = new LinkedList<String>();
	}

	public void addText(String string) {
		if (text.size() >= lines)
			text.removeFirst();
		text.addLast(string);
	}
	
	public void redraw(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.darkGray);
		g.fillRect(0,0,width,height);
		g.setColor(Color.white);
		int y = border+lineHeight;
		for(String s : text){
			g.drawString(s,border,y);
			y+= lineHeight;
			}
	}
	
	
}
