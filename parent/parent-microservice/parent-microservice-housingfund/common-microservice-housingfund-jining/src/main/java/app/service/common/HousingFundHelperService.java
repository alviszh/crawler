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
	public static String getTwoYearAgoDate(){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -2);
	    c.add(Calendar.MONTH, 0);
	    c.add(Calendar.DAY_OF_MONTH, 0);
	    String yearAgo = f.format(c.getTime());
		return yearAgo;
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
	//摘要(没写全，找了常用的)
	public static String getSummary(String summaryCode){
		String summary="汇缴";
		if(summaryCode.equals("1219")){
			summary="汇缴";
		}else if(summaryCode.equals("1001")){
			summary="单位开户";
		}else if(summaryCode.equals("1002")){
			summary="单位转移(转入)";
		}else if(summaryCode.equals("1003")){
			summary="单位转移(转出)";
		}else if(summaryCode.equals("1011")){
			summary="单位销户";
		}else if(summaryCode.equals("1012")){
			summary="零余额销户";
		}else if(summaryCode.equals("1118")){
			summary="个人开户";
		}else if(summaryCode.equals("1201")){
			summary="个人转移（转出）";
		}else if(summaryCode.equals("1202")){
			summary="个人转移(转入)";
		}else if(summaryCode.equals("1228")){
			summary="正常提取";
		}else if(summaryCode.equals("1231")){
			summary="结息";
		}else if(summaryCode.equals("1233")){
			summary="销户提取";
		}else if(summaryCode.equals("1234")){
			summary="异地转出";
		}else{
			summary="其他";
		}
		return summary;
	}
	//摘要
	public static String getAccState(String code){
		String accState="正常";
	if(code.equals("0")){
			accState="正常";
		}else if(code.equals("1")){
			accState="封存";
		}else if(code.equals("2")){
			accState="空账";
		}else if(code.equals("9")){
			accState="销户";
		}
		return accState;
	}
}
