package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.ccbchina.CcbChinaDebitcardUserinfo;
import app.bean.RequestParam;
import app.commontracerlog.TracerLog;

@Component
public class CcbChinaLoginParser {
	
	@Autowired
	private TracerLog tracerLog;
	
//	private String loginUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";

//	/**
//	 * @Des 登录
//	 * @param bankJsonBean
//	 * @return
//	 * @throws IllegalAccessException
//	 * @throws NativeException
//	 * @throws Exception
//	 */
//	public WebDriver getHtmlByAccount(BankJsonBean bankJsonBean) throws IllegalAccessException, NativeException, Exception {
//		
//		//打开建行登录页面
//		WebDriver webDriver = webDriverChromeService.getPageByChrome(loginUrl);
//		
//		WebElement usernameInput = webDriver.findElement(By.id("USERID"));
//		WebElement passwordInput = webDriver.findElement(By.id("LOGPASS"));
//		WebElement imgInput = webDriver.findElement(By.name("PT_CONFIRM_PWD"));
//		usernameInput.click();
//		
//		usernameInput.sendKeys(bankJsonBean.getLoginName());
//		passwordInput.sendKeys(bankJsonBean.getPassword());
//		
//		String path = WebDriverUnit.saveImg(webDriver, By.id("fujiama"));
//		String code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
//		tracerLog.output("crawler.bank.login.account.code", code);
//		
//		imgInput.sendKeys(code);
//		
//		//模拟点击登录按钮
//		webDriverChromeService.clickButtonByDomId(webDriver, "loginButton");
//		
//		return webDriver;
//	}

	/**
	 * @Des 获得重要参数
	 * @param html
	 * @return
	 */
	public RequestParam getParam(String html) {
		RequestParam requestParam = new RequestParam();
		Document doc = Jsoup.parse(html);
		Element form = doc.getElementById("form4Switch");
		
		Element skeyInput = form.select("[name=SKEY]").first();
		String skey = skeyInput.attr("value");
		Element branchidInput = form.select("[name=BRANCHID]").first();
		String branchid = branchidInput.attr("value");
		Element useridInput = form.select("[name=USERID]").first();
		String userid = useridInput.attr("value");
		
		requestParam.setBranchid(branchid);
		requestParam.setSkey(skey);
		requestParam.setUserid(userid);
		tracerLog.output("crawler.bank.login.param", requestParam.toString());
		return requestParam;
	}

	/**
	 * @Des 获取用户信息
	 * @param html
	 * @return
	 */
	public CcbChinaDebitcardUserinfo getUserInfo(String html) {
		
		Document doc = Jsoup.parse(html);
		Element script = doc.getElementsByTag("script").first();
		String json = script.toString();
		String lastJson = json.substring(json.indexOf("{"), json.indexOf("}")+1);
		tracerLog.output("crawler.bank.userinfo", lastJson);
		Gson gson = new Gson();
		CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = gson.fromJson(lastJson, CcbChinaDebitcardUserinfo.class);
		return ccbChinaDebitcardUserinfo;
	}

	/**
	 * @Des 通过卡号登录
	 * @param bankJsonBean
	 * @return
	 */
//	public WebDriver getHtmlByCard(BankJsonBean bankJsonBean) {
//		String cardUrl = "http://accounts.ccb.com/accounts/login_savings_ope.gsp";
//		//打开建行登录页面
//		WebDriver driver = webDriverChromeService.getPageByChrome(cardUrl);
//		
//		// Get the location of element on the page 
//		WebElement ele = driver.findElement(By.tagName("iframe")); 
//		Point point = ele.getLocation(); 
//		
//		driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
//		//用户名输入框
//		WebElement usernameInput = driver.findElement(By.id("ACC_NO_temp"));
//		//密码输入框
//		WebElement passwordInput = driver.findElement(By.name("LOGPASS"));			
//		//图片验证码输入框
//		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
//		//点击登录按钮
//		WebElement button = driver.findElement(By.className("btn"));
//		//使用键盘输入的按钮
//		WebElement keyButton = driver.findElement(By.id("useKey"));
//		
//		//有数字键盘，改成用本地键盘录入。
//		passwordInput.click();
//		keyButton.click();
//		
//		//输入用户名密码
//		usernameInput.sendKeys(bankJsonBean.getLoginName());
//		passwordInput.sendKeys(bankJsonBean.getPassword());
//		
//		try {
//			String path = WebDriverUnit.saveImg(driver,By.id("fujiama"),point.getX(),point.getY());
//			String code = chaoJiYingOcrService.callChaoJiYingService(path, "1006");
//			tracerLog.output("crawler.bank.login.card.code", code);
//			
//			imgInput.sendKeys(code);
//			button.click();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return driver;
//	}

	/**
	 * @Des 通过卡号登录获取用户信息
	 * @param html
	 * @return
	 */
	public CcbChinaDebitcardUserinfo getUserInfoByCard(String html) {
		
		CcbChinaDebitcardUserinfo ccbChinaDebitcardUserinfo = new CcbChinaDebitcardUserinfo();
		try{
			Document doc = Jsoup.parse(html);
			String name = doc.select("span.f_14_blue").get(0).text();
			String account = doc.select("span.f_14_blue").get(1).text().replace("账号： ", "");
			String accountType = doc.select("#dline").get(0).text();
			String currency = doc.select("#dline").get(1).text();
			String openDate = doc.select("#dline").get(2).text();
			String balance = doc.select("#dline").get(3).text();
			String accountStatus = doc.select("#dline").get(5).text();
			
			ccbChinaDebitcardUserinfo.setNAME(name);
			ccbChinaDebitcardUserinfo.setCardNum(account);
			ccbChinaDebitcardUserinfo.setAccountType(accountType);
			ccbChinaDebitcardUserinfo.setCurrency(currency);
			ccbChinaDebitcardUserinfo.setOpenDate(openDate);
			ccbChinaDebitcardUserinfo.setBalance(balance);
			ccbChinaDebitcardUserinfo.setAccountStatus(accountStatus);			
			
		}catch(Exception e){
			tracerLog.output("crawler.ccbchina.getuserinfobycard.error", e.getMessage());
			return null;
		}
		return ccbChinaDebitcardUserinfo;
	}

}
