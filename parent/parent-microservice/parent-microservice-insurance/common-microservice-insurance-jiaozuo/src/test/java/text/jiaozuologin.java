package text;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class jiaozuologin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String url = "http://www.hajz12333.gov.cn:8080/web/login.html";
		
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("idCardNumber");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		
		HtmlAnchor hand = (HtmlAnchor) page.getByXPath("//a[@class='hand']").get(0);
		username.setText("");//411326198806065166
		password.setText("");//yy721521
		HtmlPage page2 = hand.click();
		String alertMsg = WebCrawler.getAlertMsg();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		if(html.indexOf("退出")!=-1){
			System.out.println("登录成功");
			Document doc = Jsoup.parse(html);
			String name = doc.getElementById("userName").val();//姓名
			String val = doc.getElementById("userIdCardNumber").val();//身份证号码
			String userMobile = doc.getElementById("userMobile").val();//手机号码
			String encodeName=URLEncoder.encode(name, "utf-8");
			
			
			//个人信息
			/*String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonInsuredInfo?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%E5%85%BB%E8%80%81";//养老
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getObjData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			JSONObject object = JSONObject.fromObject(html2).getJSONObject("DetailData");
			
			String string = object.getString("PersonID");//个人编号
			String string2 = object.getString("Name");//姓名
			String string3 = object.getString("IdCard");//身份证
			String string4 = object.getString("Gender");//性别
			String string5 = object.getString("Nation");//民族
			String string6 = object.getString("Birthday");//出生日期
			String string7 = object.getString("PersonStatus");//人员状态
			String string8 = object.getString("UnitId");//单位编号
			String string9 = object.getString("UnitName");//单位名称
			String string10 = object.getString("FirstInsuredDate");//参保日期
			String string11 = object.getString("CreateAccountDate");//参保年月
			String string12 = object.getString("InsuredStatus");//参保状态
			String string13 = object.getString("ShouldPayMonths");//应缴月数
			String string14 = object.getString("RealPayMonths");//实缴月数
			String string15 = object.getString("Address");//地址
			String string16 = object.getString("Telephone");//联系方式
			
			System.out.println("个人编号:"+string+"\r姓名："+string2+"\r身份证："+string3+"\r性别："+string4+"\r民族："
			+string5+"\r出生日期："+string6+"\r人员状态："+string7+"\r单位编号："+string8+"\r单位名称："+string9+"\r参保日期："+string10+"\r参保年月："+string11
			+"\r参保状态："+string12+"\r应缴月数："+string13+"\r实缴月数："+string14+"\r地址："+string15+"\r联系方式："+string16+"\r");*/
			
			
			//养老
			/*String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%E5%85%BB%E8%80%81";//养老
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
			}*/
			
			//医疗
			/*String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%E5%8C%BB%E7%96%97";//医疗
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
			}*/
			
			//工伤
			String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%e5%b7%a5%e4%bc%a4";//工伤
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
			}
			
			//生育
			/*String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%e7%94%9f%e8%82%b2";//生育
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
			}*/
			
			//失业
			/*String tep ="http://www.hajz12333.gov.cn:9001/Insurance/CZ_GetPersonPayRecords?"
					+ "cardNumber="+val
					+ "&name="+encodeName
					+ "&insuranceCategory=%e5%a4%b1%e4%b8%9a";//生育
			String encodeName1=URLEncoder.encode(tep, "utf-8");
			String url2 = "http://www.hajz12333.gov.cn:8080/getListData.client?"
						+"url="+encodeName1;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			System.out.println(html2);
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
			}*/
		}else{
			System.out.println("登录失败");
			
			if(alertMsg!=""){
				System.out.println(alertMsg);
			}else{
				Document document = Jsoup.parse(html);
				String error = document.getElementsByTag("p").get(2).text();
				System.out.println(error);
			}
		}
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
