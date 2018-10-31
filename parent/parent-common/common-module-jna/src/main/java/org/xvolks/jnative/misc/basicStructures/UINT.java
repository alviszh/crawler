package org.xvolks.jnative.misc.basicStructures;

/**
 * $Id: UINT.java,v 1.5 2006/10/13 21:00:36 mdenty Exp $
 *
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
import org.xvolks.jnative.exceptions.*;
import org.xvolks.jnative.misc.machine.*;
import org.xvolks.jnative.pointers.*;
import org.xvolks.jnative.pointers.memory.*;

public class UINT extends AbstractBasicData<Short> {
	public UINT(int value) {
		this((short)value);
	}
	public UINT(short value) {
		super(value);
		try {
			createPointer();
		} catch (NativeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * Method getSizeOf
	 * @return   the size of this data
	 */
	public int getSizeOf() {
		return sizeOf();
	}


	/**
	 * Method createPointer
	 *
	 * @return   a Pointer
	 *
	 * @exception   NativeException
	 *
	 */
	public Pointer createPointer() throws NativeException {
		pointer = new Pointer(MemoryBlockFactory.createMemoryBlock(sizeOf()));
		pointer.setShortAt(0, mValue.shortValue());
		return pointer;
	}

	/**
	 * Method getValueFromPointer
	 *
	 * @return   a T
	 *
	 */
	public Short getValueFromPointer() throws NativeException {
		mValue = new Short(pointer.getAsShort(0));
		return mValue;
	}
	
	public void setValue(Short lValue) throws NativeException {
		mValue = lValue;
		pointer.setShortAt(0, mValue);
	}
	
	@Override
	public Short getValue() {
		try {
			return getValueFromPointer();
		} catch (NativeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static int sizeOf() {
		return Machine.SIZE * 2;
	}
}
