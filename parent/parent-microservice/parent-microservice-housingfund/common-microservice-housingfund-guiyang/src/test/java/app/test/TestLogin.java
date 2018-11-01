package app.test;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
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
		
		String loginUrl = "http://zxcx.gygjj.gov.cn";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = getHtml(loginUrl,webClient);
		System.out.println("*************************************************登录起始页面");
		System.out.println(page.asXml());
		
//		HtmlTextInput username = page.getFirstByXPath("//input[@id='IdCard']");
//		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='PassWord']");
//		HtmlTextInput code = (HtmlTextInput)page.getFirstByXPath("//input[@id='Ed_Confirmation']");
//		HtmlImage button = (HtmlImage)page.getFirstByXPath("//img[@src='/images/login.png']");
		
		HtmlImage image = page.getFirstByXPath("//img[@id='rand']");
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
		
		String capton = callChaoJiYingService(file.getAbsolutePath(),"1902");
		
//		username.setText("120103198304271124");
//		password.setText("500600");
//		code.setText(capton);
//		HtmlPage adminPage = (HtmlPage) button.click();
//		
//		System.out.println("**********************************************登录后页面");
//		System.out.println(adminPage.asXml());
		
		String admingUrl = "http://zxcx.gygjj.gov.cn/checklogin.do?method=login&aaxmlrequest=true&logintype=person&spcode=&fromtype=null&IsCheckVerifyCode=On&IdCard=120103198304271124&PassWord=500600&Ed_Confirmation="+capton+"&";
		Page adminPage = getHtml1(admingUrl,webClient);
		System.out.println("**********************************************首页数据");
		System.out.println(adminPage.getWebResponse().getContentAsString());
		
//		String userinfoUrl = "http://zxcx.gygjj.gov.cn/PersonBaseInfo.do?method=view";
//		HtmlPage userPage = (HtmlPage) getHtml1(userinfoUrl,webClient);
//		System.out.println("**********************************************用户信息数据");
//		System.out.println(userPage.asXml());
//		
//		parserUser(userPage.asXml());
//		
//		
		String detailUrl = "http://zxcx.gygjj.gov.cn/PersonAccountsList.do?method=list&aaxmlrequest=true&startTime_a=2000-01-01&startTime_b=2017-10-19&";
		XmlPage detailPage = (XmlPage) getHtml1(detailUrl,webClient);
		System.out.println("**********************************************公积金明细数据");
//		System.out.println(detailPage.asXml());
		parserAccount(detailPage.asXml());
	
	}
	
	private static void parserAccount(String asXml) {
		
		Document doc = Jsoup.parse(asXml);
//		System.out.println(doc.toString());
		String html = doc.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		Document doc1 = Jsoup.parse(html);
		System.out.println(doc1.toString());
		Elements trs = doc1.getElementsByClass("grid-tr-data");
		int i=1;
		if(null != trs && trs.size()>0){
			for(Element tr : trs){
				String accountDate = tr.child(0).text();
				String processType = tr.child(1).text();
				String payYear = tr.child(2).text();
				String income = tr.child(3).text();
				String expend = tr.child(4).text();
				String balance = tr.child(5).text();
				
				System.out.println("******************************************************   "+i);
				System.out.println("时间 : "+accountDate);
				System.out.println("处理类型 : "+processType);
				System.out.println("汇缴年月 : "+payYear);
				System.out.println("收入 : "+income);
				System.out.println("支出 : "+expend);
				System.out.println("余额 : "+balance);
				i++;

			}
		}
		
	}

	@SuppressWarnings("unused")
	private static void parserUser(String asXml) {
		Document doc = Jsoup.parse(asXml);
		
		String company = getElementText(doc,"td:contains(单位名称)");
		String companyAccount = getElementText(doc,"td:contains(单位帐号)");	
		Element e = doc.getElementsByClass("table-whole").first();
		String personalAccount = getNextElementText(e,"td:contains(个人公积金帐号)");
		String name = getNextElementText(e,"td:contains(姓名)");
		String idnum = getNextElementText(e,"td:contains(身份证号)");
		String sex = getNextElementText(e,"td:contains(性别)");
		String phoneNum = getNextElementText(e,"td:contains(手机号)");
		String cardNum = getNextElementText(e,"td:contains(卡号)");
		String wageBase = getNextElementText(e,"td:contains(工资基数)");
		String depositAmount = getNextElementText(e,"td:contains(月应缴额)");
		String companyPayPercent = getNextElementText(e,"td:contains(单位缴存比例)");
		String employeePayPercent = getNextElementText(e,"td:contains(职工缴存比例)");
		String companyDepositAmount = getNextElementText(e,"td:contains(单位月应缴存额)");
		String employeeDepositAmount = getNextElementText(e,"td:contains(职工月应缴额)");
		String openDate = getNextElementText(e,"td:contains(开户日期)");
		String startYear = getNextElementText(e,"td:contains(起缴年月)");
		String employeePayYear = getNextElementText(e,"td:contains(职工汇缴年月)");
		String remitStatus = getNextElementText(e,"td:contains(汇缴状态)");
		String managementDepartment = getNextElementText(e,"td:contains(所属管理部)");
		String closingAccountDate = getNextElementText(e,"td:contains(汇缴销户时间)");
		String isFreeze = getNextElementText(e,"td:contains(是否冻结)");
		String balance = getNextElementText(e,"td:contains(当前余额)");
		String isLoans = getNextElementText(e,"td:contains(是否贷款)");
		String companyOperator = getNextElementText(e,"td:contains(单位经办人)");
		String companyLegalPerson = getNextElementText(e,"td:contains(单位法人)");
		String companyAddress = getNextElementText(e,"td:contains(单位地址)");
		
		System.out.println("单位名称 : "+company);
		System.out.println("单位帐号 : "+companyAccount);
		System.out.println("个人公积金帐号 : "+personalAccount);
		System.out.println("姓名 : "+name);
		System.out.println("身份证号 : "+idnum);
		System.out.println("性别 : "+sex);
		System.out.println("手机号 : "+phoneNum);
		System.out.println("卡号 : "+cardNum);
		System.out.println("工资基数 : "+wageBase);
		System.out.println("月应缴额 : "+depositAmount);
		System.out.println("单位缴存比例 : "+companyPayPercent);
		System.out.println("职工缴存比例 : "+employeePayPercent);
		System.out.println("单位月应缴存额 : "+companyDepositAmount);
		System.out.println("职工月应缴额 : "+employeeDepositAmount);
		System.out.println("开户日期 : "+openDate);
		System.out.println("起缴年月 : "+startYear);
		System.out.println("职工汇缴年月 : "+employeePayYear);
		System.out.println("汇缴状态 : "+remitStatus);
		System.out.println("所属管理部 : "+managementDepartment);
		System.out.println("汇缴销户时间 : "+closingAccountDate);
		System.out.println("是否冻结 : "+isFreeze);
		System.out.println("当前余额 : "+balance);
		System.out.println("是否贷款 : "+isLoans);
		System.out.println("单位经办人 : "+companyOperator);
		System.out.println("单位法人 : "+companyLegalPerson);
		System.out.println("单位地址 : "+companyAddress);


	}

	public static HtmlPage getHtml(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	public static Page getHtml1(String url,WebClient webClient) throws Exception{
//		WebClient webClient = WebCrawler.getInstance().getWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
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
	
	public static String getElementText(Document doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			String text = es.first().text();
			return text;
		}else{
			return null;
		}			
	}

	public static String getNextElementText(Element doc, String rule){
		
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			Element text = es.first().nextElementSibling();
			return text.text();
		}
	
		return null;
	}
		

}
