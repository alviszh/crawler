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

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:56:59 
 */
@Component
public class HousingFundHelperService {
	public static final Logger log = LoggerFactory.getLogger(HousingFundHelperService.class);
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public static String getFiveYearAgoDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -4);
	    c.add(Calendar.MONTH, 0);
	    c.add(Calendar.DAY_OF_MONTH, 0);
	    String fiveYearAgo = f.format(c.getTime());
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
	
	public static String getStatus(String statusCode){
		String status="正常";
		if(statusCode.equals("0")){
			status="正常";
		}else if(statusCode.equals("1")){
			status="封存";
		}else if(statusCode.equals("2")){
			status="空账";
		}else if(statusCode.equals("9")){
			status="销户";
		}
		return status;
	}
	//获取年份(从当年开始往前推指定年份)
	public static String getYear(int i){
		SimpleDateFormat f = new SimpleDateFormat("yyyy");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -i);
	    String year = f.format(c.getTime());
	    return year;
	}
	
}
