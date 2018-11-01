package text;

public class Cs {

	public static void main(String[] args) {
		String html = "width: 40px; height: 40px; top: -128.333px; background-image: url('/Content/Image/verify_4.jpg'); background-size: 380px 280px; background-position: -164.667px -158.667px;";
		String s = html.substring(html.lastIndexOf(":")).trim();
		String ss = s.substring(s.indexOf("-")+1,s.indexOf(".")).trim();
		System.err.println(ss);
	}

}
