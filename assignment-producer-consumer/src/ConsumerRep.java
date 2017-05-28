/*
 * Author: Raphael Kimmig
 * Description: Graphical Representation of a Consumer Thread
 * June 2007
 */

import java.awt.*;
import javax.swing.*;
class ConsumerRep extends JComponent
{
  	private Consumer cons;	// reference to the consumer
    private int sheight, slen;
    private String text;
    private Color c;

    // Class constructor
    
	public ConsumerRep(Consumer c) 
	{
		this.cons = c;
	}

    public void paint(Graphics g) 
    {
    	if(cons == null || cons.getThreadControlBlock().getDone()) // if consumer is done, set its label to done and its background color to gray
    	{
            text = "Consumer " + cons.getID() + ": done";
            c = Color.gray;
        } 
    	else if(cons.getThreadControlBlock().getOccupied()) // if consumer is active, set its label to consuming and its background color to green
    	{
            text = "Consumer " + cons.getID() + ": consuming " + (new Integer(cons.getConsumedProduct())).toString();
            c = Color.green;
        } 
    	else  // if consumer is waiting, set its label to waiting and its background color to red
        {
            text = "Consumer " + cons.getID() + ": waiting";
            c = Color.red;
        }

        g.setColor(c);	// set background
        g.fillRect(0, 0, getSize().width, getSize().height);	// draw a representation area of the consumer thread

        FontMetrics fm = g.getFontMetrics();
        sheight = fm.getHeight();
        slen = fm.stringWidth(text);
        g.setColor(Color.black);
        g.drawString(text,(getSize().width-slen)/2,(getSize().height+sheight)/2);	// draw text
    }

    public Dimension getMinimumSize() 
    {
        return new Dimension(100,20);
    }
    
    public Dimension getPreferredSize() 
    {
        return new Dimension(100,20);
    }
}

