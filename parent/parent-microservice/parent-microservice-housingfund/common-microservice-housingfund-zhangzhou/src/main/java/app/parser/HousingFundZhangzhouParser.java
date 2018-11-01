package app.parser;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouAccount;
import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundZhangzhouParser {

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
		WebParam webParam = new WebParam();
		String url = "http://www.zzgjj.gov.cn/grlogin.html";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage page = webClient.getPage(webRequest);
		HtmlElement button = page.getFirstByXPath("//input[@id='txtCode']");
		HtmlPage loadPage =button.click();
		HtmlImage image = loadPage.getFirstByXPath("//img[@id='code']");
		HtmlTextInput hosingFundNumber = (HtmlTextInput)loadPage.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput pwd = page.getFirstByXPath("//input[@id='txtPassword']");
		String code = chaoJiYingOcrService.getVerifycode(image, "4004");
		HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtCode']");
		hosingFundNumber.setText(messageLoginForHousing.getHosingFundNumber());
		verifyCode.setText(code);
		pwd.setText(messageLoginForHousing.getPassword());
		HtmlElement submit = (HtmlElement)page.getFirstByXPath("//input[@id='login']");
			
		HtmlPage subPage = submit.click();
		
		System.out.println(subPage.getWebResponse().getContentAsString());
		webParam.setCode(subPage.getWebResponse().getStatusCode());
		webParam.setPage(subPage);
		webParam.setWebClient(webClient);
		return webParam;
			
		
	}


	public WebParam<HousingZhangZhouAccount> getAccountInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String url = "http://www.zzgjj.gov.cn/yuanjian/Personal/PersonalInfo.aspx";
		
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
//			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
//			webRequest.setAdditionalHeader("Connection","keep-alive");
//			webRequest.setAdditionalHeader("Host","query.xazfgjj.gov.cn");
//			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
//			webRequest.setAdditionalHeader("Referer","http://query.xazfgjj.gov.cn/index.jsp?urltype=tree.TreeTempUrl&wbtreeid=1172");
//			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
			HtmlPage page = (HtmlPage)webClient.getPage(webRequest); 
			String html3 = page.getWebResponse().getContentAsString();
			HousingZhangZhouAccount basic =htmlParserAccount(html3,messageLoginForHousing);
			webParam.setCode(page.getWebResponse().getStatusCode());
			webParam.setHousingZhangzhouAccount(basic);
			webParam.setUrl(url);
			webParam.setHtml(html3);
			webParam.setPage(page);
			return webParam;
	}



	private HousingZhangZhouAccount htmlParserAccount(String html3, MessageLoginForHousing messageLoginForHousing) {
		Document documentHtml = Jsoup.parse(html3);
		String zhzt= getNextLabelByKeyword(documentHtml,"账户状态","td");
		String zgzh= getNextLabelByKeyword(documentHtml,"职工账号","td");
		String khrq= getNextLabelByKeyword(documentHtml,"开户日期","td");
		String khyh= getNextLabelByKeyword(documentHtml,"开户银行","td");
		String dwmc= getNextLabelByKeyword(documentHtml,"单位名称","td");
		String zgxm= getNextLabelByKeyword(documentHtml,"职工姓名","td");
		String sfzh= getNextLabelByKeyword(documentHtml,"身份证号","td");
		String snjze =getNextLabelByKeyword(documentHtml,"上年结转额","td");
		String dwjjl=getNextLabelByKeyword(documentHtml,"单位缴交率","td");
		String grjjl=getNextLabelByKeyword(documentHtml,"个人缴交率","td");
		String dwyje=getNextLabelByKeyword(documentHtml,"单位应缴额","td");
		String zgyje=getNextLabelByKeyword(documentHtml,"职工应缴额","td");
		String yyje=getNextLabelByKeyword(documentHtml,"月应缴额","td");
		String jzny=getNextLabelByKeyword(documentHtml,"缴至年月","td");
		String hqye= getNextLabelByKeyword(documentHtml,"活期余额","td");
		String dqye= getNextLabelByKeyword(documentHtml,"定期余额","td");
		String zhye= getNextLabelByKeyword(documentHtml,"账户余额","td");
		String zjzx= getNextLabelByKeyword(documentHtml,"资金中心","td");
		HousingZhangZhouAccount  account = new HousingZhangZhouAccount();
		account.setAccountStatus(zhzt);
		account.setWorkNumber(zgzh);
		account.setDate(khrq);
		account.setBank(khyh);
		account.setCompanyName(dwmc);
		account.setWorkName(zgxm);
		account.setNum(sfzh);
		account.setLastYear(snjze);
		account.setCompPaymentRate(dwjjl);
		account.setPerPaymentRate(grjjl);
		account.setCompPaymentNumber(dwyje);
		account.setWorkPaymentNumber(zgyje);
		account.setMonthPayNumber(yyje);
		account.setPayYearMonth(jzny);
		account.setCurrentBalance(hqye);
		account.setRegularBalance(dqye);
		account.setAccountBalance(zhye);
		account.setCapitalCenter(zjzx);
		account.setTaskid(messageLoginForHousing.getTask_id());
		return account;
	}


	public WebParam<HousingZhangZhouDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		WebParam webParam= new WebParam();	
		
		String url = "http://www.zzgjj.gov.cn/yuanjian/Personal/PersonalList.aspx";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
//			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
//			webRequest.setAdditionalHeader("Connection","keep-alive");
//			webRequest.setAdditionalHeader("Host","uery.xazfgjj.gov.cn");
//			webRequest.setAdditionalHeader("Referer","http://query.xazfgjj.gov.cn/index.jsp?urltype=tree.TreeTempUrl&wbtreeid=1172");
//			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
//			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
			List<HtmlPage> pageList = new ArrayList<HtmlPage>();
			Page page = webClient.getPage(webRequest);
			int statusCode = page.getWebResponse().getStatusCode();
			List<HousingZhangZhouDetail>  infoList = new ArrayList<HousingZhangZhouDetail>();
			if(200== statusCode){
				String html  =page.getWebResponse().getContentAsString();
				tracer.addTag("HousingZhangzhouParser.getDetailInfo 公积金明细" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
				if(null !=infoList){
	    			webParam.setList(infoList);
	    			webParam.setUrl(url);
	    			webParam.setHtml(html);
	    			return webParam;
	    		}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}


	private List<HousingZhangZhouDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingZhangZhouDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByClass("baselistTable").select("tr");
		for(int i =1;i<baseInfo.size();i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				if(lists.size()>2){
				HousingZhangZhouDetail zhangzhouDetail =  new HousingZhangZhouDetail();
				zhangzhouDetail.setAccountNumber(lists.get(1));
				zhangzhouDetail.setName(lists.get(2));
				zhangzhouDetail.setDate(lists.get(3));
				zhangzhouDetail.setAbs(lists.get(4));
				zhangzhouDetail.setDrawMoney(lists.get(5));
				zhangzhouDetail.setPayMoney(lists.get(6));
				zhangzhouDetail.setBalance(lists.get(7));
				
				zhangzhouDetail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(zhangzhouDetail);
				}
			}
		}
		return infoList;
	}

	
}
