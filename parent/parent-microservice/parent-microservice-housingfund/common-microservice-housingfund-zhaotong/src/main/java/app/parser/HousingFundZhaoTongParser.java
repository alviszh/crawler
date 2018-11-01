package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongBase;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundZhaoTongParser {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;
	

	public WebParam getUserInfo(String html, TaskHousing taskHousing) {
		WebParam webParam = new WebParam();
		
		Document document = Jsoup.parse(html);
		
		String dwmc = getNextLabelByKeyword(document, "td", "单位名称");
		String grzh = getNextLabelByKeyword(document, "td", "个人账号");
		String xm   = getNextLabelByKeyword(document, "td", "姓名");
		String sfz  = getNextLabelByKeyword(document, "td", "身份证");
		String gjzt = getNextLabelByKeyword(document, "td", "归集状态");
		String grjksj = getNextLabelByKeyword(document, "td", "个人缴款时间");
		String gzjs   =getNextLabelByKeyword(document, "td", "工资基数");
		String yjje = getNextLabelByKeyword(document, "td", "月缴金额");
		String gjye = getNextLabelByKeyword(document, "td", "归集余额");
		
		HousingZhaoTongBase base = new HousingZhaoTongBase();
		base.setCompanyName(dwmc);
		base.setPerNum(grzh);
		base.setName(xm);
		base.setNum(sfz);
		base.setNotionalStatus(gjzt);
		base.setPerPayTime(grjksj);
		base.setWagesBase(gzjs);
		base.setMonthPay(yjje);
		base.setNotionalBalance(gjye);
		base.setTaskid(taskHousing.getTaskid());
		webParam.setHtml(html);
		webParam.setZhaotongBase(base);
		return webParam;
	}
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}



	public WebParam<HousingZhaoTongBase> crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {
		String url = "http://www.ztgjj.com/gjjcq.php";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage page = webClient.getPage(webRequest);
		HtmlImage image = page.getFirstByXPath("//img[@id='ckstr']");
		WebRequest  requestSettings = new WebRequest(new URL("http://www.ztgjj.com/gjjcq.php?add=add"), HttpMethod.POST); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("vq3", messageLoginForHousing.getUsername()));
		requestSettings.getRequestParameters().add(new NameValuePair("vq4", messageLoginForHousing.getNum()));
		requestSettings.getRequestParameters().add(new NameValuePair("vq2", messageLoginForHousing.getPassword()));
		requestSettings.getRequestParameters().add(new NameValuePair("validate", chaoJiYingOcrService.getVerifycode(image, "3004")));
		requestSettings.setCharset(Charset.forName("UTF-8"));
		HtmlPage page2 = (HtmlPage)webClient.getPage(requestSettings); 
		if(page2.asXml().contains("单位名称")){
			WebParam<HousingZhaoTongBase> webParam = getUserInfo(page2.asXml(),taskHousing);
			return webParam;
		}
		return null;
	}
}
