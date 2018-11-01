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
import com.microservice.dao.entity.crawler.insurance.jian.InsuranceJiAnPersion;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
@Component
public class InsuranceJiAnPersionParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取养老保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getPersionInfo(TaskInsurance taskInsurance,Set<Cookie> cookies) throws Exception {
		tracer.addTag("InsuranceJiAnPersionParser.getPersionInfo",taskInsurance.getTaskid());
		WebParam webParam= new WebParam();			
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}	
		String url = "http://jasi12333.xicp.net:58699/jxquery/person/account_mx.jsp";		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "jasi12333.xicp.net:58699");
		webRequest.setAdditionalHeader("Referer", "http://jasi12333.xicp.net:58699/jxquery/person/account_mx.jsp");
		HtmlPage page = webClient.getPage(webRequest);	  
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
    	if (!html.contains("人员账户月记账明细")) {
    		page=getHtml(url,webClient);
    		html=page.getWebResponse().getContentAsString();
		}
		webParam.setHtml(html);
	 	tracer.addTag("InsuranceJiAnPersionParser.getPersionInfo","<xmp>"+html+"</xmp>");
		if(200 == statusCode){
    	    List<InsuranceJiAnPersion> persionList = htmlPersionInfoParser(html,taskInsurance);	    	
	    	webParam.setPersionList(persionList); 		
	    }
	   return webParam;
	}	
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceJiAnPersion> htmlPersionInfoParser(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceJiAnPersion> personList = new ArrayList<InsuranceJiAnPersion>();
		if (null != html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element table = doc.select("table").last();
			if (null != table) {
				Elements trs = table.select("tbody").select("tr");
				int trs_size = trs.size()-2;
				if (trs_size > 0) {
					for (int i = 1; i <= trs_size; i++) {
						Elements tds = trs.get(i).select("td");
						String paymonth = tds.get(0).text();
						String base = tds.get(1).text();
						String type = tds.get(2).text();
						String monthCount = tds.get(3).text();
						String personalPay = tds.get(4).text();
						String unitPay = tds.get(5).text();
						String intoPerson = tds.get(6).text();
						String intoCompany = tds.get(7).text();				
						String accountSign = tds.get(8).text();
						String accountMonth = tds.get(9).text();
						InsuranceJiAnPersion persion=new InsuranceJiAnPersion( paymonth,  base,  type,  monthCount,  personalPay,
								 unitPay,  intoPerson,  intoCompany,  accountSign,
								 accountMonth,  taskInsurance.getTaskid());
						personList.add(persion);
					}
				}
			}
		}
		return personList;
	}	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
