package app.test;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

/**
 * 在登陆或者爬取信息的时候，由于网站自身原因，会报read time out 异常
 * @author sln
 *
 */
public class LoginTest {
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String url="https://wsbs.hzgjj.com:7005/hzgjj-wsyyt/personLogin.html";
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest); 
			
			HtmlImage image = page.getFirstByXPath("//img[@id='veryCodeImg']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			
		/*	HtmlTextInput loginIdNum = (HtmlTextInput)page.getFirstByXPath("//input[@name='certNo']"); 
			HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@name='personAcctNo']"); 
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@name='password']"); 
			HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@name='veriCode']"); 
			HtmlButton submitbt = (HtmlButton)page.getFirstByXPath("//input[@id='sub']"); 
			loginIdNum.setText("330501198911050823");
			loginName.setText("3305070625839"); 
			loginPassword.setText("050823"); 	
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(inputValue); 	
			page= submitbt.click();    //用模拟点击的方式，此处报空指针错误
		*/
			//验证登录信息的链接：
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			
			url="https://wsbs.hzgjj.com:7005/hzgjj-wsyyt/ajax/login.action";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="_sid=personLoginService_personLogin&json={\"certNo\":\"330501198911050823\",\"personAcctNo\":\"3305070625839\",\"password\":\"050823\",\"veriCode\":\""+code.trim()+"\"}&uid=";
//			String requestBody="_sid=personLoginService_personLogin&json=%7B%22certNo%22%3A%22330501198911050823%22%2C%22personAcctNo%22%3A%223305070625839%22%2C%22password%22%3A%2205082311%22%2C%22veriCode%22%3A%22"+code.trim()+"%22%7D&uid=";
			webRequest.setRequestBody(requestBody);
			Page loginResultPage=webClient.getPage(webRequest);
			if(null!=loginResultPage){
				String html = loginResultPage.getWebResponse().getContentAsString();
				System.out.println("模拟点击登录响应的信息是："+html);
				if(html.contains("操作成功")){
					System.out.println("登录成功");
					//该网站一个链接就可以响应所有的缴费数据，但是获取缴费信息需要用个人信息中的部分参数(个人账号还有姓名)
					//先获取个人基本信息
					url="https://wsbs.hzgjj.com:7005/hzgjj-wsyyt/ajax/ejx4web.action";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					requestBody="_sid=individualAccountQueryService_loadAccountInfo"
							+ "&json={\"grzh\":\"3305070625839\"}"
							+ "&uid=29597085-1"  //这个参数必须有
							;
					webRequest.setRequestBody(requestBody);
					Page page1= webClient.getPage(webRequest); 
					if(null!=page1){
						html=page1.getWebResponse().getContentAsString();
						System.out.println("获取到的用户基本信息是："+html);
					}
					
					//获取缴费信息的链接和获取基本信息的链接一样，就是参数不一样
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					requestBody="_sid=personDetailQueryService_infoQuery"
							+ "&json={\"page\":\"1\",\"rows\":\"10\","
							+ "\"searchCondition\":{"
							+ "\"grzh\":\"3305070625839\","
//							+ "\"grxm\":\"\u5F90\u6B22\u6B22\","   //姓名可以省略，经过测试发现
							+ "\"jzrqBegin\":\"20150929\","
							+ "\"jzrqEnd\":\"20180227\"}}"
							+ "&uid=29597085-1"
							+ "&_search=false"
							+ "&nd=1519711059283"
							+ "&rows=10"
							+ "&page=1"
							+ "&sidx="
							+ "&sord=asc";
					webRequest.setRequestBody(requestBody);
					page1=webClient.getPage(webRequest);
					if(null!=page1){
						html=page1.getWebResponse().getContentAsString();
						System.out.println("获取到的缴费信息是："+html);
					}
					
					
				}else{
					System.out.println("出现了登录错误信息："+html);
					//包含false
					String errorMsg = JSONObject.fromObject(html).getString("msg");
					if(errorMsg.contains("验证码错误")){
						
					}else if(errorMsg.contains("个人账户信息不存在")){
						
					}else if(errorMsg.contains("密码错误")){
						
					}else{
						System.out.println("出现了其他类型的错误"+errorMsg);
					}
					
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
	
}
