package test.jna;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.JNative;    
import org.xvolks.jnative.Type;  
import org.xvolks.jnative.exceptions.NativeException;    
import org.xvolks.jnative.pointers.Pointer;  

public class Helper {

	/** 
     * @param args 
     * @throws NativeException  
     */  
    public static void main(String[] args) throws NativeException {  
          
        JNative.setLoggingEnabled(true);  
        try {  
            JNative getUrl = new JNative("EhfscliaxDll.dll", "getUrl"); //创建 getUrl 方法的<span style="font-family: Arial, Helvetica, sans-serif;">JNative对象</span>  
            getUrl.setRetVal(Type.STRING); //设置返回值类型为：String  
            getUrl.setParameter(0, "127.0.0.1"); //按顺序设置方法需要的参数值  
            getUrl.setParameter(1, 10087);  
            getUrl.setParameter(2, 123);  
            getUrl.invoke(); //调用方法  
            System.out.println(getUrl.getRetVal()); //输出返回值  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  
          
       /* try {  
            JNative getPlayUrl = new JNative("EhfscliaxDll.dll", "getPlayUrl");  
            getPlayUrl.setRetVal(Type.INT); //此方法在DLL中定义的返回值类型为：const wchar_t*，如果设置返回值类型为：String，则只能获取到返回值的第一个字符  
              
            String mgrIp = "127.0.0.1";  
            Pointer ptr = Pointer.createPointerFromString(mgrIp);  
              
            getPlayUrl.setParameter(0, ptr);  
            getPlayUrl.setParameter(1, 10087);  
            getPlayUrl.setParameter(2, 123);  
            getPlayUrl.invoke();  
            //个人理解getRetValAsInt是获取返回值的指针地址，<span style="font-family: Arial, Helvetica, sans-serif;">getUnicodeMemoryAsString方法从指针地址的内存中读取字符串</span>  
            String url = JNative.getUnicodeMemoryAsString(getPlayUrl.getRetValAsInt());  
            System.out.println(url);  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  */
    }  

}
