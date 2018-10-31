package test.jnr;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class GetPidJNAExample {
	public interface GetPid extends Library {
		long getpid();
	}

	public static void main(String[] args) {
		GetPid getpid = (GetPid) Native.loadLibrary(GetPid.class);

		getpid.getpid();
	}
}
