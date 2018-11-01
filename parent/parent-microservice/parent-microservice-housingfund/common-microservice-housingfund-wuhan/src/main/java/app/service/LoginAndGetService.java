package app.service;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTelInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanHtml;
import com.microservice.dao.repository.crawler.housing.wuhan.HousingWuHanHtmlRepository;
import com.module.htmlunit.WebCrawler;
import com.module.jna.webdriver.WebDriverUnit;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONObject;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhan")
public class LoginAndGetService extends HousingBasicService{


	static String driverPath = "F:\\IEDriverServer_Win32\\IEDriverServer.exe";

	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";

//	private Robot robot;

	@Autowired
	private HousingWuHanHtmlRepository housingWuHanHtmlRepository;
	public Object Idcard(TaskHousing taskHousing, MessageLoginForHousing messageLoginForHousing) {

		WebClient webClient = WebCrawler.getInstance().getWebClient();
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		System.setProperty("webdriver.ie.driver", driverPath);
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		driver = new InternetExplorerDriver(ieCapabilities);
		try {
			String url = "https://whgjj.hkbchina.com/portal/pc/login.html";
			driver.get(url);
			Thread.sleep(2000);
			//帐号
			driver.findElement(By.id("LoginId")).sendKeys(messageLoginForHousing.getNum());
			//密码
			InputTab2();
			Thread.sleep(2000);
			VirtualKeyBoard.KeyPressEx(messageLoginForHousing.getPassword(), 5);
			//获取验证码
			String path = WebDriverUnit.saveImg(driver, By.id("_tokenImg"));
			System.out.println("path---------------" + path);
			String chaoJiYingResult = WebDriverUnit.getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG,
					path); // 1005
			System.out.println("chaoJiYingResult---------------" + chaoJiYingResult);
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			Thread.sleep(2000);
			//验证码
			driver.findElement(By.id("_vTokenName")).sendKeys(code);
			Thread.sleep(2000);
			List<WebElement> findElements = driver.findElements(By.xpath("//div[@class = 'loginBtn']"));
			for (WebElement webElement : findElements) {
				webElement.click();
				webElement.click();
			}
			Thread.sleep(5000);
			
			
			String html = driver.getPageSource();
			HousingWuHanHtml housingWuHanHtml = new HousingWuHanHtml();
			housingWuHanHtml.setUrl(url);
			housingWuHanHtml.setHtml(html);
			housingWuHanHtml.setPagenumber(1);
			housingWuHanHtml.setType("武汉公积金登录页面");
			housingWuHanHtmlRepository.save(housingWuHanHtml);
			//短信
			if(html.indexOf("手机动态密码")!=-1){
				
				WebElement webElement = driver.findElement(By.id("submitPhone"));
				webElement.click();
				String html1 = driver.getPageSource();
				HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
				housingWuHanHtml1.setUrl(driver.getCurrentUrl());
				housingWuHanHtml1.setHtml(html1);
				housingWuHanHtml1.setPagenumber(1);
				housingWuHanHtml1.setType("武汉公积金登录成功---->首次登录短信验证页");
				housingWuHanHtmlRepository.save(housingWuHanHtml1);
				if(html.indexOf("短信动态密码已经发送至您的绑定手机")!=-1){
					Set<Cookie> cookies2 = driver.manage().getCookies();
					for (Cookie cookie2 : cookies2) {
						System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
						webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("whgjj.hkbchina.com",
								cookie2.getName(), cookie2.getValue()));
					}
					String cookieString = CommonUnit.transcookieToJson(webClient);
					taskHousing.setCookies(cookieString);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
					taskHousing.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
					if(messageLoginForHousing.getNum().length()>11){
						taskHousing.setLogintype("IDNUM");
					}else{
						taskHousing.setLogintype("CO_BRANDED_CARD");
					}
					save(taskHousing);
					driver.quit();
					return null;
				}
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_VERIFY_SMS_TOOMANY.getMessage());
				save(taskHousing);
				return null;
			}


			if(html.indexOf("尊敬的")!=-1){
				HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
				housingWuHanHtml1.setUrl(driver.getCurrentUrl());
				housingWuHanHtml1.setHtml(html);
				housingWuHanHtml1.setPagenumber(1);
				housingWuHanHtml1.setType("武汉公积金登录成功---->首页");
				housingWuHanHtmlRepository.save(housingWuHanHtml1);
				Set<Cookie> cookies2 = driver.manage().getCookies();
				for (Cookie cookie2 : cookies2) {
					System.out.println(cookie2.getName() + "-------cookies--------" + cookie2.getValue());
					webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("whgjj.hkbchina.com",
							cookie2.getName(), cookie2.getValue()));
				}
				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookieString);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getMessage());
				if(messageLoginForHousing.getNum().length()>11){
					taskHousing.setLogintype("IDNUM");
				}else{
					taskHousing.setLogintype("CO_BRANDED_CARD");
				}
				save(taskHousing);
				driver.quit();
				return null;
			}
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getCode());
			taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SER.getMessage());
			save(taskHousing);
			driver.quit();
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆页面已修改", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getDescription());
			taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getCode());
			taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_TIMEOUT.getMessage());
			save(taskHousing);
			driver.quit();
			return null;
		}

	}
	
	//验证码验证
	public Object setPhoneCode(MessageLoginForHousing messageLogin, TaskHousing taskHousing){

		WebClient webClient = WebCrawler.getInstance().getWebClient();
		String cookies = taskHousing.getCookies();
		Set<com.gargoylesoftware.htmlunit.util.Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<com.gargoylesoftware.htmlunit.util.Cookie> j = set.iterator();
		while(j.hasNext()){
			webClient.getCookieManager().addCookie(j.next());
		}

		String url = "https://whgjj.hkbchina.com/portal/pc/htmls/MessageVerify/MessageVerify.html";
		try {
			HtmlPage page = getHtml(url, webClient);
			HtmlTelInput phoneCode = (HtmlTelInput)page.getFirstByXPath("//input[@id='phoneCode']");
			phoneCode.setText(messageLogin.getSms_code());
			HtmlButton cButton = (HtmlButton)page.getFirstByXPath("//button[@class='messagebutton_do']");
			cButton.click();

			String url2 = "https://whgjj.hkbchina.com/portal/firstLoginValidate.do?"
					+ "LoginType=F"
					+ "&_local=zh_CN"
					+ "&BankId=9999"
					+ "&_ChannelId=PMBS"
					+ "&TokenCode="+messageLogin.getSms_code();
			Page htmlPage = LoginAndGetCommon.getHtml(url2, webClient);
			String html = htmlPage.getWebResponse().getContentAsString();
			JSONObject object = JSONObject.fromObject(html);
			
			if(!object.has("_RejMsg")){
				//{"_HostTransactionCode":"reservefunds.MCQueryMobilePhone","TokenCode":"975024","FundCifNo":"228125987","LoginType":"F","_RejCode":"000000","_ClientChannel":"HTTP.WEB"}
				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookieString);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getMessage());
				if(messageLogin.getNum().length()>11){
					taskHousing.setLogintype("IDNUM");
				}else{
					taskHousing.setLogintype("CO_BRANDED_CARD");
				}
				save(taskHousing);
				return null;
			}
			if(object.has("_RejMsg")){
			String string = object.getString("_RejMsg");
			//验证码已过期,请重新获取! 
			if(string.equals("验证码不正确! ")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getMessage());
				save(taskHousing);
				return null;
			}else if(string.equals("会话已超时")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getMessage());
				save(taskHousing);
				return null;
			}
			else if(string.equals("验证码已过期,请重新获取! ")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR4.getDescription());
				taskHousing.setError_code(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.MOBILE_AUTHENTICATION_ERROR.getMessage());
				save(taskHousing);
				return null;
			}
			HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
			housingWuHanHtml1.setUrl(url2);
			housingWuHanHtml1.setHtml(string);
			housingWuHanHtml1.setPagenumber(1);
			housingWuHanHtml1.setType("武汉公积金验证码错误页面");
			housingWuHanHtmlRepository.save(housingWuHanHtml1);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			save(taskHousing);
			return null;
			}
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tracer.addTag("验证码验证失败", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR2.getDescription());
			taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getCode());
			taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR_SMS.getMessage());
			save(taskHousing);
			return null;
		}

	}


	public static void InputTab() throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}

	public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
		Thread.sleep(1000L);
		for (String s : accountNum) {
			if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
			}
			Thread.sleep(500L);
		}
	}
	public void InputTab2() throws IllegalAccessException, NativeException, Exception { 
		if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
			VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
		}
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception{

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);

		return page;

	}


}
