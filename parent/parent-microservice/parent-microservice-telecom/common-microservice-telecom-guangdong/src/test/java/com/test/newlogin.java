package com.test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class newlogin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://gd.189.cn/common/newLogin/newLogin/login.htm?"
				+ "v=3"
				+ "&SSOArea=0769"
				+ "&SSOAccount="
				+ "&SSOProType="
				+ "&SSORetryTimes="
				+ "&SSOError="
				+ "&uamError="
				+ "&SSOCustType=0"
				+ "&loginOldUri="
				+ "&SSOOldAccount="
				+ "&SSOProTypePre=";
		
		
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput account = (HtmlTextInput) page.getElementById("account");//手机号
		HtmlAnchor getPW2 = (HtmlAnchor) page.getElementById("getPW2");//点击发送短信
		HtmlTextInput password = (HtmlTextInput) page.getElementById("password");//短信验证码输入框
		HtmlTextInput loginCodeRand = (HtmlTextInput) page.getElementById("loginCodeRand");//图片验证码输入
		HtmlImage loginCodeImage = (HtmlImage) page.getElementById("loginCodeImage");//验证码图片
		HtmlButtonInput t_login = (HtmlButtonInput) page.getElementById("t_login");//登录
		
		account.setText("15322868959");
		Page click = getPW2.click();
		String passwordValue = JOptionPane.showInputDialog("请输入短信验证码……");
		password.setText(passwordValue);
		
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName); 
		loginCodeImage.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		loginCodeRand.setText(inputValue);
		
		Page page2 = t_login.click();
		
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		//errorMsg
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
