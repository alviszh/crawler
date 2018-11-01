package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanPay;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.chengdu.InsuranceChengduMedical;
import com.module.htmlunit.WebCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TestService{
	
	public static void main(String[] args) {
		
		
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//			webClient = login("372928198210170225");
//			webClient = login("37292819821017022");
			userdata();
			
//			String par = "+%CC%E1+%BD%BB+";
//			 String output = URLDecoder.decode(par, "GBK");
//			 System.out.println(output);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static WebClient login(String keyword) throws Exception {
		// String url =
		// "http://www.lcsylbx.cn/search/yiliao_Result.asp?keyword=372928198210170225&B1=+%CC%E1+%BD%BB+";

		String url = "http://www.lcsylbx.cn/search/yiliao_Result.asp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
		
		//城镇职工参保人员请输入您医保证上的参保号码，15位的请在后面添加000补足18位。
		paramsList1.add(new NameValuePair("keyword", keyword));
//		paramsList1.add(new NameValuePair("B1", "+%CC%E1+%BD%BB+"));
		paramsList1.add(new NameValuePair("B1", " 提 交 "));

		Map<String, String> map = new HashMap<String, String>();
		map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		map.put("Accept-Encoding", "gzip, deflate");
		map.put("Accept-Language", "zh-CN,zh;q=0.9");
		map.put("Cache-Control", "max-age=0");
		map.put("Connection", "keep-alive");
		map.put("Content-Type", "application/x-www-form-urlencoded");
//		map.put("Cookie", "ASPSESSIONIDSQBCQRRQ=EEHBMBJALCNMHAPALOCBOPNC");
		map.put("Host", "www.lcsylbx.cn");
		map.put("Origin", "http://www.lcsylbx.cn");
		map.put("Referer", "http://www.lcsylbx.cn/search_yiliao.asp");
		map.put("Upgrade-Insecure-Requests", "1");
		map.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

		
		Page page1 = getPage(webClient, null, url, HttpMethod.POST, paramsList1, "GBK", null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("html-------------" + html);
		
		String msg=WebCrawler.getAlertMsg();
		System.out.println("login:msg------------------------------------------"+msg);

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
	
	/**
	 * 个人信息解析
	 */
	public static void userdata() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\info.txt");
		String xmlStr = txt2String(file);
		
		Document doc = Jsoup.parse(xmlStr);
		String personal_number = getNextLabelByKeyword(doc, "个人编号");
		String pay_base = getNextLabelByKeyword(doc, "月缴费基数");
		String month_money = getNextLabelByKeyword(doc, "月划入账户金额");
		String sum_money = getNextLabelByKeyword(doc, "累积划入账户金额");
		String yearmoney_pay = getNextLabelByKeyword(doc, "缴费年月");
		System.out.println("个人编码："+personal_number);
		System.out.println("月缴费基数"+pay_base);
		System.out.println("月划入账户金额"+month_money);
		System.out.println("累积划入账户金额"+sum_money);
		System.out.println("缴费年月"+yearmoney_pay);
		//InsuranceLiaochengMedicalInfo
	}
	
	public static String replaceString(String string){
		if(string.contains("￥")){
			string = string.replaceAll("￥", "");
			return string;
		}
		return string;
	}
    
	public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
        	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8")); 
//            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = br.readLine())!=null){
                result.append(System.lineSeparator()+s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword){
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
    
}
