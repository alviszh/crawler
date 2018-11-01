package TestWap;

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
