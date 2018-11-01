import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class testlogin {

    public static void main(String[] args) throws Exception {

        org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();

        String loginUrl= "https://www.bsgjj.com/bswsyyt/index.jsp";
        HtmlPage page2 =  getHtml(loginUrl,webClient);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.waitForBackgroundJavaScript(20000);

        System.out.println(page2.asXml());

        HtmlImage image =page2.getFirstByXPath("//*[@id='tabs-1']/form/div[3]/div/span/img");

        String imageName = UUID.randomUUID() + ".jpg";
        File file = new File("/Users/manyizeng/work/img/"+imageName);
        image.saveAs(file);


        HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@name='certinum']");

        HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@id='perpwd']");
        HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@name='vericode']");
        HtmlElement button = (HtmlElement)page2.getFirstByXPath("//*[@id='tabs-1']/form/div[4]/div/button");

        System.out.println("username   :"+username.asXml());
        System.out.println("password   :"+password.asXml());
        System.out.println("button   :"+button.asXml());
        @SuppressWarnings("resource")
        /*Scanner scanner = new Scanner(System.in);
        String input = scanner.next();*/
        String input = JOptionPane.showInputDialog("请输入验证码……");
        code.setText(input);
        username.setText("533001198909260023");
        password.setText("1007842375wj");

        HtmlPage loggedPage = button.click();
        System.out.println("登陆成功后的页面  ====》》"+loggedPage.asXml());
        Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
        for(Cookie cookie:cookies){
            System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
        }

        String html=loggedPage.getWebResponse().getContentAsString();
        System.out.println("html"+html);

    }
    public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        webClient.setJavaScriptTimeout(500000);
        HtmlPage searchPage = webClient.getPage(webRequest);
        return searchPage;

    }
}
