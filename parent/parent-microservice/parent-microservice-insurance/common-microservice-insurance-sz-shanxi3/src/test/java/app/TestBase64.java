package app;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TestBase64 {
	
	public static void main(String[] args) throws Exception {
		String base64Str = "UEsDBBQACAgIACJ+LEwAAAAAAAAAAAAAAAAUAAAAREFUQVNFVFNfRVhfRWRpdFdhcmV1u1NwJVzQ";
				
		byte[] asBytes = Base64.getDecoder().decode(base64Str);  
		System.out.println(new String(asBytes, "GBK"));
	}

}
