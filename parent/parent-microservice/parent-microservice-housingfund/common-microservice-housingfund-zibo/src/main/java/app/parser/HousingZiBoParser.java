package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zibo.HousingZiBoUserinfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingZiBoParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam crawler(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.crawler.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		String url = "http://118.190.12.70/search/AccumulationFund.aspx";
		tracer.addTag("parser.login.parser.crawler.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage1 = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.crawler.page1", "<xmp>"+loginPage1.asXml()+"</xmp>");
		HtmlImage vc1 = loginPage1.getFirstByXPath("//img[@id='vc']");
		if(null != vc1){
			HtmlPage loginPage = (HtmlPage) vc1.click();
			tracer.addTag("parser.login.parser.crawler.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
			if(loginPage.asXml().contains("请同时输入账号和身份证号并点击")){
				HtmlImage vc = loginPage.getFirstByXPath("//img[@id='vc']");
				HtmlImage loginbtn = loginPage.getFirstByXPath("//img[@style=' cursor:hand; padding-top:5px;']");
				HtmlTextInput t1 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t1']");
				HtmlTextInput t2 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t2']");
				HtmlTextInput t3 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t3']");
				t1.setText(messageLoginForHousing.getHosingFundNumber());
				t2.setText(messageLoginForHousing.getNum());
				t3.setText(chaoJiYingOcrService.getVerifycode(vc, "1004"));
				HtmlPage loginedPage = (HtmlPage) loginbtn.click();
				webParam.setHtmlPage(loginedPage);
				tracer.addTag("parser.login.parser.crawler.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
				if(200 == loginedPage.getWebResponse().getStatusCode()){
					Document document = Jsoup.parse(loginedPage.asXml());
					Elements divs = document.select(".tbarea");
					if(null != divs && divs.size() > 2){
						Element div = divs.get(1);
						String text = div.text()+" ";
						if(text.contains("姓名")){
							System.out.println(text);
							int i = text.indexOf("缴至年月： ")+6;
							int o = text.indexOf("姓名： ")+4;
							int p = text.indexOf("余额： ")+4;
							
							String payDate = text.substring(i, text.indexOf(" ", i));
							String name = text.substring(o, text.indexOf(" ", o));
							String balance = text.substring(p, text.indexOf(" ", p));
							HousingZiBoUserinfo userinfo = new HousingZiBoUserinfo();
							userinfo.setPayDate(payDate);
							userinfo.setName(name);
							userinfo.setBalance(balance);
							if(null != userinfo){
								userinfo.setTaskid(taskHousing.getTaskid());
								List<HousingZiBoUserinfo> lists = new ArrayList<HousingZiBoUserinfo>();
								webParam.setList(lists);
							}
						}else{
							webParam.setHtml(text);
							tracer.addTag("parser.login.parser.crawler.error4", text);
						}
						
					}else{
						webParam.setHtml("查询页面数据异常，请您稍后重试。");
						tracer.addTag("parser.login.parser.crawler.error3", "查询页面没有存放数据的div");
					}
				}else{
					webParam.setHtml("查询页面异常，请您稍后重试。");
					tracer.addTag("parser.login.parser.crawler.error2", "查询页面状态码不是200");
				}
			}else{
				webParam.setHtml("网络异常，请您稍后重试。");
				tracer.addTag("parser.login.parser.crawler.error1", "查询页面异常。");
			}
			
		}
		return webParam;
	}
}
