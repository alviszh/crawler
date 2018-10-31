package org.xvolks.jnative.pointers.memory;

/**
 * $Id: GlobalMemoryBlock.java,v 1.4 2006/06/09 20:44:04 mdenty Exp $
 *
 * * <p><b>Win32 : </b>GlobalMemoryBlock is a block of memory reserved from the heap
 * with the function : GlobalAlloc (see MSDN)
 * <br>This seems to be very stable when calling other DLL.
 * <br><b>Type of memory</b>
 *  <pre>
 * - GHND
 * 0x0042 	Combines GMEM_MOVEABLE and GMEM_ZEROINIT.
 * - GMEM_FIXED
 * 0x0000 	Allocates fixed memory. The return value is a pointer.
 * - GMEM_MOVEABLE
 * 0x0002 	Allocates movable memory. Memory blocks are never moved in physical memory, but they can be moved within the default heap.
 *
 * The return value is a handle to the memory object. To translate the handle into a pointer, use the GlobalLock function.
 * This value cannot be combined with GMEM_FIXED.
 * - GMEM_ZEROINIT
 * 0x0040 	Initializes memory contents to zero.
 * - GPTR
 * 0x0040 	Combines GMEM_FIXED and GMEM_ZEROINIT.
 *</pre>
 * </p>
 *
 * <p><b>Linux : </b>Not implemented yet</p>
 * <br>
 *
 *
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
import org.xvolks.jnative.*;
import org.xvolks.jnative.exceptions.*;

public class GlobalMemoryBlock extends AbstractMemoryBlock {
	public static final int GMEM_FIXED = 0;
	public static final int GMEM_ZEROINIT = 0x40;
	public static final int GPTR = GMEM_FIXED | GMEM_ZEROINIT;
	public static final int GMEM_MOVEABLE = 0x2;
	public static final int GHND = GMEM_MOVEABLE | GMEM_ZEROINIT;
	
	
	final static JNative globalAlloc;
	final static JNative globalFree;
	static {
		try {
			globalAlloc = new JNative("Kernel32.dll", "GlobalAlloc");
			globalAlloc.setRetVal(Type.INT);
			globalFree = new JNative("Kernel32.dll", "GlobalFree");
			globalFree.setRetVal(Type.INT);
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private int type;
	
	public GlobalMemoryBlock(int size) throws NativeException {
		this(size, GPTR);
	}
	
	public GlobalMemoryBlock(int size, int type) throws NativeException {
		super(size);
		this.type = type;
		reserveMemory(size);
	}
	
	/**
	 * Method reserveMemory allocate a block of native memory
	 * @param    size                in bytes of the block
	 * @return   a MemoryBlock representing the reserved memory
	 * @exception   NativeException
	 */
	public int reserveMemory(int size) throws NativeException {
		setSize(size);
		if(getPointer() != null)
			dispose();
		try {
			globalAlloc.setParameter(0, Type.INT, "" + type);
			globalAlloc.setParameter(1, Type.INT, "" + size);
			globalAlloc.invoke();
			setPointer(Integer.parseInt(globalAlloc.getRetVal()));
		}
		catch(IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		return getPointer();
	}
	
	/**
	 * Method dispose provides a way to free the memory
	 * @exception   NativeException
	 */
	public void dispose() throws NativeException {
		if(getPointer() != null) {
			try {
				globalFree.setParameter(0, Type.INT, getPointer().toString());
				globalFree.invoke();
				if(Integer.parseInt(globalFree.getRetVal()) != 0) {
					throw new NativeException("Memory not freed !");
				}
				setPointer(null);
			}
			catch(IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
