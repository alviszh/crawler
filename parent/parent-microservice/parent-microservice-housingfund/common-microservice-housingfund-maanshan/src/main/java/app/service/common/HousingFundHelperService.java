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
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	//获取年份(从当年开始往前推指定年份)
	public static String getYear(int i){
		SimpleDateFormat f = new SimpleDateFormat("yyyy");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -i);
	    String year = f.format(c.getTime());
	    return year;
	}
	public static String getAccStatus(String code){
		String accStatus="正常";
		if(code.equals("01")){
			accStatus="正常";
		}else if(code.equals("02")){
			accStatus="封存";
		}else if(code.equals("03")){
			accStatus="合并销户";
		}else if(code.equals("04")){
			accStatus="外部转出销户";
		}else if(code.equals("05")){
			accStatus="提取销户";
		}else if(code.equals("06")){
			accStatus="冻结";
		}else if(code.equals("99")){
			accStatus="其他";
		}
		return accStatus;
	}
	public static String getIsloan(String code){
		String isloan="否";
		if(code.equals("1")){
			isloan="否";
		}else if(code.equals("0")){
			isloan="是";
		}
		return isloan;
	}
}
