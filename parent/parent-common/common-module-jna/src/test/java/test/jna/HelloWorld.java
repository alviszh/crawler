package test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class HelloWorld {

	public interface CLibrary extends Library {
		//CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "WinIo64" : "c"), CLibrary.class);
		
		CLibrary INSTANCE = (CLibrary) Native.loadLibrary("WinIo32", CLibrary.class);

		boolean InitializeWinIo();;
	}

	public static void main(String[] args) {
		boolean bool = CLibrary.INSTANCE.InitializeWinIo(); 
		System.out.println("bool---------"+bool);
	}

}
