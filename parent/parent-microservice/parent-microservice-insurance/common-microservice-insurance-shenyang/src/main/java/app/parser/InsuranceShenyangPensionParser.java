package app.parser;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangHtml;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentDetailsEachyear;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentPastYears;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangUserInfo;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/18.
 */
@Component
@SuppressWarnings("all")
public class InsuranceShenyangPensionParser {
    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;

    /**
     * 登陆，获取登陆后页面
     * @param reqPamam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters reqParam) throws Exception{
        tracer.qryKeyValue("taskid", reqParam.getTaskId());
        String url = "http://gs.sysb.gov.cn/sysb/";
        WebParam webParam = new WebParam();

        WebClient webClient = WebParam.getWebClient();
        //模拟访问请求
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        //添加消息头
        webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/");
        //根据模拟请求获取---登陆页面
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage htmlPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(10000);
        int status = htmlPage.getWebResponse().getStatusCode();

//        System.out.println("\n\nparser-login:"+htmlPage.getWebResponse().getContentAsString());
        if(200 == status){
            HtmlImage image = htmlPage.getFirstByXPath("//img[@class='img']");
            String code = chaoJiYingOcrService.getVerifycode(image,"1004");
            tracer.qryKeyValue("verifyCode ==>", "code");
            HtmlTextInput username = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='shbzh']");
            HtmlTextInput password = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='fjm']");
            HtmlTextInput verifyCode = (HtmlTextInput)htmlPage.getFirstByXPath("//input[@id='yzm']");
            HtmlElement loginButton = (HtmlElement)htmlPage.getFirstByXPath("//button[@type='submit']");
            if (loginButton == null) {
                tracer.addTag("InsuranceShenyang-pensionParser.login",
                        reqParam.getTaskId() + "login button can not found : null");
                throw new Exception("login button can not found : null");
            }else{
                username.setText(reqParam.getUsername());
                password.setText(reqParam.getPassword());
                verifyCode.setText(code);
                System.out.println("username="+username+"  password="+password+"  code="+code);
                HtmlPage loginPage = loginButton.click();
                webClient.waitForBackgroundJavaScript(10000);
                String html=loginPage.getWebResponse().getContentAsString();
                //获取登陆成功后结果
                String alertMsg = WebCrawler.getAlertMsg();
                webParam.setAlertMsg(alertMsg);
                webParam.setCode(loginPage.getWebResponse().getStatusCode());
                webParam.setUrl(url);
                webParam.setPage(loginPage);
                webParam.setHtml(html);
                webParam.setUserName(reqParam.getUsername());
                webParam.setPassWord(reqParam.getPassword());
//                System.out.println("parser-webParam:"+webParam);
                tracer.addTag("InsuranceShenyang-pensionParser","<xmp>"+html+"</xmp>");
                Document doc = Jsoup.parse(html);
                Element mainPage=doc.getElementById("ryjbxxkxm");
                if(null != mainPage){
                    webParam.setCode(1001);
//                    System.out.println("沈阳养老登陆成功");
                    return webParam;
                }else {
                    if(null != html && (loginPage.asText().contains("附加码不正确"))){
                        webParam.setCode(1002);
                        return webParam;
                    }else if(null != html && (loginPage.asText().contains("验证码不正确")||code.length()>4)){
                        webParam.setCode(1003);
                        return webParam;
                    }else if(null != html && loginPage.asText().contains("职工编号错误")){
                        webParam.setCode(1004);
                        return webParam;
                    }else {
                        webParam.setCode(1005);
                        return webParam;
                    }
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

    public WebParam getUserInfo(InsuranceShenyangHtml html, Set<Cookie> cookies) throws Exception {
        WebParam webParam= new WebParam();
//        String url = "http://gs.sysb.gov.cn/sysb/logingo";
//        WebClient webClient = insuranceService.getWebClient(cookies);
//        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//        //添加请求头
//        webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
//        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
//        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
//        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/logingo");
//        webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js
//        webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常
//        HtmlPage page = webClient.getPage(webRequest);
//
//        webClient.waitForBackgroundJavaScript(10000);
//        int statusCode = page.getWebResponse().getStatusCode();
        Document doc = Jsoup.parse(html.getHtml());
        Element mainPage=doc.getElementById("ryjbxxkxm");
        if(mainPage != null){

            tracer.addTag("getUserInfo 解析个人信息","<xmp>"+html+"</xmp>");

            String name = doc.select("caption#ryjbxxkxm").first().text().trim();
            String employeeId = insuranceService.getNextLabelByKeyword(doc,"职工编号");
            String companyId = insuranceService.getNextLabelByKeyword(doc,"单位编码");
            String companyName = insuranceService.getNextLabelByKeyword(doc,"单位名称");
            String birthDay = insuranceService.getNextLabelByKeyword(doc,"出生日期");
            String state = insuranceService.getNextLabelByKeyword(doc,"参保状态");

            InsuranceShenyangUserInfo userInfo = new InsuranceShenyangUserInfo();
            userInfo.setName(name);
            userInfo.setBirthDay(birthDay);
            userInfo.setCompanyId(companyId);
            userInfo.setCompanyName(companyName);
            userInfo.setEmployeeId(employeeId);
            userInfo.setInsuredState(state);
            userInfo.setTaskId(html.getTaskId());

            List<InsuranceShenyangPaymentPastYears> paymentPastYearsList = getPaymentPastYearsList(html,cookies);

            webParam.setUserInfo(userInfo);
            webParam.setPaymentPastYearsList(paymentPastYearsList);
            webParam.setCode(200);
            webParam.setHtml(html.getHtml());
//            System.out.println("webParam--------\n");
            return webParam;
        }
        return webParam;

    }

    public List<InsuranceShenyangPaymentPastYears> getPaymentPastYearsList(InsuranceShenyangHtml html,Set<Cookie> cookie){
        Document doc = Jsoup.parse(html.getHtml());
        Element table=doc.getElementById("ndjfmxklist");
        List<InsuranceShenyangPaymentPastYears> list = new ArrayList<InsuranceShenyangPaymentPastYears>();
        if(table != null){
            Elements rows = table.select("tr");
            if(rows != null){

                String taskId = html.getTaskId();
                for(Element row:rows){
                    Elements columns = row.select("td");

                    String employeeId = columns.get(0).text().trim();
                    String employeeName = columns.get(1).text().trim();
                    String accountYear = columns.get(2).text().trim();
                    String companyPayMonths = columns.get(3).text().trim();
                    String personalPayMonths = columns.get(4).text().trim();
                    String payBase = columns.get(5).text().trim();
                    String personalAcountMoney = columns.get(6).text().trim();

                    InsuranceShenyangPaymentPastYears bean = new InsuranceShenyangPaymentPastYears();
                    bean.setEmployeeId(employeeId);
                    bean.setEmployeeName(employeeName);
                    bean.setAccountYear(accountYear);
                    bean.setCompanyPayMonths(companyPayMonths);
                    bean.setPersonalPayMonths(personalPayMonths);
                    bean.setPayBase(payBase);
                    bean.setPersonalAcountmoney(personalAcountMoney);
                    bean.setTaskId(taskId);
                    bean.setInsuranceType("养老保险");
                    list.add(bean);

                }
            }
        }
//        System.out.println("detaillist----\n"+list);
        return list;
    }

    public WebParam getPaymentDetailsEachYearList(InsuranceShenyangPaymentPastYears ppy,TaskInsurance taskInsurance,Set<Cookie> cookies) throws Exception{
        WebParam webParam = new WebParam();
        List<InsuranceShenyangPaymentDetailsEachyear> list = new ArrayList<InsuranceShenyangPaymentDetailsEachyear>();
        String url = "http://gs.sysb.gov.cn/sysb/xx";
        WebClient webClient = insuranceService.getWebClient(cookies);
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        //添加请求头

        webRequest.setRequestParameters(new ArrayList<NameValuePair>());
        webRequest.getRequestParameters().add(new NameValuePair("year",ppy.getAccountYear()));
        webRequest.getRequestParameters().add(new NameValuePair("zgbh",ppy.getEmployeeId()));

        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Origin", "http://gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/logingo");
        webRequest.setAdditionalHeader("Accept", "*/*");
        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        Page page = webClient.getPage(webRequest);
        String str = page.getWebResponse().getContentAsString(Charset.forName("utf8"));
        int statusCode = page.getWebResponse().getStatusCode();
        if(statusCode == 200){
            JSONArray jsonArray = JSON.parseObject(str).getJSONArray("data");
            for(JSONObject jsonObject:jsonArray.toJavaList(JSONObject.class)){
                InsuranceShenyangPaymentDetailsEachyear pde = new InsuranceShenyangPaymentDetailsEachyear();
                String payMonth = jsonObject.getString("ny");
                String personalAccountMoney = jsonObject.getString("yje");
                String companyId = jsonObject.getString("dwbh");
                pde.setEmployeeId(ppy.getEmployeeId());
                pde.setCompanyId(companyId);
                pde.setEmployeeName(ppy.getEmployeeName());
                pde.setPayMonth(payMonth);
                pde.setPersonalAcountmoney(personalAccountMoney);
                pde.setTaskId(taskInsurance.getTaskid());
                pde.setInsuranceType("养老保险");
                list.add(pde);
            }
            webParam.setCode(200);
        }
        webParam.setUrl(url+"?year="+ppy.getAccountYear()+"&zgbh="+ppy.getEmployeeId());
        webParam.setHtml(str);
        if(page.isHtmlPage()){
            webParam.setPage((HtmlPage) page);
        }
        webParam.setPaymentDetailsEachyearList(list);

        return webParam;
    }
}
