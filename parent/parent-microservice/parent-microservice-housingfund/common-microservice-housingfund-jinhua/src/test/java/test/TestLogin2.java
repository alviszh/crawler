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

public class TestLogin2 {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://218.93.210.170:7001/login?type=1";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);	
    	webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "218.93.210.170:7001");
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
//		username.setText("321321198801050033");
//		password.setText("529557");
			
//		Page loggedPage = button.click();
//		Thread.sleep(1500);
//		System.out.println("登陆成功后的页面  ====》》"+loggedPage.getWebResponse().getContentAsString());
//		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
//		for(Cookie cookie:cookies){
//			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
//		}
//		
		    String loginUrl4 = "http://218.93.210.170:7001/login";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="username=321321198801050033&password=529557&captcha="+input+"&usertype=2";
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "218.93.210.170:7001");
			webRequest4.setAdditionalHeader("Origin", "http://218.93.210.170:7001");
			webRequest4.setAdditionalHeader("Referer", "http://218.93.210.170:7001/login?type=1");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(requestBody);
			Page page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
			//{"code":0,"message":"登入成功","url":"/"}
			 String url="http://218.93.210.170:7001/personal/jbxx";
             HtmlPage  jbxxPage= getHtml(url,webClient);
				System.out.println(jbxxPage.asXml());
				
			
			
		   String loginUrl5 = "http://218.93.210.170:7001/common/invoke/21B007";		                     
			WebRequest webRequest5= new WebRequest(new URL(loginUrl5), HttpMethod.POST);	
			String requestBody2="grzh=029529557";
			webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest5.setAdditionalHeader("Host", "218.93.210.170:7001");
			webRequest5.setAdditionalHeader("Origin", "http://218.93.210.170:7001");
			webRequest5.setAdditionalHeader("Referer", "http://218.93.210.170:7001/personal/jbxx");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest5.setRequestBody(requestBody2);
			Page page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
			//{"code":0,"message":null,"dataset":{"total":1,"rows":[{"ROWNUM_":1,"GRZH":"029529557","DWZH":"0029667","DWMC":"徐州市外事服务有限责任公司宿迁分公司（8%）","SEX":"男","SJH":null,"DWDH":null,"KHRQ":"2014.12.19","TQLAST":"2018.01.15","BZ":null,"XM":"徐恺强","ZJHM":"321321198801050033","GRZHZT":"11","GRZHZTMC":"正常汇缴","HJNY":"2018.01","SFDK":"否","GRZHYE":6828.18,"YJE":408,"GZJS":2550,"ZGYJE":204,"DWYJE":204,"LMKYH":null,"LMKH":null,"DWJJL":8,"ZGJJL":8,"ORGCODE":"100","ORGNAME":"市本部","SFDY":"否","SFDJ":"否","SFAYDK":"否","JTZZ":"江苏省宿迁市宿城区洋北镇友爱村高宅组21号","BTBL":0,"QJNY":"无","JZNY":"2018.01","JBYH":"宿迁建行","DWDZ":"宿迁市软件与服务外包产业园水杉大道1号知浩楼527室"}]}}

           
		    String loginUrl6 = "http://218.93.210.170:7001/common/invoke/21C002";		                     
			WebRequest webRequest6= new WebRequest(new URL(loginUrl6), HttpMethod.POST);	
			String requestBody3="filterscount=0&groupscount=0&pagenum=0&pagesize=100&recordstartindex=0&recordendindex=20&grzh=029529557";
			webRequest6.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest6.setAdditionalHeader("Host", "218.93.210.170:7001");
			webRequest6.setAdditionalHeader("Origin", "http://218.93.210.170:7001");
			webRequest6.setAdditionalHeader("Referer", "http://218.93.210.170:7001/personal/jcmx");
			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest6.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest6.setRequestBody(requestBody3);
			Page page6 = webClient.getPage(webRequest6);
			System.out.println(page6.getWebResponse().getContentAsString());
//			{"code":0,"message":"","dataset":{"total":31,"rows":[{"YWLX":"0101","GRZHYE":"6627.65","QRRQ":"2017.11.21","RNUM":"1","HJNY":"2017.11","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0101","GRZHYE":"6392.45","QRRQ":"2017.10.24","RNUM":"2","HJNY":"2017.10","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0101","GRZHYE":"6157.25","QRRQ":"2017.09.22","RNUM":"3","HJNY":"2017.09","YWLXMC":"正常汇缴","ZC":"","SR":"235.2"},{"YWLX":"0103","GRZHYE":"5922.05","QRRQ":"2017.09.14","RNUM":"4","HJNY":"2017.08","YWLXMC":"单位补缴","ZC":"","SR":"235.2"},{"YWLX":"0103","GRZHYE":"5686.85","QRRQ":"2017.09.14","RNUM":"5","HJNY":"2017.07","YWLXMC":"单位补缴","ZC":"","SR":"235.2"},{"YWLX":"0130","GRZHYE":"5451.65","QRRQ":"2017.06.30","RNUM":"6","HJNY":"2017.06","YWLXMC":"年度结息","ZC":"","SR":"59.78"},{"YWLX":"0101","GRZHYE":"5391.87","QRRQ":"2017.06.23","RNUM":"7","HJNY":"2017.06","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"5169.47","QRRQ":"2017.05.24","RNUM":"8","HJNY":"2017.05","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4947.07","QRRQ":"2017.05.03","RNUM":"9","HJNY":"2017.04","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4724.67","QRRQ":"2017.04.07","RNUM":"10","HJNY":"2017.03","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"4502.27","QRRQ":"2017.02.20","RNUM":"11","HJNY":"2017.02","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"4279.87","QRRQ":"2017.01.19","RNUM":"12","HJNY":"2017.01","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"4057.47","QRRQ":"2017.01.06","RNUM":"13","HJNY":"2016.12","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"3835.07","QRRQ":"2017.01.06","RNUM":"14","HJNY":"2016.11","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"3612.67","QRRQ":"2016.10.25","RNUM":"15","HJNY":"2016.10","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0101","GRZHYE":"3390.27","QRRQ":"2016.09.28","RNUM":"16","HJNY":"2016.09","YWLXMC":"正常汇缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"3167.87","QRRQ":"2016.09.13","RNUM":"17","HJNY":"2016.08","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0103","GRZHYE":"2945.47","QRRQ":"2016.08.05","RNUM":"18","HJNY":"2016.07","YWLXMC":"单位补缴","ZC":"","SR":"222.4"},{"YWLX":"0130","GRZHYE":"2723.07","QRRQ":"2016.06.30","RNUM":"19","HJNY":"2016.06","YWLXMC":"年度结息","ZC":"","SR":"19.07"},{"YWLX":"0101","GRZHYE":"2704","QRRQ":"2016.06.28","RNUM":"20","HJNY":"2016.06","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"2496","QRRQ":"2016.06.28","RNUM":"21","HJNY":"2016.05","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"2288","QRRQ":"2016.04.28","RNUM":"22","HJNY":"2016.04","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"2080","QRRQ":"2016.03.25","RNUM":"23","HJNY":"2016.03","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"1872","QRRQ":"2016.02.26","RNUM":"24","HJNY":"2016.02","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1664","QRRQ":"2016.02.15","RNUM":"25","HJNY":"2016.01","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1456","QRRQ":"2016.01.13","RNUM":"26","HJNY":"2015.12","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"1248","QRRQ":"2015.12.08","RNUM":"27","HJNY":"2015.11","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"1040","QRRQ":"2015.10.27","RNUM":"28","HJNY":"2015.10","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0103","GRZHYE":"832","QRRQ":"2015.10.10","RNUM":"29","HJNY":"2015.09","YWLXMC":"单位补缴","ZC":"","SR":"208"},{"YWLX":"0101","GRZHYE":"624","QRRQ":"2015.08.26","RNUM":"30","HJNY":"2015.08","YWLXMC":"正常汇缴","ZC":"","SR":"208"},{"YWLX":"0209","GRZHYE":"416","QRRQ":"2015.08.26","RNUM":"31","HJNY":"2015.08","YWLXMC":"其它补缴","ZC":"","SR":"416"}]}}
//{"code":0,"message":null,"dataset":{"total":46,"rows":[{"QRRQ":"2018.01.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2018.01","SR":408,"ZC":null,"GRZHYE":6828.18,"ROWNUM_":1},{"QRRQ":"2018.01.15","YWLX":"6I","YWLXMC":"按年还商业住房贷款","HJNY":"2018.01","SR":null,"ZC":4410,"GRZHYE":6420.18,"ROWNUM_":2},{"QRRQ":"2017.12.29","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.12","SR":408,"ZC":null,"GRZHYE":10830.18,"ROWNUM_":3},{"QRRQ":"2017.11.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.11","SR":408,"ZC":null,"GRZHYE":10422.18,"ROWNUM_":4},{"QRRQ":"2017.10.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.10","SR":408,"ZC":null,"GRZHYE":10014.18,"ROWNUM_":5},{"QRRQ":"2017.09.28","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.09","SR":408,"ZC":null,"GRZHYE":9606.18,"ROWNUM_":6},{"QRRQ":"2017.08.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.08","SR":408,"ZC":null,"GRZHYE":9198.18,"ROWNUM_":7},{"QRRQ":"2017.08.01","YWLX":"13","YWLXMC":"单位补缴","HJNY":"2017.07","SR":408,"ZC":null,"GRZHYE":8790.18,"ROWNUM_":8},{"QRRQ":"2017.06.30","YWLX":"30","YWLXMC":"期末结息","HJNY":"2017.06","SR":124.34,"ZC":null,"GRZHYE":8382.18,"ROWNUM_":9},{"QRRQ":"2017.06.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.06","SR":368,"ZC":null,"GRZHYE":8257.84,"ROWNUM_":10},{"QRRQ":"2017.05.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.05","SR":368,"ZC":null,"GRZHYE":7889.84,"ROWNUM_":11},{"QRRQ":"2017.04.28","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.04","SR":368,"ZC":null,"GRZHYE":7521.84,"ROWNUM_":12},{"QRRQ":"2017.04.01","YWLX":"13","YWLXMC":"单位补缴","HJNY":"2017.03","SR":368,"ZC":null,"GRZHYE":7153.84,"ROWNUM_":13},{"QRRQ":"2017.03.02","YWLX":"13","YWLXMC":"单位补缴","HJNY":"2017.02","SR":368,"ZC":null,"GRZHYE":6785.84,"ROWNUM_":14},{"QRRQ":"2017.01.25","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2017.01","SR":368,"ZC":null,"GRZHYE":6417.84,"ROWNUM_":15},{"QRRQ":"2017.01.19","YWLX":"6I","YWLXMC":"按年还商业住房贷款","HJNY":"2017.01","SR":null,"ZC":4400,"GRZHYE":6049.84,"ROWNUM_":16},{"QRRQ":"2017.01.04","YWLX":"13","YWLXMC":"单位补缴","HJNY":"2016.12","SR":368,"ZC":null,"GRZHYE":10449.84,"ROWNUM_":17},{"QRRQ":"2016.11.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.11","SR":368,"ZC":null,"GRZHYE":10081.84,"ROWNUM_":18},{"QRRQ":"2016.10.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.10","SR":368,"ZC":null,"GRZHYE":9713.84,"ROWNUM_":19},{"QRRQ":"2016.10.09","YWLX":"13","YWLXMC":"单位补缴","HJNY":"2016.09","SR":368,"ZC":null,"GRZHYE":9345.84,"ROWNUM_":20},{"QRRQ":"2016.08.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.08","SR":368,"ZC":null,"GRZHYE":8977.84,"ROWNUM_":21},{"QRRQ":"2016.07.27","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.07","SR":368,"ZC":null,"GRZHYE":8609.84,"ROWNUM_":22},{"QRRQ":"2016.06.30","YWLX":"30","YWLXMC":"期末结息","HJNY":"2016.06","SR":114.59,"ZC":null,"GRZHYE":8241.84,"ROWNUM_":23},{"QRRQ":"2016.06.28","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.06","SR":368,"ZC":null,"GRZHYE":8127.25,"ROWNUM_":24},{"QRRQ":"2016.05.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.05","SR":368,"ZC":null,"GRZHYE":7759.25,"ROWNUM_":25},{"QRRQ":"2016.04.26","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.04","SR":368,"ZC":null,"GRZHYE":7391.25,"ROWNUM_":26},{"QRRQ":"2016.03.31","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.03","SR":368,"ZC":null,"GRZHYE":7023.25,"ROWNUM_":27},{"QRRQ":"2016.02.29","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.02","SR":368,"ZC":null,"GRZHYE":6655.25,"ROWNUM_":28},{"QRRQ":"2016.02.02","YWLX":"6I","YWLXMC":"按年还商业住房贷款","HJNY":"2016.02","SR":null,"ZC":3200,"GRZHYE":6287.25,"ROWNUM_":29},{"QRRQ":"2016.01.26","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2016.01","SR":368,"ZC":null,"GRZHYE":9487.25,"ROWNUM_":30},{"QRRQ":"2015.12.29","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.12","SR":368,"ZC":null,"GRZHYE":9119.25,"ROWNUM_":31},{"QRRQ":"2015.11.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.11","SR":368,"ZC":null,"GRZHYE":8751.25,"ROWNUM_":32},{"QRRQ":"2015.10.29","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.10","SR":368,"ZC":null,"GRZHYE":8383.25,"ROWNUM_":33},{"QRRQ":"2015.09.28","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.09","SR":368,"ZC":null,"GRZHYE":8015.25,"ROWNUM_":34},{"QRRQ":"2015.08.28","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.08","SR":368,"ZC":null,"GRZHYE":7647.25,"ROWNUM_":35},{"QRRQ":"2015.07.30","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.07","SR":368,"ZC":null,"GRZHYE":7279.25,"ROWNUM_":36},{"QRRQ":"2015.06.30","YWLX":"30","YWLXMC":"期末结息","HJNY":"2015.06","SR":46.85,"ZC":null,"GRZHYE":6911.25,"ROWNUM_":37},{"QRRQ":"2015.06.26","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.06","SR":288,"ZC":null,"GRZHYE":6864.4,"ROWNUM_":38},{"QRRQ":"2015.06.12","YWLX":"D1","YWLXMC":"职工行内并户","HJNY":"2015.06","SR":3654.43,"ZC":null,"GRZHYE":6576.4,"ROWNUM_":39},{"QRRQ":"2015.05.22","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.05","SR":288,"ZC":null,"GRZHYE":2921.97,"ROWNUM_":40},{"QRRQ":"2015.05.14","YWLX":"D1","YWLXMC":"职工行内并户","HJNY":"2015.05","SR":1193.97,"ZC":null,"GRZHYE":2633.97,"ROWNUM_":41},{"QRRQ":"2015.04.21","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.04","SR":288,"ZC":null,"GRZHYE":1440,"ROWNUM_":42},{"QRRQ":"2015.03.20","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.03","SR":288,"ZC":null,"GRZHYE":1152,"ROWNUM_":43},{"QRRQ":"2015.02.15","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.02","SR":288,"ZC":null,"GRZHYE":864,"ROWNUM_":44},{"QRRQ":"2015.01.22","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2015.01","SR":288,"ZC":null,"GRZHYE":576,"ROWNUM_":45},{"QRRQ":"2014.12.22","YWLX":"11","YWLXMC":"正常汇缴","HJNY":"2014.12","SR":288,"ZC":null,"GRZHYE":288,"ROWNUM_":46}]}}
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
