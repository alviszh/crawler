package test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://gjjobs.my.gov.cn/ispobs/";
		HtmlPage page2 =  getHtml(loginUrl,webClient);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page2.asXml());
		HtmlImage image =page2.getFirstByXPath("//img[contains(@src,'globalController/CreateVerifyImg.do')]");
		
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
		    String loginUrl4 = "http://gjjobs.my.gov.cn/ispobs/loginController/grLoginVerify.do";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
		//	String requestBody="YHLX=1&USERCODE=510722199003020021&USERPWD=Michel900302&VERIFYCODE="+input;
			
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("YHLX", "1"));
			paramsList.add(new NameValuePair("USERCODE", "510722199003020021"));
			paramsList.add(new NameValuePair("USERPWD", "Michel900302"));
			paramsList.add(new NameValuePair("VERIFYCODE", input));
			
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
			webRequest4.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
			webRequest4.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestParameters(paramsList);
			Page page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
			//{"MSG":"登录成功","DATA":[{"YHZT":0,"ZCRQ":1514362825000,"QYRQ":null,"DWZH":"01205080081773","QYDA":null,"QYZT":1,"YHMM":"5212B400F7E57E13BE7247AEF075998E","AREAID":1,"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","APPLYID":0,"XINGMING":"易雪","WXHM":null,"ETPSID":6212,"RANGEID":1,"INDVID":740299,"YHLX":0,"CRMNUM":"1001956","DZYX":null,"KFID":"0","ZXRQ":null,"GRZH":"01205080081773000264","SJHM":"13118189912","FWQDID":2,"ZJHM":"510722199003020021"}],"STATE":"0000"}

		    String loginUrl5 = "http://gjjobs.my.gov.cn/ispobs/grQueryController/grzhxxcx.do";		                     
			WebRequest webRequest5= new WebRequest(new URL(loginUrl5), HttpMethod.POST);	
			
			webRequest5.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest5.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest5.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest5.setAdditionalHeader("Connection", "keep-alive");
			webRequest5.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest5.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
			webRequest5.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
			webRequest5.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/grQueryController/grxxcx.do");
			webRequest5.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest5.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			Page page5 = webClient.getPage(webRequest5);
			System.out.println(page5.getWebResponse().getContentAsString());
			//{"MSG":"","DATA":[{"ZJLXDISP":"身份证","YZBM":"621000","XINGMING":"易**","TEMPORARYWORKERS":0,"ETPSID":6212,"BANKID":null,"FINANCEDATE":"2016-10","HYZKDISP":"未婚","XINGBIE":"2","ZJLX":"01","THECITYPERSONNEL":0,"XUELI":"4","ZJHM":"510722********0021","ZHIYEDISP":"职员","HYZK":"10","ZHICHENDISP":"无","FREEZESTATE":0,"TEMPORARYWORKERSDISP":"否","DWZH":"01205080081773","CSNY":"1990-03","BANKNAME":null,"THEFARMERWORKER":0,"NEWESTMARKLOG":null,"JTZZ":"四川省三台县芦溪镇南街44号","FCDATE":null,"XUELIDISP":"学士","FREEZEMONEY":0,"BANKACCOUNTS":null,"HJYJCE":138,"INDVID":740299,"ZHIYE":"17","THECITYPERSONNELDISP":"否","SJHM":"131****9912","DWYJCE":69,"BANKLINKNO":null,"FREEZESTATETXT":null,"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","PAYBANKACCNAME":"建设银行绵阳分行营业部51001658636050302508","LOANSTTXT":null,"GRYJCE":69,"ABSTRACT":null,"PERSONALBUSINESSMANDISP":"否","PERSONALBUSINESSMAN":0,"ETPSDATE":"2017-12","JTYSR":0,"ZHIWUDISP":"无","CZYJCE":0,"ZHICHEN":"0","TOTALBALANCE":4532.54,"CURSTATUS":0,"BEGINPAYDATE":"2016-06","GDDHHM":"0816-2979123","XINGBIEDISP":"女","GRJCJS":1380,"THEFARMERWORKERDISP":"否","INDVDATE":"2017-12","FREEZESTATEDISP":"未冻结","ZHIWU":"0","MONTHLYINCOME":null,"CURSTATUSDISP":"正常","GRZH":"01205080081773000264","OTHERCODE":null,"KHRQ":"20160726"}],"STATE":"0000"}

		    
		    String loginUrl6 = "http://gjjobs.my.gov.cn/ispobs/grQueryController/grzhmxcx.do";		                     
			WebRequest webRequest6= new WebRequest(new URL(loginUrl6), HttpMethod.POST);	
			
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("KSNY", "201601"));
			paramsList2.add(new NameValuePair("JSNY", "201801"));
			paramsList2.add(new NameValuePair("orderbyname", "CALINTERDATE"));
			paramsList2.add(new NameValuePair("isasc", "DESC"));
			paramsList2.add(new NameValuePair("pagesize", "100"));
			paramsList2.add(new NameValuePair("pageindex", "1"));
			
			String requestBody3="KSNY=201601&JSNY=201801&orderbyname=CALINTERDATE&isasc=DESC&pagesize=100&pageindex=1";
			webRequest6.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest6.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest6.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest6.setAdditionalHeader("Connection", "keep-alive");
			webRequest6.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest6.setAdditionalHeader("Host", "gjjobs.my.gov.cn");
			webRequest6.setAdditionalHeader("Origin", "http://gjjobs.my.gov.cn");
			webRequest6.setAdditionalHeader("Referer", "http://gjjobs.my.gov.cn/ispobs/grQueryController/grxxcx.do");
			webRequest6.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest6.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest6.setRequestParameters(paramsList2);
			Page page6 = webClient.getPage(webRequest6);
			System.out.println(page6.getWebResponse().getContentAsString());
           //{"total":1,"records":"23","rows":[{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017121900005136","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工] 2017年12月","INTEREST":0,"KSNY":"2017-12","CALINTERDATE":"2017-12-19","ETPSFUNDID":1198710,"TOTALBALANCE":4532.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":1,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":35032292,"OPERATOR":"王敏","JSNY":"2017-12","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-12-19"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017112900001271","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年11月 至 2017年11月]","INTEREST":0,"KSNY":"2017-11","CALINTERDATE":"2017-11-29","ETPSFUNDID":1181474,"TOTALBALANCE":4394.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":2,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":34905803,"OPERATOR":"贺娜","JSNY":"2017-11","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-11-29"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017103000001828","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年10月 至 2017年10月]","INTEREST":0,"KSNY":"2017-10","CALINTERDATE":"2017-10-27","ETPSFUNDID":1162334,"TOTALBALANCE":4256.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":3,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":34458877,"OPERATOR":"贺娜","JSNY":"2017-10","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-10-30"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017092700000788","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年09月 至 2017年09月]","INTEREST":0,"KSNY":"2017-09","CALINTERDATE":"2017-09-27","ETPSFUNDID":1146776,"TOTALBALANCE":4118.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":4,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":34057524,"OPERATOR":"贺娜","JSNY":"2017-09","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-09-27"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017083000000995","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年08月 至 2017年08月]","INTEREST":0,"KSNY":"2017-08","CALINTERDATE":"2017-08-30","ETPSFUNDID":1131693,"TOTALBALANCE":3980.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":5,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":33757256,"OPERATOR":"贺娜","JSNY":"2017-08","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-08-30"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017080400000612","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年07月 至 2017年07月]","INTEREST":0,"KSNY":"2017-07","CALINTERDATE":"2017-08-04","ETPSFUNDID":1118411,"TOTALBALANCE":3842.54,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":6,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":33489919,"OPERATOR":"贺娜","JSNY":"2017-07","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-08-04"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"2017ndqc","XINGMING":"易雪","PAYMONEY":0,"ABSTRACT":"2017年度期初","INTEREST":0,"KSNY":null,"CALINTERDATE":"2017-07-01","ETPSFUNDID":1099053,"TOTALBALANCE":3704.54,"ZJHM":"510722199003020021","INDVMONEY":0,"DWZH":"01205080081773","ETPSMONEY":0,"ROW_NUM":7,"PAYMONEYOUT":null,"PAYTYPE":9,"PAYMONEYIN":null,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":33067245,"OPERATOR":"system","JSNY":null,"GRZH":"01205080081773000264","PAYNAME":"期初","SYDATE":"2017-07-05"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"2016ndjx","XINGMING":"易雪","PAYMONEY":40.93,"ABSTRACT":"2016年度结息","INTEREST":0,"KSNY":null,"CALINTERDATE":"2017-06-30","ETPSFUNDID":1099052,"TOTALBALANCE":3704.54,"ZJHM":"510722199003020021","INDVMONEY":0,"DWZH":"01205080081773","ETPSMONEY":0,"ROW_NUM":8,"PAYMONEYOUT":null,"PAYTYPE":3,"PAYMONEYIN":40.93,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":33065883,"OPERATOR":"system","JSNY":null,"GRZH":"01205080081773000264","PAYNAME":"利息","SYDATE":"2017-07-05"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017062700003488","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年06月 至 2017年06月]","INTEREST":0,"KSNY":"2017-06","CALINTERDATE":"2017-06-27","ETPSFUNDID":1084960,"TOTALBALANCE":3663.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":9,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":32267713,"OPERATOR":"贺娜","JSNY":"2017-06","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-06-27"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017060200000096","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年05月 至 2017年05月]","INTEREST":0,"KSNY":"2017-05","CALINTERDATE":"2017-05-31","ETPSFUNDID":1066278,"TOTALBALANCE":3525.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":10,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":31862164,"OPERATOR":"贺娜","JSNY":"2017-05","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-06-02"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017042800001906","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年04月 至 2017年04月]","INTEREST":0,"KSNY":"2017-04","CALINTERDATE":"2017-04-28","ETPSFUNDID":1049976,"TOTALBALANCE":3387.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":11,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":31425078,"OPERATOR":"贺娜","JSNY":"2017-04","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-04-28"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017033100000595","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年03月 至 2017年03月]","INTEREST":0,"KSNY":"2017-03","CALINTERDATE":"2017-03-29","ETPSFUNDID":1032959,"TOTALBALANCE":3249.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":12,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":30970934,"OPERATOR":"贺娜","JSNY":"2017-03","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-03-31"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017022800000374","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年02月 至 2017年02月]","INTEREST":0,"KSNY":"2017-02","CALINTERDATE":"2017-02-27","ETPSFUNDID":1014847,"TOTALBALANCE":3111.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":13,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":30561632,"OPERATOR":"贺娜","JSNY":"2017-02","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-02-28"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017020900001929","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2017年01月 至 2017年01月]","INTEREST":0,"KSNY":"2017-01","CALINTERDATE":"2017-02-09","ETPSFUNDID":1003380,"TOTALBALANCE":2973.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":14,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":30338363,"OPERATOR":"王敏","JSNY":"2017-01","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-02-09"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2017010600000239","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2016年12月 至 2016年12月]","INTEREST":0,"KSNY":"2016-12","CALINTERDATE":"2017-01-06","ETPSFUNDID":986928,"TOTALBALANCE":2835.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":15,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":30000163,"OPERATOR":"王敏","JSNY":"2016-12","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2017-01-06"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"ZJ2016121500007455","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"[单位+职工 2016年11月 至 2016年11月]","INTEREST":0,"KSNY":"2016-11","CALINTERDATE":"2016-12-15","ETPSFUNDID":972057,"TOTALBALANCE":2697.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":16,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":29754095,"OPERATOR":"王敏","JSNY":"2016-11","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-12-15"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"19329937","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"全额汇缴 2016年10月 至 2016年10月","INTEREST":0,"KSNY":"2016-10","CALINTERDATE":"2016-11-15","ETPSFUNDID":959831,"TOTALBALANCE":2559.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":17,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":29553239,"OPERATOR":"王敏","JSNY":"2016-10","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-11-15"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"19061837","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"全额汇缴 2016年09月 至 2016年09月","INTEREST":0,"KSNY":"2016-09","CALINTERDATE":"2016-10-18","ETPSFUNDID":937080,"TOTALBALANCE":2421.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":18,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":29292217,"OPERATOR":"王敏","JSNY":"2016-09","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-10-18"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"18770900","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"全额汇缴 2016年08月 至 2016年08月","INTEREST":0,"KSNY":"2016-08","CALINTERDATE":"2016-09-08","ETPSFUNDID":911964,"TOTALBALANCE":2283.61,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":19,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":29007592,"OPERATOR":"王敏","JSNY":"2016-08","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-09-08"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"18571832","XINGMING":"易雪","PAYMONEY":1869.61,"ABSTRACT":"职工易雪合并到易雪","INTEREST":0,"KSNY":"2016-08","CALINTERDATE":"2016-08-12","ETPSFUNDID":902330,"TOTALBALANCE":2145.61,"ZJHM":"510722199003020021","INDVMONEY":0,"DWZH":"01205080081773","ETPSMONEY":0,"ROW_NUM":20,"PAYMONEYOUT":null,"PAYTYPE":8,"PAYMONEYIN":1869.61,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":28825056,"OPERATOR":"蒋小青","JSNY":"2016-08","GRZH":"01205080081773000264","PAYNAME":"合并","SYDATE":"2016-08-12"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"18562176","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"全额汇缴 2016年07月 至 2016年07月","INTEREST":0,"KSNY":"2016-07","CALINTERDATE":"2016-08-11","ETPSFUNDID":901156,"TOTALBALANCE":276,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":21,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":28815579,"OPERATOR":"王敏","JSNY":"2016-07","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-08-11"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"18425719","XINGMING":"易雪","PAYMONEY":138,"ABSTRACT":"全额汇缴 2016年06月 至 2016年06月","INTEREST":0,"KSNY":"2016-06","CALINTERDATE":"2016-07-26","ETPSFUNDID":893343,"TOTALBALANCE":138,"ZJHM":"510722199003020021","INDVMONEY":69,"DWZH":"01205080081773","ETPSMONEY":69,"ROW_NUM":22,"PAYMONEYOUT":null,"PAYTYPE":0,"PAYMONEYIN":138,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":28681724,"OPERATOR":"王敏","JSNY":"2016-06","GRZH":"01205080081773000264","PAYNAME":"汇缴","SYDATE":"2016-07-26"},{"DWMC":"成都智唯易才人力资源顾问有限公司绵阳分公司","FUNDSERIALNO":"18425280","XINGMING":"易雪","PAYMONEY":0,"ABSTRACT":"期初","INTEREST":0,"KSNY":null,"CALINTERDATE":"2016-07-01","ETPSFUNDID":880597,"TOTALBALANCE":0,"ZJHM":"510722199003020021","INDVMONEY":0,"DWZH":"01205080081773","ETPSMONEY":0,"ROW_NUM":23,"PAYMONEYOUT":null,"PAYTYPE":9,"PAYMONEYIN":null,"ARCCODE":null,"FINANCEMONEY":0,"INDVFUNDID":28681285,"OPERATOR":"王敏","JSNY":null,"GRZH":"01205080081773000264","PAYNAME":"期初","SYDATE":"2016-07-26"}]}

		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
