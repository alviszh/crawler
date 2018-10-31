package com.module.jna.winio;

import com.module.jna.winio.VKMapping;
import com.module.jna.winio.WinIOAPI;

//虚拟键盘
public class VirtualKeyBoard {
	
	public static void main(String[] args) throws Exception {
		Thread.sleep(3000); 
		String s="1234567890!@#$%^&*()qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		//String s="1234567890 !@#$%^&*()qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		//  1234567890!@#$%^&*()qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM
		//  
		KeyPressEx(s,50);// 1234567890!@#$%^&*()qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM
		  
		 
	}
 
	//支持英文大小写和特殊字符，自定义休眠时间
	public static void KeyPressEx(String s,long sleeptime) throws Exception{
		Thread.sleep(1500L);
		for (int i = 0; i < s.length(); i++) { 
			char chr = s.charAt(i);  
			Integer is = VKMapping.getMapValue(""+chr);
			if(is!=null){ 
				KeyPress(VKMapping.toScanCode(""+s.charAt(i)));//  
			}else{ 
				KeyPressUppercase(VKMapping.toScanCodeUppercase(""+s.charAt(i)));//
			} 
			Thread.sleep(sleeptime);
		}
	}
	
	//支持英文大小写和特殊字符
	public static void KeyPressEx(String s) throws Exception{
		Thread.sleep(1500L);
		for (int i = 0; i < s.length(); i++) { 
			char chr = s.charAt(i);  
			Integer is = VKMapping.getMapValue(""+chr);
			if(is!=null){ 
				KeyPress(VKMapping.toScanCode(""+s.charAt(i)));//  
			}else{ 
				KeyPressUppercase(VKMapping.toScanCodeUppercase(""+s.charAt(i)));//
			} 
			Thread.sleep(500);
		}
	}
	
	//支持英文大小写和特殊字符(针对固定间隔时间输入密码不成功的情况)
	/**
	 * @param s
	 * @param least    休眠最小值							
	 * @param max	休眠最大值
	 * @throws Exception
	 * zz
	 */
	public static void KeyPressEx(String s, int least, int max) throws Exception{
		Thread.sleep(1500L);
		for (int i = 0; i < s.length(); i++) { 
			char chr = s.charAt(i);  
			Integer is = VKMapping.getMapValue(""+chr);
			if(is!=null){ 
				KeyPress(VKMapping.toScanCode(""+s.charAt(i)));//  
			}else{ 
				KeyPressUppercase(VKMapping.toScanCodeUppercase(""+s.charAt(i)));//
			} 
			int sec = (int)((max-least)*Math.random()+least);
//			int sec = (int)(Math.random()*1000);
			Thread.sleep(sec);
		}
	}
	
	public static void KeyDown(int key) throws Exception{
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,key,1);
	}
	public static void KeyDownEx(int key) throws Exception{
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,0xe0,1);
		
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,key,1);
		
	}
	public static void KeyUpEx(int key) throws Exception{
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,0xe0,1);
		
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,(key|0x80),1);
		
	}
	public static void KeyUp(int key) throws Exception{
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.CONTROL_PORT,0xd2,1);
		WinIOAPI.KBCWait4IBE();
		WinIOAPI.SetPortVal(WinIOAPI.DATA_PORT,(key|0x80),1);
		
	}
	
	public static void KeyPress(int key) throws Exception{
		KeyDown(key);
		KeyUp(key);
	}
	
	public static void KeyPressUppercase(int key) throws Exception{
		KeyDown(VKMapping.toScanCode("Shift"));
		KeyPress(key);
		KeyUp(VKMapping.toScanCode("Shift"));
	}

}
