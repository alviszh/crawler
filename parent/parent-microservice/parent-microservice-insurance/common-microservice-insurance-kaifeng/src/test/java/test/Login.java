package test;

import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;

public class Login {

	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setTimeout(70000);
			// 发送验证码请求
			WebRequest requestSettings = new WebRequest(new URL("https://gr.kf12333.cn/loginSMSAction.action"),
					HttpMethod.POST);
			String body = "phoneNumber=18738990573&sjc=Mon May 07 2018 11:08:05 GMT+0800 (中国标准时间)";
			requestSettings.setRequestBody(body);
			Page loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println("发送那个验证码请求的结果是：" + contentAsString);
			JSONObject object = JSONObject.fromObject(contentAsString);
			String message = object.getString("message");
			if (message.contains("发送成功")) {
				System.out.println("验证码发送成功！");
				String inputValue = JOptionPane.showInputDialog("请输入验证码……");
				// 登录请求
				WebRequest requestSettings1 = new WebRequest(new URL("https://gr.kf12333.cn/loginAction.action"),
						HttpMethod.POST);
				String body1 = "from=&redirect=&username=&password=&phoneNumber=18738990573&smsVerificationCode="
						+ inputValue + "&loginMode=1";
				requestSettings1.setRequestBody(body1);
				Page loginedPage1 = webClient.getPage(requestSettings1);
				String contentAsString1 = loginedPage1.getWebResponse().getContentAsString();
				System.out.println("登录结果：" + contentAsString1);
				JSONObject object2 = JSONObject.fromObject(contentAsString1);
				String message2 = object2.getString("message");
				if (message2.contains("登录成功")) {
					System.out.println("登录成功!");
				} else {
					System.out.println("登录失败!");
				}
			} else {
				System.out.println("验证码发送失败！请重新发送！");
			}
		} catch (Exception e) {
			System.out.println("网站异常请重新登录！");
			e.printStackTrace();
		}
	}

}
