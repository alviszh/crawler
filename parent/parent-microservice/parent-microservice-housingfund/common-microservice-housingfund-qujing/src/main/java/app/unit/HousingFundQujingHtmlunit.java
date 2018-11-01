package app.unit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingPayDetails;
import com.microservice.dao.entity.crawler.housing.qujing.HousingQujingUserInfo;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.HousingFundQujingHtmlParser;
import net.sf.json.JSONObject;

@Component
public class HousingFundQujingHtmlunit {
	public static final Logger log = LoggerFactory.getLogger(HousingFundQujingHtmlunit.class);
	@Autowired
	private HousingFundQujingHtmlParser  housingFundQujingHtmlParser;
	@Autowired
	private TracerLog tracer;

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			int count) throws Exception {
		tracer.addTag("parser.crawler.login", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		//获取验证码
		String imageUrl = "http://www.qjzfgjj.com/website/imageCreate.jsp";
		WebRequest imageWebRequest = new WebRequest(new URL(imageUrl), HttpMethod.GET);	
		imageWebRequest.setAdditionalHeader("Content-Type", "image/jpeg;charset=UTF-8");
		imageWebRequest.setAdditionalHeader("Connection", "keep-alive");	
		imageWebRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");			
		Page page = webClient.getPage(imageWebRequest);
		WebResponse webResponse = page.getWebResponse();
		InputStream bodyStream = webResponse.getContentAsStream();
		byte[] responseContent = ByteStreams.toByteArray(bodyStream);
		bodyStream.close();
		String path=getPathBySystem();
		File imageFile = getImageLocalPath(path);		
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		// 写入数据
		outStream.write(responseContent);
		// 关闭输出流
		outStream.close();
		//超级鹰识别
		String chaoJiYingResult = AbstractChaoJiYingHandler.getVerifycodeByChaoJiYing("1902", imageFile.getAbsolutePath());
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code ====>>" + code);
		
		String loginUrl= "http://www.qjzfgjj.com/perLogin.json?accnum=113029023172&certinum=53292519870621072X&verifyCode="+code;
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/grcxdl.html");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setCharset(Charset.forName("UTF-8"));
		Page loginPage = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String loginHtml=loginPage.getWebResponse().getContentAsString();                 
		System.out.println(loginHtml);		
		tracer.addTag("parser.crawler.loginHtmlPage",loginHtml);
		webParam.setUrl(loginUrl);
		webParam.setPage(loginPage);	
		if (loginHtml.contains("msg")) {
			JSONObject jsonObjs = JSONObject.fromObject(loginHtml);
			String loginStr = jsonObjs.getString("msg");
			if (loginStr.contains("成功")) {
				webParam.setLogin(true);
				webParam.setText("登陆成功");
				webParam.setWebClient(webClient);
			}else {
				if(loginStr.contains("输入的验证码不正确")){
					webParam.setLogin(false);			
					webParam.setText("输入的验证码不正确");
				}else if(loginStr.contains("证件号码不存在")){
					webParam.setLogin(false);
					webParam.setText("证件号码不存在");
				}
				if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
					Thread.sleep(1500);
					login(messageLoginForHousing, taskHousing, count);
				}	
			}
	      }else{	    	 
    		  webParam.setLogin(false);
	    	  if (count < 3) {
					count++;
					tracer.addTag("parser.crawler.login.count" + count, "这是第" + count + "次登陆");
					Thread.sleep(1500);
					login(messageLoginForHousing, taskHousing, count);
				}
	      }
		return webParam;
	}
	public WebParam  getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int count) throws Exception {
		tracer.addTag("parser.crawler.getUserInfo", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		webClient = addcookie(webClient,taskHousing);		
	    String url = "http://www.qjzfgjj.com/qryPerBaseInfo.json";	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/grxxcx.html");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getUserInfo", html);
		HousingQujingUserInfo userInfo=new HousingQujingUserInfo();
		webParam.setUrl(url);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setPage(page);
		webParam.setHtml(html);
		if (html.contains("result")) {
			userInfo=housingFundQujingHtmlParser.htmlUserInfoParser(html,taskHousing);
			webParam.setUserInfo(userInfo);
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getUserInfo.count" + count, "这是第" + count + "次获取用户信息");
				Thread.sleep(1500);
				getUserInfo(messageLoginForHousing, taskHousing, count);
			}		
		}		
		return webParam;
	}
	
	public WebParam getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, int count) throws Exception {
		tracer.addTag("parser.crawler.getPaydetails", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		webClient = addcookie(webClient,taskHousing);
		Calendar c = Calendar.getInstance();
		String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		c.add(Calendar.YEAR, -1);
		//http://www.qjzfgjj.com/qryPerDetails.json?pagerows=10&pagenum=1&startdate=2013-01-31&enddate=2018-08-15
		String url = "http://www.qjzfgjj.com/qryPerDetails.json?pagerows=10&pagenum=1&startdate=2013-01-01&enddate="+timeNow;	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setCharset(Charset.forName("UTF-8"));
		webRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/trans/timeSelect.jsp?className=TRA020102");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		webParam.setHtml(html);
		webParam.setPage(page);
		List<HousingQujingPayDetails> paydetails=new ArrayList<HousingQujingPayDetails>();
	   if (html.contains("result")) {
			JSONObject jsonObject = JSONObject.fromObject(html);
			String totalpage= jsonObject.getString("totalpage");
			webParam.setTotalpage(totalpage);
		    paydetails=housingFundQujingHtmlParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);	
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getPaydetails(messageLoginForHousing, taskHousing, count);
			}			
		}		
		return webParam;
	}
	
	
	public WebParam getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int pageNo, int count) throws Exception {
		tracer.addTag("parser.crawler.getPaydetails", taskHousing.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam= new WebParam();
		webClient = addcookie(webClient,taskHousing);
		Calendar c = Calendar.getInstance();
		String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		c.add(Calendar.YEAR, -1);
		String url = "http://www.qjzfgjj.com/qryPerDetails.json?pagerows=10&pagenum=1&startdate=2013-01-01&enddate="+timeNow;	
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Origin", "http://www.qjzfgjj.com");
		webRequest.setAdditionalHeader("Referer", "http://www.qjzfgjj.com/website/trans/timeSelect.jsp?className=TRA020102");
		Page page = webClient.getPage(webRequest);
		Thread.sleep(1500);
		String html=page.getWebResponse().getContentAsString();
		tracer.addTag("parser.crawler.getPaydetails", html);
		webParam.setHtml(html);
		webParam.setPage(page);	
		webParam.setUrl(url);
		webParam.setHtml(html);
		webParam.setPage(page);
		List<HousingQujingPayDetails> paydetails=new ArrayList<HousingQujingPayDetails>();
	   if (html.contains("result")) {
		    paydetails=housingFundQujingHtmlParser.htmlPaydetailsParser(html, taskHousing);
			webParam.setPaydetails(paydetails);	
		}else{
			if (count < 3) {
				count++;
				tracer.addTag("parser.crawler.getPaydetails.count" + count, "这是第" + count + "次获取公积金缴存明细信息");
				Thread.sleep(1500);
				getPaydetails(messageLoginForHousing, taskHousing, count);
			}			
		}		
		return webParam;
	}
	public WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		 for(Cookie cookie : cookies){
			 webclient.getCookieManager().addCookie(cookie);
		  }
		return webclient;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static String getPathBySystem() {
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			return path;
		}
	}
	public static File getImageLocalPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path+ "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;
	}
}
