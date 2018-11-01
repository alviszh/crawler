package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiInsuredInfo;
import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiMedical;
import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;

/**
 * @author zhangyongjie
 * @create 2017-09-22 15:36
 * @Desc
 */
@Component
public class InsuranceWuxiParser {

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private TracerLog tracer;

    /** 无锡社保登录页面URL */
    public static final String LOGIN_URL = "http://218.90.158.61/index.html";
    /** 无锡社保登录post请求获取cookies*/
    public static final String POST_URL="http://218.90.158.61/personloginvalidate.html";
    /** 无锡社保测试是否登录成功页面*/
    public static final String Login_Action = "http://218.90.158.61/person/personBaseInfo.html";

    /**
     * @Des 登录
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

        tracer.addTag("InsuranceWuxiParser.login", insuranceRequestParameters.getTaskId());
        // 登录日志
        tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
        WebParam webParam = new WebParam();
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        WebRequest webRequestLoginUrl = new WebRequest(new URL(LOGIN_URL), HttpMethod.GET);
        webRequestLoginUrl.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        webRequestLoginUrl.setAdditionalHeader("Connection", "keep-alive");
        webRequestLoginUrl.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webRequestLoginUrl.setAdditionalHeader("Host", "app.xmhrss.gov.cn");
        webRequestLoginUrl.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");

        HtmlPage searchPage = webClient.getPage(webRequestLoginUrl);
//        webClient.waitForBackgroundJavaScript(3000); //该方法在getPage()方法之后调用才能生效
        int status = searchPage.getWebResponse().getStatusCode();
        tracer.addTag("InsuranceWuxiParser.login.status",
                insuranceRequestParameters.getTaskId() + "---status:" + status);
        if (200 == status) {
            HtmlImage image = searchPage.getFirstByXPath("//form[@id='personloginform']//img[@id='f_svl']");
            // 超级鹰解析验证码
            String code = "";
            try {
                code = chaoJiYingOcrService.getVerifycode(image, "1902");
            } catch (Exception e) {
                tracer.addTag("ERROR:InsuranceWuxiParser.login.code",
                        insuranceRequestParameters.getTaskId() + "-----ERROR:" + e);
                e.printStackTrace();
            }
            tracer.addTag("InsuranceWuxiParser.login.code",
                    insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);

            // 发送POST请求登录
            WebRequest webRequest = new WebRequest(new URL(POST_URL),HttpMethod.POST);
            webRequest.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
            webRequest.setAdditionalHeader("Connection","keep-alive");
            webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
            webRequest.setAdditionalHeader("Host","app.xmhrss.gov.cn");
            webRequest.setAdditionalHeader("Origin","https://app.xmhrss.gov.cn");
            webRequest.setAdditionalHeader("Referer","https://app.xmhrss.gov.cn/login.xhtml?returnUrl=https://app.xmhrss.gov.cn:443/UCenter/logout.xhtml");
            webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
            webRequest.setAdditionalHeader("X-Requested-With","XMLHttpRequest");
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new NameValuePair("account",insuranceRequestParameters.getUsername()));
            nameValuePairs.add(new NameValuePair("password",insuranceRequestParameters.getPassword()));
            nameValuePairs.add(new NameValuePair("type","1"));
            nameValuePairs.add(new NameValuePair("captcha",code));
            webRequest.setRequestParameters(nameValuePairs);
            HtmlPage loginPage = webClient.getPage(webRequest);
            String response = loginPage.getWebResponse().getContentAsString();
            if(StringUtils.isNotBlank(response)){
                if("['success']".equals(response)){
                    WebRequest webRequest2 = new WebRequest(new URL(Login_Action),HttpMethod.GET);
                    //登陆后页面
                    HtmlPage loginActionPage = webClient.getPage(webRequest2);
                    String html = loginActionPage.getWebResponse().getContentAsString();
                    if(html.contains("个人编码")){
                        //success
                        webParam.setCode(searchPage.getWebResponse().getStatusCode());
                        webParam.setPage(searchPage);
                        return webParam;
                    }else {
                        return null;
                    }
                }else if("['wrongaccount']".equals(response)){
                    webParam.setCode(9999);
                    webParam.setPage(null);
                    return null;
                }else if("['wrongpass']".equals(response)){
                    webParam.setCode(9999);
                    webParam.setPage(null);
                    return null;
                }else if("['captchawrong']".equals(response)){
                    webParam.setCode(8888);
                    webParam.setPage(null);
                    return webParam;
                }
            }
            return null;
        }
        return null;
    }


    /**
     * 获取个人信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getUserInfo(TaskInsurance taskInsurance, String cookies) throws Exception {

        tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://218.90.158.61/person/personBaseInfo.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceWuxiParser.getUserinfo 个人信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                //判断是否打开了正确的页面
                if(html.contains("个人编码")){
                    InsuranceWuxiUserInfo insuranceWuxiUserInfo = userInfoHtmlParser(html, taskInsurance);
                    webParam.setInsuranceWuxiUserInfo(insuranceWuxiUserInfo);
                    webParam.setPage(page);
                    webParam.setHtml(html);
                    webParam.setUrl(page.getUrl().toString());
                    webParam.setCode(page.getWebResponse().getStatusCode());
                    return webParam;
                }else{
                    return null;
                }
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceWuxiParser.getUserInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 通过url获取 HtmlPage
     *
     * @param taskInsurance
     * @param cookies
     * @param url
     * @param type
     * @return
     * @throws Exception
     */
    public HtmlPage getHtmlPage(TaskInsurance taskInsurance, String cookies, String url, HttpMethod type)
            throws Exception {
        tracer.addTag("InsuranceWuxiParser.getHtmlPage---url:" + url + " ", taskInsurance.getTaskid());
        WebClient webClient = taskInsurance.getClient(cookies);
        WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
        HtmlPage searchPage = webClient.getPage(webRequest);
        int statusCode = searchPage.getWebResponse().getStatusCode();
        if (200 == statusCode) {
            String html = searchPage.getWebResponse().getContentAsString();
            tracer.addTag("InsuranceWuxiParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
                    "<xmp>" + html + "</xmp>");
            if (html.contains("无该功能的访问权限")) {
                return null;
            }
            return searchPage;
        }

        return null;
    }

    /**
     * 解析个人信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private InsuranceWuxiUserInfo userInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        InsuranceWuxiUserInfo insuranceWuxiUserInfo = new InsuranceWuxiUserInfo();
        Elements es = doc.select("li:contains(" + "个人编码" + ")");
        if (null != es && es.size() > 0) {
            Element element = es.first();
            Elements dataElements = element.parent().nextElementSibling().children();
            insuranceWuxiUserInfo.setPersonalCode(dataElements.get(0).text());
            insuranceWuxiUserInfo.setName(dataElements.get(1).text());
            insuranceWuxiUserInfo.setBirthday(dataElements.get(2).text());
            insuranceWuxiUserInfo.setSex(dataElements.get(3).text());
            insuranceWuxiUserInfo.setEducation(dataElements.get(4).text());
            insuranceWuxiUserInfo.setPersonalStatus(dataElements.get(5).text());
            insuranceWuxiUserInfo.setEmploymentType(dataElements.get(6).text());
            insuranceWuxiUserInfo.setBankCode(dataElements.get(7).text());
            insuranceWuxiUserInfo.setWorkDate(dataElements.get(8).text());
            insuranceWuxiUserInfo.setPersonalIdentity(dataElements.get(9).text());
            insuranceWuxiUserInfo.setTechnicalPosition(dataElements.get(10).text());
            insuranceWuxiUserInfo.setMilitaryPersonnel(dataElements.get(11).text());
            insuranceWuxiUserInfo.setDocumentType(dataElements.get(12).text());
            insuranceWuxiUserInfo.setCommunityId(dataElements.get(13).text());
            insuranceWuxiUserInfo.setContacts(dataElements.get(14).text());
            insuranceWuxiUserInfo.setInjuryMark(dataElements.get(15).text());
            insuranceWuxiUserInfo.setResettlementMark(dataElements.get(16).text());
            insuranceWuxiUserInfo.setPensionMonthlys(dataElements.get(17).text());
            insuranceWuxiUserInfo.setUnemploymentMonthlys(dataElements.get(18).text());
        }
        Elements es1 = doc.select("li:contains(" + "身份证号" + ")");
        if (null != es1 && es1.size() > 0) {
            Element element = es1.first();
            Elements dataElements = element.parent().nextElementSibling().children();
            insuranceWuxiUserInfo.setIdNumber(dataElements.get(0).text());
            insuranceWuxiUserInfo.setNation(dataElements.get(1).text());
            insuranceWuxiUserInfo.setTelphone(dataElements.get(2).text());
            insuranceWuxiUserInfo.setHouseholdType(dataElements.get(3).text());
            insuranceWuxiUserInfo.setPolitical(dataElements.get(4).text());
            insuranceWuxiUserInfo.setPersonnelCollectionType(dataElements.get(5).text());
            insuranceWuxiUserInfo.setMedicalPersonnelType(dataElements.get(6).text());
            insuranceWuxiUserInfo.setBankAccount(dataElements.get(7).text());
            insuranceWuxiUserInfo.setEstimateRetirementDate(dataElements.get(8).text());
            insuranceWuxiUserInfo.setAdministrativeDuty(dataElements.get(9).text());
            insuranceWuxiUserInfo.setWorkersTechnicalLevel(dataElements.get(10).text());
            insuranceWuxiUserInfo.setSpecialWorkFlag(dataElements.get(11).text());
            insuranceWuxiUserInfo.setSubordinateArea(dataElements.get(12).text());
            insuranceWuxiUserInfo.setStreetNumber(dataElements.get(13).text());
            insuranceWuxiUserInfo.setModelWorker(dataElements.get(14).text());
            insuranceWuxiUserInfo.setSpecialType(dataElements.get(15).text());
            insuranceWuxiUserInfo.setSpecialJobsMonthlys(dataElements.get(16).text());
            insuranceWuxiUserInfo.setMedicalMonthlys(dataElements.get(17).text());
            insuranceWuxiUserInfo.setMobilePhone(dataElements.get(18).text());
        }
        Elements es2 = doc.select("li:contains(" + "通信地址" + ")");
        if (null != es2 && es2.size() > 0) {
            Element element = es2.first();
            Elements dataElements = element.parent().nextElementSibling().children();
            insuranceWuxiUserInfo.setCommunicationAddress(dataElements.get(0).text());
            insuranceWuxiUserInfo.setRegisteredResidence(dataElements.get(1).text());
            insuranceWuxiUserInfo.setLivingAddress(dataElements.get(2).text());
            insuranceWuxiUserInfo.setMailingAddress(dataElements.get(3).text());
            insuranceWuxiUserInfo.setRemarks(dataElements.get(4).text());
        }
        insuranceWuxiUserInfo.setTaskid(taskInsurance.getTaskid());
        return insuranceWuxiUserInfo;
    }



    /**
     * 获取参保信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getInsuredInfo(TaskInsurance taskInsurance, String cookies,int currentPageNo) throws Exception {

        tracer.addTag("parser.crawler.getInsuredInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://218.90.158.61/person/personCBInfo.html?pagerMethod=&pageNo="+currentPageNo+"&currentPage=";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceWuxiParser.getInsuredInfo 参保信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                //判断是否打开了正确的页面
                if(html.contains("个人编码")){
                    List<InsuranceWuxiInsuredInfo> insuranceWuxiInsuredInfoList = insuredInfoHtmlParser(html, taskInsurance);
                    webParam.setList(insuranceWuxiInsuredInfoList);
                    webParam.setPage(page);
                    webParam.setHtml(html);
                    webParam.setUrl(page.getUrl().toString());
                    webParam.setCode(page.getWebResponse().getStatusCode());
                    return webParam;
                }else{
                    webParam.setList(null);
                    webParam.setPage(page);
                    webParam.setHtml(html);
                    webParam.setUrl(page.getUrl().toString());
                    webParam.setCode(page.getWebResponse().getStatusCode());
                    return webParam;
                }
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceWuxiParser.getInsuredInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析参保信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceWuxiInsuredInfo> insuredInfoHtmlParser(String html, TaskInsurance taskInsurance) {
        List<InsuranceWuxiInsuredInfo> insuranceWuxiInsuredInfoList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("dl:contains(" + "个人编码" + ")");
        Elements personalCodeElement = null;
        if (null != es && es.size() > 0) {
            Element element = es.first();
            personalCodeElement = element.children();
        }
        Elements es1 = doc.select("dl:contains(" + "姓名" + ")");
        Elements nameElement = null;
        if (null != es1 && es1.size() > 0) {
            Element element = es1.first();
            nameElement = element.children();
        }
        Elements es2 = doc.select("dl:contains(" + "身份证号" + ")");
        Elements idNumberElement = null;
        if (null != es2 && es2.size() > 0) {
            Element element = es2.first();
            idNumberElement = element.children();
        }
        Elements es3 = doc.select("dl:contains(" + "险种类型" + ")");
        Elements insuranceTypeElement = null;
        if (null != es3 && es3.size() > 0) {
            Element element = es3.first();
            insuranceTypeElement = element.children();
        }
        Elements es4 = doc.select("dl:contains(" + "参保状态" + ")");
        Elements insuredStatusElement = null;
        if (null != es4 && es4.size() > 0) {
            Element element = es4.first();
            insuredStatusElement = element.children();
        }
        Elements es5 = doc.select("dl:contains(" + "参保日期" + ")");
        Elements insuredDateElement = null;
        if (null != es5 && es5.size() > 0) {
            Element element = es5.first();
            insuredDateElement = element.children();
        }
        Elements es6 = doc.select("dl:contains(" + "单位编号" + ")");
        Elements orgNumberElement = null;
        if (null != es6 && es6.size() > 0) {
            Element element = es6.first();
            orgNumberElement = element.children();
        }
        Elements es7 = doc.select("dl:contains(" + "单位类型" + ")");
        Elements orgTypeElement = null;
        if (null != es7 && es7.size() > 0) {
            Element element = es7.first();
            orgTypeElement = element.children();
        }
        Elements es8 = doc.select("dl:contains(" + "单位名称" + ")");
        Elements orgNameElement = null;
        if (null != es8 && es8.size() > 0) {
            Element element = es8.first();
            orgNameElement = element.children();
        }
        Elements es9 = doc.select("dl:contains(" + "连续缴费月数" + ")");
        Elements continuityPaymentMonthsElement = null;
        if (null != es9 && es9.size() > 0) {
            Element element = es9.first();
            continuityPaymentMonthsElement = element.children();
        }

        for (int i = 1; i < personalCodeElement.size(); i++) {
            InsuranceWuxiInsuredInfo insuranceWuxiInsuredInfo = new InsuranceWuxiInsuredInfo();
            insuranceWuxiInsuredInfo.setTaskid(taskInsurance.getTaskid());
            insuranceWuxiInsuredInfo.setPersonalCode(personalCodeElement.get(i).text());
            insuranceWuxiInsuredInfo.setName(nameElement.get(i).text());
            insuranceWuxiInsuredInfo.setIdNumber(idNumberElement.get(i).text());
            insuranceWuxiInsuredInfo.setInsuranceType(insuranceTypeElement.get(i).text());
            insuranceWuxiInsuredInfo.setInsuredStatus(insuredStatusElement.get(i).text());
            insuranceWuxiInsuredInfo.setInsuredDate(insuredDateElement.get(i).text());
            insuranceWuxiInsuredInfo.setOrgNumber(orgNumberElement.get(i).text());
            insuranceWuxiInsuredInfo.setOrgType(orgTypeElement.get(i).text());
            insuranceWuxiInsuredInfo.setOrgName(orgNameElement.get(i).text());
            insuranceWuxiInsuredInfo.setContinuityPaymentMonths(continuityPaymentMonthsElement.get(i).text());
            insuranceWuxiInsuredInfoList.add(insuranceWuxiInsuredInfo);
        }

        return insuranceWuxiInsuredInfoList;
    }


    /**
     * 获取医疗保险信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getMedical(TaskInsurance taskInsurance, String cookies,int currentPageNo) throws Exception {

        tracer.addTag("parser.crawler.getMedical", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://218.90.158.61/person/personMedCountInfo.html?pagerMethod=&pageNo="+currentPageNo+"&currentPage=";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceWuxiParser.getMedical 医疗保险信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                //判断是否打开了正确的页面
                if(html.contains("个人编号")){
                    List<InsuranceWuxiMedical> insuranceWuxiMedicalList = medicalHtmlParser(html, taskInsurance);
                    webParam.setList(insuranceWuxiMedicalList);
                    webParam.setPage(page);
                    webParam.setHtml(html);
                    webParam.setUrl(page.getUrl().toString());
                    webParam.setCode(page.getWebResponse().getStatusCode());
                    return webParam;
                }else{
                    webParam.setList(null);
                    webParam.setPage(page);
                    webParam.setHtml(html);
                    webParam.setUrl(page.getUrl().toString());
                    webParam.setCode(page.getWebResponse().getStatusCode());
                    return webParam;
                }
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceWuxiParser.getMedical---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析医疗保险信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceWuxiMedical> medicalHtmlParser(String html, TaskInsurance taskInsurance) {
        List<InsuranceWuxiMedical> insuranceWuxiMedicalList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements es = doc.select("dl:contains(" + "年度" + ")");
        Elements yearElement = null;
        if (null != es && es.size() > 0) {
            Element element = es.first();
            yearElement = element.children();
        }
        Elements es1 = doc.select("dl:contains(" + "上年结转金额" + ")");
        Elements amountFromLastYearElement = null;
        if (null != es1 && es1.size() > 0) {
            Element element = es1.first();
            amountFromLastYearElement = element.children();
        }
        Elements es2 = doc.select("dl:contains(" + "本年单位划拨" + ")");
        Elements unitAllocationTheYearElement = null;
        if (null != es2 && es2.size() > 0) {
            Element element = es2.first();
            unitAllocationTheYearElement = element.children();
        }
        Elements es3 = doc.select("dl:contains(" + "本年帐户利息" + ")");
        Elements interestTheYearElement = null;
        if (null != es3 && es3.size() > 0) {
            Element element = es3.first();
            interestTheYearElement = element.children();
        }
        Elements es4 = doc.select("dl:contains(" + "基本帐户余额" + ")");
        Elements baseAccountBalanceElement = null;
        if (null != es4 && es4.size() > 0) {
            Element element = es4.first();
            baseAccountBalanceElement = element.children();
        }
        Elements es5 = doc.select("dl:contains(" + "企补账户余额" + ")");
        Elements supplementaryAccountBalanceElement = null;
        if (null != es5 && es5.size() > 0) {
            Element element = es5.first();
            supplementaryAccountBalanceElement = element.children();
        }
        for (int i = 1; i < yearElement.size(); i++) {
            InsuranceWuxiMedical insuranceWuxiMedical = new InsuranceWuxiMedical();
            insuranceWuxiMedical.setTaskid(taskInsurance.getTaskid());
            insuranceWuxiMedical.setYear(yearElement.get(i).text());
            insuranceWuxiMedical.setAmountFromLastYear(amountFromLastYearElement.get(i).text());
            insuranceWuxiMedical.setUnitAllocationTheYear(unitAllocationTheYearElement.get(i).text());
            insuranceWuxiMedical.setInterestTheYear(interestTheYearElement.get(i).text());
            insuranceWuxiMedical.setBaseAccountBalance(baseAccountBalanceElement.get(i).text());
            insuranceWuxiMedical.setSupplementaryAccountBalance(supplementaryAccountBalanceElement.get(i).text());
            insuranceWuxiMedicalList.add(insuranceWuxiMedical);
        }

        return insuranceWuxiMedicalList;
    }

}
