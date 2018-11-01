package app.parser;

import java.io.UnsupportedEncodingException;
import java.net.URL;
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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinAccount;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

@Component
public class HousingFundHaErBinParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.login.start", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
//		 String str = "23100419870611142X";  
//		 String str1="121212";
	     String base64=getBase64(messageLoginForHousing.getNum());  
	     String base641=getBase64(messageLoginForHousing.getPassword());  
	     System.out.println(base64+base641); 
		//登陆
		String u2="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/per.login?hidCertinum="+base64+"&certinum="+messageLoginForHousing.getNum()+"&perpwd="+base641+"&vericodesess="+taskHousing.getWebdriverHandle()+"&vericode="+taskHousing.getWebdriverHandle()+"&vcode="+messageLoginForHousing.getSms_code();
		WebParam webParam = new WebParam();
		WebRequest webRequest = new WebRequest(new URL(u2), HttpMethod.POST);
		Page page3 = webClient.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("欢迎您")){
			webParam.setUrl(u2);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setWebClient(webClient);
			return webParam;
		}
//		进行身份校验时出错
//		String url="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		
//		HtmlPage page = webClient.getPage(url);		
//		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@name='certinum']");
//		id_card.reset();
//		id_card.setText(messageLoginForHousing.getNum());
//		
//		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//input[@name='perpwd']");
//		id_account.reset();
//		id_account.setText(messageLoginForHousing.getPassword());
//		
//		HtmlImage img = page.getFirstByXPath("//*[@id='tabs-1']/form/div[3]/div/span/img");
//		
//		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
//		
//		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@id='validcode']");
//		identifying.reset();
//		identifying.setText(verifycode);
//		
//		HtmlTextInput vcode = (HtmlTextInput)page.getFirstByXPath("//input[@id='vcode']");
//		vcode.reset();
//		vcode.setText(messageLoginForHousing.getSms_code());
//		
//		
//		HtmlElement button = page.getFirstByXPath("//*[@id='tabs-1']/form/div[6]/div/button");
//		HtmlPage page2 = button.click();
//		Thread.sleep(1000);
//		System.out.println(page2.getWebResponse().getContentAsString());
//		
//		Thread.sleep(3000);
//		if(null != page2)
//		{
//			int code = page2.getWebResponse().getStatusCode();
//			if(code==200)
//			{
//				if(alertMsg=="")
//				{
//					String string = page2.getWebResponse().getContentAsString();
//					webParam.setCode(code);
//					webParam.setHtml(string);
//					webParam.setHtmlPage(page2);
//					webParam.setUrl(verifycode);
//					webParam.setWebClient(page2.getWebClient());
//					return webParam;
//				}
//				else
//				{
//					webParam.setCode(code);
//					webParam.setHtml(alertMsg);
//					webParam.setHtmlPage(page2);
//					webParam.setUrl(url);
//					webParam.setWebClient(page2.getWebClient());
//					return webParam;
//				}
//			}
//		}
		return null;
	}

	//个人信息
	public WebParam<HousingFundHaErBinUserInfo> getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.getUserInfo.start", taskHousing.getTaskid());
		
		String url = "http://bh.hrbgjj.org.cn:47598/hrbwsyyt/init.summer?_PROCID=70000001";
		WebParam<HousingFundHaErBinUserInfo> webParam = new WebParam<HousingFundHaErBinUserInfo>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("个人基本信息查询"))
		{
				Document parse = Jsoup.parse(page.getWebResponse().getContentAsString());
				HousingFundHaErBinUserInfo h = new HousingFundHaErBinUserInfo();
				h.setName(parse.getElementById("accname").val());
				h.setStatus(parse.getElementById("proptype").val());
				h.setCardType(parse.getElementById("certitype").val());
				h.setIdCard(parse.getElementById("certinum").val());
				h.setOpenDate(parse.getElementById("opnaccdate").val());
				h.setPhone(parse.getElementById("handset").val());
				h.setPersonalNum(parse.getElementById("accnum").val());
				h.setPersonalStatus(parse.getElementById("indiaccstate").val());
				h.setComapnyNum(parse.getElementById("unitaccnum").val());
				h.setCompany(parse.getElementById("unitaccname").val());
				h.setCompanyRatio(parse.getElementById("unitprop").val());
				h.setPersonalRatio(parse.getElementById("indiprop").val());
				h.setPersonalBase(parse.getElementById("basenum").val());
				h.setLastDate(parse.getElementById("lpaym").val());
				h.setPersonalMonth(parse.getElementById("indipayamt").val());
				h.setCompanyMonth(parse.getElementById("unitpayamt").val());
				h.setMonthSave(parse.getElementById("indipaysum").val());
				h.setFee(parse.getElementById("bal").val());
				h.setTaskid(taskHousing.getTaskid());
				System.out.println(h);
				webParam.setHousingHaErBinUserInfo(h);
				webParam.setUrl(url);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				return webParam;
			}
		return null;
	}

	
	//发验证码
	public WebParam getCode(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//图片验证码
		String u="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/vericode.jsp?0.20117761121627398";
		Page page4 = webClient.getPage(u);
//		System.out.println(page4.getWebResponse().getContentAsString());
		String u1="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/getvercode.jsp?task=getvercode";
		Page page2 = webClient.getPage(u1);
		String string = page2.getWebResponse().getContentAsString();
		JSONObject fromObject = JSONObject.fromObject(string);
		String string2 = fromObject.getString("vericode");
		System.out.println(string2);
				
		//短信验证码
		String url="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/platform/workflow/sendMessage.jsp?uuid="+System.currentTimeMillis()+"&task=send&trancode=430303&type=socket&message=%3Ccertinum%3E"+messageLoginForHousing.getNum()+"%3C%2F%3E";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page.getWebResponse().getContentAsString().contains("ok"))
		{
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			webParam.setHtml(string2);
			return webParam;
		}
		return null;
	}
	
	
	 //加密  
    public static String getBase64(String str){  
        byte[] b=null;  
        String s=null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if(b!=null){  
            s=new BASE64Encoder().encode(b);  
        }  
        return s;  
    }

    
    //流水
	public WebParam<HousingFundHaErBinAccount> getAccount(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing,int time) throws Exception {
		WebParam<HousingFundHaErBinAccount> webParam = new WebParam<HousingFundHaErBinAccount>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies2 = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookies2);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
	    }
		
		String u="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/init.summer?_PROCID=70000002";
		WebRequest webRequest1 = new WebRequest(new URL(u), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest1);
		System.out.println(page2.getWebResponse().getContentAsString());
		
		
//		String u2="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/command.summer?uuid="+System.currentTimeMillis();
//		WebRequest webRequest2 = new WebRequest(new URL(u2), HttpMethod.POST);
//		webRequest2.setRequestBody("%24page=%2Fydpx%2F70000002%2F700002_01.ydpx&_ACCNUM=801015274307&_RW=w&_PAGEID=step1&_IS=-726085&_UNITACCNAME=%E9%BB%91%E9%BE%99%E6%B1%9F%E7%9C%81%E6%98%93%E6%89%8D%E4%BA%BA%E5%8A%9B%E8%B5%84%E6%BA%90%E5%92%A8%E8%AF%A2%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8&_LOGIP=20180428141044527&_ACCNAME=%E6%9D%8E%E5%A8%87%E5%A9%B7&isSamePer=false&_PROCID=70000002&_SENDOPERID=23100419870611142X&_DEPUTYIDCARDNUM=23100419870611142X&_SENDTIME=2018-04-28&_BRANCHKIND=0&_SENDDATE=2018-04-28&CURRENT_SYSTEM_DATE=2018-04-28&_TYPE=init&_ISCROP=0&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2&_WITHKEY=0&begdate=2018-01-01&enddate=2018-12-31&year=2018&accnum=801015274307&flag=1");
//		Page page3 = webClient.getPage(webRequest2);
//		System.out.println(page3.getWebResponse().getContentAsString());
		
		Document parse = Jsoup.parse(page2.getWebResponse().getContentAsString());
		Elements elementsByTag = parse.getElementsByTag("textarea");
		String text = elementsByTag.get(0).text();
		String text2 = elementsByTag.get(1).text();
		String encode = "rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAMODAxMDE1Mjc0MzA3dAADX1JXdAABd3QAC19VTklUQUNDTlVNcHQAB19QQUdFSUR0%0AAAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2%0AYS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2FTru3QADF9VTklUQUNDTkFNRXQAMOm7kem%2B%0Ameaxn%2BecgeaYk%2BaJjeS6uuWKm%2Bi1hOa6kOWSqOivouaciemZkOWFrOWPuHQABl9MT0dJUHQAETIw%0AMTgwNDI4MTQxMDQ0NTI3dAAIX0FDQ05BTUV0AAnmnY7lqIflqbd0AAlpc1NhbWVQZXJ0AAVmYWxz%0AZXQAB19QUk9DSUR0AAg3MDAwMDAwMnQAC19TRU5ET1BFUklEdAASMjMxMDA0MTk4NzA2MTExNDJY%0AdAAQX0RFUFVUWUlEQ0FSRE5VTXEAfgAadAAJX1NFTkRUSU1FdAAKMjAxOC0wNC0yOHQAC19CUkFO%0AQ0hLSU5EdAABMHQACV9TRU5EREFURXQACjIwMTgtMDQtMjh0ABNDVVJSRU5UX1NZU1RFTV9EQVRF%0AcQB%2BACF0AAVfVFlQRXQABGluaXR0AAdfSVNDUk9QcQB%2BAB90AAlfUE9SQ05BTUV0ABjkuKrkurrm%0AmI7nu4bkv6Hmga%2Fmn6Xor6J0AAdfVVNCS0VZcHQACF9XSVRIS0VZcQB%2BAB94dAAIQFN5c0RhdGV0%0AAAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBTeXNZZWFy";
		
		String encode2 = "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0ANRzZWxlY3QgaW5zdGFuY2VudW0sIHRyYW5zZGF0ZSwgcGVv%0AcGxlbnVtLCBmcmVldXNlNCwgdW5pdGFjY25hbWUsIGFtdDQsIGFtdDEsIGFtdDIsIGJhc2VudW1i%0AZXIsIGZyZWV1c2UxLCBiZWdpbmRhdGVjLCBlbmRkYXRlYywgZnJlZXVzZTYgZnJvbSBkcDA3NyB3%0AaGVyZSBpbnN0YW5jZW51bSA9LTcyNjA4NSBvcmRlciBieSB0cmFuc2RhdGUgZGVzYywgZnJlZXVz%0AZTYgZGVzY3g%3D";
		System.out.println(encode);
		System.out.println(encode2);
//		String url = "http://bh.hrbgjj.org.cn:47598/hrbwsyyt/dynamictable?uuid="+System.currentTimeMillis();

		String url = "http://bh.hrbgjj.org.cn:47598/hrbwsyyt/dynamictable?uuid="+System.currentTimeMillis()+"&dynamicTable_id=datalist2&dynamicTable_currentPage=0&dynamicTable_pageSize=10&dynamicTable_nextPage=1&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx&dynamicTable_paging=true&dynamicTable_configSqlCheck=0&errorFilter=1%3D1&begdate="+getDateBefore("yyyy", time)+"-01-01&enddate="+getDateBefore("yyyy", time)+"-12-31&year="+getDateBefore("yyyy",time)+"&accnum="+taskHousing.getWebdriverHandle()+"&_APPLY=0&_CHANNEL=1&_PROCID=70000002&DATAlISTGHOST="+encode2+"&_DATAPOOL_="+encode;
		System.out.println(url);
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Origin", "http://bh.hrbgjj.org.cn:47598");
		webRequest.setAdditionalHeader("Host", "bh.hrbgjj.org.cn:47598");
		webRequest.setAdditionalHeader("Referer","http://bh.hrbgjj.org.cn:47598/hrbwsyyt/init.summer?_PROCID=70000002");
		webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.62 Safari/537.36");
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>(); 
//		paramsList.add(new NameValuePair("dynamicTable_id", "datalist2")); 
//		paramsList.add(new NameValuePair("dynamicTable_currentPage", "0")); 
//		paramsList.add(new NameValuePair("dynamicTable_pageSize", "10"));
//		paramsList.add(new NameValuePair("dynamicTable_nextPage", "1"));
//		paramsList.add(new NameValuePair("dynamicTable_page", "/ydpx/70000002/700002_01.ydpx"));
//		paramsList.add(new NameValuePair("dynamicTable_paging", "true"));
//		paramsList.add(new NameValuePair("dynamicTable_configSqlCheck", "0"));
//		paramsList.add(new NameValuePair("errorFilter", "1=1"));
//		paramsList.add(new NameValuePair("begdate", "2018-01-01"));
//		paramsList.add(new NameValuePair("enddate", "2018-12-31"));
//		paramsList.add(new NameValuePair("year", "2018"));
//		paramsList.add(new NameValuePair("accnum", "801015274307"));
//		paramsList.add(new NameValuePair("_APPLY", "0"));
//		paramsList.add(new NameValuePair("_CHANNEL", "1"));
//		paramsList.add(new NameValuePair("_PROCID", "70000002"));
//		paramsList.add(new NameValuePair("DATAlISTGHOST", encode));
//		paramsList.add(new NameValuePair("_DATAPOOL_", encode2));
//		webRequest.setRequestParameters(paramsList);

		
//		webRequest.setRequestBody("");
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("unitaccname"))
		{
			JSONObject fromObject = JSONObject.fromObject(page.getWebResponse().getContentAsString());
			System.out.println(fromObject);
			String string = fromObject.getString("data");
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string2 = fromObject2.getString("data");
			System.out.println(string2);
			JSONArray fromObject3 = JSONArray.fromObject(string2);
			HousingFundHaErBinAccount h = null;
			List<HousingFundHaErBinAccount> list= new ArrayList<HousingFundHaErBinAccount>();
			for (int i = 0; i < fromObject3.size(); i++) {
				h = new HousingFundHaErBinAccount();
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(i));
				h.setDatea(fromObject4.getString("transdate"));
				h.setDescr(fromObject4.getString("freeuse4"));
				h.setMoney(fromObject4.getString("amt4"));
				h.setFee(fromObject4.getString("basenumber"));
				if(fromObject4.getString("freeuse1").contains("0"))
				{
					h.setStatus("正常");
				}
				else
				{
					h.setStatus("非正常");
				}
				h.setStartDate(fromObject4.getString("begindatec"));
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
				System.out.println(list);
			}
			webParam.setList(list);
			webParam.setUrl(url);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		}
		return null;
	}
	
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
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
