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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingLostwork;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class InsuranceChongqingLostworkParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取失业保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getLostworkInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String year,String currentPage) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		URL url  = new URL("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
	    WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);	   		
		//设置请求参数
	    requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
	    requestSettings.getRequestParameters().add(new NameValuePair("code", "043"));
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
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=043");
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
     	tracer.addTag("getLostworkInfo",html);
		if(200 == statusCode){	
	    	List<InsuranceChongqingLostwork> lostworkList = htmlLostworkInfoParser(html,taskInsurance);	
	    	webParam.setLostworkList(lostworkList);			
	    }
	   return webParam;
	}
	/**
	 * @Des 根据获取的获取失业险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceChongqingLostwork>  htmlLostworkInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceChongqingLostwork>  lostworkList =new ArrayList<InsuranceChongqingLostwork>();	
		if (null != html) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
			String message = object.get("message").getAsString();// 获取message值
			if (message.contains("操作成功")) {
				JsonArray jsonArray = object.get("result").getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject subObject = jsonArray.get(i).getAsJsonObject();
					String feeMonth = "";
					if(subObject.get("xssj") !=null){
						feeMonth=subObject.get("xssj").getAsString();// 费款所属期
					}
							
					String companyName ="";
					if(subObject.get("dwmc") !=null){
						companyName=subObject.get("dwmc").getAsString();// 单位名称						
					}
					String payPlace ="";
					if (subObject.get("cbd") !=null) {
						payPlace=subObject.get("cbd").getAsString();// 参保地
					}
				
					String payBase = "";
					if (subObject.get("jfjs") !=null) {
						payBase=subObject.get("jfjs").getAsString();// 参保基数
					}					
					String personalPay="";
					if (subObject.get("grjfje") !=null) {
						 personalPay = subObject.get("grjfje").getAsString();// 个人缴费金额(元)
					}
					String payMark = subObject.get("jfbj").getAsString();// 缴费标志			
					String personNumber = subObject.get("grbh").getAsString();// 个人编号
					String idNum = subObject.get("sfzh").getAsString();// 身份证号
					String paymentType=subObject.get("jflx").getAsString();// 缴费类型
					String accountDate="";
					if (subObject.get("dzrq")!=null) {
						accountDate=subObject.get("dzrq").getAsString();// 到账日期
					}					
					String feeMonth2 = subObject.get("fkssq").getAsString();// 费款所属期
					String socialSecurityCardNum = subObject.get("jbjgmc").getAsString();// 社会保险经办机构名称
					String companyNum = subObject.get("dwbh").getAsString();// 单位编号
					//重庆失业					
					InsuranceChongqingLostwork loatwork = new InsuranceChongqingLostwork();
					loatwork.setFeeMonth(feeMonth);
					loatwork.setCompanyName(companyName);
					loatwork.setPayPlace(payPlace);
					loatwork.setPayBase(payBase);
					loatwork.setPersonalPay(personalPay);
					loatwork.setPayMark(payMark);	
					loatwork.setPersonNumber(personNumber);
					loatwork.setIdNum(idNum);
					loatwork.setPaymentType(paymentType);
					loatwork.setAccountDate(accountDate);
					loatwork.setFeeMonth2(feeMonth2);
					loatwork.setSocialSecurityCardNum(socialSecurityCardNum);
					loatwork.setCompanyNum(companyNum);
					loatwork.setTaskid(taskInsurance.getTaskid());
					lostworkList.add(loatwork);
				}
			}

		}	
		return lostworkList;
	}
}
