package Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class TestUrl {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String decode = URLDecoder.decode("%B2%E9%D1%AF","GBK");
		System.out.println(decode);
	}
}
