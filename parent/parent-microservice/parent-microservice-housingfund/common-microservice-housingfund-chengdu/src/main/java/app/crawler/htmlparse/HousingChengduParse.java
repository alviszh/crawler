package app.crawler.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduPay;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class HousingChengduParse {

	@Autowired
	private TracerLog tracer;
	
	@Value("${datasource.driverPath}")
	public String driverPath;

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing) {

		WebParam webParam = new WebParam();
		try {
			ChromeDriver driver = intiChrome();
			driver.manage().window().maximize();

			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
			String baseUrl = "https://www.cdzfgjj.gov.cn:9802/cdnt/login.jsp#per";
			driver.get(baseUrl);
			String adrOptionString = "#aType option[value='4']";
			if(StatusCodeLogin.ACCOUNT_NUM.equals(messageLoginForHousing.getLogintype())){
				adrOptionString = "#aType option[value='3']";
			}else if(StatusCodeLogin.CO_BRANDED_CARD.equals(messageLoginForHousing.getLogintype())){
				adrOptionString = "#aType option[value='5']";
			}

			WebElement adrOption = driver.findElement(By.cssSelector(adrOptionString));
			adrOption.click();
			Thread.sleep(1000);
			driver.findElement(By.id("j_username")).sendKeys(messageLoginForHousing.getNum());
			Thread.sleep(1000);
			driver.findElement(By.id("j_password")).sendKeys(messageLoginForHousing.getPassword());
			Thread.sleep(1000);

			Actions action = new Actions(driver);
			// 获取滑动滑块的标签元素
			WebElement source = driver.findElement(By.xpath("//div[@class='Slider ui-draggable']"));
			// 确保每次拖动的像素不同，故而使用随机数
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
			Thread.sleep(2000);
			action.clickAndHold(source).moveByOffset((int) (Math.random() * 10) + 80, 0);
			Thread.sleep(2000);
			// 拖动完释放鼠标
			action.moveToElement(source).release();
			// 组织完这些一系列的步骤，然后开始真实执行操作
			Action actions = action.build();
			actions.perform();
			Thread.sleep(3000);
			driver.findElement(By.id("btn-login")).click();
			Thread.sleep(5000);
			String text = "";
			try {
				text = driver.findElement(By.id("error")).getText();
			} catch (Exception e) {
				text = "";
				tracer.addTag("HousingChengduParse.login.By.id(error)):应该是登录成功",
						messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
				e.printStackTrace();
			}
			if (text == null || "".equals(text)) {
				
				try {
					Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
							.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
					WebElement ele1 = wait.until(new Function<WebDriver, WebElement>() {
						public WebElement apply(WebDriver driver) {
							return driver.findElement(By.id("259333"));
						}
					});
				} catch (Exception e) {
					tracer.addTag("HousingChengduParse.login.Wait",
							messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
					e.printStackTrace();
					//特殊情况登录失败抓取错误信息
					try {
						text = driver.findElement(By.id("error")).getText();
					} catch (Exception e1) {
						text = "";
						tracer.addTag("HousingChengduParse.login.Wait.By.id(error)):应该是登录失败",
								messageLoginForHousing.getTask_id() + "---ERROR:" + e1.toString());
						e.printStackTrace();
					}
				}
				// 获取cookies
				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
				Set<CookieJson> cookiesSet = new HashSet<CookieJson>();
				for (org.openqa.selenium.Cookie cookie : cookies) {
					CookieJson cookiejson = new CookieJson();
					cookiejson.setDomain(cookie.getDomain());
					cookiejson.setKey(cookie.getName());
					cookiejson.setValue(cookie.getValue());
					cookiesSet.add(cookiejson);
				}
				String cookieJson = new Gson().toJson(cookiesSet);
				// 借用个url传递cookie
				webParam.setCookies(cookieJson);
			}
			// html传递text
			webParam.setHtml(text);
			driver.quit();
			return webParam;
		} catch (Exception e) {
			tracer.addTag("HousingChengduParse.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
			
		}

		return null;

	}

	public  ChromeDriver intiChrome() throws Exception {
//		String driverPath = "/opt/selenium/chromedriver-2.31";
		System.setProperty("webdriver.chrome.driver", driverPath);
		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		// chromeOptions.addArguments("--headless");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1980,1068");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}

	/**
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingChengduParse.getUserInfo", taskHousing.getTaskid());

		WebParam webParam = new WebParam();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "https://www.cdzfgjj.gov.cn:9802/cdnt/per/querPerInfo.do?menuid=259333";

			Page page = getPage(webClient, taskHousing, url, null, null, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingChengduParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingChengduUserInfo housingChengduUserInfo = htmlUserInfoParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setHousingChengduUserInfo(housingChengduUserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingChengduParse.getUserInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	private HousingChengduUserInfo htmlUserInfoParser(String html, TaskHousing taskHousing) {

		try {
			Document doc = Jsoup.parse(html);
			// 个人账号
			String percode = doc.getElementById("percode").val();

			// 个人姓名
			String pername1 = doc.getElementById("pername1").val();

			// 证件类型
			String idtype = "";
			if (html.contains("slectInput_idtype[0]")) {
				String slectInput_idtype = html.substring(html.indexOf("slectInput_idtype[0]"));
				String[] split = slectInput_idtype.split("\"");
				if (split.length > 1) {
					idtype = split[1];
				}
			}

			// 证件号码
			String idcard1 = doc.getElementById("idcard1").val();

			// 出生日期
			String birthday = doc.getElementById("birthday").val();

			// 个人邮箱
			String mail = doc.getElementById("mail").val();

			// 移动电话
			String phone1 = doc.getElementById("phone1").val();

			// 面签状态
			String signflag_desc = "";
			if (html.contains("slectInput_signflag[0]")) {
				String slectInput_idtype = html.substring(html.indexOf("slectInput_signflag[0]"));
				String[] split = slectInput_idtype.split("\"");
				if (split.length > 1) {
					signflag_desc = split[1];
				}
			}

			// 发卡银行
			String banktype = "";
			if (html.contains("slectInput_banktype[0]")) {
				String slectInput_idtype = html.substring(html.indexOf("slectInput_banktype[0]"));
				String[] split = slectInput_idtype.split("\"");
				if (split.length > 1) {
					banktype = split[1];
				}
			}

			// 联名卡卡号
			String bankcode = doc.getElementById("bankcode").val();

			// 缴存基数
			String basemny = "";
			if (html.contains("\"basemny\":\"")) {
				String slectInput_idtype = html.substring(html.indexOf("\"basemny\":\""));
				String[] split = slectInput_idtype.split("\"");
				if (split.length > 3) {
					basemny = split[3];
				}
			}

			HousingChengduUserInfo housingChengduUserInfo = new HousingChengduUserInfo(taskHousing.getTaskid(), percode,
					pername1, idtype, idcard1, birthday, mail, phone1, signflag_desc, banktype, bankcode, basemny);

			return housingChengduUserInfo;

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingChengduParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
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
		tracer.addTag("HousingChengduParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingChengduParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingChengduParse.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}

	/**
	 * 获取时间集合
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public List<String> getPaylist(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingChengduParse.getPaylist", taskHousing.getTaskid());

		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "https://www.cdzfgjj.gov.cn:9802/cdnt/per/depositRecordQueryAction.do?menuid=259597";
			Page html = getPage(webClient, taskHousing, url, null, null, false);
			String json = html.getWebResponse().getContentAsString();
			tracer.addTag("HousingChengduParse.getPaylist---Datahtml" + taskHousing.getTaskid(),
					"<xmp>" + json + "</xmp>");
			List<String> list = new ArrayList<String>();
			if (json.contains("yearList = $.parseJSON('")) {
				String yearList = json.substring(json.indexOf("yearList = $.parseJSON('"));
				String[] split = yearList.split("'");
				if (split.length > 1) {
					String datajson = split[1];
					Object obj = new JSONTokener(datajson).nextValue();
					if (obj instanceof JSONObject) {
						JSONObject jsonObject = (JSONObject) obj;
						String data = jsonObject.getString("id");
						list.add(data);

					} else if (obj instanceof JSONArray) {
						JSONArray jsonArray = (JSONArray) obj;
						for (Object object : jsonArray) {
							JSONObject jsonObject = JSONObject.fromObject(object);
							String data = jsonObject.getString("id");
							list.add(data);
						}
					}
				}
			}
			tracer.addTag("HousingChengduParse.getPaylist---list:" + taskHousing.getTaskid(), list.toString());
			return list;
		} catch (Exception e) {
			tracer.addTag("HousingChengduParse.getPaylist---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	public WebParam<HousingChengduPay> getPay(TaskHousing taskHousing,String data) throws Exception {

		tracer.addTag("HousingChengduParse.getPay", taskHousing.getTaskid());

		WebParam<HousingChengduPay> webParam = new WebParam<HousingChengduPay>();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "https://www.cdzfgjj.gov.cn:9802/cdnt/per/depositRecordQueryAction!queryAepositDetailInfo.do?dto%5B'year'%5D="+data+"&&";

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, null, false);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingChengduParse.getPay---缴费信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingChengduPay> list = htmlPayParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingChengduParse.getPay---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
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
	private List<HousingChengduPay> htmlPayParser(String html, TaskHousing taskHousing) {

		List<HousingChengduPay> list = new ArrayList<HousingChengduPay>();
		try {
			HousingChengduPay housingChengduPay = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONObject listsJson = jsonObj.getJSONObject("lists");
			JSONObject dataListJson = listsJson.getJSONObject("dataList");
			String listJson = dataListJson.getString("list");
			Object obj = new JSONTokener(listJson).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 缴存年月起
				String paybmnh = jsonObject.getString("paybmnh");
				// 缴存年月止
				String payemnh = jsonObject.getString("payemnh");
				// 缴款时间
				String acctime = jsonObject.getString("acctime");
				// 单位缴存额(元)
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人缴存额(元)
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计(元)
				String depmny = jsonObject.getString("depmny");
				// 业务类型 '1':'汇缴' '2':'补缴'
				String bustype = jsonObject.getString("bustype");
				// 单位账号
				String corpcode = jsonObject.getString("corpcode");
				housingChengduPay = new HousingChengduPay(taskHousing.getTaskid(), corpname, paybmnh, payemnh, acctime, corpdepmny.trim(),
						perdepmny.trim(), depmny.trim(), bustype, corpcode);
				list.add(housingChengduPay);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 单位名称
					String corpname = jsonObject.getString("corpname");
					// 缴存年月起
					String paybmnh = jsonObject.getString("paybmnh");
					// 缴存年月止
					String payemnh = jsonObject.getString("payemnh");
					// 缴款时间
					String acctime = jsonObject.getString("acctime");
					// 单位缴存额(元)
					String corpdepmny = jsonObject.getString("corpdepmny");
					// 个人缴存额(元)
					String perdepmny = jsonObject.getString("perdepmny");
					// 合计(元)
					String depmny = jsonObject.getString("depmny");
					// 业务类型 '1':'汇缴' '2':'补缴'
					String bustype = jsonObject.getString("bustype");
					// 单位账号
					String corpcode = jsonObject.getString("corpcode");
					housingChengduPay = new HousingChengduPay(taskHousing.getTaskid(), corpname, paybmnh, payemnh, acctime, corpdepmny.trim(),
							perdepmny.trim(), depmny.trim(), bustype, corpcode);
					list.add(housingChengduPay);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingChengduParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

}
