package test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {

	public static void main(String[] args) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("8a2631e665f17ead0165f17f9d130000");
		System.out.println("finalPassword--------"+finalPassword);

	}

}
