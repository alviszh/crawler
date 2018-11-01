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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingPaydetails;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
@Component
public class InsuranceAnQingPersionParser {
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取养老保险缴费详情页面
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam getPaydetails(TaskInsurance taskInsurance,Set<Cookie> cookies) throws Exception {
		tracer.addTag("InsuranceAnQingPersionParser.getPaydetails",taskInsurance.getTaskid());
		WebParam webParam= new WebParam();			
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}		
		String url = "http://220.179.13.107:7001/webeps/sbyw/jbbuiness/getMvac07.do";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("pageNumber", "1"));
		paramsList.add(new NameValuePair("totalPages", "1"));
		paramsList.add(new NameValuePair("pageSize", "10000"));
		paramsList.add(new NameValuePair("wcm_sys_pageGo", "1"));
		paramsList.add(new NameValuePair("mv_ac07.aae140", ""));
		paramsList.add(new NameValuePair("mv_ac07.aae002", ""));
		paramsList.add(new NameValuePair("mv_ac07.aae003", ""));
		paramsList.add(new NameValuePair("mv_ac07.aae004", ""));
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "220.179.13.107:7001");
		webRequest.setAdditionalHeader("Referer", "http://220.179.13.107:7001/webeps/sbyw/jbbuiness/getMvac07.do");
		webRequest.setRequestParameters(paramsList);
		Page page = webClient.getPage(webRequest);	  
		Thread.sleep(1500);
	    int statusCode = page.getWebResponse().getStatusCode();
	    webParam.setCode(statusCode);
     	webParam.setUrl(url);
    	String html = page.getWebResponse().getContentAsString();
		webParam.setHtml(html);
	 	tracer.addTag("InsuranceAnQingPersionParser.getPaydetails","<xmp>"+html+"</xmp>");
		if(200 == statusCode){
    	    List<InsuranceAnQingPaydetails> paydetails = htmlPaydetailsInfoParser(html,taskInsurance);	    	
	    	webParam.setInsuranceAnQingPaydetails(paydetails);	
	    }
	   return webParam;
	}	
	/**
	 * @Des 根据获取的获取养老保险缴费详情页面解析具体信息
	 * @param html
	 * @return
	 */
	private List<InsuranceAnQingPaydetails> htmlPaydetailsInfoParser(String html, TaskInsurance taskInsurance)
			throws Exception {
		List<InsuranceAnQingPaydetails> paydetails = new ArrayList<InsuranceAnQingPaydetails>();
		if (null != html) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element table = doc.select("table").get(2);
			if (null != table) {
				Elements trs = table.select("tbody").select("tr");
				int trs_size = trs.size();
				if (trs_size > 0) {
					for (int i = 1; i < trs_size; i++) {
						Elements tds = trs.get(i).select("td");
						String type = tds.get(1).text();// 险种类型
						String beginmonth = tds.get(2).text();// 建账年月
						String accountbeginmonth = tds.get(3).text();// 账目起始年月
						String accountendmonth = tds.get(4).text();// 账目截止年月
						String personalbase = tds.get(5).text();// 个人缴费基数
						String companyamount = tds.get(6).text();// 单位缴费金额
						String companyratio = tds.get(7).text();// 单位缴费金额
						String personalratio = tds.get(8).text();// 个人缴费比例
						String personalamount = tds.get(9).text();// 个人缴费金额
						String overduefine = tds.get(10).text();// 滞纳金
						String interest = tds.get(11).text();// 利息
						String checksign = tds.get(12).text();// 核实标志
						String checktime = tds.get(13).text();// 核实时间
						InsuranceAnQingPaydetails paydetail = new InsuranceAnQingPaydetails(type, beginmonth,
								accountbeginmonth, accountendmonth, personalbase, companyratio, companyamount,
								personalratio, personalamount, overduefine, interest, checksign, checktime,
								taskInsurance.getTaskid());
						paydetails.add(paydetail);
					}
				}
			}
		}
		return paydetails;
	}
}
