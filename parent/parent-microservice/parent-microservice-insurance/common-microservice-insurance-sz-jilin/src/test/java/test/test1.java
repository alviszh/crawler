package test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class test1 {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String encodeName=URLEncoder.encode("随恩丽", "utf-8");
		//String encodeName1=URLEncoder.encode(encodeName, "utf-8");
		System.out.println(encodeName);
		//%E9%9A%8F%E6%81%A9%E4%B8%BD
	}
}
