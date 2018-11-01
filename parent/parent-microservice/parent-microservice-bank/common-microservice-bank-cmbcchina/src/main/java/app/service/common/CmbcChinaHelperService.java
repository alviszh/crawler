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
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:56:59 
 */
@Component
public class CmbcChinaHelperService {
	public static final Logger log = LoggerFactory.getLogger(CmbcChinaHelperService.class);
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public WebClient addcookie(TaskBank taskBank) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskBank.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	//由于民生银行页面容易卡死，故在进行相关操作后杀死进程
	public static void killProcess(){
		Runtime runTime = Runtime.getRuntime();
		try {
			runTime.exec("TASKKILL /F /IM iexplore.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getAcTypeName(String acTypeCode){
		String acTypeName="其他";
		if(acTypeCode.equals("03")){
			acTypeName="民生卡";
		}else if(acTypeCode.equals("01")){
			acTypeName="活期一本通";
		}else if(acTypeCode.equals("06")){
			acTypeName="其他";
		}
		return acTypeName;
	}
	
	//从当前月开始往前推i个月
	public static String getBeforeMonth(int i){
	    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.MONTH, -i);
	    String beforeMonth = f.format(c.getTime());
	    return beforeMonth;
	}
	
	//从当前年开始往前推i年
	public static String getBeforeYear(int i){
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.YEAR, -i);
	    String beforeYear = f.format(c.getTime());
//			    System.out.println(fiveYearAgo);
	    return beforeYear;
	}
	//==================================================================
	//信用卡传参数，需要的只是年和月
	public static String getCreditPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyMM");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	
	//从当前月开始往前推i个月
	public static String getCreditBeforeMonth(int i){
	    SimpleDateFormat f = new SimpleDateFormat("yyMM");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.MONTH, -i);
	    String beforeMonth = f.format(c.getTime());
	    return beforeMonth;
	}
	
	//民生银行信用卡相关参数的解析
	public static String getCurrencyName(String currencyFlag){
		String currencyName="人民币";
		if(currencyFlag.equals("L")){
			currencyName="人民币";
		}else if(currencyFlag.equals("I")){
			currencyName="美元";
		}else {
			currencyName="其他币种";
		}
		return currencyName;
	}
	public static String getCardMainFlag(String cardMainFlagCode){
		String getCardMainFlag="主卡";
		if(cardMainFlagCode.equals("1")){
			getCardMainFlag="主卡";
		}else {
			getCardMainFlag="附卡";
		}
		return getCardMainFlag;
	}
	public static String getCardState(String cardStateCode){
		String getCardState="正常";
		if(cardStateCode.equals("0")){
			getCardState="正常";
		}else {
			getCardState="非正常";   //其他状态并不知道，有可能是挂失或者其他
		}
		return getCardState;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(getBeforeYear(1));
//		System.out.println(getBeforeMonth(3));
//		System.out.println(11-4*3);
//		System.out.println(getCreditPresentDate());
		System.out.println(getCreditBeforeMonth(12));
	}
	
}
