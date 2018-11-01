package test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class AppTest {
	public static void main(String[] args) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://115.231.121.147/";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);

			HtmlRadioButtonInput rbl = (HtmlRadioButtonInput) html.getFirstByXPath("//*[@id=\"rbl_2\"]");
			rbl.click();
			
			// 身份证号
			HtmlTextInput name = (HtmlTextInput) html.getFirstByXPath("//input[@id='name']");
			// 密码
			HtmlPasswordInput password = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='password']");
			// 查询按钮
			HtmlElement btn_ok = (HtmlElement) html.getFirstByXPath("//input[@id='btn_ok']");
			// 验证码输入框
			HtmlTextInput yzm2 = (HtmlTextInput) html.getFirstByXPath("//input[@id='yzm2']");
			// 图片
			HtmlButtonInput login_check_code = (HtmlButtonInput) html
					.getFirstByXPath("//input[@id='login_check_code']");

			String base = login_check_code + "";
			String[] split = base.split("value=");
			String[] split2 = split[1].split("\"");
			String[] split3 = split2[1].split("=");
			String[] split4 = split3[0].split("\\+");
			String left = split4[0].trim();
			String right = split4[1].trim();
			int intleft = Integer.parseInt(left);
			int intright = Integer.parseInt(right);
			int yzm = intleft + intright;

			name.setText("330902198911128721");
			password.setText("128721");
			yzm2.setText(yzm + "");

			Page click = btn_ok.click();
			String contentAsString = click.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
		} catch (Exception e) {
		}
	}
}
