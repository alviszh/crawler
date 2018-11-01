package app;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLogin extends AbstractChaoJiYingHandler {
	
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	static Gson gson = new GsonBuilder().create();
	static WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	
	public static void main(String[] args) {
		String loginUrl = "http://www.smsic.cn:8080/sheB/index.jsp";
		try {
			loginByHtmlunit(loginUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String userinfoUrl = "http://www.smsic.cn:8080/sheB/login.do?action=6";
//		try {
//			getUserinfo(userinfoUrl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		String pensionUrl = "http://www.smsic.cn:8080/sheB/sboper.do?action=6";
//		try {
//			getUserinfo(pensionUrl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		String pensionUrl = "http://www.smsic.cn:8080/sheB/ac10a.do?action=0";
//		try {
//			getPension(pensionUrl);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String injuryUrl = "http://www.smsic.cn:8080/sheB/gssy.do?action=0";
		try {
			getInjury(injuryUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void getInjury(String injuryUrl) throws Exception {
		HtmlPage html = getHtml(injuryUrl,webClient);
		System.out.println("=================养老基本信息=====================");
		String page = html.getWebResponse().getContentAsString();
		System.out.println(html.getWebResponse().getContentAsString());
		
		Document doc = Jsoup.parse(page);
		String pageCount = doc.getElementById("RecuSetCountys").attr("value");
		
		System.out.println("总页数为："+pageCount);
		
		for(int i = 1;i<=8;i++){
			
			List<String> params = getInjuryDetail(injuryUrl,pageCount,i);
		}
		
	}


	private static List<String> getInjuryDetail(String injuryUrl, String pageCount, int pageSize) throws Exception {
		
		WebRequest requestSettings = new WebRequest(new URL(injuryUrl), HttpMethod.POST); 
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		
		requestSettings.setAdditionalHeader("Referer", "http://www.smsic.cn:8080/sheB/gssy.do?action=0");
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "www.smsic.cn:8080"); 
		requestSettings.setAdditionalHeader("Origin", "http://www.smsic.cn:8080");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//		
//		requestSettings.getRequestParameters().add(new NameValuePair("callback", "jQuery183008660428427514177_"+String.valueOf(System.currentTimeMillis())));
//		requestSettings.getRequestParameters().add(new NameValuePair("_", String.valueOf(System.currentTimeMillis())));		
		
		String requestBody = "pageCountys=10&RecuSetCountys="+pageCount+"&pageIndexys="+pageSize+"&aae140=-1&page=1";		
		requestSettings.setRequestBody(requestBody);
		
		
		Page page = webClient.getPage(requestSettings); 						
		String html = page.getWebResponse().getContentAsString();
		
		System.out.println("===================第"+pageSize+"页=====================");
		System.out.println(html);
		
		List<String> params = parserParam(html);
		
		return params;
	}


	private static List<String> parserParam(String html) throws Exception {
		Document doc1 = Jsoup.parse(html);
		Elements inputs = doc1.select("[value=详细]");
		String onClick = inputs.get(0).attr("onclick");
		String code = onClick.substring(onClick.indexOf("\'")+1, onClick.length()-2).trim();
		System.out.println(code);
		
		String url = "http://www.smsic.cn:8080/sheB/gssy.do?action=1&id="+code;
		HtmlPage page = getHtml(url,webClient);
		System.out.println("=================工伤保险基本信息=====================");
		String detailHtml = page.getWebResponse().getContentAsString();
		System.out.println(detailHtml);
		
		Document doc = Jsoup.parse(detailHtml);
		String businessNum = doc.select("nobr:contains(业务流水号)").first().parent().parent().nextElementSibling().text();
		String personCode = doc.select("span:contains(个人编号)").first().parent().nextElementSibling().text();
		String insuranceType = doc.select("span:contains(险种类型)").first().parent().nextElementSibling().text();
		String payType = doc.select("span:contains(缴费类型)").first().parent().nextElementSibling().text();
		String companyName = doc.select("nobr:contains(单位名称)").first().parent().parent().nextElementSibling().text();
		String declarationDate = doc.select("span:contains(申报年月)").first().parent().nextElementSibling().text();
		String payWay = doc.select("nobr:contains(缴费方式)").first().parent().parent().nextElementSibling().text();
		String companyPayCardinal = doc.select("span:contains(单位缴费基数)").first().parent().nextElementSibling().text();
		String personPayCardinal = doc.select("span:contains(个人缴费基数)").first().parent().nextElementSibling().text();
		String companyPayScale = doc.select("nobr:contains(单位缴费比例)").first().parent().parent().nextElementSibling().text();
		String personPayScale = doc.select("span:contains(个人缴费比例)").first().parent().nextElementSibling().text();
		String companyPayMoney = doc.select("span:contains(应缴金额单位)").first().parent().nextElementSibling().text();
		String personPayMoney = doc.select("span:contains(应缴金额个人)").first().parent().nextElementSibling().text();
		String payMoney = doc.select("nobr:contains(应缴金额)").first().parent().parent().nextElementSibling().text();
		String noticeOrder = doc.select("span:contains(通知单流水号)").first().parent().nextElementSibling().text();
		
		System.out.println("业务流水号："+businessNum);
		System.out.println("个人编号："+personCode);
		System.out.println("险种类型："+insuranceType);
		System.out.println("缴费类型："+payType);
		System.out.println("单位名称："+companyName);
		System.out.println("申报年月："+declarationDate);
		System.out.println("缴费方式："+payWay);
		System.out.println("单位缴费基数："+companyPayCardinal);
		System.out.println("个人缴费基数："+personPayCardinal);
		System.out.println("单位缴费比例："+companyPayScale);
		System.out.println("个人缴费比例："+personPayScale);
		System.out.println("应缴金额单位："+companyPayMoney);
		System.out.println("应缴金额个人："+personPayMoney);
		System.out.println("应缴金额："+payMoney);
		System.out.println("通知单流水号："+noticeOrder);

		
		
		return null;
	}


	private static void getPension(String pensionUrl) throws Exception {
		HtmlPage html = getHtml(pensionUrl,webClient);
		System.out.println("=================养老基本信息=====================");
		String page = html.getWebResponse().getContentAsString();
		System.out.println(html.getWebResponse().getContentAsString());
		
		Document doc = Jsoup.parse(page);
		Elements inputs = doc.select("[value=详细]");
		String onClick = inputs.get(0).attr("onclick");
		String code = onClick.substring(onClick.indexOf("\'")+1, onClick.length()-2).trim();
		System.out.println(code);
		
		String detail = "http://www.smsic.cn:8080/sheB/ac10a.do?action=1&baeah2="+code;
		HtmlPage detailHtml = getHtml(detail,webClient);
		System.out.println("==================养老信息缴纳详情=================");
		System.out.println(detailHtml.getWebResponse().getContentAsString());
		
		parserDetail(detailHtml.getWebResponse().getContentAsString());
		
	}


	private static void parserDetail(String contentAsString) {
		Document doc = Jsoup.parse(contentAsString);
		String accountMonthly = doc.select("span:contains(到帐年月)").first().parent().nextElementSibling().text();
		String declarationDate = doc.select("span:contains(申报日期)").first().parent().nextElementSibling().text();
		String companyCode = doc.select("span:contains(单位编号)").first().parent().nextElementSibling().text();
		String payMonth = doc.select("span:contains(缴费月数)").first().parent().nextElementSibling().text();
		String beginDate = doc.select("span:contains(费款所属起始日期)").first().parent().nextElementSibling().text();
		String endDate = doc.select("span:contains(费款所属截止日期)").first().parent().nextElementSibling().text();
		String companyPayCardinal = doc.select("span:contains(单位缴费基数)").first().parent().nextElementSibling().text();
		String companyPayScale = doc.select("span:contains(单位缴费比例)").first().parent().nextElementSibling().text();
		String ordinatingMoney = doc.select("span:contains(统筹金额)").first().parent().nextElementSibling().text();
		String personPayCardinal = doc.select("span:contains(个人缴费基数)").first().parent().nextElementSibling().text();
		String personPayScale = doc.select("span:contains(个人缴费比例)").first().parent().nextElementSibling().text();
		String personAccountMoney = doc.select("span:contains(个人账户金额)").first().parent().nextElementSibling().text();
		String Ashare = doc.select("span:contains(个帐比例)").first().parent().nextElementSibling().text();
		String companyPartMoney = doc.select("span:contains(单位划拨部分金额)").first().parent().nextElementSibling().text();
		String totalMoney = doc.select("span:contains(实缴总金额)").first().parent().nextElementSibling().text();

		System.out.println("到账年月："+accountMonthly);
		System.out.println("申报日期："+declarationDate);
		System.out.println("单位编号："+companyCode);
		System.out.println("缴费月数："+payMonth);
		System.out.println("费款所属起始日期："+beginDate);
		System.out.println("费款所属截止日期："+endDate);
		System.out.println("单位缴费基数："+companyPayCardinal);
		System.out.println("单位缴费比例："+companyPayScale);
		System.out.println("统筹金额："+ordinatingMoney);
		System.out.println("个人缴费基数："+personPayCardinal);
		System.out.println("个人缴费比例："+personPayScale);
		System.out.println("个人账户金额："+personAccountMoney);
		System.out.println("个帐比例："+Ashare);
		System.out.println("单位划拨部分金额："+companyPartMoney);
		System.out.println("实缴总金额："+totalMoney);

		
	}


	private static void getUserinfo(String userinfoUrl) throws Exception {
		HtmlPage html = getHtml(userinfoUrl,webClient);
		System.out.println("=================个人信息=====================");
//		System.out.println(html.getWebResponse().getContentAsString());
		
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
		Element table = doc.select("[bgcolor=#71a7d9]").first();
		Elements trs = table.select("tr");
		Element tr = trs.get(3);
		System.out.println(tr.toString());
		String idnum = tr.child(1).child(0).text();
		String name = tr.child(3).child(0).text();
		
		System.out.println("身份证号："+idnum);
		System.out.println("姓名："+name);
		
		tr = trs.get(4);	
		String address = tr.child(1).child(0).text();		
		System.out.println("通讯地址："+address);
		
		tr = trs.get(5);
		String phone = tr.child(1).child(0).text();
		String postcode = tr.child(3).child(0).text();
		
		System.out.println("联系电话："+phone);
		System.out.println("邮政编码："+postcode);
		
		tr = trs.get(6);
		String companyCode = tr.child(1).child(0).text();
		String companyName = tr.child(3).child(0).text();
		
		System.out.println("单位编码："+companyCode);
		System.out.println("单位名称："+companyName);
		
		tr = trs.get(7);
		String birthday = tr.child(1).child(0).text();
		String wordType = tr.child(3).child(0).text();
		
		System.out.println("出生年月："+birthday);
		System.out.println("用工形式："+wordType);
		
		tr = trs.get(8);
		String workTime = tr.child(1).child(0).text();
		String pensionMonth = tr.child(3).child(0).text();
		
		System.out.println("参加工作年月："+workTime);
		System.out.println("参加养老保险年月："+pensionMonth);
		
		tr = trs.get(9);
		String payMonth = tr.child(1).child(0).text();
		String beginMonth = tr.child(3).child(0).text();
		
		System.out.println("建账前累计缴费月数："+payMonth);
		System.out.println("建立个人账户年月："+beginMonth);
		
		tr = trs.get(11);
		String payMonthEnd = tr.child(1).child(0).text();
		String payBaseTotal = tr.child(3).child(0).text();
		
		System.out.println("建帐后累计缴费月数："+payMonthEnd);
		System.out.println("缴费总基数："+payBaseTotal);
		
		tr = trs.get(12);
		String accountTotalMoney = tr.child(1).child(0).text();
		String myNum = tr.child(3).child(0).text();
		
		System.out.println("个人账户总金额："+accountTotalMoney);
		System.out.println("个人编号："+myNum);
		
		
	}


	private static void loginByHtmlunit(String loginUrl) throws Exception {
		
		for(int i = 0;i<4;i++){
			HtmlPage html = getHtml(loginUrl,webClient);
			Thread.sleep(10000);
			System.out.println(html.getWebResponse().getContentAsString());
			//获取登录名输入框
			HtmlTextInput idnum = html.getFirstByXPath("//input[@id='card']");
			System.out.println("用户名输入框："+idnum.asXml());
			//获取密码输入框
			HtmlPasswordInput password = html.getFirstByXPath("//input[@id='pwd']");
			System.out.println("密码输入框： "+password.asXml());
			//获取图片验证码输入框
			HtmlTextInput checkCode = html.getFirstByXPath("//input[@id='code']");
			System.out.println("验证码输入框： "+checkCode.asXml());
			//获取登录按钮
			HtmlImageInput button = html.getFirstByXPath("//input[@type='image']");
			System.out.println("登录按钮 ： "+button.asXml());
			//获取图片验证码并保存在指定路径
			HtmlImage image = html.getFirstByXPath("//img[@src='/sheB/jsp/code/image.jsp']");
			System.out.println("图片验证码："+image.asXml());
			String imageName = UUID.randomUUID() + ".jpg";
			File file = new File("D:\\img\\"+imageName);
			image.saveAs(file);
			//超级鹰识别
			String chaoJiYingResult = getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, file.getAbsolutePath());
			System.out.println("超级鹰识别之后的结果： "+chaoJiYingResult);
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code : "+code);
			
			if(StringUtils.isBlank(code)){
				continue;
			}else{
				//输入值
				idnum.setText("362323199404066512");
				password.setText("ls123456");
				checkCode.setText(code);
				
				HtmlPage page = (HtmlPage) button.click();
				System.out.println("点击登录后返回的页面："+page.getWebResponse().getContentAsString());
				webClient = page.getWebClient();
				break;
			}			
		}
		
	}


	public static HtmlPage getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;		
	}
}
