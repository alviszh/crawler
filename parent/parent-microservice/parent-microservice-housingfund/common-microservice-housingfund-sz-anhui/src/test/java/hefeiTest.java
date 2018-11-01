import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */
public class hefeiTest {

    private static String baseUrl = "https://sso.ahzwfw.gov.cn/uccp-server/login";
    //	private static String baseUrl = "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx?credentNo=F38wrqoLFj/znMCuz29AJsWsRFD%2B8Tx5&bindPhone=rIc84ce0fhwMZ2QtcLCw7g==&name=wDeI%2BTSe0XOQwhjkqpU9VA==";
    private static WebClient webClient = null;

    public static void main(String[] args) throws Exception {
        webClient = WebCrawler.getInstance().getNewWebClient();
        HtmlPage htmlPage = (HtmlPage) getHtml(baseUrl, webClient);
//		System.out.println("=================================================");
//		System.out.println(htmlPage.asXml());

        Document doc = Jsoup.parse(htmlPage.asXml());

        String lt = doc.select("[name=lt]").attr("value");
        String execution = doc.select("[name=execution]").attr("value");
        String _eventId = doc.select("[name=_eventId]").attr("value");
        String loginType = doc.select("[name=loginType]").attr("value");
        String credentialType = doc.select("[name=credentialType]").attr("value");

        String platform = doc.select("[name=platform]").attr("value");
//		String ukeyType = doc.select("[name=ukeyType]").attr("value");
//		String ahcaukey = doc.select("[name=ahcaukey]").attr("value");
//		String ahcasign = doc.select("[name=ahcasign]").attr("value");
        String userType = doc.select("[name=userType]").attr("value");


        String url = "https://sso.ahzwfw.gov.cn/uccp-server/login";
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new NameValuePair("lt", lt));
        paramsList.add(new NameValuePair("execution", execution));
        paramsList.add(new NameValuePair("_eventId", _eventId));
        paramsList.add(new NameValuePair("platform", platform));
        paramsList.add(new NameValuePair("loginType", loginType));
        paramsList.add(new NameValuePair("credentialType", credentialType));
        paramsList.add(new NameValuePair("userType", userType));
        paramsList.add(new NameValuePair("username", "15077905701"));
        paramsList.add(new NameValuePair("password", "xiaomei821"));
//		paramsList.add(new NameValuePair("username", "zhaojuan0623"));
//		paramsList.add(new NameValuePair("password", "y15856785647"));
        paramsList.add(new NameValuePair("random", ""));

        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        webRequest.setRequestParameters(paramsList);

        webClient.setJavaScriptTimeout(500000);
        Page searchPage = webClient.getPage(webRequest);
        System.out.println("============");
        System.out.println(searchPage.getWebResponse().getContentAsString());

        if(searchPage.getWebResponse().getContentAsString().indexOf("用户名或密码不正确")!=-1){
            System.out.println("=========false=============");
        }else{
            System.out.println("=========true=============");
        }

        //跳转到合肥登录成功页面
        String urlHefei = "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx";
        WebRequest webRequestHefei = new WebRequest(new URL(urlHefei), HttpMethod.GET);
        Page page = webClient.getPage(webRequestHefei);
        System.out.println("============hefei============");
        String html = page.getWebResponse().getContentAsString();

		/*HtmlPage htmlPage1 = (HtmlPage)page;

		HtmlElement loginButton = (HtmlElement)  htmlPage1.getFirstByXPath("/*//*[@id='form2']/div[3]/table[2]/tbody/tr[2]/td/table/tbody/tr/td[2]/table[4]/tbody/tr[1]/td[1]/a/img");
		loginButton.click();
		Thread.sleep(5000);
		System.out.println("============hefei============");
		System.out.println(htmlPage1.asXml());
		WebClient webClient2 = htmlPage1.getWebClient();*/
//		webClient2.getOptions().setJavaScriptEnabled(false);

        String urlJieguo = "http://www.ahgjj.gov.cn"+splitData(html,"window.open('..","')");
//		String urlJieguo = "http://www.ahgjj.gov.cn/gjjcx/cx_fromsfzhm_sz.aspx?cx=20180426&sfzhm=F38wrqoLFj/znMCuz29AJsWsRFD%2B8Tx5";
        WebRequest webRequestJieguo = new WebRequest(new URL(urlJieguo), HttpMethod.GET);
        webRequestJieguo.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,**/*//*;q=0.8");
        webRequestJieguo.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        webRequestJieguo.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
        webRequestJieguo.setAdditionalHeader("Cache-Control", "max-age=0");
        webRequestJieguo.setAdditionalHeader("Connection", "keep-alive");
        webRequestJieguo.setAdditionalHeader("Host", "www.ahgjj.gov.cn");
        webRequestJieguo.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
        webRequestJieguo.setAdditionalHeader("Referer", "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx");
        webRequestJieguo.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.104 Safari/537.36");
        Page jieguoPage = webClient.getPage(webRequestJieguo);
        System.out.println("============hefei== jieguoPage ==========");
        System.out.println(jieguoPage.getWebResponse().getContentAsString());



//		HtmlSpan form = htmlPage.getFirstByXPath("//*[@id='person']");
//
//		HtmlInput nameInput = form.getFirstByXPath("//*[@id='perName']");
//		HtmlInput passInput = form.getFirstByXPath("//*[@id='perPsd']");
//
//		nameInput.setValueAttribute("15856785648");
//		passInput.setValueAttribute("y15856785648");
//
//
//		HtmlElement loginButton = (HtmlElement) form.getFirstByXPath("//*[@id='psdBtn']");
//		htmlPage = loginButton.click();
//		System.out.println("=================================================");
//		System.out.println(htmlPage.asXml());
    }


    public static Page getHtml(String url, WebClient webClient) throws Exception {
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

        webClient.setJavaScriptTimeout(500000);
        Page searchPage = webClient.getPage(webRequest);
        return searchPage;

    }

    public static String splitData(String str, String strStart, String strEnd) {
        int i = str.indexOf(strStart);
        int j = str.indexOf(strEnd, i);
        String tempStr=str.substring(i+strStart.length(), j);
        return tempStr;
    }

}
