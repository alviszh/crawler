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
import com.microservice.dao.entity.crawler.insurance.chongqing.InsuranceChongqingMedical;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class InsuranceChongqingMedicalParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取医疗保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String year,String currentPage) throws Exception {
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		 URL url  = new URL("http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_query.do");
		WebRequest  requestSettings = new WebRequest(url, HttpMethod.POST);		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("code", "023"));
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
		requestSettings.setAdditionalHeader("Referer", "http://ggfw.cqhrss.gov.cn/ggfw/QueryBLH_main.do?code=023");
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
		tracer.addTag("getMedicalInfo",html);
		if(200 == statusCode){
	     	List<InsuranceChongqingMedical> medicalList = htmlMedicalInfoParser(html,taskInsurance);	
	    	webParam.setMedicalList(medicalList);	    	
	    	return webParam;	 
	    }
	   return webParam;
	}
    /**
	 * @Des 根据获取的获取养老保险缴费数据解析
	 * @param html  zcx
	 * @return
	 */
	private List<InsuranceChongqingMedical>  htmlMedicalInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceChongqingMedical>  medicalList =new ArrayList<InsuranceChongqingMedical>();
		if (null !=html) {
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
			String message = object.get("message").getAsString();// 获取message值
	     	if (message.contains("操作成功")) {
				JsonArray jsonArray = object.get("result").getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject subObject = jsonArray.get(i).getAsJsonObject();
					 String feeMonth = subObject.get("xssj").getAsString();// 对应费款所属期 ; 
					 String companyName= subObject.get("dwmc").getAsString() ; // 单位名称
					 String payPlace=subObject.get("cbd").getAsString(); // 参保地
					 String payBase=subObject.get("jfjs").getAsString(); // 缴费基数(元)
					 String personalPay=subObject.get("grjfje").getAsString();//个人缴费金额(元)
					 String payMark=subObject.get("jfbz").getAsString(); // 缴费标记
					 String payType=subObject.get("fkkm").getAsString();//费款科目	
					 String dealDate="";
					 if (subObject.get("jbrq") !=null) {
						dealDate=subObject.get("jbrq").getAsString();//经办日期
					  }				
					 String idNum=subObject.get("sfzh").getAsString();//身份证号
					 String name=subObject.get("xm").getAsString();//姓名
					 String personNumber=subObject.get("grbh").getAsString();//个人编号
					 String socialSecurityCardNum=subObject.get("ickh").getAsString();//社保卡号
					 String paymentType=subObject.get("jflx").getAsString();//缴费类型
					 String feeMonth2=subObject.get("fkssq").getAsString();//费款所属期
					 String companyNum=subObject.get("dwbh").getAsString();//单位编号
					//重庆医保
					InsuranceChongqingMedical medical = new InsuranceChongqingMedical();
					medical.setFeeMonth(feeMonth);
					medical.setCompanyName(companyName);
					medical.setPayPlace(payPlace);
					medical.setPayBase(payBase);
					medical.setPersonalPay(personalPay);
					medical.setPayMark(payMark);
					medical.setPayType(payType);
					medical.setDealDate(dealDate);
					medical.setIdNum(idNum);
					medical.setName(name);
					medical.setPersonNumber(personNumber);
					medical.setSocialSecurityCardNum(socialSecurityCardNum);
					medical.setPaymentType(paymentType);
					medical.setFeeMonth2(feeMonth2);
					medical.setCompanyNum(companyNum);
					medical.setTaskid(taskInsurance.getTaskid());
					medicalList.add(medical);
				}					
			}	
		}
		return medicalList;
	}
}
