package test.jna;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;  

public class WinIOTest {

	public static void main(String[] args) throws NativeException {
		 JNative jnative = new JNative("WinIo32","InitializeWinIo"); 
		 
		 
		 
		 
		 
 
	}
	
	/*public interface CLibrary extends Library {
		CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

		void printf(String format, Object... args);
	}

	public static void main(String[] args) {
		CLibrary.INSTANCE.printf("Hello, World/n");
		for (int i = 0; i < args.length; i++) {
			CLibrary.INSTANCE.printf("Argument %d: %s/n", i, args[i]);
		}
	}*/

}
