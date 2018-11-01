package test.housingfund.shanghai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiPay;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiUserInfo;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduSummary;
import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiGeneral;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ShanghaiTest {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookie = login(webClient, "celina428", "ss123456");
			// webClient = addcookie(cookie);
//			getUserInfo();
//			getPay();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			// https://persons.shgjj.com/MainServlet?username=celina428&password=ss123456&imagecode=7375&SUBMIT.x=42&SUBMIT.y=15&password_md5=BAF0F0AFD319BF489C5FE7D5A44B7A52&ID=0
			e.printStackTrace();
		}
	}
	
	private static void getPay() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\pay.txt");
		String json = txt2String(file);
		List<HousingShanghaiPay> list = new ArrayList<HousingShanghaiPay>();
		HousingShanghaiPay housingShanghaiPay = null;
		
		Document doc = Jsoup.parse(json);
		
		Elements link1 = doc.getElementsByTag("tr");
		for (Element element : link1) {
			Elements link2 = element.getElementsByTag("td");
			if (link2.size() == 5) {
				String date = link2.get(0).text();
				if(!"日期".equals(date)){
					String company = link2.get(3).text();
					String money = link2.get(1).text();
					String description = link2.get(2).text();
					String reason = link2.get(4).text();
					housingShanghaiPay = new HousingShanghaiPay("", date, company, money, description, reason);
					list.add(housingShanghaiPay);
				}
			}
		}
		
		JSONArray jsonObject = JSONArray.fromObject(list);
		
		System.out.println(jsonObject);
		System.out.println(jsonObject.size());
		
	}
	

	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\loginEnd.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		
		String name = getNextLabelByKeyword(doc, "姓 名");
		String openingDate = getNextLabelByKeyword(doc, "开户日期");
		String housingNum = getNextLabelByKeyword(doc, "公积金账号");
		String company = getNextLabelByKeyword(doc, "所属单位");
		String lastDepositDate = getNextLabelByKeyword(doc, "末次缴存年月");
		String balance = getNextLabelByKeyword(doc, "账户余额");
		String monthlyDeposit = getNextLabelByKeyword(doc, "月缴存额");
		String accountState = getNextLabelByKeyword(doc, "当前账户状态");
		String phone = getNextLabelByKeyword(doc, "绑定手机号");
		String state = getNextLabelByKeyword(doc, "名认证状态");

		HousingShanghaiUserInfo housingShanghaiUserInfo = new HousingShanghaiUserInfo("", name, openingDate, housingNum,
				company, lastDepositDate, balance, monthlyDeposit, accountState, phone, state);
		
		JSONObject jsonObject = JSONObject.fromObject(housingShanghaiUserInfo);
		
		System.out.println(jsonObject.toString());
//		System.out.println(housingShanghaiUserInfo.toString());
	}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("div:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.parent().nextElementSibling();
			Elements elspan = nextElement.getElementsByTag("a");
			for (Element element2 : elspan) {
				element2.empty();
			}
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	public static String login(WebClient webClient, String username, String password) throws Exception {

		String url = "https://persons.shgjj.com/";
		HtmlPage searchPage = getHtmlPage(webClient, url, null);
		if (null != searchPage) {
			HtmlImage image = searchPage.getFirstByXPath("//img[@src='VerifyImageServlet']");
			try {
				String imageName = "111.jpg";
				File file = new File("D:\\img\\" + imageName);
				try {
					image.saveAs(file);
				} catch (Exception e) {
					System.out.println("图片有误");
				}
				// code = chaoJiYingOcrService.getVerifycode(image, "1902");

			} catch (Exception e) {
				e.printStackTrace();
			}

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[name='username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[name='username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("input[name='password']");
			if (inputPassword == null) {
				throw new Exception("password input text can not found :" + "input[name='password']");
			} else {
				inputPassword.reset();
				inputPassword.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[name='imagecode']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[name='imagecode']");
			} else {
				String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");
				inputuserjym.reset();
				inputuserjym.setText(inputuserjymtemp);
			}
			HtmlImageInput loginButton = (HtmlImageInput) searchPage.querySelector("input[name='SUBMIT']");
			// HtmlTextInput loginButton =
			// searchPage.getFirstByXPath("//input[@name='SUBMIT']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				// Page page = (HtmlPage)loginButton.click();
				HtmlPage page = (HtmlPage) loginButton.click();

				String login = page.getWebResponse().getContentAsString();
				HtmlFont font = page.querySelector(
						"form[name='loginform'] table tbody tr td table tbody tr td table tbody tr td div font[color='#CC0000']");
				if(login.contains("当年公积金账户查询")){
					
				}else{
					System.out.println("login----------" + font.asText());
				}
				// System.out.println("login----------"+login);
			}
		}
		String cookieJson = "";

		return cookieJson;

	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList,
			Boolean code) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}

		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();

		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
