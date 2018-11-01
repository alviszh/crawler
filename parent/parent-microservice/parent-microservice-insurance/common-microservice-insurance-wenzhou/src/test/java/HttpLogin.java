import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

import com.gargoylesoftware.htmlunit.util.NameValuePair;


public class HttpLogin {


    public static void main(String[] args) throws Exception{
        test();
    }
    private static void test() throws Exception{
        String url = "http://dq12333.gov.cn/gerenshebao!getlist.parser";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        WebRequest webRequest;
        webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
        webRequest.setRequestParameters(new ArrayList<NameValuePair>());
//        webRequest.getRequestParameters().add(new NameValuePair("code", "1"));
//        webRequest.getRequestParameters().add(new NameValuePair("pid", "1"));
//        webRequest.getRequestParameters().add(new NameValuePair("shebaohao","232302198310127032"));
//        webRequest.getRequestParameters().add(new NameValuePair("name", "%E5%A7%9C%E6%88%90" ));
        webRequest.setAdditionalHeader("Host", "dq12333.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://dq12333.gov.cn/");
        webRequest.setRequestBody("code=1&pid=1&shebaohao=232302198310127032&name="+URLEncoder.encode("姜成"));
        HtmlPage userInfoPage = webClient.getPage(webRequest);
        System.out.println(userInfoPage.asText());
    }
}