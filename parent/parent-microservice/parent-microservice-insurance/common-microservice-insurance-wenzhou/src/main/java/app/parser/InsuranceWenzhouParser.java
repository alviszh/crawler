package app.parser;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouUserinfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

/**
 * 登陆，获取用户信息，获取流水信息
 * Created by Mu on 2017/9/25.
 */
@Component
@SuppressWarnings("all")
public class InsuranceWenzhouParser {
    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;

    /**
     * 个人用户登陆，获取登陆后页面
     * @param reqPamam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters reqParam) throws Exception{
        Long start = System.currentTimeMillis();
        String url = "https://puser.zjzwfw.gov.cn/sso/usp.do";
        WebParam webParam = new WebParam();
        WebClient webClient = getWebClient(null);
        //模拟访问请求
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        //根据模拟请求获取---登陆页面
        HtmlPage htmlPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(10000);
        int status = htmlPage.getWebResponse().getStatusCode();
        if(200 == status){
            HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='loginname']");
            HtmlPasswordInput password = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='loginpwd']");
            HtmlImage codeImage = (HtmlImage)htmlPage.getFirstByXPath("//input[@id='captcha_img']");
            HtmlTextInput codeInput = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='verifycode']");
            HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='submit']");
            if (loginButton == null) {
                tracer.addTag("InsuranceWenzhou-pensionParser.login",
                        reqParam.getTaskId() + "login button can not found : null");
                throw new Exception("login button can not found : null");
            }else{
                username.setText(reqParam.getUsername());
                password.setText(reqParam.getPassword());
                String code = "";
                if(codeImage != null && codeInput != null){
                    String imgUrl = "https://puser.zjzwfw.gov.cn/sso/usp.do?parser=verifyimg&type=1&rd=%27+Math.random()+%27&t=0.5131140503077416";
                    code = chaoJiYingOcrService.getVerifycode(imgUrl,webClient.getCookieManager().getCookies(), "1004");
                    codeInput.setText(code);
                }
//                System.out.println("username = " + username + "  password = "+password + " code = " + code);
                HtmlPage loginPage = loginButton.click();
                webClient.waitForBackgroundJavaScript(10000);
                String loginHtml = loginPage.getWebResponse().getContentAsString();
//                System.out.println("登陆页面\n"+loginPage.asText());
                if(loginPage.asText().contains("社保查询")){
//                    System.out.println("登陆成功！！");
                    Long end = System.currentTimeMillis();
                    System.out.println("登陆耗时"+(end-start));
                    //获取登陆成功后结果
                    HtmlElement insuranceButton = (HtmlElement)loginPage.getFirstByXPath("//div[@title='社保查询']");
                    HtmlPage insuran = insuranceButton.click();
                    String html = insuran.getWebResponse().getContentAsString();
                    String alertMsg = WebCrawler.getAlertMsg();
                    webParam.setAlertMsg(alertMsg);
                    webParam.setCode(insuran.getWebResponse().getStatusCode());
                    webParam.setUrl(url);
                    webParam.setPage(insuran);
                    webParam.setHtml(html);
                    webParam.setUserName(reqParam.getUsername());
                    webParam.setPassWord(reqParam.getPassword());
                    tracer.addTag("InsuranceWenzhou-Parser-login","<xmp>"+html+"</xmp>");
                    webParam.setCode(1001);
                    return webParam;
                }else if(null != loginHtml && (loginPage.asText().contains("用户名、密码是否正确？请重试"))){
                        webParam.setCode(1002);
                        return webParam;
                }else if(null != loginHtml && (loginPage.asText().contains("密码错误,请重新输入"))){
                    webParam.setCode(1003);
                    return webParam;
                }else if(null != loginHtml && ((loginPage.asText().contains("验证码不正确"))||code.length()>4)){
                    webParam.setCode(1004);
                    return webParam;
                }else{
                    webParam.setCode(1005);
                    return webParam;
                }
            }
        }

        return null;
    }
    /**
     * 法人用户登陆，获取登陆后页面
     * @param reqPamam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public WebParam legalPersionlogin(InsuranceRequestParameters reqParam) throws Exception{
        Long start = System.currentTimeMillis();
        String url = "https://oauth.zjzwfw.gov.cn/login.jsp";
        WebParam webParam = new WebParam();
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        //模拟访问请求
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        //根据模拟请求获取---登陆页面
        HtmlPage htmlPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(10000);
        int status = htmlPage.getWebResponse().getStatusCode();
        if(200 == status){
            HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='username']");
            HtmlPasswordInput password = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='password']");
            HtmlImage codeImage = (HtmlImage)htmlPage.getFirstByXPath("//input[@id='captcha_img']");
            HtmlTextInput codeInput = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='verifycode']");
            HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='commitBtn']");
            if (loginButton == null) {
                tracer.addTag("InsuranceWenzhou-pensionParser.login",
                        reqParam.getTaskId() + "login button can not found : null");
                throw new Exception("login button can not found : null");
            }else{
                username.setText(reqParam.getUsername());
                password.setText(reqParam.getPassword());
                String code = "";
                if(codeImage != null && codeInput != null){
                    String imgUrl = "https://puser.zjzwfw.gov.cn/sso/usp.do?parser=verifyimg&type=1&rd=%27+Math.random()+%27&t=0.5131140503077416";
                    code = chaoJiYingOcrService.getVerifycode(imgUrl,webClient.getCookieManager().getCookies(), "1004");
                    codeInput.setText(code);
                }
//                System.out.println("username = " + username + "  password = "+password + " code = " + code);
                HtmlPage loginPage = loginButton.click();
                webClient.waitForBackgroundJavaScript(10000);
                String loginHtml = loginPage.getWebResponse().getContentAsString();
                if(loginPage.asText().contains("社保查询")){
//                    System.out.println("登陆成功！！");
                    Long end = System.currentTimeMillis();
                    System.out.println("登陆耗时"+(end-start));
                    //获取登陆成功后结果
                    HtmlElement insuranceButton = (HtmlElement)loginPage.getFirstByXPath("//div[@title='社保查询']");
                    HtmlPage insuran = insuranceButton.click();
                    String html = insuran.getWebResponse().getContentAsString();
                    String alertMsg = WebCrawler.getAlertMsg();
                    webParam.setAlertMsg(alertMsg);
                    webParam.setCode(insuran.getWebResponse().getStatusCode());
                    webParam.setUrl(url);
                    webParam.setPage(insuran);
                    webParam.setHtml(html);
                    webParam.setUserName(reqParam.getUsername());
                    webParam.setPassWord(reqParam.getPassword());
                    tracer.addTag("InsuranceWenzhou-Parser-login","<xmp>"+html+"</xmp>");
                    webParam.setCode(1001);
                    return webParam;
                }else if(null != loginHtml && (loginPage.asText().contains("用户名、密码是否正确"))){
                    webParam.setCode(1002);
                    return webParam;
                }else if(null != loginHtml && (loginPage.asText().contains("密码错误,请重新输入"))){
                    webParam.setCode(1003);
                    return webParam;
                }else if(null != loginHtml && ((loginPage.asText().contains("验证码不正确"))||code.length()>4)){
                    webParam.setCode(1004);
                    return webParam;
                }else{
                    webParam.setCode(1005);
                    return webParam;
                }
            }
        }

        return null;
    }
    /**
     * 获取用户信息
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getUserInfo(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);

        String cityListUrl = "http://sbcx.pcyyhj.zjzwfw.gov.cn/lib/city.json";
        String cityUrl = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/local";
        String userUrl = "https://puser.zjzwfw.gov.cn/sso/usp.do?parser=user";
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        String allUrl = cityListUrl + "@@@@" + cityUrl + "@@@@" + userUrl;
        Page page = webClient.getPage(cityUrl);
        String s = page.getWebResponse().getContentAsString();
//        System.out.println("\n获取城市==========>>>>\n" + s );

        JSONObject cityJson = JSON.parseObject(s);
        String areaStart = cityJson.getJSONObject("data").getString("areaStart");
        String areaEnd = cityJson.getJSONObject("data").getString("areaEnd");

        Page cityPage = webClient.getPage(cityListUrl);
        String citys = cityPage.getWebResponse().getContentAsString();

        StringBuilder stb = new StringBuilder("");
        JSONArray cityList = JSON.parseObject(citys).getJSONArray("data");
        for(JSONObject area:cityList.toJavaList(JSONObject.class)){
            if(area.getString("value").equals(areaStart)){
                stb.append(area.getString("text"));
//                System.out.println(area.getString("text"));
                for(JSONObject ar:area.getJSONArray("children").toJavaList(JSONObject.class)){
                    if(ar.getString("value").equals(areaEnd)){
                        stb.append("--").append(ar.getString("text"));
//                        System.out.println(ar.getString("text"));
                    }
                }
            }
        }
        WebRequest userRequest = new WebRequest(new URL(userUrl), HttpMethod.GET);
        HtmlPage userPage = webClient.getPage(userRequest);
        String html = s + "@@@@" + cityPage + "@@@@"  + cityList ;
        if(userPage.getWebResponse().getStatusCode() == 200) {
            webParam.setCode(200);
            String userpage = userPage.getWebResponse().getContentAsString();
            html += ("@@@@"  + userpage);
            Document doc = Jsoup.parse(userpage);
            //获取用户信息
            InsuranceWenzhouUserinfo userInfo = getUser(doc,reqParam);
            userInfo.setInsuranceCity(stb.toString());
            webParam.setUserInfo(userInfo);
        }
        webParam.setPage(userPage);
        webParam.setUrl(allUrl);
        webParam.setHtml(html);
        return webParam;
    }

    private   InsuranceWenzhouUserinfo getUser(Document doc ,InsuranceRequestParameters reqParam){
        String phoneNum = doc.select("div.lanmu_sub1:contains(手机号码)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String nameAndIDNum = doc.select("div.lanmu_sub1:contains(实名认证)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
//        获取姓名及身份证
        String s = nameAndIDNum.substring(nameAndIDNum.indexOf("|")+1).trim();

        String s2 = s.substring(s.indexOf("    ")+4) ;
        String name = s2.substring(0,s2.indexOf("    "));
        String idNum = s2.substring(s2.indexOf("    ") + 4);
//        System.out.println("s---->>>>"+s+"\nname---->>>>"+name+"\nidNum---->>>>"+idNum);
        String mailNum = doc.select("div.lanmu_sub1:contains(邮箱)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String gender = doc.select("div.lanmu_sub1:contains(性别)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String nation = doc.select("div.lanmu_sub1:contains(民族)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String address = doc.select("div.lanmu_sub1:contains(居住地址)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();

        InsuranceWenzhouUserinfo userInfo = new InsuranceWenzhouUserinfo();
        userInfo.setGender(gender);
        userInfo.setIdnum(idNum);
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setEmail(mailNum);
        userInfo.setNation(nation);
        userInfo.setTaskId(reqParam.getTaskId());
//        System.out.println(userInfo);
        return userInfo;
    }


    public WebClient getWebClient(Set<Cookie> cookies) {

        WebClient  webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(15000); // 15->60
        webClient.waitForBackgroundJavaScript(10000); // 5s
        try{
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        }catch(Exception e){

        }

        webClient.getOptions().setUseInsecureSSL(true); //
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理


        if(cookies != null)
            for(Cookie cookie:cookies){
                webClient.getCookieManager().addCookie(cookie);
            }

        return webClient;
    }

}
