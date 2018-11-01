package test;




import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoginTest {
	
	public static void main(String[] args) {
//       String password = "zly123456";
//      byte[] encodeBytes = Base64.encodeBase64(password.getBytes());  
//       System.out.println(new String(encodeBytes));  
       
       String html = "每页10条记录 | 共8条记录 | 当前1/1页";
//        Document doc = Jsoup.parse(html);
//		Elements ele1 = doc.select("td.page_num");
		String p = html.replace(" ", "");
		p = p.substring(p.indexOf("/")+1,p.lastIndexOf("页"));
		int page1 = Integer.parseInt(p);
		System.out.println(page1);
	}

}
