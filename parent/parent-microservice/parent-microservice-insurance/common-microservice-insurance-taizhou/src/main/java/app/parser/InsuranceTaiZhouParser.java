package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.huaian.InsuranceHuaiAnEndowment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouEndowment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouInjury;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouMaternity;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouMedical;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.taizhou.InsuranceTaiZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceTaiZhouParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://58.222.185.50:9009/personwork.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='account']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='password']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		
		WebParam webParam = new WebParam();
		HtmlImage img = page.getFirstByXPath("//*[@id='f_svl']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='captcha']");
		h.setText(verifycode);
		
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='logininfo']/div[2]/div[2]/ul/li[2]/input");
		firstByXPath.click();
		
		String url2="http://58.222.185.50:9009/getlogininfo.html";
		Page page3 = webClient.getPage(url2);
//		System.out.println(page3.getWebResponse().getContentAsString());
		webParam.setHtml(page3.getWebResponse().getContentAsString());
		webParam.setUrl(url2);
		webParam.setWebClient(webClient);
		return webParam;
	}

	//个人信息
	public WebParam<InsuranceTaiZhouUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://58.222.185.50:9009/person/personMessage.html";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		
		WebParam<InsuranceTaiZhouUserInfo> webParam = new WebParam<InsuranceTaiZhouUserInfo>();
		if(page.getWebResponse().getContentAsString().contains("个人编号"))
		{
			Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
			Element elementById = parse.getElementById("queryform").getElementsByClass("condition_box").get(0).getElementsByTag("ul").get(1);
			Element elementById1 = parse.getElementById("queryform").getElementsByClass("condition_box").get(0).getElementsByTag("ul").get(3);
			InsuranceTaiZhouUserInfo i = new InsuranceTaiZhouUserInfo();
			i.setPersonalNum(elementById.getElementsByTag("li").get(0).getElementsByTag("input").val());
			i.setName(elementById.getElementsByTag("li").get(1).getElementsByTag("input").val());
			i.setBirth(elementById.getElementsByTag("li").get(2).getElementsByTag("input").val());
			i.setSex(elementById.getElementsByTag("li").get(3).getElementsByTag("input").val());
			i.setPersonalStatus(elementById.getElementsByTag("li").get(4).getElementsByTag("input").val());
			i.setType(elementById.getElementsByTag("li").get(5).getElementsByTag("input").val());
			
			i.setIdNum(elementById1.getElementsByTag("li").get(0).getElementsByTag("input").val());
			i.setNational(elementById1.getElementsByTag("li").get(1).getElementsByTag("input").val());
			i.setPhone(elementById1.getElementsByTag("li").get(2).getElementsByTag("input").val());
			i.setMedical(elementById1.getElementsByTag("li").get(3).getElementsByTag("input").val());
			i.setHome(elementById1.getElementsByTag("li").get(4).getElementsByTag("input").val());
			i.setAddr(elementById1.getElementsByTag("li").get(5).getElementsByTag("input").val());
			i.setTaskid(taskInsurance.getTaskid());
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsuranceTaiZhouUserInfo(i);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}

	//养老
	public WebParam<InsuranceTaiZhouEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://58.222.185.50:9009/person/monthAccountYLao.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		
		WebParam<InsuranceTaiZhouEndowment> webParam = new WebParam<InsuranceTaiZhouEndowment>();
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("page_num"))
		{
			String num = doc.getElementsByClass("page_num").get(0).getElementsByTag("li").get(1).text().substring(1, 2);
			System.out.println(num);
			int b = Integer.parseInt(num);
			if(b>0)
			{
				for (int i = 1; i < b+1; i++) {
					String url1="http://58.222.185.50:9009/person/monthAccountYLao.html?page_num="+b;
					WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.POST);	
					Page page1 = webClient.getPage(webRequest1);
					Document parse = Jsoup.parse(page1.getWebResponse().getContentAsString());
					InsuranceTaiZhouEndowment in = null;
					Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
					
					Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
					
					Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
					
					Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
					
					Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");
					
					Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");
					
					Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");
					
					Elements elementsByClass7 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(7).getElementsByTag("dd");
					
					Elements elementsByClass8 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(8).getElementsByTag("dd");
					
					Elements elementsByClass9 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(9).getElementsByTag("dd");

					List<InsuranceTaiZhouEndowment> list = new ArrayList<InsuranceTaiZhouEndowment>();
					for (int ii = 0; ii < elementsByClass.size(); ii++) {
						in = new InsuranceTaiZhouEndowment();
						in.setDatea(elementsByClass.get(ii).text());
						in.setInDatea(elementsByClass1.get(ii).text());
						in.setType(elementsByClass2.get(ii).text());
						in.setBase(elementsByClass3.get(ii).text());
						in.setPersonalMoney(elementsByClass4.get(ii).text());
						in.setCompany(elementsByClass5.get(ii).text());
						in.setMonth(elementsByClass6.get(ii).text());
						in.setName(elementsByClass7.get(ii).text());
						in.setIDNum(elementsByClass8.get(ii).text());
						in.setPersonalNum(elementsByClass9.get(ii).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setList(list);
					webParam.setUrl(url1);
				}
				
			}
		}
		return webParam;
	}

	//医疗
	public WebParam<InsuranceTaiZhouMedical> getMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception {
		String url="http://58.222.185.50:9009/person/yiliaozhanghu_result.html?&inputvalue="+getDateBefore("yyyy-MM-dd", 3)+"&inputvalue_end="+getTime("yyyy-MM-dd");
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());	
		WebParam<InsuranceTaiZhouMedical> webParam = new WebParam<InsuranceTaiZhouMedical>();
		if(page.getWebResponse().getContentAsString().contains("listinfo"))
		{
			Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceTaiZhouMedical in = null;
	        Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
			
			Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
			
			Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
			
			Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
			
			Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");
			
			Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");
			
			Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");
			
			Elements elementsByClass7 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(7).getElementsByTag("dd");
			
			
			List<InsuranceTaiZhouMedical> list = new ArrayList<InsuranceTaiZhouMedical>();
			for (int i = 0; i < elementsByClass.size(); i++) {
				in = new InsuranceTaiZhouMedical();
				in.setDatea(elementsByClass.get(i).text());
				in.setDescr(elementsByClass1.get(i).text());
				in.setInMoney(elementsByClass2.get(i).text());
				in.setOutMoney(elementsByClass3.get(i).text());
				in.setFee(elementsByClass4.get(i).text());
				in.setName(elementsByClass5.get(i).text());
				in.setIDNum(elementsByClass6.get(i).text());
				in.setPersonalNum(elementsByClass7.get(i).text());
				in.setTaskid(taskInsurance.getTaskid());
				list.add(in);
			}
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setList(list);
			webParam.setUrl(url);
			
		}
		return webParam;
	}

	
	//失业
	public WebParam<InsuranceTaiZhouUnemployment> getUnemployment(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception {
		String url="http://58.222.185.50:9009/person/shiyezhifu.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());	
		WebParam<InsuranceTaiZhouUnemployment> webParam = new WebParam<InsuranceTaiZhouUnemployment>();
		if(page.getWebResponse().getContentAsString().contains("listinfo"))
		{
			Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceTaiZhouUnemployment in = null;
			
	        Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
	        System.out.println(elementsByClass.get(0).text());
	        if(elementsByClass.get(0).text().equals(""))
			{
	        	return null;
			}
	        else{
                Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
				
				Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
				
				Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
				
				List<InsuranceTaiZhouUnemployment> list = new ArrayList<InsuranceTaiZhouUnemployment>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					in = new InsuranceTaiZhouUnemployment();
					in.setGetMonth(elementsByClass.get(i).text());
					in.setMonth(elementsByClass1.get(i).text());
					in.setIDNum(elementsByClass2.get(i).text());
					in.setName(elementsByClass3.get(i).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
	        }
		}
		return webParam;
	}

	
	//工伤
	public WebParam<InsuranceTaiZhouInjury> getInjury(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception {
		String url="http://58.222.185.50:9009/person/gsdqdyffmx_result.html?pageNo=1&aae002_begin="+getDateBefore("yyyyMM", 3)+"&aae002_end="+getTime("yyyyMM");
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());	
		
		
		WebParam<InsuranceTaiZhouInjury> webParam = new WebParam<InsuranceTaiZhouInjury>();
		if(page.getWebResponse().getContentAsString().contains("listinfo"))
		{
			Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceTaiZhouInjury in = null;
	        Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
	        if(elementsByClass.get(0).text().equals(""))
			{
	        	return null;
			}
	        else{
				Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
				
				Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
				
				Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
				Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");

				Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");

				Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");

				List<InsuranceTaiZhouInjury> list = new ArrayList<InsuranceTaiZhouInjury>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					in = new InsuranceTaiZhouInjury();
					in.setDatea(elementsByClass.get(i).text());
					in.setMoney(elementsByClass1.get(i).text());
					in.setInMoney(elementsByClass2.get(i).text());
					in.setCareMoney(elementsByClass3.get(i).text());
					in.setName(elementsByClass4.get(i).text());
					in.setIDNum(elementsByClass5.get(i).text());
					in.setPersonalNum(elementsByClass6.get(i).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
			}
		}
		return webParam;
	}

	//生育
	public WebParam<InsuranceTaiZhouMaternity> getMaternity(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://58.222.185.50:9009/person/sybxdyff.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		Page page = webClient.getPage(url);
		System.out.println(page.getWebResponse().getContentAsString());		
		
		
		WebParam<InsuranceTaiZhouMaternity> webParam = new WebParam<InsuranceTaiZhouMaternity>();
		if(page.getWebResponse().getContentAsString().contains("listinfo"))
		{
			Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
			InsuranceTaiZhouMaternity in = null;
	        Elements elementsByClass = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
	        if(elementsByClass.get(0).text().equals(""))
			{
	        	return null;
			}
	        else{
				Elements elementsByClass1 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(1).getElementsByTag("dd");
				
				Elements elementsByClass2 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(2).getElementsByTag("dd");
				
				Elements elementsByClass3 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(3).getElementsByTag("dd");
				Elements elementsByClass4 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(4).getElementsByTag("dd");
				Elements elementsByClass5 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(5).getElementsByTag("dd");
				Elements elementsByClass6 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(6).getElementsByTag("dd");
				Elements elementsByClass7 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(7).getElementsByTag("dd");
				Elements elementsByClass8 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(8).getElementsByTag("dd");
				Elements elementsByClass9 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(9).getElementsByTag("dd");
				Elements elementsByClass10 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(10).getElementsByTag("dd");
				Elements elementsByClass11 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(11).getElementsByTag("dd");
				Elements elementsByClass12 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(12).getElementsByTag("dd");
				Elements elementsByClass13 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(13).getElementsByTag("dd");
				Elements elementsByClass14 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(14).getElementsByTag("dd");
				Elements elementsByClass15 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(15).getElementsByTag("dd");
				Elements elementsByClass16 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(16).getElementsByTag("dd");
				Elements elementsByClass17 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(17).getElementsByTag("dd");
				Elements elementsByClass18 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(18).getElementsByTag("dd");
				Elements elementsByClass19 = parse.getElementsByClass("listinfo").get(0).getElementsByTag("dl").get(19).getElementsByTag("dd");

				List<InsuranceTaiZhouMaternity> list = new ArrayList<InsuranceTaiZhouMaternity>();
				for (int i = 0; i < elementsByClass.size(); i++) {
					in = new InsuranceTaiZhouMaternity();
					in.setCompanyNum(elementsByClass.get(i).text());
					in.setCompnay(elementsByClass1.get(i).text());
					in.setType(elementsByClass2.get(i).text());
					in.setHospitalName(elementsByClass3.get(i).text());
					in.setInDate(elementsByClass4.get(i).text());
					in.setOutDate(elementsByClass5.get(i).text());
					in.setTime(elementsByClass6.get(i).text());
					in.setBabyBirth(elementsByClass7.get(i).text());
					in.setStatus(elementsByClass8.get(i).text());
					in.setLiuChan(elementsByClass9.get(i).text());
					in.setShouShu(elementsByClass10.get(i).text());
					in.setSum(elementsByClass11.get(i).text());
					in.setBaoXiao(elementsByClass12.get(i).text());
					in.setJintie(elementsByClass13.get(i).text());
					in.setJiSheng(elementsByClass14.get(i).text());
					in.setLiuJinTie(elementsByClass15.get(i).text());
					in.setBuZhu(elementsByClass16.get(i).text());
					in.setName(elementsByClass17.get(i).text());
					in.setIDNum(elementsByClass18.get(i).text());
					in.setPersonalNum(elementsByClass19.get(i).text());
					in.setTaskid(taskInsurance.getTaskid());
					list.add(in);
				}
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
			}
		}
		return webParam;
	}
	
	
	// 当前时间
	public String getTime(String fmt) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	
	/*
	 * @Des 获取当前月 的前i个年的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
