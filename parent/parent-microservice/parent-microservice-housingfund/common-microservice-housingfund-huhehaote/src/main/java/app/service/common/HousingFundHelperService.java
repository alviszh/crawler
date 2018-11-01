package app.service.common;

import java.text.SimpleDateFormat;
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
	//摘要
	public static String getTransStatus(String code){
		String transStatus="正常";
		if(code.equals("0")){
			transStatus="正常";
		}else if(code.equals("1")){
			transStatus="红字";
		}else if(code.equals("2")){
			transStatus="蓝字";
		}else if(code.equals("6")){
			transStatus="撤销";
		}else if(code.equals("9")){
			transStatus="抹账";
		}else if(code.equals("8")){
			transStatus="冲账";
		}else if(code.equals("7")){
			transStatus="当日冲正";
		}
		return transStatus;
	}
}
