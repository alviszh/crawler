package com.module.ddxoft;

import java.util.Properties;
import java.util.Set;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class VK {
	
	//字符串按键输入，一般用于密码
	public static void KeyPress(String str){
		DD.INSTANCE.DD_str(str); //字符串
	}
	
	//按Tab键
	public static void Tab(){
		DD.INSTANCE.DD_key(300, 1);DD.INSTANCE.DD_key(300, 2); 
	}
	
	//按Enter键
	public static void Enter(){
		DD.INSTANCE.DD_key(313, 1);DD.INSTANCE.DD_key(313, 2); 
	}
	
	//按Esc键
	public static void Esc(){
		DD.INSTANCE.DD_key(100, 1);DD.INSTANCE.DD_key(100, 2); 
	}
	
	
	 
	 
	 public interface DD extends Library {
		   DD INSTANCE = (DD)Native.loadLibrary("DD81200x64.64", DD.class);
		   //64位JAVA调用*64.dll, 32位调用*32.dll 。与系统本身位数无关。、
		   public int DD_mov(int x, int y);
		   public int DD_movR(int dx, int dy);
		   public int DD_btn(int btn);
		   public int DD_whl(int whl);
		   public int DD_key(int ddcode, int flag);
		   public int DD_str(String s);
	 }
	 
	 
	 public static void main(String[] args) throws InterruptedException {
		 System.out.println("测试开始");
		 Thread.sleep(1500L);  
		 Properties properties = System.getProperties(); 
		 Set<String> names = properties.stringPropertyNames(); 
		 for(String name:names){
			 System.out.println(name+"-----"+System.getProperty(name));
		 } 
		 //DD.INSTANCE.DD_mov(500, 500);   //绝对移动 
		 //DD.INSTANCE.DD_movR(100, 100); //相对移动1234567890!@#$%
		 //DD.INSTANCE.DD_btn(4);DD.INSTANCE.DD_btn(8); //鼠标右键
		 //DD.INSTANCE.DD_key(601, 1);//DD.INSTANCE.DD_key(601, 2); //键盘win	1是按下，2是弹起	
		 //DD.INSTANCE.DD_key(300, 1);DD.INSTANCE.DD_key(300, 2); //键盘tab 	1是按下，2是弹起   
		 //DD.INSTANCE.DD_str("1234567890!@#$%^&*()-,qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"); //字符串
		 //Tab();		
		 KeyPress("1234567890!@#$%^&*()-,qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM");
		 //1234567890!@#$%^&*()-,qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM 
	 }
	 
	 

}

/**

函数说明：
所有函数(除特别说明外)的参数和返回值都为传值int32类型
1. DD_btn(参数)
功能： 模拟鼠标点击
参数： 1 =左键按下 ，2 =左键放开
4 =右键按下 ，8 =右键放开
16 =中键按下 ，32 =中键放开
64 =4键按下 ，128 =4键放开
256 =5键按下 ，512 =5键放开 
例子：模拟鼠标右键 只需要连写(中间可添加延迟) dd_btn(4); dd_btn(8);


2. DD_mov(参数x,参数y)
功能： 模拟鼠标结对移动
参数： 参数x , 参数y 以屏幕左上角为原点。
例子： 把鼠标移动到分辨率1920*1080 的屏幕正中间，
int x = 1920/2 ; int y = 1080/2;
DD_mov(x,y) ;



3. DD_movR(参数dx,参数dy)
功能： 模拟鼠标相对移动
参数： 参数dx , 参数dy 以当前坐标为原点。
例子： 把鼠标向左移动10像素
DD_movR(-10,0) ;



4. DD_whl(参数)
功能: 模拟鼠标滚轮
参数: 1=前 , 2 = 后
例子: 向前滚一格, DD_whl(1)



5. DD_key(参数1，参数2)
功能： 模拟键盘按键
参数： 参数1 ，请查看[DD虚拟键盘码表]。
参数2，1=按下，2=放开
例子： 模拟单键WIN，
DD_key(601, 1);DD_key(601, 2);
	组合键：ctrl+alt+del
	DD_key(600,1);	
	DD_key(602,1);	
	DD_key(706,1);	
	DD_key(706,2);
	DD_key(602,2);
	DD_key(600,2);



6. DD_str(参数)
功能： 直接输入键盘上可见字符和空格
参数： 字符串, (注意，这个参数不是int32 类型)
例子： DD_str("MyEmail@aa.bb.cc !@#$")

普通游戏和桌面操作自动化，所有鼠标键盘模拟操作都只需 DD_btn , DD_mov , DD_whl，DD_movR 四个操控鼠标的函数和 DD_key 一个操控键盘的函数即可完成。

QQ：1458056700(已满) 2827362732(新)

* 
* 
* */
