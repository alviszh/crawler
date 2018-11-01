package test;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class text {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://wssb.jlsi.gov.cn:8443/login.jsp";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		HtmlSubmitInput d = (HtmlSubmitInput) page.getFirstByXPath("//input[@type='submit']");
		
		username.setText("220122198901270026");//220122198901270026
		password.setText("momo3322");//momo3322
		Page page2 = d.click();
		
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		if(html.indexOf("菜单导航")!=-1){
			System.out.println("登录成功");
	/***
	 * 个人信息完成（OK！）		
	 */
			/*
			String url2 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxCbxxcx.do";
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			
			Document doc = Jsoup.parse(html2);
			Element element = doc.getElementsByTag("table").get(0);
			
			String text = element.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text();//姓名
			String text2 = element.getElementsByTag("tr").get(0).getElementsByTag("td").get(3).text();//性别
			String text3 = element.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();//证件号码
			String text4 = element.getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text();//出生日期
			String text5 = element.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();//个人身份
			String text6 = element.getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text();//用工形式
			String text7 = element.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();//人员状态
			String text8 = element.getElementsByTag("tr").get(3).getElementsByTag("td").get(3).text();//农民工标识
			
			Element element2 = doc.getElementsByTag("table").get(1);
			String text9 = element2.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text();//单位编号
			String text10 = element2.getElementsByTag("tr").get(0).getElementsByTag("td").get(3).text();//单位名称
			String text11 = element2.getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text();//养老建账时间
			String text12 = element2.getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text();//参工时间
			String text13 = element2.getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text();//养老参保时间
			String text14 = element2.getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text();//失业参保时间
			String text15 = element2.getElementsByTag("tr").get(3).getElementsByTag("td").get(1).text();//养老参保状态
			String text16 = element2.getElementsByTag("tr").get(3).getElementsByTag("td").get(3).text();//失业参保状态
			String text17 = element2.getElementsByTag("tr").get(4).getElementsByTag("td").get(1).text();//养老缴费状态
			String text18 = element2.getElementsByTag("tr").get(4).getElementsByTag("td").get(3).text();//失业缴费状态
			
			*/  
	
			/**
			 * //个人缴费基数完成（OK！）
			 */
			/*String url3 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxJfjscx.do";
			Page page4 = getHtml(url3, webClient);
			String html3 = page4.getWebResponse().getContentAsString();
			
			Document document = Jsoup.parse(html3);
			Element elementById = document.getElementById("contentTable");
			Elements byTag = elementById.getElementsByTag("tr");
			for(int i=1;i<byTag.size();i++){
				String text = byTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text2 = byTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text3 = byTag.get(i).getElementsByTag("td").get(2).text();//开始期号
				String text4 = byTag.get(i).getElementsByTag("td").get(3).text();//结束期号
				String text5 = byTag.get(i).getElementsByTag("td").get(4).text();//申报工资
				String text6 = byTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
			}*/ 
			
			/***
			 * 个人缴费断档信息(OK)
			 */
			/*String url4 = "https://wssb.jlsi.gov.cn:8443/grzx/grddjf.do";
			Page page5 = getHtml(url4, webClient);
			String html5 = page5.getWebResponse().getContentAsString();
			
			Document parse = Jsoup.parse(html5);
			Elements elementsByTag = parse.getElementsByClass("table").get(0).getElementsByTag("tr");
			for(int i=1;i<elementsByTag.size();i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//年度
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//期号
			}*/
			
			/**
			 * 养老保险缴纳明细(OK)
			 */
			/*String url5 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxJfmxcx.do";
			Page page3 = getHtml(url5, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			
			Document document = Jsoup.parse(html2);
			Elements elementsByTag = document.getElementById("contentTable").getElementsByTag("tr");
			for(int i=1;i<elementsByTag.size()-2;i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text1 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//费款所属期
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//险种类型
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//缴费类型
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//个人缴费金额
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//个人缴费标志
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(8).text();//个人缴费到账日期
				String text9 = elementsByTag.get(i).getElementsByTag("td").get(9).text();//单位缴费划账户金额
				String text10 = elementsByTag.get(i).getElementsByTag("td").get(10).text();//单位缴费划统筹金额
				String text11 = elementsByTag.get(i).getElementsByTag("td").get(11).text();//单位缴费标志
				String text12 = elementsByTag.get(i).getElementsByTag("td").get(12).text();//单位缴费到账日期
				String text13 = elementsByTag.get(i).getElementsByTag("td").get(13).text();//缴费月数
				String text14 = elementsByTag.get(i).getElementsByTag("td").get(14).text();//缴费单位编号
				String text15 = elementsByTag.get(i).getElementsByTag("td").get(15).text();//缴费单位名称
			}*/
			/**
			 * 个人失业缴费明细(OK)
			 */
			/*String url2 = "https://wssb.jlsi.gov.cn:8443/grzx/grzxSybxjfmxcx.do";
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			
			Document document = Jsoup.parse(html2);
			Elements elementsByTag = document.getElementById("contentTable").getElementsByTag("tr");
			for(int i=1;i<elementsByTag.size()-2;i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//姓名
				String text1 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号码
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//费款所属期
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//险种类型
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//缴费类型
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//缴费基数
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//个人缴费金额
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//个人缴费标志
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(8).text();//个人缴费到账日期
				String text9 = elementsByTag.get(i).getElementsByTag("td").get(9).text();//单位缴费划账户金额
				String text10 = elementsByTag.get(i).getElementsByTag("td").get(10).text();//单位缴费划统筹金额
				String text11 = elementsByTag.get(i).getElementsByTag("td").get(11).text();//单位缴费标志
				String text12 = elementsByTag.get(i).getElementsByTag("td").get(12).text();//单位缴费到账日期
				String text13 = elementsByTag.get(i).getElementsByTag("td").get(13).text();//缴费月数
				String text14 = elementsByTag.get(i).getElementsByTag("td").get(14).text();//缴费单位编号
				String text15 = elementsByTag.get(i).getElementsByTag("td").get(15).text();//缴费单位名称
				System.out.println(i);
			}*/
			/**
			 * 缴费状态变更记录(OK)
			 */
			/*String url2 = "https://wssb.jlsi.gov.cn:8443/dwcx/txbbgjlcxAction!querylist.do";
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html2);
			Elements elementsByTag = doc.getElementById("contentTable").getElementsByTag("tr");
			for(int i=1;i<elementsByTag.size();i++){
				String text = elementsByTag.get(i).getElementsByTag("td").get(0).text();//个人编号
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//身份证号
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//姓名
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//单位编号
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//单位名称
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(5).text();//变更类型
				String text7 = elementsByTag.get(i).getElementsByTag("td").get(6).text();//变更时间
				String text8 = elementsByTag.get(i).getElementsByTag("td").get(7).text();//变更原因
				System.out.println(i);
			}*/
			
		}else{
			Document doc = Jsoup.parse(html);
			String text = doc.getElementById("loginError").text();
			System.out.println("登录失败:"+text);
		}
	}
	
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
