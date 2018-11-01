package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class luoyanglogin {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://gr.ly12333.com/login.html";

		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//帐号
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");//密码
		HtmlButtonInput hand = (HtmlButtonInput) page.getElementById("btn_login_0");//登录
//		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("validateCode");//验证码
//
//		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='imgValidateCode']");//验证码
//		String imageName = "111.jpg"; 
//		File file = new File("F:\\img\\" + imageName);
//		image.saveAs(file);
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		String name = "410302198411050021";
		String pass = "841105";
		username.setText(name);//410302198411050021
		password.setText(pass);//841105
//		captcha.setText(inputValue);
		hand.click();
		String url2 = "http://gr.ly12333.com/grpt/zgbx/zgbx_jbxxcx.shtml";
		Page page2 = getHtml(url2, webClient);
		String html = page2.getWebResponse().getContentAsString();
		if(html.indexOf("人社平台-职工保险-基本信息查询")!=-1){
			System.out.println("登录成功");
			System.out.println(html);
			//个人信息
			/*String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxJbxxcxAction001.action";
			Page page4 = gethtmlPost(webClient, null, url4);
			String html4 = page4.getWebResponse().getContentAsString();
			System.out.println(html4);
			String string = JSONObject.fromObject(JSONObject.fromObject(html4).getString("json_jbxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			JSONObject obj = object.getJSONObject(0);
			if(obj.equals("")){
				obj = object.getJSONObject(1);
				
			}
			String string2 = obj.getString("F007");//出生日期
			String string3 = obj.getString("F006ZH");//民族
			String string4 = obj.getString("F008");//首次参保时间
			String string5 = obj.getString("F001");//单位编号
			String string6 = obj.getString("F001ZH");//单位名称
			String string7 = obj.getString("F002");//个人编号
			String string8 = obj.getString("F003");//身份证号
			String string9 = obj.getString("F004");//姓名
			String string10 = obj.getString("F005ZH");//性别
			String string11 = obj.getString("F009ZH");//人员状态
			
			System.out.println("出生日期:"+string2+"\r民族："+string3+"\r首次参保时间:"+string4+"\r单位编号:"
					+string5+"\r单位名称:"+string6+"\r个人编号:"+string7+"\r身份证号:"+string8+"\r姓名:"+
					string9+"\r性别:"+string10+"\r人员状态:"+string11);*/
			
		//养老保险
			/*String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
			Page page3 = gethtmlPost(webClient, null, url4);
			String html3 = page3.getWebResponse().getContentAsString();
			System.out.println(html3);
			
			String string = JSONObject.fromObject(JSONObject.fromObject(html3).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for(int i=2;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string13 = object.getJSONObject(i).getString("F004");//计划月份
				String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string17 = object.getJSONObject(i).getString("F001");//单位编号
				
				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
			}*/
		//医疗
			/*String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
			Page page4 = gethtmlPost(webClient, null, url4);
			String html4 = page4.getWebResponse().getContentAsString();
			
			String string = JSONObject.fromObject(JSONObject.fromObject(html4).getString("json_jfxx_data")).getString("data");
			
			JSONArray object = JSONArray.fromObject(string);
			
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string12 = object.getJSONObject(i).getString("F003");//征缴月份
				String string13 = object.getJSONObject(i).getString("F004");//计划月份
				String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string17 = object.getJSONObject(i).getString("F001");//单位编号
				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string12+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
			}*/
		//工伤
//			String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxGsbxjfxxcxAction001.action";
//			Page page4 = gethtmlPost(webClient, null, url4);
//			String html4 = page4.getWebResponse().getContentAsString();
//			
//			String string = JSONObject.fromObject(JSONObject.fromObject(html4).getString("json_jfxx_data")).getString("data");
//			
//			JSONArray object = JSONArray.fromObject(string);
//			
//			for(int i=0;i<object.size();i++){
//				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
//				String string3 = object.getJSONObject(i).getString("F007");//合计
//				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
//				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
//				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
//				String string7 = object.getJSONObject(i).getString("F013");//到账日期
//				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
//				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
//				String string12 = object.getJSONObject(i).getString("F003");//征缴月份
//				String string13 = object.getJSONObject(i).getString("F004");//计划月份
//				String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
//				String string17 = object.getJSONObject(i).getString("F001");//单位编号
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string12+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
//			}
			//失业
			/*String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxSybxjfxxcxAction001.action";
			Page page4 = gethtmlPost(webClient, null, url4);
			String html4 = page4.getWebResponse().getContentAsString();
			
			String string = JSONObject.fromObject(JSONObject.fromObject(html4).getString("json_jfxx_data")).getString("data");
			
			JSONArray object = JSONArray.fromObject(string);
			
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string12 = object.getJSONObject(i).getString("F003");//征缴月份
				String string13 = object.getJSONObject(i).getString("F004");//计划月份
				String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string17 = object.getJSONObject(i).getString("F001");//单位编号
				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string12+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
			}*/
			
			//生育
			String url4 = "http://gr.ly12333.com/grpt/zgbx/zgbxMybxjfxxcxAction001.action";
			Page page4 = gethtmlPost(webClient, null, url4);
			String html4 = page4.getWebResponse().getContentAsString();
			
			String string = JSONObject.fromObject(JSONObject.fromObject(html4).getString("json_jfxx_data")).getString("data");
			
			JSONArray object = JSONArray.fromObject(string);
			
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string12 = object.getJSONObject(i).getString("F003");//征缴月份
				String string13 = object.getJSONObject(i).getString("F004");//计划月份
				String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string17 = object.getJSONObject(i).getString("F001");//单位编号
				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string12+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
			}
		}else{
//			String url3 = "https://gr.ly12333.com:8443/AjaxLogin/Personloginin.cspx?"
//					+ "IdCard="+name
//					+ "&Password="+pass
//					+ "&validatecode="+inputValue;
//			Page page3 = gethtmlPost(webClient, null, url3);
//			String er = page3.getWebResponse().getContentAsString();
//			String error = JSONObject.fromObject(er).getString("Message");
			
			Document doc = Jsoup.parse(html);
			String error = doc.getElementById("li_tip_id_0").text();
			System.out.println("登录失败:"+error);
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
