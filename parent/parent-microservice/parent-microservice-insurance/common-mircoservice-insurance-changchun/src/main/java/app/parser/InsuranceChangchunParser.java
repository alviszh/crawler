package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunAccountInfo;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunEndowmentInfo;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunUnemploymentInfo;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

@Component
public class InsuranceChangchunParser {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceChangchunParser.class);
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	
	//登录入口
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
				//要爬取的url
				String url = "http://www.ccshbx.org.cn/member/login.jhtml";
				
				WebParam webParam= new WebParam();
				WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//				webClient.getOptions().setJavaScriptEnabled(false);
				webClient.getOptions().setTimeout(30000);
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
				HtmlPage page = webClient.getPage(webRequest);
				int status = page.getWebResponse().getStatusCode();
				
				if(status == 200){
					HtmlImage image = page.getFirstByXPath("//img[@id='captcaImg']");
					String code = chaoJiYingOcrService.getVerifycode(image, "4004");
					HtmlTextInput username = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
					HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
					HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='checkCode']");
					
					username.setText(insuranceRequestParameters.getUsername());
					password.setText(insuranceRequestParameters.getPassword());
					verifyCode.setText(code);
					HtmlElement button = (HtmlElement)page.getFirstByXPath("//a[@id='login_btn']");
					
					
					HtmlPage loadPage = button.click();
					System.out.println("登陆后的页面   ==》》"+loadPage.getWebResponse().getContentAsString());
					
					webParam.setCode(loadPage.getWebResponse().getStatusCode());
					webParam.setPage(loadPage);
					
					String url2 = "http://www.ccshbx.org.cn/getData.jspx";
					WebRequest requestSettings = new WebRequest(new URL(url2), HttpMethod.POST);
					Page page2 = webClient.getPage(requestSettings);
					System.out.println(page2.getWebResponse().getContentAsString());
					return webParam;
				}
				
				return null;				
	}
	
	//爬取用户信息
	public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {
		WebParam webParam= new WebParam();	
		String url = "http://www.ccshbx.org.cn/employeeQuery.jsp?fileName=webQuery/employeeBaseinfoQuery.jsp&bz=1&jc10bz=1&nmbz=";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		
		//添加各种header，搞的像真的浏览器一样
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "www.ccshbx.org.cn");
//		requestSettings.setAdditionalHeader("Referer", "http://www.ccshbx.org.cn/validate.jsp?aac001=3020978757&employeePassword=196169&bz=");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		//获取网页
		HtmlPage page = webClient.getPage(requestSettings);
		
//		System.out.println(page.asXml());
		
		int statusCode = page.getWebResponse().getStatusCode();
		if(200 == statusCode){
	    	String html = page.getWebResponse().getContentAsString();
	    	tracer.addTag("InsuranceChangchuniParser.getUserInfo 个人信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
	    	//调用解析方法
	    	InsuranceChangchunUserInfo userInfo = htmlParserUserInfo(html,taskInsurance);
	    	
	    	//保存各种网页信息
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceChangchunUserInfo(userInfo);
	    	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;
		}
		
		return null;
	}
	
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */	
	
	//html的解析方法
	private InsuranceChangchunUserInfo htmlParserUserInfo(String html,TaskInsurance taskInsurance) {
		
		InsuranceChangchunUserInfo insuranceChangchunUserInfo = new InsuranceChangchunUserInfo();
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.select("tr[style]").select("td");
		
		String personNumber = doc.select("td:contains(个人编号)").first().nextElementSibling().text();
		String personName = doc.select("td:contains(姓名)").first().nextElementSibling().text();
		String personID = doc.select("td:contains(身份证号)").first().nextElementSibling().text();
		String personBirth = doc.select("td:contains(档案出生日期)").first().nextElementSibling().text();
		String personSex = doc.select("td:contains(性别)").first().nextElementSibling().text();
		String personNation = doc.select("td:contains(民族)").first().nextElementSibling().text();
		String personKind = doc.select("td:contains(个人身份)").first().nextElementSibling().text();
		String workTime = doc.select("td:contains(参加工作时间)").first().nextElementSibling().text();		
		String residenceKind = doc.select("td:contains(户口性质)").first().nextElementSibling().text();
		String personStatus = doc.select("td:contains(人员状态)").first().nextElementSibling().text();
		String companyName = doc.select("td:contains(单位名称)").first().nextElementSibling().text();
		String firstEndowmentInsurance = doc.select("td:contains(养老参保时间:)").first().nextElementSibling().text();
		String endowmentInsuranceStatus = doc.select("td:contains(养老参保状态:)").first().nextElementSibling().text();
		String firstUnemploymentInsurance = doc.select("td:contains(失业参保时间:)").first().nextElementSibling().text();
		String unemploymentInsuranceStatus = doc.select("td:contains(失业参保状态:)").first().nextElementSibling().text();
		
		//将解析出来的东西塞到对象里面
		insuranceChangchunUserInfo.setPersonNumber(personNumber);
		insuranceChangchunUserInfo.setPersonName(personName);
		insuranceChangchunUserInfo.setPersonID(personID);
		insuranceChangchunUserInfo.setPersonBirth(personBirth);
		insuranceChangchunUserInfo.setPersonSex(personSex);
		insuranceChangchunUserInfo.setPersonNation(personNation);
		insuranceChangchunUserInfo.setPersonKind(personKind);
		insuranceChangchunUserInfo.setWorkTime(workTime);
		insuranceChangchunUserInfo.setResidenceKind(residenceKind);
		insuranceChangchunUserInfo.setPersonStatus(personStatus);
		insuranceChangchunUserInfo.setCompanyName(companyName);
		insuranceChangchunUserInfo.setFirstEndowmentInsurance(firstEndowmentInsurance);
		insuranceChangchunUserInfo.setEndowmentInsuranceStatus(endowmentInsuranceStatus);
		insuranceChangchunUserInfo.setFirstUnemploymentInsurance(firstUnemploymentInsurance);
		insuranceChangchunUserInfo.setUnemploymentInsuranceStatus(unemploymentInsuranceStatus);
		insuranceChangchunUserInfo.setTaskid(taskInsurance.getTaskid());
		return insuranceChangchunUserInfo;
	}
	
	/**
	 * @Des 个人账户信息
	 * @return
	 */	
	public WebParam getAccountInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception{
		WebParam webParam= new WebParam();
		String url = "http://www.ccshbx.org.cn/service/115198.jhtml";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		
		HtmlPage page = webClient.getPage(requestSettings);
		int statusCode = page.getWebResponse().getStatusCode();
		
		if(200 == statusCode){
	    	String html = page.getWebResponse().getContentAsString();
	    	System.out.println(html);
	    	tracer.addTag("InsuranceChangchuniParser.getAccount 个人账户信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
	    	//调用解析方法
	    	InsuranceChangchunAccountInfo accountInfo = htmlParserAccountInfo(html,taskInsurance);
	    	
	    	//保存各种网页信息
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceChangchunAccountInfo(accountInfo);
	    	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);
	    	return webParam;
		}		
		return null;		
	}
	
	/**
	 * @Des 解析个人账户信息
	 * @param html
	 * @return
	 */	
	private InsuranceChangchunAccountInfo htmlParserAccountInfo(String html,TaskInsurance taskInsurance) {
		
		InsuranceChangchunAccountInfo insuranceChangchunAccountInfo = new InsuranceChangchunAccountInfo();
		Document accountDoc = Jsoup.parse(html);
		Elements accountInfoCol = accountDoc.getElementsByClass("wtjcb-rdback");		
		
		insuranceChangchunAccountInfo.setPersonNumber(accountInfoCol.get(0).text());
		insuranceChangchunAccountInfo.setCardId(accountInfoCol.get(1).text());
		insuranceChangchunAccountInfo.setPersonName(accountInfoCol.get(2).text());
		insuranceChangchunAccountInfo.setSex(accountInfoCol.get(3).text());
		insuranceChangchunAccountInfo.setNation(accountInfoCol.get(4).text());
		
		Element zhze = accountDoc.getElementById("zhze");
		insuranceChangchunAccountInfo.setAccountTotal(zhze.text());
		Element zyje = accountDoc.getElementById("zyje");
		insuranceChangchunAccountInfo.setTurnTotal(zyje.text());
		Element grbf = accountDoc.getElementById("grbf");
		insuranceChangchunAccountInfo.setAccountTotalPersonal(grbf.text());
		Element grzr = accountDoc.getElementById("grzr");
		insuranceChangchunAccountInfo.setTurnPersonal(grzr.text());
		
		return insuranceChangchunAccountInfo;		
	}
	
	/**
	 * @Des 个人养老保险信息
	 * @return
	 */
	public WebParam getEndowmentInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception{
			
		WebParam webParam= new WebParam();
		WebClient webClient = insuranceService.getWebClient(cookies);
		String urls = "http://www.ccshbx.org.cn/employeeQuery.jsp?fileName=webQuery/personMonthFeeQuery.jsp";
		WebRequest requestSettingss = new WebRequest(new URL(urls), HttpMethod.GET);
		HtmlPage pages = webClient.getPage(requestSettingss);
		
		String htmls = pages.getWebResponse().getContentAsString();
		int a = htmls.indexOf("上一页");
		int b = htmls.indexOf("共", a);
		int c = htmls.indexOf("页", b);
		String strnum = htmls.substring(a+1, c);
		int pagesize = Integer.parseInt(strnum.substring(19));
		
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		List<InsuranceChangchunEndowmentInfo> infoList = new ArrayList<InsuranceChangchunEndowmentInfo>();
		for(int i=1;i<pagesize+1;i++){
			String url = "http://www.ccshbx.org.cn/employeeQuery.jsp?fileName=webQuery/personMonthFeeQuery.jsp&PageCount="+i+"&jc10bz=1&queryForword=pre&select1=01&bz=1";
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			requestSettings.setAdditionalHeader("Accept-Encoding","gzip, deflate, sdch");
			requestSettings.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection","keep-alive");
			requestSettings.setAdditionalHeader("Host","www.ccshbx.org.cn");
			requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//			requestSettings.setAdditionalHeader("Referer","http://www.ccshbx.org.cn/employeeQuery.jsp?select1=01&fileName=webQuery%2FpersonMonthFeeQuery.jsp&bz=1&jc10bz=1");
			
		
			HtmlPage page = webClient.getPage(requestSettings);
			int statusCode = page.getWebResponse().getStatusCode();
			
			if(200 == statusCode){
				webParam.setCode(200);
				
		    	String html = page.getWebResponse().getContentAsString();
		    	tracer.addTag("InsuranceChangchuniParser.getEndowmentInfo 养老保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
		    	if(html.contains("对不起，不存在此人的基本养老保险月账户信息!")){
		    		break;
		    	}else{
		    		pageList.add(page);
		    		infoList = htmlParserEndowmentInfo(page,taskInsurance,infoList);
		    		if(null != infoList){
		    			webParam.setList(infoList);
		    		}
		    	}
			}			
		}
		
		if(null != pageList){
			webParam.setHtmlPage(pageList);
		}
	
		return webParam;		
	}
	
	/**
	 * 失业保险信息获取
	 * @param page
	 * @param taskInsurance
	 * @param webParam
	 * @return
	 */
	
	public WebParam<HtmlPage> getUnemploymentInfo(TaskInsurance taskInsurance, Set<Cookie> cookies)throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = insuranceService.getWebClient(cookies);
		String urls = "http://www.ccshbx.org.cn/employeeQuery.jsp?fileName=webQuery/personMonthFeeQuery.jsp";
		WebRequest requestSettingss = new WebRequest(new URL(urls), HttpMethod.GET);
		HtmlPage pages = webClient.getPage(requestSettingss);
		
		String htmls = pages.getWebResponse().getContentAsString();
		int a = htmls.indexOf("上一页");
		int b = htmls.indexOf("共", a);
		int c = htmls.indexOf("页", b);
		String strnum = htmls.substring(a+1, c);
		int pagesize = Integer.parseInt(strnum.substring(19));
		List<HtmlPage> pageList = new ArrayList<HtmlPage>();
		List<InsuranceChangchunUnemploymentInfo> infoList = new ArrayList<InsuranceChangchunUnemploymentInfo>();
		for(int i=1;i<pagesize+1;i++){
			String url = "http://www.ccshbx.org.cn/employeeQuery.jsp?fileName=webQuery/personMonthFeeQuery.jsp&PageCount="+i+"&jc10bz=1&queryForword=next&select1=02&bz=1";
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			requestSettings.setAdditionalHeader("Accept-Encoding","gzip, deflate, sdch");
			requestSettings.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection","keep-alive");
			requestSettings.setAdditionalHeader("Host","www.ccshbx.org.cn");
			requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			HtmlPage page = webClient.getPage(requestSettings);
			
			int statusCode = page.getWebResponse().getStatusCode();
			
			if(200 == statusCode){
		    	String html = page.getWebResponse().getContentAsString();
		    	tracer.addTag("InsuranceChangchuniParser.getUnemploymentInfo 失业保险信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
		    	if(html.contains("对不起，不存在此人的失业保险月账户信息!")){
		    		break;
		    	}else{
		    		pageList.add(page);
		    		infoList = htmlParserUnemploymentInfo(page,taskInsurance,infoList);
		    		if(null !=infoList){
		    			webParam.setList(infoList);
		    		}
		    	}
			}	
		}
		if(null != pageList){
			webParam.setHtmlPage(pageList);
		}
		return webParam;
	}
	/**
	 * 解析养老保险信息
	 * @param page
	 * @param taskInsurance
	 * @param infoList
	 * @return  infoList
	 */
	public List<InsuranceChangchunEndowmentInfo> htmlParserEndowmentInfo(HtmlPage page,TaskInsurance taskInsurance, List<InsuranceChangchunEndowmentInfo> infoList) {
	
		Document endowmentDoc = Jsoup.parse(page.asXml());
		
		Elements endowmentInfo = endowmentDoc.select("tr[style=font-size:14px;]");
		int num=1;
		for(int i=num;i<endowmentInfo.size();i++){
			Elements tds = endowmentInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				InsuranceChangchunEndowmentInfo insuranceChangchunEndowmentInfo = new InsuranceChangchunEndowmentInfo();
				insuranceChangchunEndowmentInfo.setInsuranceTime(lists.get(0));
				insuranceChangchunEndowmentInfo.setCompanyNumber(lists.get(1));
				insuranceChangchunEndowmentInfo.setSalary(lists.get(2));
				insuranceChangchunEndowmentInfo.setSalaryBaseNumber(lists.get(3));
				insuranceChangchunEndowmentInfo.setPersonalFee(lists.get(4));
				insuranceChangchunEndowmentInfo.setCompanyAccountFee(lists.get(5));
				insuranceChangchunEndowmentInfo.setIsFlag(lists.get(6));
				
				insuranceChangchunEndowmentInfo.setTaskid(taskInsurance.getTaskid());
				infoList.add(insuranceChangchunEndowmentInfo);
			}
			
		}
		return infoList;	
	}

	
	/**
	 * 解析失业保险信息
	 * @param page
	 * @param taskInsurance
	 * @param infoList
	 * @return infoList
	 */
	public List<InsuranceChangchunUnemploymentInfo> htmlParserUnemploymentInfo(HtmlPage page,TaskInsurance taskInsurance,List<InsuranceChangchunUnemploymentInfo> infoList) {
		
		Document endowmentDoc = Jsoup.parse(page.asXml());
		Elements unemploymentInfo = endowmentDoc.select("tr[style=font-size:14px;]");
		int num=1;
		for(int i=num;i<unemploymentInfo.size();i++){
			Elements tds = unemploymentInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				InsuranceChangchunUnemploymentInfo insuranceChangchunUnemploymentInfo  = new InsuranceChangchunUnemploymentInfo();
				insuranceChangchunUnemploymentInfo.setInsuranceTime(lists.get(0));
				insuranceChangchunUnemploymentInfo.setCompanyNumber(lists.get(1));
				insuranceChangchunUnemploymentInfo.setSalary(lists.get(2));
				insuranceChangchunUnemploymentInfo.setSalaryBaseNumber(lists.get(3));
				insuranceChangchunUnemploymentInfo.setPersonalFee(lists.get(4));
				insuranceChangchunUnemploymentInfo.setCompanyFee(lists.get(5));
				insuranceChangchunUnemploymentInfo.setIsFlagPersonal(lists.get(6));
				insuranceChangchunUnemploymentInfo.setIsFlagCompany(lists.get(7));
				insuranceChangchunUnemploymentInfo.setTaskid(taskInsurance.getTaskid());
				infoList.add(insuranceChangchunUnemploymentInfo);
			}
		}
		return infoList;
	}
}
