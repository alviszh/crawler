package app.eurekaest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetEurekaTest {
	public static void main(String[] args) {
		System.out.println(replaceBlank("0=eurekaserver\n   1=housingfund-qiqihaer\n  2=housingfund-xian\n   3=housingfund-dongguan"));
	}
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
