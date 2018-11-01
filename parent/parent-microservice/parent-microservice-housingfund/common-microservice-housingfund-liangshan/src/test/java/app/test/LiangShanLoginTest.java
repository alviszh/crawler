package app.test;

import java.net.URL;
import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;


/**
 * @description:   此方式无法登陆成功
 * @author: sln 
 * @date: 2018年2月2日 下午2:01:13 
 */
public class LiangShanLoginTest {
	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		String url = "http://118.122.8.57:81/ispobs/Forms/SysFiles/Login.aspx"; 
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest); 
		
		HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtUserName']"); 
		HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='txtPassWord']"); 
		HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtGRDLYZM']"); 
		HtmlButtonInput submitbt = (HtmlButtonInput)page.getFirstByXPath("//input[@class='fm-button']"); 
		
		
		url="http://118.122.8.57:81/ispobs/Forms/SysFiles/Sys_Yzm.aspx";
		webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page ypage = webClient.getPage(webRequest); 
		if(null!=ypage){
			SavaImageUtil.getImagePath(ypage);
		}
		
		loginName.setText("513401198708266937"); 
		loginPassword.setText("000000"); 	
		String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
		validateCode.setText(inputValue); 	
		Page lPage = submitbt.click(); 
		
//		String alertMsg = WebCrawler.getAlertMsg();
//		if(alertMsg!=null){
//			System.out.println("获取的弹出框登录信息是："+alertMsg);    //不能获取弹框信息
//		}
		String html = lPage.getWebResponse().getContentAsString(); 
		System.out.println("模拟点击登录后获取的页面信息是："+html);
		
		//测试访问个人账户信息
		url="http://118.122.8.57:81/ispobs/Forms/CX/CX_GRZHJBXXCX.aspx";
		webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page userpage = webClient.getPage(webRequest); 
		if(null!=userpage){
			String contentAsString = userpage.getWebResponse().getContentAsString();
			System.out.println("请求用户个人信息，响应的信息是："+contentAsString);
		}
		
	}

}
