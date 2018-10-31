package com.module.jna.winio;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.pointers.Pointer;
import org.xvolks.jnative.pointers.memory.MemoryBlockFactory;


public class WinIOAPI {
	public static final int CONTROL_PORT = 0x64;//输入键盘按下消息的端口 驱动中把 0x64 叫命令端口 
	public static final int DATA_PORT = 0x60;//输入键盘弹起消息的端口 驱动中把 0x60 叫数据端口
	static{
		System.out.println(System.getProperty("java.library.path"));
		System.out.println(System.getProperty("java.version")); 
		System.loadLibrary("WinIo32");
		JNative.setLoggingEnabled(true);
		if(!InitializeWinIo()){
			System.err.println("Cannot Initialize the WinIO");
			System.exit(1);
		}
		
	}
	public static void main(String[] args) throws Exception {
//		
//		Thread.sleep(3000);
//		KeyDown(VK_0);
//		Thread.sleep(10000);
//		
//		KeyUp(VK_0);
//		
//		
	}
	
	
	
	public static void KBCWait4IBE() throws Exception{
		int val=0;
		do {
			Pointer p=new Pointer(MemoryBlockFactory.createMemoryBlock(8));
			if(!GetPortVal(CONTROL_PORT,p, 1)){
				System.err.println("Cannot get the Port");
			}
			val = p.getAsInt(0);
			
		} while ((0x2&val)>0);
		
		
		
		
	}
	
	public static boolean InitializeWinIo() {
		System.err.println("-------InitializeWinIo start--------");
		try{
		 JNative jnative = new JNative("WinIo32","InitializeWinIo");
		 
		 System.err.println("-------InitializeWinIo 1--------");
		 
		 jnative.setRetVal(Type.INT);
		 
		 System.err.println("-------InitializeWinIo 2--------");
		 
		 jnative.invoke();
		 
		 System.err.println("-------InitializeWinIo 3--------");
		 
		 int re=jnative.getRetValAsInt();
		 
		 System.err.println("-------InitializeWinIo 4--------");
		 
		 jnative.dispose();
		 
		 System.err.println("-------InitializeWinIo 5--------re:"+re);
		 
		 return re>0;
		}catch (Exception e) {
			System.err.println("InitializeWinIo Exception:"+e.toString());
			e.printStackTrace();
			return false;
		}finally{
			 System.err.println("-------InitializeWinIo final--------");
		}
		 
	}
	
	
	public static boolean GetPortVal(int portAddr, Pointer pPortVal, int size) throws Exception{
		
		JNative j2 = new JNative("WinIo32","GetPortVal");
		 j2.setRetVal(Type.INT);
		 
		 j2.setParameter(0, portAddr);
		 j2.setParameter(1, pPortVal);
		 j2.setParameter(2, size);
		 j2.invoke();
		 int re=j2.getRetValAsInt();
		 j2.dispose();
		 return re>0;
		
	}
	
	public static boolean SetPortVal(int portAddr, int portVal, int size) throws Exception{
		
		JNative j2 = new JNative("WinIo32","SetPortVal");
		 j2.setRetVal(Type.INT);
		 j2.setParameter(0, portAddr);
		 j2.setParameter(1,portVal);
		 j2.setParameter(2, size);
		 j2.invoke();
		 int re=j2.getRetValAsInt();
		 j2.dispose();
		 return re>0;
		
	}
	

}
