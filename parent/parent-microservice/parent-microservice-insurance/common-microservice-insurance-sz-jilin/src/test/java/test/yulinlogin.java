package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
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
import com.module.htmlunit.WebCrawler;

public class yulinlogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://222.83.253.69/ylsbwz/login.jsp";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput sfzh = (HtmlTextInput) page.getElementById("sfzh");//帐号
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");//密码
		HtmlTextInput yzm = (HtmlTextInput) page.getElementById("yzm");//验证码输入框
		HtmlImage image = (HtmlImage) page.getElementById("Verify");//图片验证码

		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");

		HtmlAnchor btnLogin = (HtmlAnchor) page.getElementById("btnLogin");//登录

		sfzh.setText("452501198812286100");//452501198812286100
		password.setText("qwer1234");//qwer1234
		yzm.setText(inputValue);
		btnLogin.click();
		String url2 = "http://222.83.253.69/ylsbwz/login?"
				+ "sfzh=452501198812286100"
				+ "&password=qwer1234"
				+ "&yzm="+inputValue;
		Page page3 = getHtml(url2, webClient);

		String contentAsString = page3.getWebResponse().getContentAsString();
		System.out.println(contentAsString);
		String error = WebCrawler.getAlertMsg();
		if (contentAsString.indexOf("个人查询")!=-1) {
			System.out.println("登录成功");

			/**
			 * 个人基本信息
			 */
			/*String url3 = "http://222.83.253.69/ylsbwz/grxxCx";
			Page page2 = getHtml(url3, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			Document document = Jsoup.parse(contentAsString2);
			String val = document.select("input[readonly='readonly']").get(0).val();//姓名
			String val2 = document.select("input[readonly='readonly']").get(1).val();//性别
			String val3 = document.select("input[readonly='readonly']").get(2).val();//民族
			String val4 = document.select("input[readonly='readonly']").get(3).val();//身份证类型
			String val5 = document.select("input[readonly='readonly']").get(4).val();//公民身份号码
			String val6 = document.select("input[readonly='readonly']").get(5).val();//个人编号
			String val7 = document.select("input[readonly='readonly']").get(6).val();//户口性质
			String val8 = document.select("input[readonly='readonly']").get(7).val();//户口所在地行政区域
			String val9 = document.select("input[readonly='readonly']").get(8).val();//户口所在地详细地址
			String val10 = document.select("input[readonly='readonly']").get(9).val();//常住地行政区域
			String val11 = document.select("input[readonly='readonly']").get(10).val();//邮政编码
			String val12 = document.select("input[readonly='readonly']").get(11).val();//常住地详细地址
			String val13 = document.select("input[readonly='readonly']").get(12).val();//移动电话
			String val14 = document.select("input[readonly='readonly']").get(13).val();//办公电话
			String val15 = document.select("input[readonly='readonly']").get(14).val();//传真
			String val16 = document.select("input[readonly='readonly']").get(15).val();//出生日期 
			String val17 = document.select("input[readonly='readonly']").get(16).val();//参加工作日期
			String val18 = document.select("input[readonly='readonly']").get(17).val();//人员资料登记状态
			String val19 = document.select("input[readonly='readonly']").get(18).val();//离退休状态
			String val20 = document.select("input[readonly='readonly']").get(19).val();//离退休日期
			String val21 = document.select("input[readonly='readonly']").get(20).val();//医疗人员类别
			String val22 = document.select("input[readonly='readonly']").get(21).val();//享受医疗退休待遇日期
			String val23 = document.select("input[readonly='readonly']").get(22).val();//社保卡号
			System.out.println(1);*/


			/**
			 * 企业养老
			 */
			/*String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
					+ "xzlxdmxz=11";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			 *//**
			 * 获取页数
			 *//*
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);

			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
						+ "xzlxdmxz=11"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				Document parse = Jsoup.parse(html);
				Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
				for (int k = 1; k < elementsBy.size(); k++) {
					String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
					String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
					String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
					String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
					String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
					String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
					String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
					String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
					String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
					String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
					System.out.println("-----------------------------第"+j+"页数据----------------------------");
					System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
							+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
							+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				}

			}*/


			/*	*//***
			 * 医疗
			 *//*
		String url3 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=31";
		Page page2 = getHtml(url3, webClient);
		String contentAsString2 = page2.getWebResponse().getContentAsString();
		Document document = Jsoup.parse(contentAsString2);
		String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
		int i = Integer.parseInt(text);

		for (int j = 1; j < i + 1; j++) {
			String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
					+ "xzlxdmxz=31"
					+ "&page="+j;
			Page page4 = gethtmlPost(webClient, null, url5);
			String html = page4.getWebResponse().getContentAsString();
			Document parse = Jsoup.parse(html);
			Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
			for (int k = 1; k < elementsBy.size(); k++) {
				String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
				String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
				String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
				String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
				String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
				String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
				String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
				String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
				String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
				String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
				System.out.println("-----------------------------第"+j+"页数据----------------------------");
				System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
						+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
						+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
			}

		}*/
			/***
			 * 失业
			 */

			/*String url3 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=31";
			Page page2 = getHtml(url3, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);

			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
						+ "xzlxdmxz=21"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				Document parse = Jsoup.parse(html);
				Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
				for (int k = 1; k < elementsBy.size(); k++) {
					String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
					String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
					String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
					String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
					String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
					String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
					String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
					String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
					String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
					String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
					System.out.println("-----------------------------第"+j+"页数据----------------------------");
					System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
							+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
							+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				}

			}*/
			
			/**
			 * 工伤
			 */
		/*	String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=41";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);

			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
						+ "xzlxdmxz=41"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				Document parse = Jsoup.parse(html);
				Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
				for (int k = 1; k < elementsBy.size(); k++) {
					String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
					String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
					String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
					String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
					String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
					String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
					String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
					String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
					String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
					String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
					System.out.println("-----------------------------第"+j+"页数据----------------------------");
					System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
							+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
							+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				}

			}*/
			/**
			 * 生育
			 */
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=51";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);

			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
						+ "xzlxdmxz=51"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				Document parse = Jsoup.parse(html);
				Elements elementsBy = parse.getElementById("tb_1").getElementsByTag("tr");
				for (int k = 1; k < elementsBy.size(); k++) {
					String string = elementsBy.get(k).getElementsByTag("td").get(0).text();//单位名称
					String string2 = elementsBy.get(k).getElementsByTag("td").get(1).text();//结算期
					String string3 = elementsBy.get(k).getElementsByTag("td").get(2).text();//费款所属期
					String string4 = elementsBy.get(k).getElementsByTag("td").get(3).text();//险种类型
					String string5 = elementsBy.get(k).getElementsByTag("td").get(4).text();//缴费类型
					String string6 = elementsBy.get(k).getElementsByTag("td").get(5).text();//缴费基数
					String string7 = elementsBy.get(k).getElementsByTag("td").get(6).text();//应收总额
					String string8 = elementsBy.get(k).getElementsByTag("td").get(7).text();//单位应收金额
					String string9 = elementsBy.get(k).getElementsByTag("td").get(8).text();//个人应收金额
					String string10 = elementsBy.get(k).getElementsByTag("td").get(9).text();//缴费状态
					System.out.println("-----------------------------第"+j+"页数据----------------------------");
					System.out.println("单位名称："+string+"\r结算期:"+string2+"\r费款所属期:"+string3
							+"\r险种类型:"+string4+"\r缴费类型:"+string5+"\r缴费基数:"+string6+"\r应收总额"+string7
							+"\r单位应收金额:"+string8+"\r个人应收金额:"+string9+"\r缴费状态:"+string10);
				}

			}
		}else{
			System.out.println("登录失败:"+error);
		}
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//		webClient.setJavaScriptTimeout(50000); 
		//		webClient.getOptions().setTimeout(50000); // 15->60 
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
