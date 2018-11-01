package test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\test2.html"),"UTF-8");
		System.out.println(html);
		String pattern = "params.grzh=\"([^\"]*)\"";
		 // 创建 Pattern 对象
	     Pattern r = Pattern.compile(pattern);
		 Matcher m = r.matcher(html);
		 String numid="";
	      while (m.find( )) {
	         System.out.println(" value1: " + m.group(1));
	         numid=m.group(1);
	         break;
	      } 
	    
	      System.out.println(numid);
	}

}
