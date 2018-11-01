package app;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLogin extends AbstractChaoJiYingHandler{
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	static Gson gson = new GsonBuilder().create();
	static WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	
	
	public static void main(String[] args) {
		String url = "https://www.zhldj.gov.cn/zhrsClient/login.jsp";
		//登录
		try {
			loginByHtmlUnit(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		//获取用户信息
//		try {
//			getUserInfo();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//获取养老保险
//		try {
//			getPension();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//获取失业保险
		try {
			getUnemployment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void getUnemployment() throws Exception {
		String unemploymentUrl = "https://www.zhldj.gov.cn/zhrsClient/social.do?doMethod=listFee&insured_kind=2&curr_page=1";
		HtmlPage html = getHtml1(unemploymentUrl,webClient);
		System.out.println("==========================失业信息页面==========================");
		System.out.println(html.getWebResponse().getContentAsString());	
	}

	private static void getPension() throws Exception {
		String pensionUrl = "https://www.zhldj.gov.cn/zhrsClient/social.do?doMethod=listFee&insured_kind=1&curr_page=1";
		HtmlPage html = getHtml1(pensionUrl,webClient);
		System.out.println("==========================养老信息页面==========================");
		System.out.println(html.getWebResponse().getContentAsString());
		
		parserStr(html);
	}
	
	
	private static String parserStr(HtmlPage html){
		String page = html.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(page);
		Elements fieldsets = doc.getElementsByTag("fieldset");
		if(null != fieldsets && fieldsets.size()>2){
			for(int i = 1; i<fieldsets.size();i++){
				Element fieldset = fieldsets.get(i);
				
				String month = fieldset.child(0).child(0).text();
				String companyName = fieldset.child(0).child(1).text();
				String payType = fieldset.child(0).child(2).text();
				
				System.out.println("缴纳月份："+month);
				System.out.println("单位名称："+companyName);
				System.out.println("缴费类型："+payType);
				
				
				String paySalary = getMsgByKeyword1(page,"缴费工资");
				System.out.println("缴费工资："+paySalary);
				String payPerson = getMsgByKeyword1(page,"个人缴费");
				System.out.println("个人缴费："+payPerson);
				String payCompany = getMsgByKeyword1(page,"单位缴费");
				System.out.println("单位缴费："+payCompany);
				String companyClassified = getMsgByKeyword1(page,"单位划入");
				System.out.println("单位划入："+companyClassified);
				String dealDate = getMsgByKeyword1(page,"处理日期");
				System.out.println("处理日期:"+dealDate);
				String capitalSource = getMsgByKeyword1(page,"资金来源");
				System.out.println("资金来源:"+capitalSource);
				
				System.out.println("====================================================================");

				
			}
		}
		
		return null;
		
	}

	private static void getUserInfo() throws Exception {
		String userInfoUrl = "https://www.zhldj.gov.cn/zhrsClient/social.do";
		HtmlPage html = getHtml1(userInfoUrl,webClient);
		System.out.println("==========================用户信息页面==========================");
		System.out.println(html.getWebResponse().getContentAsString());
		
		String SSN = getMsgByKeyword(html.getWebResponse().getContentAsString(),"社会保障号码");
		System.out.println("社会保障号码："+SSN);
		String name = getMsgByKeyword(html.getWebResponse().getContentAsString(),"姓名");
		System.out.println("姓名："+name);
		String certificate = getMsgByKeyword(html.getWebResponse().getContentAsString(),"法人代码");
		System.out.println("法人代码："+certificate);
		String companyName = getMsgByKeyword(html.getWebResponse().getContentAsString(),"单位名称");
		System.out.println("单位名称："+companyName);
		String insuranceKind = getMsgByKeyword(html.getWebResponse().getContentAsString(),"参保险种");
		System.out.println("参保险种："+insuranceKind);
		String declareSalary = getMsgByKeyword(html.getWebResponse().getContentAsString(),"最新申报工资");
		System.out.println("最新申报工资："+declareSalary);
		String pensionPayMonthTotal = getMsgByKeyword(html.getWebResponse().getContentAsString(),"养老保险累计缴费月数");
		System.out.println("养老保险累计缴费月数："+pensionPayMonthTotal);
		String pensionCapital = getMsgByKeyword(html.getWebResponse().getContentAsString(),"养老保险个人帐户本金");
		System.out.println("养老保险个人帐户本金："+pensionCapital);
		String pensionPayCapital = getMsgByKeyword(html.getWebResponse().getContentAsString(),"养老保险个人缴费本金");
		System.out.println("养老保险个人缴费本金："+pensionPayCapital);
		String householdType = getMsgByKeyword(html.getWebResponse().getContentAsString(),"户口类型");
		System.out.println("户口类型："+householdType);
		String employmentForm = getMsgByKeyword(html.getWebResponse().getContentAsString(),"用工形式");
		System.out.println("用工形式："+employmentForm);
		String InsuranceType = getMsgByKeyword(html.getWebResponse().getContentAsString(),"社保状态");
		System.out.println("社保状态："+InsuranceType);
		String companySSN = getMsgByKeyword(html.getWebResponse().getContentAsString(),"单位社保号");
		System.out.println("单位社保号："+companySSN);
		String socialSecurityNum = getMsgByKeyword(html.getWebResponse().getContentAsString(),"社保卡号");
		System.out.println("社保卡号："+socialSecurityNum);
		String socialSecurityNumPerson = getMsgByKeyword(html.getWebResponse().getContentAsString(),"个人社保号");
		System.out.println("个人社保号："+socialSecurityNumPerson);
	}

	private static void loginByHtmlUnit(String url) throws Exception {
		HtmlPage html = getHtml1(url,webClient);
//		//获取登录名输入框
//		HtmlTextInput idnum = html.getFirstByXPath("//input[@id='i_dno']");
//		System.out.println("用户名输入框："+idnum.asXml());
//		//获取密码输入框
//		HtmlPasswordInput password = html.getFirstByXPath("//input[@id='password']");
//		System.out.println("密码输入框： "+password.asXml());
//		//获取图片验证码输入框
//		HtmlTextInput checkCode = html.getFirstByXPath("//input[@id='verifycode']");
//		System.out.println("验证码输入框： "+checkCode.asXml());
//		//获取登录按钮
//		HtmlButtonInput button = html.getFirstByXPath("//input[@class='textfield3']");
//		System.out.println("登录按钮 ： "+button.asXml());
		//获取图片验证码并保存在指定路径
		HtmlImage image = html.getFirstByXPath("//img[@id='getcode1']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		//超级鹰识别
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath());
		System.out.println("超级鹰识别之后的结果： "+chaoJiYingResult);
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code : "+code);
		
		
		String loginUrl = "https://www.zhldj.gov.cn/zhrsClient/login.do?password=82708256&id_card=440882199008258614&verifycode="+code+"&user_type=1&rand=0.0805975288931653";
		HtmlPage loginPage = getHtml1(loginUrl,webClient);
		System.out.println("登录页面："+loginPage.getWebResponse().getContentAsString());
		
		webClient = loginPage.getWebClient();
		//		//输入值
//		idnum.setText("440882199008258614");
//		password.setText("82708256");
//		checkCode.setText(code);
//		
//		HtmlPage page = button.click();
//		System.out.println("点击登录后返回的页面："+page.getWebResponse().getContentAsString());
		
//		webClient = page.getWebClient();
	}
	
	public static HtmlPage getHtml1(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	 public static String getMsgByKeyword(String html, String keyword) {
		 	Document document = Jsoup.parse(html);
	        Elements es = document.select("label:contains(" + keyword + ")");
	        if (null != es && es.size() > 0) {
	            Element element = es.first();
	            String text = element.nextElementSibling().attr("value");
	            return text;
	        }
	        return null;
	    }
	 
	 public static String getMsgByKeyword1(String html, String keyword) {
		 	Document document = Jsoup.parse(html);
	        Elements es = document.select("label:contains(" + keyword + ")");
	        if (null != es && es.size() > 0) {
	            Element element = es.first();
	            String text = element.nextElementSibling().text();
	            return text;
	        }
	        return null;
	    }

}
