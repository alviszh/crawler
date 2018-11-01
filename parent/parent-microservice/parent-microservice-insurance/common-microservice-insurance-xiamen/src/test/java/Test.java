import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.net.URL;

/**
 * Created by kaixu on 2017/9/26.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        WebRequest webRequest = new WebRequest(new URL("https://app.xmhrss.gov.cn/login.xhtml"), HttpMethod.GET);
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.setJavaScriptTimeout(10000);//设置JS执行的超时时间
        wc.getOptions().setCssEnabled(false); //禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX

// 等待JS驱动dom完成获得还原后的网页
// wc.getCache().setMaxSize(10);
        wc.waitForBackgroundJavaScriptStartingBefore(10000);
        wc.waitForBackgroundJavaScript(10000);
        HtmlPage searchPage = wc.getPage(webRequest);


    }
}
