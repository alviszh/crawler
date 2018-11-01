

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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class leshanlogin {

	public static void main(String[] args) throws Exception {
		String url = "http://www.scls.lss.gov.cn:8888/lswtqt/toLogin.jhtml";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		HtmlTextInput randomnum = (HtmlTextInput) page.getElementById("captcha_gr");
		
		HtmlImage image = (HtmlImage) page.getElementById("codeimg");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		String usernamea = "511123198409122263";
		username.setText(usernamea);
		password.setText("ls66021520LS");
		randomnum.setText(inputValue);
		
		HtmlAnchor login_btn = page.getFirstByXPath("//a[@onclick='login();']");
		Thread.sleep(8000);
		HtmlPage click = login_btn.click();
		
		String url22 = "http://www.scls.lss.gov.cn:8888/lswtqt/toUsercenter.jhtml";
		Page page2 = getHtml(url22, webClient);
		//Page page2 = login_btn.click();
	
		String asString = page2.getWebResponse().getContentAsString();
		System.out.println(asString);
		if(asString.indexOf("个人用户中心")!=-1){
			System.out.println("登录成功");
			/**
			 * 个人信息
			 */
			String userurl = "http://www.scls.lss.gov.cn:8888/lswtqt/117372/Q2025.jspx";
			Page page3 = getHtml(userurl, webClient);
			String contentAsString = page3.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			
			JSONObject object = JSONObject.fromObject(contentAsString).getJSONObject("fieldData");
			String string = object.getString("aac004").trim();//性别
			String string2 = object.getString("aac003").trim();//姓名
			String string3 = object.getString("aac006").trim();//出生日期
			String string4 = object.getString("aac005").trim();//民族
			String string5 = object.getString("aac009").trim();//户口类型
			String string6 = object.getString("aac060").trim();//人员状态
			String string7 = object.getString("age").trim();//年龄
			String string8 = object.getString("aaf002").trim();//银行类别
			String string9 = object.getString("aae008").trim();//银行网点
			String string10 = object.getString("aae009").trim();//户名
			String string11 = object.getString("aae010").trim();//代扣银行账号
			String string12 = object.getString("aae005").trim();//电话
			String string13 = object.getString("aae006").trim();//常住地址
			System.out.println("性别:"+string+"\r姓名:"+string2+"\r出生日期:"+string3+"\r民族:"+string4+"\r户口类型:"+
			string5+"\r人员状态:"+string6+"\r年龄:"+string7+"\r银行类别:"+string8+"\r银行网点:"+string9+"\r户名:"+string10+"\r代扣银行账号:"+
					string11+"\r电话:"+string12+"\r常住地址:"+string13);
			
			
			
//			aae140险种类型 aae036_s开始日期 aae036_e截止日期
			/**
			 * 养老
			 */
			String ylurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
				+ "aae140=110"
				+ "&pageSize=1000"
				+ "&aae036_s=200001"
				+ "&aae036_e=201804"
				+ "&notkeyflag=0";
			Page page4 = gethtmlPost(webClient, null, ylurl);
			String contentAsString2 = page4.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if(contentAsString2.indexOf("lists")!=-1){
				JSONArray jsonArray = JSONObject.fromObject(contentAsString2).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
				for (int i = 0; i < jsonArray.size(); i++) {
					String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
					String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
					String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
					String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
					String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
					String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
					String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
					String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
					String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
					
					System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
							"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				}
			}
			
			
			
			
			/**
			 * 失业
			 */
			/*String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=210"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e=201804"
					+ "&notkeyflag=0";
				Page page5 = gethtmlPost(webClient, null, yurl);
				String contentAsString3 = page5.getWebResponse().getContentAsString();
				System.out.println(contentAsString3);
				if(contentAsString3.indexOf("lists")!=-1){
					JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
					for (int i = 0; i < jsonArray.size(); i++) {
						String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
						String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
						String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
						String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
						String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
						String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
						String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
						String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
						String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
						System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
								"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
					}
				}*/
			
			
			/**
			 * 医疗
			 */
			/*String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=310"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e=201804"
					+ "&notkeyflag=0";
				Page page5 = gethtmlPost(webClient, null, yurl);
				String contentAsString3 = page5.getWebResponse().getContentAsString();
				System.out.println(contentAsString3);
				if(contentAsString3.indexOf("lists")!=-1){
					JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
					for (int i = 0; i < jsonArray.size(); i++) {
						String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
						String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
						String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
						String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
						String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
						String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
						String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
						String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
						String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
						System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
								"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
					}
				}*/
			
			/**
			 * 工伤
			 */
			/*String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=410"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e=201804"
					+ "&notkeyflag=0";
				Page page5 = gethtmlPost(webClient, null, yurl);
				String contentAsString3 = page5.getWebResponse().getContentAsString();
				System.out.println(contentAsString3);
				if(contentAsString3.indexOf("lists")!=-1){
					JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
					for (int i = 0; i < jsonArray.size(); i++) {
						String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
						String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
						String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
						String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
						String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
						String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
						String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
						String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
						String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
						System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
								"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
					}
				}*/
			/**
			 * 生育
			 */
			String yurl = "http://www.scls.lss.gov.cn:8888/lswtqt/115090/Q2005.jspx?"
					+ "aae140=510"
					+ "&pageSize=1000"
					+ "&aae036_s=200001"
					+ "&aae036_e=201804"
					+ "&notkeyflag=0";
				Page page5 = gethtmlPost(webClient, null, yurl);
				String contentAsString3 = page5.getWebResponse().getContentAsString();
				System.out.println(contentAsString3);
				if(contentAsString3.indexOf("lists")!=-1){
					JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
					for (int i = 0; i < jsonArray.size(); i++) {
						String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
						String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
						String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
						String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
						String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
						String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
						String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
						String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
						String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
						System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
								"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
					}
				}
		}else{
			System.out.println("登录失败");
			if(click.asText().contains("用户名不能为空！")){
				System.out.println("用户名不能为空！");
			}else if(click.asText().contains("此处为个人用户登录，请输入正确的身份证号或手机号")){
				System.out.println("此处为个人用户登录，请输入正确的身份证号或手机号");
			}else if(click.asText().contains("未检测到"+usernamea+"的信息,请检查账号是否填写正确")){
				System.out.println("未检测到"+usernamea+"的信息,请检查账号是否填写正确");
			}else if(click.asText().contains("请先输入密码！")){
				System.out.println("请先输入密码！");
			}else if(click.asText().contains("请输入图形验证码!")){
				System.out.println("请输入图形验证码!");
			}else if(click.asText().contains("验证码错误！")){
				System.out.println("验证码错误！");
			}else if(click.asText().contains("用户名或者密码不正确，如忘记密码，可使用“忘记密码”功能找回密码。连续错误20次之后将锁定此账户。")){
				System.out.println("用户名或者密码不正确，如忘记密码，可使用“忘记密码”功能找回密码。连续错误20次之后将锁定此账户。");
			}
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
