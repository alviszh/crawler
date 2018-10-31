package org.xvolks.jnative.pointers.memory;
import org.xvolks.jnative.exceptions.*;

/**
 * $Id: MemoryBlock.java,v 1.4 2006/06/09 20:44:04 mdenty Exp $
 *
 * <p>This interface represents a block of native memory</p>
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public interface MemoryBlock {
	
	/**
	 * Method getPointer
	 *
	 * @return   the pointer that addresses the memory block
	 * @exception   NullPointerException if the pointer is null
	 *
	 */
	public Integer getPointer() throws NullPointerException;
	
	/**
	 * Method getSize
	 *
	 * @return   the size of this memory block
	 *
	 * @exception   NullPointerException if the pointer is null
	 *
	 */
	public int getSize() throws NullPointerException;
	
	/**
	 * Method reserveMemory allocate a block of native memory
	 *
	 * @param    size                in bytes of the block
	 *
	 * @return   the address of the reserved memory
	 *
	 * @exception   NativeException
	 *
	 */
	public int reserveMemory(int size) throws NativeException;
	
	/**
	 * Method dispose provides a way to free the memory
	 *
	 * @exception   NativeException
	 *
	 */
	public void dispose() throws NativeException;
	
	
}
