package test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;

import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class guiganglogin {

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.gxggsi.gov.cn:7005/loginPerson.jsp";
		HtmlPage pag = (HtmlPage) getHtml(url, webClient);
		HtmlImage imag = (HtmlImage) pag.getFirstByXPath("//img[@id='codeimg']");
		HtmlPage page = (HtmlPage) imag.click();
		Thread.sleep(1000);
		HtmlTextInput c_username = (HtmlTextInput) page.getElementById("c_username");//身份证
		HtmlPasswordInput c_password = (HtmlPasswordInput) page.getElementById("c_password");//密码
		HtmlTextInput checkCode = (HtmlTextInput) page.getElementById("checkCode");//验证码
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='codeimg']");//验证码图片
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlDivision log  = page.getFirstByXPath("//div[@class='login2_loginBtn']");//登录

		c_username.setText("450702198112235113");//450702198112235113
		c_password.setText("Ffupeng1981");//Ffupeng1981
		checkCode.setText(inputValue);
		log.click();
//		Thread.sleep(10000);
//		String url2 = "http://www.gxggsi.gov.cn:7005/privateLoginAction!login.do?userid=278475";//登录报错json
//		Page page2 = getHtml(url2, webClient);
//		String err = page2.getWebResponse().getContentAsString();
//		JSONObject obj = JSONObject.fromObject(err);
//		String error = obj.getString("msg");
		
		
		String url3 = "http://www.gxggsi.gov.cn:7005/indexAction.do?indexstyle=default";//登录成功首页
		Page page3 = getHtml(url3, webClient);
		
		String html3 = page3.getWebResponse().getContentAsString();
		System.out.println(html3);
		if(html3.indexOf("贵港市社会保险网上大厅")!=-1){
			String url2 = "http://www.gxggsi.gov.cn:7005/privateLoginAction!login.do?userid=278475";//登录报错json
			Page page4 = getHtml(url2, webClient);
			String err = page4.getWebResponse().getContentAsString();
			JSONObject obj = JSONObject.fromObject(err);
			String error = obj.getString("msg");
			System.out.println(error);
			System.out.println("登录成功");
			System.out.println(html3);

			//个人信息
			/*	Cookie cookie = webClient.getCookieManager().getCookie("userid");
			String userid = cookie.getValue();
			System.out.println("userid------------>"+userid);
			String url6 = "http://www.gxggsi.gov.cn:7005/privateHomeAction!query.do?userid="+userid;
			Page page4 = getHtml(url6, webClient);
			String html4 = page4.getWebResponse().getContentAsString();
			System.out.println(html4);
			
			JSONObject object = JSONObject.fromObject(html4);
			String username = object.getJSONObject("fieldData").getString("aac003");//姓名
			String gsname = object.getJSONObject("fieldData").getString("aab069");//公司名称
			String zt = object.getJSONObject("fieldData").getString("aac031");//状态
			String idcard = object.getJSONObject("fieldData").getString("aac002");//身份证
			String tel = object.getJSONObject("fieldData").getString("aae005");//电话
			String yl = object.getJSONObject("fieldData").getString("aae240yl");//养老账户余额
			String yb = object.getJSONObject("fieldData").getString("aae240yb");//医保账户余额
			String time = object.getJSONObject("fieldData").getString("maxaab191");//最近缴费时间

			String user2 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryInsuranceInfoAction!toSearchPersonInfo.do";
			Page page5 = getHtml(user2, webClient);
			String html5 = page5.getWebResponse().getContentAsString();
			System.out.println(html5);
			JSONObject object2 = JSONObject.fromObject(html5);
			String num = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac001");//个人编号
			String sex = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac004");//性别
			String mz = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac005");//民族
			String brithday = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac006");//出生日期
			String cgtime = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac007");//参加工作日期
			String hkxz = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac009");//户口性质
			String hkszd = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac010");//户口所在地
			String ryzt = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac008");//人员状态

			System.out.println("个人信息如下：\r姓名："+username+"\r公司名称:"+gsname+"\r状态:"+zt+"\r身份证:"
			+idcard+"\r电话:"+tel+"\r养老账户余额:"+yl+"\r医保账户余额:"+yb+"\r最近缴费时间:"+time+"\r个人编号:"+num
			+"\r性别:"+sex+"\r民族:"+mz+"\r出生日期:"+brithday+"\r参加工作日期:"+cgtime+"\r户口性质:"+hkxz+"\r户口所在地:"+hkszd+"\r人员状态:"+ryzt);
			 */

			//养老保险流水
			for (int j = 0; j < 5; j++) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				year = year - j;

			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do"
					+ "?dto%5B%27aae002%27%5D="+year
					+ "&&dto%5B%27aae140%27%5D=110&&";//110-->养老
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				JSONObject object = JSONObject.fromObject(html6);
				JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String string = object2.getString("aab069");//公司名称
					String string2 = object2.getString("aac003");//姓名
					String string3 = object2.getString("aae002");//期号
					String string4 = object2.getString("aic020");//缴费工资
					String string5 = object2.getString("dwjfhtcje");//单位缴费
					String string6 = object2.getString("grjnje");//个人缴费
					String string7 = object2.getString("aab191");//到账日期
					String string8 = object2.getString("aac066");//职务类型
					String type = "企业养老";
					System.out.println(year+"年的第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				}
			}else{
				System.out.println("无数据");
				
			}
			}


			//医疗
			for (int j = 0; j < 5; j++) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				year = year - j;
				String url6 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do"
						+ "?dto%5B'aae002'%5D="+year
						+ "&&dto%5B'aae140'%5D=310&&";//310-->基本医疗
				Page page6 = getHtml(url6, webClient);
				String html = page6.getWebResponse().getContentAsString();
				System.out.println(html);
				if(html.indexOf("msg")==-1){
					System.out.println("有数据：");
					JSONObject object = JSONObject.fromObject(html);
					JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
					for (int i = 0; i < array.size(); i++) {
						JSONObject object2 = array.getJSONObject(i);
						String string = object2.getString("aab069");//公司名称
						String string2 = object2.getString("aac003");//姓名
						String string3 = object2.getString("aae002");//期号
						String string4 = object2.getString("aic020");//缴费工资
						String string5 = object2.getString("dwjfhtcje");//单位缴费
						String string6 = object2.getString("grjnje");//个人缴费
						String string7 = object2.getString("aab191");//到账日期
						String string8 = object2.getString("aac066");//职务类型
						String type = "基本医疗";
						System.out.println(year+"年的第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
					}
				}else{
					System.out.println("无数据");
				}
			}
			
			//失业
			for (int j = 0; j < 5; j++) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				year = year - j;

			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=210&&";//210-->失业
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				JSONObject object = JSONObject.fromObject(html6);
				JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String string = object2.getString("aab069");//公司名称
					String string2 = object2.getString("aac003");//姓名
					String string3 = object2.getString("aae002");//期号
					String string4 = object2.getString("aic020");//缴费工资
					String string5 = object2.getString("dwjfhtcje");//单位缴费
					String string6 = object2.getString("grjnje");//个人缴费
					String string7 = object2.getString("aab191");//到账日期
					String string8 = object2.getString("aac066");//职务类型
					String type = "失业保险";
					System.out.println(year+"年的第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				}
			}else{
				System.out.println("无数据");
				
			}
			}
			
			
			
			//工伤
			for (int j = 0; j < 5; j++) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				year = year - j;

			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=410&&";//410-->工伤
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				JSONObject object = JSONObject.fromObject(html6);
				JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String string = object2.getString("aab069");//公司名称
					String string2 = object2.getString("aac003");//姓名
					String string3 = object2.getString("aae002");//期号
					String string4 = object2.getString("aic020");//缴费工资
					String string5 = object2.getString("dwjfhtcje");//单位缴费
					String string6 = object2.getString("grjnje");//个人缴费
					String string7 = object2.getString("aab191");//到账日期
					String string8 = object2.getString("aac066");//职务类型
					String type = "工伤保险";
					System.out.println(year+"年的第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				}
			}else{
				System.out.println("无数据");
				
			}
			}
			
			
			//生育
			for (int j = 0; j < 5; j++) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				year = year - j;

			String url4 = "http://www.gxggsi.gov.cn:7005/yhwssb/query/queryPersPayAction!queryPayment.do?"
					+ "dto%5B'aae002'%5D="+year
					+ "&&dto%5B'aae140'%5D=510&&";//510-->生育
			Page page6 = getHtml(url4, webClient);
			String html6 = page6.getWebResponse().getContentAsString();
			System.out.println(html6);
			if(html6.indexOf("msg")==-1){
				System.out.println("有数据：");
				JSONObject object = JSONObject.fromObject(html6);
				JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject object2 = array.getJSONObject(i);
					String string = object2.getString("aab069");//公司名称
					String string2 = object2.getString("aac003");//姓名
					String string3 = object2.getString("aae002");//期号
					String string4 = object2.getString("aic020");//缴费工资
					String string5 = object2.getString("dwjfhtcje");//单位缴费
					String string6 = object2.getString("grjnje");//个人缴费
					String string7 = object2.getString("aab191");//到账日期
					String string8 = object2.getString("aac066");//职务类型
					String type = "生育保险";
					System.out.println(year+"年的第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				}
			}else{
				System.out.println("无数据");
				
			}
			}

		}else{
			//System.out.println("登录失败："+error);
			System.out.println("登录失败");
			System.out.println(html3);
			webClient.close();
			main(args);
		}
	}
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		webRequest.setCharset(Charset.forName("UTF-8"));
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
