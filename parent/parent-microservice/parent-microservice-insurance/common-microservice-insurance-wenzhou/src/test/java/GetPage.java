import app.service.ChaoJiYingOcrService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
@SuppressWarnings("all")
public class GetPage {

    public static void main(String[] args) throws Exception{
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
//        ChaoJiYingOcrService cjy = new ChaoJiYingOcrService();
        Long start = System.currentTimeMillis();
        String url = "https://puser.zjzwfw.gov.cn/sso/usp.do";
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.setJavaScriptTimeout(100000);//设置JS执行的超时时间
        wc.getOptions().setCssEnabled(false); //禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(0); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX
        // 等待JS驱动dom完成获得还原后的网页
        wc.getCache().setMaxSize(10);
        wc.waitForBackgroundJavaScriptStartingBefore(10000);
        wc.waitForBackgroundJavaScript(10000);
        HtmlPage htmlPage = wc.getPage(url);
//        int i=0;
//        while (true) {
//            if (htmlPage.asText().contains("个人用户登录")) {
//                break;
//            }
//            System.out.println("开始等待第:"+ ++i +"次爬取");
//            wc.getCache().clear();
//            htmlPage = wc.getPage(url);
//            synchronized (htmlPage) {
//                htmlPage.wait(5000);
//            }
//        }

        // 网页内容
//        System.out.println("\n\n--------------"+htmlPage.asXml()+"\n-------------\n");

        HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='loginname']");
        HtmlPasswordInput password = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='loginpwd']");
        HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='submit']");

        System.out.println("username   :"+username.asXml());
        System.out.println("password   :"+password.asXml());
        System.out.println("button   :"+loginButton.asXml());


        username.setText("412721198804173842");
        password.setText("g123456");

        HtmlPage page = loginButton.click();
        wc.waitForBackgroundJavaScript(10000);

//        System.out.println("\n\n=========\n\n"+page.asText());
        if(page.asText().contains("社保查询")){
            System.out.println("登陆成功！！");
        }
        Long end = System.currentTimeMillis();
        System.out.println("登陆耗时"+(end-start));
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        for(Cookie cookie:wc.getCookieManager().getCookies())
            webClient.getCookieManager().addCookie(cookie);
        HtmlElement insuranceButton = (HtmlElement)page.getFirstByXPath("//div[@title='社保查询']");
        HtmlPage insuran = insuranceButton.click();
        String name = insuran.getElementsById("principalKey").get(0).getAttribute("value");
        System.out.println("\n\nname === "+ name);
        getNext(webClient);
        TextPage json = webClient.getPage("http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/local");
        System.out.println("json---->>\n"+json.getContent());
//        HtmlPage pageJson = webClient.getPage("https://puser.zjzwfw.gov.cn/sso/usp.do?action=user");
//        HtmlPage pageJson = webClient.getPage("https://puser.zjzwfw.gov.cn/sso/usp.do?action=user");
//        HtmlPage pageJson = webClient.getPage("https://puser.zjzwfw.gov.cn/sso/usp.do?action=user");
        webClient.waitForBackgroundJavaScript(10000);
        System.out.println("\n=====\n====\n=====\n=====\n=====");
        System.out.println(insuran.getWebResponse().getContentAsString());


//        HtmlElement ele = page.getFirstByXPath("//div[@class='fastTrack_pic']");
//        HtmlPage pageNext = ele.click();
//        webClient.waitForBackgroundJavaScript(10000);
//        System.out.println("\n\n\n\n\n\n\n\n\n=========="+pageNext.asXml());
    }
    private static void getNext(WebClient webClient) throws Exception{
//        WebClient webClient = WebCrawler.getInstance().getNewWebClient();

        URL url  = new URL("http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/local");

        WebRequest  requestSettings = new WebRequest(url, HttpMethod.GET);

//        requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//        requestSettings.getRequestParameters().add(new NameValuePair("year", "2017"));
//        requestSettings.getRequestParameters().add(new NameValuePair("zgbh", "40451074"));

        requestSettings.setAdditionalHeader("Host", "zjzwfw.gov.cn");
        requestSettings.setAdditionalHeader("Origin", "http://sbcx.pcyyhj.zjzwfw.gov.cn");
//        requestSettings.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/logingo");
        requestSettings.setAdditionalHeader("Accept", "*/*");
        requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        Page page = webClient.getPage(requestSettings);

        String s = page.getWebResponse().getContentAsString();
        System.out.println("\n=========="+s+"\n===================");
//        JSONObject json = JSON.parseObject(s);
//        System.out.println(json.getJSONArray("data"));
//        System.out.println(json.getIntValue("size"));
//        JSONArray jsonArray = json.getJSONArray("data");
//        for(JSONObject js:jsonArray.toJavaList(JSONObject.class)){
//            System.out.println("jsonArray分解========="+js.getString("jfbz"));
//        }

    }
}
