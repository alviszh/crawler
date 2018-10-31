package org.xvolks.jnative.misc;
import org.xvolks.jnative.exceptions.*;
import org.xvolks.jnative.misc.basicStructures.*;
import org.xvolks.jnative.pointers.*;
import org.xvolks.jnative.pointers.memory.*;

/**
 * $Id: Point.java,v 1.5 2006/06/09 20:44:05 mdenty Exp $
 *
 *<pre>
 * Structure C
 * typedef struct tagPOINT {
 * &nbsp;	LONG  x;
 * &nbsp;	LONG  y;
 * } POINT, *PPOINT, NEAR *NPPOINT, FAR *LPPOINT;
 *
 * </pre>
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public class Point extends AbstractBasicData<Point> {
	protected int x, y;
	
	public Point() {
		this(0, 0);
	}
	public Point(int x, int y) {
		super(null);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Method createPointer reserves a native MemoryBlock and copy its value in it
	 * @return   a Pointer on the reserved memory
	 * @exception   NativeException
	 */
	public Pointer createPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(sizeOf()));
		pointer.setIntAt(0, x);
		pointer.setIntAt(4, y);
		return pointer;
	}
	
	/**
	 * Method getValueFromPointer
	 *
	 * @return   a T
	 *
	 * @exception   NativeException
	 *
	 */
	public Point getValueFromPointer() throws NativeException {
		x = pointer.getAsInt(0);
		y = pointer.getAsInt(4);
		return this;
	}
	
	
	
	
	/**
	 * Returns X
	 *
	 * @return    an int
	 */
	public int getX() {
		return x;
	}
	/**
	 * Returns Y
	 *
	 * @return    an int
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Method getValue
	 *
	 * @return   a T
	 *
	 */
	@Override
	public Point getValue() {
		return this;
	}
	
	/**
	 * Method getSizeOf
	 * @return   the size of this data
	 */
	public int getSizeOf() {
		return sizeOf();
	}
	
	public static int sizeOf() {
		return 16;
	}
	
}
