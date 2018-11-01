package app.service;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public static WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	
	public static String getAccState(String code){
		String accState="正常";
		if(code.equals("01")){
			accState="正常";
		}else if(code.equals("02")){
			accState="封存";
		}else if(code.equals("03")){
			accState="合并销户";
		}else if(code.equals("04")){
			accState="外部转出销户";
		}else if(code.equals("05")){
			accState="提取销户";
		}else if(code.equals("06")){
			accState="冻结";
		}else{
			accState="";
		}
		return accState;
	}
	public static String getIdType(String code){
		String idType="二代身份证";
		if(code.equals("01")){
			idType="二代身份证";
		}else if(code.equals("02")){
			idType="军官证";
		}else if(code.equals("03")){
			idType="护照";
		}else if(code.equals("04")){
			idType="外国人永久居住证";
		}else if(code.equals("99")){
			idType="其他";
		}else{
			idType="";
		}
		return idType;
	}
	//在网页上没有找到相关枚举类
	public static String getAccType(String code){
		String accType="一般公积金";
		if(code.equals("01")){
			accType="一般公积金";
		}else{
			accType="";
		}
		return accType;
	}
	//在网页上没有找到相关枚举类
	public static String getBorrowingMarks(String code){
		String borrowingMarks="贷";
		if(code.equals("02")){
			borrowingMarks="贷";
		}else if(code.equals("01")){
			borrowingMarks="借";
		}else{
			borrowingMarks="";
		}
		return borrowingMarks;
	}
}
