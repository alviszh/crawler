package app.parser;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.quanzhou.*;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangyongjie
 * @create 2017-09-19 14:06
 */
@Component
public class InsuranceQuanzhouParser {

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private TracerLog tracer;

    /**
     * @Des 登录
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

        tracer.addTag("InsuranceQuanzhouParser.login", insuranceRequestParameters.getTaskId());
        // 登录日志
        tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
        String loginUrl = "http://www.fj12333.gov.cn:268/fwpt/loginPage.html";
        WebParam webParam = new WebParam();
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//        webClient.getOptions().setJavaScriptEnabled(false);
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
        HtmlPage searchPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(30000);
        int status = searchPage.getWebResponse().getStatusCode();
        tracer.addTag("InsuranceQuanzhouParser.login.status",
                insuranceRequestParameters.getTaskId() + "---status:" + status);
        if (200 == status) {
            HtmlImage image = searchPage.getFirstByXPath("//form[@id='default']//img[@id='checkImg']");
            // 超级鹰解析验证码
            String code = "";
            try {
                code = chaoJiYingOcrService.getVerifycode(image, "1902");
            } catch (Exception e) {
                tracer.addTag("ERROR:InsuranceQuanzhouParser.login.code",
                        insuranceRequestParameters.getTaskId() + "-----ERROR:" + e);
                e.printStackTrace();
            }
            tracer.addTag("InsuranceQuanzhouParser.login.code",
                    insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);
            String selector_name = "form[id='default'] input[id='aac003']";
            String selector_username2 = "form[id='default'] input[id='aac002']";
            String selector_password2 = "form[id='default'] input[id='ysc002']";
            String selector_userjym2 = "form[id='default'] input[id='randCode']";
            //个人姓名
            HtmlTextInput inputName = (HtmlTextInput) searchPage.querySelector(selector_name);
            if (inputName == null) {
                tracer.addTag("InsuranceQuanzhouParser.login", insuranceRequestParameters.getTaskId()
                        + "name input text can not found :" + selector_name);
                throw new Exception("name input text can not found :" + selector_name);
            } else {
                inputName.reset();
                inputName.setText(insuranceRequestParameters.getName());
                tracer.addTag("InsuranceQuanzhouParser.login.getName", insuranceRequestParameters.getTaskId()
                        + "---getName:" + insuranceRequestParameters.getName());
            }
            //公民身份证号码
            HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector(selector_username2);
            if (inputUserName == null) {
                tracer.addTag("InsuranceQuanzhouParser.login", insuranceRequestParameters.getTaskId()
                        + "username input text can not found :" + selector_username2);
                throw new Exception("username input text can not found :" + selector_username2);
            } else {
                inputUserName.reset();
                inputUserName.setText(insuranceRequestParameters.getUsername());
                tracer.addTag("InsuranceQuanzhouParser.login.getUsername", insuranceRequestParameters.getTaskId()
                        + "---getUsername:" + insuranceRequestParameters.getUsername());
            }
            //密码
            HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector(selector_password2);
            if (inputPassword == null) {
                tracer.addTag("InsuranceQuanzhouParser.login",
                        insuranceRequestParameters.getTaskId() + "password input text can not found :" + inputPassword);
                throw new Exception("password input text can not found :" + selector_password2);
            } else {
                inputPassword.reset();
                inputPassword.setText(insuranceRequestParameters.getPassword());
                tracer.addTag("InsuranceQuanzhouParser.login.getPassword", insuranceRequestParameters.getTaskId()
                        + "---getPassword:" + insuranceRequestParameters.getPassword());
            }
            //验证码
            HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector(selector_userjym2);
            if (inputuserjym == null) {
                tracer.addTag("InsuranceQuanzhouParser.login",
                        insuranceRequestParameters.getTaskId() + "code input text can not found :" + selector_userjym2);
                throw new Exception("code input text can not found :" + selector_userjym2);
            } else {
                inputuserjym.reset();
                inputuserjym.setText(code);
            }
            // HtmlAnchor loginButton=
            // searchPage.getFirstByXPath("//a[@onclick='javascript:check_and_submit(document.login_form1);']");
            //登录按钮
            HtmlAnchor loginButton = searchPage.getFirstByXPath(
                    "//form[@id='default']//a[@id='loginimg']");
            if (loginButton == null) {
                tracer.addTag("InsuranceQuanzhouParser.login",
                        insuranceRequestParameters.getTaskId() + "login button can not found : null");
                throw new Exception("login button can not found : null");
            } else {
                try {
                    searchPage = loginButton.click();
                    webClient.waitForBackgroundJavaScript(30000);
                    tracer.addTag("InsuranceQuanzhouParser.login",
                            insuranceRequestParameters.getTaskId() + "<xmp>" + searchPage.asXml() + "</xmp>");
                } catch (Exception e) {
                    tracer.addTag("InsuranceQuanzhouParser.login.searchPage.asXml():ERROR" , insuranceRequestParameters.getTaskId()+"---ERROR:"+e);
                }
            }
            webParam.setCode(searchPage.getWebResponse().getStatusCode());
            webParam.setPage(searchPage);
            return webParam;
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
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/queryPersonInfo.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getUserinfo 个人信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                InsuranceQuanzhouUserInfo insuranceQuanzhouUserInfo = userInfoHtmlParser(html, taskInsurance);
                webParam.setInsuranceQuanzhouUserInfo(insuranceQuanzhouUserInfo);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getUserInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取企业职工基本养老保险
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpPension(TaskInsurance taskInsurance, String cookies) throws Exception {

        tracer.addTag("parser.crawler.getEmpPension", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/corpPersion.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getEmpPension 企业职工基本养老保险" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                Map<String,Object> params = empPensionHtmlParser(html);
                webParam.setParams(params);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpPension---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析企业职工基本养老保险所需参数
     *
     * @param html
     * @return
     */
    public Map<String,Object> empPensionHtmlParser(String html) {
        int start = html.indexOf("/fwpt/queryCorpPersionInsu.do?");
        html = html.substring(start);
        int end = html.indexOf("\\\"\" +");
        html = html.substring(30,end);
        String[] arr = html.split("=");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(arr[0],arr[1]);
        tracer.addTag("parser.crawler.getEmpPensionParams", params.toString());
        return params;
    }


    /**
     * 获取企业职工基本养老保险的参保信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpPensionInsuredInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getEmpPensionInsuredInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionInsu.do?aae140="+params.get("aae140");
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getEmpPensionInsuredInfo 企业职工基本养老保险的参保信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = pensionInsuredInfoHtmlParser(html, taskInsurance);
                webParam.setInsuranceQuanzhouInsuredInfo(insuranceQuanzhouInsuredInfo);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpPensionInsuredInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取企业职工基本养老保险的缴费信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpPensionPayInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getEmpPensionPayInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpPersionPerFund.do";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, url, HttpMethod.POST,params);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();

                tracer.addTag("InsuranceQuanzhouParser.getEmpPensionPayInfo 企业职工基本养老保险的缴费信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                List<InsuranceQuanzhouEmpPension> insuranceQuanzhouEmpPensionList = empPensionPayInfoHtmlParser(html, taskInsurance);
                webParam.setList(insuranceQuanzhouEmpPensionList);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpPensionPayInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
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
    public HtmlPage getHtmlPage(TaskInsurance taskInsurance, String cookies, String url, HttpMethod type,Map<String,Object> params)
            throws Exception {
        tracer.addTag("InsuranceQuanzhouParser.getHtmlPage---url:" + url + " ", taskInsurance.getTaskid());
        WebClient webClient = taskInsurance.getClient(cookies);
        WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
        if(HttpMethod.POST.equals(type)){
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new NameValuePair("aae140",params.get("aae140").toString()));
            nameValuePairs.add(new NameValuePair("aae041","197001"));
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
            String dateString = formatter.format(now);
            nameValuePairs.add(new NameValuePair("aae042",dateString));
            nameValuePairs.add(new NameValuePair("ylzps_tr_","true"));
            nameValuePairs.add(new NameValuePair("ylzps_p_","1"));
            nameValuePairs.add(new NameValuePair("ylzps_mr_","10000"));
//            nameValuePairs.add(new NameValuePair("ylzps_p_",params.get("ylzps_p_").toString()));
//            nameValuePairs.add(new NameValuePair("ylzps_mr_",params.get("ylzps_mr_").toString()));
            webRequest.setRequestParameters(nameValuePairs);
        }
        HtmlPage searchPage = webClient.getPage(webRequest);
        int statusCode = searchPage.getWebResponse().getStatusCode();
        if (200 == statusCode) {
            String html = searchPage.getWebResponse().getContentAsString();
            tracer.addTag("InsuranceQuanzhouParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
                    "<xmp>" + html + "</xmp>");
            if (html.contains("获取不到参保信息")||html.contains("查询不到缴费划拨信息")||html.contains("查无缴费记录")
                    ||html.contains("找不到工伤保险个人的缴费信息")||html.contains("查无缴费信息")) {
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
    private InsuranceQuanzhouUserInfo userInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        String name = getNextLabelByKeyword(doc, "姓名");
        String idNumber = getNextLabelByKeyword(doc, "身份证号");
        String address = getNextLabelByKeyword(doc, "通讯地址");
        String postal = getNextLabelByKeyword(doc, "邮政编码");
        String email = getNextLabelByKeyword(doc, "电子邮箱");
        String insuranceNumber = getNextLabelByKeyword(doc, "社保卡号");
        String currentStatus = getNextLabelByKeyword(doc, "当前状态");
        InsuranceQuanzhouUserInfo insuranceQuanzhouUserInfo = new InsuranceQuanzhouUserInfo(taskInsurance.getTaskid(),
                name,idNumber,address,postal,email,insuranceNumber,currentStatus);
        return insuranceQuanzhouUserInfo;
    }

    /**
     * 解析养老参保信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private InsuranceQuanzhouInsuredInfo pensionInsuredInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        String insuranceType = getNextLabelByKeyword(doc, "险种类型");
        String orgName = getNextLabelByKeyword(doc, "单位名称");
        String area = getNextLabelByKeyword(doc, "地区");
        String personnelNumber = getNextLabelByKeyword(doc, "人员编号");
        String insuredStatus = getNextLabelByKeyword(doc, "参保状态");
        String insuredDate = getNextLabelByKeyword(doc, "首次参保时间");
        String createAccountsTime = getNextLabelByKeyword(doc, "建立个人帐户年月");
        String paymentStatus = getNextLabelByKeyword(doc, "缴费状态");
        String workStatus = getNextLabelByKeyword(doc, "工作状态");
        String dataUpdateTime = getNextLabelByKeyword(doc, "数据更新时间");
        InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = new InsuranceQuanzhouInsuredInfo(taskInsurance.getTaskid(),insuranceType,
                orgName,area,personnelNumber,insuredStatus,insuredDate,createAccountsTime,paymentStatus,workStatus,dataUpdateTime);
        return insuranceQuanzhouInsuredInfo;
    }

    /**
     * 解析企业职工基本养老保险缴费信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceQuanzhouEmpPension> empPensionPayInfoHtmlParser(String html, TaskInsurance taskInsurance) {
        Document doc=Jsoup.parse(html);
        List<InsuranceQuanzhouEmpPension> empPensionList=new ArrayList<InsuranceQuanzhouEmpPension>();
        InsuranceQuanzhouEmpPension insuranceQuanzhouEmpPension=null;
        Elements elementsByTag = doc.getElementsByClass("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element element : elementsByTag) {
            insuranceQuanzhouEmpPension = new InsuranceQuanzhouEmpPension();
            insuranceQuanzhouEmpPension.setTaskid(taskInsurance.getTaskid());
//			建账年月
            insuranceQuanzhouEmpPension.setPrepareMonthly(element.getElementsByTag("td").get(0).text());
//			单位名称
            insuranceQuanzhouEmpPension.setOrgName(element.getElementsByTag("td").get(1).text());
//			账目类型
            insuranceQuanzhouEmpPension.setAccountType(element.getElementsByTag("td").get(2).text());
//			个人缴费金额
            insuranceQuanzhouEmpPension.setIndividualPaymentAmount(element.getElementsByTag("td").get(3).text());
//			单位缴费金额
            insuranceQuanzhouEmpPension.setUnitPaymentAmount(element.getElementsByTag("td").get(4).text());
//			划入账户金额
            insuranceQuanzhouEmpPension.setIncludedAccountAmount(element.getElementsByTag("td").get(5).text());
//			缴费基数
            insuranceQuanzhouEmpPension.setSocialInsuranceBase(element.getElementsByTag("td").get(6).text());
            empPensionList.add(insuranceQuanzhouEmpPension);
        }
        return empPensionList;
    }

    /**
     * @Des 获取目标标签的下一个兄弟标签的内容
     * @param document
     * @param keyword
     * @return
     */
    public static String getNextLabelByKeyword(Document document, String keyword) {
        Elements es = document.select("td:contains(" + keyword + ")");
        if (null != es && es.size() > 0) {
            if(es.size()==1){
                Element element = es.first();
                Element nextElement = element.nextElementSibling();
                if (null != nextElement) {
                    return nextElement.text();
                }
            }else{
                String value = "";
                for(Element element :es){
                    Element nextElement = element.nextElementSibling();
                    value = nextElement.text();
                    if(value!=null&&!value.contains("  ")){
                        return value;
                    }
                }
            }

        }
        return null;
    }


    /**
     * 获取城镇职工基本医疗保险
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpMedical(TaskInsurance taskInsurance, String cookies) throws Exception {

        tracer.addTag("parser.crawler.getEmpMedical", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/corpMedical.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getEmpMedical 城镇职工基本医疗保险" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                Map<String,Object> params = empMedicalHtmlParser(html);
                webParam.setParams(params);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpMedical---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析城镇职工基本医疗保险所需参数
     *
     * @param html
     * @return
     */
    public Map<String,Object> empMedicalHtmlParser(String html) {
        int start = html.indexOf("/fwpt/queryCorpMedicalInsu.do?");
        html = html.substring(start);
        int end = html.indexOf("\\\"\" +");
        html = html.substring(30,end);
        String[] arr = html.split("=");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(arr[0],arr[1]);
        tracer.addTag("parser.crawler.empMedicalHtmlParser", params.toString());
        return params;
    }


    /**
     * 获取城镇职工基本医疗保险的参保信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpMedicalInsuredInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getEmpMedicalInsuredInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/queryCorpMedicalInsu.do?aae140="+params.get("aae140");
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getEmpMedicalInsuredInfo 城镇职工基本医疗保险的参保信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = medicalInsuredInfoHtmlParser(html, taskInsurance);
                webParam.setInsuranceQuanzhouInsuredInfo(insuranceQuanzhouInsuredInfo);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpMedicalInsuredInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析医疗参保信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private InsuranceQuanzhouInsuredInfo medicalInsuredInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        String insuranceType = "城镇职工医疗保险";
        String area = getNextLabelByKeyword(doc, "地区");
        String orgName = getNextLabelByKeyword(doc, "单位名称");
        String insuredDate = getNextLabelByKeyword(doc, "参保日期");
        String annualPaymentBase = getNextLabelByKeyword(doc, "年度缴费基数");
        String workStatus = getNextLabelByKeyword(doc, "工作状态");
        String dataUpdateTime = getNextLabelByKeyword(doc, "数据更新时间");
        InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = new InsuranceQuanzhouInsuredInfo(taskInsurance.getTaskid(),insuranceType, orgName, area, insuredDate, annualPaymentBase, workStatus, dataUpdateTime);
        return insuranceQuanzhouInsuredInfo;
    }


    /**
     * 获取城镇职工基本医疗保险的缴费信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getEmpMedicalPayInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getEmpMedicalPayInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String url = "http://www.fj12333.gov.cn:268/fwpt/queryCorpMedicalPerFund.do";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, url, HttpMethod.POST,params);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();

                tracer.addTag("InsuranceQuanzhouParser.getEmpMedicalPayInfo 城镇职工基本医疗保险的缴费信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                List<InsuranceQuanzhouEmpMedical> insuranceQuanzhouEmpMedicalList = empMedicalPayInfoHtmlParser(html, taskInsurance);
                webParam.setList(insuranceQuanzhouEmpMedicalList);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getEmpMedicalPayInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析城镇职工基本医疗保险缴费信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceQuanzhouEmpMedical> empMedicalPayInfoHtmlParser(String html, TaskInsurance taskInsurance) {
        Document doc=Jsoup.parse(html);
        List<InsuranceQuanzhouEmpMedical> empMedicalList=new ArrayList<InsuranceQuanzhouEmpMedical>();
        InsuranceQuanzhouEmpMedical insuranceQuanzhouEmpMedical=null;
        Elements elementsByTag = doc.getElementsByClass("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element element : elementsByTag) {
            insuranceQuanzhouEmpMedical = new InsuranceQuanzhouEmpMedical();
            insuranceQuanzhouEmpMedical.setTaskid(taskInsurance.getTaskid());
//			建账年月
            insuranceQuanzhouEmpMedical.setPrepareMonthly(element.getElementsByTag("td").get(0).text());
//			账目类型
            insuranceQuanzhouEmpMedical.setAccountType(element.getElementsByTag("td").get(1).text());
//			个人缴费金额
            insuranceQuanzhouEmpMedical.setIndividualPaymentAmount(element.getElementsByTag("td").get(2).text());
//			单位缴费金额
            insuranceQuanzhouEmpMedical.setUnitPaymentAmount(element.getElementsByTag("td").get(3).text());
//			划入账户金额
            insuranceQuanzhouEmpMedical.setIncludedAccountAmount(element.getElementsByTag("td").get(4).text());
//			缴费基数
            insuranceQuanzhouEmpMedical.setSocialInsuranceBase(element.getElementsByTag("td").get(5).text());
            empMedicalList.add(insuranceQuanzhouEmpMedical);
        }
        return empMedicalList;
    }


    /**
     * 获取工伤保险
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getInjury(TaskInsurance taskInsurance, String cookies) throws Exception {

        tracer.addTag("parser.crawler.getInjury", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/injury.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getInjury 工伤保险" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                Map<String,Object> params = injuryHtmlParser(html);
                webParam.setParams(params);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getInjury---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析工伤保险所需参数
     *
     * @param html
     * @return
     */
    public Map<String,Object> injuryHtmlParser(String html) {
        int start = html.indexOf("/fwpt/queryInjuryInsu.do?");
        html = html.substring(start);
        int end = html.indexOf("\\\"\" +");
        html = html.substring(25,end);
        String[] arr = html.split("=");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(arr[0],arr[1]);
        tracer.addTag("parser.crawler.injuryHtmlParser", params.toString());
        return params;
    }

    /**
     * 获取工伤保险的参保信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getInjuryInsuredInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getInjuryInsuredInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/queryInjuryInsu.do?aae140="+params.get("aae140");
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getInjuryInsuredInfo 工伤保险的参保信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = injuryInsuredInfoHtmlParser(html, taskInsurance);
                webParam.setInsuranceQuanzhouInsuredInfo(insuranceQuanzhouInsuredInfo);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getInjuryInsuredInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析工伤保险参保信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private InsuranceQuanzhouInsuredInfo injuryInsuredInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        String insuranceType = "工伤保险";
        String area = getNextLabelByKeyword(doc, "地区");
        String orgName = getNextLabelByKeyword(doc, "单位名称");
        String name = getNextLabelByKeyword(doc, "姓名");
        String insuredStatus = getNextLabelByKeyword(doc, "参保状态");
        String idNumber = getNextLabelByKeyword(doc, "身份证号");
        String workStatus = getNextLabelByKeyword(doc, "工作状态");
        String annualPaymentBase = getNextLabelByKeyword(doc, "缴费基数");
        String insuredDate = getNextLabelByKeyword(doc, "参保日期");
        String dataUpdateTime = getNextLabelByKeyword(doc, "数据更新时间");
        InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = new InsuranceQuanzhouInsuredInfo();
        insuranceQuanzhouInsuredInfo.setTaskid(taskInsurance.getTaskid());
        insuranceQuanzhouInsuredInfo.setInsuranceType(insuranceType);
        insuranceQuanzhouInsuredInfo.setArea(area);
        insuranceQuanzhouInsuredInfo.setOrgName(orgName);
        insuranceQuanzhouInsuredInfo.setName(name);
        insuranceQuanzhouInsuredInfo.setInsuredStatus(insuredStatus);
        insuranceQuanzhouInsuredInfo.setIdNumber(idNumber);
        insuranceQuanzhouInsuredInfo.setWorkStatus(workStatus);
        insuranceQuanzhouInsuredInfo.setAnnualPaymentBase(annualPaymentBase);
        insuranceQuanzhouInsuredInfo.setInsuredDate(insuredDate);
        insuranceQuanzhouInsuredInfo.setDataUpdateTime(dataUpdateTime);
        return insuranceQuanzhouInsuredInfo;
    }


    /**
     * 获取工伤保险的缴费信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getInjuryPayInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getInjuryPayInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String url = "http://www.fj12333.gov.cn:268/fwpt/queryPerFund.do";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, url, HttpMethod.POST,params);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();

                tracer.addTag("InsuranceQuanzhouParser.getInjuryPayInfo 工伤保险的缴费信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                List<InsuranceQuanzhouInjury> insuranceQuanzhouInjuryList = injuryPayInfoHtmlParser(html, taskInsurance);
                webParam.setList(insuranceQuanzhouInjuryList);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getInjuryPayInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析工伤保险缴费信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceQuanzhouInjury> injuryPayInfoHtmlParser(String html, TaskInsurance taskInsurance) {
        Document doc=Jsoup.parse(html);
        List<InsuranceQuanzhouInjury> injuryList=new ArrayList<InsuranceQuanzhouInjury>();
        InsuranceQuanzhouInjury insuranceQuanzhouInjury=null;
        Elements elementsByTag = doc.getElementsByClass("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element element : elementsByTag) {
            insuranceQuanzhouInjury = new InsuranceQuanzhouInjury();
            insuranceQuanzhouInjury.setTaskid(taskInsurance.getTaskid());
//			建账年月
            insuranceQuanzhouInjury.setPrepareMonthly(element.getElementsByTag("td").get(0).text());
//			单位缴费金额
            insuranceQuanzhouInjury.setUnitPaymentAmount(element.getElementsByTag("td").get(1).text());
 //			缴费基数
            insuranceQuanzhouInjury.setSocialInsuranceBase(element.getElementsByTag("td").get(2).text());
//			单位缴费比例
            insuranceQuanzhouInjury.setOrgPayRatio(element.getElementsByTag("td").get(3).text());
            injuryList.add(insuranceQuanzhouInjury);
        }
        return injuryList;
    }

    /**
     * 获取生育保险
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getBirth(TaskInsurance taskInsurance, String cookies) throws Exception {

        tracer.addTag("parser.crawler.getBirth", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/birth.html";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getBirth 生育保险" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                Map<String,Object> params = birthHtmlParser(html);
                webParam.setParams(params);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getBirth---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析生育保险所需参数
     *
     * @param html
     * @return
     */
    public Map<String,Object> birthHtmlParser(String html) {
        int start = html.indexOf("/fwpt/queryBirInsu.do?");
        html = html.substring(start);
        int end = html.indexOf("\\\"\" +");
        html = html.substring(22,end);
        String[] arr = html.split("=");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put(arr[0],arr[1]);
        tracer.addTag("parser.crawler.birthHtmlParser", params.toString());
        return params;
    }


    /**
     * 获取生育保险的参保信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getBirthInsuredInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getBirthInsuredInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String urlData = "http://www.fj12333.gov.cn:268/fwpt/queryBirInsu.do?aae140="+params.get("aae140");
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null,new HashMap<String,Object>());
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceQuanzhouParser.getBirthInsuredInfo 生育保险的参保信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = birthInsuredInfoHtmlParser(html, taskInsurance);
                webParam.setInsuranceQuanzhouInsuredInfo(insuranceQuanzhouInsuredInfo);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getBirthInsuredInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析生育保险参保信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private InsuranceQuanzhouInsuredInfo birthInsuredInfoHtmlParser(String html, TaskInsurance taskInsurance) {

        Document doc = Jsoup.parse(html);
        String insuranceType = "生育保险";
        String area = getNextLabelByKeyword(doc, "地区");
        String orgName = getNextLabelByKeyword(doc, "单位名称");
        String insuredStatus = getNextLabelByKeyword(doc, "参保状态");
        String annualPaymentBase = getNextLabelByKeyword(doc, "缴费基数");
        String workStatus = getNextLabelByKeyword(doc, "工作状态");
        String dataUpdateTime = getNextLabelByKeyword(doc, "数据更新时间");
        InsuranceQuanzhouInsuredInfo insuranceQuanzhouInsuredInfo = new InsuranceQuanzhouInsuredInfo();
        insuranceQuanzhouInsuredInfo.setTaskid(taskInsurance.getTaskid());
        insuranceQuanzhouInsuredInfo.setInsuranceType(insuranceType);
        insuranceQuanzhouInsuredInfo.setArea(area);
        insuranceQuanzhouInsuredInfo.setOrgName(orgName);
        insuranceQuanzhouInsuredInfo.setInsuredStatus(insuredStatus);
        insuranceQuanzhouInsuredInfo.setAnnualPaymentBase(annualPaymentBase);
        insuranceQuanzhouInsuredInfo.setWorkStatus(workStatus);
        insuranceQuanzhouInsuredInfo.setDataUpdateTime(dataUpdateTime);
        return insuranceQuanzhouInsuredInfo;
    }


    /**
     * 获取生育保险的缴费信息
     *
     * @param taskInsurance
     * @param cookies
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public WebParam getBirthPayInfo(TaskInsurance taskInsurance, String cookies,Map<String,Object> params) throws Exception {

        tracer.addTag("parser.crawler.getBirthPayInfo", taskInsurance.getTaskid());
        WebParam webParam = new WebParam();
        String url = "http://www.fj12333.gov.cn:268/fwpt/queryBirthPerFund.do";
        try {
            HtmlPage page = getHtmlPage(taskInsurance, cookies, url, HttpMethod.POST,params);
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();

                tracer.addTag("InsuranceQuanzhouParser.getBirthPayInfo 生育保险的缴费信息" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                List<InsuranceQuanzhouBirth> insuranceQuanzhouBirthList = birthPayInfoHtmlParser(html, taskInsurance);
                webParam.setList(insuranceQuanzhouBirthList);
                webParam.setPage(page);
                webParam.setHtml(html);
                webParam.setUrl(page.getUrl().toString());
                webParam.setCode(page.getWebResponse().getStatusCode());
                return webParam;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceQuanzhouParser.getBirthPayInfo---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析生育保险缴费信息
     *
     * @param html
     * @param taskInsurance
     * @return
     */
    private List<InsuranceQuanzhouBirth> birthPayInfoHtmlParser(String html, TaskInsurance taskInsurance) {
        Document doc=Jsoup.parse(html);
        List<InsuranceQuanzhouBirth> injuryList=new ArrayList<InsuranceQuanzhouBirth>();
        InsuranceQuanzhouBirth insuranceQuanzhouBirth=null;
        Elements elementsByTag = doc.getElementsByClass("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
        for (Element element : elementsByTag) {
            insuranceQuanzhouBirth = new InsuranceQuanzhouBirth();
            insuranceQuanzhouBirth.setTaskid(taskInsurance.getTaskid());
//			建账年月
            insuranceQuanzhouBirth.setPrepareMonthly(element.getElementsByTag("td").get(0).text());
//			单位缴费金额
            insuranceQuanzhouBirth.setUnitPaymentAmount(element.getElementsByTag("td").get(1).text());
            //			缴费基数
            insuranceQuanzhouBirth.setSocialInsuranceBase(element.getElementsByTag("td").get(2).text());
//			单位缴费比例
            insuranceQuanzhouBirth.setOrgPayRatio(element.getElementsByTag("td").get(3).text());
            injuryList.add(insuranceQuanzhouBirth);
        }
        return injuryList;
    }
}
