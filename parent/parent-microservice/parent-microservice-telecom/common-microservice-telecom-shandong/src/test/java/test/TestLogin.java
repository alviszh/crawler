package test;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	
	public static void main(String[] args) throws Exception{
		
//		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog"); 
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18953105871");
		passwordInput.setText("238378");

		HtmlPage htmlpage = button.click();
//		System.out.println(htmlpage.asXml());
		HtmlPage htmlPage2 = getHtml("http://sd.189.cn/selfservice/service/account", webClient);
		
		/*
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
		fileOutputStream = new FileOutputStream("E:\\Codeimg\\codeimg.jpg");
			while ((len = codeimg.get.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}*/
		
		
		
		String jifenurl = "http://sd.189.cn/selfservice/bill?tag=monthlyDetail";
		WebRequest webRequest = new WebRequest(new URL(jifenurl), HttpMethod.GET);
		HtmlPage yuepage = webClient.getPage(webRequest);
		
		HtmlImage codeimg = yuepage.getFirstByXPath("//img[@id='rand_rn']");
		HtmlTextInput code = (HtmlTextInput) yuepage.getFirstByXPath("//input[@id='validatecode_2busi']");
		HtmlElement getbtn = (HtmlElement) yuepage.getFirstByXPath("//a[@id='getDynamicHref_rn']");
		File fp=new File("E:\\Codeimg\\rand_rn.jpg");
		codeimg.saveAs(fp);	
		
		System.out.println("开始输入验证码：");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String verifycode = scanner.next();
		
		
		String url1 = "http://sd.189.cn/selfservice/service/realnVali";
		WebRequest webRequest2 = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "application/json, text/javascript, */*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
//		webRequest2.setAdditionalHeader("Content-Length", "234");
		webRequest2.setAdditionalHeader("Content-Type", "application/json");
		webRequest2.setAdditionalHeader("Host", "sd.189.cn");
		webRequest2.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest2.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill?tag=hisDetail");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest2.setRequestBody("{\"username_2busi\":\"%e8%94%a1%e5%85%83%e8%b4%a2\",\"credentials_type_2busi\":\"1\",\"credentials_no_2busi\":\"370181198903027153\",\"validatecode_2busi\":\""+verifycode+"\",\"randomcode_2busi\":\"655665\",\"randomcode_flag\":\"0\",\"rid\":1,\"fid\":\"bill_monthlyDetail\"}");
		Page page = webClient.getPage(webRequest2);
		String send1 = "E:\\crawler\\telecomshandong\\checkrand.txt";
		savefile(send1,page.getWebResponse().getContentAsString());
		
		
		System.out.println(webClient.getCookieManager().getCookies());
		code.setText(verifycode);
		HtmlPage click = getbtn.click();
		Thread.sleep(1000);
		String send = "E:\\crawler\\telecomshandong\\duanxin.txt";
		savefile(send,click.asXml());
		
		if(click.asXml().contains("您输入的验证码错误")){
		}

		String smsrec = "http://sd.189.cn/selfservice/service/sendSms";
		WebRequest webRequest1 = new WebRequest(new URL(smsrec), HttpMethod.POST);
		webRequest1.setAdditionalHeader("Host", "sd.189.cn");
		webRequest1.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest1.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill?tag=monthlyDetail");
		webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest1.setRequestBody("{\"orgInfo\":\"18953105871\",\"valicode\":\""+verifycode+"\",\"smsFlag\":\"real_2busi_validate\"}");
		/*List<NameValuePair> paramsList = new ArrayList<>();
		paramsList.add(new NameValuePair("orgInfo", "18953105871"));
		paramsList.add(new NameValuePair("valicode", verifycode));
		paramsList.add(new NameValuePair("smsFlag", "real_2busi_validate"));
		webRequest1.setRequestParameters(paramsList);*/
		
		Page page1 = webClient.getPage(webRequest1);
		System.out.println("-/-/-/-/-/-/-"+page1.getWebResponse().getContentAsString());
		String path2 = "E:\\crawler\\telecomshandong\\duanxin.txt";
		savefile(path2,page1.getWebResponse().getContentAsString());
		
		/*String url2 = "http://sd.189.cn/selfservice/service/sendSms";
		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Host", "sd.189.cn");
		webRequest2.setAdditionalHeader("Origin", "http://sd.189.cn");
		webRequest2.setAdditionalHeader("Referer", "http://sd.189.cn/selfservice/bill?tag=monthlyDetail");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest2.setRequestBody("{\"orgInfo\":\"18953198110\",\"valicode\":\"7886\",\"smsFlag\":\"real_2busi_validate\"}");
		
		Page page = webClient.getPage(webRequest2);
		String path2 = "E:\\crawler\\telecomshandong\\sendsms.txt";
		savefile(path2,page.getWebResponse().getContentAsString());*/
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//获取市话详单页面信息
	public static HtmlPage getShihuaInfo(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/billDetailQueryFra.parser"), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("billDetailValidate", "true"));
		webRequest.getRequestParameters().add(new NameValuePair("billDetailType", "2"));
		webRequest.getRequestParameters().add(new NameValuePair("startTime", "2017-08-01"));
		webRequest.getRequestParameters().add(new NameValuePair("endTime", "2017-08-24"));
		webRequest.getRequestParameters().add(new NameValuePair("pagingInfo.currentPage", "0"));
		webRequest.getRequestParameters().add(new NameValuePair("contactID", "201708240948267398068"));
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//通过身份验证1和身份验证2
	public static Page getShenfen(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/toDetailBillFra.parser?fastcode=00710602&cityCode=jl"), HttpMethod.GET);
		HtmlPage yanzhengPage1 = webClient.getPage(webRequest);
		Thread.sleep(3000);
		HtmlTextInput idNum = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='certCode']");
		HtmlTextInput name = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='cust_name']");
		HtmlTextInput validateInput = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='vCode2']");
		HtmlElement submitbt = (HtmlElement)yanzhengPage1.getFirstByXPath("//a[@class='btn-1']");
		HtmlImage validateImage = yanzhengPage1.getFirstByXPath("//img[@id='vImgCode2']");
		
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		validateImage.saveAs(file);
		//输入图片验证码1
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		idNum.setText("220102198908196169");
		name.setText("苗金玲");
		validateInput.setText(input);
		HtmlPage yanzhengPage2 = submitbt.click();
		if(null != yanzhengPage2){
//			Thread.sleep(3000);
//			HtmlTextInput validateInput2 = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='vCode']");
//			HtmlTextInput msgCodeInput = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='sRandomCode']");
//			HtmlElement submitbt2 = (HtmlElement)yanzhengPage2.getFirstByXPath("//a[@class='btn-1']");
			HtmlImage validateImage2 = yanzhengPage2.getFirstByXPath("//img[@id='vImgCode']");
			
			//输入图片验证码2
			validateImage2.saveAs(file);
			@SuppressWarnings("resource")
			Scanner scanner2 = new Scanner(System.in);
			String input2 = scanner2.next();
//			validateInput2.setText(input2);
			
			//输入短信验证码
			@SuppressWarnings("resource")
			Scanner scanner3 = new Scanner(System.in);
			String input3 = scanner3.next();
			
//			msgCodeInput.setText(input3);
//			HtmlPage page = submitbt2.click();
			
			WebRequest webRequest2 = new WebRequest(new URL("http://jl.189.cn/service/bill/doDetailBillFra.parser"), HttpMethod.POST);
			webRequest2.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest2.getRequestParameters().add(new NameValuePair("sRandomCode", input3));
			webRequest2.getRequestParameters().add(new NameValuePair("randCode", input2));
			Page searchPage = webClient.getPage(webRequest2);
			
			return searchPage;
		}
		return null;
	}
	
	//获取客户资料页面信息
	public static HtmlPage getUserInfo(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/manage/modifyUserInfoFra.parser?fastcode=00700588&cityCode=jl"), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
	
	/**
	 * @Des 通过关键字获取具体的标签内容
	 * @param html
	 * @param keyword
	 * @return
	 */
	public String getMsgByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			return element.text();
		}
		return null;
	}
	
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
	
}
