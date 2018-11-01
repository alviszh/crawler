package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangMedical;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;

@Component
public class InsuranceWeiFangMedicalParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取医疗保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance, Set<Cookie> cookies,String year) throws Exception {
		tracer.addTag("InsuranceWeiFangMedicalParser.getMedicalInfo",taskInsurance.getTaskid()+" "+year);
		WebParam webParam= new WebParam();	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");		
		String currentYear=sdf.format(new Date());
		String url="";
		if (year.equals(currentYear)) {
			url = "https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl";
		} else {
			url = "https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl?time=" + year;
		}
		WebRequest webRequest=  new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/zgyl");	
		HtmlPage page=webClient.getPage(webRequest);
		Thread.sleep(1500);	
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);  
		tracer.addTag("InsuranceWeiFangMedicalParser.getMedicalInfo"+year,"<xmp>"+html+"</xmp>");
		if(200 == statusCode){
	     	List<InsuranceWeiFangMedical> medicalList = htmlMedicalInfoParser(html,taskInsurance);	
	    	webParam.setMedicalList(medicalList);	    	
	    }
	   return webParam;
	}
    /**
	 * @Des 根据获取的获取养老保险缴费数据解析
	 * @param html  zcx
	 * @return
	 */
	private List<InsuranceWeiFangMedical>  htmlMedicalInfoParser(String html,TaskInsurance taskInsurance)throws Exception {
		List<InsuranceWeiFangMedical>  medicalList=new ArrayList<InsuranceWeiFangMedical>();
		if (null !=html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element  table=doc.select("table").last();
	     	if (null !=table) {
	     		Elements trs = table.select("tbody").select("tr");
	     		int trs_size = trs.size()-1;
				if (trs_size > 0) {
					for (int i = 0; i < trs_size; i++) {
						InsuranceWeiFangMedical medical=new InsuranceWeiFangMedical();
						Elements tds = trs.get(i).select("td");
						String rnum=tds.get(0).text();
						String beginMonth=tds.get(1).text();
						String endMonth=tds.get(2).text();
						String payPersonBase=tds.get(3).text();
						String payCompanyBase=tds.get(4).text();
						String payPersonAmount=tds.get(5).text();
						String payCompanyAmount=tds.get(6).text();
						medical.setRnum(rnum);
						medical.setBeginMonth(beginMonth);
						medical.setEndMonth(endMonth);
						medical.setPayPersonBase(payPersonBase);
						medical.setPayCompanyBase(payCompanyBase);
						medical.setPayCompanyAmount(payCompanyAmount);
						medical.setPayPersonAmount(payPersonAmount);
						medical.setTaskid(taskInsurance.getTaskid());
						medicalList.add(medical);
					}
				}
			}	
		}
		return medicalList;
	}
}
