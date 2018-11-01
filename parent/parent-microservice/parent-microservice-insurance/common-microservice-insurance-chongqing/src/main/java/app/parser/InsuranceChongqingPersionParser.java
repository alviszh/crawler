package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingPersion;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
@Component
public class InsuranceChongqingPersionParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取养老保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getPersionInfo(TaskInsurance taskInsurance,Set<Cookie> cookies,String year,String currentPage) throws Exception {
		WebParam webParam= new WebParam();			
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
	    URL url  = new URL("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
		WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("code", "015"));
		requestSettings.getRequestParameters().add(new NameValuePair("year", year));
		if ("2".equals(currentPage)) {
			requestSettings.getRequestParameters().add(new NameValuePair("currentPage", currentPage));
			requestSettings.getRequestParameters().add(new NameValuePair("goPage", currentPage));  	
		}			
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		  }
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=015");
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");		  
		
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 		
		Page page = webClient.getPage(requestSettings); 		
		Thread.sleep(1500);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTagWrap("getPersionInfo",html);
		if(200 == statusCode){
    	    List<InsuranceChongqingPersion> chongqingPersionList = htmlPersionInfoParser(html,taskInsurance);	    	
	    	webParam.setPersionList(chongqingPersionList); 		
	    }
	   return webParam;
	}	
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceChongqingPersion>  htmlPersionInfoParser(String html,TaskInsurance taskInsurance) throws Exception{
		List<InsuranceChongqingPersion>  personList =new ArrayList<InsuranceChongqingPersion>();
		if (null != html) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
			String message = object.get("message").getAsString();// 获取message值		
			if (message.contains("操作成功")) {
				JsonArray jsonArray = object.get("result").getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject subObject = jsonArray.get(i).getAsJsonObject();
					String feeMonth = subObject.get("xssj").getAsString();// 对应费款所属期
					String companyName = subObject.get("dwmc").getAsString();// 单位名称
					String payPlace = subObject.get("cbd").getAsString();// 参保地
					String payBase = subObject.get("jfjs").getAsString();// 参保基数
					String personalPay = subObject.get("grjfje").getAsString();// 个人缴费金额(元)
					String payMark = subObject.get("jfbz").getAsString();// 缴费标志
					String companyNum = subObject.get("dwbh").getAsString();// 单位编号
					String personNumber = subObject.get("grbh").getAsString();// 个人编号
					String idNum = subObject.get("sfzh").getAsString();// 身份证号
					String name = subObject.get("xm").getAsString();// 姓名
					String interestAcrossYear = subObject.get("bjgrknlx").getAsString();// 补缴本人跨年利息
					String interestYear = subObject.get("bjgrbnlx").getAsString();// 补缴本人本年利息
					String paymentType = subObject.get("jflx").getAsString();// 缴费类型
					String accountDate="";
					if (subObject.get("dzrq")!=null) {
						accountDate=subObject.get("dzrq").getAsString();// 到账日期
					}					
					// 重庆社保
					InsuranceChongqingPersion person = new InsuranceChongqingPersion();
					person.setFeeMonth(feeMonth);
					person.setCompanyName(companyName);
					person.setPayPlace(payPlace);
					person.setPayBase(payBase);
					person.setPersonalPay(personalPay);
					person.setPayMark(payMark);
					person.setCompanyNum(companyNum);
					person.setPersonNumber(personNumber);
					person.setIdNum(idNum);
					person.setName(name);
					person.setInterestAcrossYear(interestAcrossYear);
					person.setInterestYear(interestYear);
					person.setPaymentType(paymentType);
					person.setAccountDate(accountDate);
					person.setTaskid(taskInsurance.getTaskid());
					personList.add(person);
				}
			}
		}
		return personList;
	}
}
