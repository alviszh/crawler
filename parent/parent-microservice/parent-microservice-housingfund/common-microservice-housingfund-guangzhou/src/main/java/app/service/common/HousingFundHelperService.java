package app.service.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

@Component
public class HousingFundHelperService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundHelperService.class);
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public static String getFiveYearAgoDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -4);
	    c.add(Calendar.MONTH, 0);
	    c.add(Calendar.DAY_OF_MONTH, 0);
	    String fiveYearAgo = f.format(c.getTime());
		return fiveYearAgo;
	}
	//获取登录所有的cookie
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	public static void killProcess(){
		Runtime runTime = Runtime.getRuntime();
		try {
			runTime.exec("TASKKILL /F /IM iexplore.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
