package app.service.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 上午11:33:28 
 */
@Component
public class HousingFundHelperService {
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
//        System.out.println(dateNowStr);
        return dateNowStr;
	}
	public static String getFiveYearAgoDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
//	    System.out.println("之前:" + f.format(c.getTime()));
	    c.add(Calendar.YEAR, -4);
	    c.add(Calendar.MONTH, 0);
	    c.add(Calendar.DAY_OF_MONTH, 0);
	    String fiveYearAgo = f.format(c.getTime());
//	    System.out.println("之后:" + f.format(c.getTime()));
		return fiveYearAgo;
	}
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
}
