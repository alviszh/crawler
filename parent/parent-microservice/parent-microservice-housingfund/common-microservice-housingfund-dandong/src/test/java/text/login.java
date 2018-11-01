package text;

import java.util.Date;

public class login {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		String substring = new Date().toLocaleString().substring(5,7);
		System.out.println(substring);
	}
}
