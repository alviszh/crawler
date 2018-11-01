package app.parser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoBasic;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoPay;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;

@Component
public class HousingFundJiaoZuoParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	

	//身份证登陆
	public WebParam loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		//登陆界面
		String url="http://www.jzgjj.gov.cn:7002/wt-web/login"; 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		HtmlPage page = webClient.getPage(url); 
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']"); 
		elementById.reset(); 
		elementById.setText(messageLoginForHousing.getNum()); 

		String encryptedPhone = encryptedPhone(messageLoginForHousing.getPassword()); 
		System.out.println(encryptedPhone); 

		HtmlImage img = page.getFirstByXPath("//img[@style='cursor: pointer;']"); 
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902"); 
		String requestBody="username="+messageLoginForHousing.getNum()+"&password="+encryptedPhone+"&force=force&captcha="+verifycode+"&logintype=1"; 
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST); 

		webRequest.setRequestBody(requestBody); 
		Page page2 = webClient.getPage(webRequest); 
		Thread.sleep(1000); 
		String html = page2.getWebResponse().getContentAsString();
		
		System.out.println(html); 
		WebParam webParam = new WebParam(); 
//		if(page2.getWebResponse().getContentAsString().contains("缴存信息")) 
//		{ 
		webParam.setHtml(page2.getWebResponse().getContentAsString()); 
		webParam.setUrl(url); 
		webParam.setWebClient(webClient); 
		return webParam; 
//		} 
//		return null;
	}


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

	@Async
	public WebParam<HousingJiaoZuoBasic> getBasicInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception  {
		WebParam webParam = new WebParam();
		String cookies = taskHousing.getCookies(); 
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies); 
		for (Cookie cookie : cookies1) { 
		webClient.getCookieManager().addCookie(cookie); 
		}
		String url = "http://www.jzgjj.gov.cn:7002/wt-web/gr/jbxx";
						
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept","*/*");
//		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection","keep-alive");
//		webRequest.setAdditionalHeader("Content-Length","0");
//		webRequest.setAdditionalHeader("Host","www.jzgjj.gov.cn:7002");
//		webRequest.setAdditionalHeader("Origin","http://www.jzgjj.gov.cn:7002");
//		webRequest.setAdditionalHeader("Referer","http://www.jzgjj.gov.cn:7002/wt-web/home?logintype=1");
//		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With","XMLHttpRequest");
		Page page =webClient.getPage(webRequest); 
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		HousingJiaoZuoBasic basic = htmlParserBasic(html,messageLoginForHousing);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setHtml(html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHousingJiaoZuoBasic(basic);
		return webParam;
	}
	
	
	@Async
	public WebParam<HousingJiaoZuoPay> getPayInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception{
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies(); 
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies); 
		for (Cookie cookie : cookies1) { 
		webClient.getCookieManager().addCookie(cookie); 
		}
		String url = "http://www.jzgjj.gov.cn:7002/wt-web/gr/jcqqxx";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept","*/*");
//		webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection","keep-alive");
//		webRequest.setAdditionalHeader("Content-Length","0");
//		webRequest.setAdditionalHeader("Host","www.jzgjj.gov.cn:7002");
//		webRequest.setAdditionalHeader("Origin","http://www.jzgjj.gov.cn:7002");
//		webRequest.setAdditionalHeader("Referer","http://www.jzgjj.gov.cn:7002/wt-web/home?logintype=1");
//		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		webRequest.setAdditionalHeader("X-Requested-With","XMLHttpRequest");
		Page page = webClient.getPage(webRequest); 
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		HousingJiaoZuoPay pay = htmlParserPay(html,messageLoginForHousing);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setHtml(html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHousingJiaoZuoPay(pay);
		return webParam;
	}

	private HousingJiaoZuoPay htmlParserPay(String html, MessageLoginForHousing messageLoginForHousing) {
		//{"success":true,"msg":null,"totalcount":0,"results":null,"erros":null,"vdMapList":null,"data":{"a000":null,"a001":null,"a002":null,"a008":null,"a003":null,"a004":null,"a070":null,"a013":null,"a033":null,"a011":"1,600.00","a044":"2,880.00","a036":"0.00","a038":"2,240.00","a039":"640.00","a040":"0.00","a045":"0.00","dwjcl":null,"grjcl":null,"a077":"201712","a197":null,"a204":null,"yddh":null,"jtzz":null,"a079":null,"a081":null,"khyh":null,"yhkh":null,"userid":0,"g013":null,"yjce":"320.00","pageSize":0,"pageNum":0,"style":"","mc":null,"a097":"单位比例10%个人比例10%","a035":"160.00","a034":"160.00","tqlx":null,"ywlsh":null,"rq":null,"jfje":null,"dfje":null,"ye":null,"zy":null,"b011":null,"b013":null,"b017":null,"b019":null}}
		JSONObject obj = JSONObject.fromObject(html);
		String obj1 = obj.getString("data"); 
		JSONObject jsonObj = JSONObject.fromObject(obj1);
		String jcbl = jsonObj.getString("a097");
		String yjgz = jsonObj.getString("a011");
		String dwyc = jsonObj.getString("a035");
		String gryc = jsonObj.getString("a034");
		String sjny = jsonObj.getString("a077");
		String snjz = jsonObj.getString("a036");
		String bnhj = jsonObj.getString("a038");
		String bnbj = jsonObj.getString("a039");
		String bntq = jsonObj.getString("a040");
		String ye   = jsonObj.getString("a044");
		String bnlx = jsonObj.getString("a045");
		HousingJiaoZuoPay pay = new HousingJiaoZuoPay();
		pay.setPayProportion(jcbl);
		pay.setMonthSalary(yjgz);
		pay.setCompanyDeposit(dwyc);
		pay.setPersonDeposit(gryc);
		pay.setPayYears(sjny);
		pay.setLastYearCarryover(snjz);
		pay.setYearRemitted(bnhj);
		pay.setYearBack(bnbj);
		pay.setYearExtract(bntq);
		pay.setBalance(ye);
		pay.setYearInterest(bnlx);
		pay.setTaskid(messageLoginForHousing.getTask_id());
		return pay;
	}


	private HousingJiaoZuoBasic htmlParserBasic(String html, MessageLoginForHousing messageLoginForHousing) {
		//{"success":true,"msg":null,"totalcount":0,"results":null,"erros":null,"vdMapList":null,"data":{"a000":null,"a001":"013505001729","a002":"随恩丽","a008":"411326198806065166","a003":"013505","a004":"河南成就人力资源有限公司","a070":null,"a013":"2017-07-05","a033":null,"a011":null,"a044":null,"a036":null,"a038":null,"a039":null,"a040":null,"a045":null,"dwjcl":null,"grjcl":null,"a077":null,"a197":null,"a204":null,"yddh":"18839110921","jtzz":" ","a079":null,"a081":null,"khyh":" ","yhkh":" ","userid":0,"g013":null,"yjce":null,"pageSize":0,"pageNum":0,"style":"","mc":"正常","a097":null,"a035":null,"a034":null,"tqlx":null,"ywlsh":null,"rq":null,"jfje":null,"dfje":null,"ye":null,"zy":null,"b011":null,"b013":null,"b017":null,"b019":null}}
		JSONObject obj = JSONObject.fromObject(html);
		String obj1 = obj.getString("data"); 
		JSONObject jsonObj = JSONObject.fromObject(obj1);
		String zgzh = jsonObj.getString("a001");
		String zgxm = jsonObj.getString("a002");
		String dwzh = jsonObj.getString("a003");
		String dwmc =  jsonObj.getString("a004");
		String zhzt =  jsonObj.getString("mc");
		String zjhm =  jsonObj.getString("a008");
		String yddh = jsonObj.getString("yddh");
		String jtzz =  jsonObj.getString("jtzz");
		String khyh =  jsonObj.getString("khyh");
		String yhkh = jsonObj.getString("yhkh");
		String khrq =  jsonObj.getString("a013");
		HousingJiaoZuoBasic  basic = new HousingJiaoZuoBasic();
		basic.setWorkNum(zgzh);
		basic.setWorkName(zgxm);
		basic.setCompanyNum(dwzh);
		basic.setCompanyName(dwmc);
		basic.setAccountStatus(zhzt);
		basic.setNum(zjhm);
		basic.setPhoneNum(yddh);
		basic.setHomeAddress(jtzz);
		basic.setBank(khyh);
		basic.setBankNum(yhkh);
		basic.setTime(khrq);
		basic.setTaskid(messageLoginForHousing.getTask_id());
		return basic;
	}
	// 加密 
	public  String encryptedPhone(String phonenum) throws Exception { 
	ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn"); 
	String path = readResource("zhumadian.js", Charsets.UTF_8); 
	// System.out.println(path); 
	// FileReader reader1 = new FileReader(path); // 执行指定脚本 
	engine.eval(path); 
	final Invocable invocable = (Invocable) engine; 
	Object data = invocable.invokeFunction("encryptedString", phonenum); 
	return data.toString(); 
	} 

	public  String readResource(final String fileName, Charset charset) throws IOException { 
	return Resources.toString(Resources.getResource(fileName), charset); 
	}
	


	
}
