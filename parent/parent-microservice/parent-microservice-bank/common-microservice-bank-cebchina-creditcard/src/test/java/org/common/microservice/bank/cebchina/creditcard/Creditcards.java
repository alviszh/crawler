package org.common.microservice.bank.cebchina.creditcard;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class Creditcards {

	public static void main(String[] args) {
		try {

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "https://xyk.cebbank.com/mall/login";
			HtmlPage page = (HtmlPage)getHtml(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);

			HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='userName']");//身份证
			//yzm_get
			HtmlTextInput tu = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzmcode']");//图片验证码

			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@class='grid-mock']");//图片
			String imageName = "111.jpg";
			File file = new File("F:\\img\\" + imageName);
			image.saveAs(file);

			HtmlTextInput login = (HtmlTextInput)page.getFirstByXPath("//input[@id='verification-code']");//短信框

			cardno.setText("6226580034768784");
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			tu.setText(inputValue);


			String url2 = "https://xyk.cebbank.com/mall/api/usercommon/dynamic?"
					+ "name=6226580034768784"
					+ "&code="+inputValue;

			Page page3 =gethtmlPost(webClient, null, url2);
			String html2 = page3.getWebResponse().getContentAsString();
			//{"status":200,"data":{"success":true,"result":"60"}} 成功
			if(html2.indexOf("status")!=-1){
				System.out.println("发送短信成功");

				String duanxin = JOptionPane.showInputDialog("请输入短信验证码……");
				login.setText(duanxin);
				
				HtmlSubmitInput submit = (HtmlSubmitInput)page.getFirstByXPath("//input[@class='btn login-style-bt ']");
				
				
				Page page2 = submit.click();
				
				String url3 = "https://xyk.cebbank.com/mycard/home/home.htm";
				Page page4 = getHtml(url3, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);

			}else{
				System.out.println("发送失败，发生错误："+html2);
			}
			
			/***
			 * 可用额度 1
			 */
			//https://xyk.cebbank.com/mycard/home/getAvailability.htm?cardno=6226580034768784
			/**
			 * 当月欠款 1
			 */
			//https://xyk.cebbank.com/mycard/home/accinfo.htm?cardno=6226580034768784
			/***
			 * 当月消费记录 1
			 */
			//https://xyk.cebbank.com/mycard/bill/unprintbill-query.htm?ACCT_NBR=6226580034768784&ORG=103  （103为人民币）
			/**
			 * 账单 1
			 */
			//https://xyk.cebbank.com/mycard/bill/havingprintbill-query.htm
			/**
			 * 积分和信用卡数 ？
			 */
			//https://xyk.cebbank.com/mycard/home/home.htm
			/**
			 * 积分新增记录 1
			 */
			//https://xyk.cebbank.com/mycard/exchange/detail_doQuery.htm?begindate=2017-09-30&enddate=2017-11-29&currentpage=2&totalpage=2
			/**
			 * 将过期积分 1
			 */
			//https://xyk.cebbank.com/mycard/point/total.htm
			/**
			 * 信用卡信息 1
			 */
			//https://xyk.cebbank.com/mycard/home/acclist.htm?num=5004706
			/**
			 * 还款详情 1
			 */
			//https://xyk.cebbank.com/mycard/bill/billnearly-query.htm?num=5004706 post
			/**
			 * 信用卡额度 1
			 */
			//https://xyk.cebbank.com/mycard/home/getCreditLimit.htm?num=5004706 post
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
