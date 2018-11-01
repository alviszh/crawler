package app.util;

import com.gargoylesoftware.htmlunit.*;

public class WebClientUtil {
    public static WebClient getNewWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(20000); // 15->60
        webClient.waitForBackgroundJavaScript(50000); // 5s
        try{
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        }catch(Exception e){
            e.printStackTrace();
        }

        webClient.getOptions().setUseInsecureSSL(true); //
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理

        return webClient;
    }

    public static void setAdditionalHeader(WebRequest webRequest, String cookie) {
        webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
        webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        if (cookie != null && cookie.trim().length() != 0) {
            webRequest.setAdditionalHeader("Cookie", cookie);
        }
    }

}
