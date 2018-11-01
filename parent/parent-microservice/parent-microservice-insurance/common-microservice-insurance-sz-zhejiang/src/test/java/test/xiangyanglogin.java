package test;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class xiangyanglogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.xf12333.cn/xywzweb/html/fwdt/xxcx/shbxjfcx/index.shtml";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = page.getElementByName("sfzh");
		HtmlPasswordInput pass = page.getElementByName("scbh");
		HtmlSubmitInput sub = page.getElementByName("cmdok");

		String name = "42062119850121746X";
		String password = "4206995506122";
		username.setText("42062119850121746X");//42062119850121746X
		pass.setText("");//4206995506122
		Page page2 = sub.click();

//		String html = page2.getWebResponse().getContentAsString();
//
//		System.out.println(html);
		int date = new Date().getDate();
		System.out.println(date);
		int mydate = date + 99;
		System.out.println(mydate);
		String url2 = "http://www.xf12333.cn/hbwz/qtpage/fwdt/shbxjfcx_result.jsp?"
				+ "scbh="+password
				+ "&sfzh="+name
				+ "&dt="+mydate;
		Page page3 = getHtml(url2, webClient);
		String html2 = page3.getWebResponse().getContentAsString();

		System.out.println(html2);
		String alertMsg = WebCrawler.getAlertMsg();
		
		if(html2.indexOf("查询结果")!=-1){
			System.out.println("登录成功");
			Document doc = Jsoup.parse(html2);
			String text = doc.select("td:contains(社保证号码)+td").first().text();//社保证号码
			String text2 = doc.select("td:contains(姓名)+td").first().text();//姓名
			String text3 = doc.select("td:contains(身份证号码)+td").first().text();//身份证号码
			String text4 = doc.select("td:contains(性别)+td").first().text();//性别
			String text5 = doc.select("td:contains(企业养老保险)+td").first().text();//企业养老保险
			String text6 = doc.select("td:contains(机关事业养老保险)+td").first().text();//机关事业养老保险
			String text7 = doc.select("td:contains(基本医疗保险)+td").first().text();//基本医疗保险
			String text8 = doc.select("td:contains(基本医疗保险(仅住院))+td").first().text();//基本医疗保险(仅住院)
			String text9 = doc.select("td:contains(工伤保险)+td").first().text();//工伤保险
			String text10 = doc.select("td:contains(生育保险)+td").first().text();//生育保险
			String text11 = doc.select("td:contains(失业保险)+td").first().text();//失业保险
			String text12 = doc.select("td:contains(公务员医疗补助保险)+td").first().text();//公务员医疗补助保险
			String text13 = doc.select("td:contains(大额救助保险)+td").first().text();//大额救助保险
			String text14 = doc.select("td:contains(离休干部医疗保险)+td").first().text();//离休干部医疗保险
			String text15 = doc.select("td:contains(低保对象医疗保险)+td").first().text();//低保对象医疗保险
			String text16 = doc.select("td:contains(农民工综合保险)+td").first().text();//农民工综合保险
			String text17 = doc.select("td:contains(单位名称)+td").first().text();//单位名称
			System.out.println(text17+text+text2+text3+text4+text5+text6+text7+text8+text9+text10+text11+text12+text13+text14+text15+text16);
			
			//yanglao
//			Element element = doc.getElementById("zxbs_2008_ta6").getElementsByTag("table").get(2);
//			Elements elementsByTag = element.getElementsByTag("tr");
//			for (int i = 3; i < elementsByTag.size()-1; i++) {
//				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
//				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
//				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
//				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
//				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
//				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
//				System.out.println(string+string2+string3+string4+string5+string6);
//			}
			//yiliao
//			Element element = doc.getElementById("zxbs_2008_ta5").getElementsByTag("table").get(4);
//			Elements elementsByTag = element.getElementsByTag("tr");
//			for (int i = 2; i < elementsByTag.size()-1; i++) {
//				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
//				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
//				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
//				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
//				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
//				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
//				System.out.println(string+string2+string3+string4+string5+string6);
//			}
			
			
			//shiye
//			Element element = doc.getElementById("zxbs_2008_ta7").getElementsByTag("table").get(2);
//			Elements elementsByTag = element.getElementsByTag("tr");
//			for (int i = 3; i < elementsByTag.size(); i++) {
//				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
//				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
//				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
//				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
//				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
//				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
//				System.out.println(string+string2+string3+string4+string5+string6);
//			}
			//gongshang
			Element element = doc.getElementById("zxbs_2008_ta9").getElementsByTag("table").get(2);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 3; i < elementsByTag.size(); i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
			}
			
		}else{
			System.out.println("登录失败："+alertMsg);
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
