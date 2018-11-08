package org.common.microservice.eureka.china.unicom;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class GJJTest {

	static String url = "http://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp";

	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {
		webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage htmlPage = getHtml(url, webClient);

		System.out.println(htmlPage.asXml());

		HtmlForm form = htmlPage.getFormByName("form1");

		HtmlInput nameInput = form.getInputByName("bh5");
		HtmlInput passInput = form.getInputByName("mm5");

		System.out.println("--------------------------------------");
		System.out.println(nameInput.asXml());

		System.out.println("--------------------------------------");
		System.out.println(passInput.asXml());

		nameInput.setValueAttribute("6217000010101968397");
		passInput.setValueAttribute("096571");

		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='sds']/img");
		ImageReader imageReader = valiCodeImg.getImageReader();
		BufferedImage bufferedImage = imageReader.read(0);

		JFrame f2 = new JFrame();
		JLabel l = new JLabel();
		l.setIcon(new ImageIcon(bufferedImage));
		f2.getContentPane().add(l);
		f2.setSize(100, 100);
		f2.setTitle("验证码");
		f2.setVisible(true);

		String valicodeStr = JOptionPane.showInputDialog("请输入验证码：");

		HtmlInput verfiyInput = form.getInputByName("gjjcxjjmyhpppp5");
	
		
		System.out.println("--------------------------------------");
		System.out.println(verfiyInput.asXml());
		verfiyInput.setValueAttribute(valicodeStr);

		HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='login_tab_0']/div/div[4]/input[1]");
		System.out.println(loginButton.asXml());
		htmlPage = loginButton.click();
		System.out.println(htmlPage.asXml());
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
