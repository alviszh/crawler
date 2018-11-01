package org.common.microservice.housingfund.changchun;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageReader;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import app.service.common.LoginAndGetCommon;

public class changchuntest {

	public static void main(String[] args) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://www.cczfgjj.gov.cn/grlogin.jhtml";

		try {
			HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
			HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='guestbookCaptcha']");
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

			url = "http://www.cczfgjj.gov.cn/GJJQuery";
			webClient.addRequestHeader("Host", "www.cczfgjj.gov.cn");
			webClient.addRequestHeader("Origin", "http://www.cczfgjj.gov.cn");
			webClient.addRequestHeader("Referer", "http://www.cczfgjj.gov.cn/grlogin.jhtml");
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("tranCode", "142805".trim()));
			paramsList.add(new NameValuePair("task", "".trim()));
			//paramsList.add(new NameValuePair("accnum", "801142044167"));
			
			paramsList.add(new NameValuePair("accnum", ""));
			paramsList.add(new NameValuePair("certinum", "220102198908196169".trim()));
			paramsList.add(new NameValuePair("pwd", "196169".trim()));
			paramsList.add(new NameValuePair("verify", valicodeStr));
			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());

			Set<Cookie> cookies = webClient.getCookieManager().getCookies();

			Map<String, String> map = new HashMap<>();
			for (Cookie cookie : cookies) {
				System.out.println(cookie.toString());
				/*if (cookie.getName().indexOf("gjjaccnum") != -1) {
					map.put(cookie.getName(), cookie.getValue());
				}
				if (cookie.getName().indexOf("gjjcertinum") != -1) {
					map.put(cookie.getName(), cookie.getValue());
				}*/
			}

			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("tranCode", "112813"));
			paramsList.add(new NameValuePair("task", ""));
			paramsList.add(new NameValuePair("accnum",map.get("gjjaccnum").trim()));
			paramsList.add(new NameValuePair("certinum", map.get("gjjcertinum").trim()));

			page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());

			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("tranCode", "112814"));
			paramsList.add(new NameValuePair("task", "ftp"));
			paramsList.add(new NameValuePair("accnum",map.get("gjjaccnum").trim()));
			paramsList.add(new NameValuePair("begdate", "2015-10-13"));
			paramsList.add(new NameValuePair("enddate", "2017-10-13"));

			page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
