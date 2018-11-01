package test.insurance.luohe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanGeneral;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheUserInfo;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestService {

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",    "org.apache.commons.logging.impl.NoOpLog");  
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")  
            .setLevel(Level.OFF);  
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient")  
            .setLevel(Level.OFF); 
       
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String username = "18639511181";
			String password = "zhao1986";
			InsuranceRequestParameters insuranceRequestParameters = new InsuranceRequestParameters();
			insuranceRequestParameters.setCity("漯河市");
			insuranceRequestParameters.setUsername(username);
			insuranceRequestParameters.setPassword(password);
			insuranceRequestParameters.setTaskId("luohe111");
//			webClient = login(insuranceRequestParameters);
			
//			webClient = getInfo(webClient,insuranceRequestParameters);
//			getloginInfo(webClient); 
			
			
			File file = new File("C:\\Users\\Administrator\\Desktop\\aaa.txt");
			String xmlStr = txt2String(file);
			
			JSONObject fromObject = JSONObject.fromObject(getuserinfo("", xmlStr));
			System.out.println(fromObject.toString());
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static InsuranceLuoheUserInfo getuserinfo(String taskId, String html) {
		try {
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jbxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			JSONObject obj = object.getJSONObject(0);
			String gr_num = obj.getString("F002");// 个人编号
			String name = obj.getString("F004");// 职工姓名
			String sex = obj.getString("F005ZH");// 性别
			String nation = obj.getString("F006ZH");// 民族
			String idcard = obj.getString("F003");// 身份证号
			String birthday = obj.getString("F007");// 出生日期
			String insuredtime = obj.getString("F008");// 参加工作时间
			String state = obj.getString("F009ZH");// 人员状态
			String dw_num = obj.getString("F001");// 单位编号
			String dw_name = obj.getString("F001ZH");// 单位名称

			InsuranceLuoheUserInfo insuranceLuoheUserInfo = new InsuranceLuoheUserInfo(taskId, gr_num, name, sex,
					nation, idcard, birthday, insuredtime, state, dw_num, dw_name);
			return insuranceLuoheUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static WebClient login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl = "http://www.halh.lss.gov.cn:9000/login.html";
		
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000);
		int status = searchPage.getWebResponse().getStatusCode();
		if (200 == status) {
			HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='username']");
			if (inputUserName == null) {
				throw new Exception("username input text can not found :" + "input[id='username']");
			} else {
				inputUserName.reset();
				inputUserName.setText(insuranceRequestParameters.getUsername());
			}
			HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector("input[id='password']");
			if (inputPassword == null) {
				throw new Exception("password input text can not found :" + "input[id='password']");
			} else {
				inputPassword.reset();
				inputPassword.setText(insuranceRequestParameters.getPassword());
			}
			HtmlButtonInput loginButton = searchPage.getFirstByXPath("//input[@id='btn_login_0']");
			if (loginButton == null) {
				throw new Exception("login button can not found : null");
			} else {
				searchPage = loginButton.click();
				webClient.waitForBackgroundJavaScript(10000);
				HtmlListItem li = (HtmlListItem) searchPage.querySelector("li[id='li_tip_id_0']");
				String xmlStr = searchPage.asXml();
				System.out.println(xmlStr);
				System.out.println("------------------------------------------");
				System.out.println(li.asText());
				
			}
			return webClient;
		}
		return null;
	}
	
	
	
	public static WebClient getInfo(WebClient webClient,InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		String url="http://www.halh.lss.gov.cn:9000/loginAction.action?from=&redirect="
				+ "&username="+insuranceRequestParameters.getUsername()
				+ "&password="+insuranceRequestParameters.getPassword()
				+ "&phoneNumber=&smsVerificationCode=&loginMode=0";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page searchPage = webClient.getPage(webRequest);
		String string = searchPage.getWebResponse().getContentAsString();
		String msg = JSONObject.fromObject(string).getString("message");
		System.out.println(msg);
		return webClient;
	}
	
	
	public static WebClient getloginInfo(WebClient webClient) throws Exception {
		String url="http://www.halh.lss.gov.cn:9000/grpt/zgbx/zgbxJbxxcxAction001.action";
		Page page = getPage(webClient, null, url, HttpMethod.POST, null, null, null, null);
		String string = page.getWebResponse().getContentAsString();
		System.out.println(string);
		return webClient;

	}
	
	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			// BufferedReader br = new BufferedReader(new FileReader(file));
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
		// tracerLog.output("CmbChinaService.getPage---url:", url + "---taskId:"
		// + taskid);
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
		// tracerLog.output("CmbChinaService.getPage.statusCode:" + statusCode,
		// url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}
	
	

}
