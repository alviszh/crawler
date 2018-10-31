package com.module.jna.winio;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.module.jna.winio.User32;

//按键码映射关系
public class VKMapping {
	
	public static final Map<String,Integer> map=new HashMap();
	
	public static final Map<String,String> mapUp=new HashMap();
	
	static{
		mapUp.put("!", "1");
		mapUp.put("@", "2");
		mapUp.put("#", "3");
		mapUp.put("$", "4");
		mapUp.put("%", "5");
		mapUp.put("^", "6");
		mapUp.put("&", "7");
		mapUp.put("*", "8");
		mapUp.put("(", "9");
		mapUp.put(")", "0");
		mapUp.put("A", "a");
		mapUp.put("B", "b");
		mapUp.put("C", "c");
		mapUp.put("D", "d");
		mapUp.put("E", "e");
		mapUp.put("F", "f");
		mapUp.put("G", "g");
		mapUp.put("H", "h");
		mapUp.put("I", "i");
		mapUp.put("J", "j");
		mapUp.put("K", "k");
		mapUp.put("L", "l");
		mapUp.put("M", "m");
		mapUp.put("N", "n");
		mapUp.put("O", "o");
		mapUp.put("P", "p");
		mapUp.put("Q", "q");
		mapUp.put("R", "r");
		mapUp.put("S", "s");
		mapUp.put("T", "t");
		mapUp.put("U", "u");
		mapUp.put("V", "v");
		mapUp.put("W", "w");
		mapUp.put("X", "x");
		mapUp.put("Y", "y");
		mapUp.put("Z", "z"); 
		mapUp.put(">", "."); 
	}
	
	static{
		map.put("0", KeyEvent.VK_0);
		map.put("1", KeyEvent.VK_1);
		map.put("2", KeyEvent.VK_2);
		map.put("3", KeyEvent.VK_3);
		map.put("4", KeyEvent.VK_4);
		map.put("5", KeyEvent.VK_5);
		map.put("6", KeyEvent.VK_6);
		map.put("7", KeyEvent.VK_7);
		map.put("8", KeyEvent.VK_8);
		map.put("9", KeyEvent.VK_9);
		map.put("a", KeyEvent.VK_A);
		map.put("b", KeyEvent.VK_B);
		map.put("c", KeyEvent.VK_C);
		map.put("d", KeyEvent.VK_D);
		map.put("e", KeyEvent.VK_E);
		map.put("f", KeyEvent.VK_F);
		map.put("g", KeyEvent.VK_G);
		map.put("h", KeyEvent.VK_H);
		map.put("i", KeyEvent.VK_I);
		map.put("j", KeyEvent.VK_J);
		map.put("k", KeyEvent.VK_K);
		map.put("l", KeyEvent.VK_L);
		map.put("m", KeyEvent.VK_M);
		map.put("n", KeyEvent.VK_N);
		map.put("o", KeyEvent.VK_O);
		map.put("p", KeyEvent.VK_P);
		map.put("q", KeyEvent.VK_Q);
		map.put("r", KeyEvent.VK_R);
		map.put("s", KeyEvent.VK_S);
		map.put("t", KeyEvent.VK_T);
		map.put("u", KeyEvent.VK_U);
		map.put("v", KeyEvent.VK_V);
		map.put("w", KeyEvent.VK_W);
		map.put("x", KeyEvent.VK_X);
		map.put("y", KeyEvent.VK_Y);
		map.put("z", KeyEvent.VK_Z);
		map.put("Tab", KeyEvent.VK_TAB);
		map.put("Space", KeyEvent.VK_SPACE);
		map.put("Shift", KeyEvent.VK_SHIFT);
		map.put("Cntl", KeyEvent.VK_CONTROL);
		map.put("CapsLock", KeyEvent.VK_CAPS_LOCK);
		map.put("Enter",13); 
		map.put("Alt", KeyEvent.VK_ALT);
		map.put("F1",KeyEvent.VK_F1);
		map.put("F2",KeyEvent.VK_F2);
		map.put("F3",KeyEvent.VK_F3);
		map.put("F4",KeyEvent.VK_F4);
		map.put("F5",KeyEvent.VK_F5);
		map.put("F6",KeyEvent.VK_F6);
		map.put("F7",KeyEvent.VK_F7);
		map.put("F8",KeyEvent.VK_F8);
		map.put("F9",KeyEvent.VK_F9);
		map.put("F10",KeyEvent.VK_F10);
		map.put("F11",KeyEvent.VK_F11);
		map.put("F12",KeyEvent.VK_F12);
		map.put(".",KeyEvent.VK_PERIOD); 
	}
	
	public static int toVK(String key){
		return map.get(key);
	}
	
	//注释掉catch ,将异常跑出来,以便定为模拟键盘操作的异常 20171030 by meidi
	public static int toScanCode(String key) throws Exception{
		System.out.println("toScanCode------>"+key+"<--");
		//try {
			int vkey = User32.MapVirtualKeyA(getMapValue(key).intValue()); 
			System.out.println("vkey--------"+vkey+"<--");
			System.out.println("map.get(key).intValue()--------"+map.get(key)+"<--");
			return vkey;
		//} catch (Exception e) { 
			//System.out.println(">>"+key+"-------toScanCode Exception--------"+e.toString()+"<--");
			//return 0;
		//}
	} 
	
	//注释掉catch ,将异常跑出来,以便定为模拟键盘操作的异常 20171030 by meidi
	public static int toScanCodeUppercase(String key) throws Exception{
		System.out.println("toScanCodeUppercase------>"+key+"<--"); 
		String upStr = getMapUpValue(key);
		if(upStr==null){
			throw new RuntimeException("不能处理的字符>>"+key+"<<");
		}else{
			int vkey = User32.MapVirtualKeyA(getMapValue(upStr).intValue()); 
			System.out.println("vkey--------"+vkey+"<--"); 
			return vkey;
		}
		  
	}
	
	public static Integer getMapValue(String key){
		if(map.get(key)==null){
			return null;
			//throw new RuntimeException("不能处理的字符>>"+key+"<<");
		}else{
			return map.get(key);
		}
		
	}
	
	public static String getMapUpValue(String key){
		return mapUp.get(key);
	}
	 
	
	public static List<String> getKeyName(){
		String [] array=(String[]) map.keySet().toArray(new String[map.keySet().size()]);
		List<String> list=Arrays.asList(array);
		Collections.sort(list);
		return list;
		 
	}
	
}
