/*
 * ============================================================================
 * file:	$RCSfile: Buffer.java,v $
 * created by:Frank Drewek
 * revision:	$Revision: 1.1 $
 *
 * last modification at $Date: 1997/11/10 12:51:02 $
 *		   by $Author: drewek $
 *
 * (c)	Universitaet Karlsruhe
 *	Institut fuer Betriebs- und Dialogsysteme, Lehrstuhl Betriebssysteme
 *	Am Fasanengarten 5, 76128 Karlsruhe
 *
 * Permission to use this software for non-profit educational purposes is
 * hereby granted. All other rights are reserved. This software is provided
 * "as is", without any express or implied warranty.
 * ============================================================================
 */

// package threads;


/**
 * The interface for objects which contain a buffer of integers.
 *
 * @author    Frank Drewek
 * @author    Juergen Schneider
 * @see ThreadTA3
 * @see ThreadTA4
 * @see ThreadTA5A
 * @see ThreadTA5B
 * @see ThreadTA7
 * @see ThreadBuffer
 * @see ProductThread
 * @see PrintBufferThread
 * @see GraphicBufferThreadA
 * @see GraphicBufferThreadB
 * @see FileGraphicProdThread
 * @see ThreadGraphicOutput
 */
public interface Buffer
 {
   // access //////////////////////////////////////////////////////////////////

   /**
    * setBuffer sets the overgiven length on position pos.
    * @param pos      position in the buffer
    * @param length   value of pos in the buffer
    */   
   public void setBuffer(int pos, int length);
   
   /**
    * getBuffer returns the value of position pos.
    * @param pos    position in the buffer
    */
   public int getBuffer(int pos);

   /**
    * isEmpty returns true if each position in the buffer is set on
    * zero otherwise it returns false.
    */ 
   public boolean isEmpty();

   /**
    * getMax returns the maximal number of integers in the buffer.
    */
   public int getMax();    

   // service /////////////////////////////////////////////////////////////////


   // debug ///////////////////////////////////////////////////////////////////


   /** @deprecated	for debugging purposes */
   public String getRevision();
 }
