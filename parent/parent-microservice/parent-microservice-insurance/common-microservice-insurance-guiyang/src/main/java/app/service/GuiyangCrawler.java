package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangBuChongYibaoInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangDaeInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangGongshangInfoqqq;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangHtml;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangJiGuanYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangJuminYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShengyuInfoqqq;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangShiyeInfoqqq;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYanglaoInfoqqq;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYibaoInfoqqq;
import com.microservice.dao.entity.crawler.insurance.guiyang.InsuranceGuiyangYiliaozhuruInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangBuchongYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangDaeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangGongshangInfoRepositoryqqq;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangJiGuanYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangJuminYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangShengyuInfoRepositoryqqq;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangShiyeInfoRepositoryqqq;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangYanglaoInfoRepositoryqqq;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangYibaoInfoRepositoryqqq;
import com.microservice.dao.repository.crawler.insurance.guiyang.InsuranceGuiyangYiliaoZhuruInfoRepository;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 贵阳社保爬取
 * 
 * @author qizhongbin
 *
 */
@Component
public class GuiyangCrawler {

	public static final Logger log = LoggerFactory.getLogger(GuiyangCrawler.class);

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceGuiyangHtmlRepository insuranceGuiyangHtmlRepository;
	@Autowired
	private InsuranceGuiyangBaseInfoRepository insuranceGuiyangBaseInfoRepository;
	@Autowired
	private InsuranceGuiyangYanglaoInfoRepositoryqqq insuranceGuiyangYanglaoInfoRepositoryqqq;
	@Autowired
	private InsuranceGuiyangShiyeInfoRepositoryqqq insuranceGuiyangShiyeInfoRepositoryqqq;
	@Autowired
	private InsuranceGuiyangShengyuInfoRepositoryqqq insuranceGuiyangShengyuInfoRepositoryqqq;
	@Autowired
	private InsuranceGuiyangYibaoInfoRepositoryqqq insuranceGuiyangYibaoInfoRepositoryqqq;
	@Autowired
	private InsuranceGuiyangGongshangInfoRepositoryqqq insuranceGuiyangGongshangInfoRepositoryqqq;

	@Autowired
	private InsuranceGuiyangJiGuanYanglaoInfoRepository insuranceGuiyangJiGuanYanglaoInfoRepository;
	@Autowired
	private InsuranceGuiyangDaeInfoRepository insuranceGuiyangDaeInfoRepository;
	@Autowired
	private InsuranceGuiyangBuchongYanglaoInfoRepository insuranceGuiyangBuchongYanglaoInfoRepository;
	@Autowired
	private InsuranceGuiyangYiliaoZhuruInfoRepository insuranceGuiyangYiliaoZhuruInfoRepository;
	@Autowired
	private InsuranceGuiyangJuminYanglaoInfoRepository insuranceGuiyangJuminYanglaoInfoRepository;
	/** 贵阳社保登录的URL */
	public static final String LoginPage = "http://118.112.188.109/nethall/login.jsp";
	/** 贵阳社保主页中基本信息的URL */
	public static final String BASE_URL = "http://ytrsj.gov.cn:8081/hsp/hspUser.do?method=fwdQueryPerInfo";
	/** 贵阳社保主页中养老的URL */
	public static final String YANGLAO_URL = "http://ytrsj.gov.cn:8081/hsp/siAd.do?method=queryAgedPayHis";
	/** 贵阳社保主页中医疗的URL */
	public static final String YILIAO_URL = "http://ytrsj.gov.cn:8081/hsp/siMedi.do?method=queryMediPayHis";
	/** 贵阳社保主页中工伤的URL */
	public static final String GONGSHANG_URL = "http://ytrsj.gov.cn:8081/hsp/siHarm.do?method=queryHarmPayHis";
	/** 贵阳社保主页中生育的URL */
	public static final String SHANGYU_URL = "http://ytrsj.gov.cn:8081/hsp/siBirth.do?method=queryBirthPayHis";
	/** 贵阳社保主页中失业的URL */
	public static final String SHIYE_URL = "http://ytrsj.gov.cn:8081/hsp/siLost.do?method=queryLostPayHis";

	static String driverPath = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	@Autowired
	private app.commontracerlog.TracerLog tracer;

	WebDriver driver;

	/**
	 * 贵阳社保登录
	 * 
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public TaskInsurance login(InsuranceRequestParameters parameter) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			System.setProperty("webdriver.ie.driver", driverPath);
			driver = new InternetExplorerDriver(ieCapabilities);
			// 设置超时时间界面加载和js加载
			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
			String baseUrl = LoginPage;
			driver.get(baseUrl);
			driver.manage().window().maximize();

			Thread.sleep(2000L);

			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement login_person = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("login_person"));
				}
			});

			System.out.println("获取到login_person---->" + login_person.getText());

			Thread.sleep(2000L);// 这里需要休息2秒，不然点击事件可能无法弹出登录框

			login_person.click();

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
			WebElement username = wait2.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("c_username"));
				}
			});
			System.out.println("获取到username---->" + username.getText());

			username.click();
			username.clear();

			// 用户名
			String uname = parameter.getUsername().trim();

			username.sendKeys(uname);

			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);
			VK.Tab();
			Thread.sleep(1000);

			String password = parameter.getPassword().trim();
			VK.KeyPress(password);

			// 验证码
			String path = WebDriverUnit.saveImg(driver, By.id("codeimgC"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1007", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
							// 1~5位英文数字
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);

			driver.findElement(By.id("checkCodeC")).sendKeys(code);

			// 点击登录
			WebElement loginBtnC = driver.findElement(By.id("loginBtnC"));
			loginBtnC.click();

			Thread.sleep(5000);

			String currentUrl = driver.getCurrentUrl();
			if (LoginPage.contains(currentUrl)) {
				System.out.println("登录失败！");

				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				System.out.println("登录失败" + path2);

				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsuranceRepository.save(taskInsurance);

			} else {
				System.out.println("登录成功！");

				// 截图
				String path2 = WebDriverUnit.saveScreenshotByPath(driver, this.getClass());
				System.out.println("登录成功" + path2);

				// 主界面地址 http://118.112.188.109/nethall//indexAction.do
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();

				WebClient webClient = WebCrawler.getInstance().getWebClient();//

				for (org.openqa.selenium.Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "-------cookies--------" + cookie.getValue());
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(
							"118.112.188.109", cookie.getName(), cookie.getValue()));
				}

				String cookieJson = CommonUnit.transcookieToJson(webClient);

				taskInsurance.setCookies(cookieJson);
				System.out.println("执行保存！");
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}

	/**
	 * 爬取贵阳社保-基本信息
	 * 
	 * @param parameter
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws IOException
	 */
	public void crawlerBaseInfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("service.crawlerBaseInfo.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String baseurl = "http://118.112.188.109/nethall/yhwssb/query/queryInsuranceInfoAction.do?___businessId=01391304";
			WebRequest webRequestbase;
			webRequestbase = new WebRequest(new URL(baseurl), HttpMethod.GET);
			webRequestbase.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryInsuranceInfoAction.do?___businessId=01391304");
			webRequestbase.setAdditionalHeader("Host", "118.112.188.109");
			HtmlPage base = webClient.getPage(webRequestbase);
			String contentAsString = base.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(contentAsString);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("基本信息");
			insuranceGuiyangHtml.setUrl(baseurl);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			Document doc = Jsoup.parse(contentAsString);
			Element elementById = doc.getElementById("aac003");
			if (null != elementById) {
				System.out.println("获取基本信息数据成功！");
				// 姓名
				String name = elementById.val();
				// 身份证
				String cardid = doc.getElementById("aac002").val();
				// 性别
				String sex = "";
				String[] split = contentAsString.split("slectInput_aac004");
				String[] split2 = split[2].split("\"");
				if ("1".equals(split2[1])) {
					sex = "男";
				}
				if ("2".equals(split2[1])) {
					sex = "女";
				}
				if ("9".equals(split2[1])) {
					sex = "无";
				}
				// 出生日期
				String birthdate = doc.getElementById("aac006").val();
				// 参工日期
				String cangongdate = doc.getElementById("aac007").val();

				// 户口性质
				String hkxz = "";
				String[] split1 = contentAsString.split("slectInput_aac009");
				String[] split21 = split1[2].split("\"");
				if ("1".equals(split21[1])) {
					hkxz = "城镇";
				}
				if ("2".equals(split21[1])) {
					hkxz = "农村";
				}
				// 户口所在地
				String hkaddr = doc.getElementById("aac010").val();
				// 人员状态
				String status = "";
				String[] split11 = contentAsString.split("slectInput_aac008");
				String[] split211 = split11[2].split("\"");
				if ("1".equals(split211[1])) {
					status = "在职";
				}
				if ("2".equals(split211[1])) {
					status = "退休";
				}
				if ("3".equals(split211[1])) {
					status = "死亡";
				}
				if ("5".equals(split211[1])) {
					status = "转出终止";
				}
				if ("6".equals(split211[1])) {
					status = "退休终止";
				}
				// 邮政编码
				String yzbm = doc.getElementById("aae007").val();
				// 联系地址
				String linkaddr = doc.getElementById("aae006").val();
				// 个人身份
				String grsf = "";
				String[] split111 = contentAsString.split("slectInput_aac012");
				String[] split2111 = split111[2].split("\"");
				if ("1".equals(split2111[1])) {
					grsf = "干部";
				}
				if ("2".equals(split2111[1])) {
					grsf = "工人";
				}
				if ("3".equals(split2111[1])) {
					grsf = "灵活就业人员";
				}
				if ("4".equals(split2111[1])) {
					grsf = "实习生";
				}
				// 用工形式
				String ygxs = "";
				String[] split1111 = contentAsString.split("slectInput_aac013");
				String[] split21111 = split1111[2].split("\"");
				if ("1".equals(split21111[1])) {
					ygxs = "原固定职工";
				}
				if ("2".equals(split21111[1])) {
					ygxs = "城镇合同制职工";
				}
				if ("3".equals(split21111[1])) {
					ygxs = "农民合同制工人";
				}
				if ("4".equals(split21111[1])) {
					ygxs = "临时工";
				}
				if ("5".equals(split21111[1])) {
					ygxs = "农民工失业缴纳个人部份";
				}
				if ("9".equals(split21111[1])) {
					ygxs = "其他用工形式";
				}

				InsuranceGuiyangBaseInfo insuranceGuiyangBaseInfo = new InsuranceGuiyangBaseInfo();
				insuranceGuiyangBaseInfo.setTaskid(taskInsurance.getTaskid().trim());
				insuranceGuiyangBaseInfo.setName(name);
				insuranceGuiyangBaseInfo.setCardid(cardid);
				insuranceGuiyangBaseInfo.setSex(sex);
				insuranceGuiyangBaseInfo.setBirthdate(birthdate);
				insuranceGuiyangBaseInfo.setCangongdate(cangongdate);
				insuranceGuiyangBaseInfo.setHkxz(hkxz);
				insuranceGuiyangBaseInfo.setHkaddr(hkaddr);
				insuranceGuiyangBaseInfo.setStatus(status);
				insuranceGuiyangBaseInfo.setYzbm(yzbm);
				insuranceGuiyangBaseInfo.setLinkaddr(linkaddr);
				insuranceGuiyangBaseInfo.setGrsf(grsf);
				insuranceGuiyangBaseInfo.setYgxs(ygxs);
				insuranceGuiyangBaseInfoRepository.save(insuranceGuiyangBaseInfo);
				// 更新Task表为 基本信息数据解析成功
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			} else {
				System.out.println("获取基本信息数据失败！");
				// 更新Task表为 基本信息数据解析失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 爬取贵阳社保-城镇职工基本养老保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_yanglao_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_yanglao_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=01&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%9F%BA%E6%9C%AC%E5%85%BB%E8%80%81%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工基本养老保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			if (success == "true") {
				System.out.println("城镇职工基本养老保险数据获取成功！");
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================镇职工基本养老保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangYanglaoInfoqqq insuranceGuiyangYanglaoInfoqqq = new InsuranceGuiyangYanglaoInfoqqq();
					insuranceGuiyangYanglaoInfoqqq.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangYanglaoInfoqqq.setUnitName(unitName);
					insuranceGuiyangYanglaoInfoqqq.setUnitNo(unitNo);
					insuranceGuiyangYanglaoInfoqqq.setCompanyPay(companyPay);
					insuranceGuiyangYanglaoInfoqqq.setPersonalPay(personalPay);
					insuranceGuiyangYanglaoInfoqqq.setPayCardinal(payCardinal);
					insuranceGuiyangYanglaoInfoqqq.setName(name);
					insuranceGuiyangYanglaoInfoqqq.setYearMonth(yearMonth);
					insuranceGuiyangYanglaoInfoRepositoryqqq.save(insuranceGuiyangYanglaoInfoqqq);
				}
				// 更新Task表为 城镇职工基本养老保险数据成功
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			} else {
				System.out.println("城镇职工基本养老保险数据获取失败！");
				// 更新Task表为 城镇职工基本养老保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-城镇职工失业保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_shiye_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_shiye_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=02&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%A4%B1%E4%B8%9A%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工失业保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("城镇职工失业保险数据获取成功！");
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================城镇职工失业保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangShiyeInfoqqq insuranceGuiyangShiyeInfoqqq = new InsuranceGuiyangShiyeInfoqqq();
					insuranceGuiyangShiyeInfoqqq.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangShiyeInfoqqq.setUnitName(unitName);
					insuranceGuiyangShiyeInfoqqq.setUnitNo(unitNo);
					insuranceGuiyangShiyeInfoqqq.setCompanyPay(companyPay);
					insuranceGuiyangShiyeInfoqqq.setPersonalPay(personalPay);
					insuranceGuiyangShiyeInfoqqq.setPayCardinal(payCardinal);
					insuranceGuiyangShiyeInfoqqq.setName(name);
					insuranceGuiyangShiyeInfoqqq.setYearMonth(yearMonth);
					insuranceGuiyangShiyeInfoRepositoryqqq.save(insuranceGuiyangShiyeInfoqqq);
				}

				// 更新Task表为 城镇职工失业保险数据成功
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);

			} else {
				System.out.println("城镇职工失业保险数据获取失败！");
				// 更新Task表为 城镇职工失业保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-城镇职工基本医疗保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_yiliao_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_yiliao_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=02&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%A4%B1%E4%B8%9A%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工基本医疗保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("城镇职工基本医疗保险数据获取成功！");
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================城镇职工基本医疗保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangYibaoInfoqqq insuranceGuiyangYibaoInfoqqq = new InsuranceGuiyangYibaoInfoqqq();
					insuranceGuiyangYibaoInfoqqq.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangYibaoInfoqqq.setUnitName(unitName);
					insuranceGuiyangYibaoInfoqqq.setUnitNo(unitNo);
					insuranceGuiyangYibaoInfoqqq.setCompanyPay(companyPay);
					insuranceGuiyangYibaoInfoqqq.setPersonalPay(personalPay);
					insuranceGuiyangYibaoInfoqqq.setPayCardinal(payCardinal);
					insuranceGuiyangYibaoInfoqqq.setName(name);
					insuranceGuiyangYibaoInfoqqq.setYearMonth(yearMonth);
					insuranceGuiyangYibaoInfoRepositoryqqq.save(insuranceGuiyangYibaoInfoqqq);
				}

				// 更新Task表为 城镇职工基本医疗保险数据成功
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);

			} else {
				System.out.println("城镇职工基本医疗保险数据获取失败！");
				// 更新Task表为 城镇职工基本医疗保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-城镇职工工伤保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_gongshang_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_gongshang_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=04&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%B7%A5%E4%BC%A4%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工工伤保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("城镇职工工伤保险数据获取成功！");
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================城镇职工工伤保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangGongshangInfoqqq insuranceGuiyangGongshangInfoqqq = new InsuranceGuiyangGongshangInfoqqq();
					insuranceGuiyangGongshangInfoqqq.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangGongshangInfoqqq.setUnitName(unitName);
					insuranceGuiyangGongshangInfoqqq.setUnitNo(unitNo);
					insuranceGuiyangGongshangInfoqqq.setCompanyPay(companyPay);
					insuranceGuiyangGongshangInfoqqq.setPersonalPay(personalPay);
					insuranceGuiyangGongshangInfoqqq.setPayCardinal(payCardinal);
					insuranceGuiyangGongshangInfoqqq.setName(name);
					insuranceGuiyangGongshangInfoqqq.setYearMonth(yearMonth);
					insuranceGuiyangGongshangInfoRepositoryqqq.save(insuranceGuiyangGongshangInfoqqq);
				}

				// 更新Task表为 城镇职工工伤保险数据成功
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);

			} else {
				System.out.println("城镇职工工伤保险数据获取失败！");
				// 更新Task表为城镇职工工伤保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-城镇职工生育保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_shengyu_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_shengyu_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=04&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%B7%A5%E4%BC%A4%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工生育保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("城镇职工生育保险数据获取成功！");
				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================城镇职工生育保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangShengyuInfoqqq insuranceGuiyangShengyuInfoqqq = new InsuranceGuiyangShengyuInfoqqq();
					insuranceGuiyangShengyuInfoqqq.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangShengyuInfoqqq.setUnitName(unitName);
					insuranceGuiyangShengyuInfoqqq.setUnitNo(unitNo);
					insuranceGuiyangShengyuInfoqqq.setCompanyPay(companyPay);
					insuranceGuiyangShengyuInfoqqq.setPersonalPay(personalPay);
					insuranceGuiyangShengyuInfoqqq.setPayCardinal(payCardinal);
					insuranceGuiyangShengyuInfoqqq.setName(name);
					insuranceGuiyangShengyuInfoqqq.setYearMonth(yearMonth);
					insuranceGuiyangShengyuInfoRepositoryqqq.save(insuranceGuiyangShengyuInfoqqq);
				}

			} else {
				System.out.println("城镇职工生育保险数据获取失败！");
				// 更新Task表为城镇职工生育保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-机关事业养老保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_jiguan_yanglao_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_jiguan_yanglao_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=06&dto%5B'aae140_desc'%5D=%E6%9C%BA%E5%85%B3%E4%BA%8B%E4%B8%9A%E5%85%BB%E8%80%81%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("机关事业养老保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("机关事业养老保险数据获取成功！");

				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================机关事业养老保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangJiGuanYanglaoInfo insuranceGuiyangJiGuanYanglaoInfo = new InsuranceGuiyangJiGuanYanglaoInfo();
					insuranceGuiyangJiGuanYanglaoInfo.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangJiGuanYanglaoInfo.setUnitName(unitName);
					insuranceGuiyangJiGuanYanglaoInfo.setUnitNo(unitNo);
					insuranceGuiyangJiGuanYanglaoInfo.setCompanyPay(companyPay);
					insuranceGuiyangJiGuanYanglaoInfo.setPersonalPay(personalPay);
					insuranceGuiyangJiGuanYanglaoInfo.setPayCardinal(payCardinal);
					insuranceGuiyangJiGuanYanglaoInfo.setName(name);
					insuranceGuiyangJiGuanYanglaoInfo.setYearMonth(yearMonth);
					insuranceGuiyangJiGuanYanglaoInfoRepository.save(insuranceGuiyangJiGuanYanglaoInfo);
				}

			} else {
				System.out.println("机关事业养老保险数据获取失败！");
				// 更新Task表为机关事业养老保险数据失败
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,
						InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-城镇职工大额救助医疗保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_czzg_dae_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_czzg_dae_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=07&dto%5B'aae140_desc'%5D=%E5%9F%8E%E9%95%87%E8%81%8C%E5%B7%A5%E5%A4%A7%E9%A2%9D%E6%95%91%E5%8A%A9%E5%8C%BB%E7%96%97%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("城镇职工大额救助医疗保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("城镇职工大额救助医疗保险数据获取成功！");

				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================城镇职工大额救助医疗保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangDaeInfo insuranceGuiyangDaeInfo = new InsuranceGuiyangDaeInfo();
					insuranceGuiyangDaeInfo.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangDaeInfo.setUnitName(unitName);
					insuranceGuiyangDaeInfo.setUnitNo(unitNo);
					insuranceGuiyangDaeInfo.setCompanyPay(companyPay);
					insuranceGuiyangDaeInfo.setPersonalPay(personalPay);
					insuranceGuiyangDaeInfo.setPayCardinal(payCardinal);
					insuranceGuiyangDaeInfo.setName(name);
					insuranceGuiyangDaeInfo.setYearMonth(yearMonth);
					insuranceGuiyangDaeInfoRepository.save(insuranceGuiyangDaeInfo);
				}
			} else {
				System.out.println("城镇职工大额救助医疗保险数据获取失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-补充医疗保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_buchong_yiliao_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_buchong_yiliao_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=08&dto%5B'aae140_desc'%5D=%E8%A1%A5%E5%85%85%E5%8C%BB%E7%96%97%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("补充医疗保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("补充医疗保险数据获取成功！");

				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================补充医疗保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangBuChongYibaoInfo insuranceGuiyangBuChongYibaoInfo = new InsuranceGuiyangBuChongYibaoInfo();
					insuranceGuiyangBuChongYibaoInfo.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangBuChongYibaoInfo.setUnitName(unitName);
					insuranceGuiyangBuChongYibaoInfo.setUnitNo(unitNo);
					insuranceGuiyangBuChongYibaoInfo.setCompanyPay(companyPay);
					insuranceGuiyangBuChongYibaoInfo.setPersonalPay(personalPay);
					insuranceGuiyangBuChongYibaoInfo.setPayCardinal(payCardinal);
					insuranceGuiyangBuChongYibaoInfo.setName(name);
					insuranceGuiyangBuChongYibaoInfo.setYearMonth(yearMonth);
					insuranceGuiyangBuchongYanglaoInfoRepository.save(insuranceGuiyangBuChongYibaoInfo);
				}
			} else {
				System.out.println("补充医疗保险数据获取失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-医疗注入资金
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_yiliao_zhuruzijin_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_yiliao_zhuruzijin_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=09&dto%5B'aae140_desc'%5D=%E5%8C%BB%E7%96%97%E6%B3%A8%E5%85%A5%E8%B5%84%E9%87%91&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("医疗注入资金");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("医疗注入资金数据获取成功！");

				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================医疗注入资金数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangYiliaozhuruInfo insuranceGuiyangYiliaozhuruInfo = new InsuranceGuiyangYiliaozhuruInfo();
					insuranceGuiyangYiliaozhuruInfo.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangYiliaozhuruInfo.setUnitName(unitName);
					insuranceGuiyangYiliaozhuruInfo.setUnitNo(unitNo);
					insuranceGuiyangYiliaozhuruInfo.setCompanyPay(companyPay);
					insuranceGuiyangYiliaozhuruInfo.setPersonalPay(personalPay);
					insuranceGuiyangYiliaozhuruInfo.setPayCardinal(payCardinal);
					insuranceGuiyangYiliaozhuruInfo.setName(name);
					insuranceGuiyangYiliaozhuruInfo.setYearMonth(yearMonth);
					insuranceGuiyangYiliaoZhuruInfoRepository.save(insuranceGuiyangYiliaozhuruInfo);
				}
			} else {
				System.out.println("医疗注入资金数据获取失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 爬取贵阳社保-居民养老保险
	 * 
	 * @param parameter
	 * @return
	 */
	public void crawler_jumin_yanglao_Insurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			int year) {
		tracer.addTag("service.crawler_jumin_yanglao_Insurance.taskid", taskInsurance.getTaskid().trim());
		TaskInsurance findByTaskid = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid().trim());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = findByTaskid.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {

			String year2 = year + "";

			String url = "http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction!queryPayment.do?dto%5B'aae140'%5D=10&dto%5B'aae140_desc'%5D=%E5%B1%85%E6%B0%91%E5%85%BB%E8%80%81%E4%BF%9D%E9%99%A9&dto%5B'aae002'%5D="
					+ year2 + "&gridInfo%5B'payment_list_limit'%5D=100&gridInfo%5B'payment_list_start'%5D=0&";

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("Host", "118.112.188.109");
			requestSettings.setAdditionalHeader("Referer",
					"http://118.112.188.109/nethall/yhwssb/query/queryPersPayAction.do");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			InsuranceGuiyangHtml insuranceGuiyangHtml = new InsuranceGuiyangHtml();
			insuranceGuiyangHtml.setHtml(html);
			insuranceGuiyangHtml.setPageCount(1);
			insuranceGuiyangHtml.setTaskid(taskInsurance.getTaskid().trim());
			insuranceGuiyangHtml.setType("居民养老保险");
			insuranceGuiyangHtml.setUrl(url);
			insuranceGuiyangHtmlRepository.save(insuranceGuiyangHtml);

			JSONObject jsonhtml = JSONObject.fromObject(html);
			String success = jsonhtml.getString("success");
			System.out.println("true".equals(success));
			if (success == "true") {
				System.out.println("居民养老保险数据获取成功！");

				String lists = jsonhtml.getString("lists");
				JSONObject jsonlists = JSONObject.fromObject(lists);
				String payment_list = jsonlists.getString("payment_list");
				JSONObject jsonpayment_list = JSONObject.fromObject(payment_list);
				String list = jsonpayment_list.getString("list");
				JSONArray arraylist = JSONArray.fromObject(list);
				for (int i = 0; i < arraylist.size(); i++) {
					JSONObject jsonlist = JSONObject.fromObject(arraylist.get(i));
					String unitNo = jsonlist.getString("aab001");
					String companyPay = jsonlist.getString("dwjf");
					String payCardinal = jsonlist.getString("jfjs");
					String personalPay = jsonlist.getString("grjf");
					String yearMonth = jsonlist.getString("qh");
					String name = jsonlist.getString("xz");
					String unitName = jsonlist.getString("aab004");
					System.out.println("================居民养老保险数据================");
					System.out.println("单位编号----" + unitNo);
					System.out.println("公司交费----" + companyPay);
					System.out.println("交费基数----" + payCardinal);
					System.out.println("个人交费----" + personalPay);
					System.out.println("期号----" + yearMonth);
					System.out.println("险种----" + name);
					System.out.println("公司名称----" + unitName);
					InsuranceGuiyangJuminYanglaoInfo insuranceGuiyangJuminYanglaoInfo = new InsuranceGuiyangJuminYanglaoInfo();
					insuranceGuiyangJuminYanglaoInfo.setTaskid(taskInsurance.getTaskid().trim());
					insuranceGuiyangJuminYanglaoInfo.setUnitName(unitName);
					insuranceGuiyangJuminYanglaoInfo.setUnitNo(unitNo);
					insuranceGuiyangJuminYanglaoInfo.setCompanyPay(companyPay);
					insuranceGuiyangJuminYanglaoInfo.setPersonalPay(personalPay);
					insuranceGuiyangJuminYanglaoInfo.setPayCardinal(payCardinal);
					insuranceGuiyangJuminYanglaoInfo.setName(name);
					insuranceGuiyangJuminYanglaoInfo.setYearMonth(yearMonth);
					insuranceGuiyangJuminYanglaoInfoRepository.save(insuranceGuiyangJuminYanglaoInfo);
				}
			} else {
				System.out.println("居民养老保险数据获取失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
