package app.service;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * @description:根据相关代号，解析代号对应的意义,以及部分日期相关处理方法
 * @author: sln 
 * @date: 2017年12月19日 上午11:35:15 
 */
@Component
public class InsuranceTaiAnHelpService {
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	
	//MD5加密登录密码     32位小写
	public static String md5Password(String password) throws Exception{
		String result = "";  
		MessageDigest md5 = MessageDigest.getInstance("MD5");  
		md5.update((password).getBytes("UTF-8"));  
		byte b[] = md5.digest();  
		  
		int i;  
		StringBuffer buf = new StringBuffer("");  
		for(int offset=0; offset<b.length; offset++){  
		    i = b[offset];  
		    if(i<0){  
		        i+=256;  
		    }  
		    if(i<16){  
		        buf.append("0");  
		    }  
		    buf.append(Integer.toHexString(i));  
		}  
		result = buf.toString();  
		return result;  
	}
}
