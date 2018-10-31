package org.xvolks.test.Ldcnlib;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class Test {
	
	public static void main(String[] args) throws NativeException, InterruptedException, IllegalAccessException {
//		System.setProperty("jnative.loadNative", "E:\workspace\JNativeCpp\Win32_Release\libJNativeCpp.dll")
		for(String s : JNative.getDLLFileExports("C:\\Documents and Settings\\Administrateur\\Bureau\\ldcnlib\\Ldcnlib.dll")) {
			System.err.println(s);
		}
		System.load("C:\\Documents and Settings\\Administrateur\\Bureau\\ldcnlib\\Ldcnlib.dll");
		JNative LdcnInit = new JNative("Ldcnlib.dll", "LdcnInit");
		LdcnInit.setRetVal(Type.INT);
		LdcnInit.setParameter(0, "COM1");
		LdcnInit.setParameter(1, 19200);
		LdcnInit.invoke();
		
		System.err.println("LdcnInit returned "+LdcnInit.getRetValAsInt());
	}
}
