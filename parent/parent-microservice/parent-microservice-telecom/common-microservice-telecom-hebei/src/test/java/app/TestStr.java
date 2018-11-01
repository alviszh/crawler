package app;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestStr {
	
	 private final static SimpleDateFormat longSdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss",Locale.UK);  
	
	public static void main(String[] args) {
//		TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
//		longSdf.setTimeZone(TimeZone.getTimeZone("GMT+0800"));  
		Date date = new Date();
		String str = URLEncoder.encode(longSdf.format(date));
		System.out.println(str); 
		//Tue+Feb+27+2018+14%3A39%3A53+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		for(int i = 0; i<7; i++){			
			c1.setTime(new Date());
			c1.add(Calendar.MONTH, -i);
			c2.setTime(new Date());
			c2.add(Calendar.MONTH, -i+1);
			Date date1 = c1.getTime();
			Date date2 = c2.getTime();
			String mon = sdf.format(date1);
			String nextMon = sdf.format(date2);
			System.out.println(nextMon);
			System.out.println("***********************");
			System.out.println(mon);
			System.out.println("=======================");
		}*/
		
		
		String text = "<root><actionFlag>0</actionFlag><actionMsg></actionMsg></root>";
		Document doc = Jsoup.parse(text);
		System.out.println(doc.select("actionFlag").first().text());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();  
		calendar.add(Calendar.MONTH, -1);  
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(sdf.format(calendar.getTime()));
		calendar.add(Calendar.MONTH, 1);  
		calendar.set(Calendar.DAY_OF_MONTH, 0);  
		System.out.println(sdf.format(calendar.getTime()));
		
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		System.out.println(sdf2.format(new Date()));
//		Calendar c1 = Calendar.getInstance();
//		c1.setTime(new Date());
//		c1.add(Calendar.MONTH, -1);
//		Date date1 = c1.getTime();
//		String currentmon = sdf2.format(date1);
//		System.out.println(currentmon);
		
		
//		System.out.println(getCurrentYearStartTime());
//		System.out.println(getCurrentYearEndTime());
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, -1);
//		Date date = c.getTime();
//		String mon = sdf.format(date);
////		System.out.println(mon);
//		
//		String contentAsString = "doQuery('18931110706','188','1411909','赵芝建','8','615902283');";
//		String regex = "doQuery[^,]*,'(.*)'?";
//		Pattern p = Pattern.compile(regex); 
//		Matcher m = p.matcher(contentAsString);
//		if(m.find()){
//			String cityCode = m.group(0);
//			System.out.println(cityCode);
//			String code = cityCode.split(",")[1].replace("'", "");
//			System.out.println(code);
//		}
//		
		
		
		
	}
	
	public static String getCurrentYearStartTime() {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
        c.add(Calendar.MONTH, +6);
        Date m3 = c.getTime();
        String mon3 = format.format(m3);
        return mon3;  
    }  
	
	
	public static String parserField(Document doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
		
	}
	
	 public static String getCurrentYearEndTime() {  
		 Calendar c = Calendar.getInstance();  
	     String now = null;  
	     try {  
	         c.set(Calendar.MONTH, 11);  
	         c.set(Calendar.DATE, 31);  
	         now = longSdf.format(c.getTime());  
	     } catch (Exception e) {  
	         e.printStackTrace();  
	     }  
	     return now;  
	}  

	 
	
}
