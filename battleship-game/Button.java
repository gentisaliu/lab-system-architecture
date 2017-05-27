import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;


public class Button {
	protected int width,height;
	protected View view;
	protected Font font;
	protected String text;

	public Button (int width, int height, View view) {
        this.width = width;
        this.height = height;
        this.view = view;
        this.font = new Font("Arial",Font.PLAIN,height/2);
		this.text = "Cancel";
	}

	public Button( int width, int height, View view, String text) {
		this.width = width;
		this.height = height;
		this.view = view;
		this.font = new Font("Arial",Font.PLAIN,height/2);
		this.text = text;
	}
	
	public void redraw(Graphics2D g) {
		g.setColor(new Color(60,162,230));
		g.fillRoundRect(0,0,width,height,height/5,height/5);
		
		g.setColor(Color.lightGray);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int slen = fm.stringWidth(text);
		g.drawString(text,(width-slen)/2,(int)(0.75 * height));
	}
	
	public void handleEvent() {
		;
	}
}
