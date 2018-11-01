import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class test {

	public static void main(String[] args) throws Exception {
		String encode = URLEncoder.encode("覃莉", "UTF-8");
		System.out.println(encode);
	}

}
