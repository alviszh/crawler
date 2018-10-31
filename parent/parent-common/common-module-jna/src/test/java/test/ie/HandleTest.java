package test.ie;

import org.xvolks.jnative.exceptions.NativeException;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.DWORD;

import test.winio.User32;

public class HandleTest {

	public static void main(String[] args) throws IllegalAccessException, NativeException {
		System.setProperty("jnative.debug", "true");
		String cmbchina = "http://118.112.188.109/nethall/login.jsp";

		OpenIE(cmbchina);
		 
		
		User32.GetWindowText(User32.GetForegroundWindow());
		
	}

	public static void OpenIE(String url) {
		System.out.println("============================= CASE 2");
 
		// http://msdn.microsoft.com/en-us/library/windows/desktop/ms682425%28v=vs.85%29.aspx
		Kernel32 kernel32 = Kernel32.INSTANCE;
		SECURITY_ATTRIBUTES procSecAttr = new SECURITY_ATTRIBUTES();
		SECURITY_ATTRIBUTES threadSecAttr = new SECURITY_ATTRIBUTES();
		WinBase.PROCESS_INFORMATION.ByReference pi = new WinBase.PROCESS_INFORMATION.ByReference();
		WinBase.STARTUPINFO startupInfo = new WinBase.STARTUPINFO();
		boolean success = kernel32.CreateProcess(null, "C:\\Program Files\\Internet Explorer\\iexplore.exe " + url,
				procSecAttr, threadSecAttr, false, new DWORD(0x00000010), null, null, startupInfo, pi);
 
		// Shell32.INSTANCE.ShellExecute(null, "open", "http://news.baidu.com",
		// null, null, 9);

		if (!success) {
			System.out.println("打开IE失败");
		} else {
			System.out.println("打开IE成功");
		}

		kernel32.CloseHandle(pi.hProcess);
		kernel32.CloseHandle(pi.hThread);

		System.out.println();
	}
}
