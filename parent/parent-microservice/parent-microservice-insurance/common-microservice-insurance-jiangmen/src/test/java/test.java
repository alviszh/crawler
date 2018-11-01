import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		String username = "440782198604200646";
//		String password = "140308";
//		String message = "358699";
//		Base64.Encoder encoder = Base64.getEncoder();
//		byte[] textByte = username.getBytes("UTF-8");
//		byte[] textByte1 = password.getBytes("UTF-8");
//		byte[] textByte2 = message.getBytes("UTF-8");
//		String encodedText = encoder.encodeToString(textByte);
//		String encodedText1 = encoder.encodeToString(textByte1);
//		String encodedText2 = encoder.encodeToString(textByte2);
//		System.out.println(encodedText);
//		System.out.println(encodedText1);
//		System.out.println(encodedText2);
		String url = "/wEWFwKvq6O4AgL2g8m2DAL0z6nBDAKNk/jDAQK9+7qcDgL5h8GfAwKv4uKxAgLCi5reAwLb+8OvCQLii9jdAwL6hrQbAoylvswLAoDq25wPAoPY6rgGAsbMrrkJAteGsBwCp7zKtAkCp7zOtAkCz4KjqwYClPnEnAgC1KLwnQYC7KW1tQgC4sm40QNRGHaoutG/dYRNaYOpT4ffPyQkBA==";
		String encodeURL=URLEncoder.encode( url, "UTF-8" );
		System.out.println(encodeURL);
	}

}
