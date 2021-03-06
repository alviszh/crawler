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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingBirth;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class InsuranceChongqingBirthParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取生育保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getBirthInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String year,String currentPage) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getWebClient();		
		URL url  = new URL("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
	    WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);		
		//设置请求参数
	    requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
	    requestSettings.getRequestParameters().add(new NameValuePair("code", "062"));
	    requestSettings.getRequestParameters().add(new NameValuePair("year", year));
		if ("2".equals(currentPage)) {
			requestSettings.getRequestParameters().add(new NameValuePair("currentPage", currentPage));
			requestSettings.getRequestParameters().add(new NameValuePair("goPage", "")); 
		}	 		
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		  }
		requestSettings.setCharset(Charset.forName("UTF-8"));
		requestSettings.setAdditionalHeader("Host", "ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Origin", "http://ggfw.cqhrss.gov.cn");
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=062"); 
		requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); 
		requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page=webClient.getPage(requestSettings);
		Thread.sleep(1500);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效 		
	    int statusCode = page.getWebResponse().getStatusCode();
		webParam.setCode(statusCode);
     	webParam.setUrl("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("getBirthInfo",html);
		if(200 == statusCode){	   
    		List<InsuranceChongqingBirth>  birthList= htmlBirthInfoParser(html,taskInsurance);
    		webParam.setBirthList(birthList); 			
	    }
	   return webParam;
	}
	/**
	 * @Des 根据获取的获取生育险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceChongqingBirth>  htmlBirthInfoParser(String html,TaskInsurance taskInsurance)throws Exception {	
		List<InsuranceChongqingBirth>  birthList =new ArrayList<InsuranceChongqingBirth>();
		if (null !=html) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
			String message = object.get("message").getAsString();// 获取message值
			if (message.contains("操作成功")) {
				JsonArray jsonArray = object.get("result").getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject subObject = jsonArray.get(i).getAsJsonObject();
					 String payMonth = subObject.get("xssj").getAsString();// 缴费年月				
					 String companyName=subObject.get("dwmc").getAsString(); // 单位名称
					 String payPlace=subObject.get("cbd").getAsString();  // 参保地
					 String payBase=subObject.get("jfjs").getAsString();  // 缴费基数(元)
					 String payState=subObject.get("cbzt").getAsString() ; // 参保状态
					 String payMark=subObject.get("jfbj").getAsString(); // 缴费标记
					 String personNumber=subObject.get("grbh").getAsString();//个人编号	
				
					 String idNum=subObject.get("sfzh").getAsString();//身份证号
					 String paymentType=subObject.get("jflx").getAsString();//缴费类型
					 String companyNum=subObject.get("dwbh").getAsString();//缴费类型
					 String accountDate="";
					 if(subObject.get("dzrq")!=null){
						accountDate=subObject.get("dzrq").getAsString();//到帐日期
					 }				
					//重庆生育
					InsuranceChongqingBirth birth = new InsuranceChongqingBirth();				    
					birth.setPayMonth(payMonth);
					birth.setCompanyName(companyName);
					birth.setPayPlace(payPlace);
					birth.setPayBase(payBase);
					birth.setPayState(payState);
					birth.setPayMark(payMark);		
					birth.setPersonNumber(personNumber);
					birth.setIdNum(idNum);
					birth.setPaymentType(paymentType);
					birth.setCompanyNum(companyNum);
				    birth.setAccountDate(accountDate);
					birth.setTaskid(taskInsurance.getTaskid());
					birthList.add(birth);
				}
			}			
		}
		return birthList;
	}
}
