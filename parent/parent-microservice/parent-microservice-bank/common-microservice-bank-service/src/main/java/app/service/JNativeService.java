package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xvolks.jnative.exceptions.NativeException;

import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.commontracerlog.TracerLog;

@Component
public class JNativeService {
	
	@Autowired
	private TracerLog tracerLog;
	
	public void Input(String input) throws IllegalAccessException, NativeException, Exception { 
		System.out.println("GetForegroundWindow------------"+User32.GetWindowText(User32.GetForegroundWindow()));
		tracerLog.addTag("JNativeService Input", input);
		//修改为KeyPressEx 次方法支持大小写特殊字符（空格除外），修改时间 20171108 by meidi
		VirtualKeyBoard.KeyPressEx(input);
		/*char[] c = input.toCharArray();
		for (char s : c) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPressEx(VKMapping.toScanCode(String.valueOf(s)));
			}
			Thread.sleep(500L);
		}*/
	}
	
	public void InputTab() throws IllegalAccessException, NativeException, Exception { 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
	
	public void InputEnter() throws IllegalAccessException, NativeException, Exception { 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter")); 
		} 
	}
}
