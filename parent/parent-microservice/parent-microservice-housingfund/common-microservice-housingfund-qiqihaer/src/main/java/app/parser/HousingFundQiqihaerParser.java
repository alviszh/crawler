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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qiqihaer.HousingQiqihaerAccount;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundQiqihaerParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
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


	public WebParam<HousingQiqihaerAccount> crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception  {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url ="http://gjj.qqhr.gov.cn/Search_G.php";
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("select", "card"));
		requestSettings.getRequestParameters().add(new NameValuePair("text", messageLoginForHousing.getNum()));
		requestSettings.getRequestParameters().add(new NameValuePair("pws", messageLoginForHousing.getPassword()));
		requestSettings.getRequestParameters().add(new NameValuePair("Submit", "%CC%E1%BD%BB"));
		
		
		HtmlPage page = (HtmlPage)webClient.getPage(requestSettings); 
		String html3 = page.getWebResponse().getContentAsString();
		if(html3.contains("返回继续")){
			WebParam<HousingQiqihaerAccount> webParam = getAccountInfo(html3,taskHousing);
			webParam.setHtml(html3);
			webParam.setUrl(url);
			return webParam;
		}
		
		
		return null; 
		
	}


	private WebParam<HousingQiqihaerAccount> getAccountInfo(String html3, TaskHousing taskHousing) {
		WebParam webParam = new WebParam();
		
	
		Document document = Jsoup.parse(html3);
		Elements eles = document.getElementsByAttributeValue("bgcolor", "#CCCCCC");
		Element table = eles.select("table").first();
		Elements tr = eles.select("tr");
		for(int i =2;i<tr.size();i++){
			Elements tds = tr.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingQiqihaerAccount account = new HousingQiqihaerAccount();
				account.setNum(lists.get(0));
				account.setName(lists.get(1));
				account.setCompany(lists.get(2));
				account.setIdCard(lists.get(3));
				account.setBalance(lists.get(4));
				account.setStatus(lists.get(5));
				account.setTaskid(taskHousing.getTaskid());
				webParam.setHousingQiqihaerAccount(account);
				return webParam;
			}
		}
		return null;
	}

}
