package test;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class Hctest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        String url ="http://www.hczfgjj.com:8080/wt-web/logout";
		
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest1);
		
		HtmlTextInput num = page.getFirstByXPath("//*[@id='username']");
		HtmlPasswordInput password = page.getFirstByXPath("//*[@id='in_password']");
		HtmlTextInput code = page.getFirstByXPath("//*[@id='captcha']");
		HtmlImage valiCodeImg = page.getFirstByXPath("//*[@class='col-sm-3']/img");
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
		num.setText("452730198706260524");
		password.setText("351732");
		code.setText(valicodeStr);
		
		HtmlElement button = page.getFirstByXPath("//*[@class='col-sm-offset-3']/button");
		HtmlPage html = button.click();
		String url1 = "http://www.hczfgjj.com:8080/wt-web/person/jbxx?random=0.8492712360802399";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "www.hczfgjj.com:8080");
//		webRequest.setAdditionalHeader("Origin", "http://www.hczfgjj.com:8080");
//		webRequest.setAdditionalHeader("Referer", "http://www.hczfgjj.com:8080/wt-web/home?logintype=1");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		Page searchPage = webClient.getPage(webRequest);
		System.err.println(searchPage.getWebResponse().getContentAsString());
	}

}
