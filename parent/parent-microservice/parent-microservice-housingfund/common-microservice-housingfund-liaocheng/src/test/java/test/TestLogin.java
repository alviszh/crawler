package test;

import java.io.File;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://222.175.23.30/hfmis_wt/login;jsessionid=N1L4hwVf5YHTGTpL2DKdTg2GL4g2q330S2yN7wSPcgQppJKyc5Ln!1530361531";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "222.175.23.30:7001");
		webRequest.setAdditionalHeader("Referer", "http://www.lcgjj.com.cn/");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		
		HtmlPage page2 = webClient.getPage(webRequest);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page2.asXml());
		HtmlImage image =page2.getFirstByXPath("//img[@id='captcha_img1']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
	

//		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@id='username_per']");
//
//		
//		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@name='password']");	
//		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@id='captcha']");		
//		HtmlButton button = (HtmlButton)page2.getFirstByXPath("//button[@id='loginBtn']");
//		
//		System.out.println("username   :"+username.asXml());
//		System.out.println("password   :"+password.asXml());	
//		System.out.println("button   :"+button.asXml());
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
//		code.setText(input);
//		username.setText("372928198210170225");
//		password.setText("123654");
			
//		Page loggedPage = button.click();
//		Thread.sleep(1500);
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
//		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
//		}
//		
		    String loginUrl4 = "http://222.175.23.30/hfmis_wt/login";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="username=372928198210170225&password=123654&captcha="+input+"&usertype=2";
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "222.175.23.30");
			webRequest4.setAdditionalHeader("Origin", "http://222.175.23.30");
			webRequest4.setAdditionalHeader("Referer", "http://222.175.23.30/hfmis_wt/login");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(requestBody);
			Page page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
			//{"code":0,"message":"登入成功","url":"/hfmis_wt/"}
			//{"code":-94,"message":"用户名不存在"}
			//{"code":-90,"message":"密码错误"}
			//{code: -93, message: "验证码错误"}

			 String url="http://222.175.23.30/hfmis_wt/personal/jbxx";
             HtmlPage  jbxxPage= getHtml(url,webClient);
				System.out.println(jbxxPage.asXml());
				
			
			
		   String loginUrl5 = "http://222.175.23.30/hfmis_wt/common/zhfw/invoke/21B007";		                     
			WebRequest webRequest5= new WebRequest(new URL(loginUrl5), HttpMethod.POST);	
			String requestBody2="grzh=021063697";
			webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest5.setAdditionalHeader("Host", "222.175.23.30");
			webRequest5.setAdditionalHeader("Origin", "http://222.175.23.30");
			webRequest5.setAdditionalHeader("Referer", "http://222.175.23.30/hfmis_wt/personal/jbxx");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest5.setRequestBody(requestBody2);
			Page page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
			//{"code":0,"message":"","dataset":{"total":1,"rows":[{"SCHJNY":"无","GRZHYE":"6,627.65","DWZH":"0100609","SFDK":"是","SPTEL":"","LXDH":"","HJNY":"2017.11","SJH":"","DWMC":"青岛中劳联劳务服务连锁有限公司聊城分公司","ZJHM":"3729**********0225","KHRQ":"2015.08.26","XM":"","GRZHZTMC":"正常","YZBM":"","GRZHZT":"01","GZJS":"1470","LXDZ":"","SEX":"男","GRZH":"021063697","DWJJL":"8","YJE":"235.20","ZGJJL":"8","ORGNAME":"市中心本部"}]}}
           
		    String loginUrl6 = "http://222.175.23.30/hfmis_wt/common/zhfw/invoke/21C002";		                     
			WebRequest webRequest6= new WebRequest(new URL(loginUrl6), HttpMethod.POST);	
			String requestBody3="filterscount=0&groupscount=0&pagenum=0&pagesize=100&recordstartindex=0&recordendindex=20&grzh=021063697";
			webRequest6.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest6.setAdditionalHeader("Host", "222.175.23.30");
			webRequest6.setAdditionalHeader("Origin", "http://222.175.23.30");
			webRequest6.setAdditionalHeader("Referer", "http://222.175.23.30/hfmis_wt/personal/jcmx");
			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest6.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest6.setRequestBody(requestBody3);
			Page page6 = webClient.getPage(webRequest6);
			System.out.println(page6.getWebResponse().getContentAsString());
//			{"code":0,"message":"","dataset":{"total":31,"rows":[{"YWLX":"0101","GRZHYE":"6627.65","QRRQ":"2017.11.21","RNUM":"1","HJNY":"2017.11","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0101","GRZHYE":"6392.45","QRRQ":"2017.10.24","RNUM":"2","HJNY":"2017.10","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0101","GRZHYE":"6157.25","QRRQ":"2017.09.22","RNUM":"3","HJNY":"2017.09","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0103","GRZHYE":"5922.05","QRRQ":"2017.09.14","RNUM":"4","HJNY":"2017.08","YWLXMC":"单位补缴","ZC":"","SR":"235.2"},{"YWLX":"0103","GRZHYE":"5686.85","QRRQ":"2017.09.14","RNUM":"5","HJNY":"2017.07","YWLXMC":"单位补缴","ZC":"","SR":"235.2"},{"YWLX":"0130","GRZHYE":"5451.65","QRRQ":"2017.06.30","RNUM":"6","HJNY":"2017.06","YWLXMC":"年度结息","ZC":"","SR":"59.78"},{"YWLX":"0101","GRZHYE":"5391.87","QRRQ":"2017.06.23","RNUM":"7","HJNY":"2017.06","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"5169.47","QRRQ":"2017.05.24","RNUM":"8","HJNY":"2017.05","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4947.07","QRRQ":"2017.05.03","RNUM":"9","HJNY":"2017.04","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4724.67","QRRQ":"2017.04.07","RNUM":"10","HJNY":"2017.03","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"4502.27","QRRQ":"2017.02.20","RNUM":"11","HJNY":"2017.02","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"4279.87","QRRQ":"2017.01.19","RNUM":"12","HJNY":"2017.01","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4057.47","QRRQ":"2017.01.06","RNUM":"13","HJNY":"2016.12","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"3835.07","QRRQ":"2017.01.06","RNUM":"14","HJNY":"2016.11","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"3612.67","QRRQ":"2016.10.25","RNUM":"15","HJNY":"2016.10","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"3390.27","QRRQ":"2016.09.28","RNUM":"16","HJNY":"2016.09","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"3167.87","QRRQ":"2016.09.13","RNUM":"17","HJNY":"2016.08","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"2945.47","QRRQ":"2016.08.05","RNUM":"18","HJNY":"2016.07","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0130","GRZHYE":"2723.07","QRRQ":"2016.06.30","RNUM":"19","HJNY":"2016.06","YWLXMC":"年度结息","ZC":"","SR":"19.07"},{"YWLX":"0101","GRZHYE":"2704","QRRQ":"2016.06.28","RNUM":"20","HJNY":"2016.06","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"2496","QRRQ":"2016.06.28","RNUM":"21","HJNY":"2016.05","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"2288","QRRQ":"2016.04.28","RNUM":"22","HJNY":"2016.04","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"2080","QRRQ":"2016.03.25","RNUM":"23","HJNY":"2016.03","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"1872","QRRQ":"2016.02.26","RNUM":"24","HJNY":"2016.02","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1664","QRRQ":"2016.02.15","RNUM":"25","HJNY":"2016.01","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1456","QRRQ":"2016.01.13","RNUM":"26","HJNY":"2015.12","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1248","QRRQ":"2015.12.08","RNUM":"27","HJNY":"2015.11","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"1040","QRRQ":"2015.10.27","RNUM":"28","HJNY":"2015.10","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"832","QRRQ":"2015.10.10","RNUM":"29","HJNY":"2015.09","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"624","QRRQ":"2015.08.26","RNUM":"30","HJNY":"2015.08","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0209","GRZHYE":"416","QRRQ":"2015.08.26","RNUM":"31","HJNY":"2015.08","YWLXMC":"其它补缴","ZC":"","SR":"416"}]}}

		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
