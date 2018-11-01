package app.crawler.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiPay;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingShanghaiParse {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		tracer.addTag("HousingShanghaiParse.login", taskHousing.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {
			String url = "https://persons.shgjj.com/";
			HtmlPage searchPage = getHtmlPage(webClient, url, null);
			if (null != searchPage) {
				HtmlImage image = searchPage.getFirstByXPath("//img[@src='VerifyImageServlet']");
				String code = "";
				try {
					code = chaoJiYingOcrService.getVerifycode(image, "1902");
				} catch (Exception e) {
					tracer.addTag("HousingShanghaiParse.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
					e.printStackTrace();
				}

				HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[name='username']");
				if (inputUserName == null) {
					throw new Exception("username input text can not found :" + "input[name='username']");
				} else {
					inputUserName.reset();
					inputUserName.setText(messageLoginForHousing.getNum());
				}
				HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage
						.querySelector("input[name='password']");
				if (inputPassword == null) {
					throw new Exception("password input text can not found :" + "input[name='password']");
				} else {
					inputPassword.reset();
					inputPassword.setText(messageLoginForHousing.getPassword());
				}
				HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[name='imagecode']");
				if (inputuserjym == null) {
					throw new Exception("code input text can not found :" + "input[name='imagecode']");
				} else {
					inputuserjym.reset();
					inputuserjym.setText(code);
				}
				HtmlImageInput loginButton = (HtmlImageInput) searchPage.querySelector("input[name='SUBMIT']");
				if (loginButton == null) {
					throw new Exception("login button can not found : null");
				} else {
					HtmlPage page = (HtmlPage) loginButton.click();
					webClient.waitForBackgroundJavaScript(10000);
					String contentAsString = page.getWebResponse().getContentAsString();
					if(contentAsString.contains("当年公积金账户查询")){
						
					}else{
						HtmlFont font = page.querySelector(
								"form[name='loginform'] table tbody tr td table tbody tr td table tbody tr td div font[color='#CC0000']");
						
						String asText = font.asText();
						webParam.setText(asText);
					}
					String cookies = CommonUnit.transcookieToJson(page.getWebClient());
					webParam.setCookies(cookies);
					webParam.setHtml(page.getWebResponse().getContentAsString());
					return webParam;
				}
			}
		} catch (Exception e) {
			tracer.addTag("HousingShanghaiParse.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;

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
	 * 解析用户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public HousingShanghaiUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingShanghaiParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");

		try {
			Document doc = Jsoup.parse(html);

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

			HousingShanghaiUserInfo housingShanghaiUserInfo = new HousingShanghaiUserInfo(taskHousing.getTaskid(), name,
					openingDate, housingNum, company, lastDepositDate, balance, monthlyDeposit, accountState, phone,
					state);

			return housingShanghaiUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingShanghaiParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return null;

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

	public WebClient addcookie(TaskHousing taskHousing) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
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
	public Page getPage(WebClient webClient, TaskHousing taskHousing, String url, HttpMethod type,
			List<NameValuePair> paramsList, Boolean code) throws Exception {
		tracer.addTag("HousingShanghaiParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingShanghaiParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingShanghaiParse.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}


	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<HousingShanghaiPay> getPay(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingShanghaiParse.getPay", taskHousing.getTaskid());

		WebParam<HousingShanghaiPay> webParam = new WebParam<HousingShanghaiPay>();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "https://persons.shgjj.com/MainServlet?ID=11";

			Page page = getPage(webClient, taskHousing, url, null, null, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingShanghaiParse.getPay---缴费信息" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");
				List<HousingShanghaiPay> list = htmlPayParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingShanghaiParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<HousingShanghaiPay> htmlPayParser(String html, TaskHousing taskHousing) {

		List<HousingShanghaiPay> list = new ArrayList<HousingShanghaiPay>();
		try {
			HousingShanghaiPay housingShanghaiPay = null;
			
			Document doc = Jsoup.parse(html);
			
			Elements link1 = doc.getElementsByTag("tr");
			for (Element element : link1) {
				Elements link2 = element.getElementsByTag("td");
				if (link2.size() == 5) {
					String date = link2.get(0).text();
					if(!"日期".equals(date)){
						String company = link2.get(1).text();
						String money = link2.get(2).text();
						String description = link2.get(3).text();
						String reason = link2.get(4).text();
						housingShanghaiPay = new HousingShanghaiPay(taskHousing.getTaskid(), date, company, money, description, reason);
						list.add(housingShanghaiPay);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingShanghaiParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

}
