package app.gaibantest;

import org.apache.commons.codec.digest.DigestUtils;

public class Md5Test {
	public static void main(String[] args) {
		String md5Hex = DigestUtils.md5Hex("713616l");
		System.out.println(md5Hex);
	}
}
