package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class jingzhoulogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://58.54.135.133/wt-web/login";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码
		
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");

		username.setText("421022198708056610");//421022198708056610
		pass.setText("chaojiee");//chaojiee
		captcha.setText(inputValue);
		
		HtmlButton login = (HtmlButton) page.getFirstByXPath("//button[@onclick='individualSubmitForm();']");
		
		HtmlPage page3 = login.click();
		
		String url2 = "http://58.54.135.133/wt-web/home?logintype=1";
		Page page2 = getHtml(url2, webClient);
		String html = page2.getWebResponse().getContentAsString();

		System.out.println(html);
		if(html.indexOf("加载中 ...")!=-1){
			System.out.println("登录成功");
			String date = new Date().toLocaleString().substring(0, 9);
			String payurl = "http://58.54.135.133/wt-web/personal/jcmxlist?"
					+ "UserId=1"
					+ "&beginDate=2000-01-01"
					+ "&endDate="+date
					+ "&userId=1"
					+ "&pageNum=1"
					+ "&pageSize=1000";
			Page page4 = gethtmlPost(webClient, null, payurl);
			String html4 = page4.getWebResponse().getContentAsString();
			System.out.println(html4);
			JSONArray jsonArray = JSONObject.fromObject(html4).getJSONArray("results");
			for (int i = 0; i < jsonArray.size(); i++) {
				String string = jsonArray.getJSONObject(i).getString("dfje");//存入金额
				String string2 = jsonArray.getJSONObject(i).getString("jfje");//提取金额
				String string3 = jsonArray.getJSONObject(i).getString("rq");//日期
				String string4 = jsonArray.getJSONObject(i).getString("ye");//余额
				String string5 = jsonArray.getJSONObject(i).getString("ywlsh");//流水号
				String string6 = jsonArray.getJSONObject(i).getString("zy");//摘要
				
				System.out.println("\r存入金额:"+string+"\r提取金额:"+string2+"\r日期:"+string3+"\r余额："+string4+"\r流水号:"+string5+"\r摘要:"+string6);
				
			}
			
			String userurl = "http://58.54.135.133/wt-web/person/bgcx";
			Page page5 = gethtmlPost(webClient, null, userurl);
			String string = page5.getWebResponse().getContentAsString();
			JSONObject object3 = JSONObject.fromObject(string).getJSONArray("results").getJSONObject(0);
			String string2 = object3.getString("a003");//单位帐号
			String string3 = object3.getString("a004");//单位名称
			String string4 = object3.getString("a001");//职工帐号
			String string5 = object3.getString("a002");//职工姓名
			String string6 = object3.getString("a021");//证件类型
			String string7 = object3.getString("a008");//证件号码
			String string8 = object3.getString("yddh");//移动电话
			
			System.out.println(string2+string3+string4+string5+string6+string7+string8);
		}else{
			String error = "用户名、密码是否正确？请重试";
			if(page3.asText().contains("身份证不能为空")){
				error = "身份证不能为空";
			}else if(page3.asText().contains("密码不能为空")){
				error = "密码不能为空";
			}else if(page3.asText().contains("密码不正确")){
				error = "密码不正确";
			}else if(page3.asText().contains("密码格式不正确")){
				error = "密码格式不正确";
			}else if(page3.asText().contains("身份证格式错误")){
				error = "身份证格式错误";
			}else if(page3.asText().contains("验证码格式不正确")){
				error = "验证码格式不正确";
			}else if(page3.asText().contains("验证码不能为空")){
				error = "验证码不能为空";
			}else if(page3.asText().contains("验证码错误")){
				error = "验证码错误";
			}
			System.out.println("登录失败:"+error);
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
