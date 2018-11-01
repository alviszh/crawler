package test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
import com.google.gson.JsonObject;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class jiyuanlogin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://rspt.jiyuan.gov.cn:9000/login.html?"
				+ "redirect=http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbx_jbxxcx.shtml";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
		HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
		HtmlButtonInput btn_login_0 = (HtmlButtonInput) page.getElementById("btn_login_0");
		
		String name = "412828199007061877";
		String pass = "123456";
		username.setText(name);//412828199007061877
		password.setText(pass);//123456
		btn_login_0.click();
		
		String url8 = "http://rspt.jiyuan.gov.cn:9000/loginAction.action?"
				+ "from="
				+ "&redirect=http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbx_jbxxcx.shtml"
				+ "&username="+name
				+ "&password="+pass
				+ "&phoneNumber="
				+ "&smsVerificationCode="
				+ "&loginMode=0";
		Page page3 = gethtmlPost(webClient, null, url8);
		String string12 = page3.getWebResponse().getContentAsString();
		String err = JSONObject.fromObject(string12).getString("message");
		System.out.println(err);
		/**
		 * 个人基本信息
		 */
		String url2 = "http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxJbxxcxAction001.action";
		Page page2 = gethtmlPost(webClient, null, url2);
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jbxx_data")).getString("data");
		JSONArray object = JSONArray.fromObject(string);
		JSONObject obj = object.getJSONObject(1);
		if(obj.equals("")){
			obj = object.getJSONObject(0);
			
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
				string9+"\r性别:"+string10+"\r人员状态:"+string11);
		
		
		/***
		 * 失业
		 * http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxSybxjfxxcxAction001.action
		 * 生育
		 * http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxMybxjfxxcxAction001.action
		 */
		/**
		 * 养老保险个人缴费流水
		 */
/*		String url2 = "http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
		Page page2 = gethtmlPost(webClient, null, url2);
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		
		String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
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
			String string10 = object.getJSONObject(i).getString("F003");//征缴月份
			String string13 = object.getJSONObject(i).getString("F004");//计划月份
			String string16 = object.getJSONObject(i).getString("F001ZH");//单位名称
			String string17 = object.getJSONObject(i).getString("F001");//单位编号
			
			System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
					+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string13+"\r单位名称："+string16+"\r单位编号："+string17+"\r\r");
		}
		*/
		
		/**
		 * 医疗保险流水
		 */
	/*	String url2 = "http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
		Page page2 = gethtmlPost(webClient, null, url2);
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
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
		}*/
		
		/**
		 * 工伤保险流水
		 */
		/*String url2 = "http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbxGsbxjfxxcxAction001.action";
		Page page2 = gethtmlPost(webClient, null, url2);
		String html = page2.getWebResponse().getContentAsString();
		System.out.println(html);
		String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
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
		}*/
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
