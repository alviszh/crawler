package app.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microservice.dao.entity.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetails;
import com.module.htmlunit.WebCrawler;

import app.bean.RequestParam;
import app.controller.CcbChinaController;

public class Test {
	
	String driverPathIE = "C:\\IEDriverServer_Win32\\IEDriverServer.exe";

	public static Test t = new Test();
	public static void main(String[] args) {
		
		try {
//			t.loginByAccountNum("111", "111");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		mingxi();

		Map<String, Long> map = new HashMap<String, Long>();

		
		
	}
	
	public WebDriver openloginIE() {
		driver = getNewWebDriver();
		try {
			driver = getPage(driver, loginUrl);
		} catch (NoSuchWindowException e) {
			driver = getPage(driver, loginUrl);
		}
		return driver;
	}

	public WebDriver getPage(WebDriver driver,String url) throws NoSuchWindowException{
		driver.get(url);
		return driver;
	}
	
	public WebDriver getNewWebDriver(){ 
		 
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        
		System.setProperty("webdriver.ie.driver", driverPathIE );  
		if(driverPathIE==null){
			throw new RuntimeException("webdriver.ie.driver 初始化失败！");
		}
		WebDriver driver = new InternetExplorerDriver(ieCapabilities);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);//页面加载timeout 30秒 
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS); //JavaScript加载timeout 30秒
	 
		return driver;
	}
	
	public static void mingxi() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\ccb.txt");
		String json = txt2String(file);
//		Test t = new Test();
		List<CcbChinaDebitCardBillDetails> transFlows = new ArrayList<CcbChinaDebitCardBillDetails>();
		
		Document docin = Jsoup.parse(json);
		Element formin = docin.getElementById("jhform");
		Elements trsin = formin.getElementsByClass("td_span");
		if (trsin.first().attr("zcsr").equals("|")) {
//			return;
		} else {
			List<CcbChinaDebitCardBillDetails> list = t.parserResult(trsin, "", "");
			transFlows.addAll(list);
		}
		for (CcbChinaDebitCardBillDetails ccbChinaDebitCardBillDetails : transFlows) {
			System.out.println(ccbChinaDebitCardBillDetails.toString());
		}
		
		System.out.println(transFlows.size());
		
//		String  total = "";
//		String totalPageString="totalPage:";
//		int index = json.indexOf(totalPageString);
//		if (index != -1) {
//			String str = json.substring(index + totalPageString.length());
//			String[] split = str.split("'");
//			if(split.length>1){
//				 total = split[1];
//			}
//		}
//		System.out.println(total);
	}
	
	

	private WebDriver driver;
	private String loginUrl = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_00?SERVLET_NAME=B2CMainPlat_00&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=CLOGIN&DESKTOP=0&EXIT_PAGE=login.jsp&WANGZHANGLOGIN=&FORMEPAY=2";
	String driverPathChrome = "D:\\WD\\s\\chromedriver\\chromedriver.exe";

	public void loginByAccountNum(String loginName, String password) throws Exception {

		// 打开建行登录页面
//		driver = getPageByChrome(loginUrl);
		
		driver = openloginIE();

		WebElement usernameInput = driver.findElement(By.id("USERID"));
		WebElement passwordInput = driver.findElement(By.id("LOGPASS"));
//		WebElement imgInput = driver.findElement(By.name("PT_CONFIRM_PWD"));
		usernameInput.click();

		usernameInput.sendKeys(loginName);
		passwordInput.sendKeys(password);

		// String path = WebDriverUnit.saveImg(driver, By.id("fujiama"));
		// String code = chaoJiYingOcrService.callChaoJiYingService(path,
		// "1006");
		String code = "asdw2";

//		imgInput.sendKeys(code);

		// 模拟点击登录按钮
		driver.findElement(By.id("loginButton")).click();
		
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		
		WebElement main = null;
		try {
			main = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By.id("kh_dlcs"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (main != null) {
			String html = driver.getPageSource();
			
			RequestParam requestParam = new RequestParam();
			Document doc = Jsoup.parse(html);
			Element form = doc.getElementById("form4Switch");

			Element skeyInput = form.select("[name=SKEY]").first();
			String skey = skeyInput.attr("value");
			Element branchidInput = form.select("[name=BRANCHID]").first();
			String branchid = branchidInput.attr("value");
			Element useridInput = form.select("[name=USERID]").first();
			String userid = useridInput.attr("value");
			

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
			for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
				Cookie cookieWebClient = new Cookie("ibsbjstar.ccb.com.cn", cookie.getName(), cookie.getValue());
				webClient.getCookieManager().addCookie(cookieWebClient);
			}
			String cookieJson = CommonUnit.transcookieToJson(webClient);
			requestParam.setCookies(cookieJson);
			requestParam.setBranchid(branchid);
			requestParam.setSkey(skey);
			requestParam.setUserid(userid);
			crawler(loginName, password, requestParam);
			return;
		}
		String html = driver.getPageSource();
		if (html.contains("您输入的登录密码不正确")) {
			driver.quit();
		} else if (html.contains("您输入的信息有误")) {
			driver.quit();
		} else {
			driver.quit();
		}
	}
	
	/**
	 * 爬取
	 * 
	 * @param loginName
	 * @param password
	 * @param params
	 */

	public void crawler(String loginName, String password, RequestParam params) {

		try {
			List<CcbChinaDebitCardBillDetails> transFlows = getBankStatement(loginName, password, params);
			if (null != transFlows) {
				driver.quit();
				System.out.println("!!!!!!!!!!!"+transFlows.size());
				for (CcbChinaDebitCardBillDetails ccbChinaDebitCardBillDetails : transFlows) {
					System.out.println(ccbChinaDebitCardBillDetails.toString());
				}
			}
		} catch (IOException e) {
			driver.quit();
		}

	}
	

	Gson gson = new Gson();
	String SKEY = "";
	String BRANCHID = "";
	String USERID = "";
	WebClient webClient = null;
	String ACC_SIGN = "";
	String PDT_CODE = "";

	public List<CcbChinaDebitCardBillDetails> getBankStatement(String loginName1, String password,
			RequestParam requestParam) throws IOException {

		List<CcbChinaDebitCardBillDetails> transFlows = new ArrayList<CcbChinaDebitCardBillDetails>();
		webClient = addcookie(requestParam.getCookies());
		SKEY = requestParam.getSkey();
		BRANCHID = requestParam.getBranchid();
		USERID = requestParam.getUserid();

		HtmlPage page11 = getAccountQueryResult(USERID);

		List<String> params = getUrlParam(page11);
		if (null != params) {
			for (int i = 0; i < params.size(); i++) {

				String[] param = params.get(i).split("\\|");
				String ACC_NO = param[0];
				String branchid = param[1];
				String ACC_TYPE_FLAG = param[2];
				String CurTypeDesc = param[3];
				String AgreeType = param[5];
				String BranchDesc = param[6];
				String l_acc_type = param[7];

				@SuppressWarnings("deprecation")
				String url12 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31001&SKEY="
						+ SKEY + "&USERID=" + USERID + "&BRANCHID=" + branchid + "&ACC_NO=" + ACC_NO
						+ "&ACC_TYPE_FLAG=" + ACC_TYPE_FLAG + "&CurTypeDesc=" + URLEncoder.encode(CurTypeDesc)
						+ "&AccAlias=&AgreeType=" + AgreeType + "&BranchDesc=" + URLEncoder.encode(BranchDesc)
						+ "&SEND_USERID=&NOACCTJ=1";
				HtmlPage page12 = getHtml(url12, null, null);

				if (page12.asXml().contains("该交易尚未开通")) {
					continue;
				} else {

					getAccSignAndPdtCode(page12);

					String url13 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&SKEY="
							+ SKEY + "&USERID=" + USERID + "&BRANCHID=" + branchid + "&TXCODE=310201&PAGE=1&ACC_NO="
							+ ACC_NO + "&ACC_SIGN=" + ACC_SIGN + "&PDT_CODE=" + PDT_CODE + "&STR_USERID=" + USERID
							+ "&SEND_USERID=&TXTYPE=1";
					HtmlPage searchPage = getHtml(url13, null, null);

					
					String detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
							AgreeType, BranchDesc, l_acc_type, "1", "1", USERID, "", "","1");
					String url15 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=310204&isAjaxRequest=true&SKEY="
							+ SKEY + "&ACC_TYPE=0&TXTYPE=0&BRANCHID=" + branchid + "&USERID=" + USERID + "&ACC_NO="
							+ ACC_NO;
					getHtml(url15, null, null);
					String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
//					String url16 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1";
					HtmlPage resultPage = getHtml(url16, detailParam, null);
					String contentAsString = resultPage.getWebResponse().getContentAsString();
					System.out.println("#############################");
					System.out.println(contentAsString);
					Document doc = Jsoup.parse(contentAsString);
					Element form = doc.getElementById("jhform");
					Elements trs = form.getElementsByClass("td_span");
					if (trs.first().attr("zcsr").equals("|")) {
						break;
					} else {
						List<CcbChinaDebitCardBillDetails> list = parserResult(trs, ACC_NO, BranchDesc);
						transFlows.addAll(list);
					}
					
					String a_str="";
					String filesearchstr = "";
					String toDeleteA_str ="parent.document.getElementById(\"A_STR\").value";
					int indexa_str = contentAsString.indexOf(toDeleteA_str);
					if (indexa_str != -1) {
						String str = contentAsString.substring(indexa_str + toDeleteA_str.length());
						String[] split = str.split("'");
						if(split.length>1){
							a_str = split[1];
							a_str = URLEncoder.encode(a_str);
						}
					}
					String toDeleteFilesearchstr ="window.parent.document.getElementById(\"FILESEARCHSTR\").value";
					int indexFilesearchstr = contentAsString.indexOf(toDeleteFilesearchstr);
					if (indexFilesearchstr != -1) {
						String str = contentAsString.substring(indexFilesearchstr + toDeleteFilesearchstr.length());
						String[] split = str.split("\"");
						if (split.length > 1) {
							filesearchstr = split[1];
						}
					}
					System.out.println(filesearchstr + "((((((((((" + a_str);
					
					int totalpage = 100;
					String totalPageString = "totalPage:";
					int index = contentAsString.indexOf(totalPageString);
					if (index != -1) {
						String str = contentAsString.substring(index + totalPageString.length());
						String[] split = str.split("'");
						if (split.length > 1) {
							String total = split[1];
							totalpage = Integer.valueOf(total) + 1;
						}
					}
					
					for (int j = 2; j < totalpage; j++) {
						detailParam = getDetailParam(searchPage, ACC_NO, branchid, ACC_TYPE_FLAG, CurTypeDesc,
								AgreeType, BranchDesc, l_acc_type, String.valueOf(j), String.valueOf(j - 1), USERID,a_str,filesearchstr,"4");

						String url17 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_12&CCB_IBSVersion=V6&PT_STYLE=1";
						
						HtmlPage resultPagein = getHtml(url17, detailParam, null);
						String contentAsStringin = resultPagein.getWebResponse().getContentAsString();
						System.out.println("#############################"+j);
						System.out.println(contentAsStringin);
						Document docin = Jsoup.parse(contentAsStringin);
						Element formin = docin.getElementById("jhform");
						Elements trsin = formin.getElementsByClass("td_span");
						if (trsin.first().attr("zcsr").equals("|")) {
							break;
						} else {
							List<CcbChinaDebitCardBillDetails> list = parserResult(trsin, ACC_NO, BranchDesc);
							transFlows.addAll(list);
						}
					}

				}
			}
		}
		return transFlows;
	}

	
	
	private List<CcbChinaDebitCardBillDetails> parserResult(Elements trs, String bankNum, String BranchDesc) {

		List<CcbChinaDebitCardBillDetails> transFlows = new ArrayList<CcbChinaDebitCardBillDetails>();
		
		for (Element tr : trs) {
			try {
				
				Elements tds = tr.getElementsByTag("td");
				
				/**
				 * 收入 【金额】 1）金额列为下载数据中的收入金额
				 */
				String income = parserData(tds.get(3),0);
				
				//收入列金额为“0”的数据不做上传
				if("-".equals(income)){
					continue;
				}
				
				// 摘要
				String digest = parserData(tds.get(7),5);
				if ("结息".equals(digest) || "利息存入".equals(digest)) {
					continue;
				}
				
				//交易地点
				String dealPlace = parserData(tds.get(8),5);
				// 对方账户
				String reciprocalAccountNum = parserData(tr.select("tr>script").first(),0);
				
				/**
				 * 对方户名 【存款人】 【实际存管人】列取值逻辑顺序如下： 
				 * 1）、【对方户名】为空，则【实际存管人】保存为“无”；
				 * 2）、【对方户名】前3个字符=“支付宝”，则【实际存管人】 = 【交易地点】去掉“支付宝转账”后的字符串 
				 * 3）、【实际存管人】=【对方户名】
				 */
				String name = parserData(tds.get(5),5);
				String nameNew = name;
				if (StringUtils.isBlank(name)) {
					nameNew = "无";
				} else if (name.contains("产品实时赎回户")) {
					continue;
				} else if (StringUtils.isNotBlank(reciprocalAccountNum) && name.equals("夏靖")
						&& (reciprocalAccountNum.endsWith("3763") || reciprocalAccountNum.endsWith("8683"))) {
					continue;
				} else if (name.startsWith("支付宝")) {
					if (dealPlace.contains("支付宝转账")) {
						nameNew = dealPlace.replace("支付宝转账", "");
					}
				}
				
				
				/**
				 * 【序号】 上传数据库中序号规则是按照顺序增加，且第一个序号由人工填写
				 */
				String num = "";
				/**
				 * 交易日期 【存款日期】 1），存款日期为下载日记账中的“交易日期”列
				 */
				String deal_date = tds.get(1).text();
				/**
				 * 【入账银行】 1），下载的哪个账号的数据，入账银行就填写哪个银行账号， 账号分别为 
				 * 1，6227000016510033763
				 * 2，6214880016868683
				 * 
				 */
				String account = bankNum;
				/**
				 * 【查账状态】 1），此列为恒定值，填写“未查账”字样
				 */
				String status = "未查账";
				/**
				 * 【合同编号】 1），此列无需填写任何，为空值
				 */
				String contract_num = "";
				/**
				 * 【查账日期】 1），此列无需填写任何，为空值
				 */
				String audit_date = "";
				//交易时间
				String time = parserData(tds.get(1),0);
				/**
				 * 【备注】 1），【交易地点】&【对方户名】&【交易时间】
				 */
				String remark = dealPlace+name+time;
				/**
				 * 卡号后四位
				 */
				String card_no = "";

				CcbChinaDebitCardBillDetails ccbChinaDebitCardTransFlow = new CcbChinaDebitCardBillDetails(num,
						deal_date, account, income, nameNew, status, contract_num, audit_date, remark, card_no,"");

				transFlows.add(ccbChinaDebitCardTransFlow);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return transFlows;
	}
	
	/**
	 * 
	 * @param td
	 * @param i  截取字符串位数
	 * @return
	 */
	public String parserData(Element td, int i) {
		
		try {
			if (StringUtils.isBlank(td.attr("title"))) {
				Elements scripts = td.getElementsByTag("script");
				if (null != scripts && scripts.size() > 0) {

					if (td.tagName().equals("script")) {
						String script = td.toString();
//						String leftStr = "accountProtect2(";
//						String rightStr = ")+\"'>\"";
//						String data = script.substring(script.indexOf(leftStr) + leftStr.length(), script.indexOf(rightStr) - 1);
//						return data;
						Pattern pattern = Pattern.compile("\\('(.+)'\\)");
						Matcher m = pattern.matcher(script);
						if (m.find()) {
							return m.group(1);
						} else {
							return "";
						}

					} else {
						String script = td.getElementsByTag("script").first().toString();
						String data = script.substring(script.indexOf("(") + 2, script.indexOf(")") - 1 - i);
						return data;
					}
				} else {
					return td.text();
				}
			} else {
				String title = td.attr("title");
				return title;
			}
		} catch (Exception e) {
			log.error("------------------"+e.toString());
			e.printStackTrace();
		}

		return "";

	}
	public static final Logger log = LoggerFactory.getLogger(CcbChinaController.class);
	/**
	 * @Des 最终结果页请求参数
	 * @param htmlPage
	 * @param aCC_NO
	 * @param branchid2
	 * @param aCC_TYPE_FLAG
	 * @param curTypeDesc
	 * @param agreeType
	 * @param branchDesc
	 * @param l_acc_type
	 * @param page
	 * @param currentPage
	 * @param l_userid
	 * @return
	 */
	private String getDetailParam(HtmlPage htmlPage, String aCC_NO, String branchid2, String aCC_TYPE_FLAG,
			String curTypeDesc, String agreeType, String branchDesc, String l_acc_type, String page, String currentPage,
			String l_userid,String a_str,String filesearchstr,String flagnext) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
		Element username = doc.getElementById("l_username");
		String l_username = username.attr("value");

		String param = "ACC_NO="+aCC_NO
				+ "&ACCSIGN="+ACC_SIGN+"|"+PDT_CODE+"|人民币|0|0"
				+ "&START_DATE=20100101"
				+ "&END_DATE="+sdf.format(new Date())
				+ "&xiaxian="
				+ "&shangxian="
				+ "&select="
				+ "&yzacc="
				+ "&yzflag=0"
				+ "&yztxcode=310200"
				+ "&flagnext="+flagnext//首页：1  下一页 ：4
				+ "&INOUTFLAG="
				+ "&QUEFlag=1"
				+ "&AMTDOWN="
				+ "&AMTUP="
				+ "&TXCODE=310200"
				+ "&SKEY="+SKEY
				+ "&USERID="+l_userid
				+ "&SEND_USERID="
				+ "&STR_USERID="+l_userid
				+ "&BRANCHID="+branchid2
				+ "&PAGE="+page
				+ "&CURRENT_PAGE="+currentPage
				+ "&PDT_CODE="+PDT_CODE
				+ "&BANK_NAME="+branchDesc
				+ "&ACC_TYPE_NAME="
				+ "&CUR_NAME="
				+ "&ACC_ALIAS="
				+ "&A_STR="+a_str//
//				+ "&A_STR="//
				+ "&A_STR_TEMP="
				+ "&IS_UPDATE=1"
				+ "&UPDATE_DETAIL=1"
				+ "&v_acc="+aCC_NO
				+ "&v_sign="+ACC_SIGN+"|"+PDT_CODE
				+ "&DEPOSIT_BKNO=0"
				+ "&SEQUENCE_NO=0"
				+ "&ACCTYPE2="+l_acc_type
				+ "&LUGANGTONG=0"
				+ "&CURRENCE_NAME=人民币"
				+ "&B_STR="
				+ "&v_acc2="+aCC_NO
//				+ "&v_sign2="+ACC_SIGN //
				+ "&v_sign2="+aCC_NO
				+ "&QUERY_ACC_DETAIL_FLAG=310201"
				+ "&v_acc_type=0"
				+ "&ACC_SIGN=" + ACC_SIGN
//				+ "&ACC_SIGN_TEM="+ACC_SIGN //
				+ "&ACC_SIGN_TEM="+aCC_NO
				+ "&COMPLETENESS="//
//				+ "&COMPLETENESS=0"//
				+ "&FILESEARCHSTR="+filesearchstr//
				+ "&zcAllTmp=0.00"
				+ "&srAllTmp=0.00"
				+ "&clientFileName="
				+ "&l_acc_no="+aCC_NO
				+ "&l_acc_no_u="+aCC_NO
//				+ "&l_acc_no_u=6217000010*****4280"
				+ "&l_acc_al="
				+ "&l_branch="+branchDesc
				+ "&l_branchcode="+branchid2
				+ "&l_acc_sign="+aCC_NO
				+ "&l_acc_type="+l_acc_type
				+ "&l_cur_desc=人民币"
				+ "&l_acc_e=0"
				+ "&l_userid="+l_userid
				+ "&l_username="+l_username;
		
		
		System.out.println("param-------"+param);

		return param;
	}

	/**
	 * @Des 得到url13所需的两个参数
	 * @param html
	 */
	private void getAccSignAndPdtCode(HtmlPage html) {
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());

		Element es = doc.select("a:contains(明细)").first();
		String onclick = es.attr("onclick");

		String last = onclick.substring(onclick.indexOf("|") + 1, onclick.indexOf(")") - 1);
		String[] params = last.split("\\|");

		ACC_SIGN = params[0];
		PDT_CODE = params[1];
	}

	/**
	 * @Des 获取url12 请求所需参数
	 * @param page11
	 * @return
	 */
	private List<String> getUrlParam(HtmlPage page11) {

		List<String> params = new ArrayList<String>();
		Document doc = Jsoup.parse(page11.getWebResponse().getContentAsString());

		Elements lis = doc
				.select("[names=ACC_NO|BRANCHID|ACC_TYPE_FLAG|CurTypeDesc|AccAlias|AgreeType|BranchDesc|TXCODE]");
		if (null != lis) {
			for (Element li : lis) {
				if (li.text().contains("信用卡")) {
				} else {
					String value = li.attr("values");
					params.add(value);
				}
			}
		}
		return params;
	}

	/**
	 * @Des 前11个请求。
	 * @param bankJsonBean
	 * @return
	 */
	public HtmlPage getAccountQueryResult(String loginName) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("X-Requested-With", "XMLHttpRequest");

//		/**
//		 * 可删
//		 */
//		String url1 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=NCST02&BLKTYPE=ALL&ISPRIVATE=0&_="
//				+ System.currentTimeMillis();
//		getHtml(url1, null, null);
//
//		/**
//		 * 可删
//		 */
//		String url2 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N31010&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY;
//		getHtml(url2, null, null);
//		/**
//		 * 可删
//		 */
//		String url3 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&PT_LANGUAGE=CN&PT_STYLE=1&TXCODE=N60003&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY;
//		getHtml(url3, null, null);
//		/**
//		 * 可删
//		 */
//		String url4 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=JF0000";
//		getHtml(url4, null, map);
//		/**
//		 * 可删
//		 */
//		String url5 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=N00998";
//		getHtml(url5, null, map);
//		/**
//		 * 可删
//		 */
//		String url6 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=FULLMENU";
//		getHtml(url6, null, map);
//		/**
//		 * 可删
//		 */
//		String url7 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N12000&SKEY="
//				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&Udt_Ind=0&isAjaxRequest=true&getTime="
//				+ System.currentTimeMillis();
//		getHtml(url7, null, null);
//		/**
//		 * 可删
//		 */
//		String url8 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=IDXFRM06&PREFIXONLY=1";
//		getHtml(url8, null, null);
//		/**
//		 * 可删
//		 */
//		String url9 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=EBD001&ADNO=IDXFRM03&PREFIXONLY=1";
//		getHtml(url9, null, null);
//		/**
//		 * 可删
//		 */
//		String url10 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&USERID="
//				+ loginName + "&BRANCHID=" + BRANCHID + "&SKEY=" + SKEY + "&TXCODE=310103";
//		getHtml(url10, null, null);

		
		String url11 = "https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_11?SERVLET_NAME=B2CMainPlat_11&CCB_IBSVersion=V6&PT_STYLE=1&TXCODE=N31000&SKEY="
				+ SKEY + "&USERID=" + loginName + "&BRANCHID=" + BRANCHID + "&SELECT_TYPE=all";
		HtmlPage page11 = getHtml(url11, null, null);

		return page11;

	}

	public HtmlPage getHtml(String url, String params, Map<String, String> headers) {

		HtmlPage page = null;
		try {
			WebRequest request = null;
			if (null == params) {
				request = new WebRequest(new URL(url), HttpMethod.GET);
			} else {
				request = new WebRequest(new URL(url), HttpMethod.POST);
				request.setRequestBody(params);
			}

			if (null != headers) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					request.setAdditionalHeader(entry.getKey(), entry.getValue());
				}
			}
			request.setAdditionalHeader("Connection", "keep-alive");
			request.setAdditionalHeader("Host", "ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("Origin", "https://ibsbjstar.ccb.com.cn");
			request.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

			page = webClient.getPage(request);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return page;

	}


	public WebDriver getPageByChrome(String url) {

		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPathChrome);

		if (driverPathChrome == null) {
			throw new RuntimeException("webdriver.chrome.driver 初始化失败！");
		}

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("disable-gpu");

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);

		driver.get(url);

		return driver;

	}


	public static WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
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
