package TestTianjin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.InsuranceService;

public class TestInsuranceTianJing extends AbstractChaoJiYingHandler{
	private static InsuranceService insuranceService;
	private static WebClient webClient = WebCrawler.getInstance().getWebClient();
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) {
		String id = getId();
		System.out.println("id ===> "+id);
		
		String imgFilePath = getImg(id);
		System.out.println("imgFilePath ===> "+imgFilePath);
		
		String code = getOcrCode(imgFilePath);
		System.out.println("code ===> "+code);
		login(code,id,webClient);
	}

//	private static void getUserInfo() {
//		String url = "http://public.tj.hrss.gov.cn/api/security/user";
//		
//		try {
//			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
//			
//			requestSettings.setAdditionalHeader("Accept", "application/json, text/plain, */*");
//			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
//			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			requestSettings.setAdditionalHeader("Host", "public.tj.hrss.gov.cn");
//			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
////			requestSettings.setAdditionalHeader("Referer", "http://public.tj.hrss.gov.cn/ehrss/si/person/ui/?code=zfRa4z");
//			
//			Page page = webClient.getPage(requestSettings);
//			String html = page.getWebResponse().getContentAsString();
//			
//			
//			
//			
//			
//			System.out.println("个人信息    =====>> "+html);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private static void login(String code, String id,WebClient webClient) {
		
		String url = "http://221.207.175.178:7989/uaa/views/person/login.html";
		//webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getCache().setMaxSize(0);
		try {
			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			
			requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			requestSettings.setAdditionalHeader("Origin", "http://221.207.175.178:7989");
			requestSettings.setAdditionalHeader("Host", "221.207.175.178:7989");
			requestSettings.setAdditionalHeader("Referer", "http://221.207.175.178:7989/uaa/personlogin");
			requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("username", "231084198511174027"));
			requestSettings.getRequestParameters().add(new NameValuePair("password", "851117"));
			requestSettings.getRequestParameters().add(new NameValuePair("captchaId", id));
			requestSettings.getRequestParameters().add(new NameValuePair("captchaWord", code));
			
			//Page page = webClient.getPage(requestSettings);
			Page page = webClient.getPage(requestSettings);
			String string = page.getWebResponse().getContentAsString();
			System.out.println(string);
			System.out.println("登录页面 ==》"+page.getWebResponse().getContentAsString());
			
			Set<Cookie> cookies = webClient.getCookieManager().getCookies();
			for(Cookie cookie : cookies){
				System.out.println("登录后的cookie ===> "+cookie.getName()+": "+cookie.getValue());
			}
			
			//String url1 = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/persons/21000845870";
			//WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
			//HtmlPage searchPage = webClient.getPage(webRequest);
			
			/*webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			// webClient.getOptions().setJavaScriptEnabled(false);
			// 2 禁用Css，可避免自动二次请求CSS进行渲染
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setUseInsecureSSL(true);
			// 3 启动客户端重定向
			webClient.getOptions().setRedirectEnabled(true);
			// 4 js运行错误时，是否抛出异常
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			// 5 设置超时
			webClient.getOptions().setTimeout(80000);
			// 等待JS驱动dom完成获得还原后的网页
			webClient.waitForBackgroundJavaScript(50000);
			// 等待JS驱动dom完成获得还原后的网页
			
			webClient.waitForBackgroundJavaScript(50000);*/
			
			//WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
			
			
			//Page page2=webClient.getPage(webRequest);
			//String json = page2.getWebResponse().getContentAsString();
			//System.out.println(json);
			
//			Date now = new Date(); 
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
//			//System.out.println(dateFormat);
//			
//			String hehe = dateFormat.format( now ); 
//			//System.out.println(hehe); 
//			String endtime = hehe.substring(0,6);
//			int startint = Integer.parseInt(endtime);
//			int start1 = (startint-1000);
//			String starttime = start1+"";

//			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
//			int year = c.get(Calendar.YEAR); 
//			int month = c.get(Calendar.MONTH);
//			String beginTime1 = (year -10)+"";
//			String beginTime = beginTime1+"0"+month;
//			String endTime = hehe;
//			System.out.println(starttime+"*********"+endtime);
//			String url2 = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=310&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
//		//	System.out.println(url2);
//			
//			Page page2=webClient.getPage(url2);
//			String json = page2.getWebResponse().getContentAsString();
			// System.out.println(json);
			//JSONObject jsonObj = JSONObject.fromObject(json);
			//JSONObject jsonObj = new JSONObject(json);
			//String string2 = jsonObj.getString("empPaymentDTO");
			//String string2 = jsonObj.getString("empPaymentDTO");
			//String string3 = jsonObj.getString("total");
			//System.out.println(string2);
			//List<InsuranceTianjinMedical> insuranceTianjinMedicals = new ArrayList<InsuranceTianjinMedical>(); 
			//ObjectMapper objectMapper = new ObjectMapper();
			//InsuranceTianjinMedicalList insuranceTianjinMedicalList = (InsuranceTianjinMedicalList)objectMapper.readValue(string2, InsuranceTianjinMedicalList.class);
			//List<InsuranceTianjinMedical> insuranceTianjinMedicals2 = insuranceTianjinMedicalList.getEmpPaymentDTO();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	private static String getOcrCode(String imgFilePath) {
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, imgFilePath);
		System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		return code;
	}

	private static String getImg(String id) {
		String url = "http://221.207.175.178:7989/uaa/captcha/img/"+id;
		
		Connection con = Jsoup.connect(url).header("Accept","image/webp,image/*,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "zh-CN,zh;q=0.8")
					.header("Connection", "keep-alive")
					.header("Host", "221.207.175.178:7989")
					.header("Content-Type","image/jpeg")
					.header("Referer", "http://221.207.175.178:7989/uaa/personlogin")
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		try {
			Response response = con.ignoreContentType(true).execute();
			String imageName = UUID.randomUUID() + ".jpg";
			File file = new File("D:\\img\\"+imageName);
			String imgagePath = file.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
			
			out.write(response.bodyAsBytes()); 
			out.close();
			return imgagePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static String getId() {
		
		String url ="http://221.207.175.178:7989/uaa/captcha/img";
		try {
			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			
			requestSettings.setAdditionalHeader("Accept", "application/json, text/plain, */*");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("Host", "221.207.175.178:7989");
			requestSettings.setAdditionalHeader("Referer", "http://221.207.175.178:7989/uaa/personlogin");
			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			Page page = webClient.getPage(requestSettings); 
			String loggedhtml = page.getWebResponse().getContentAsString();
			JSONObject jsonObject = new JSONObject(loggedhtml);
			return jsonObject.getString("id");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
