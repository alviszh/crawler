package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuBasic;
import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundYiWuParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	

	//身份证登陆
	public WebParam loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		//登陆界面
		WebParam webParam = new WebParam();
		String url = "http://122.226.76.37/Account/Logon?returnUrl=%2Fpsl";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage page = webClient.getPage(webRequest);
		HtmlImage image = page.getFirstByXPath("//img[@onclick='getPic(this)']");
		WebRequest  requestSettings = new WebRequest(new URL("http://122.226.76.37/Account/Logon?returnUrl=%2Fpsl"), HttpMethod.POST); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("UserName", messageLoginForHousing.getNum()));
		requestSettings.getRequestParameters().add(new NameValuePair("Password", messageLoginForHousing.getPassword()));
		requestSettings.getRequestParameters().add(new NameValuePair("Racha", chaoJiYingOcrService.getVerifycode(image, "1005")));
		requestSettings.setCharset(Charset.forName("UTF-8"));
		HtmlPage page2 = (HtmlPage)webClient.getPage(requestSettings); 
		webParam.setHtml(page2.getWebResponse().getContentAsString()); 
		webParam.setUrl(url); 
		webParam.setWebClient(webClient); 
		return webParam; 
	}


	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(keyword+":contains("+tag+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}

//	@Async
	public WebParam<HousingYiWuBasic> getBasicInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam<HousingYiWuBasic> webParam = new WebParam<HousingYiWuBasic>();
		String cookies = taskHousing.getCookies(); 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies); 
		for (Cookie cookie : cookies1) { 
		webClient.getCookieManager().addCookie(cookie); 
		}
		String url = "http://122.226.76.37/psl";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page =webClient.getPage(webRequest); 
		String html = page.getWebResponse().getContentAsString();
		HousingYiWuBasic basic = htmlParserBasic(html,messageLoginForHousing);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setHtml(html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHousingYiWuBasic(basic);
		return webParam;
	}
	
	
//	@Async
	public WebParam<HousingYiWuDetail> getPayInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception{
		WebParam<HousingYiWuDetail> webParam= new WebParam<HousingYiWuDetail>();	
		String url = "http://122.226.76.37/psl/detail";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Host","122.226.76.37");
		webRequest.setAdditionalHeader("Referer","http://122.226.76.37/psl");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36");
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		Page page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		List<HousingYiWuDetail>  infoList = new ArrayList<HousingYiWuDetail>();
		if(200==statusCode){
			String html  =page.getWebResponse().getContentAsString();
			tracer.addTag("HousingYiwuParser.getDetailInfo 公积金明细" + messageLoginForHousing.getTask_id(),
					"<xmp>" + html + "</xmp>");
			infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
			if(null !=infoList){
    			webParam.setList(infoList);
    			webParam.setUrl(url);
    			webParam.setHtml(html);
    			return webParam;
    		}
	   }
		return null;
	}

	private List<HousingYiWuDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingYiWuDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByClass("grid").select("tr");
		for(int i =1;i<baseInfo.size();i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				HousingYiWuDetail detail =  new HousingYiWuDetail();
				detail.setDate(lists.get(0));
				detail.setAbs(lists.get(1));
				detail.setDrawMoney(lists.get(2));
				detail.setIncomeMoney(lists.get(3));
				detail.setBalance(lists.get(4));
				detail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(detail);
			}
		}
		return infoList;
	}




	private HousingYiWuBasic htmlParserBasic(String html, MessageLoginForHousing messageLoginForHousing) {
		WebParam webParam = new WebParam();
		
		Document document = Jsoup.parse(html);
		
		String zgxm = getNextLabelByKeyword(document,"th","职工姓名");
		String zgzh = getNextLabelByKeyword(document,"th","职工账号");
		String sfzh = getNextLabelByKeyword(document,"th","身份证号");
		String sjh  = getNextLabelByKeyword(document,"th","手机号");
		String khrq = getNextLabelByKeyword(document,"th","开户日期");
		String dqzt = getNextLabelByKeyword(document,"th","当前状态");
		String yhzh = getNextLabelByKeyword(document,"th","银行账号");
		String xhrq = getNextLabelByKeyword(document,"th","销户日期");
		String fcrq = getNextLabelByKeyword(document,"th","封存日期");
		String dwzh = getNextLabelByKeyword(document,"th","单位账号");
		String dwmc = getNextLabelByKeyword(document,"th","单位名称");
		String snjz = getNextLabelByKeyword(document,"th","上年结转");
		String dqye = getNextLabelByKeyword(document,"th","当前余额");
		String yjjs = getNextLabelByKeyword(document,"th","月缴基数");
		String yjje = getNextLabelByKeyword(document,"th","月缴金额");
		String grjcbl = getNextLabelByKeyword(document,"td","个人");
		String dwjcbl = getNextLabelByKeyword(document,"td","单位");
		String dwyje = getNextLabelByKeyword(document,"th","单位月缴额");
		String gryje = getNextLabelByKeyword(document,"th","个人月缴额");
		String bcjcl = getNextLabelByKeyword(document,"td","补充缴存率");
		
		HousingYiWuBasic basic = new HousingYiWuBasic();
		basic.setName(zgxm);
		basic.setWorkNum(zgzh);
		basic.setNum(sfzh);
		basic.setPhoneNum(sjh);
		basic.setOpenDate(khrq);
		basic.setNowStatus(dqzt);
		basic.setBankNum(yhzh);
		basic.setCancelDate(xhrq);
		basic.setSealedDate(fcrq);
		basic.setCompNum(dwzh);
		basic.setCompanyName(dwmc);
		basic.setLastYearCarryover(snjz);
		basic.setNowBalance(dqye);
		basic.setMonthBase(yjjs);
		basic.setPerson(grjcbl);
		basic.setCompany(dwjcbl);
		basic.setSupplement(bcjcl);
		basic.setMonthPay(yjje);
		basic.setCompanyPay(dwyje);
		basic.setPersonPay(gryje);
		basic.setTaskid(messageLoginForHousing.getTask_id());
		return basic;
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

	
}
