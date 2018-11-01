package test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

/**
 * Unit test for simple App.
 */
public class AppTest_yuncheng {
	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.sxycgjj.gov.cn/login.jspx";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);

			// 用户名
			HtmlTextInput IDcard = (HtmlTextInput) html.getFirstByXPath("//input[@id='username']");
			// 密码
			HtmlPasswordInput password = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='password']");
			// 验证码输入框
			HtmlTextInput captcha = (HtmlTextInput) html.getFirstByXPath("//input[@id='captcha']");
			// 图片
			HtmlImage randImage = (HtmlImage) html.getFirstByXPath("//img[@id='captchaImg']");
			// 查询按钮
			HtmlSubmitInput button = (HtmlSubmitInput) html
					.getFirstByXPath("//*[@id=\"jvForm\"]/table[2]/tbody/tr/td[2]/div/table/tbody/tr[6]/td/input");
			
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			randImage.saveAs(file); 

			// 验证登录信息的链接：
            String code = JOptionPane.showInputDialog("请输入验证码……");
			
			IDcard.setText("zhanfengbo");
			password.setText("790623");
			captcha.setText(code);

			HtmlPage htmlpage = button.click();
			String contentAsString = htmlpage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
