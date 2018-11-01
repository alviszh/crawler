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
	//获取单位账户状态
	public static String getUnitAccState(String unitAccStateCode){
		String unitAccState="正常";
		if(unitAccStateCode.equals("0")){
			unitAccState="正常";
		}else if(unitAccStateCode.equals("1")){
			unitAccState="封存";
		}else if(unitAccStateCode.equals("2")){
			unitAccState="新开户";
		}else if(unitAccStateCode.equals("9")){
			unitAccState="销户";
		}
		return unitAccState;
	}
	//获取短信发送标识
	public static String getSmsFlag(String smsFlagCode){
		String smsFlag="不发短信";
		if(smsFlagCode.equals("2")){
			smsFlag="不发短信";
		}else{  // 1
			smsFlag="发短信";
		}
		return smsFlag;
	}
	//获取账户状态
	public static String getIndiAccState(String indiAccStateCode){
		String indiAccState="正常";
		if(indiAccStateCode.equals("0")){
			indiAccState="正常";
		}else if(indiAccStateCode.equals("1")){
			indiAccState="封存";
		}else if(indiAccStateCode.equals("3")){
			indiAccState="转入封存";
		}else if(indiAccStateCode.equals("9")){
			indiAccState="销户";
		}
		return indiAccState;
	}
	
}
