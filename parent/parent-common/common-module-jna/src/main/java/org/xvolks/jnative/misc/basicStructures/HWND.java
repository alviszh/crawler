
package org.xvolks.jnative.misc.basicStructures;
import org.xvolks.jnative.exceptions.*;
import org.xvolks.jnative.misc.machine.*;
import org.xvolks.jnative.pointers.*;
import org.xvolks.jnative.pointers.memory.*;

/**
 * $Id: HWND.java,v 1.5 2006/06/09 20:44:05 mdenty Exp $
 *
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public class HWND extends AbstractBasicData<Integer> {
	public HWND(int value) {
		super(value);
	}
	
	/**
	 * Method createPointer
	 *
	 * @return   a MemoryBlock
	 *
	 */
	public Pointer createPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(sizeOf()));
		pointer.setIntAt(0, mValue);
		return pointer;
	}
	
	/**
	 * Method getValueFromPointer
	 *
	 * @return   a T
	 *
	 */
	public Integer getValueFromPointer() throws NativeException {
		mValue = pointer.getAsInt(0);
		return mValue;
	}
	
	
	
	/**
	 * Method getSizeOf
	 * @return   the size of this data
	 */
	public int getSizeOf() {
		return sizeOf();
	}
	
	
	/**
	 * Method sizeOf
	 * @return   the size of this structure
	 */
	public static int sizeOf() {
		return Machine.SIZE * 4;
	}
}
