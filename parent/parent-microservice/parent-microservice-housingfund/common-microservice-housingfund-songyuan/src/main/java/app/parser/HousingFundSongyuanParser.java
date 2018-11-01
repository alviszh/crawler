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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanAccount;
import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundSongyuanParser {

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


	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception  {
		String url = "http://www.syszfgjj.com/";
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		if(status == 200){
			HtmlTextInput num = (HtmlTextInput)page.getFirstByXPath("//input[@id='identity']");
			HtmlImage image = page.getFirstByXPath("//img[@height='30']");
			String code = chaoJiYingOcrService.getVerifycode(image, "3004");
			HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='AuthCodeId']");
			num.setText(messageLoginForHousing.getNum());
			verifyCode.setText(code);
			HtmlElement button = (HtmlElement)page.getFirstByXPath("//img[@value='提交']");
			
			HtmlPage loadPage = button.click();
			
			webParam.setCode(loadPage.getWebResponse().getStatusCode());
			webParam.setPage(loadPage);
			webParam.setWebClient(webClient);
		}
		
		return webParam;
	}


	public WebParam<HousingSongYuanAccount> getAccountInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();	
		String url = "http://www.syszfgjj.com/show.jsp?gcid=10235187&identity="+messageLoginForHousing.getNum();
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Host","www.syszfgjj.com");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
			List<HtmlPage> pageList = new ArrayList<HtmlPage>();
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			
			List<HousingSongYuanAccount> infoList = new ArrayList<HousingSongYuanAccount>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSongyuanParser.getAccountInfo 账户信息" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				infoList = htmlParserAccount(html,messageLoginForHousing,infoList);
				if(null != infoList){
					webParam.setList(infoList);
					webParam.setHtml(html);
					webParam.setUrl(url);
					return webParam;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	


	private List<HousingSongYuanAccount> htmlParserAccount(String html, MessageLoginForHousing messageLoginForHousing, List<HousingSongYuanAccount> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.select("tr[height='30']");
		for(int i =1;i<baseInfo.size();i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingSongYuanAccount account = new HousingSongYuanAccount();
				account.setName(lists.get(0));
				account.setCompanyCode(lists.get(1));
				account.setPersonCode(lists.get(2));
				account.setWageSettlement(lists.get(3));
				account.setCompanyMonthPay(lists.get(4));
				account.setPersonMonthPay(lists.get(5));
				account.setMonthPay(lists.get(6));
				account.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(account);
			}
		}
		return infoList;
	}





	public WebParam getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url ="http://www.syszfgjj.com/show.jsp?gcid=10235187&identity="+messageLoginForHousing.getNum();
		WebRequest requestSettingss = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage pages = webClient.getPage(requestSettingss);
		
		String htmls = pages.getWebResponse().getContentAsString();
		
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		List<HousingSongYuanDetail>  infoList = new ArrayList<HousingSongYuanDetail>();
//		for( int i = 1;i<;i++)
		return null;
	}

	
}
