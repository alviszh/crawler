package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\555.html"),"UTF-8");
		 Document doc = Jsoup.parse(html, "utf-8"); 
		 System.out.println(doc);
//		 String username=getNextLabelByKeyword(doc," 个人编号");
//		 System.out.println(username);
//		 
//	
//		 //String html2="{"errMsg":"登录成功！","errid":null,"type":"1"}";
//		 String html2="登录成功！";
//		 if (html2.contains("登录成功")) {
//			System.out.println("登录成功");
//		}
//			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");  
//			Calendar c = Calendar.getInstance();  
//			String endTime=format.format(c.getTime());//开始设置十年之前
//			System.out.println(endTime);
//			c.add(Calendar.YEAR, -10); //年份减3  
//			String startTime=format.format(c.getTime());
//			System.out.println(startTime);
	}
	public static String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("td:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
    }
}
