package app.parser;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouAccount;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;

@Component
public class HousingFundZhuZhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://zhfw.zzsgjj.com/ish/index.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='zdyyhm']");
		searchpwd.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='dlmm']");
		searchpwd1.setText(messageLoginForHousing.getPassword());
		
        HtmlImage img = page.getFirstByXPath("//*[@id='yzm_pic']");
		
//		String imageName = "111.jpg";
//		File file = new File("D:\\img\\" + imageName);
//		img.saveAs(file); 
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='yzm']");
		identifying.reset();
		identifying.setText(verifycode); 
		
		HtmlElement button = page.getFirstByXPath("//*[@id='login_tab_0']/form/div/div[6]/button[2]");
		HtmlPage page2 = button.click();
//		System.out.println(page2.getWebResponse().getContentAsString());//您好，欢迎登录本系统	
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("您好，欢迎登录本系统"))
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	public WebParam<HousingFundZhuZhouUserInfo> crawlerUser(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String url2="http://zhfw.zzsgjj.com/ish/flow/menu/GRXXQRY";
		Page page3 = webClient.getPage(url2);
//		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<HousingFundZhuZhouUserInfo> webParam = new WebParam<HousingFundZhuZhouUserInfo>();
		if(page3.getWebResponse().getContentAsString().contains("个人基本信息查询"))
		{
			Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
			
			int indexOf = page3.getWebResponse().getContentAsString().indexOf("_POOLKEY");
			String substring = page3.getWebResponse().getContentAsString().substring(indexOf);
			String substring2 = substring.substring(11,56);//流水参数
//			System.out.println(substring2);
			
			
			HousingFundZhuZhouUserInfo h = new HousingFundZhuZhouUserInfo();
			h.setPersonalNum(doc.getElementById("grzh").val());
			System.out.println(doc.getElementById("zjlx").val());
			if(doc.getElementById("zjlx").val().contains("01"))
			{
				h.setStatus("身份证");//01身份证02
			}
			else if(doc.getElementById("zjlx").val().contains("02"))
			{
				h.setStatus("军官证");//01身份证02
			}
			else if(doc.getElementById("zjlx").val().contains("03"))
			{
				h.setStatus("护照");//01身份证02
			}
			else if(doc.getElementById("zjlx").val().contains("04"))
			{
				h.setStatus("外国人永久居住证");//01身份证02
			}
			
			else if(doc.getElementById("frzflag").val().contains("05"))
			{
				h.setStatus("港澳通行证");//01身份证02
			}
			else if(doc.getElementById("zjlx").val().contains("06"))
			{
				h.setStatus("其他");//01身份证02
			}
			h.setCardNum(doc.getElementById("zjhm").val());
			h.setName(doc.getElementById("xingming").val());
			h.setCompanyNum(doc.getElementById("dwzh").val());
			h.setCompany(doc.getElementById("unitaccname").val());
			h.setPhone(doc.getElementById("sjhm").val());
			h.setOpenDate(doc.getElementById("khrq").val());
			h.setStatus(doc.getElementById("grzhzt").val());
			h.setYf(doc.getElementById("frzflag").val());
			h.setYfMoney(doc.getElementById("isloanflag").val());
			h.setEndDate(doc.getElementById("jzny").val());
			h.setPersonalBase(doc.getElementById("grjcjs").val());
			h.setMonthPay(doc.getElementById("indipaysum").val());
			h.setCompanyPay(doc.getElementById("dwyjce").val());
			h.setPersonalMonth(doc.getElementById("gryjce").val());
			h.setCompanyRatio(doc.getElementById("unitprop").val());
			h.setPersonalRatio(doc.getElementById("indiprop").val());
			h.setFee(doc.getElementById("grzhye").val());
			h.setConnectNum(doc.getElementById("cardno").val());
			h.setTaskid(taskHousing.getTaskid());
			webParam.setUrl(url2);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setHousingFundZhuZhouUserInfo(h);
			taskHousing.setCrawlerHost(substring2);
			taskHousingRepository.save(taskHousing);
			return webParam;
		}
		
		return null;
	}

	public WebParam<HousingFundZhuZhouAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		String u="http://zhfw.zzsgjj.com/ish/flow/command/GRMXQRY/step1/grmxqry/"+taskHousing.getCrawlerHost()+"?grzh="+taskHousing.getWebdriverHandle()+"&grxm="+URLEncoder.encode(messageLoginForHousing.getUsername(), "UTF-8")+"&begdate="+getDateBefore("yyyy-MM-dd", 6)+"&enddate="+getDate("yyyy-MM-dd");
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.POST);
		Page page2 = webClient.getPage(webRequest1);
		if(page2.getWebResponse().getContentAsString().contains("succ"))
		{
			String url="http://zhfw.zzsgjj.com/ish/ydpx/parsepage";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setCharset(Charset.forName("UTF-8")); 
			webRequest.setRequestBody("grzh="+taskHousing.getWebdriverHandle()+"&grxm="+URLEncoder.encode(messageLoginForHousing.getUsername(), "UTF-8")+"&begdate="+getDateBefore("yyyy-MM-dd",6)+"&enddate="+getDate("yyyy-MM-dd")+"&%24page=GRYW_GRZHMXQRY.ydpx&_POOLKEY="+taskHousing.getCrawlerHost()+"&list_id=grmxlist&dataset_id=grmx&list_page_no=1&grmx_order_by=");
			Page page = webClient.getPage(webRequest);
			if(page.getWebResponse().getContentAsString().contains("grmxlist"))
			{
				JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
				String string = fromObject.getString("html");
				System.out.println(page.getWebResponse().getContentAsString());
				Document doc = Jsoup.parse(string);
				Elements elementsByTag = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
//				System.out.println(elementsByTag);
				HousingFundZhuZhouAccount h = null;
				List<HousingFundZhuZhouAccount> list = new ArrayList<HousingFundZhuZhouAccount>();
				WebParam<HousingFundZhuZhouAccount> webParam = new WebParam<HousingFundZhuZhouAccount>();
				for (int i = 0; i < elementsByTag.size(); i++) {
					h = new HousingFundZhuZhouAccount();
					Elements elementsByTag2 = elementsByTag.get(i).getElementsByTag("td");
//					System.out.println(elementsByTag2.text());
					h.setDatea(elementsByTag2.get(0).text());
					h.setPersonalNum(elementsByTag2.get(1).text());
					h.setName(elementsByTag2.get(2).text());
					h.setCompanyNum(elementsByTag2.get(3).text());
					h.setCompany(elementsByTag2.get(4).text());
					h.setMoney(elementsByTag2.get(5).text());
					h.setFee(elementsByTag2.get(6).text());
					h.setDesce(elementsByTag2.get(7).text());
					h.setStartDate(elementsByTag2.get(8).text());
					h.setEndDate(elementsByTag2.get(9).text());
					h.setPeople(elementsByTag2.get(10).text());
					h.setTaskid(taskHousing.getTaskid());
					list.add(h);
				}
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(url);
				return webParam;
			}
		}
		return null;
	}
	
	
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	
	public String getDate(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	

}
