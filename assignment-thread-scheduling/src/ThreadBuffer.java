/*
 * Author: Genti saliu
 * Description: Central buffer
 */

public class ThreadBuffer implements Buffer {

private int[] buffer;
private int[] done;
private int[] max;

	public ThreadBuffer(int[] buffer) {
	this.buffer = buffer;
	this.done = new int[buffer.length];
	this.max = new int[buffer.length];
	}

   /**
    * setBuffer sets the overgiven length on position pos.
    * @param pos      position in the buffer
    * @param length   value of pos in the buffer
    */ 

	public void setBuffer(int pos, int length) {
		if(length >=0) buffer[pos] = length;
		else done[pos] = length;
	}
	
	/**
	 * setMax sets the value 'length' of the outerloop in thread in position 'pos' in the array
	 * @param pos 		position in the buffer
	 * @param length 	outerloop value
	 */
	
	public void setMax(int pos, int length)
	{
		max[pos] = length;
	}
	
	/**
	 * getMax returns the value of the outer loop of thread in position 'pos' in the array
	 * @param pos    position in the buffer
	 */
	
	public int getMax(int pos)
	{
		return max[pos];
	}

   /**
    * getBuffer returns the value of position pos.
    * @param pos    position in the buffer
    */

	public int getBuffer(int pos) {
		return buffer[pos];
	}

   /**
    * isEmpty returns true if each position in the buffer is set on
    * zero otherwise it returns false.
    */

	public boolean isEmpty() {
		for(int i = 0; i < buffer.length; i++) if(done[i] >= 0) return false;
		return true;
	}

   /**
    * getMax returns the maximal number of integers in the buffer.
    */

	public int getMax() {
		return buffer.length;
	}

	/** @deprecated	for debugging purposes */
   public String getRevision() {
	return " ";
   }
}