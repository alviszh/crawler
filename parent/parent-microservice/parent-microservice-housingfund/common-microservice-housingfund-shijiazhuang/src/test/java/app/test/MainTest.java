package app.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;


import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月19日 下午2:09:47 
 */
public class MainTest {
	public static void main(String[] args) throws Exception, IOException{ 
		String url="http://www.sjzgjj.cn/wsyyt/";
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Host", "www.sjzgjj.cn");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
		HtmlPage hPage = webClient.getPage(webRequest);	
		if(null!=hPage){
			webClient = hPage.getWebClient();
			String imageUrl="http://www.sjzgjj.cn/wsyyt/vericode.jsp";
			WebRequest webRequestImage = new WebRequest(new URL(imageUrl), HttpMethod.GET);
			webRequestImage.setAdditionalHeader("Host", "www.sjzgjj.cn");
			webRequestImage.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			webClient.getPage(webRequestImage);
			Thread.sleep(1000);
			HtmlImage image =hPage.getFirstByXPath("//img[@src='vericode.jsp']"); 
			image.click(); //获取之后	再点击下
			Thread.sleep(2000);
			image=hPage.getFirstByXPath("//img[@src='vericode.jsp']"); 
			String imageName = System.currentTimeMillis()+".jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']"); 
			HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']"); 
			HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
			loginName.setText("130181199004034827"); 
			loginPassword.setText("123123"); 	
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(inputValue); 	
			HtmlPage logonPage = submitbt.click(); 
			System.out.println("点击登陆后获取的页面是："+logonPage.asXml());
			
			//登录成功之后的webClient
			webClient = logonPage.getWebClient();
			url="http://www.sjzgjj.cn/wsyyt/init.summer?_PROCID=60020007";
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			System.out.println("获取数据===第一部分用户信息的页面是："+page.getWebResponse().getContentAsString());
		}
	}
	
	public static String getImage(HtmlPage hPage,WebClient webClient) throws Exception{
		//有时候打开登录页面的时候，验证码是一个不显示数据的图片，故需要点击下（刷新显示出来）
		String imageUrl="http://www.sjzgjj.cn/wsyyt/vericode.jsp";
		WebRequest webRequestImage = new WebRequest(new URL(imageUrl), HttpMethod.GET);
		Page imagePage= webClient.getPage(webRequestImage);
		String contentAsString = imagePage.getWebResponse().getContentAsString();
		return contentAsString;
	}
}
