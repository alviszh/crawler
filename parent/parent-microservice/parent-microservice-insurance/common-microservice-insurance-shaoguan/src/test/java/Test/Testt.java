package Test;

import app.unit.Base64Util;

public class Testt {
	public static void main() throws Exception {
		String a ="440202199310060620";
		en(a);
		
	}
	
	public static void en(String a) {
		String encode = Base64Util.encode(a.getBytes());
		System.out.println(encode);

	}
}
