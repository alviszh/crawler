package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;
import com.module.ocr.utils.ChaoJiYingUtils;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	
	public static void main(String[] args) throws Exception {
//		String imgUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/validationCodeServlet.do?d="+System.currentTimeMillis();
//		String code = getVerifycode(imgUrl,null,"1902");
//		System.out.println("*******************图片验证码："+code);
		
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp";
		HtmlPage html = getHtml1(url,webClient);
		System.out.println("搜索页源代码================================================>>"+html.asXml());
		
		HtmlImage image = html.getFirstByXPath("//img[@id='indsafecode']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		@SuppressWarnings("resource")
		Scanner scanner1 = new Scanner(System.in);
		String input1 = scanner1.next();
		String sendSmsUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/passwordSetAction!getTelSafeCode?idCode=130406199110233017&logPass=Zhang1314&safeCode="+input1;
		UnexpectedPage smsPage = getHtml(sendSmsUrl,webClient);
		System.out.println("*******************发送验证码："+smsPage.getWebResponse().getContentAsString());
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		String loginUrl = "http://www.bjrbj.gov.cn/csibiz/indinfo/login_check?type=1&flag=3&j_username=130406199110233017&j_password=Zhang1314&safecode="+input1+"&i_phone="+input+"&x=47&y=26";
		HtmlPage adminPage = getHtml1(loginUrl,webClient);
		System.out.println("***********************************************登录后页面");
		System.out.println(adminPage.asXml());
	}
	
	public static UnexpectedPage getHtml(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		UnexpectedPage searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	public static HtmlPage getHtml1(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	
	public static String getVerifycode(String imgUrl, Set<Cookie> cookies, String codeType){
		
		Map<String,String> cookieMap = new HashMap<String,String>();
		if(null != cookies){
			for(Cookie cookie:cookies){
				cookieMap.put(cookie.getName(), cookie.getValue());
			}			
		}
		
		Connection con = Jsoup.connect(imgUrl).header("Content-Type","image/jpeg");

		
		String imgagePath = null;
		try {
			Response response = con.cookies(cookieMap).ignoreContentType(true).execute();
			File codeImageFile = new File("D:\\img\\1.jpg");
			
			imgagePath = codeImageFile.getAbsolutePath();
			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
			
			out.write(response.bodyAsBytes()); 
			out.close();
//			saveImageStream(imgagePath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callChaoJiYingService(imgagePath,codeType);
		
	}

	public static String callChaoJiYingService(String imgPath, String codeType){
		Gson gson = new GsonBuilder().create();
		String chaoJiYingResult = getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);
	
		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);		
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			return ChaoJiYingUtils.getErrMsg(errNo);
		}
	
		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
	
	}

}
