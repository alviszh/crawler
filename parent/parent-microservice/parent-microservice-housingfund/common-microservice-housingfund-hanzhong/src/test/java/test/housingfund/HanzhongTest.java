package test.housingfund;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.hanzhong.HousingHanzhongPay;
import com.microservice.dao.entity.crawler.housing.hanzhong.HousingHanzhongUserInfo;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanUserInfo;
import com.microservice.dao.entity.crawler.housing.xiangyang.HousingXiangyangPay;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class HanzhongTest {

	private static String fileSavePath = "D:\\img\\ls";

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String cookie = "[{\"domain\":\"www.tygjj.com\",\"key\":\"SESSION\",\"value\":\"529cd9dc-67b6-4a37-961e-dd69cdc83445\"},{\"domain\":\"www.tygjj.com\",\"key\":\"td_cookie\",\"value\":\"18446744072343004340\"},{\"domain\":\"www.tygjj.com\",\"key\":\"token\",\"value\":\"d2438602005b4da58e20f6461189fd9a\"}]";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			// cookie = login(webClient, "18671955558", "mc12zyf85");
			// webClient = addcookie(cookie);
			// getUserInfo(webClient);
			 getUserInfo();
			// loginqzdl_div();

			// cookie = login1(webClient, "18671955558", "mc12zyf85");

			// getImg(webClient);

//			TaskHousing taskHousing = new TaskHousing();
//			taskHousing.setTaskid("1111111");
//			JSONArray jsonObject = JSONArray.fromObject(htmlPayParser("", taskHousing));
//			System.out.println(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private static List<HousingXiangyangPay> htmlPayParser(String html, TaskHousing taskHousing) {

		List<HousingXiangyangPay> list = new ArrayList<HousingXiangyangPay>();
		try {
			File file = new File("C:\\Users\\Administrator\\Desktop\\xyjcmx.txt");
			html = txt2String(file);

			HousingXiangyangPay housingXiangyangPay = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String results = jsonObj.getString("results");
			Object obj = new JSONTokener(results).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 记账日期
				String jzrq = jsonObject.getString("jzrq");
				// 归集和提取业务类型
				String gjhtqywlx = jsonObject.getString("gjhtqywlx");
				// 发生额
				String fse = jsonObject.getString("fse");
				// 发生利息额
				String fslxe = jsonObject.getString("fslxe");
				// 个人余额
				String grzhye = jsonObject.getString("grzhye");
				// 提取原因
				String tqyy = jsonObject.getString("tqyy");
				// 提取方式
				String tqfs = jsonObject.getString("tqfs");

				housingXiangyangPay = new HousingXiangyangPay("", jzrq, gjhtqywlx, fse, fslxe, grzhye, tqyy, tqfs);
				list.add(housingXiangyangPay);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 记账日期
					String jzrq = jsonObject.getString("jzrq");
					// 归集和提取业务类型
					String gjhtqywlx = jsonObject.getString("gjhtqywlx");
					// 发生额
					String fse = jsonObject.getString("fse");
					// 发生利息额
					String fslxe = jsonObject.getString("fslxe");
					// 个人余额
					String grzhye = jsonObject.getString("grzhye");
					// 提取原因
					String tqyy = jsonObject.getString("tqyy");
					// 提取方式
					String tqfs = jsonObject.getString("tqfs");

					housingXiangyangPay = new HousingXiangyangPay("", jzrq, gjhtqywlx, fse, fslxe, grzhye, tqyy, tqfs);
					list.add(housingXiangyangPay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public static String getImg(WebClient webClient) throws Exception {

		String url = "https://www.xyzfgjj.cn/wt-web/jcr/jcrzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&grxx=00307964&blqd=wt_02&ksrq=1970-01-01&jsrq=2018-04-18&page=1&size=6&singlePage=true&fontSize=13px&pageNum=1&pageSize=10&totalcount=16&pages=2&random=0.5664358879612468";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("Cookie", "SESSION=c7606769-a3a9-45e1-ba70-629977475756; token=967def565a284a0a871a199a19e654eb");
		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, null, null, map);
		if (null != searchPage) {
			// String filePath = getImagePath(searchPage);
			// System.out.println("html----------------------" + filePath);
			String contentAsString = searchPage.getWebResponse().getContentAsString();
			System.out.println("contentAsString----------------------" + contentAsString);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	public static String getImg1(WebClient webClient) throws Exception {

		String url = "https://www.xyzfgjj.cn/wt-web/jcr/jcrzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&grxx=00307964&blqd=wt_02&ksrq=1970-01-01&jsrq=2018-04-18&page=1&size=6&singlePage=true&fontSize=13px&pageNum=1&pageSize=10&totalcount=16&pages=2&random=0.5664358879612468";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("Cookie", "SESSION=c7606769-a3a9-45e1-ba70-629977475756; token=967def565a284a0a871a199a19e654eb");
		Page searchPage = getPage(webClient, url, HttpMethod.POST, null, null, null, map);
		if (null != searchPage) {
			// String filePath = getImagePath(searchPage);
			// System.out.println("html----------------------" + filePath);
			String contentAsString = searchPage.getWebResponse().getContentAsString();
			System.out.println("contentAsString----------------------" + contentAsString);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	public static String login1(WebClient webClient, String username, String password) throws Exception {

		String url = "https://www.xyzfgjj.cn/wt-web/grlogin";

		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);

		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		if (null != searchPage) {
			HtmlImage image2 = searchPage.getFirstByXPath("//img[@id='captcha_img']");
			String verifycode2 = "1902";
			String imageName2 = "222.jpg";
			File file2 = new File("D:\\img\\" + imageName2);
			try {
				image2.saveAs(file2);
				String imgagePath = file2.getAbsolutePath();
				verifycode2 = ocrService.callChaoJiYingService(imgagePath, "1902");
				System.out.println("验证码222222==========" + verifycode2);
			} catch (Exception e) {
				System.out.println("图片有误");
			}
			HtmlTextInput inputforce_captcha = (HtmlTextInput) searchPage.querySelector("input[id='force_captcha']");
			if (inputforce_captcha == null) {
				throw new Exception("code input text can not found :" + "input[id='force_captcha']");
			} else {
				inputforce_captcha.reset();
				inputforce_captcha.setText(verifycode2);
			}
			HtmlButton loginButton2 = (HtmlButton) searchPage.querySelector("#qzdl");
			searchPage = loginButton2.click();
			String login = searchPage.getWebResponse().getContentAsString();
			System.out.println("login-------------" + login);

			HtmlImage image = searchPage.getFirstByXPath("//img[@id='captcha_img']");
			String verifycode = "1902";
			String imageName = "111.jpg";
			File file = new File("D:\\img\\" + imageName);
			try {
				image.saveAs(file);
				String imgagePath = file.getAbsolutePath();
				verifycode = ocrService.callChaoJiYingService(imgagePath, "1902");
				System.out.println("验证码==========" + verifycode);
			} catch (Exception e) {
				System.out.println("图片有误");
			}

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[id='username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage.querySelector("input[id='in_password']");
			if (inputpassword == null) {
				throw new Exception("password input text can not found :" + "input[id='in_password']");
			} else {
				inputpassword.reset();
				inputpassword.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='captcha']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[id='captcha']");
			} else {
				inputuserjym.reset();
				inputuserjym.setText(verifycode);
			}
			HtmlButtonInput loginButton = (HtmlButtonInput) searchPage.querySelector("input[id='gr_login']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginButton.click();
				// Thread.sleep(10000);
				// webClient.waitForBackgroundJavaScript(30000);
				// String login =
				// searchPage.getWebResponse().getContentAsString();
				System.out.println("login-------------" + login);
				if (!login.contains("个人网厅登录页")) {
					System.out.println("=================+成功");
				} else {
					System.out.println("=================+失败");
					Document doc = Jsoup.parse(login);
					String username_tip = doc.getElementById("username_tip").text();
					System.out.println("username_tip-------------" + username_tip);

					String pwd_tip = doc.getElementById("pwd_tip").text();
					System.out.println("pwd_tip-------------" + pwd_tip);

					String yzm_tip = doc.getElementById("yzm_tip").text();
					System.out.println("yzm_tip-------------" + yzm_tip);

					if (pwd_tip.contains("错误")) {
						System.out.println("===================错误");
					}
					if (yzm_tip.contains("验证码错误")) {
						System.out.println("===================验证码错误");
					}

					String qzdl_div = doc.getElementById("qzdl_div").attr("aria-hidden");
					System.out.println("qzdl_div-----------" + qzdl_div);
					if (qzdl_div.equals("true")) {
						// HtmlImage image2 =
						// searchPage.getFirstByXPath("//img[@id='captcha_img']");
						// String verifycode2 = "1902";
						// String imageName2 = "222.jpg";
						// File file2 = new File("D:\\img\\" + imageName2);
						// try {
						// image2.saveAs(file2);
						// String imgagePath = file2.getAbsolutePath();
						// verifycode2 =
						// ocrService.callChaoJiYingService(imgagePath, "1902");
						// System.out.println("验证码222222=========="+verifycode2);
						// } catch (Exception e) {
						// System.out.println("图片有误");
						// }
						// HtmlTextInput inputforce_captcha = (HtmlTextInput)
						// searchPage.querySelector("input[id='force_captcha']");
						// if (inputforce_captcha == null) {
						// throw new Exception("code input text can not found :"
						// + "input[id='force_captcha']");
						// } else {
						// inputforce_captcha.reset();
						// inputforce_captcha.setText(verifycode2);
						// }
						// HtmlButton loginButton2 = (HtmlButton)
						// searchPage.querySelector("#qzdl");
						searchPage = loginButton2.click();
						String login2 = searchPage.getWebResponse().getContentAsString();
						System.out.println("login2-------------" + login2);
						if (!login2.contains("个人网厅登录页")) {
							System.out.println("=================+成功");
						} else {
							System.out.println("=================+失败");
							Document doc2 = Jsoup.parse(login2);
							String username_tip2 = doc2.getElementById("username_tip").text();
							System.out.println("username_tip-------------" + username_tip2);

							String pwd_tip2 = doc2.getElementById("pwd_tip").text();
							System.out.println("pwd_tip2-------------" + pwd_tip2);

							String yzm_tip2 = doc2.getElementById("yzm_tip").text();
							System.out.println("yzm_tip2-------------" + yzm_tip2);

							if (pwd_tip2.contains("错误")) {
								System.out.println("===================错误");
							}
							if (yzm_tip2.contains("验证码错误")) {
								System.out.println("===================验证码错误");
							}
						}
					}
				}
			}
		}
		// String cookieJson = CommonUnit.transcookieToJson(webClient);
		// System.out.println("cookieJson-------------" + cookieJson);
		// return cookieJson;
		return "";

	}

	public static void loginqzdl_div() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xiangyanglogin.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		String qzdl_div = doc.getElementById("qzdl_div").attr("aria-hidden");
		System.out.println("qzdl_div-------------" + qzdl_div);
	}

	private static void getUserInfo() throws Exception {

		File file = new File("C:\\Users\\Administrator\\Desktop\\hanzhongUser.txt");
		String json = txt2String(file);
		List<HousingHanzhongUserInfo> list = new ArrayList<HousingHanzhongUserInfo>();
		HousingHanzhongUserInfo housingHanzhongUserInfo = null;
		JSONObject jsonObj = JSONObject.fromObject(json);
		String results = jsonObj.getString("results");
		Object obj = new JSONTokener(results).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 姓名
			String xingming = jsonObject.getString("xingming");
			// 出生年月
			String csny = jsonObject.getString("csny");
			// 性别
			String xingbie = jsonObject.getString("xingbie");
			// 证件类型
			String zjlx = jsonObject.getString("zjlx");
			// 证件号码
			String zjhm = jsonObject.getString("zjhm");
			// 手机号码
			String sjhm = jsonObject.getString("sjhm");
			// 固定电话号码
			String gddhhm = jsonObject.getString("gddhhm");
			// 邮政编码
			String yzbm = jsonObject.getString("yzbm");
			// 家庭月收入
			String jtysr = jsonObject.getString("jtysr");
			// 家庭住址
			String jtzz = jsonObject.getString("jtzz");
			// 婚姻状况
			String hyzk = jsonObject.getString("hyzk");
			// 贷款情况
			String dkqk = jsonObject.getString("dkqk");
			// 账户账号
			String grzh = jsonObject.getString("grzh");
			// 账户状态
			String grzhzt = jsonObject.getString("grzhzt");
			// 账户余额
			String grzhye = jsonObject.getString("grzhye");
			// 开户日期
			String djrq = jsonObject.getString("djrq");
			// 单位名称
			String dwmc = jsonObject.getString("dwmc");
			// 缴存比例
			String jcbl = jsonObject.getString("jcbl");
			// 个人缴存基数
			String grjcjs = jsonObject.getString("grjcjs");
			// 月缴存额
			String yjce = jsonObject.getString("yjce");
			// 个人存款账户开户银行名称
			String grckzhkhyhmc = jsonObject.getString("grckzhkhyhmc");
			// 个人存款账户号码
			String grckzhhm = jsonObject.getString("grckzhhm");
			housingHanzhongUserInfo = new HousingHanzhongUserInfo("", xingming, csny, xingbie, zjlx,
					zjhm, sjhm, gddhhm, yzbm, jtysr, jtzz, hyzk, dkqk, grzh, grzhzt, grzhye, djrq, dwmc, jcbl, grjcjs, yjce,
					grckzhkhyhmc, grckzhhm);
			list.add(housingHanzhongUserInfo);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 姓名
				String xingming = jsonObject.getString("xingming");
				// 出生年月
				String csny = jsonObject.getString("csny");
				// 性别
				String xingbie = jsonObject.getString("xingbie");
				// 证件类型
				String zjlx = jsonObject.getString("zjlx");
				// 证件号码
				String zjhm = jsonObject.getString("zjhm");
				// 手机号码
				String sjhm = jsonObject.getString("sjhm");
				// 固定电话号码
				String gddhhm = jsonObject.getString("gddhhm");
				// 邮政编码
				String yzbm = jsonObject.getString("yzbm");
				// 家庭月收入
				String jtysr = jsonObject.getString("jtysr");
				// 家庭住址
				String jtzz = jsonObject.getString("jtzz");
				// 婚姻状况
				String hyzk = jsonObject.getString("hyzk");
				// 贷款情况
				String dkqk = jsonObject.getString("dkqk");
				// 账户账号
				String grzh = jsonObject.getString("grzh");
				// 账户状态
				String grzhzt = jsonObject.getString("grzhzt");
				// 账户余额
				String grzhye = jsonObject.getString("grzhye");
				// 开户日期
				String djrq = jsonObject.getString("djrq");
				// 单位名称
				String dwmc = jsonObject.getString("dwmc");
				// 缴存比例
				String jcbl = jsonObject.getString("jcbl");
				// 个人缴存基数
				String grjcjs = jsonObject.getString("grjcjs");
				// 月缴存额
				String yjce = jsonObject.getString("yjce");
				// 个人存款账户开户银行名称
				String grckzhkhyhmc = jsonObject.getString("grckzhkhyhmc");
				// 个人存款账户号码
				String grckzhhm = jsonObject.getString("grckzhhm");
				housingHanzhongUserInfo = new HousingHanzhongUserInfo("", xingming, csny, xingbie, zjlx,
						zjhm, sjhm, gddhhm, yzbm, jtysr, jtzz, hyzk, dkqk, grzh, grzhzt, grzhye, djrq, dwmc, jcbl, grjcjs, yjce,
						grckzhkhyhmc, grckzhhm);
				list.add(housingHanzhongUserInfo);
			}
		}
		JSONArray jsonObject1 = JSONArray.fromObject(list);
		System.out.println(jsonObject1);

	}

	public static String getUserInfo(WebClient webClient) throws Exception {
		String url = "https://www.xyzfgjj.cn/wt-web/jcr/jcrkhxxcx_mh.service";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		paramsList.add(new NameValuePair("ffbm", "01"));
		paramsList.add(new NameValuePair("ywfl", "01"));
		paramsList.add(new NameValuePair("ywlb", "99"));
		paramsList.add(new NameValuePair("cxlx", "01"));

		Map<String, String> map = new HashMap<String, String>();
		// map.put("Cookie",
		// "SESSION=bef95916-90dd-4858-b8b2-59655e6d03af;
		// token=2a919ff9b9db44d5bd925a93400cffd7");

		Page searchPage = getPage(webClient, url, HttpMethod.POST, paramsList, null, null, map);
		if (null != searchPage) {
			String html = searchPage.getWebResponse().getContentAsString();
			System.out.println("html----------------------" + html);
		}
		String cookieJson = CommonUnit.transcookieToJson(webClient);

		return cookieJson;

	}

	public static String login(WebClient webClient, String username, String password) throws Exception {

		String url = "https://www.xyzfgjj.cn/wt-web/grlogin";

		HtmlPage searchPage = getHtmlPage(webClient, url, null, null);
		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		if (null != searchPage) {
			HtmlImage image = searchPage.getFirstByXPath("//img[@id='captcha_img']");
			String verifycode = "1902";
			String imageName = "111.jpg";
			File file = new File("D:\\img\\" + imageName);
			try {
				image.saveAs(file);
				String imgagePath = file.getAbsolutePath();
				verifycode = ocrService.callChaoJiYingService(imgagePath, "1902");
				System.out.println("验证码==========" + verifycode);
			} catch (Exception e) {
				System.out.println("图片有误");
			}

			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[id='username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(username);
			}
			HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage.querySelector("input[id='in_password']");
			if (inputpassword == null) {
				throw new Exception("password input text can not found :" + "input[id='in_password']");
			} else {
				inputpassword.reset();
				inputpassword.setText(password);
			}
			HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='captcha']");
			if (inputuserjym == null) {
				throw new Exception("code input text can not found :" + "input[id='captcha']");
			} else {
				inputuserjym.reset();
				inputuserjym.setText(verifycode);
			}
			HtmlButtonInput loginButton = (HtmlButtonInput) searchPage.querySelector("input[id='gr_login']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginButton.click();
				// Thread.sleep(10000);
				// webClient.waitForBackgroundJavaScript(30000);
				String login = searchPage.getWebResponse().getContentAsString();
				System.out.println("login-------------" + login);
				if (!login.contains("个人网厅登录页")) {
					System.out.println("=================+成功");
				} else {
					System.out.println("=================+失败");
					Document doc = Jsoup.parse(login);
					String username_tip = doc.getElementById("username_tip").text();
					System.out.println("username_tip-------------" + username_tip);

					String pwd_tip = doc.getElementById("pwd_tip").text();
					System.out.println("pwd_tip-------------" + pwd_tip);

					String yzm_tip = doc.getElementById("yzm_tip").text();
					System.out.println("yzm_tip-------------" + yzm_tip);

					if (pwd_tip.contains("错误")) {
						System.out.println("===================错误");
					}
					if (yzm_tip.contains("验证码错误")) {
						System.out.println("===================验证码错误");
					}

					String qzdl_div = doc.getElementById("qzdl_div").attr("aria-hidden");
					System.out.println("qzdl_div-----------" + qzdl_div);
					if (qzdl_div.equals("true")) {
						HtmlImage image2 = searchPage.getFirstByXPath("//img[@id='captcha_img']");
						String verifycode2 = "1902";
						String imageName2 = "222.jpg";
						File file2 = new File("D:\\img\\" + imageName2);
						try {
							image2.saveAs(file2);
							String imgagePath = file2.getAbsolutePath();
							verifycode2 = ocrService.callChaoJiYingService(imgagePath, "1902");
							System.out.println("验证码222222==========" + verifycode2);
						} catch (Exception e) {
							System.out.println("图片有误");
						}
						HtmlTextInput inputforce_captcha = (HtmlTextInput) searchPage
								.querySelector("input[id='force_captcha']");
						if (inputforce_captcha == null) {
							throw new Exception("code input text can not found :" + "input[id='force_captcha']");
						} else {
							inputforce_captcha.reset();
							inputforce_captcha.setText(verifycode2);
						}
						HtmlButton loginButton2 = (HtmlButton) searchPage.querySelector("#qzdl");
						searchPage = loginButton2.click();
						String login2 = searchPage.getWebResponse().getContentAsString();
						System.out.println("login2-------------" + login2);
						if (!login2.contains("个人网厅登录页")) {
							System.out.println("=================+成功");
						} else {
							System.out.println("=================+失败");
							Document doc2 = Jsoup.parse(login2);
							String username_tip2 = doc2.getElementById("username_tip").text();
							System.out.println("username_tip-------------" + username_tip2);

							String pwd_tip2 = doc2.getElementById("pwd_tip").text();
							System.out.println("pwd_tip2-------------" + pwd_tip2);

							String yzm_tip2 = doc2.getElementById("yzm_tip").text();
							System.out.println("yzm_tip2-------------" + yzm_tip2);

							if (pwd_tip2.contains("错误")) {
								System.out.println("===================错误");
							}
							if (yzm_tip2.contains("验证码错误")) {
								System.out.println("===================验证码错误");
							}
						}
					}
				}
			}
		}
		// String cookieJson = CommonUnit.transcookieToJson(webClient);
		// System.out.println("cookieJson-------------" + cookieJson);
		// return cookieJson;
		return "";

	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type, String body) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

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
			String code, String body, Map<String, String> map) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}

		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}

		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
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

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		String path = fileSavePath;
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		String imgagePath = path + "/" + imageName;
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

}
