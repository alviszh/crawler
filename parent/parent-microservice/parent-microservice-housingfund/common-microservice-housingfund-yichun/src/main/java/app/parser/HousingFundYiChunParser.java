package app.parser;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yichun.HousingYiChunBase;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundYiChunParser {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	


	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		String url = "http://www.yczfgjj.com/Model/Query/Query2.aspx";
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if(status == 200){
			HtmlTextInput num = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtIDCode']");
			HtmlPasswordInput pwd = page.getFirstByXPath("//input[@id='txtPWD']");
			num.setText(messageLoginForHousing.getNum());
			pwd.setText(messageLoginForHousing.getPassword());
			HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='Button1']");
			HtmlPage loadPage = button.click();
			
			String html =loadPage.getWebResponse().getContentAsString(); 
			if(html.contains("单位月交")){
				WebParam<HousingYiChunBase> webParam1 = getBaseInfo(html,taskHousing);
				tracer.addTag("HousingYiChunParser.getBaseInfo 个人公积金信息" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				return webParam1;
			}
		}
		return null;
	}
	private WebParam<HousingYiChunBase> getBaseInfo(String html, TaskHousing taskHousing) {
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByAttributeValue("color","red");
		String xm = html.substring(html.indexOf("名：")+2,html.indexOf("<br>&nbsp;身份证"));
		HousingYiChunBase base = new HousingYiChunBase();
		base.setName(xm);
		base.setNum(baseInfo.get(1).text());
		base.setCompCode(baseInfo.get(2).text());
		base.setPerCode(baseInfo.get(3).text());
		base.setPerMonthPay(baseInfo.get(4).text());
		base.setCompMonthPay(baseInfo.get(5).text());
		base.setBalance(baseInfo.get(6).text());
		base.setPayMonth(baseInfo.get(7).text());
		base.setCompanyName(baseInfo.get(8).text());
		base.setTaskid(taskHousing.getTaskid());
		webParam.setHtml(html);
		webParam.setYichunBase(base);
		return webParam;
	}
}
