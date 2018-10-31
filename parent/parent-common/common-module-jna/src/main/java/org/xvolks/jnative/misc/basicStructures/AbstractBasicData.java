package org.xvolks.jnative.misc.basicStructures;

/**
 * $Id: AbstractBasicData.java,v 1.6 2006/06/09 20:44:05 mdenty Exp $
 *
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
import org.xvolks.jnative.pointers.*;
import org.xvolks.jnative.exceptions.*;

public abstract class AbstractBasicData<T> implements BasicData<T> {
	protected T mValue;
	protected Pointer pointer;
	protected int offset;
	
	protected byte getNextByte() throws NativeException {
		return pointer.getAsByte(offset++);
	}
	protected short getNextShort() throws NativeException {
		try {
			return pointer.getAsShort(offset);
		} finally {
			offset += 2;
		}
	}
	protected int getNextInt() throws NativeException {
		try {
			return pointer.getAsInt(offset);
		} finally {
			offset += 4;
		}
	}
	protected long getNextLong() throws NativeException {
		try {
			return pointer.getAsLong(offset);
		} finally {
			offset += 8;
		}
	}
	
	/**
	 * Method getPointer
	 *
	 * @return   a Pointer
	 *
	 */
	public Pointer getPointer() {
		return pointer;
	}
	
	protected AbstractBasicData(T lValue) {
		mValue = lValue;
	}
	
	/**
	 * Method getValue
	 *
	 * @return   a T
	 *
	 */
	public T getValue() {
		return mValue;
	}
	
	public String getValueAsString() {
		if(mValue == null) return null;
		else return mValue.toString();
	}
	
	@Override
	public String toString() {
		if(mValue == null) {
			return "NULL";
		}
		return mValue.toString();
	}
}
