package test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

public class Test2 {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String decode = URLDecoder.decode("%CB%E6%D6%DD","GBK");
		String encode=URLEncoder.encode("全部", "UTF-8");//%E5%85%A8%E9%83%A8   
		System.out.println(decode);
		System.out.println(encode);
		Calendar calendar = Calendar.getInstance();
		for(int i=0;i<5;i++){
			String year = String.valueOf(calendar.get(Calendar.YEAR)-i);
			String yearBefore = String.valueOf(calendar.get(Calendar.YEAR)-i-1);
			System.out.println(year);
			System.out.println("yearBefore="+yearBefore);
			
		}
	}

}
