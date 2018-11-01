package app.parser;

import java.net.URL;
import java.net.URLEncoder;
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
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiEndowment;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiInjury;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiMaternity;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiMedical;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUnemployment;
import com.microservice.dao.entity.crawler.insurance.enshi.InsuranceEnShiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceEnShiParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://escsi.eszrsj.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//*[@id='userName']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getName());
		
		HtmlTextInput pwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='userCard']");
		pwd.reset();
		pwd.setText(insuranceRequestParameters.getUsername());
		
		HtmlImage img = page.getFirstByXPath("//*[@id='Im_Code']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1007");
		
		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='PageCodes']");
		h.setText(verifycode);
		
		HtmlElement firstByXPath = page.getFirstByXPath("//*[@id='QueryLogin']");
		Page page2 = firstByXPath.click();
		
//		System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("参保信息"))
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}		
		return null;
	}

	public  WebParam<InsuranceEnShiUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_General.aspx?CMD=A5FD5B2061264CF720E9345A652B8ED4";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page3 = webClient.getPage(webRequest);
		Document parse = Jsoup.parse(page3.getWebResponse().getContentAsString());
		WebParam<InsuranceEnShiUserInfo> webParam = new WebParam<InsuranceEnShiUserInfo>();
		if(page3.getWebResponse().getContentAsString().contains("个人参保信息"))
		{
			Element elementById = parse.getElementById("ctl00_ContentPlaceHolder1_BaseInfoTab");
//			System.out.println(elementById);
			InsuranceEnShiUserInfo i = new InsuranceEnShiUserInfo();
//			String nextLabelByKeyword = getNextLabelByKeyword(elementById, "姓名");
			i.setName(getNextLabelByKeyword(elementById, "姓名"));
			i.setSex(getNextLabelByKeyword(elementById, "性别"));
			i.setBirth(getNextLabelByKeyword(elementById, "出生日期"));
			i.setPersonal(getNextLabelByKeyword(elementById, "户口性质"));
			i.setNational(getNextLabelByKeyword(elementById, "民族"));
			i.setLevel(getNextLabelByKeyword(elementById, "文化程度"));
			i.setStatus(getNextLabelByKeyword(elementById, "人员状态"));
			i.setIDNum(getNextLabelByKeyword(elementById, "身份证号"));
			i.setJoinDate(getNextLabelByKeyword(elementById, "参加工作日期"));
			i.setPersonalNews(getNextLabelByKeyword(elementById, "个人身份"));
			i.setPhone(getNextLabelByKeyword(elementById, "联系电话"));
			i.setCompany(getNextLabelByKeyword(elementById, "工作单位"));
			i.setCompanyLevel(getNextLabelByKeyword(elementById, "行政职务"));
			i.setYongGong(getNextLabelByKeyword(elementById, "用工形式"));
			i.setEndDate(getNextLabelByKeyword(elementById, "离退休日期"));
			i.setProfessonal(getNextLabelByKeyword(elementById, "专业技术职务"));
			i.setWorker(getNextLabelByKeyword(elementById, "工人技术等级"));
			i.setSpecial(getNextLabelByKeyword(elementById, "特殊工种标识"));
			i.setHighMoney(getNextLabelByKeyword(elementById, "医保本年度基本医疗统筹支付最高限额"));
			i.setHouseLand(getNextLabelByKeyword(elementById, "居住地"));
			i.setHomeLand(getNextLabelByKeyword(elementById, "户口所在地"));
			i.setCb(getNextLabelByKeyword(elementById, "参保所在地"));
			i.setSbjg(getNextLabelByKeyword(elementById, "社保机构"));
			i.setTaskid(taskInsurance.getTaskid());
//			System.out.println(i);	
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setInsuranceEnShiUserInfo(i);;
			webParam.setUrl(url);
			return webParam;
		}
		
		return null;
	}
	
	 /**
     * @param elementById
     * @param keyword
     * @return
     * @Des 获取目标标签的下一个兄弟标签的内容
     */
    public  String getNextLabelByKeyword(Element elementById, String keyword) {
        Elements es = elementById.select("td:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Element nextElement = element.nextElementSibling();
            if (null != nextElement) {
                return nextElement.text();
            }
        }
        return null;
    }

    
    //医疗
	public WebParam<InsuranceEnShiMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		//医疗
		String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YbAccCon.aspx?CMD=E11A90A1FA2A2C12";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page33 = webClient.getPage(webRequest1);
//		System.out.println(page33.getWebResponse().getContentAsString());
		
		InsuranceEnShiMedical in = null;
		List<InsuranceEnShiMedical> list = new ArrayList<InsuranceEnShiMedical>();
		WebParam<InsuranceEnShiMedical> webParam = new WebParam<InsuranceEnShiMedical>();

		if(page33.getWebResponse().getContentAsString().contains("医保缴费查询"))
		{
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YbAccCon.aspx?CMD=E11A90A1FA2A2C12";
			for (int j =0 ; j <6; j++) {
				String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears="+getDateBefore("yyyy", j)+"&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
				WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
				requestSettings.setRequestBody(a);
				Page page4 = webClient.getPage(requestSettings);
//				System.out.println(page4.getWebResponse().getContentAsString());
				if(page4.getWebResponse().getContentAsString().contains("查询结果为空,请更换年份再查询"))
				{
					break;
				}
				else
				{
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					Document parse = Jsoup.parse(page4.getWebResponse().getContentAsString());
					
					Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView4").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int i = 1; i < elementById.size(); i++) { 
//						System.out.println(elementById.get(i));	
						in = new InsuranceEnShiMedical();
						in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
						in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
						in.setXz(elementById.get(i).getElementsByTag("td").get(2).text());
						in.setMoneyType(elementById.get(i).getElementsByTag("td").get(3).text());
						in.setPayType(elementById.get(i).getElementsByTag("td").get(4).text());
						in.setBase(elementById.get(i).getElementsByTag("td").get(5).text());
						in.setPayNow(elementById.get(i).getElementsByTag("td").get(6).text());
						in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(7).text());
						in.setGetFlag(elementById.get(i).getElementsByTag("td").get(8).text());
						in.setGetDate(elementById.get(i).getElementsByTag("td").get(9).text());
						in.setSendFlag(elementById.get(i).getElementsByTag("td").get(10).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
				}
			}
			webParam.setList(list);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}
	
	//时间
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	//养老
	public WebParam<InsuranceEnShiEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YlAccCon.aspx?CMD=9B1F78E47D043A3F2253E780063C1D40";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page33 = webClient.getPage(webRequest1);
//		System.out.println(page33.getWebResponse().getContentAsString());
		
		InsuranceEnShiEndowment in = null;
		List<InsuranceEnShiEndowment> list = new ArrayList<InsuranceEnShiEndowment>();
		WebParam<InsuranceEnShiEndowment> webParam = new WebParam<InsuranceEnShiEndowment>();

		if(page33.getWebResponse().getContentAsString().contains("养老缴费查询"))
		{
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_YlAccCon.aspx?CMD=9B1F78E47D043A3F2253E780063C1D40";
			for (int j =0 ; j <6; j++) {
				String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears="+getDateBefore("yyyy", j)+"&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
				WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
				requestSettings.setRequestBody(a);
				Page page4 = webClient.getPage(requestSettings);
//				System.out.println(page4.getWebResponse().getContentAsString());
				if(page4.getWebResponse().getContentAsString().contains("查询结果为空,请更换年份再查询"))
				{
					break;
				}
				else
				{
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					Document parse = Jsoup.parse(page4.getWebResponse().getContentAsString());
					
					Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView2").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int i = 1; i < elementById.size(); i++) {
//						System.out.println(elementById.get(i));	
						in = new InsuranceEnShiEndowment();
						in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
						in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
						in.setMoneyType(elementById.get(i).getElementsByTag("td").get(2).text());
						in.setPayType(elementById.get(i).getElementsByTag("td").get(3).text());
						in.setBase(elementById.get(i).getElementsByTag("td").get(4).text());
						in.setPayNow(elementById.get(i).getElementsByTag("td").get(5).text());
						in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(6).text());
						in.setGetFlag(elementById.get(i).getElementsByTag("td").get(7).text());
						in.setGetDate(elementById.get(i).getElementsByTag("td").get(8).text());
						in.setSendFlag(elementById.get(i).getElementsByTag("td").get(9).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
				}
			}
			webParam.setList(list);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}

	//生育
	public WebParam<InsuranceEnShiMaternity> crawlerMaternity(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_BirthInsAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page33 = webClient.getPage(webRequest1);
//		System.out.println(page33.getWebResponse().getContentAsString());
		
		InsuranceEnShiMaternity in = null;
		List<InsuranceEnShiMaternity> list = new ArrayList<InsuranceEnShiMaternity>();
		WebParam<InsuranceEnShiMaternity> webParam = new WebParam<InsuranceEnShiMaternity>();

		if(page33.getWebResponse().getContentAsString().contains("生育保险缴费查询"))
		{
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_BirthInsAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
			for (int j =0 ; j <6; j++) {
				String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears="+getDateBefore("yyyy", j)+"&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
				WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
				requestSettings.setRequestBody(a);
				Page page4 = webClient.getPage(requestSettings);
//				System.out.println(page4.getWebResponse().getContentAsString());
				if(page4.getWebResponse().getContentAsString().contains("查询结果为空,请更换年份再查询"))
				{
					break;
				}
				else
				{
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					Document parse = Jsoup.parse(page4.getWebResponse().getContentAsString());
					
					Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int i = 1; i < elementById.size(); i++) {
//						System.out.println(elementById.get(i));	
						in = new InsuranceEnShiMaternity();
						in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
						in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
						in.setMoneyType(elementById.get(i).getElementsByTag("td").get(2).text());
						in.setPayType(elementById.get(i).getElementsByTag("td").get(3).text());
						in.setBase(elementById.get(i).getElementsByTag("td").get(4).text());
						in.setPayNow(elementById.get(i).getElementsByTag("td").get(5).text());
						in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(6).text());
						in.setGetFlag(elementById.get(i).getElementsByTag("td").get(7).text());
						in.setGetDate(elementById.get(i).getElementsByTag("td").get(8).text());
						in.setSendFlag(elementById.get(i).getElementsByTag("td").get(9).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
				}
			}
			webParam.setList(list);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}

	
	//工伤
	public WebParam<InsuranceEnShiInjury> crawlerInjury(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_IndInjAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page33 = webClient.getPage(webRequest1);
//		System.out.println(page33.getWebResponse().getContentAsString());
		
		InsuranceEnShiInjury in = null;
		List<InsuranceEnShiInjury> list = new ArrayList<InsuranceEnShiInjury>();
		WebParam<InsuranceEnShiInjury> webParam = new WebParam<InsuranceEnShiInjury>();

		if(page33.getWebResponse().getContentAsString().contains("工伤保险缴费查询"))
		{
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_IndInjAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
			for (int j =0 ; j <6; j++) {
				String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears="+getDateBefore("yyyy", j)+"&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
				WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
				requestSettings.setRequestBody(a);
				Page page4 = webClient.getPage(requestSettings);
//				System.out.println(page4.getWebResponse().getContentAsString());
				if(page4.getWebResponse().getContentAsString().contains("查询结果为空,请更换年份再查询"))
				{
					break;
				}
				else
				{
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					Document parse = Jsoup.parse(page4.getWebResponse().getContentAsString());
					
					Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int i = 1; i < elementById.size(); i++) {
//						System.out.println(elementById.get(i));	
						in = new InsuranceEnShiInjury();
						in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
						in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
						in.setMoneyType(elementById.get(i).getElementsByTag("td").get(2).text());
						in.setPayType(elementById.get(i).getElementsByTag("td").get(3).text());
						in.setBase(elementById.get(i).getElementsByTag("td").get(4).text());
						in.setPayNow(elementById.get(i).getElementsByTag("td").get(5).text());
						in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(6).text());
						in.setGetFlag(elementById.get(i).getElementsByTag("td").get(7).text());
						in.setGetDate(elementById.get(i).getElementsByTag("td").get(8).text());
						in.setSendFlag(elementById.get(i).getElementsByTag("td").get(9).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
				}
				
			}
			webParam.setList(list);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}

	//失业
	public WebParam<InsuranceEnShiUnemployment> crawlerUnemployment(
			InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskInsurance.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String u=   "http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_UnemInsAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		HtmlPage page33 = webClient.getPage(webRequest1);
//		System.out.println(page33.getWebResponse().getContentAsString());
		
		InsuranceEnShiUnemployment in = null;
		List<InsuranceEnShiUnemployment> list = new ArrayList<InsuranceEnShiUnemployment>();
		WebParam<InsuranceEnShiUnemployment> webParam = new WebParam<InsuranceEnShiUnemployment>();

		if(page33.getWebResponse().getContentAsString().contains("失业保险缴费查询"))
		{
			HtmlHiddenInput firstByXPath44 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATE']");
			String attribute44 = firstByXPath44.getAttribute("value");
			String encode44 = URLEncoder.encode(attribute44, "UTF-8");
			
			HtmlHiddenInput firstByXPath55 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__EVENTVALIDATION']");
			String attribute55 = firstByXPath55.getAttribute("value");
			String encode55 = URLEncoder.encode(attribute55, "UTF-8");
			
			HtmlHiddenInput firstByXPath66 = (HtmlHiddenInput)page33.getFirstByXPath("//*[@id='__VIEWSTATEGENERATOR']");
			String attribute66 = firstByXPath66.getAttribute("value");
			String encode66 = URLEncoder.encode(attribute66, "UTF-8");
			String url4="http://escsi.eszrsj.gov.cn/QueryEntity/CSIM_UnemInsAccCon.aspx?CMD=0D5C4CD2407B762619FD18D4C1690EB2";
			for (int j =0 ; j <6; j++) {
				String a="&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode44+"&__VIEWSTATEGENERATOR="+encode66+"&__EVENTVALIDATION="+encode55+"&ctl00%24ContentPlaceHolder1%24QueryYears="+getDateBefore("yyyy", j)+"&ctl00%24ContentPlaceHolder1%24QueryResultBtn=%E6%9F%A5%E8%AF%A2";
				WebRequest requestSettings = new WebRequest(new URL(url4), HttpMethod.POST);
				requestSettings.setRequestBody(a);
				Page page4 = webClient.getPage(requestSettings);
//				System.out.println(page4.getWebResponse().getContentAsString());
				if(page4.getWebResponse().getContentAsString().contains("查询结果为空,请更换年份再查询"))
				{
					break;
				}
				else
				{
					webParam.setHtml(page4.getWebResponse().getContentAsString());
					Document parse = Jsoup.parse(page4.getWebResponse().getContentAsString());
					Elements elementById = parse.getElementById("ctl00_ContentPlaceHolder1_GridView1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
					for (int i = 1; i < elementById.size(); i++) {
//						System.out.println(elementById.get(i));	
						in = new InsuranceEnShiUnemployment();
						in.setPersonalNum(elementById.get(i).getElementsByTag("td").get(0).text());
						in.setInDate(elementById.get(i).getElementsByTag("td").get(1).text());
						in.setMoneyType(elementById.get(i).getElementsByTag("td").get(2).text());
						in.setPayType(elementById.get(i).getElementsByTag("td").get(3).text());
						in.setBase(elementById.get(i).getElementsByTag("td").get(4).text());
						in.setPayNow(elementById.get(i).getElementsByTag("td").get(5).text());
						in.setPersonalMoney(elementById.get(i).getElementsByTag("td").get(6).text());
						in.setGetFlag(elementById.get(i).getElementsByTag("td").get(7).text());
						in.setGetDate(elementById.get(i).getElementsByTag("td").get(8).text());
						in.setSendFlag(elementById.get(i).getElementsByTag("td").get(9).text());
						in.setTaskid(taskInsurance.getTaskid());
						list.add(in);
					}
				}
			}
			webParam.setList(list);
			webParam.setUrl(url4);
			return webParam;
		}
		return null;
	}


}
