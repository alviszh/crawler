package test;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;

public class Login_username_password {
	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setTimeout(70000);
			// 发送验证码请求
			WebRequest requestSettings = new WebRequest(new URL("https://gr.kf12333.cn/loginAction.action"),
					HttpMethod.POST);
			String body = "from=&redirect=&username=510704198806124927&password=123456&phoneNumber=&smsVerificationCode=&loginMode=0";
			requestSettings.setRequestBody(body);
			Page loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println("登录结果：" + contentAsString);
			JSONObject object2 = JSONObject.fromObject(contentAsString);
			String message2 = object2.getString("message");
			if (message2.contains("登录成功")) {
				System.out.println("登录成功!");
			} else {
				System.out.println("登录失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
