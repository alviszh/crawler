import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String  name = "罗燕";
		String encode = URLEncoder.encode(name, "UTF-8");
		String keyWord = URLDecoder.decode(name, "UTF-8");
		System.out.println(keyWord);
	}

}
