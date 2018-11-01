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
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangPersion;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
@Component
public class InsuranceWeiFangPersionParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取养老保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getPersionInfo(TaskInsurance taskInsurance,Set<Cookie> cookies,String year) throws Exception {
		tracer.addTag("InsuranceWeiFangPersionParser.getPersionInfo",taskInsurance.getTaskid()+" "+year);
		WebParam webParam= new WebParam();			
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");		
		String currentYear=sdf.format(new Date());
		String url = "";
		if (year.equals(currentYear)) {
			url = "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls";
		}else {
			url = "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls?time=" + year;
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.sdwfhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "https://www.sdwfhrss.gov.cn/rsjwz/self/yljfls");	
		HtmlPage page = webClient.getPage(webRequest);	  
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("InsuranceWeiFangPersionParser.getPersionInfo"+year,"<xmp>"+html+"</xmp>");
		if(200 == statusCode){
    	    List<InsuranceWeiFangPersion> persionList = htmlPersionInfoParser(html,taskInsurance);	    	
	    	webParam.setPersionList(persionList); 		
	    }
	   return webParam;
	}	
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceWeiFangPersion> htmlPersionInfoParser(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceWeiFangPersion> personList = new ArrayList<InsuranceWeiFangPersion>();
		if (null != html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element table = doc.select("table").last();
			if (null != table) {
				Elements trs = table.select("tbody").select("tr");
				int trs_size = trs.size()-1;
				if (trs_size > 0) {
					for (int i = 0; i < trs_size; i++) {
						InsuranceWeiFangPersion persion = new InsuranceWeiFangPersion();
						Elements tds = trs.get(i).select("td");
						String rnum = tds.get(0).text();
						String beginMonth = tds.get(1).text();
						String endMonth = tds.get(2).text();
						String payPersonBase = tds.get(3).text();
						String payCompanyBase = tds.get(4).text();
						String payPersonAmount = tds.get(5).text();
						String payCompanyAmount = tds.get(6).text();
						persion.setRnum(rnum);
						persion.setBeginMonth(beginMonth);
						persion.setEndMonth(endMonth);
						persion.setPayPersonBase(payPersonBase);
						persion.setPayCompanyBase(payCompanyBase);
						persion.setPayCompanyAmount(payCompanyAmount);
						persion.setPayPersonAmount(payPersonAmount);
						persion.setTaskid(taskInsurance.getTaskid());
						personList.add(persion);
					}
				}
			}
		}
		return personList;
	}
}
