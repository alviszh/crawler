package app.crawler.telecomhtmlunit;

import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomYunNanCanShuAccidBean;
import app.bean.TelecomYunNanCanUserIdShuBean;
import app.unit.TeleComCommonUnit;

public class LognAndGetYunNanUnit {

	public static final Logger log = LoggerFactory.getLogger(LognAndGetYunNanUnit.class);

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static Page gethtmlPostByWebRequest(WebClient webClient, WebRequest webRequest, String url) {

		try {
			webClient.getOptions().setJavaScriptEnabled(false);
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public static TelecomYunNanCanUserIdShuBean getUserIdunit(MessageLogin messageLogin, TaskMobile taskMobile) {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

			String url = "http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=9A001&fastcode=01941226&cityCode=yn";
			Page page = TeleComCommonUnit.getHtml(url, webClient);

			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Elements screles = doc.select("script");
			for (Element screle : screles) {
				if (screle.toString().indexOf(messageLogin.getName().trim()) != -1) {
					String areacode = screle.toString().split(",")[1].replaceAll("[^0-9]", "");
					String weizhi = screle.toString().split(",")[2].replaceAll("[^0-9]", "");
					String weizhi1 = screle.toString().split(",")[3].replaceAll("[^0-9]", "");
					String weizhi2 = screle.toString().split(",")[4].replaceAll("[^0-9]", "");
					String userid = screle.toString().split(",")[5].replaceAll("[^0-9]", "");
					TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean = new TelecomYunNanCanUserIdShuBean();

					telecomYunNanCanUserIdShuBean.setAreacode(areacode);
					telecomYunNanCanUserIdShuBean.setWeizhi(weizhi);
					telecomYunNanCanUserIdShuBean.setWeizhi1(weizhi1);
					telecomYunNanCanUserIdShuBean.setWeizhi2(weizhi2);
					telecomYunNanCanUserIdShuBean.setUserid(userid);
					return telecomYunNanCanUserIdShuBean;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public static TelecomYunNanCanShuAccidBean getAccid(MessageLogin messageLogin, TaskMobile taskMobile)  {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
			String url = "http://yn.189.cn/service/jt/bill/qry_mainjt2.jsp?fastcode=01941227&cityCode=yn";

			Page page = TeleComCommonUnit.getHtml(url, webClient);
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Element screle = doc.select("select#qryNum").select("option").first();
			TelecomYunNanCanShuAccidBean telecomYunNanCanShuAccidBean = new TelecomYunNanCanShuAccidBean();

			telecomYunNanCanShuAccidBean.setProdid(screle.attr("prodid"));
			telecomYunNanCanShuAccidBean.setAccid(screle.attr("accid"));
			telecomYunNanCanShuAccidBean.setAeracode(screle.attr("aeracode"));
			telecomYunNanCanShuAccidBean.setProdtype(screle.attr("prodtype"));

			return telecomYunNanCanShuAccidBean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
	}


	/*
	 * public static void main(String[] args) throws Exception { HtmlPage
	 * htmlpage = login("18003658894", "211314"); htmlpage =
	 * getphonecode(htmlpage); MessageResult messageResult = new
	 * MessageResult(); JFrame f2 = new JFrame(); f2.setSize(100, 100);
	 * f2.setTitle("短信验证码"); f2.setVisible(true); String valicodeStr =
	 * JOptionPane.showInputDialog("请输入短信验证码："); f2.setVisible(false);
	 * messageResult.setCode(valicodeStr); htmlpage =
	 * setphonecode(messageResult); String html = getCallThemHtml(messageResult,
	 * "18003658894", "20175","2"); System.out.println(html); }
	 */

}
