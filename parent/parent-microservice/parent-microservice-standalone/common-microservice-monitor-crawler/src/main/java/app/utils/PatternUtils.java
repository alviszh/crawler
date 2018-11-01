package app.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * @description: 工具类
 * @author: sln 
 */
@Component
public class PatternUtils {
	
	//Java 获取字符串中第N次出现的字符位置
	public static int getCharacterPosition(String targetUrl){
	    //这里是获取"/"符号的位置
	    Matcher slashMatcher = Pattern.compile("/").matcher(targetUrl);
	    int mIdx = 0;
	    while(slashMatcher.find()) {
	       mIdx++;
	       //当"/"符号第三次出现的位置
	       if(mIdx == 3){
	          break;
	       }
	    }
	    return slashMatcher.start();
	 }
	//对于无法返回绝对路径的js，用如下方法
	/**
	 * 
	 * @param targetUrl
	 * @return
	 */
	public static String getLoginHttpAndHost(String targetUrl){
		int characterPosition = getCharacterPosition(targetUrl);
		String substring = targetUrl.substring(0, characterPosition);
		return substring;
	}
	public static String splitData(String str, String strStart, String strEnd) {  
       /* String tempStr;  
        tempStr = str.substring(str.indexOf(strStart)+strStart.length(), str.indexOf(strEnd)); */ 
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
	
	//将io流转化为字符串，并解决乱码问题(之前采用这个，但是读取效率低下)
	public static String readStream1(InputStream in) throws IOException{
        //定义一个内存输入流 , bos不用关闭，关闭无效
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int len=-1;
        byte buffer[] = new byte[1024];
        while ((len=in.read(buffer))!=-1) {
            bos.write(buffer,0,len);            
        }
        String string = new String(bos.toByteArray(),"utf-8");
       /* invalid byte sequence for encoding "UTF8": 0x00（注意：若不是0x00则很可能是字符集设置有误），
                    是PostgreSQL独有的错误信息，直接原因是varchar型的字段或变量不接受含有'\0'
                   （也即数值0x00、UTF编码'\u0000'）的字符串 。官方给出的解决方法：事先去掉字符串中的'\0'，
                   例如在Java代码中使用str.replaceAll('\u0000', '')，貌似这是目前唯一可行的方法。*/
        string=string.replaceAll("\u0000", "");  //这样操作的原因：如上，引发此修改网站：通辽市公积金的js 
        return string;
    }
	
	//如上方式读取的js内容过长时候会卡死，故修改方法
	public static String readStream(InputStream inputstream){
		String stringResult="";
		StringBuffer buffer = new StringBuffer(); 
		try {
			String line; // 用来保存每行读取的内容 
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream)); 
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream,"UTF-8"));
			line = bufferedReader.readLine(); // 读取一行 
			while (line != null) { // 如果 line 为空说明读完了 
				buffer.append(line); // 将读到的内容添加到 buffer 中 
				buffer.append("\n"); // 添加换行符 
				line = bufferedReader.readLine(); // 读取下一行 
			} 
			stringResult = buffer.toString();
			stringResult=stringResult.replaceAll("\u0000", "");
//			inputstream.close(); 
		} catch (Exception e) {
			System.out.println("读取流的时候出现异常："+e.toString());
		}
		
		return stringResult;
	}
	/**
	 * 如下方法用于过滤网页内容或者是js内容中的部分符号（特别是用于注释的符号，因为调研时发现，有时响应回来的html或者js中，
	 * 上一次任务对某段代码是没有注释的，后一次任务对这段代码是有注释的，这样的话，对md5加密结果是有影响的，故决定用如下方式
	 * 进行处理，将总体监控网页改版与否的思路改为：既对比两次任务的md5码，也对比前后两次任务处理过后内容的长度，若是其中一个没有
	 * 变化，则认为该检测项没有发生变化）
	 * 
	 * 用这个方法的好处在于：影响加密结果的内容除了上述极少出现的“注释与否”现象之外，更多的是部分隐藏域或者非元素标签中带有随机数，
	 * 如果用replaceAll或者是split、remove这样的方法解决，会为调研增加很大工作量
	 * 
	 * 例如有的js无法很精确的取split,例如某网站的一个js,首次执行的时候，function函数命名均是ma,之后又变成了na或者fa,故用如下
	 * 方式处理是最简单的
	 * 
	 * 决定用如下方式还有个原因：经过调研发现，随机变化的部分，虽然内容在变化，但是长度却没有变化，所以，用如下方式，将所有涉及掉注释的
	 * 部分“取消注释”，就能保证处理后内容的统一性，之后再对文本进行长度计算和比较
	 * 
	 *====================================
	 * 总而言之：加密处理之前的html或者js内容
	 * 		  计算处理之后的html或者js内容长度
	 *====================================
	 */
	public static String filter(String str) {
	    String regEx = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]";
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(str);
	    return m.replaceAll("").trim();  //替换掉所有的空格(避免空格影响长度计算的结果)
	}
}
