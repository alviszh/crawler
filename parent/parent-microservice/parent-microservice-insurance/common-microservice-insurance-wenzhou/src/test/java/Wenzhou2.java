import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouInsuranceBasic;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouPensionPaywater;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.*;

@SuppressWarnings("all")
public class Wenzhou2 {

    public static void main(String[] args) throws Exception{
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
//        ChaoJiYingOcrService cjy = new ChaoJiYingOcrService();
        Long start = System.currentTimeMillis();
        String url = "https://puser.zjzwfw.gov.cn/sso/usp.do";
        WebClient wc = new WebClient(BrowserVersion.CHROME);
//        WebClient wc = WebCrawler.getInstance().getNewWebClient();
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.setJavaScriptTimeout(10000);//设置JS执行的超时时间
        wc.getOptions().setCssEnabled(false); //禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX

        // 等待JS驱动dom完成获得还原后的网页
//        wc.getCache().setMaxSize(10);
        wc.waitForBackgroundJavaScriptStartingBefore(10000);
        wc.waitForBackgroundJavaScript(10000);
        HtmlPage htmlPage = wc.getPage(webRequest);

        HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='loginname']");
        HtmlPasswordInput password = (HtmlPasswordInput)htmlPage.getFirstByXPath("//input[@id='loginpwd']");
        HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//input[@id='submit']");
        HtmlImage image = (HtmlImage)htmlPage.getFirstByXPath("//img[@id='captcha_img']");
        HtmlTextInput code = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='verifycode']");
        String imgUrl = "https://puser.zjzwfw.gov.cn/sso/usp.do?parser=verifyimg&type=1&rd=%27+Math.random()+%27&t=0.5131140503077416";
        Connection con = Jsoup.connect(imgUrl).header("Content-Type","image/jpeg");

        File f = new File("d:\\img\\img.jpg");
        if(image!=null && code != null) {
            if (f.exists())
                f.delete();

            Set<Cookie> setCookie = wc.getCookieManager().getCookies();
            Map<String,String> mapCookie = new HashMap<String,String>();
            for(Cookie coco:setCookie)
                mapCookie.put(coco.getName(),coco.getValue());
            Connection.Response response = con.cookies(mapCookie).ignoreContentType(true).execute();
            FileOutputStream out = (new FileOutputStream(f));
            out.write(response.bodyAsBytes());
            out.close();

            System.out.println("请输入验证码:");
            Scanner sc = new Scanner(System.in);
            String co = sc.nextLine();
            code.setText(co);
        }
        System.out.println("username   :"+username.asXml());
        System.out.println("password   :"+password.asXml());
        System.out.println("button   :"+loginButton.asXml());


        username.setText("412721198804173842");
        password.setText("g123456");

        HtmlPage page = loginButton.click();
        wc.waitForBackgroundJavaScript(10000);

        System.out.println("\n\n=========\n\n"+page.asText());
        if(page.asText().contains("社保查询")){
            System.out.println("登陆成功！！");
        }
        Long end = System.currentTimeMillis();
        System.out.println("登陆耗时"+(end-start));

        HtmlElement insuranceButton = (HtmlElement)page.getFirstByXPath("//div[@title='社保查询']");

        HtmlPage insuran = insuranceButton.click();
        System.out.println(insuran.getWebClient().getOptions().isJavaScriptEnabled());
//        String name = insuran.getElementsById("principalKey").get(0).getAttribute("value");

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        for(Cookie cookie:wc.getCookieManager().getCookies()) {
            webClient.getCookieManager().addCookie(cookie);
            System.out.println(cookie);
        }
//        getUserInfo(webClient );
//        getShebaoInfo(webClient);
        getyangLaoInfo(webClient);
//        getChengxiangYangLaoInfo(webClient);
//        geBirthUserinfo(webClient);
    }
    //基本养老
    private static void getyangLaoInfo(WebClient webClient) throws Exception{
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_grjbxx&_t="+System.currentTimeMillis();
        UnexpectedPage page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("养老信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac002"));
            basic.setTaskId("");
            basic.setInsuranceType("基本养老保险");
            System.out.println(basic);
        }
    }
    //城乡养老
    private static void getChengxiangYangLaoInfo(WebClient webClient) throws Exception{
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_cjylxx&_t="+System.currentTimeMillis();
        HtmlPage page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("城乡养老信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceStatus(bean.getString("aac008"));
            basic.setInsurancePayStatus(bean.getString("aac031"));
            basic.setTaskId("");
            basic.setInsuranceType("城乡养老保险");
            System.out.println(basic);
        }
    }


    private static void getJibenYanglaoFlow(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_yljf&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_=" + timeMilions + "&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("养老流水信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayBase(bean.getString("jfjs"));
            water.setPayCompany(bean.getString("aab004"));
            water.setPayMonth(bean.getString("aae002"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setTransferStatus(bean.getString("aae111"));
            System.out.println(water);
        }
    }


    private static void getJibenYiliaoInfo(WebClient webClient) throws Exception{
        System.out.println(System.currentTimeMillis());
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_ybxxcx&_t="+System.currentTimeMillis();
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("基本医疗信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setInsuranceNum(bean.getString("aac001"));
            basic.setUserName(bean.getString("aac003"));
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setWorkStatus(bean.getString("ryxz"));
            basic.setComment("{\"医疗类型\":\""+bean.getString("mdtype")+"\",\"门诊类型\":\""+bean.getString("dmtype") +"\"}");
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setCompanyNum(bean.getString("aab001"));
            basic.setTaskId("");
            basic.setInsuranceType("基本医疗保险");
            System.out.println(basic);
        }
    }
    private static void getChengxiangYanglaoFlow(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_yljf&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_=" + timeMilions + "&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("养老流水信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayBase(bean.getString("jfjs"));
            water.setPayCompany(bean.getString("aab004"));
            water.setPayMonth(bean.getString("aae002"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setTransferStatus(bean.getString("aae111"));
            System.out.println(water);
        }
    }


    private static void getChengxiangYiliaoUserinfo(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_cjybxx&_t" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("城乡医疗信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setUserName(bean.getString("aac003"));
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setInsuranceNum(bean.getString("aac001"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("城乡医疗保险");
            System.out.println(basic);
        }
    }
    private static void getGongshangUserinfo(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_gssyxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("工伤保险信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("idcard"));
            basic.setUserName(bean.getString("name"));
            basic.setInsuranceCompanyName(bean.getString("corp_name"));
            basic.setInsuranceStatus(bean.getString("indi_join_sta"));
            basic.setInsuranceType("工伤保险");
            System.out.println(basic);
        }
    }

    private static void geBirthUserinfo(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_shengyxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("生育保险信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("生育保险");
            System.out.println(basic);
        }
    }
    private static void geShiyeUserinfo(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_syjbxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("失业保险信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("失业保险");
            System.out.println(basic);
        }
    }
    private static void getJiguanYanglaoUserinfo(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/XXXLTgyYTktYzhiOGU2YmYzN2Zi?dataset=sbcx_sheng$sbcx_grjbxx_jg&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("失业保险信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("机关养老保险");
            System.out.println(basic);
        }
    }
    private static void getJiguanYanglaoJiaofei(WebClient webClient) throws Exception{
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/XXXLTgyYTktYzhiOGU2YmYzN2Zi?dataset=sbcx_sheng$sbcx_yljf_jg&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_t=" + timeMilions + "&_=" + timeMilions;
        Page page = webClient.getPage(url);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("失业保险信息--->>>" + s);
        JSONObject json = JSON.parseObject(s);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayMonth(bean.getString("aae002"));
            water.setPayBase(bean.getString("jfjs"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setPayType(bean.getString("aae140"));
            water.setInsuranceType(bean.getString("aae111"));
            water.setTaskId("");
            System.out.println(water);
        }
    }
    private static void getUserInfo(WebClient webClient ) throws Exception{
        String cityListUrl = "http://sbcx.pcyyhj.zjzwfw.gov.cn/lib/city.json";
        String cityUrl = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/local";
        String userUrl ="https://puser.zjzwfw.gov.cn/sso/usp.do?parser=user";
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        Page page = webClient.getPage(cityUrl);
        String s = page.getWebResponse().getContentAsString();
        System.out.println("\n获取城市==========>>>>\n" + s );

        JSONObject cityJson = JSON.parseObject(s);
        String areaStart = cityJson.getJSONObject("data").getString("areaStart");
        String areaEnd = cityJson.getJSONObject("data").getString("areaEnd");

        Page cityPage = webClient.getPage(cityListUrl);
        String citys = cityPage.getWebResponse().getContentAsString();
//        System.out.println("\n获取城市==========>>>>\n" + pageCity.getWebResponse().getContentAsString("UTF-8"));
        JSONArray cityList = JSON.parseObject(citys).getJSONArray("data");
        for(JSONObject area:cityList.toJavaList(JSONObject.class)){
            if(area.getString("value").equals(areaStart)){
                System.out.println(area.getString("text"));
                for(JSONObject ar:area.getJSONArray("children").toJavaList(JSONObject.class)){
                    if(ar.getString("value").equals(areaEnd)){
                        System.out.println(ar.getString("text"));
                    }
                }
            }
        }
        WebRequest userRequest = new WebRequest(new URL(userUrl), HttpMethod.GET);
        HtmlPage userPage = webClient.getPage(userRequest);
        String userpage = userPage.getWebResponse().getContentAsString();
//        System.out.println("userpage = " + userpage);
        Document doc = Jsoup.parse(userpage);
        getUser(doc);
    }
    private static void getUser(Document doc ){
        String phoneNum = doc.select("div.lanmu_sub1:contains(手机号码)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String nameAndIDNum = doc.select("div.lanmu_sub1:contains(实名认证)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
//        获取姓名及身份证
        String s = nameAndIDNum.substring(nameAndIDNum.indexOf("|")+1).trim();
        String s2 = s.substring(s.indexOf("    ")+4) ;
        String name = s2.substring(0,s2.indexOf("    "));
        String idNum = s2.substring(s2.indexOf("    ") + 4);
        String mailNum = doc.select("div.lanmu_sub1:contains(邮箱)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String gender = doc.select("div.lanmu_sub1:contains(性别)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String nation = doc.select("div.lanmu_sub1:contains(民族)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();
        String address = doc.select("div.lanmu_sub1:contains(居住地址)").first().nextElementSibling().getElementsByClass("lanmu_sub2_1").first().text();

        System.out.println("phoneNum = " + phoneNum);
        System.out.println("name = " + name);
        System.out.println("idNum = " + idNum);
        System.out.println("mailNum = " + mailNum);
        System.out.println("gender = " + gender);
        System.out.println("nation = " + nation);
        System.out.println("address = " + address);

    }
}
