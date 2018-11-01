package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.jiyuan.HousingJiyuanUserInfo;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBillSum;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class TestService {

	public static void main(String[] args) {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//			 webClient = login(webClient);
//			getuserInfo();
			getjson();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getuserInfo() {

		File file = new File("C:\\Users\\Administrator\\Desktop\\zmd.txt");
		String msg = txt2String(file);
//		String msg = "{\"MSG\":\"未查询到信息，请检查查询条件！\",\"MS_TYPE\":\"1\"}";
		// String json =
		// "{\"ctn\":[{\"YJCE\":458.4,\"GZE\":2292,\"GRZT\":\"正常\",\"GRXM\":\"赵真真\",\"JZYF\":\"2017/12\",\"KHRQ\":\"2015/01/22\",\"JCYE\":16503.14}],\"MS_TYPE\":\"2\"}";
		JSONObject jsonObj = JSONObject.fromObject(msg);
		if(msg.contains("未查询到信息")){
			String m = jsonObj.getString("MSG");
			System.out.println(m);
		}else{
			String ctn = jsonObj.getString("ctn");
			JSONArray jsonArray = JSONArray.fromObject(ctn);
			JSONObject obj = jsonArray.getJSONObject(0);

			String name = obj.getString("GRXM");// 姓名
			String base = obj.getString("GZE");// 工资基数
			String monthly_pay = obj.getString("YJCE");// 月缴额
			String balance = obj.getString("JCYE");// 缴存余额
			String open_date = obj.getString("KHRQ");// 开户日期
			String pay_year = obj.getString("JZYF");// 缴至年月
			String state = obj.getString("GRZT");// 账户状态

			System.out.println("姓名-----" + name);
			System.out.println("工资基数-----" + base);
			System.out.println("月缴额-----" + monthly_pay);
			System.out.println("缴存余额-----" + balance);
			System.out.println("开户日期-----" + open_date);
			System.out.println("缴至年月-----" + pay_year);
			System.out.println("账户状态-----" + state);
		}
		
		

	}

	public static WebClient login(WebClient webClient) throws Exception {

		String url = "http://61.163.209.58:8089/lhgjj_ws_client/GjjcxAction.do";

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

//		paramsList.add(new NameValuePair("GRXM", "赵真真"));
		paramsList.add(new NameValuePair("GRXM", ""));
		paramsList.add(new NameValuePair("GRBH", ""));
		paramsList.add(new NameValuePair("SFZH", "41112319860518154X"));
//		paramsList.add(new NameValuePair("SFZH", ""));

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put("Cache-Control", "max-age=0");
		// map.put("Connection", "keep-alive");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Host", "search.jygjj.com");
		// map.put("Origin", "http://www.jygjj.com");
		// map.put("Referer", "http://www.jygjj.com/");
		// map.put("Upgrade-Insecure-Requests", "1");
		// map.put("User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like
		// Gecko) Chrome/63.0.3239.84 Safari/537.36");

		Page page1 = getPage(webClient, null, url, HttpMethod.POST, paramsList, "UTF-8", null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("html-------------" + html);

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
	
	public static void getjson() {

		File file = new File("C:\\Users\\Administrator\\Desktop\\zzz.txt");
		String xmlStr = txt2String(file);
		JSONObject jsonObj = JSONObject.fromObject(xmlStr);
		String payLog = jsonObj.getString("resultMessage");
		Object obj = new JSONTokener(payLog).nextValue();
		if (obj instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) obj;
			String STRATEGY_INST_ID = jsonObject.getString("STRATEGY_INST_ID");
			String PRICING_VALUE = jsonObject.getString("PRICING_VALUE");

			System.out.println("STRATEGY_INST_ID-----" + STRATEGY_INST_ID);
			System.out.println("PRICING_VALUE-----" + PRICING_VALUE);
		} else if (obj instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				String STRATEGY_INST_ID = jsonObject.getString("STRATEGY_INST_ID");
				String PRICING_VALUE = jsonObject.getString("PRICING_VALUE");

				System.out.println("STRATEGY_INST_ID-----" + STRATEGY_INST_ID);
				System.out.println("PRICING_VALUE-----" + PRICING_VALUE);
			}
		}
		
	}

}
