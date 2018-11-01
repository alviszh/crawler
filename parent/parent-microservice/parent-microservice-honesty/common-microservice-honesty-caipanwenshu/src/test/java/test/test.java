package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class test {

	public static void main(String[] args) throws UnsupportedEncodingException {

//		int a = 4904817;
//
//		int b = 2;
//
//		int c = 0;
//		
//		if(a%b!=0){
//			c = a/b+1;
//		}else{
//			c=a/b;
//		}
//		System.out.println(c);
		String encodeName=URLEncoder.encode("案件类型:民事案件", "utf-8"); 
		System.out.println(encodeName);
		
		String number = "adwqWQEQQ";
		System.out.println(number.substring(0,4));
		//%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B%3A%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6
		//%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B%3A%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6
//http://wenshu.court.gov.cn/list/list/?sorttype=1&number=CA2LKZEY&guid=2a9e4a2a-01ac-ea72d4e1-dea4d576a96e
	}
}
