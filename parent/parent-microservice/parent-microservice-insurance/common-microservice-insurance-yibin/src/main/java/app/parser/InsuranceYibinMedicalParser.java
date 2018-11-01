package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yibin.InsuranceYinbinMedical;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class InsuranceYibinMedicalParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取医疗保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {
		tracer.addTag("InsuranceYibinMedicalParser.getMedicalInfo",taskInsurance.getTaskid());
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String url="http://cx2.ybxww.cn:6655/hrss.asp?id=c2";	
		WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "cx2.ybxww.cn:6655");
		webRequest.setAdditionalHeader("Referer", "http://cx2.ybxww.cn:6655/left.asp");	
		HtmlPage page=webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 	
		Thread.sleep(1500);	
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);  
		tracer.addTag("InsuranceYibinMedicalParser.getMedicalInfo","<xmp>"+html+"</xmp>");
		if(200 == statusCode){
	     	List<InsuranceYinbinMedical> medicalList = htmlMedicalInfoParser(html,taskInsurance);	
	    	webParam.setMedicalList(medicalList);	    	
	    }
	   return webParam;
	}
    /**
	 * @Des 根据获取的获取养老保险缴费数据解析
	 * @param html  zcx
	 * @return
	 */
	private List<InsuranceYinbinMedical>  htmlMedicalInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceYinbinMedical>  medicalList=new ArrayList<InsuranceYinbinMedical>();
		if (null !=html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element  table=doc.select("table").last();
	     	if (null !=table) {
	     		Elements trs = table.select("tr");
	     		int trs_size = trs.size()-1;
				if (trs_size >0) {
					for (int i = 1; i < trs_size; i++) {					
						Elements tds = trs.get(i).select("td");
						String useraccount=tds.get(0).text();
						String type=tds.get(1).text();
						String paymonth=tds.get(2).text();
						String personType=tds.get(3).text();
						String paybase=tds.get(4).text();
						String companyAmount=tds.get(5).text();
						String personAmount=tds.get(6).text();
						String totalAmount=tds.get(7).text();
						String companyActuallyAmount=tds.get(8).text();
						String personActuallyAmount=tds.get(9).text();
						String totalActuallyAmount=tds.get(10).text();
						String paySign=tds.get(11).text();
						String companyName=tds.get(12).text();
						InsuranceYinbinMedical	medical=new InsuranceYinbinMedical(useraccount,type,paymonth,personType,paybase,
								 companyAmount,personAmount,totalAmount,companyActuallyAmount,
								 personActuallyAmount,totalActuallyAmount,paySign,companyName,
								 taskInsurance.getTaskid());
						medicalList.add(medical);
					}
				}
			}	
		}
		return medicalList;
	}
}
