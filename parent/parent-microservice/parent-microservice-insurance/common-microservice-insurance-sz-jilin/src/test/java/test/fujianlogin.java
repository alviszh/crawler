package test;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class fujianlogin {

	public static void main(String[] args) throws Exception {
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.fj12333.gov.cn:268/fwpt/loginPage.html?service=corpPersion.html";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) page.getElementById("aac003");//姓名
		HtmlTextInput idcard = (HtmlTextInput) page.getElementById("aac002");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("ysc002");//密码
		HtmlTextInput randCode = (HtmlTextInput) page.getElementById("randCode");//验证码
		HtmlImage image = (HtmlImage) page.getElementById("checkImg");//图片验证码
		HtmlAnchor sub = (HtmlAnchor) page.getElementById("loginimg");//提交
		
		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		username.setText("李胜");//游晓芬      李胜
		idcard.setText("362323199404066512");//350600198611021521  362323199404066512
		pass.setText("ls123456");//528671lhy    ls123456
		randCode.setText(inputValue);
		HtmlPage click = sub.click();
		String error = WebCrawler.getAlertMsg();
		String html = click.getWebResponse().getContentAsString();
		System.out.println(html);
		if(html.indexOf("安全退出")!=-1){
			System.out.println("登录成功");
			
			
			
		}else{
			
			if(click.asText().contains("密码不正确，请重新输入！")){
				error = "密码不正确，请重新输入！";
			}else if(click.asText().contains("您输入的验证码有误,请重新输入!")){
				error = "您输入的验证码有误,请重新输入!";
			}else if(click.asText().contains("该身份证号码未注册，请先注册！")){
				error = "该身份证号码未注册，请先注册！";
			}else if(click.asText().contains("输入的姓名或身份证号不正确，请核对！")){
				error = "输入的姓名或身份证号不正确，请核对！";
			}
			System.out.println(error);
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
}
