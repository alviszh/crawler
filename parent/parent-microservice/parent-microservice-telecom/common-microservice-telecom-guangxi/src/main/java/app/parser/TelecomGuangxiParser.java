package app.parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBill;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiCall;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiMessage;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiPay;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiScore;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.domain.crawler.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomGuangxiParser {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	// 当前时间
	public String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	/**
	 * 获取手机验证码
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomGuangxiParser> getphonecodeTwo(MessageLogin messageLogin, TaskMobile taskMobile)throws Exception {
			String	mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8");
			WebParam<TelecomGuangxiParser> webParam = new WebParam<TelecomGuangxiParser>();
			WebClient webClient = addcookieFirst(taskMobile);
//			String url="http://gx.189.cn/chaxun/iframe/user_center.jsp?SERV_NO=FCX-4";
//			Page page2 = webClient.getPage(url);
//			String json = page2.getWebResponse().getContentAsString();
//			if(json.contains("PRODTYPE"))
//			{
//				int indexOf = json.indexOf("PRODTYPE");
//				String substring2 = json.substring(indexOf+10, indexOf+17);
//				System.out.println(substring2);
				String urlData ="http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE="+taskMobile.getTesthtml()+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+taskMobile.getTesthtml()+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE="+getTime()+"&ACCT_DATE_1="+getTime()+"&PASSWORD=&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+messageLogin.getIdNum();
//				String urlData ="http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2020966&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2020966&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&ACCT_DATE=201808&ACCT_DATE_1=201808&FIND_TYPE=1031&radioQryType=on&PASSWORD=&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_TYPE=1&CARD_NO=340602199307040416";
//				PRODTYPE=2020966&RAND_TYPE=002&BureauCode=1100&ACC_NBR=18172055939&PROD_TYPE=2020966&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME=18172055939&OPER_TYPE=CR1&ACCT_DATE=201810&ACCT_DATE_1=201810&FIND_TYPE=1031&radioQryType=on&PASSWORD=&CUST_NAME=%E6%9D%A8%E7%A3%8A&CARD_TYPE=1&CARD_NO=340602199307040416
				Page page = webClient.getPage(urlData);
//				System.out.println(page.getWebResponse().getContentAsString());
				webParam.setWebClient(webClient);
				webParam.setHtml(page.getWebResponse().getContentAsString());
//			}
			return webParam;
	}

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomGuangxiParser.getHtml---url:" + url + " ", taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomGuangxiParser.getHtml.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomGuangxiParser.getHtml---url:" + url + "---taskid:" + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			return searchPage;
		}
		return null;
	}

	/**
	 * 验证验证码
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public WebParam setphonecodeTwo(TaskMobile taskMobile, MessageLogin messageLogin, String starttime, String endtime)
	throws Exception {
		    tracer.addTag("parser.TelecomSetphoneTwo.taskMobile", taskMobile.getTaskid());
//		    System.out.println(taskMobile.getBasicUser().getName());
		    WebParam webParam = new WebParam();
			String	mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8");
			if(mytext2 != null)
			{
				String urlData = "http://gx.189.cn/public/realname/checkRealName.jsp?NUM=" + messageLogin.getName()+ "&V_PASSWORD=" + messageLogin.getSms_code() + "&CUST_NAME=" +mytext2+ "&CARD_NO="+taskMobile.getBasicUser().getIdnum()+ "&CARD_TYPE=1&RAND_TYPE=002";
				WebClient webClient = addcookie(taskMobile);
				//Html page = getPage(webClient, taskMobile, urlData, null);
				Page page =  getHtmlPage(urlData, webClient);
				
				if (null != page) {
					String html = page.getWebResponse().getContentAsString();
					tracer.addTag("parser.TelecomSetPhoneTwo.Success" + taskMobile.getTaskid(),"<xmp>" + html + "</xmp>");
					webParam.setWebClient(webClient);
					webParam.setHtml(html);
				}
			}
			return webParam;
	}
	
	
	public WebClient addcookie(TaskMobile taskMobile) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}
	
	//用于第一次【爬取的addCookie
	public WebClient addcookieFirst(TaskMobile taskMobile) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getNexturl());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type) throws Exception {
		tracer.addTag("TelecomGuangxiParser.getPage---url:", url + "taskId:" + taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomGuangxiParser.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomGuangxiParser.getPage---taskid:",
					taskMobile.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}
	
	/**
	 * 
	 * 
	 */
	public  Page getHtmlPage(String url, WebClient webClient) throws Exception, IOException {
		WebRequest webRequest;
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;

	}
	
	//本地通话详单 
	public WebParam<TelecomGuangxiCall> getCall(MessageLogin messageLogin, TaskMobile taskMobile,int a) throws Exception {
		Thread.sleep(2000);
		String dateBefore = getDateBefore("yyyyMM", a);
		tracer.addTag("TelecomGuangxiParser.getCall" , taskMobile.getTaskid());
		String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 
	  
		WebClient webClient = addcookie(taskMobile);
		
		//找到prod_type
		String url1 ="http://gx.189.cn/chaxun/iframe/qdcx_new_div.jsp?SERV_NO=FCX-4";
		Page page2 = webClient.getPage(url1);
		if(page2.getWebResponse().getContentAsString().contains("qry_num"))
		{
			Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
			Element elementById = doc1.getElementById("qry_num");
//			System.out.println(elementById);
			String string = elementById.toString();
			int area_codel = string.indexOf("area_code");
			int prod_typel = string.indexOf("prod_type");
			int prod_namel = string.indexOf("prod_name");
			String substring = string.substring(area_codel, prod_typel);
			String area_code = substring.substring(11,substring.length()-2);//城市代码
			String substring1 = string.substring(prod_typel, prod_namel);
			String prod_type = substring1.substring(11,substring1.length()-2);	
			
			
			WebParam<TelecomGuangxiCall> webParam = new WebParam<TelecomGuangxiCall>();
			TelecomGuangxiCall telecomGuangxiCall = null;
			String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp";
		    WebRequest webRequestpack1 = new WebRequest(new URL(url), HttpMethod.POST);
			String reString ="PRODTYPE="+prod_type+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+prod_type+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1031&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
			webRequestpack1.setRequestBody(reString);
				
			HtmlPage page = webClient.getPage(webRequestpack1);
			Thread.sleep(2000);
			webParam.setUrl(url);
			if(null != page)
			{
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				int statusCode = page.getWebResponse().getStatusCode();
				if(statusCode==200)
				{
					webParam.setCode(statusCode);
	//				System.out.println("++++++++++++++++++++"+page.getWebResponse().getContentAsString());
					tracer.addTag("getCALL.content", page.getWebResponse().getContentAsString());
					HtmlTable object = page.getFirstByXPath("//table[@id='list_table']");
					//System.out.println(object.asXml());
					
					if(page.getWebResponse().getContentAsString().contains("无记录"))
					{
						return webParam;
					}
					else if(object != null)
					{
						List<TelecomGuangxiCall> list = new ArrayList<TelecomGuangxiCall>();
						Document doc = Jsoup.parse(object.asXml());
						Elements elements = doc.getElementsByTag("tr");
						for (int i = 3; i < elements.size()-2; i++) {
							telecomGuangxiCall = new TelecomGuangxiCall();
							Elements elementsByTag = elements.get(i).select("td");
							telecomGuangxiCall.setStartTime(elementsByTag.get(1).text());
							telecomGuangxiCall.setAddr(elementsByTag.get(2).text());
							telecomGuangxiCall.setCallType(elementsByTag.get(3).text());
							telecomGuangxiCall.setHisNum(elementsByTag.get(4).text());
							telecomGuangxiCall.setCallTime(elementsByTag.get(5).text());
							telecomGuangxiCall.setCallStatus(elementsByTag.get(6).text());
							telecomGuangxiCall.setMoney(elementsByTag.get(7).text());
							telecomGuangxiCall.setTaskid(messageLogin.getTask_id());
							list.add(telecomGuangxiCall);
						}
						webParam.setList(list);
						return webParam;
					}
				}
			}
		}
		return null;
		
	}

	//短信详单
	public WebParam<TelecomGuangxiMessage> getSMS(MessageLogin messageLogin, TaskMobile taskMobile,int a) throws Exception {
		
		tracer.addTag("TelecomGuangxiParser.getSMS.start" , taskMobile.getTaskid());
		String dateBefore = getDateBefore("yyyyMM", a);
		WebClient webClient = addcookie(taskMobile);
		String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 

		//找到prod_type
		String url1 ="http://gx.189.cn/chaxun/iframe/qdcx_new_div.jsp?SERV_NO=FCX-4";
		Page page2 = webClient.getPage(url1);
		WebParam<TelecomGuangxiMessage> webParam = new WebParam<TelecomGuangxiMessage>();
//		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("qry_num"))
		{
			tracer.addTag("TelecomGuangxiParser.getSMS" ,page2.getWebResponse().getContentAsString());
			Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
			Element elementById = doc1.getElementById("qry_num");
//			System.out.println(elementById);
			String string = elementById.toString();
			int area_codel = string.indexOf("area_code");
			int prod_typel = string.indexOf("prod_type");
			int prod_namel = string.indexOf("prod_name");
			String substring = string.substring(area_codel, prod_typel);
			String area_code = substring.substring(11,substring.length()-2);//城市代码
			String substring1 = string.substring(prod_typel, prod_namel);
			String prod_type = substring1.substring(11,substring1.length()-2);	
			
			String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?";
		    WebRequest webRequestpack1 = new WebRequest(new URL(url), HttpMethod.POST);
			String reString ="PRODTYPE="+prod_type+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+prod_type+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1032&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
			webRequestpack1.setRequestBody(reString);
		
			webParam.setUrl(url);
			TelecomGuangxiMessage telecomGuangxiMessage = null;
			HtmlPage page = webClient.getPage(webRequestpack1);
			webParam.setPage(page);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			tracer.addTag("getSMS.crawler", page.getWebResponse().getContentAsString());
			if(null != page)
			{
				int statusCode = page.getWebResponse().getStatusCode();
//				System.out.println("------------===================="+page.getWebResponse().getContentAsString());
				if(statusCode==200 && page.getWebResponse().getContentAsString().contains("list_table"))
				{
					tracer.addTag("getSMS.content", page.getWebResponse().getContentAsString());
					webParam.setCode(statusCode);
					//String url1="http://gx.189.cn/service/bill/getRand.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode=1100&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1030&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD=&CUST_NAME=&CARD_TYPE=1&CARD_NO=";
					HtmlTable object = page.getFirstByXPath("//table[@id='list_table']");
					if(null != object.asXml())
					{
						List<TelecomGuangxiMessage> list = new ArrayList<TelecomGuangxiMessage>();
						Document doc = Jsoup.parse(object.asXml());
						if(page.getWebResponse().getContentAsString().contains("无记录"))
						{
							webParam.setHtml(page.getWebResponse().getContentAsString());
							return webParam;
						}
						else if(doc !=null)
						{
							Elements elements = doc.getElementsByTag("tr");
							for (int i = 2; i < elements.size()-1; i++) {
								telecomGuangxiMessage = new TelecomGuangxiMessage();
								Elements elementsByTag = elements.get(i).getElementsByTag("td");
								telecomGuangxiMessage.setNameType(elementsByTag.get(1).text());
								telecomGuangxiMessage.setGetType(elementsByTag.get(2).text());
								telecomGuangxiMessage.setMyNum(elementsByTag.get(3).text());
								telecomGuangxiMessage.setHisNum(elementsByTag.get(4).text());
								telecomGuangxiMessage.setStartTime(elementsByTag.get(5).text());
								telecomGuangxiMessage.setAboveMoney(elementsByTag.get(6).text());
								telecomGuangxiMessage.setBehindMoney(elementsByTag.get(7).text());
								telecomGuangxiMessage.setUseTime(elementsByTag.get(8).text());
								telecomGuangxiMessage.setTaskid(messageLogin.getTask_id());
								list.add(telecomGuangxiMessage);
							}
							webParam.setList(list);
							return webParam;
						}
					}
					
				}
			}
		}
		return null;
	}

	// 业务
	public WebParam<TelecomGuangxiBusiness> getBusiness(MessageLogin messageLogin, TaskMobile taskMobile)
			throws Exception {
		tracer.addTag("TelecomGuangxiParser.getBusiness" , taskMobile.getTaskid());
		WebClient webClient = addcookie(taskMobile);
		WebParam<TelecomGuangxiBusiness> webParam = new WebParam<TelecomGuangxiBusiness>();
//		 不带数据业务
		 String url4="http://gx.189.cn/chaxun/iframe/zyywcx_new_div.jsp";
//		// 数据业务
//		String url4 = "http://gx.189.cn/chaxun/iframe/actioncenter.jsp?SERV_NO=FCX-9&QRY_NUM="+messageLogin.getName()+"&QRY_AREA_CODE="+taskMobile.getTrianNum()+"&ACCNBR="+messageLogin.getName()+"&BUREAUCODE="+taskMobile.getTrianNum()+"&QryType=1&QryPara=serv_id&BureauCode="+taskMobile.getTrianNum()+"&AccNbr="+messageLogin.getName()+"&PROD_TYPE=2100297&PROD_CODE=2100297&PROD_NO="+messageLogin.getName()+"&USER_ID_TYPE=1";
		Page page = getPage(webClient, taskMobile, url4, null);
		TelecomGuangxiBusiness t = null;
		List<TelecomGuangxiBusiness> a = new ArrayList<TelecomGuangxiBusiness>();
		if (null != page) {
			int code = page.getWebResponse().getStatusCode();
			if (code == 200) {
				// Page page2 = webClient.getPage(url4);
				JSONObject jsonBusiness = JSONObject.fromObject(page.getWebResponse().getContentAsString());
				String string2 = jsonBusiness.getString("RETSPLIST");
				if(string2 != null)
				{
					JSONArray arrayBs = JSONArray.fromObject(string2);
					for (int j = 0; j < arrayBs.size(); j++) {
						JSONObject fromObjectBs = JSONObject.fromObject(arrayBs.get(j) + "");
						t = new TelecomGuangxiBusiness();
						t.setName(fromObjectBs.getString("SP_NAME"));// 业务名称
						t.setStartTime(fromObjectBs.getString("SP_EFF_DATE"));// 生效时间
						// t.setOutTime("");//失效时间
						a.add(t);
					}
				}
				

				String string1 = jsonBusiness.getString("SERVPRODQRYLIST");
				if(string1 != null)
				{
					JSONArray jsonArray = JSONArray.fromObject(string1);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i) + "");
						t = new TelecomGuangxiBusiness();
						t.setName(jsonObject.getString("ProdName"));
						a.add(t);
					}
				}
			}
		}

		// 套餐
		String url5 = "http://gx.189.cn/chaxun/iframe/actioncenter.jsp?SERV_NO=FCX-10&QRY_NUM="+messageLogin.getName()+"&QRY_AREA_CODE="+taskMobile.getTrianNum()+"&BUREAUCODE="+taskMobile.getTrianNum()+"&ACCNBR="+messageLogin.getName()+"&PROD_TYPE=2100297";
		tracer.addTag("TelecomGuangxiParser.getBusiness.actioncenter" , taskMobile.getTaskid());
		Page page3 = webClient.getPage(url5);
		if (null != page3) {
			int statusCode = page3.getWebResponse().getStatusCode();
			if (statusCode == 200) {
				JSONObject jsonObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
				String string = jsonObject.getString("OFFERLIST");
				if(string != null)
				{
					JSONArray array = JSONArray.fromObject(string);
					for (int i = 0; i < array.size(); i++) {
						JSONObject jsonObject2 = JSONObject.fromObject(array.get(i) + "");
						t = new TelecomGuangxiBusiness();
						t.setPackageTime(jsonObject2.getString("OfferName"));
						t.setFavourable(jsonObject2.getString("EffDate"));
						t.setIntroduce(jsonObject2.getString("Notes"));
						t.setBuyTime(jsonObject2.getString("ExpDate"));
						t.setTaskid(messageLogin.getTask_id());
						a.add(t);
					}
				}
			}
		}
		webParam.setHtml(page.getWebResponse().getContentAsString());
		webParam.setUrl(url4);
		webParam.setTelecomGuangxiBusiness(t);
		webParam.setList(a);
		return webParam;
	}

	// 个人信息
	public WebParam<TelecomGuangxiUserInfo> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile)
			throws Exception {
		tracer.addTag("TelecomGuangxiParser.getUserInfo" , taskMobile.getTaskid());
		String url = "http://gx.189.cn/chaxun/iframe/hfcx_new_div.jsp?SERV_NO=FCX-2";
		WebClient webClient = addcookieFirst(taskMobile);
		TelecomGuangxiUserInfo telecomGuangxiUserInfo = null;
		WebParam<TelecomGuangxiUserInfo> webParam = new WebParam<TelecomGuangxiUserInfo>();
		HtmlPage page = webClient.getPage(url);
//		System.out.println("---------"+page.getWebResponse().getContentAsString());
		if(null != page)
		{
			int i = page.getWebResponse().getStatusCode();
			if(i==200)
			{
				String element = page.getElementById("balance_hfcx2").asText();
				if(element != null)
				{
					//System.out.println("---------"+element);
				    telecomGuangxiUserInfo = new TelecomGuangxiUserInfo();
					telecomGuangxiUserInfo.setBalance(element);
				}
			}
		}
		String url1 = "http://gx.189.cn/service/manage/contactslist.jsp?RC=";
//		WebClient webClient = addcookie(taskMobile);
//		WebParam<TelecomGuangxiUserInfo> webParam = new WebParam<TelecomGuangxiUserInfo>();
		HtmlPage page1 = webClient.getPage(url1);
		//System.out.println("----------------============"+page1.getWebResponse().getContentAsString());
		if (null != page1) {
			int i = page1.getWebResponse().getStatusCode();
			if (i == 200 && page1.getWebResponse().getContentAsString().contains("contact_json_div")) {
				String element2 = page1.getElementById("contact_json_div").asText();
				if(null != element2)
				{
					String replace = element2.replaceAll("&#034", "\"\"");
					String replaceAll = replace.replaceAll(";", "");
					//System.out.println(replaceAll);
					JSONObject jsonObject1 = JSONObject.fromObject(replaceAll);

					String string2 = jsonObject1.getString("CUST_CONTACT_LIST_JSON");
					//System.out.println(string2);
					String substring = string2.substring(1, string2.length() - 1);
					
					JSONObject jsonObject = JSONObject.fromObject(substring);
					//System.out.println(jsonObject);
					telecomGuangxiUserInfo.setContactAdderss(jsonObject.getString("ContactAdderss"));
					telecomGuangxiUserInfo.setContactEmployer(jsonObject.getString("ContactEmployer"));
					telecomGuangxiUserInfo.setContactName(jsonObject.getString("ContactName"));
					telecomGuangxiUserInfo.setEasyLetter(jsonObject.getString("EasyLetter"));
					telecomGuangxiUserInfo.setEmail(jsonObject.getString("Email"));
					telecomGuangxiUserInfo.setFax(jsonObject.getString("Fax"));
					telecomGuangxiUserInfo.setHomePhone(jsonObject.getString("HomePhone"));
					telecomGuangxiUserInfo.setMicroLetter(jsonObject.getString("MicroLetter"));
					telecomGuangxiUserInfo.setWeiBo(jsonObject.getString("WeiBo"));
					telecomGuangxiUserInfo.setQQNo(jsonObject.getString("QQNo"));
					telecomGuangxiUserInfo.setPostCode(jsonObject.getString("PostCode"));
					telecomGuangxiUserInfo.setPontactAdderss(jsonObject.getString("PontactAdderss"));
					telecomGuangxiUserInfo.setPhoneNo(jsonObject.getString("PhoneNo"));
					telecomGuangxiUserInfo.setOfficePhone(jsonObject.getString("OfficePhone"));
					telecomGuangxiUserInfo.setTaskid(messageLogin.getTask_id());
					webParam.setCode(i);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					webParam.setPage(page);
					webParam.setUrl(url1);
					webParam.setTelecomGuangxiUserInfo(telecomGuangxiUserInfo);
				}
			}
		}
		return webParam;
	}

	// 缴费
	public WebParam<TelecomGuangxiPay> getPay(MessageLogin messageLogin, TaskMobile taskMobile,int a ) throws Exception {
		Thread.sleep(2000);
		tracer.addTag("TelecomGuangxiParser.getPay" , taskMobile.getTaskid());

		String url1 ="http://gx.189.cn/chaxun/iframe/myserv_payList.jsp?REFRESH_FLAG=2&IPAGE_INDEX=4&AccNbr=&OPEN_TYPE=&Logon_Name=&USER_FLAG=001&USE_PROTOCOL=&LOGIN_TYPE=&USER_NO=&ESFlag=8&REDIRECT_URL=%2Fchaxun%2F%3FSERV_NO%3DFCX-1&AccNbr11="+messageLogin.getName()+"&START_ASK_DATE="+getFirstDay(a)+"&END_ASK_DATE="+getLastDay(a);
		WebClient webClient = addcookie(taskMobile);
		WebParam<TelecomGuangxiPay> webParam = new WebParam<TelecomGuangxiPay>();
		HtmlPage page = webClient.getPage(url1);
		Thread.sleep(3000);
		TelecomGuangxiPay telecomGuangxiPay = null;
		List<TelecomGuangxiPay> list = new ArrayList<TelecomGuangxiPay>();
		if(null != page)
		{
			int i = page.getWebResponse().getStatusCode();
			if(i==200)
			{
				String string = page.getWebResponse().getContentAsString();
				//System.out.println("-===================-------------------------------"+string);
				int j = string.indexOf("var dataJson");
				int k = string.indexOf("空格");
				if(k-j>=276)
				{
					String string3 = string.substring(j+16, k-12);
					if(null !=string3)
					{
						String mytext2 = URLDecoder.decode(string3,"utf-8").trim();
//						System.out.println(mytext2);  
//						JSONObject fromObject = JSONObject.fromObject(mytext2.substring(0, mytext2.length()-2));
						JSONObject fromObject = JSONObject.fromObject(mytext2);
//						System.out.println(fromObject);
						String json = fromObject.getString("PaymentList");
						String string2 = json.substring(1, json.length()-1);
						JSONObject fromObject2 = JSONObject.fromObject(string2);
						String num = fromObject2.getString("AccNbr");
						String type = fromObject2.getString("PaySource");
						String datea = fromObject2.getString("PayDate");
						String money = fromObject2.getString("PayCharge");
						telecomGuangxiPay = new TelecomGuangxiPay();
						telecomGuangxiPay.setDatea(datea);
						telecomGuangxiPay.setMoney(money);
						telecomGuangxiPay.setType(type);
						telecomGuangxiPay.setNum(num);
						telecomGuangxiPay.setTaskid(messageLogin.getTask_id());
						list.add(telecomGuangxiPay);
						webParam.setCode(i);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setTelecomGuangxiPay(telecomGuangxiPay);
						webParam.setUrl(url1);
						webParam.setList(list);
						return webParam;
					}
				}
			}
		}
		return null;
	}

	// 账单
	public WebParam<TelecomGuangxiBill> getBill(MessageLogin messageLogin, TaskMobile taskMobile,int a) throws Exception {
		tracer.addTag("TelecomGuangxiParser.getBill" , taskMobile.getTaskid());

		String dateBefore = getDateBefore("yyyyMM", a);
		WebClient webClient = addcookieFirst(taskMobile);
		WebParam<TelecomGuangxiBill> webParam = new WebParam<TelecomGuangxiBill>();
		String url1="http://gx.189.cn/chaxun/iframe/cust_zd.jsp?ACC_NBR="+messageLogin.getName()+"&DATE="+dateBefore+"&_="+System.currentTimeMillis()+"?ACC_NBR="+messageLogin.getName()+"&DATE="+dateBefore+"&_="+System.currentTimeMillis();
		HtmlPage page2 = webClient.getPage(url1);
		//System.out.println(page2.getWebResponse().getContentAsString());
		TelecomGuangxiBill telecomGuangxiBill = null;
		if(null != page2)
		{
			int code = page2.getWebResponse().getStatusCode();
			if(code==200)
			{ 
				Document doc = Jsoup.parse(page2.getWebResponse().getContentAsString());
				//System.out.println("=============---------------------+++++++++++++"+doc.text());
				if(doc.text().contains("本项小计"))
				{
				//	System.out.println(doc.getElementsByClass("divC").get(1).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr"));
					int size = doc.getElementsByClass("divC").get(1).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").size();
					if(size==6 | size==4 | size==8)
					{
						Elements class1 = doc.getElementById("divFee").getElementsByClass("divC").get(0).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
						String mealMoney = getNextLabelByKeywordTwo(class1,"套餐月基本费", "div");
						String sumMoney = class1.get(0).getElementsByTag("span").text();
						
						Elements eles1 = class1.select("[style= float:left; text-align: left;]");
						List<TelecomGuangxiBill> list = new ArrayList<TelecomGuangxiBill>();
						telecomGuangxiBill = new TelecomGuangxiBill();
						for (Element element : eles1) {
							String serviceName = element.text().trim().replace(" ", "");
							Element nextElement = element.nextElementSibling();
							String fee = "";
							
							telecomGuangxiBill.setMealMoney(mealMoney);
							telecomGuangxiBill.setSumMoney(sumMoney);
							if (null != nextElement) {
								fee = nextElement.text();
							}
							
							if(serviceName.contains("国内短信费"))
							{
								telecomGuangxiBill.setMessageMoney(fee);
							}
							else if(serviceName.contains("优惠费用"))
							{
								telecomGuangxiBill.setFavourable(fee);
							}
							else if(serviceName.contains("国内通话费"))
							{
								telecomGuangxiBill.setLandMoney(fee);
							}
							else if(serviceName.contains("4G包月流量包费"))
							{
								telecomGuangxiBill.setFourMoney(fee);
							}
							else if(serviceName.contains("无线宽带费"))
							{
								telecomGuangxiBill.setNetMoney(fee);
							}
							else if(serviceName.contains("红包返还"))
							{
								telecomGuangxiBill.setRedMoney(fee);
							}
							telecomGuangxiBill.setMonth(dateBefore);
							telecomGuangxiBill.setTaskid(taskMobile.getTaskid());
							list.add(telecomGuangxiBill);
						}
						webParam.setCode(code);
						webParam.setHtml(page2.getWebResponse().getContentAsString());
						webParam.setList(list);
						webParam.setPage(page2);
						webParam.setUrl(url1);
						return webParam;
					}
//					else if(doc.getElementsByClass("divC").get(1).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").size()==8)
//					{
//						Elements class1 = doc.getElementById("divFee").getElementsByClass("divC").get(0).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
//						String mealMoney = getNextLabelByKeywordTwo(class1,"套餐月基本费", "div");
//						String sumMoney = class1.get(0).getElementsByTag("span").text();
//						
//						Elements eles1 = class1.select("[style= float:left; text-align: left;]");
//						List<TelecomGuangxiBill> list = new ArrayList<TelecomGuangxiBill>();
//						telecomGuangxiBill = new TelecomGuangxiBill();
//						for (Element element : eles1) {
//							String serviceName = element.text().trim().replace(" ", "");
//							Element nextElement = element.nextElementSibling();
//							String fee = "";
//							
//							telecomGuangxiBill.setMealMoney(mealMoney);
//							telecomGuangxiBill.setSumMoney(sumMoney);
//							if (null != nextElement) {
//								fee = nextElement.text();
//							}
//							
//							if(serviceName.contains("国内短信费"))
//							{
//								telecomGuangxiBill.setMessageMoney(fee);
//							}
//							else if(serviceName.contains("优惠费用"))
//							{
//								telecomGuangxiBill.setFavourable(fee);
//							}
//							else if(serviceName.contains("国内通话费"))
//							{
//								telecomGuangxiBill.setLandMoney(fee);
//							}
//							else if(serviceName.contains("4G包月流量包费"))
//							{
//								telecomGuangxiBill.setFourMoney(fee);
//							}
//							else if(serviceName.contains("无线宽带费"))
//							{
//								telecomGuangxiBill.setNetMoney(fee);
//							}
//							telecomGuangxiBill.setMonth(dateBefore);
//							telecomGuangxiBill.setTaskid(messageLogin.getTask_id());
//							list.add(telecomGuangxiBill);
//						}
//						webParam.setCode(code);
//						webParam.setHtml(page2.getWebResponse().getContentAsString());
//						webParam.setList(list);
//						webParam.setPage(page2);
//						webParam.setUrl(url1);
//						return webParam;
//					}
				}
					
			}
				
		}	
		return null;
	}

//	// 余额
//	public WebParam<TelecomGuangxiUserInfo> getBalance(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
//		String url = "http://gx.189.cn/chaxun/iframe/hfcx_new_div.jsp?SERV_NO=FCX-2";
//		WebClient webClient = addcookie(taskMobile);
//		WebParam<TelecomGuangxiUserInfo> webParam = new WebParam<TelecomGuangxiUserInfo>();
//		HtmlPage page = webClient.getPage(url);
//		if(null != page)
//		{
//			int i = page.getWebResponse().getStatusCode();
//			if(i==200)
//			{
//				String element = page.getElementById("balance_hfcx2").asText();
//				if(element != null)
//				{
//					System.out.println("---------"+element);
//					TelecomGuangxiUserInfo telecomGuangxiUserInfo = new TelecomGuangxiUserInfo();
//					telecomGuangxiUserInfo.setBalance(element);
//					webParam.setTelecomGuangxiUserInfo(telecomGuangxiUserInfo);
//					webParam.setHtml(page.getWebResponse().getContentAsString());
//					webParam.setUrl(url);
//					webParam.setCode(i);
//					webParam.setPage(page);
//					return webParam;
//				}
//			}
//		}
//		return null;
//	}

	// 积分
	public WebParam<TelecomGuangxiScore> getScore(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomGuangxiParser.getScore" , taskMobile.getTaskid());
		String url1 = "http://gx.189.cn/chaxun/iframe/kyjfcx_new_div.jsp";
//		String url1="http://gx.189.cn/chaxun/iframe/actioncenter.jsp?SERV_NO=FCX-22&QRY_NUM="+messageLogin.getName()+"&QRY_AREA_CODE="+taskMobile.getTrianNum()+"&ListFlag=2&AccNbr=42005545305&BureauCode="+taskMobile.getTrianNum()+"&SearchMonth=&AcctMonth=";
		WebClient webClient = addcookie(taskMobile);
		WebParam<TelecomGuangxiScore> webParam = new WebParam<TelecomGuangxiScore>();
		
		Page page = webClient.getPage(url1);
		tracer.addTag("TelecomGuangxiParser.getpage", taskMobile.getTaskid());
		
		List<TelecomGuangxiScore> list = new ArrayList<TelecomGuangxiScore>();
		if(null != page)
		{
			tracer.addTag("TelecomGuangxiParser.pageNotNull", taskMobile.getTaskid());
			webParam.setUrl(url1);
			int code = page.getWebResponse().getStatusCode();
			String string = page.getWebResponse().getContentAsString();
			webParam.setHtml(string);
			if(code==200)
			{
				webParam.setCode(code);
				if(string.contains("BUNUSLIST"))
				{
					tracer.addTag("TelecomGuangxiParser.json", taskMobile.getTaskid());
					JSONObject fromObject = JSONObject.fromObject(string);
					String string2 = fromObject.getString("BUNUSLIST");
					if(null != string2)
					{
						//String string3 = string2.substring(1, string2.length()-1);
						//System.out.println(string3);
						JSONArray fromObject2 = JSONArray.fromObject(string2);
						for (int i = 0; i < fromObject2.size(); i++) {
							JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
							String increaseScore = fromObject3.getString("PlusBonusIntegral");
							String sumScore = fromObject3.getString("TotalIntegral");
							String month = fromObject3.getString("BunusMonth");
							String giveScore = fromObject3.getString("RewardIntegral");
							String useScore = fromObject3.getString("ConsumeIntegral");
							String sendScore = fromObject3.getString("GivenIntegral");
							String netScore = fromObject3.getString("OnlineIntegral");
							TelecomGuangxiScore t = new TelecomGuangxiScore();
							t.setGiveScore(giveScore);
							t.setIncreaseScore(increaseScore);
							t.setMonth(month);
							t.setNetScore(netScore);
							t.setSendScore(sendScore);
							t.setSumScore(sumScore);
							t.setUseScore(useScore);
							t.setTaskid(messageLogin.getTask_id());
							list.add(t);
						}
						webParam.setList(list);
					}
				}
			}
		}
		return webParam;
	}

	// 登陆
	public WebParam loginlogin(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setTimeout(200000); 
		//通过如下连接获取参数
		String url="http://gx.189.cn/chaxun/iframe/user_center.jsp";
		WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.getOptions().setJavaScriptEnabled(false);
		WebParam webParam = new WebParam();
		HtmlPage hPage = webClient.getPage(webRequest);
		if(null!=hPage){
			String html=hPage.asXml();
			Document doc = Jsoup.parse(html);
			String codeId = doc.getElementById("IDNum").text();
			String key1=html.substring(html.indexOf("key1=")+6, html.indexOf("key1=")+12);
			String key2=html.substring(html.indexOf("key2=")+6, html.indexOf("key2=")+12);
			String key3=html.substring(html.indexOf("key3=")+6, html.indexOf("key3=")+12);
			//加密登陆密码
			String logon_passwd = strEnc(messageLogin.getPassword(),key1,key2,key3);
		
			url="http://gx.189.cn/public/login.jsp";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="LOGIN_TYPE=21&RAND_TYPE=001"
					+ "&codeId="+codeId+""
					+ "&AREA_CODE="
					+ "&logon_name="+messageLogin.getName()
					+ "&password_type_ra=1"
					+ "&logon_passwd=__"+logon_passwd+""
					+ "&logon_valid=%E8%AF%B7%E8%BE%93%E5%85%A5%E9%AA%8C%E8%AF%81%E7%A0%81";
			webRequest.setRequestBody(requestBody);
			Page page=webClient.getPage(webRequest);
			
			if(null!=page){
				html=page.getWebResponse().getContentAsString();
//				System.out.println("验证登陆信息返回的页面是："+html);
				String[] split = html.split("<AREA_CODE>");
				String[] split2 = split[1].split("<");
//				System.out.println("CAONIMAXE ---"+split2[0]);
				
				Page page2 = webClient.getPage("http://gx.189.cn/chaxun/iframe/user_center.jsp");
//				System.out.println("验证登陆信息返回的页面是："+page2.getWebResponse().getContentAsString());
				String string = page2.getWebResponse().getContentAsString();
				if(string.contains("PRODTYPE"))
				{
					int indexOf = string.indexOf("PRODTYPE");
					String substring = string.substring(indexOf);
					String substring2 = substring.substring(10,17);
//					System.out.println(substring2);
					webParam.setHtml(html);
					webParam.setUrl(split2[0]);
					webParam.setWebClient(webClient);
					webParam.setProdType(substring2);
				}
			}
		}
		return webParam;
	}
	
	public String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
	public String strEnc(String str,String key1,String key2,String key3) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("des.js", Charsets.UTF_8);
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction("strEnc",str,key1,key2,key3);
		return data.toString(); 
	}
	
	public String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }

	// 登陆
	public WebParam login(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomGuangxiParser.login" , taskMobile.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		// 正常页面登陆
//		String url = "http://login.189.cn/web/login";
//		HtmlPage html = getHtml(url, webClient);
//		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");// logon_name
//		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");// logon_passwd
//		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
//
//		HtmlImage imgCaptcha = (HtmlImage)html.getFirstByXPath("//img[@id='imgCaptcha']");//verify_key
//		
//		username.setText(messageLogin.getName());
//		passwordInput.setText(messageLogin.getPassword());
//		taskMobile.setTesthtml(messageLogin.getPassword());
//		String[] split = imgCaptcha.asXml().split("src");
//		String[] split2 = split[1].split("id");
//		String substring = split2[0].substring(1);
//		if(substring.length() != 3)
//		{
//			HtmlTextInput txtCaptcha = (HtmlTextInput)html.getFirstByXPath("//input[@id='txtCaptcha']");//logon_valid
//			String code = chaoJiYingOcrService.getVerifycode(imgCaptcha,"1902");
//			txtCaptcha.setText(code);
//		}
//		
//		
//		
//		
//		HtmlPage htmlpage2 = button.click();
//		// System.out.println("---------------------"+htmlpage2.getWebResponse().getContentAsString());
//		if (htmlpage2.asXml().indexOf("登录失败") != -1) {
//			System.out.println("=======失败==============");
//			return null;
//		}
//		webClient = htmlpage2.getWebClient();
//
//		// 第一次登陆进去的界面
//		String url33 = "http://www.189.cn/gx/";
//		HtmlPage page = webClient.getPage(url33);
//		webClient = page.getWebClient();
//		System.out.println("---------------===================="+page.getWebResponse().getContentAsString());
		// 登陆广西
		String url3 = "http://gx.189.cn/chaxun/iframe/user_center.jsp";
		HtmlPage page3 = webClient.getPage(url3);
//		System.out.println(page3.getWebResponse().getContentAsString());
		HtmlTextInput username1 = (HtmlTextInput) page3.getFirstByXPath("//input[@id='logon_name']");// logon_name
		HtmlPasswordInput passwordInput1 = (HtmlPasswordInput) page3.getFirstByXPath("//input[@id='logon_passwd']");// logon_passwd
		username1.setText(messageLogin.getName());
		passwordInput1.setText(messageLogin.getPassword());
		taskMobileRepository.save(taskMobile);
		HtmlAnchor firstByXPath = page3.getFirstByXPath("//*[@id='login_form']/div/div/div[4]/a[1]");
		HtmlImage imgCaptcha = (HtmlImage)page3.getFirstByXPath("//img[@id='verify_key']");//verify_key
		String[] split = imgCaptcha.asXml().split("src");
		String[] split2 = split[1].split("id");
		String substring = split2[0].substring(1);
		if(substring.length() != 3)
		{
			HtmlTextInput txtCaptcha = (HtmlTextInput)page3.getFirstByXPath("//input[@id='logon_valid']");//logon_valid
			String code = chaoJiYingOcrService.getVerifycode(imgCaptcha,"1902");
			txtCaptcha.setText(code);
		}
		HtmlPage page4 = firstByXPath.click();
		Thread.sleep(5000);
		String alertMsg = WebCrawler.getAlertMsg();
//		System.out.println(alertMsg);
		WebParam webParam = new WebParam();
		webParam.setHtml(alertMsg);
//		System.out.println(page4.getWebResponse().getContentAsString());
		webParam.setPage(page4);
		
		if(taskMobile.getCity().equals("南宁"))
		{
			taskMobile.setTrianNum(1100);
		}else if(taskMobile.getCity().equals("崇左"))
		{
			taskMobile.setTrianNum(2100);
		}
		else if(taskMobile.getCity().equals("柳州"))
		{
			taskMobile.setTrianNum(1200);
		}
		else if(taskMobile.getCity().equals("来宾"))
		{
			taskMobile.setTrianNum(2200);
		}
		else if(taskMobile.getCity().equals("桂林"))
		{
			taskMobile.setTrianNum(1300);
		}
		else if(taskMobile.getCity().equals("梧州"))
		{
			taskMobile.setTrianNum(1400);
		}
		else if(taskMobile.getCity().equals("贺州"))
		{
			taskMobile.setTrianNum(2100);
		}
		else if(taskMobile.getCity().equals("玉林"))
		{
			taskMobile.setTrianNum(1500);
		}
		else if(taskMobile.getCity().equals("贵港"))
		{
			taskMobile.setTrianNum(2500);
		}
		else if(taskMobile.getCity().equals("百色"))
		{
			taskMobile.setTrianNum(1600);
		}
		else if(taskMobile.getCity().equals("钦州"))
		{
			taskMobile.setTrianNum(1700);
		}
		else if(taskMobile.getCity().equals("河池"))
		{
			taskMobile.setTrianNum(1800);
		}
		else if(taskMobile.getCity().equals("北海"))
		{
			taskMobile.setTrianNum(1900);
		}
		else if(taskMobile.getCity().equals("防城港"))
		{
			taskMobile.setTrianNum(2000);
		}
//		
//		
//		if (alertMsg.indexOf("连续输错") != -1) {
//
//			HtmlImage valiCodeImg = page4.getFirstByXPath("//*[@id='verify_key']");
//			if ((valiCodeImg.isDisplayed())) {
//				System.out.println("Element is  displayed!");
//				tracer.addTag("电信登录 验证码", "<xmp>" + "Element is  displayed!" + "</xmp>");
//				String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1902");
//				tracer.addTag("电信登录 验证码内容", "<xmp>" + valicodeStr + "</xmp>");
//				page4 = loginByImage(page4, messageLogin);
//				if (page4.asXml().indexOf("验证码不正确") != -1) {
//					System.out.println("===========验证码不正确 ，再次验证================");
//
//					page4 = loginByImage(page4, messageLogin);
//					if (page4.asXml().indexOf("验证码不正确") != -1) {
//						page4 = loginByImage(page4, messageLogin);
//					}
//
//				}
//				
//			}
//		}
		return webParam;
	}
	
	private HtmlPage loginByImage(HtmlPage html, MessageLogin messageLogin) throws Exception {
		HtmlImage valiCodeImg = html.getFirstByXPath("//*[@id='verify_key']");
//		System.out.println("Element is  displayed!");
		tracer.addTag("电信登录 验证码", "<xmp>" + "Element is  displayed!" + "</xmp>");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");
		tracer.addTag("电信登录 验证码内容", "<xmp>" + valicodeStr + "</xmp>");

		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='logon_name']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='logon_passwd']");
		HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("//*[@id='logon_valid']");

		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='dl']");

		username.setText(messageLogin.getName().trim());
		passwordInput.setText(messageLogin.getPassword().trim());
		valicodeStrinput.setText(valicodeStr.toLowerCase().trim());

		System.out.println("==========" + username);
		System.out.println("==========" + passwordInput);
		System.out.println("==========" + valicodeStrinput);

		HtmlPage htmlpage2 = button.click();
		System.out.println("登录" + htmlpage2.asXml());
		return htmlpage2;
	}
	

	// 通过URL获得HTMLpage
	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeyword(Document document, String keyword, String tag) {
		Elements es = document.select(tag+":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag+":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	
	/**
	 * 获取当月第一天和最后一天
	 */
	public String getFirstDay(int m)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		//获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, -m);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = format.format(c.getTime());
        //System.out.println("===============first:"+first);
		return first;
	}
	
	/**
	 * 获取最后一天
	 */
	public String getLastDay(int m)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		//获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.add(Calendar.MONTH, -m);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = format.format(ca.getTime());
        //System.out.println("===============last:"+last);
		return last;
	}

	//国际电话信息
	public WebParam<TelecomGuangxiCall> getNationalCall(MessageLogin messageLogin, TaskMobile taskMobile, int a)throws Exception  {
		Thread.sleep(1000);
		tracer.addTag("TelecomGuangxiParser.getNationalCall" , taskMobile.getTaskid());
        String dateBefore = getDateBefore("yyyyMM", a);
//		String dateBefore ="201710";
        WebClient webClient = addcookie(taskMobile);
        
    	//找到prod_type
		String url1 ="http://gx.189.cn/chaxun/iframe/qdcx_new_div.jsp?SERV_NO=FCX-4";
		Page page2 = webClient.getPage(url1);
		if(page2.getWebResponse().getContentAsString().contains("qry_num"))
		{
			Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
			Element elementById = doc1.getElementById("qry_num");
//			System.out.println(elementById);
			String string = elementById.toString();
			int area_codel = string.indexOf("area_code");
			int prod_typel = string.indexOf("prod_type");
			int prod_namel = string.indexOf("prod_name");
			String substring = string.substring(area_codel, prod_typel);
			String area_code = substring.substring(11,substring.length()-2);//城市代码
			String substring1 = string.substring(prod_typel, prod_namel);
			String prod_type = substring1.substring(11,substring1.length()-2);
			String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 
//		    String url ="http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&BEGIN_DATE=&END_DATE=&REFRESH_FLAG=1&QRY_FLAG=1&FIND_TYPE=1038&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+dateBefore; 

		    String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp";
		    WebRequest webRequestpack1 = new WebRequest(new URL(url), HttpMethod.POST);
			String reString ="PRODTYPE="+prod_type+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+prod_type+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1035&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
			webRequestpack1.setRequestBody(reString);
			
			WebParam<TelecomGuangxiCall> webParam = new WebParam<TelecomGuangxiCall>();
			TelecomGuangxiCall telecomGuangxiCall = null;
			HtmlPage page = webClient.getPage(webRequestpack1);
			Thread.sleep(1000);
			if(null != page)
			{
				int statusCode = page.getWebResponse().getStatusCode();
				if(statusCode==200)
				{
//					System.out.println("++++++++++++++++++++++"+page.getWebResponse().getContentAsString());
					HtmlTable object = page.getFirstByXPath("//table[@id='list_table']");
					//System.out.println(object.asXml());
					if(page.getWebResponse().getContentAsString().contains("无记录"))
					{
						webParam.setHtml(page.getWebResponse().getContentAsString());
						return webParam;
					}
					else if(object != null)
					{
						List<TelecomGuangxiCall> list = new ArrayList<TelecomGuangxiCall>();
						Document doc = Jsoup.parse(object.asXml());
						Elements elements = doc.getElementsByTag("tr");
						for (int i = 3; i < elements.size()-2; i++) {
							telecomGuangxiCall = new TelecomGuangxiCall();
							Elements elementsByTag = elements.get(i).select("td");
							telecomGuangxiCall.setStartTime(elementsByTag.get(1).text());
							telecomGuangxiCall.setAddr(elementsByTag.get(2).text());
							telecomGuangxiCall.setCallType(elementsByTag.get(3).text());
							telecomGuangxiCall.setHisNum(elementsByTag.get(4).text());
							telecomGuangxiCall.setCallTime(elementsByTag.get(5).text());
							telecomGuangxiCall.setCallStatus(elementsByTag.get(6).text());
							telecomGuangxiCall.setMoney(elementsByTag.get(7).text());
							telecomGuangxiCall.setTaskid(messageLogin.getTask_id());
							list.add(telecomGuangxiCall);
						}
						webParam.setCode(statusCode);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setList(list);
						webParam.setPage(page);
						webParam.setUrl(url);
						return webParam;
					}
				}
			}
		}
		return null;
	}

	//本地打异地
	public WebParam<TelecomGuangxiCall> getLongCall(MessageLogin messageLogin, TaskMobile taskMobile, int a)throws Exception  {
		Thread.sleep(2000);
		tracer.addTag("TelecomGuangxiParser.getLongCall" , taskMobile.getTaskid());

        String dateBefore = getDateBefore("yyyyMM", a);
//		String dateBefore ="201710";
        WebClient webClient = addcookie(taskMobile);
        WebParam<TelecomGuangxiCall> webParam = new WebParam<TelecomGuangxiCall>();
		TelecomGuangxiCall telecomGuangxiCall = null;
//		找到prod_type
		String url1 ="http://gx.189.cn/chaxun/iframe/qdcx_new_div.jsp?SERV_NO=FCX-4";
		Page page2 = webClient.getPage(url1);
		if(page2.getWebResponse().getContentAsString().contains("qry_num"))
		{
			Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
			Element elementById = doc1.getElementById("qry_num");
//			System.out.println(elementById);
			String string = elementById.toString();
			int area_codel = string.indexOf("area_code");
			int prod_typel = string.indexOf("prod_type");
			int prod_namel = string.indexOf("prod_name");
			String substring = string.substring(area_codel, prod_typel);
			String area_code = substring.substring(11,substring.length()-2);//城市代码
			String substring1 = string.substring(prod_typel, prod_namel);
			String prod_type = substring1.substring(11,substring1.length()-2);
			String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 
//          String url ="http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&BEGIN_DATE=&END_DATE=&REFRESH_FLAG=1&QRY_FLAG=1&FIND_TYPE=1038&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+dateBefore; 
	
		    String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp";
		    WebRequest webRequestpack1 = new WebRequest(new URL(url), HttpMethod.POST);
			String reString ="PRODTYPE="+prod_type+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+prod_type+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1040&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
			webRequestpack1.setRequestBody(reString);
		
			HtmlPage page = webClient.getPage(webRequestpack1);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			Thread.sleep(1000);
			if(null != page)
			{
				int statusCode = page.getWebResponse().getStatusCode();
				if(statusCode==200)
				{
	//				System.out.println("++++++++++++++++++++++"+page.getWebResponse().getContentAsString());
					HtmlTable object = page.getFirstByXPath("//table[@id='list_table']");
					//System.out.println(object.asXml());
					if(page.getWebResponse().getContentAsString().contains("无记录"))
					{
						return webParam;
					}
					else if(object != null)
					{
						List<TelecomGuangxiCall> list = new ArrayList<TelecomGuangxiCall>();
						Document doc = Jsoup.parse(object.asXml());
						Elements elements = doc.getElementsByTag("tr");
						for (int i = 3; i < elements.size()-2; i++) {
							telecomGuangxiCall = new TelecomGuangxiCall();
							Elements elementsByTag = elements.get(i).select("td");
							telecomGuangxiCall.setStartTime(elementsByTag.get(1).text());
							telecomGuangxiCall.setAddr(elementsByTag.get(2).text());
							telecomGuangxiCall.setCallType(elementsByTag.get(3).text());
							telecomGuangxiCall.setHisNum(elementsByTag.get(4).text());
							telecomGuangxiCall.setCallTime(elementsByTag.get(5).text());
							telecomGuangxiCall.setCallStatus(elementsByTag.get(6).text());
							telecomGuangxiCall.setMoney(elementsByTag.get(7).text());
							telecomGuangxiCall.setTaskid(messageLogin.getTask_id());
							list.add(telecomGuangxiCall);
						}
						webParam.setCode(statusCode);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setList(list);
						webParam.setPage(page);
						webParam.setUrl(url);
					}
				}
			}
		}
		return webParam;
	}
	

	//异地通话记录
	public WebParam<TelecomGuangxiCall> getCityCall(MessageLogin messageLogin, TaskMobile taskMobile, int a)throws Exception  {
		Thread.sleep(2000);
		tracer.addTag("TelecomGuangxiParser.getCityCall" , taskMobile.getTaskid());

        String dateBefore = getDateBefore("yyyyMM", a);
//		String dateBefore ="201710";

        WebClient webClient = addcookie(taskMobile);
        WebParam<TelecomGuangxiCall> webParam = new WebParam<TelecomGuangxiCall>();
		TelecomGuangxiCall telecomGuangxiCall = null;
//		找到prod_type
		String url1 ="http://gx.189.cn/chaxun/iframe/qdcx_new_div.jsp?SERV_NO=FCX-4";
		Page page2 = webClient.getPage(url1);
		if(page2.getWebResponse().getContentAsString().contains("qry_num"))
		{
			Document doc1 = Jsoup.parse(page2.getWebResponse().getContentAsString());
			Element elementById = doc1.getElementById("qry_num");
//			System.out.println(elementById);
			String string = elementById.toString();
			int area_codel = string.indexOf("area_code");
			int prod_typel = string.indexOf("prod_type");
			int prod_namel = string.indexOf("prod_name");
			String substring = string.substring(area_codel, prod_typel);
			String area_code = substring.substring(11,substring.length()-2);//城市代码
			String substring1 = string.substring(prod_typel, prod_namel);
			String prod_type = substring1.substring(11,substring1.length()-2);
			String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 
//          String url ="http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&BEGIN_DATE=&END_DATE=&REFRESH_FLAG=1&QRY_FLAG=1&FIND_TYPE=1038&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+dateBefore; 
	
		    String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp";
		    WebRequest webRequestpack1 = new WebRequest(new URL(url), HttpMethod.POST);
			String reString ="PRODTYPE="+prod_type+"&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE="+prod_type+"&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1041&radioQryType=on&ACCT_DATE="+dateBefore+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
			System.out.println(reString);
			webRequestpack1.setRequestBody(reString);
			HtmlPage page = webClient.getPage(webRequestpack1);
			Thread.sleep(2000);
			if(null != page)
			{
				int statusCode = page.getWebResponse().getStatusCode();
				if(statusCode==200)
				{
//					System.out.println("++++++++++++++++++++++"+page.getWebResponse().getContentAsString());
					HtmlTable object = page.getFirstByXPath("//table[@id='list_table']");
					//System.out.println(object.asXml());
					if(page.getWebResponse().getContentAsString().contains("无记录"))
					{
						webParam.setHtml(page.getWebResponse().getContentAsString());
						return webParam;
					}
					else if(object != null)
					{
						List<TelecomGuangxiCall> list = new ArrayList<TelecomGuangxiCall>();
						Document doc = Jsoup.parse(object.asXml());
						Elements elements = doc.getElementsByTag("tr");
						for (int i = 3; i < elements.size()-2; i++) {
							telecomGuangxiCall = new TelecomGuangxiCall();
							Elements elementsByTag = elements.get(i).select("td");
							telecomGuangxiCall.setStartTime(elementsByTag.get(1).text());
							telecomGuangxiCall.setAddr(elementsByTag.get(2).text());
							telecomGuangxiCall.setCallType(elementsByTag.get(3).text());
							telecomGuangxiCall.setHisNum(elementsByTag.get(4).text());
							telecomGuangxiCall.setCallTime(elementsByTag.get(5).text());
							telecomGuangxiCall.setCallStatus(elementsByTag.get(6).text());
							telecomGuangxiCall.setMoney(elementsByTag.get(7).text());
							telecomGuangxiCall.setTaskid(messageLogin.getTask_id());
							list.add(telecomGuangxiCall);
						}
						webParam.setCode(statusCode);
						webParam.setHtml(page.getWebResponse().getContentAsString());
						webParam.setList(list);
						webParam.setPage(page);
						webParam.setUrl(url);
						return webParam;
					}
				}
			}
		}
		return null;
	}
	
	
	//第一次获取
	public WebParam<TelecomGuangxiParser> getPhoneCodeFirst(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			tracer.addTag("TelecomGuangxiParser.getPhoneCodeFirst" , taskMobile.getTaskid());

			WebParam<TelecomGuangxiParser> webParam = new WebParam<TelecomGuangxiParser>();
			WebClient webClient = addcookie(taskMobile);
			
			String url ="http://gx.189.cn/service/bill/getRand.jsp?MOBILE_NAME="+messageLogin.getName()+"&RAND_TYPE=025&OPER_TYPE=CR1&PRODTYPE="+taskMobile.getTesthtml();//025
			Page page = webClient.getPage(url);
			webParam.setWebClient(webClient);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			return webParam;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomGuangxiParser.getphonecode---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return null;
	}
	
	
	//第1次验证
	public WebParam setphonecodeFirst(TaskMobile taskMobile, MessageLogin messageLogin, String starttime,String endtime) throws Exception {
		tracer.addTag("TelecomGuangxiParser.setphonecodeFirst" , taskMobile.getTaskid());
		String urlData = "http://gx.189.cn/public/realname/checkRealName.jsp?NUM="+messageLogin.getName()+"&V_PASSWORD="+messageLogin.getSms_code()+":&RAND_TYPE:025";//025
			WebClient webClient = addcookieFirst(taskMobile);
			Page page = getPage(webClient, taskMobile, urlData, null);
			WebParam webParam = new WebParam();
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				tracer.addTag("parser.Telecom.setphonecodeFirst",html);
				webParam.setHtml(html);
				webParam.setWebClient(webClient);
				return webParam;
			}
		return null;
	}
	
	
	//实名认证
	public String realName(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		
		String mytext2 = java.net.URLEncoder.encode(taskMobile.getBasicUser().getName(),"utf-8"); 
	    String url = "http://gx.189.cn/chaxun/iframe/inxxall_new.jsp?PRODTYPE=2100297&RAND_TYPE=002&BureauCode="+taskMobile.getTrianNum()+"&ACC_NBR="+messageLogin.getName()+"&PROD_TYPE=2100297&PROD_PWD=&REFRESH_FLAG=1&BEGIN_DATE=&END_DATE=&SERV_NO=&QRY_FLAG=1&MOBILE_NAME="+messageLogin.getName()+"&OPER_TYPE=CR1&FIND_TYPE=1039&radioQryType=on&ACCT_DATE="+getTime()+"&ACCT_DATE_1="+getTime()+"&PASSWORD="+messageLogin.getSms_code()+"&CUST_NAME="+mytext2+"&CARD_TYPE=1&CARD_NO="+taskMobile.getBasicUser().getIdnum();
	    WebClient webClient = addcookie(taskMobile);
	    HtmlPage page = webClient.getPage(url);
	    if(null != page)
	    {
	    	int i = page.getWebResponse().getStatusCode();
	    	if(i==200)
	    	{
	    		//System.out.println(page.getWebResponse().getContentAsString());
	    		return page.getWebResponse().getContentAsString();
	    	}
	    }
		return null;
	}
	
	
	
}	
