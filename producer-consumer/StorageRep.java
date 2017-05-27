/*
 * Author: Raphael Kimmig
 * Description: Graphical Representation of the Warehouse
 * June 2007
 */

import java.awt.*;
import javax.swing.*;

class StorageRep extends JComponent
{
    private int bufSize, numItems;	// buffer size, number of elements currently in the buffer
    private int spacer = 2;
    private int x, y, h, w, itemHeight, itemWidth;
	private Buffer buffer;	// reference to the buffer
	private CircularBuffer cbuffer;

	// class constructor
	
    public StorageRep(Buffer b) 
    {
		this.buffer = b;
		this.cbuffer = b.getCircularBuffer();
		cbuffer.setStorageRep(this);
    }

    public void paint(Graphics g) 
    {
        h = getSize().height;	// get graphics dimensions
        w = getSize().width;
		bufSize = cbuffer.getBufferSize();	// get buffer capacity
		numItems = cbuffer.getNumberOfFullSlots();	// get number of taken buffer slots

		
        x = y = 0;

        g.setColor(Color.black);
        g.fillRect(x, y, w, h); // draw rectangle in coordinates (x, y) of width w and height h

        w -= 2*spacer;
        h -= 2*spacer;

        g.setColor(Color.white);
        g.fillRect(spacer, spacer, w, h);


        g.setColor(Color.gray);
        itemHeight = (int)((h - (bufSize+1)*spacer) / bufSize);
        itemWidth = w - 2*spacer;

        x = 2*spacer;
        y = spacer;
		
        // fills with gray the graphical slots
        for (int i = 0; i < numItems; i++) 
        {
            y += spacer;
            g.fillRect(x, y, itemWidth, itemHeight);
            y += itemHeight;
        }
    }

    public Dimension getMinimumSize() 
    {
        return new Dimension(50,200);
    }
    
    public Dimension getPreferredSize() 
    {
        return new Dimension(50,400);
    }
}