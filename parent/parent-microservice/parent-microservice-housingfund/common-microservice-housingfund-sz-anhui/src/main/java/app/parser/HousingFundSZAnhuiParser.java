package app.parser;


import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiPay;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiUserInfo;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HousingFundSZAnhuiParser {
    @Autowired
    TracerLog tracer;

    public WebParam login(MessageLoginForHousing messageLoginForHousing) throws Exception{
//        String url = "http://www.ahgjj.gov.cn/include/SzLogin.aspx";
        String url = "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx?credentNo=F38wrqoLFj/znMCuz29AJsWsRFD%2B8Tx5&bindPhone=rIc84ce0fhwMZ2QtcLCw7g==&name=wDeI%2BTSe0XOQwhjkqpU9VA==";
        HtmlPage loginPage = null;
        WebParam webParam = new WebParam();
        int statusCode = 0;
        try {
            WebClient webClient = WebCrawler.getInstance().getNewWebClient();
            WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
            loginPage = webClient.getPage(webRequest);
            System.out.println("loginPage="+loginPage);
            statusCode = loginPage.getWebResponse().getStatusCode();

        } catch (Exception e) {
            tracer.addTag("HousingFundSZAnhuiParser===>login", messageLoginForHousing.getTask_id() + "登录页获取超时！" + e);
            webParam.setCode(statusCode);
            webParam.setPage(loginPage);
            webParam.setAlertMsg("连接超时");
            webParam.setUrl(url);
            return webParam;
        }
        if (200 == statusCode) {
            HtmlTextInput username = loginPage.getFirstByXPath("//input[@id='TextBox1']");
            HtmlPasswordInput password = loginPage.getFirstByXPath("//input[@id='TextBox2']");
            HtmlElement button = loginPage.getFirstByXPath("//input[@id='btnsumbit']");

            username.setText(messageLoginForHousing.getNum()); //登录账号
            password.setText(messageLoginForHousing.getPassword());//密码
            loginPage = button.click();

            String alertMsg = WebCrawler.getAlertMsg();
            webParam.setCode(loginPage.getWebResponse().getStatusCode());
            webParam.setPage(loginPage);
            webParam.setAlertMsg(alertMsg);
            webParam.setUrl(url);
            String cookies = CommonUnit.transcookieToJson(loginPage.getWebClient());
            webParam.setCookies(cookies);
            return webParam;
        }
        return null;
    }

    public WebParam<HousingSZAnhuiUserInfo> userInfoParser(TaskHousing taskHousing, String html) {
        WebParam<HousingSZAnhuiUserInfo> webParam=new WebParam<HousingSZAnhuiUserInfo>();
        Document doc = Jsoup.parse(html);

        HousingSZAnhuiUserInfo housingSZAnhuiUserInfo = new HousingSZAnhuiUserInfo();
        housingSZAnhuiUserInfo.setCompanyCode(doc.select("span#dwzhLabel").text());//单位代码
        housingSZAnhuiUserInfo.setCompanyName(doc.select("span#dwmcLabel").text());//单位名称
        housingSZAnhuiUserInfo.setCompanyBankNum(doc.select("span#dwyhzhLabel").text());//单位银行账号
        housingSZAnhuiUserInfo.setIdNum(doc.select("span#sfzhmLabel").text());//身份证号
        housingSZAnhuiUserInfo.setStartPayMonth(doc.select("span#qjrqLabel").text());//起缴月份
        housingSZAnhuiUserInfo.setPayableMonth(doc.select("span#dc_yjjeLabel").text());//月应缴额
        housingSZAnhuiUserInfo.setCompanyDepositRatio(doc.select("span#DwblLabel").text());//单位缴存比例
        housingSZAnhuiUserInfo.setIntrustBank(doc.select("span#SYhTypeLabel").text());//委托银行
        housingSZAnhuiUserInfo.setPerCode(doc.select("span#grzhLabel").text());//个人代码
        housingSZAnhuiUserInfo.setPerName(doc.select("span#grxmLabel").text());  //个人姓名
        housingSZAnhuiUserInfo.setPerBankNum(doc.select("span#SGrYhZh").text());  //个人银行账号
        housingSZAnhuiUserInfo.setBalance(doc.select("span#ljyeLabel").text()); //余额
        housingSZAnhuiUserInfo.setEndPayMonth(doc.select("span#DtJzrqLabel").text()); //缴至月份
        housingSZAnhuiUserInfo.setAccountStatus(doc.select("span#IFcbjLabel").text());   //账户状态
        housingSZAnhuiUserInfo.setPerDepositRatio(doc.select("span#GrblLabel").text());    //个人缴存比例
        housingSZAnhuiUserInfo.setTaskid(taskHousing.getTaskid());
        webParam.setHousingSZAnhuiUserInfo(housingSZAnhuiUserInfo);
        return webParam;
    }

    public List<HousingSZAnhuiPay> payDetailsParser(TaskHousing taskHousing, String html) {
        List<HousingSZAnhuiPay> housingSZAnhuiPayList = new ArrayList<HousingSZAnhuiPay>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table#GridViewZm");
        if (tables != null){
            Element table = tables.get(0);
            Elements trs = table.select("tbody").select("tr");
            if (trs != null) {
                for (int i = 1; i < trs.size(); i++) {
                    HousingSZAnhuiPay housingSZAnhuiPay = new HousingSZAnhuiPay();
                    housingSZAnhuiPay.setTaskid(taskHousing.getTaskid());
                    Elements tds=trs.get(i).select("td");
                    housingSZAnhuiPay.setDate(tds.get(0).text());
                    housingSZAnhuiPay.setSummary(tds.get(1).text());
                    housingSZAnhuiPay.setOutAccount(tds.get(2).text());
                    housingSZAnhuiPay.setInAccount(tds.get(3).text());
                    housingSZAnhuiPay.setBalance(tds.get(4).text());
                    housingSZAnhuiPayList.add(housingSZAnhuiPay);
                }
            }
        }
        return housingSZAnhuiPayList;
    }

}
