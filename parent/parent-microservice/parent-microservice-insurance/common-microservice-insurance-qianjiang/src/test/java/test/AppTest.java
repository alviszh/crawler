package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AppTest {
	public static void main(String[] args) {
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url1 = "http://219.138.153.190:806/personageDemand/YanZhengGeRenYongHu?sfz=220183199206066629&sbka=488428";
			WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
			HtmlPage html1 = webClient.getPage(webRequest1);
			String contentAsString = html1.getWebResponse().getContentAsString();
			System.out.println("登陆结果-----" + contentAsString);
			if (contentAsString.contains("身份验证成功")) {
				System.out.println("登陆成功！");
				// 中间过滤的请求
				String url2 = "http://219.138.153.190:806/personageDemand/Index";
				WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
				webClient.getPage(webRequest2);
				// 基本信息的请求
				String url3 = "http://219.138.153.190:806/personageDemand/grcbjfzmyb";
				WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
				HtmlPage html3 = webClient.getPage(webRequest3);
				String contentAsString3 = html3.getWebResponse().getContentAsString();
				
				Document doc = Jsoup.parse(contentAsString3);
				Element table = doc.getElementsByTag("table").get(0);
				Elements trs = table.getElementsByTag("tr");
				Elements tds0 = trs.get(0).getElementsByTag("td");
				// 姓名
				String xm = tds0.get(1).text();
				System.out.println("姓名：" + xm);
				// 身份证号码
				String sfzhm = tds0.get(3).text();
				System.out.println("身份证号码：" + sfzhm);
				Elements tds1 = trs.get(1).getElementsByTag("td");
				// 参保状态
				String cbzt = tds1.get(1).text();
				System.out.println("参保状态：" + cbzt);
				// 社保卡号
				String sbkh = tds1.get(3).text();
				System.out.println("社保卡号：" + sbkh);
				
				// 五险信息的请求
				String url4 = "http://219.138.153.190:806/personageDemand/PayCostRecordQuery?page=1&count=500&lx=0";
				WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
				HtmlPage html4= webClient.getPage(webRequest4);
				String contentAsString4 = html4.getWebResponse().getContentAsString();
				System.out.println(contentAsString4);
				Document doc2 = Jsoup.parse(contentAsString4);
				String DataJson = doc2.getElementById("DataJson").val();
				JSONObject object = JSONObject.fromObject(DataJson);
				String rows = object.getString("rows").trim();
				JSONArray array = JSONArray.fromObject(rows);
				for (int i = 0; i < array.size(); i++) {
					String string = array.get(i).toString();
					JSONObject object2 = JSONObject.fromObject(string);
					//单位名称
					String dwmc = object2.getString("DWMC").trim();
					System.out.println("单位名称"+dwmc);
					//险种类型
					String xzlx = object2.getString("XZLX").trim();
					System.out.println("险种类型"+xzlx);
					//缴费状态
					String jfzt = object2.getString("YJLX").trim();
					System.out.println("缴费状态"+jfzt);
					//缴费年月
					String jfny = object2.getString("JFNY").trim();
					System.out.println("缴费年月"+jfny);
					//缴费基数
					String jfjs = object2.getString("JFJS").trim();
					System.out.println("缴费基数"+jfjs);
					// 缴费金额
					String jfje = object2.getString("GRJFJE").trim();
					System.out.println("缴费金额"+jfje);
					System.out.println("======================");
				}
				
			} else {
				System.out.println("登陆失败！");
			}
		} catch (Exception e) {
		}
		File file = new File("C:\\Users\\Administrator\\Desktop\\qqq.txt");
		String json = txt2String(file);
		
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

}
