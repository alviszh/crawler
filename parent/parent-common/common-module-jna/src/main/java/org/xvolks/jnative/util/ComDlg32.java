package org.xvolks.jnative.util;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.ChooseColor;
import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LONG;
import org.xvolks.jnative.misc.basicStructures.LPARAM;

/**
 * 
 * $Id: ComDlg32.java,v 1.2 2006/06/09 20:44:05 mdenty Exp $
 * 
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public class ComDlg32 {
	public static final String DLL_NAME = "COMDLG32.DLL";

	//Cache the JNative object between calls.
	private static JNative nChooseColor;
	
	/**
	 * 
	 * @param lOwner
	 * @param hInstance
	 * @param Flags
	 * @param lCustData
	 * @param lCallback
	 * @param lTemplateName
	 * @return -1 if the use does not click on OK button, the RGB value else.
	 * @throws NativeException
	 * @throws IllegalAccessException
	 */
	public static int ChooseColor(HWND lOwner, HWND hInstance, LONG Flags, LPARAM lCustData, Callback lCallback, String lTemplateName) throws NativeException, IllegalAccessException {
		if(nChooseColor == null) {
			//JNative uses ANSI version ChooseColorA vs ChooseColorW
			nChooseColor = new JNative(DLL_NAME, "ChooseColorA");
			//BOOL is in fact an INT
			nChooseColor.setRetVal(Type.INT);
		}
		ChooseColor nChooseColorStruct = new ChooseColor();
		nChooseColorStruct.setOwner(lOwner);
		nChooseColorStruct.setInstance(hInstance);
		nChooseColorStruct.setFlags(Flags);
		nChooseColorStruct.setCustData(lCustData);
		nChooseColorStruct.addCallback(lCallback);
		nChooseColorStruct.setTemplateName(lTemplateName);
		
		nChooseColor.setParameter(0, nChooseColorStruct.getPointer());
		nChooseColor.invoke();
		if("0".equals(nChooseColor.getRetVal())) {
			return -1;
		} else {
			return nChooseColorStruct.getValueFromPointer().rgbResult.getValue();
		}
	}

	public static void main(String[] args) throws NativeException, IllegalAccessException {
		ChooseColor(new HWND(0), new HWND(0), new LONG(0), new LPARAM(0), null, "TOTO");
		ChooseColor(new HWND(0), new HWND(0), new LONG(0), new LPARAM(0), null, "TOTO");
	}
}
