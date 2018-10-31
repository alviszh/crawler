package org.xvolks.jnative.util;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.HANDLE;
import org.xvolks.jnative.misc.basicStructures.LONG;
import org.xvolks.jnative.pointers.NullPointer;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;

/**
 * $Id: PsAPI.java,v 1.5 2006/10/03 21:38:39 mdenty Exp $
 * <p>
 * PsAPI.java
 * </p>
 * 
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public class PsAPI {
	public static final String DLL_NAME = "PSAPI.DLL";
	private static JNative nEnumProcess;
	private static Pointer nReadProcess;
	private static JNative nEnumProcessModules;
	private static LONG lpcbNeeded;
	private static JNative nGetModuleBaseName;

	/**
	 * <p>
	 * The EnumProcesses function retrieves the process identifier for each
	 * process object in the system.
	 * </p>
	 * 
	 * <pre>
	 * 	 BOOL EnumProcesses(
	 * 	 DWORD* pProcessIds,
	 * 	 DWORD cb,
	 * 	 DWORD* pBytesReturned
	 * 	 );
	 * 	 
	 * 	 
	 * 	 
	 * 	 Parameters
	 * 	 
	 * 	 pProcessIds
	 * 	 [out] Pointer to an array that receives the list of process identifiers.
	 * 	 cb
	 * 	 [in] Size of the pProcessIds array, in bytes.
	 * 	 pBytesReturned
	 * 	 [out] Number of bytes returned in the pProcessIds array.
	 * 	 
	 * 	 Return Values
	 * 	 
	 * 	 If the function succeeds, the return value is nonzero.
	 * 	 
	 * 	 If the function fails, the return value is zero. To get extended error information, call GetLastError.
	 * 	 Remarks
	 * 	 
	 * 	 It is a good idea to use a large array, because it is hard to predict how many processes there will be at the time you call EnumProcesses.
	 * 	 
	 * 	 To determine how many processes were enumerated, divide the pBytesReturned value by sizeof(DWORD). There is no indication given when the buffer is too small to store all process identifiers. Therefore, if pBytesReturned equals cb, consider retrying the call with a larger array.
	 * 	 
	 * 	 To obtain process handles for the processes whose identifiers you have just obtained, call the OpenProcess function.
	 * 	
	 * </pre>
	 * 
	 * </p>
	 */
	public static int[] EnumProcess(int maxSizeOfInitialBuffer)
			throws NativeException, IllegalAccessException {
		if (maxSizeOfInitialBuffer < 4) {
			maxSizeOfInitialBuffer = 4;
		}

		if (nEnumProcess == null) {
			nEnumProcess = new JNative(DLL_NAME, "EnumProcesses");
			nEnumProcess.setRetVal(Type.INT);
			nReadProcess = new Pointer(MemoryBlockFactory.createMemoryBlock(4));
			nEnumProcess.setParameter(2, nReadProcess);
		}
		Pointer pProcessIds = new Pointer(MemoryBlockFactory
				.createMemoryBlock(maxSizeOfInitialBuffer));
		nEnumProcess.setParameter(0, pProcessIds);
		nEnumProcess.setParameter(1, Type.INT, maxSizeOfInitialBuffer + "");
		nEnumProcess.invoke();
		if ("0".equals(nEnumProcess.getRetVal())) {
			return null;
		} else {
			int sizeNeeded = nReadProcess.getAsInt(0);
			if (sizeNeeded > maxSizeOfInitialBuffer) {
				System.err.println(maxSizeOfInitialBuffer
						+ " is to low, will recall with " + sizeNeeded);
				return EnumProcess(sizeNeeded);
			} else {
				int numProcess = nReadProcess.getAsInt(0) / 4;
				int[] process = new int[numProcess];
				for (int i = 0; i < numProcess; i++) {
					process[i] = pProcessIds.getAsInt(i * 4);
				}
				pProcessIds.dispose();
				return process;
			}
		}
	}

	/**
	 * <pre>
	 * 	 Retrieves a handle for each module in the specified process.
	 * 	 
	 * 	 BOOL WINAPI EnumProcessModules(
	 * 	 HANDLE hProcess,
	 * 	 HMODULE* lphModule,
	 * 	 DWORD cb,
	 * 	 LPDWORD lpcbNeeded
	 * 	 );
	 * 	 
	 * 	 Parameters
	 * 	 
	 * 	 hProcess
	 * 	 [in] A handle to the process.
	 * 	 lphModule
	 * 	 [out] An array that receives the list of module handles.
	 * 	 cb
	 * 	 [in] The size of the lphModule array, in bytes.
	 * 	 lpcbNeeded
	 * 	 [out] The number of bytes required to store all module handles in the lphModule array.
	 * 	 
	 * 	 Return Values
	 * 	 
	 * 	 If the function succeeds, the return value is nonzero.
	 * 	 
	 * 	 If the function fails, the return value is zero. To get extended error information, call GetLastError.
	 * </pre>
	 * 
	 */
	public static Pointer EnumProcessModules(HANDLE hProcessHandle,
			int maxSizeOfInitialBuffer) throws NativeException,
			IllegalAccessException {
		if (maxSizeOfInitialBuffer < 4) {
			maxSizeOfInitialBuffer = 4;
		}
		if (nEnumProcessModules == null) {
			nEnumProcessModules = new JNative(DLL_NAME, "EnumProcessModules");
			nEnumProcessModules.setRetVal(Type.INT);
			lpcbNeeded = new LONG(0);
			nEnumProcessModules.setParameter(3, lpcbNeeded.createPointer());
		}
		Pointer lphModule = new Pointer(MemoryBlockFactory
				.createMemoryBlock(maxSizeOfInitialBuffer));
		nEnumProcessModules.setParameter(0, hProcessHandle.getValue());
		nEnumProcessModules.setParameter(1, lphModule);
		nEnumProcessModules.setParameter(2, maxSizeOfInitialBuffer);
		nEnumProcessModules.invoke();
		int sizeNeeded = lpcbNeeded.getValueFromPointer();
		if ("0".equals(nEnumProcess.getRetVal())) {
			lphModule.dispose();
			return new NullPointer();
		} else if (sizeNeeded > maxSizeOfInitialBuffer) {
			lphModule.dispose();
			System.err.println(maxSizeOfInitialBuffer
					+ " is to low, will recall with " + sizeNeeded);
			return EnumProcessModules(hProcessHandle, sizeNeeded);
		} else {
			return lphModule;
		}
	}

	/**
	 * 
	 * <pre>
	 *  Retrieves the base name of the specified module.
	 * 	 
	 * 	 DWORD WINAPI GetModuleBaseName(
	 * 	 HANDLE hProcess,
	 * 	 HMODULE hModule,
	 * 	 LPTSTR lpBaseName,
	 * 	 DWORD nSize
	 * 	 );
	 * 	 
	 * 	 Parameters
	 * 	 
	 * 	 hProcess
	 * 	 [in] Handle to the process that contains the module. To specify the current process, use GetCurrentProcess.
	 * 	 
	 * 	 The handle must have the PROCESS_QUERY_INFORMATION and PROCESS_VM_READ access rights. For more information, see Process Security and Access Rights.
	 * 	 hModule
	 * 	 [in] Handle to the module. If this parameter is NULL, this function returns the name of the file used to create the calling process.
	 * 	 lpBaseName
	 * 	 [out] Pointer to the buffer that receives the base name of the module. If the base name is longer than maximum number of characters specified by the nSize parameter, the base name is truncated.
	 * 	 nSize
	 * 	 [in] Size of the lpBaseName buffer, in characters.
	 * 	 
	 * 	 Return Values
	 * 	 
	 * 	 If the function succeeds, the return value specifies the length of the string copied to the buffer, in characters.
	 * 	 
	 * 	 If the function fails, the return value is zero. To get extended error information, call GetLastError.
	 * </pre>
	 */

	public static String GetModuleBaseName(HANDLE hProcessHandle, int hModule,
			int nSize) throws NativeException, IllegalAccessException {
		if (nGetModuleBaseName == null) {
			nGetModuleBaseName = new JNative(DLL_NAME, "GetModuleBaseNameA");
			nGetModuleBaseName.setRetVal(Type.INT);
		}
		Pointer lpBaseName = new Pointer(MemoryBlockFactory
				.createMemoryBlock(nSize + 1));
		nGetModuleBaseName.setParameter(0, hProcessHandle.getValue());
		nGetModuleBaseName.setParameter(1, hModule);
		nGetModuleBaseName.setParameter(2, lpBaseName);
		nGetModuleBaseName.setParameter(3, nSize);
		nGetModuleBaseName.invoke();
		String ret = null;
		if (!"0".equals(nGetModuleBaseName.getRetVal())) {
			ret = lpBaseName.getAsString();
		}
		lpBaseName.dispose();
		return ret;
	}
}
