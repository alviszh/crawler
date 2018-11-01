package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class pingan {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.pingan.com/property_insurance/pa18AutoInquiry/login.screen";
		HtmlPage page = getHtml(url, webClient);
		HtmlTextInput vehiclePolicyNo = (HtmlTextInput) page.getElementById("vehiclePolicyNo");//保单号
		HtmlTextInput vehicleIdNo = (HtmlTextInput) page.getElementById("vehicleIdNo");//证件号
		HtmlTextInput vehicleVerifyCode = (HtmlTextInput) page.getElementById("vehicleVerifyCode");//验证码输入
		HtmlImage image = (HtmlImage)page.getFirstByXPath("//img[@src='./rand.do']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		vehiclePolicyNo.setText("13308013900482149897");//13308013900482149897
		vehicleIdNo.setText("140322197311250048");//140322197311250048
		vehicleVerifyCode.setText(inputValue);
		
		HtmlAnchor but = (HtmlAnchor) page.getElementById("search");
		HtmlPage page2 = but.click();
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		//发送短信
		if(html.indexOf("<title>车险承保理赔信息查询</title>")!=-1){
			System.out.println("保单号及其证件号正确，开始短信验证！");
			
			HtmlTextInput elementById = (HtmlTextInput) page2.getElementById("mobileNo");
			elementById.setText("");//手机号码
			HtmlAnchor sendMessage = (HtmlAnchor) page2.getElementById("sendMessage");
			HtmlPage page3 = sendMessage.click();
			if(page3.asText().contains("手机号码不符合规范，请您重输入。")){
				System.out.println("手机号码不符合规范，请您重输入。");
			}else if(page3.asText().contains("手机号码错误,请重新输入")){
				System.out.println("手机号码错误,您一共有5次机会。");
			}else{
				System.out.println("发送成功");
			}
		//验证短信
			HtmlTextInput checkNo = (HtmlTextInput) page3.getElementById("checkNo");
			checkNo.setText("");
			HtmlAnchor submit = page.getFirstByXPath("//a[@onclick='return submitForm()']");
			HtmlPage page4 = submit.click();
			if(page4.asText().contains("请您输入6位验证码。")){
				System.out.println("请您输入6位验证码。");
			}else if(page4.asText().contains("验证码已过期,请重新获取")){
				System.out.println("验证码已过期,请重新获取");
			}else if(page4.asText().contains("验证码不正确,请重新获取")){
				System.out.println("验证码不正确,请重新获取");
			}else{
				System.out.println("短信验证成功");
			}
		}else{
			if(page2.asText().contains("保单号不能为空")){
				System.out.println("保单号不能为空");
			}else if(page2.asText().contains("保单号不合规范")){
				System.out.println("保单号不合规范");
			}else if(page2.asText().contains("证件号不能为空")){
				System.out.println("证件号不能为空");
			}else if(page2.asText().contains("验证码不能为空")){
				System.out.println("验证码不能为空");
			}else if(page2.asText().contains("请输入正确的验证码")){
				System.out.println("请输入正确的验证码");
			}else if(page2.asText().contains("您查询的车辆未在我公司投保，如有疑问请留言或联系本公司。")){
				System.out.println("保单号码输入错误，查询不到该保单");
			}else if(page2.asText().contains("保单号与证件号不匹配，请核实后重新输入！")){
				System.out.println("保单号与证件号不匹配，请核实后重新输入！");
			}
		}
		
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
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
