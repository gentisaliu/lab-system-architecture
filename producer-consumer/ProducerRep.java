/*
 * Author: Raphael Kimmig
 * Description: Graphical Representation of a Producer Thread
 * June 2007
 */

import java.awt.*;
import javax.swing.*;
class ProducerRep extends JComponent 
{
    private Producer prod;	// reference to the producer
    private int sheight, slen;
    private String text;
    private Color c;
	
	public ProducerRep(Producer p) 
	{
		this.prod = p;
	}
	
	//	 Class constructor
	
    public void paint(Graphics g)
    {
    	if(prod == null ||  prod.getThreadControlBlock().getDone()) // if producer is done, set its label to done and its background color to gray
    	{
            text = "Producer " + prod.getID() + ": done";
            c = Color.gray;
        } 
    	else if(prod.getThreadControlBlock().getOccupied()) // if producer is active, set its label to consuming and its background color to green
    	{
            text = "Producer " + prod.getID() + ": producing";
            c = Color.green;
        } 
    	else // if consumer is waiting, set its label to waiting and its background color to red
    	{
            text = "Producer " + prod.getID() + ": waiting";
            c = Color.red;
        } 
        
        g.setColor(c);	// set background
        g.fillRect(0,0,getSize().width,getSize().height);	// draw a representation area of the producer thread
        
        FontMetrics fm = g.getFontMetrics();
        sheight = fm.getHeight();
        slen = fm.stringWidth(text);
        g.setColor(Color.black);
        g.drawString(text,(getSize().width-slen)/2,(getSize().height+sheight)/2); // draw text
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
