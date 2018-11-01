package TestUrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月29日 上午9:56:48 
 */
public class HelperUtils {
	//将源码进行加密
	public static String getMd5Result(String sourceCode){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(sourceCode.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	//将io流转化为字符串
	public static String readStream(InputStream in) throws IOException{
        //定义一个内存输入流 , bos不用关闭，关闭无效
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int len=-1;
        byte buffer[] = new byte[1024];
        while ((len=in.read(buffer))!=-1) {
            bos.write(buffer,0,len);            
        }
        return new String(bos.toByteArray(),"gbk");
    }
	//用如下方式返回ip地址中第三个  / 之前的信息（js无法返回绝对路径，需要借助这个方法）
	public static String getLoginHttpAndHost(String targetUrl){
		int characterPosition = getCharacterPosition(targetUrl);
		String substring = targetUrl.substring(0, characterPosition);
		return substring;
	}
	
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
}
