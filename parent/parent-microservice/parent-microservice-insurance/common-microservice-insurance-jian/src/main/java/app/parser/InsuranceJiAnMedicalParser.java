package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnMedical;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceJiAnMedicalParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取医疗保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String year) throws Exception {
		tracer.addTag("InsuranceJiAnMedicalParser.getMedicalInfo",taskInsurance.getTaskid()+" "+year);
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String url="http://www.ja12333.cn/jayb/web/f50020103/getAc11PageAction.action";
		String requestBody="nd0000="+year+"&pageSize=100&start=0";
		WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.ja12333.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.ja12333.cn");
		webRequest.setRequestBody(requestBody);
		HtmlPage page=webClient.getPage(webRequest);
		Thread.sleep(1000);	
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);  
		tracer.addTag("InsuranceJiAnMedicalParser.getMedicalInfo"+year,"<xmp>"+html+"</xmp>");
		if(200 == statusCode){
	     	List<InsuranceJiAnMedical> medicalList = htmlMedicalInfoParser(html,taskInsurance);	
	    	webParam.setMedicalList(medicalList);	    	
	    }
	   return webParam;
	}
    /**
	 * @Des 根据获取的获取养老保险缴费数据解析
	 * @param html  zcx
	 * @return
	 */
	private List<InsuranceJiAnMedical>  htmlMedicalInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceJiAnMedical>  medicalList=new ArrayList<InsuranceJiAnMedical>();
		if (null != html && html.contains("result")) {
			JSONObject list1ArrayObjs = JSONObject.fromObject(html);
			String listObjsStr = list1ArrayObjs.getString("result");
			if (null != listObjsStr) {
				JSONArray listArray = JSONArray.fromObject(listObjsStr);
				for (int i = 0; i < listArray.size(); i++) {
					JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
					String usernum = listArrayObjs.getString("aac001");// 个人编号
					String username = listArrayObjs.getString("aac003");// 姓名
					String type = listArrayObjs.getString("aae140m");// 险种类型
					String personalBase = listArrayObjs.getString("aab120");// 个人缴费基数
					String personalPay = listArrayObjs.getString("aac123");// 个人缴费金额
					String unitPay = listArrayObjs.getString("aac125");// 单位缴费金额
					String financePay = listArrayObjs.getString("bac003");// 财政缴费金额
					String intoPerson = listArrayObjs.getString("aac125");// 划入个人账户金额
					String paymonth = listArrayObjs.getString("aae002");// 费款所属期
					InsuranceJiAnMedical medical = new InsuranceJiAnMedical(usernum, username, type, personalBase,
							personalPay, unitPay, financePay, intoPerson, paymonth, taskInsurance.getTaskid());
					medicalList.add(medical);

				}
			}
		}
		return medicalList;
	}
	
}
