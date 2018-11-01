package test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class zhaoqinglogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url ="http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance.action?"
				+ "areacode=441200"
				+ "&zhencode="
				+ "&cuncode=";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("sbusername");//姓名
		HtmlTextInput idcard = (HtmlTextInput) page.getElementById("sbidcard");//身份证号
		HtmlButtonInput button = page.getFirstByXPath("//input[@type = 'button']");//查询
		String name = "梁彬";
		String idCard = "441202198911282014";
		username.setText(name);//梁彬
		idcard.setText(idCard);//441202198911282014
		Thread.sleep(2000);
		Page page2 = button.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		if(html.indexOf("个人信息 ")!=-1){
			//个人信息
			System.out.println("登录成功");
			Document doc = Jsoup.parse(html);
			Elements elementsByTag = doc.getElementsByTag("td");
			String text = elementsByTag.get(1).text().trim();
			String text2 = elementsByTag.get(3).text().trim();
			String text3 = elementsByTag.get(5).text().trim();
			String text4 = elementsByTag.get(7).text().trim();
			String text5 = elementsByTag.get(9).text().trim();
			String text6 = elementsByTag.get(11).text().trim();
			System.out.println(text+text2+text3+text4+text5+text6);

			//养老
			String payurl = "http://wsbs.zhaoqing.gov.cn/portal/jcfw2/insurance/insurance!findInsurance.action";
			List<NameValuePair> paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("type", "4"));
			paramsList.add(new NameValuePair("userid", "11122615"));
			paramsList.add(new NameValuePair("sbusername", name));
			paramsList.add(new NameValuePair("sbidcard", idCard));
			paramsList.add(new NameValuePair("areacode", "441200"));
			paramsList.add(new NameValuePair("years", "2017"));
			paramsList.add(new NameValuePair("zhencode", ""));
			paramsList.add(new NameValuePair("cuncode", ""));
			Page page3 = gethtmlPost(webClient, paramsList, payurl);

			String htmlpay = page3.getWebResponse().getContentAsString();
			System.out.println(htmlpay);
			String title = Jsoup.parse(htmlpay).getElementsByTag("title").text().trim();
			for (int i = 0; i < 4; i++) {
				if(title.indexOf("查询异常")!=-1) {
					page3 = gethtmlPost(webClient, paramsList, payurl);
					htmlpay = page3.getWebResponse().getContentAsString();
					System.out.println(htmlpay);
					title = Jsoup.parse(htmlpay).getElementsByTag("title").text().trim();
					if(title.equals("养老保险缴费明细")){
						break;
					}
				}
			}
			if(title.indexOf("养老保险缴费明细")!=-1){
				Document parse = Jsoup.parse(htmlpay);
				Elements elementsByClass = parse.getElementsByClass("line_td");
				for(int i=0;i<elementsByClass.size();i++){
					String string = elementsByClass.get(i).getElementsByTag("td").get(0).text().trim();//费款所属期
					String string1 = elementsByClass.get(i).getElementsByTag("td").get(1).text().trim();//单位名称
					String string2 = elementsByClass.get(i).getElementsByTag("td").get(2).text().trim();//缴费基数
					String string3 = elementsByClass.get(i).getElementsByTag("td").get(3).text().trim();//单位缴费金额
					String string4 = elementsByClass.get(i).getElementsByTag("td").get(4).text().trim();//个人缴费金额
					String string5 = elementsByClass.get(i).getElementsByTag("td").get(5).text().trim();//合计

					System.out.println("\r费款所属期:"+string+"\r单位名称:"+string1+"\r缴费基数:"+string2+"\r单位缴费金额:"+
							string3+"\r个人缴费金额:"+string4+"\r合计:"+string5);
				}
			}else{
				System.out.println(title+",网络异常,请稍后重新登录...");
			}
			
			//医疗
			

		}else{
			System.out.println("登录失败");
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
