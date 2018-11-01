package telecom.jiangsu.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBalance;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBill;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBillSum;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuCallRecord;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuMessage;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuPay;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuUserInfo;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class TestJiangsu {

	public static WebClient addcookie(String cookie) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookie);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	public static List<TelecomJiangsuBalance> list = new ArrayList<TelecomJiangsuBalance>();

	public static List<TelecomJiangsuPay> listPay = new ArrayList<TelecomJiangsuPay>();

	public static List<TelecomJiangsuBillSum> listBillSum = new ArrayList<TelecomJiangsuBillSum>();

	public static void main(String[] args) {

		String cookieT = "[{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"628c96057d20d0cb71132884618b5318\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"17712867723\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"96FE5121-DC39-4450-B77E-42F4B982E0AF\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"SKceXKjuQPmKcBujoYa8ZN27SfObZWPNO9178P7OfAwvIXZoa5Jgew%3D%3D\"},{\"domain\":\".189.cn\",\"key\":\"oldtime\",\"value\":\"17613\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX1%2ByeLC0uzdFy6p7BwzqFE9FO7vy3dJL50%2BjZtxnVcQO3l93eP%2FsQ0mK%2B0WaLZw2dezE1IF0wgsaxqzg12d0kOQ2KeBPT%2FWGWDE%3D\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"0\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"D95DECB8A7BF4F6AC68C8D312B528499-n3\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10011\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"2128905520\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"9BAAB3B72AFE3BC0\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"572\"},{\"domain\":\"js.189.cn\",\"key\":\"servJSessionID\",\"value\":\"2D0B10E1BCB35DCA9DD53E5DFF1CB529-an3\"},{\"domain\":\"www.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"415E475A97BFEC58-25ECB3C6E738FC91\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000029914209\"},{\"domain\":\"js.189.cn\",\"key\":\"nmallJSessionID\",\"value\":\"FD8A3502DF9111CFB8CF4473D6A59AE5-an3\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"js\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"C4CFABE41E7750FEA7EAA9059ECE01C1\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"884\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"IadK6UgKfwLowamkffFBioyVUUEic7P5JnWGaHDhjsU6hgZ1Fq7jlP2tQ5qjyM9Q0yMdmV5EEhLrgNytj%2FKkSyqQorfmicTwTLwGmoWzlnVsdbDjartUQZVDa9K4r3oQbou2nGoUD8U%3D\"}]";
		String cookie = "[{\"domain\":\".189.cn\",\"key\":\"loginStatus\",\"value\":\"logined\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsLoginToken\",\"value\":\"IadK6UgKfwLowamkffFBioyVUUEic7P5JnWGaHDhjsU6hgZ1Fq7jlP2tQ5qjyM9Q19bBSk5fjvef24PlQEwNB0yMbPBspw%2BCnzHnf0Gqp3cvx%2BoU04sMfyFEBf8ZNrU9dCynYIhW87I%3D\"},{\"domain\":\".189.cn\",\"key\":\"cityCode\",\"value\":\"js\"},{\"domain\":\".189.cn\",\"key\":\"trkId\",\"value\":\"F44DF225-F5F9-4A7D-87AD-9A790E668E7F\"},{\"domain\":\".189.cn\",\"key\":\"trkHmClickCoords\",\"value\":\"0\"},{\"domain\":\".189.cn\",\"key\":\"s_fid\",\"value\":\"2B0BCE61462284EF-1BA31C142A3949B1\"},{\"domain\":\".189.cn\",\"key\":\"userId\",\"value\":\"201%7C20170100000029914209\"},{\"domain\":\".189.cn\",\"key\":\"isLogin\",\"value\":\"logined\"},{\"domain\":\"login.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".189.cn\",\"key\":\".ybtj.189.cn\",\"value\":\"C4CFABE41E7750FEA7EAA9059ECE01C1\"},{\"domain\":\"graph.qq.com\",\"key\":\"__qc_wId\",\"value\":\"715\"},{\"domain\":\".189.cn\",\"key\":\"lvid\",\"value\":\"27d64cbb6817dc57ed59c1e69dd65fc0\"},{\"domain\":\".189.cn\",\"key\":\"aactgsh111220\",\"value\":\"17712867723\"},{\"domain\":\"login.189.cn\",\"key\":\"ECS_ReqInfo_login1\",\"value\":\"U2FsdGVkX187Oao86w1NwfzaYv5pBgaOoSgdZ0cuakzomwTLILDPkFTLoMQVPjeqpYmyEluEuR8%2B4G1Ast27k9VSSc5%2FB%2BTDy8DJ%2BY8M6io%3D\"},{\"domain\":\".189.cn\",\"key\":\"SHOPID_COOKIEID\",\"value\":\"10011\"},{\"domain\":\"www.189.cn\",\"key\":\"code_v\",\"value\":\"20170913\"},{\"domain\":\".189.cn\",\"key\":\"svid\",\"value\":\"2477243A285D43D8\"},{\"domain\":\".189.cn\",\"key\":\"oldtime\",\"value\":\"17614\"},{\"domain\":\"www.189.cn\",\"key\":\"JSESSIONID-JT\",\"value\":\"13CD670BF28DD636357E5147075D155B-n3\"},{\"domain\":\".login.189.cn\",\"key\":\"pgv_pvid\",\"value\":\"6088773953\"},{\"domain\":\"login.189.cn\",\"key\":\"__qc_wId\",\"value\":\"699\"},{\"domain\":\".189.cn\",\"key\":\"nvid\",\"value\":\"1\"},{\"domain\":\"js.189.cn\",\"key\":\"nmallJSessionID\",\"value\":\"0491B8B51729FF079791C83D635B2861-an2\"},{\"domain\":\"js.189.cn\",\"key\":\"servJSessionID\",\"value\":\"E1C12DA1F4247C7109C7DADD32FDC2DE-an2\"},{\"domain\":\".189.cn\",\"key\":\"s_cc\",\"value\":\"true\"},{\"domain\":\"login.189.cn\",\"key\":\"EcsCaptchaKey\",\"value\":\"2d7UatCv45c6NYdJZ3gWesKVb%2FhuuHeUVrp%2FZB4rOSKF6DLt6wFglA%3D%3D\"}]";

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			 String username = "18912102052";
			 String password = "100509";

			String username1 = "17789651834";
			String password1 = "060504";

//			String username = "17712867723";
//			String password = "550520";

//			webClient = addcookie(cookie);
			
			loginNew(webClient, username, password);
			
//			 webClient = login(webClient, username, password);
//			webClient = tiaozhuan(webClient);

			String beginTime = getDateBefore("yyyy-MM-dd", 0, -6, 0);
			String endTime = getDateBefore("yyyy-MM-dd", 0, 0, 0);
			System.out.println(beginTime);
			System.out.println(endTime);
			tonghua(webClient, beginTime, endTime);
			/**
			 * 上网
			 */
			// shangwang(webClient, username, beginTime, endTime);

			/**
			 * 短信
			 */
			// duanxin(webClient, username, beginTime, endTime);

			/**
			 * 通话
			 */
//			 tonghua(webClient, username, beginTime, endTime);

			/**
			 * 各种余额-----暂时不要
			 */
			// gezhongyue(webClient);

			/**
			 * 本月消费-----暂时不要
			 */
			// benyuexiaofei(webClient);

			// webClient = addcookie(cookieT);

			for (int i = 0; i < 6; i++) {
				String yearmonth = getDateBefore("yyyyMM",0, i,0);
				// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				// Calendar calendar = Calendar.getInstance();
				// calendar.setTime(StrToDate(yearmonth));
				// calendar.set(Calendar.DATE,
				// calendar.getActualMinimum(Calendar.DATE));
				// String beginTime = format.format(calendar.getTime());
				// calendar.set(Calendar.DATE,
				// calendar.getActualMaximum(Calendar.DATE));
				// String endTime = format.format(calendar.getTime());
				// System.out.println(yearmonth);
				/**
				 * 余额变动详情---------------------
				 */
				// yue(webClient, yearmonth);
				/**
				 * 充值缴费
				 */
				// chongzhi(webClient, yearmonth);

			}

			for (int i = 1; i < 7; i++) {
				String yearmonth = getDateBefore("yyyyMM", 0,i,0);

				// System.out.println(yearmonth);

				/**
				 * 合计
				 */
				// billsum(webClient, username, yearmonth);

				/**
				 * 账单
				 */
				// bill(webClient, username, yearmonth);

			}
			// System.out.println(listBill.toString());
			// System.out.println(listBillSum.toString());

			/**
			 * 余额变动详情---------------------
			 */
			// System.out.println(list.toString());
			/**
			 * 充值缴费
			 */
			// System.out.println(listPay.toString());

			/**
			 * 我的业务
			 */
			// yewu(webClient, username);
			// yewu2(webClient, username);

			/**
			 * 套餐使用情况
			 */
			// for (int i = 0; i < 6; i++) {
			// String yearmonth = getDateBefore("yyyyMM01", i);
			// System.out.println(yearmonth);
			// taocanshiyong(webClient, username, yearmonth);
			// }
			
			
//			billNew();
			/**
			 * 用户信息----------------
			 */
//			 userInfo(webClient);
//			 yewu(webClient);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 个人信息
	 */
	private static WebClient yewu(WebClient webClient) throws Exception {
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\yewu.txt");
		String json = txt2String(file);
		
		String myBusinessArr = "var myBusinessArr=";
		
		int i = json.indexOf(myBusinessArr);
		if (i > 0) {
			json = json.substring(i + myBusinessArr.length());
			String myBusinessArrSort = "}];";
			int j = json.indexOf(myBusinessArrSort);
			if (j > 0) {
				json = json.substring(0,j+2);
			}
		}
		
		List<TelecomJiangsuBusiness> list = new ArrayList<TelecomJiangsuBusiness>();
		TelecomJiangsuBusiness telecomJiangsuBusiness = null;

		Object obj = new JSONTokener(json).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 业务名称
			String offerSpecName = jsonObject.getString("offerSpecName");
			// 订购时间
			String startDt = jsonObject.getString("startDt");
			// 资费
			String referPrice = jsonObject.getString("bssPrice");
			telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, referPrice);
			list.add(telecomJiangsuBusiness);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 业务名称
				String offerSpecName = jsonObject.getString("offerSpecName");
				// 订购时间
				String startDt = jsonObject.getString("startDt");
				// 资费
				String referPrice = jsonObject.getString("bssPrice");
				telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, referPrice);
				list.add(telecomJiangsuBusiness);
			}
		}
		
		

		return webClient;
	}
	
	
	
	
	/**
	 * 个人信息
	 */
	private static WebClient userInfo(WebClient webClient) throws Exception {

//		String url = "http://js.189.cn/getSessionInfo.action";
//
//		Page html = getPage(webClient, url, HttpMethod.POST, null, false);
//
//		String json = html.getWebResponse().getContentAsString();

//		System.out.println(json);
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\userinfo.txt");
		String json = txt2String(file);
		
		Document doc = Jsoup.parse(json);
		// 登录账户
		String productId = getNextLabelByKeyword(doc, "登录账户");
		System.out.println(productId);
		// 用户姓名
		String userName = getchildByKeyword(doc, "用户姓名");
		System.out.println(userName);
		// 所在地区
		String areaCode = getchildByKeyword(doc, "所在地区");
		System.out.println(areaCode);
		// 客户姓名
		String customer_name = getNextLabelByKeyword(doc, "客户姓名");
		System.out.println(customer_name);
		// 证件类型
		String document_type = getNextLabelByKeyword(doc, "证件类型");
		System.out.println(document_type);
		// 证件号码
		String document_code = getNextLabelByKeyword(doc, "证件号码");
		System.out.println(document_code);
		// 通讯地址
		String userAddress = getNextLabelByKeyword(doc, "通讯地址");
		System.out.println(userAddress);
		// 通讯编码
		String zipCode = getNextLabelByKeyword(doc, "通讯编码");
		System.out.println(zipCode);
		// 联系人
		String contactPerson = getNextLabelByKeyword(doc, "联系人");
		System.out.println(contactPerson);
		// 联系电话
		String contactPhone = getNextLabelByKeyword(doc, "联系电话");
		System.out.println(contactPhone);
		// 电子邮件
		String email = getNextLabelByKeyword(doc, "电子邮件");
		System.out.println(email);
		
		
		
		

		return webClient;
	}
	
	
	
	private static void billNew() throws Exception {
		
		List<TelecomJiangsuBill> listBill = new ArrayList<TelecomJiangsuBill>();

		File file = new File("C:\\Users\\Administrator\\Desktop\\billmx.txt");
		String json = txt2String(file);
		
		JSONObject jsonObj = JSONObject.fromObject(json);
		//本期费用合计
		String dcc_charge = jsonObj.getString("dccCharge");
		//本期已付费用
		String dcc_bal_owe_used = jsonObj.getString("dccBalOweUsed");
		// 本期应付费用
		String dcc_owe_charge = jsonObj.getString("dccOweCharge");
		String consumptionList = jsonObj.getString("consumptionList");
		
		listBill = jiexi(listBill, consumptionList, dcc_charge, dcc_bal_owe_used, dcc_owe_charge, "", 0);
		
		
		JSONArray jsonArray = JSONArray.fromObject(listBill);
		System.out.println(jsonArray);

	}
	
	private static List<TelecomJiangsuBill> jiexi(List<TelecomJiangsuBill> listBill, String json, String dcc_charge,
			String dcc_bal_owe_used, String dcc_owe_charge,String yearmonth,int i) throws Exception {
		
		i++;
		TelecomJiangsuBill TelecomJiangsuBill = null;

		JSONArray jsonArray = JSONArray.fromObject(json);
		for (Object object : jsonArray) {
			System.out.println(i);
			
			JSONObject jsonObject = JSONObject.fromObject(object);
			// 计费名目
			String itemName = jsonObject.getString("dccBillItemName");
			// 金额
			String itemCharge = jsonObject.getString("dccBillFee");
			
			TelecomJiangsuBill = new TelecomJiangsuBill("", dcc_charge, dcc_bal_owe_used, dcc_owe_charge, itemName,
					itemCharge, yearmonth, i+"");
			listBill.add(TelecomJiangsuBill);
			// dccBillList
			String dccBillList = jsonObject.getString("dccBillList");
			jiexi(listBill, dccBillList, dcc_charge, dcc_bal_owe_used, dcc_owe_charge, yearmonth, i);
		}
		
		return listBill;
	}
	
	
	
	
	/**
	 * 通话
	 */
	private static WebClient tonghua(WebClient webClient,String begDate, String endDate)
			throws Exception {

		String url = "http://js.189.cn/nservice/listQuery/queryList";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("listType", "1"));
		paramsList.add(new NameValuePair("stTime", begDate));
		paramsList.add(new NameValuePair("endTime", endDate));

		Page html = getPage(webClient, "", url, HttpMethod.POST, paramsList, null, null, null);

		String json = html.getWebResponse().getContentAsString();
		System.out.println(json);

		List<TelecomJiangsuCallRecord> list = new ArrayList<TelecomJiangsuCallRecord>();
		try {
			TelecomJiangsuCallRecord telecomJiangsuCallRecord = null;

			JSONObject jsonObj = JSONObject.fromObject(json);

			String items = jsonObj.getString("respMsg");

			Object obj = new JSONTokener(items).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 对方号码
				String accNbr1 = jsonObject.getString("nbr");
				// 日期
				String startDateNew = jsonObject.getString("startDateNew");
				// 呼叫类型
				String ticketTypeNew = jsonObject.getString("ticketTypeNew");
				// 通话类型
				String durationType = jsonObject.getString("durationType");
				// 通话地区
				String areaCode = jsonObject.getString("areaCode");
				// 通话开始时间（时分秒）
				String startTimeNew = jsonObject.getString("startTimeNew");
				// 通话时长（时分秒）
				String durationCh = jsonObject.getString("duartionCh");
				// 通话时间（时分秒）
				String startTime = jsonObject.getString("startTime");
				// 金额（元）
				String ticketChargeCh = jsonObject.getString("ticketChargeCh");

				telecomJiangsuCallRecord = new TelecomJiangsuCallRecord("", accNbr1, startDateNew,
						ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
				list.add(telecomJiangsuCallRecord);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object1 : jsonArray) {
					JSONArray jsonArray2 = JSONArray.fromObject(object1);
					for (Object object : jsonArray2) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 对方号码
						String accNbr1 = jsonObject.getString("nbr");
						// 日期
						String startDateNew = jsonObject.getString("startDateNew");
						// 呼叫类型
						String ticketTypeNew = jsonObject.getString("ticketTypeNew");
						// 通话类型
						String durationType = jsonObject.getString("durationType");
						// 通话地区
						String areaCode = jsonObject.getString("areaCode");
						// 通话开始时间（时分秒）
						String startTimeNew = jsonObject.getString("startTimeNew");
						// 通话时长（时分秒）
						String durationCh = jsonObject.getString("duartionCh");
						// 通话时间（时分秒）
						String startTime = jsonObject.getString("startTime");
						// 金额（元）
						String ticketChargeCh = jsonObject.getString("ticketChargeCh");

						telecomJiangsuCallRecord = new TelecomJiangsuCallRecord("", accNbr1, startDateNew,
								ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
						list.add(telecomJiangsuCallRecord);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(list.toString());

		return webClient;
	}
	
	
	/**
	 * 登录
	 * 
	 * @param webClient
	 * @param name
	 * @param password
	 * @return
	 */
	private static WebClient loginNew(WebClient webClient, String name, String password) {

		try {
//			String url = "http://js.189.cn/nservice/login/toLogin?favurl=http://js.189.cn/index";
			String url = "http://js.189.cn/nservice/login/toLogin";
			HtmlPage html = getHtmlPage(webClient, url, null);
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='cellphone']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/div/input");
			username.setText(name);
			passwordInput.setText(password);
			HtmlImage valiCodeImg = html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/p[2]/span/img");
			
			ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
			String verifycode = "1902";
			String imageName = "111.jpg";
			File file = new File("D:\\img\\" + imageName);
			try {
				valiCodeImg.saveAs(file);
				String imgagePath = file.getAbsolutePath();
				verifycode = ocrService.callChaoJiYingService(imgagePath, "1902");
				System.out.println("验证码==========" + verifycode);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("图片有误");
			}
			
			
			HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/p[2]/input");
			valicodeStrinput.reset();
			valicodeStrinput.setText(verifycode);
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='login_byPhone']");
			HtmlPage htmlPage = button.click();
			
			String contentAsString = htmlPage.getWebResponse().getContentAsString();
			System.out.println("lllllllllllll_-------------" + contentAsString);
			Document doc = Jsoup.parse(contentAsString);
			Element showTimeMsgPupup = doc.getElementById("showTimeMsgPupup");
			
			if (showTimeMsgPupup != null) {
				System.out.println("登录失败");
				String text = showTimeMsgPupup.text();
				System.out.println("信息----" + text);
				if (text != null && !text.equals("")) {
					System.out.println(text);
				}
			} else {
				System.out.println("登录成功");
				String cookieString = CommonUnit.transcookieToJson(html.getWebClient());
				System.out.println("login---cookieString---------------------->" + cookieString);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return webClient;

	}
	
	
	
	
	

	/**
	 * 合计
	 */
	/*private static WebClient billsum(WebClient webClient, String accNbr, String billingCycleId) throws Exception {

		String url = "http://js.189.cn/chargeQuery/chargeQuery_queryCustBill.parser?billingCycleId=" + billingCycleId
				+ "&queryFlag=0&productId=2&accNbr=" + accNbr;

		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		TelecomJiangsuBillSum telecomJiangsuBillSum = null;

		JSONObject jsonObj = JSONObject.fromObject(json);

		String feeCountAndPresendtedFee = jsonObj.getString("feeCountAndPresendtedFee");

		Object obj = new JSONTokener(feeCountAndPresendtedFee).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 计费名目
			String itemName = jsonObject.getString("itemName");
			// 金额
			String itemCharge = jsonObject.getString("itemCharge");
			// 序号
			String treeNum = jsonObject.getString("treeNum");
			telecomJiangsuBillSum = new TelecomJiangsuBillSum("", itemName, itemCharge, billingCycleId, treeNum);
			listBillSum.add(telecomJiangsuBillSum);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 计费名目
				String itemName = jsonObject.getString("itemName");
				// 金额
				String itemCharge = jsonObject.getString("itemCharge");
				// 序号
				String treeNum = jsonObject.getString("treeNum");
				telecomJiangsuBillSum = new TelecomJiangsuBillSum("", itemName, itemCharge, billingCycleId, treeNum);
				listBillSum.add(telecomJiangsuBillSum);
			}
		}
		return webClient;
	}*/

	/**
	 * 跳转
	 */
	private static WebClient tiaozhuan(WebClient webClient) throws Exception {

		String url2 = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		HtmlPage html2 = getHtmlPage(webClient, url2, null);
		String json2 = html2.getWebResponse().getContentAsString();
		webClient = html2.getWebClient();

		return webClient;
	}

	/**
	 * 积分账单
	 */
	/*private static WebClient bill(WebClient webClient, String accNbr, String billingCycleId) throws Exception {

		// String url =
		// "http://js.189.cn/chargeQuery/chargeQuery_queryCustBill.parser?billingCycleId="
		// + billingCycleId
		// + "&queryFlag=0&productId=2&accNbr=" + accNbr;

		String url = "http://js.189.cn/chargeQuery/chargeQuery_queryCustBill.action?billingCycleId=" + billingCycleId
				+ "&queryFlag=0&productId=2&accNbr=" + accNbr;

		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		TelecomJiangsuBill telecomJiangsuBill = null;

		JSONObject jsonObj = JSONObject.fromObject(json);

		String custBillInfoList = jsonObj.getString("custBillInfoList");

		Object obj = new JSONTokener(custBillInfoList).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 计费名目
			String itemName = jsonObject.getString("itemName");
			// 金额
			String itemCharge = jsonObject.getString("itemCharge");
			// 序号
			String treeNum = jsonObject.getString("treeNum");
			telecomJiangsuBill = new TelecomJiangsuBill("", itemName, itemCharge, billingCycleId, treeNum);
			listBill.add(telecomJiangsuBill);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 计费名目
				String itemName = jsonObject.getString("itemName");
				// 金额
				String itemCharge = jsonObject.getString("itemCharge");
				// 序号
				String treeNum = jsonObject.getString("treeNum");
				telecomJiangsuBill = new TelecomJiangsuBill("", itemName, itemCharge, billingCycleId, treeNum);
				listBill.add(telecomJiangsuBill);
			}
		}
		return webClient;
	}*/


	/**
	 * 业务2
	 */
	private static WebClient yewu2(WebClient webClient, String accNbr) throws Exception {

		String url = "http://js.189.cn/ywbl/ywbl_queryPackageInfoList.action?accNbr=" + accNbr + "&userType=4";

		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		List<TelecomJiangsuBusiness> list = new ArrayList<TelecomJiangsuBusiness>();
		TelecomJiangsuBusiness telecomJiangsuBusiness = null;

		Object obj = new JSONTokener(json).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 业务名称
			String offerSpecName = jsonObject.getString("offerSpecName");
			// 订购时间
			String startDt = jsonObject.getString("startDt");
			// 资费
			String referPrice = jsonObject.getString("referPrice");
			telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, referPrice);
			list.add(telecomJiangsuBusiness);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 业务名称
				String offerSpecName = jsonObject.getString("offerSpecName");
				// 订购时间
				String startDt = jsonObject.getString("startDt");
				// 资费
				String referPrice = jsonObject.getString("referPrice");
				telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, referPrice);
				list.add(telecomJiangsuBusiness);
			}
		}

		System.out.println(list.toString());

		return webClient;
	}

	/**
	 * 业务
	 */
	private static WebClient yewu(WebClient webClient, String accNbr) throws Exception {

		// String url7 = "http://www.189.cn/js/";
		// HtmlPage html7 = getHtmlPage(webClient, url7, HttpMethod.GET);
		// String json7 = html7.getWebResponse().getContentAsString();
		// System.out.println("json7--------------" + json7);

		// String url2 = "http://js.189.cn/getSessionInfo.action";
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept", "application/json, text/javascript, */*");
		// map.put("X-Requested-With", "XMLHttpRequest");
		// map.put("Referer", "http://js.189.cn/service/bill?tabFlag=billing2");
		// map.put("Accept-Language", "zh-CN");
		// map.put("Content-Type", "text/plain;charset=UTF-8");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64;
		// Trident/7.0; rv:11.0) like Gecko");
		// map.put("Host", "js.189.cn");
		// map.put("Connection", "keep-alive");
		// map.put("Cache-Control", "no-cache");
		// map.put("Cookie",
		// "s_cc=true; loginStatus=logined; trkHmClickCoords=0;
		// dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83;
		// cityCode=js_nj; svid=A27BBF163B42BDD9;
		// s_fid=5AEF34516BFEDD57-26554793E2697896;
		// lvid=b666bdaa15ebd3ab55e97579bf031d35; nvid=1;
		// trkId=6ABC4F96-DD89-4E32-B76B-3C90DAAA7446; SHOPID_COOKIEID=10011;
		// aactgsh111220=17712867723; userId=201%7C20170100000029914209;
		// isLogin=logined; .ybtj.189.cn=C4CFABE41E7750FEA7EAA9059ECE01C1;
		// oldtime=17613;
		// NTKF_T2D_CLIENTID=guest22C59619-9B80-9DCB-579D-4C6B1A4952CA;
		// s_sq=eship-189-js%3D%2526pid%253Djs.189.cn%25252Fservice%25252Fbill%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257BcheckValidateValueZDCX%252528%252527check%252527%252529%25257D%2526oidt%253D2%2526ot%253DA%2526oi%253D1740%26eshipeship-189-all%3D%2526pid%253D%25252Fjs%25252F%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fwww.189.cn%25252Fdqmh%25252FfrontLinkSkip.do%25253Fmethod%25253Dskip%252526shopId%25253D10011%252526toStUrl%25253Dhttp%25253A%25252F%25252Fjs.189.cn%25252Fservice%25252Fbi%2526ot%253DA%2526oi%253D430;
		// nTalk_CACHE_DATA={uid:kf_9643_ISME9754_17712867723_2000004,tid:1521714055386710};
		// servJSessionID=914CA663B0FD776EC941FE3471DEE7E4-an2");
		// Page html2 = getPage(webClient, "", url2, HttpMethod.POST, null,
		// null, null, map);

		// String url6 = "http://www.189.cn/dqmh/my189/initMy189home.do";
		// String url6 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/ywbl/ywbl_goOwnBusinessPage.action";
		// HtmlPage html6 = getHtmlPage(webClient, url6, HttpMethod.GET);
		// String json6 = html6.getWebResponse().getContentAsString();
		// System.out.println("json6--------------" + json6);

		// String url7 = "http://www.189.cn/dqmh/my189/checkMy189Session.do?";
		// Page html7 = getPage(webClient, url7, HttpMethod.POST, null, false);
		// String jso7 = html6.getWebResponse().getContentAsString();
		// System.out.println("json6--------------" + json6);

		// String url5 = "http://js.189.cn/service/transaction";
		// HtmlPage html5 = getHtmlPage(webClient, url5, null);
		// String json5 = html5.getWebResponse().getContentAsString();
		// System.out.println("json5--------------" + json5);

		// String url4 = "http://js.189.cn/ywbl/ywbl_goOwnBusinessPage.action";
		// HtmlPage html4 = getHtmlPage(webClient, url4, null);
		// String json4 = html4.getWebResponse().getContentAsString();
		// System.out.println("json4--------------" + json4);

		// String url =
		// "http://js.189.cn/ywbl/mybl_queryMyBusinessList.parser?currentNum=" +
		// accNbr
		// + "&userType=4&prodId=";
		String url = "http://js.189.cn/ywbl/mybl_queryMyBusinessList.action?currentNum=" + accNbr
				+ "&userType=4&prodId=";
		Page html = getPage(webClient, url, HttpMethod.POST, null, false);
		String json = html.getWebResponse().getContentAsString();
		System.out.println("json-------------------" + json);

		List<TelecomJiangsuBusiness> list = new ArrayList<TelecomJiangsuBusiness>();
		TelecomJiangsuBusiness telecomJiangsuBusiness = null;
		JSONObject jsonObj = JSONObject.fromObject(json);
		String items = jsonObj.getString("queryAttachOfferByProdResp");

		Object obj = new JSONTokener(items).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 业务名称
			String offerSpecName = jsonObject.getString("offerSpecName");
			// 订购时间
			String startDt = jsonObject.getString("startDt");
			// 资费
			String fee = "";
			if (jsonObject.has("fee")) {
				fee = jsonObject.getString("fee");
			}
			telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, fee);
			list.add(telecomJiangsuBusiness);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 业务名称
				String offerSpecName = jsonObject.getString("offerSpecName");
				// 订购时间
				String startDt = jsonObject.getString("startDt");
				// 资费
				String fee = "";
				if (jsonObject.has("fee")) {
					fee = jsonObject.getString("fee");
				}
				telecomJiangsuBusiness = new TelecomJiangsuBusiness("", offerSpecName, startDt, fee);
				list.add(telecomJiangsuBusiness);
			}
		}

		System.out.println(list.toString());

		return webClient;
	}

	/**
	 * 短信
	 */
	private static WebClient duanxin(WebClient webClient, String accNbr, String begDate, String endDate)
			throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://js.189.cn/mobileInventoryAction.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("inventoryVo.accNbr", accNbr));
		paramsList.add(new NameValuePair("inventoryVo.getFlag", "3"));
		paramsList.add(new NameValuePair("inventoryVo.begDate", begDate));
		paramsList.add(new NameValuePair("inventoryVo.endDate", endDate));
		paramsList.add(new NameValuePair("inventoryVo.family", "4"));
		paramsList.add(new NameValuePair("inventoryVo.accNbr97", ""));
		paramsList.add(new NameValuePair("inventoryVo.productId", "4"));
		paramsList.add(new NameValuePair("inventoryVo.acctName", accNbr));

		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		List<TelecomJiangsuMessage> listmessage = new ArrayList<TelecomJiangsuMessage>();
		TelecomJiangsuMessage telecomJiangsuMessage = null;

		JSONObject jsonObj = JSONObject.fromObject(json);

		String items = jsonObj.getString("items");

		Object obj = new JSONTokener(items).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 对方号码
			String accNbr1 = jsonObject.getString("accNbr");
			// 业务类型
			String ticketType = jsonObject.getString("ticketType");
			// 发送日期（年月日）
			String startDateNew = jsonObject.getString("startDateNew");
			// 发送开始时间（时分秒）
			String startTimeNew = jsonObject.getString("startTimeNew");
			// 金额（元）
			String ticketChargeCh = jsonObject.getString("ticketChargeCh");
			// 产品名称
			String productName = jsonObject.getString("productName");

			telecomJiangsuMessage = new TelecomJiangsuMessage("", accNbr1, ticketType, startDateNew, startTimeNew,
					ticketChargeCh, productName);
			listmessage.add(telecomJiangsuMessage);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject fromObject = JSONObject.fromObject(object);
				// 对方号码
				String accNbr1 = fromObject.getString("accNbr");
				// 业务类型
				String ticketType = fromObject.getString("ticketType");
				// 发送日期（年月日）
				String startDateNew = fromObject.getString("startDateNew");
				// 发送开始时间（时分秒）
				String startTimeNew = fromObject.getString("startTimeNew");
				// 金额（元）
				String ticketChargeCh = fromObject.getString("ticketChargeCh");
				// 产品名称
				String productName = fromObject.getString("productName");
				telecomJiangsuMessage = new TelecomJiangsuMessage("", accNbr1, ticketType, startDateNew, startTimeNew,
						ticketChargeCh, productName);
				listmessage.add(telecomJiangsuMessage);

			}
		}
		System.out.println(listmessage.toString());
		return webClient;
	}

	/**
	 * 通话Old
	 */
	private static WebClient tonghua(WebClient webClient, String accNbr, String begDate, String endDate)
			throws Exception {

//		String url2 = "http://js.189.cn/getSessionInfo.action";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("Accept", "application/json, text/javascript, */*");
//		map.put("X-Requested-With", "XMLHttpRequest");
//		map.put("Referer", "http://js.189.cn/service/bill?tabFlag=billing2");
//		map.put("Accept-Language", "zh-CN");
//		map.put("Content-Type", "text/plain;charset=UTF-8");
//		map.put("Accept-Encoding", "gzip, deflate");
//		map.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
//		map.put("Host", "js.189.cn");
//		map.put("Connection", "keep-alive");
//		map.put("Cache-Control", "no-cache");
//		map.put("Cookie",
//				"s_cc=true; loginStatus=logined; trkHmClickCoords=0; dqmhIpCityInfos=%E5%8C%97%E4%BA%AC%E5%B8%82+%E8%81%94%E9%80%9A%E6%95%B0%E6%8D%AE%E4%B8%AD%E5%BF%83; cityCode=js_nj; svid=A27BBF163B42BDD9; s_fid=5AEF34516BFEDD57-26554793E2697896; lvid=b666bdaa15ebd3ab55e97579bf031d35; nvid=1; trkId=6ABC4F96-DD89-4E32-B76B-3C90DAAA7446; SHOPID_COOKIEID=10011; aactgsh111220=17712867723; userId=201%7C20170100000029914209; isLogin=logined; .ybtj.189.cn=C4CFABE41E7750FEA7EAA9059ECE01C1; oldtime=17613; NTKF_T2D_CLIENTID=guest22C59619-9B80-9DCB-579D-4C6B1A4952CA; s_sq=eship-189-js%3D%2526pid%253Djs.189.cn%25252Fservice%25252Fbill%2526pidt%253D1%2526oid%253Dfunctiononclick%252528event%252529%25257BcheckValidateValueZDCX%252528%252527check%252527%252529%25257D%2526oidt%253D2%2526ot%253DA%2526oi%253D1740%26eshipeship-189-all%3D%2526pid%253D%25252Fjs%25252F%2526pidt%253D1%2526oid%253Dhttp%25253A%25252F%25252Fwww.189.cn%25252Fdqmh%25252FfrontLinkSkip.do%25253Fmethod%25253Dskip%252526shopId%25253D10011%252526toStUrl%25253Dhttp%25253A%25252F%25252Fjs.189.cn%25252Fservice%25252Fbi%2526ot%253DA%2526oi%253D430; nTalk_CACHE_DATA={uid:kf_9643_ISME9754_17712867723_2000004,tid:1521714055386710}; servJSessionID=914CA663B0FD776EC941FE3471DEE7E4-an2");
//
//		Page html2 = getPage(webClient, "", url2, HttpMethod.POST, null, null, null, null);
//		String json2 = html2.getWebResponse().getContentAsString();
//		System.out.println("json2---------------------" + json2);

		String url = "http://js.189.cn/queryVoiceMsgAction.action";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("inventoryVo.accNbr", accNbr));
		paramsList.add(new NameValuePair("inventoryVo.getFlag", "3"));
		paramsList.add(new NameValuePair("inventoryVo.begDate", begDate));
		paramsList.add(new NameValuePair("inventoryVo.endDate", endDate));
		paramsList.add(new NameValuePair("inventoryVo.family", "4"));
		paramsList.add(new NameValuePair("inventoryVo.accNbr97", ""));
		paramsList.add(new NameValuePair("inventoryVo.productId", "4"));
		paramsList.add(new NameValuePair("inventoryVo.acctName", accNbr));

		Page html = getPage(webClient, "", url, HttpMethod.POST, paramsList, null, null, null);

		String json = html.getWebResponse().getContentAsString();
		System.out.println(json);

		List<TelecomJiangsuCallRecord> list = new ArrayList<TelecomJiangsuCallRecord>();
		TelecomJiangsuCallRecord telecomJiangsuCallRecord = null;

		JSONObject jsonObj = JSONObject.fromObject(json);

		String items = jsonObj.getString("items");

		Object obj = new JSONTokener(items).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 对方号码
			String accNbr1 = jsonObject.getString("nbr");
			// 日期
			String startDateNew = jsonObject.getString("startDateNew");
			// 呼叫类型
			String ticketTypeNew = jsonObject.getString("ticketTypeNew");
			// 通话类型
			String durationType = jsonObject.getString("durationType");
			// 通话地区
			String areaCode = jsonObject.getString("areaCode");
			// 通话开始时间（时分秒）
			String startTimeNew = jsonObject.getString("startTimeNew");
			// 通话时长（时分秒）
			String durationCh = jsonObject.getString("duartionCh");
			// 通话时间（时分秒）
			String startTime = jsonObject.getString("startTime");
			// 金额（元）
			String ticketChargeCh = jsonObject.getString("ticketChargeCh");

			telecomJiangsuCallRecord = new TelecomJiangsuCallRecord("", accNbr1, startDateNew,
					ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
			list.add(telecomJiangsuCallRecord);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 对方号码
				String accNbr1 = jsonObject.getString("nbr");
				// 日期
				String startDateNew = jsonObject.getString("startDateNew");
				// 呼叫类型
				String ticketTypeNew = jsonObject.getString("ticketTypeNew");
				// 通话类型
				String durationType = jsonObject.getString("durationType");
				// 通话地区
				String areaCode = jsonObject.getString("areaCode");
				// 通话开始时间（时分秒）
				String startTimeNew = jsonObject.getString("startTimeNew");
				// 通话时长（时分秒）
				String durationCh = jsonObject.getString("duartionCh");
				// 通话时间（时分秒）
				String startTime = jsonObject.getString("startTime");
				// 金额（元）
				String ticketChargeCh = jsonObject.getString("ticketChargeCh");

				telecomJiangsuCallRecord = new TelecomJiangsuCallRecord("", accNbr1, startDateNew,
						ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
				list.add(telecomJiangsuCallRecord);
				list.add(telecomJiangsuCallRecord);
			}
		}
		System.out.println(list.toString());

		return webClient;
	}

	/**
	 * 套餐使用情况
	 */
	/*private static WebClient taocanshiyong(WebClient webClient, String userID, String selectTime) throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		// String url =
		// "http://js.189.cn/userLogin/service/queryWare.parser?userID=" +
		// userID
		// + "&userType=2000004&selectTime=" + selectTime;
		String url = "http://js.189.cn/userLogin/service/queryWare.action?userID=" + userID
				+ "&userType=2000004&selectTime=" + selectTime;

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("Action", "post"));
		paramsList.add(new NameValuePair("Name", "forWare"));
		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		List<TelecomJiangsuComboUse> list = new ArrayList<TelecomJiangsuComboUse>();
		TelecomJiangsuComboUse telecomJiangsuComboUse = null;

		Object obj = new JSONTokener(json).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			// 开始时间
			String startTime = jsonObject.getString("startTime");
			// 失效时间
			String endTime = jsonObject.getString("endTime");
			// 总量
			String cumulationTotal = jsonObject.getString("cumulationTotal");
			// 剩余
			String cumulationLeft = jsonObject.getString("cumulationLeft");
			// 套餐名
			String offerName = jsonObject.getString("offerName");
			// 套餐分支名
			String accuName = jsonObject.getString("accuName");
			// 已使用
			String cumulationAlready = jsonObject.getString("cumulationAlready");
			// 单位
			String unitName = jsonObject.getString("unitName");
			// 是否不限
			String ratableResourceId = jsonObject.getString("ratableResourceId");

			telecomJiangsuComboUse = new TelecomJiangsuComboUse("", startTime, endTime, cumulationTotal, cumulationLeft,
					offerName, accuName, cumulationAlready, unitName, ratableResourceId);
			list.add(telecomJiangsuComboUse);

		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 开始时间
				String startTime = jsonObject.getString("startTime");
				// 失效时间
				String endTime = jsonObject.getString("endTime");
				// 总量
				String cumulationTotal = jsonObject.getString("cumulationTotal");
				// 剩余
				String cumulationLeft = jsonObject.getString("cumulationLeft");
				// 套餐名
				String offerName = jsonObject.getString("offerName");
				// 套餐分支名
				String accuName = jsonObject.getString("accuName");
				// 已使用
				String cumulationAlready = jsonObject.getString("cumulationAlready");
				// 单位
				String unitName = jsonObject.getString("unitName");
				// 是否不限
				String ratableResourceId = jsonObject.getString("ratableResourceId");

				telecomJiangsuComboUse = new TelecomJiangsuComboUse("", startTime, endTime, cumulationTotal,
						cumulationLeft, offerName, accuName, cumulationAlready, unitName, ratableResourceId);
				list.add(telecomJiangsuComboUse);
			}
		}
		System.out.println("---------------------------------------------------------------------");
		System.out.println(list.toString());

		return webClient;
	}*/

	/**
	 * 各种余额
	 */
	private static WebClient gezhongyue(WebClient webClient) throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://js.189.cn/feeQueryNew/query_callConQueryBalance.parser?dccQueryFlag=1";
		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		JSONObject jsonObj = JSONObject.fromObject(json);

		return webClient;
	}

	/**
	 * 本月消费
	 */
	private static WebClient benyuexiaofei(WebClient webClient) throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://js.189.cn/feeQueryNew/query_callAcctItemConsumqr.parser";
		Page html = getPage(webClient, url, HttpMethod.POST, null, false);

		String json = html.getWebResponse().getContentAsString();

		System.out.println(json);

		JSONObject jsonObj = JSONObject.fromObject(json);

		return webClient;
	}

	/**
	 * 充值缴费
	 */
	private static WebClient chongzhi(WebClient webClient, String dccBillingCycle) throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://js.189.cn/callUgetPayment.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("dccBillingCycle", dccBillingCycle));
		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();
		System.out.println(json);

		JSONObject jsonObj = JSONObject.fromObject(json);
		String errorCode = jsonObj.getString("errorCode");
		if ("0".equals(errorCode)) {
			JSONObject bodys = jsonObj.getJSONObject("bodys");
			int dccControl101 = bodys.getInt("dccControl101");
			if (dccControl101 > 1) {
				JSONArray dccControl101list = bodys.getJSONArray("dccControl101list");
				for (Object object : dccControl101list) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					// 流水号
					String dccPaySerialNbr = jsonObj1.getString("dccPaySerialNbr");
					// 被充值号码
					String dccPaymentNbr = jsonObj1.getString("dccPaymentNbr");
					// 入帐时间
					String dccPaidTime = jsonObj1.getString("dccPaidTime");
					/**
					 * 交费渠道 0-----------营业厅 1-----------网厅 2-----------欢GO客户端
					 * 3-----------翼支付 4-----------第三方支付 5-----------自助缴费
					 * 6-----------银行 7-----------其它
					 */
					String dccChargeSourceId = jsonObj1.getString("dccChargeSourceId");
					/**
					 * 交费方式 11------------现金 12------------支票 14------------代缴
					 * 15------------充值 16------------套餐促销费用 17------------托收
					 * 18------------空中充值 19------------银行卡 20------------充值卡
					 */
					String dccPaymentMethod = jsonObj1.getString("dccPaymentMethod");
					// 入帐金额（元）
					String dccPaymentAmount = jsonObj1.getString("dccPaymentAmount");
					/**
					 * 使用范围 0-----------------通用 1-----------------专用
					 * 2-----------------用户 3-----------------用户帐目组
					 * 4-----------------帐户帐目组
					 */
					String tBalanceTypeId = jsonObj1.getString("tBalanceTypeId");

					TelecomJiangsuPay telecomJiangsuPay = new TelecomJiangsuPay("", dccPaySerialNbr, dccPaymentNbr,
							dccPaidTime, dccChargeSourceId, dccPaymentMethod, dccPaymentAmount, tBalanceTypeId);
					listPay.add(telecomJiangsuPay);
				}
			} else if (dccControl101 == 1) {
				JSONObject dccControl101list = bodys.getJSONObject("dccControl101list");
				// 流水号
				String dccPaySerialNbr = dccControl101list.getString("dccPaySerialNbr");
				// 被充值号码
				String dccPaymentNbr = dccControl101list.getString("dccPaymentNbr");
				// 入帐时间
				String dccPaidTime = dccControl101list.getString("dccPaidTime");
				/**
				 * 交费渠道 0-----------营业厅 1-----------网厅 2-----------欢GO客户端
				 * 3-----------翼支付 4-----------第三方支付 5-----------自助缴费
				 * 6-----------银行 7-----------其它
				 */
				String dccChargeSourceId = dccControl101list.getString("dccChargeSourceId");
				/**
				 * 交费方式 11------------现金 12------------支票 14------------代缴
				 * 15------------充值 16------------套餐促销费用 17------------托收
				 * 18------------空中充值 19------------银行卡 20------------充值卡
				 */
				String dccPaymentMethod = dccControl101list.getString("dccPaymentMethod");
				// 入帐金额（元）
				String dccPaymentAmount = dccControl101list.getString("dccPaymentAmount");
				/**
				 * 使用范围 0-----------------通用 1-----------------专用
				 * 2-----------------用户 3-----------------用户帐目组
				 * 4-----------------帐户帐目组
				 */
				String tBalanceTypeId = dccControl101list.getString("tBalanceTypeId");

				TelecomJiangsuPay telecomJiangsuPay = new TelecomJiangsuPay("", dccPaySerialNbr, dccPaymentNbr,
						dccPaidTime, dccChargeSourceId, dccPaymentMethod, dccPaymentAmount, tBalanceTypeId);
				listPay.add(telecomJiangsuPay);
			}
		}

		return webClient;
	}

	/**
	 * 余额变动明细
	 * 
	 * @param webClient
	 * @param dccBillingCycle
	 * @return
	 * @throws Exception
	 */
	private static WebClient yue(WebClient webClient, String dccBillingCycle) throws Exception {

		// String url2 =
		// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
		// HtmlPage html2 = getHtmlPage(webClient, url2, null);
		// String json2 = html2.getWebResponse().getContentAsString();

		String url = "http://js.189.cn/feeQueryNew/query_callUgetBalChgDetail.parser";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("dccBillingCycle", dccBillingCycle));
		Page html = getPage(webClient, url, HttpMethod.POST, paramsList, false);

		String json = html.getWebResponse().getContentAsString();

		JSONObject jsonObj = JSONObject.fromObject(json);
		System.out.println(jsonObj);

		String errorCode = jsonObj.getString("errorCode");
		if ("0".equals(errorCode)) {
			JSONObject bodys = jsonObj.getJSONObject("bodys");
			int dccControl101 = bodys.getInt("dccControl101");
			if (dccControl101 > 1) {
				JSONArray dccControl101list = bodys.getJSONArray("dccControl101list");
				for (Object object : dccControl101list) {
					JSONObject jsonObj1 = JSONObject.fromObject(object);
					// 时间 状态时间（变动时间）
					String dccStateDate = jsonObj1.getString("dccStateDate");
					// 余额类型 余额类型标识0：通用余额（帐户级，网厅）1：专用余额2：用户级3：用户帐目组级4：帐户帐目组级
					String aocUnit = jsonObj1.getString("aocUnit");
					// 入帐（元） 本期入帐
					String dccPaymentAmount = jsonObj1.getString("dccPaymentAmount");
					// 支出（元） 本期支出
					String dccBalUsedAmount = jsonObj1.getString("dccBalUsedAmount");
					// 变动类型 余额变动类型,其中0-4代表入帐，5-9代表支出
					// (入帐：0：现金充值1：预存返还2：赠费3：退费4：积分兑换，支出：
					// 5：话费支出6：代收费7：余额失效8：其它支出9：转坏帐)
					String dccBalUnitTypeId = jsonObj1.getString("dccBalUnitTypeId");
					// 余额（元） 本期末余额
					String balanceAmount = jsonObj1.getString("balanceAmount");
					// 变动号码 号码信息描述
					String dccCounts = jsonObj1.getString("dccCounts");
					TelecomJiangsuBalance telecomJiangsuBalance = new TelecomJiangsuBalance("", dccStateDate, aocUnit,
							dccPaymentAmount, dccBalUsedAmount, dccBalUnitTypeId, balanceAmount, dccCounts);
					list.add(telecomJiangsuBalance);
				}
			} else if (dccControl101 == 1) {
				JSONObject dccControl101list = bodys.getJSONObject("dccControl101list");
				// 时间 状态时间（变动时间）
				String dccStateDate = dccControl101list.getString("dccStateDate");
				// 余额类型 余额类型标识0：通用余额（帐户级，网厅）1：专用余额2：用户级3：用户帐目组级4：帐户帐目组级
				String aocUnit = dccControl101list.getString("aocUnit");
				// 入帐（元） 本期入帐
				String dccPaymentAmount = dccControl101list.getString("dccPaymentAmount");
				// 支出（元） 本期支出
				String dccBalUsedAmount = dccControl101list.getString("dccBalUsedAmount");
				// 变动类型 余额变动类型,其中0-4代表入帐，5-9代表支出
				// (入帐：0：现金充值1：预存返还2：赠费3：退费4：积分兑换，支出：
				// 5：话费支出6：代收费7：余额失效8：其它支出9：转坏帐)
				String dccBalUnitTypeId = dccControl101list.getString("dccBalUnitTypeId");
				// 余额（元） 本期末余额
				String balanceAmount = dccControl101list.getString("balanceAmount");
				// 变动号码 号码信息描述
				String dccCounts = dccControl101list.getString("dccCounts");

				TelecomJiangsuBalance telecomJiangsuBalance = new TelecomJiangsuBalance("", dccStateDate, aocUnit,
						dccPaymentAmount, dccBalUsedAmount, dccBalUnitTypeId, balanceAmount, dccCounts);
				list.add(telecomJiangsuBalance);
			}
		}

		return webClient;
	}

	/**
	 * 登录
	 * 
	 * @param webClient
	 * @param name
	 * @param password
	 * @return
	 */
	private static WebClient login(WebClient webClient, String name, String password) {

		try {
			String url = "http://login.189.cn/login";
			HtmlPage html = getHtmlPage(webClient, url, null);
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
			username.setText(name);
			passwordInput.setText(password);
			// HtmlImage valiCodeImg =
			// html.getFirstByXPath("//*[@id='imgCaptcha']");
			// try {
			// String imageName = "111.jpg";
			// File file = new File("D:\\img\\" + imageName);
			// try {
			// valiCodeImg.saveAs(file);
			// } catch (Exception e) {
			// System.out.println("图片有误");
			// }
			// // code = chaoJiYingOcrService.getVerifycode(image, "1902");
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// HtmlTextInput valicodeStrinput = (HtmlTextInput)
			// html.getFirstByXPath("//*[@id='txtCaptcha']");
			// String inputuserjymtemp =
			// JOptionPane.showInputDialog("请输入验证码……");
			// valicodeStrinput.reset();
			// valicodeStrinput.setText(inputuserjymtemp);
			HtmlPage htmlpage = button.click();

			if (htmlpage.asXml().indexOf("登录失败") != -1) {
				System.out.println("登录失败");
			} else {
				System.out.println("登录成功");
				String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
				System.out.println("login---cookieString---------------------->" + cookieString);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return webClient;

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

		// webRequest.setAdditionalHeader("Accept", "*/*");

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

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
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
	public static Page getPage(WebClient webClient, String taskid, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {

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
	
	
	/*
	 * @Des 获取时间
	 */
	public static String getDateBefore(String fmt, int yearCount, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, yearCount);
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
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
	
	public static String getchildByKeyword(Document document, String keyword) {
		Elements es = document.select("p:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.child(0);
//			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	
}
