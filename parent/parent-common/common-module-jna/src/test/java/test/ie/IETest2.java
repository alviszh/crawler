package test.ie;

import java.awt.Robot;
import java.awt.event.InputEvent;

import org.xvolks.jnative.exceptions.NativeException;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.DWORD;

import test.winio.User32;
import test.winio.VKMapping;
import test.winio.VirtualKeyBoard;

public class IETest2 {

	public static void main(String[] args) throws IllegalAccessException, NativeException, Exception {
		System.setProperty("jnative.debug", "true");
		String cmbchina = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/Login.aspx";

		OpenIE(cmbchina);
		

		Thread.sleep(4000L);
		
		String[] accountNum = { "6", "2", "1", "4", "8", "3", "0", "1", "6", "1", "3", "0", "0", "9", "2", "5" };

		String[] password = { "3", "3", "1", "4", "8", "3" };

		

		Input(accountNum);// 输入账户

		InputTab(); // 输入 Tab 切换到密码框

		Input(password);// 输入密码
		
		
		RobotSubmit();//机器人点击登录按钮
	}
	
	public static void RobotSubmit() throws Exception{
		Thread.sleep(1000L);
		Robot robot = new Robot();
		//移动鼠标
        robot.mouseMove(1122, 487);

        //点击鼠标
        //鼠标左键
        System.out.println("单击");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
		
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Windows Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}

	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Windows Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void OpenIE(String url) {
		System.out.println("============================= CASE 2");

		// 打开 IE 浏览器
		// http://msdn.microsoft.com/en-us/library/windows/desktop/ms682425%28v=vs.85%29.aspx
		Kernel32 kernel32 = Kernel32.INSTANCE;
		SECURITY_ATTRIBUTES procSecAttr = new SECURITY_ATTRIBUTES();
		SECURITY_ATTRIBUTES threadSecAttr = new SECURITY_ATTRIBUTES();
		WinBase.PROCESS_INFORMATION.ByReference pi = new WinBase.PROCESS_INFORMATION.ByReference();
		WinBase.STARTUPINFO startupInfo = new WinBase.STARTUPINFO();
		boolean success = kernel32.CreateProcess(null, "C:\\Program Files\\Internet Explorer\\iexplore.exe " + url,
				procSecAttr, threadSecAttr, false, new DWORD(0x00000010), null, null, startupInfo, pi);

		// 将使用默认浏览器打开（我这里是火狐浏览器）
		// Shell32.INSTANCE.ShellExecute(null, "open", "http://news.baidu.com",
		// null, null, 9);

		if (!success) {
			System.out.println("打开IE浏览器失败");
		} else {
			System.out.println("打开IE浏览器成功");
		}

		kernel32.CloseHandle(pi.hProcess);
		kernel32.CloseHandle(pi.hThread);

		System.out.println();
	}
	

}
