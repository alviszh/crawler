package test;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=4fc73e5bcd794b08889f39ad2b89acde&service=https://uc.ewoho.com/provinceCas/login?whService=aHR0cDovL3d3dy5ld29oby5jb20vcGVyc29uYWxjZW50ZXIvbG9hZGhvdXNlZnVuZC5kbw";
		// 调用下面的getHtml方法
		WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest1);
		String html = page.asXml();
		Document doc = Jsoup.parse(html);
		Element elementById = doc.getElementById("legpsdFm");
		String lt = elementById.getElementsByAttributeValue("name", "lt").val();
		System.out.println(lt);
		String execution = elementById.getElementsByAttributeValue("name", "execution").val();
		System.out.println(execution);
		String _eventId = elementById.getElementsByAttributeValue("name", "_eventId").val();
		System.out.println(_eventId);
		String platform = elementById.getElementsByAttributeValue("name", "platform").val();
		System.out.println(platform);
		String loginType = elementById.getElementsByAttributeValue("name", "loginType").val();
		System.out.println(loginType);
		
		// 调用下面的getHtml方法
		String url="https://sso.ahzwfw.gov.cn/uccp-server/login?appCode=4fc73e5bcd794b08889f39ad2b89acde&service=https://uc.ewoho.com/provinceCas/login?whService=aHR0cDovL3d3dy5ld29oby5jb20vcGVyc29uYWxjZW50ZXIvbG9hZGhvdXNlZnVuZC5kbw";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(new ArrayList<NameValuePair>());
		webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
		webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
		webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
		webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
		webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
		webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
		webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
		webRequest.getRequestParameters().add(new NameValuePair("username", "34020219931028142X"));
		webRequest.getRequestParameters().add(new NameValuePair("password", "xh123456"));
		webRequest.getRequestParameters().add(new NameValuePair("random", ""));
		Page loginPage = webClient.getPage(webRequest);
		String loginHtml=loginPage.getWebResponse().getContentAsString();
		System.out.println(loginHtml);
		
//		Page  housefund=getHtml("http://www.ewoho.com/personalcenter/loadhousefund.do",webClient);
//		System.out.println(housefund.getWebResponse().getContentAsString());
      
		Document doc2 = Jsoup.parse(loginHtml);
		Elements option = doc2.getElementsByTag("option");
		for (Element element : option) {
			String text = element.val();
			System.out.println(text);
		}
		Page  page5=getHtml("http://www.ewoho.com/personalcenter/getBaseInfoByUnit.do?perAccount=104252400200",webClient);
		System.out.println(page5.getWebResponse().getContentAsString());
		
		//{"currentMonth":"2017-06","creditorAmount":"721.74","userFoundAccount":{"ACCOUNT_BALANCE":"1441.74","ACCOUNT_STATE":"封存","IDCARD":"34020219931028142X","LASTYEAR_BALANCE":"1441.74","MONTHPAY":"0","NAME":"顾雨芹","OPEN_DATE":"2017-04-25 15:03:40","PAY_DATE":"2017-04-15 00:00:00","PAY_SCALE":"","PERSON_ACCOUNT":"00200","PERSON_MONTH_PAY":"0","SALARY":"3000","UNIT_ACCOUNT":"1042524","UNIT_MONTH_PAY":"0","UNIT_NAME":"芜湖恒天易开软件科技股份有限公司","WAGE_BASE":""},"collectFundLoanInfo":{"DKCS":"","DKD":"","DKJE":"","HKBJ":"","SFDK":"0"},"houseFund":{"ACCOUNTSTATE":"正常","ACCOUNT_BALANCE":"2883.74","CREDITORAMOUNT":"206","GENDER":"女","IDCARD":"34020219931028142X","LASTACCOUNTDATE":"2018-03","MONTHPAY":"206","NAME":"顾雨芹","PERSONACCOUNT":"01080","PERSONBANKACCOUNT":"","STARTPAYDATE":"2017-05-13","TOTALMONTH":"9","UNITACCOUNT":"1042925","UNITNAME":"安徽智易人力资源顾问有限公司芜湖分公司"},"year":2018,"month":3,"allLoanList":[{"accounting_date":"2017-06-30 00:00:00","accumulate_balance":"1441.74","creditor_amount":"1.74","debtor_amount":"0","summary":"结息"},{"accounting_date":"2017-06-23 00:00:00","accumulate_balance":"1440","creditor_amount":"720","debtor_amount":"0","summary":"2017-05[1]汇缴"},{"accounting_date":"2017-05-13 00:00:00","accumulate_balance":"720","creditor_amount":"720","debtor_amount":"0","summary":"2017-04[1]汇缴"}],"inLoanList":[{"accounting_date":"2017-06-30 00:00:00","accumulate_balance":"1441.74","creditor_amount":"1.74","debtor_amount":"0","summary":"结息"},{"accounting_date":"2017-06-23 00:00:00","accumulate_balance":"1440","creditor_amount":"720","debtor_amount":"0","summary":"2017-05[1]汇缴"},{"accounting_date":"2017-05-13 00:00:00","accumulate_balance":"720","creditor_amount":"720","debtor_amount":"0","summary":"2017-04[1]汇缴"}],"outLoanList":[]}

		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
