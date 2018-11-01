package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.jersey.core.util.Base64;

public class Test64 {

	public static void main(String[] args) throws Exception {
		String parser = parser(args);
		//System.out.println(parser.substring(4));
	    String base64Decode = Base64.base64Decode(parser.substring(4));
	    
	    
	    String name = base64Decode;  
        name = new String(name.getBytes("UTF8"), "UTF8");      
        System.out.println(name);  
        name = new String(name.getBytes("GBK"), "GBK");  
        System.out.println(name); 
		//System.out.println(base64Decode);
		byte[] decodeBase64 = decodeBase64(parser.substring(4));
		//System.out.println(decodeBase64);
		byte[] decode = decode(parser.substring(4));
		//System.out.println(decode);
	}
	
	
	  public static byte[] decodeBase64(String input) throws Exception{  
	        Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");  
	        Method mainMethod= clazz.getMethod("decode", String.class);  
	        mainMethod.setAccessible(true);  
	         Object retObj=mainMethod.invoke(null, input);  
	         return (byte[])retObj;  
	    }
	  
	   public static byte[] decode(String str){    
		   byte[] bt = null;    
		   try {    
		       sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
		       bt = decoder.decodeBuffer( str );    
		   } catch (IOException e) {    
		       e.printStackTrace();    
		   }    
		   
		       return bt;    
		   } 
	public static String parser(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/qwe.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
               // System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		Document doc = Jsoup.parse(txt);
		return doc.text();
	}
}
