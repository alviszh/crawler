package Test;

import java.net.URLDecoder;

public class TestDecode {

	public static void main(String[] args) throws Exception {
		String decode = URLDecoder.decode("826b4ada6ac3bf9505c26f101b16fe58", "UTF-8");
		System.out.println(decode);
	}
}
