package app.parser;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiGeneral;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 合肥市社保
 * @author zmy
 * @data  2017年9月26日
 */
@Component
public class InsuranceHeFeiParser {
    public static final Logger log = LoggerFactory.getLogger(InsuranceHeFeiParser.class);

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private TracerLog tracer;

    /**
     * 登录
     * @param insuranceRequestParameters
     * @return
     * @throws Exception
     */
    public WebParam login(InsuranceRequestParameters insuranceRequestParameters,String ip) throws Exception{
        tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
        String url = "http://"+ip+"/wssb/grlogo.jsp";
        System.out.println("*********url=" + url);
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
            tracer.addTag("InsuranceHeFeiParser===>login", insuranceRequestParameters.getTaskId() + "登录页获取超时！" + e);
            webParam.setCode(statusCode);
            webParam.setPage(loginPage);
            webParam.setAlertMsg("连接超时");
            webParam.setUrl(url);
            return webParam;
        }
        System.out.println("statusCode="+statusCode);
        if (200 == statusCode) {
            HtmlImage image = loginPage.getFirstByXPath("//img[@id='image']");
            String code = chaoJiYingOcrService.getVerifycode(image, "1004");
            HtmlTextInput username = loginPage.getFirstByXPath("//input[@name='xingm']");
            HtmlTextInput idnum = loginPage.getFirstByXPath("//input[@name='sfz']");
            HtmlPasswordInput password = loginPage.getFirstByXPath("//input[@name='sbh']");
            HtmlTextInput verifyCode = loginPage.getFirstByXPath("//input[@name='verify']");
            HtmlElement button = loginPage.getFirstByXPath("//img[@name='dl']");
            TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
            /*BasicUserInsurance basicUserInsurance = taskInsurance.getBasicUserInsurance();
            String name =  null;
            if (basicUserInsurance != null) {
                name = basicUserInsurance.getName();
                username.setText(name); //姓名
            }*/
            username.setText(insuranceRequestParameters.getName()); //姓名
            idnum.setText(insuranceRequestParameters.getUsername());//身份证号码
            password.setText(insuranceRequestParameters.getPassword());
            verifyCode.setText(code);
            loginPage = button.click();

            String alertMsg = WebCrawler.getAlertMsg();
            webParam.setCode(loginPage.getWebResponse().getStatusCode());
            webParam.setPage(loginPage);
            webParam.setAlertMsg(alertMsg);
            webParam.setUrl(url);
            return webParam;
        }
        return null;
    }

    /**
     * 解析个人基本信息
     * @param taskInsurance
     * @param html
     * @return
     */
    public WebParam<InsuranceHeFeiUserInfo> userInfoParser(TaskInsurance taskInsurance, String html) {
        WebParam<InsuranceHeFeiUserInfo> webParam=new WebParam<InsuranceHeFeiUserInfo>();
        System.out.println("=========================");
        System.out.println(html);
        Document doc = Jsoup.parse(html);

        InsuranceHeFeiUserInfo insuranceHeFeiUserInfo = new InsuranceHeFeiUserInfo();
        String perNum = doc.select("td:contains(个人编号)+td").first().select("input").attr("value");
        System.out.println("---------------个人编号- " + perNum);
        System.out.println("---------------姓名- " + doc.select("td:contains(姓)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setPerNum(perNum);
        insuranceHeFeiUserInfo.setName(doc.select("td:matches([\\s\\S]*姓[\\s\\S]*名[\\s\\S]*)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setSex(doc.select("td:matches([\\s\\S]*性[\\s\\S]*别[\\s\\S]*)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setPersionStatus(doc.select("td:contains(养老参保状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setUnemployStatus(doc.select("td:contains(失业参保状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setMedicalStatus(doc.select("td:contains(医疗参保状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setInjuryStatus(doc.select("td:contains(工伤参保状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setBirthStatus(doc.select("td:contains(生育参保状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setCompanyName(doc.select("td:contains(单位名称)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setPersionPayYear(doc.select("td:contains(养老视同缴费年限)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setSocialSecurityNum(doc.select("td:contains(社会保障卡卡号)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setIdNum(doc.select("td:matches([\\s\\S]*身[\\s\\S]*份[\\s\\S]*证[\\s\\S]*)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setWorkTime(doc.select("td:contains(参加工作时间)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setPersionPayBasenum(doc.select("td:contains(养老缴费基数)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setUnemployPayBasenum(doc.select("td:contains(失业缴费基数)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setMedicalPayBasenum(doc.select("td:contains(医疗缴费基数)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setInjuryPayBasenum(doc.select("td:contains(工伤缴费基数)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setBirthPayBasenum(doc.select("td:contains(生育缴费基数)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setWorkStatus(doc.select("td:contains(工作状态)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setUnemployPayYear(doc.select("td:contains(失业视同缴费年限)+td").first().select("input").attr("value"));
        insuranceHeFeiUserInfo.setTaskid(taskInsurance.getTaskid());
        webParam.setInsuranceHeFeiUserInfo(insuranceHeFeiUserInfo);
        return webParam;
    }

    /**
     * 解析社保-五险信息
     * @param taskInsurance
     * @param html
     * @return
     */
    public List<InsuranceHeFeiGeneral> insurGeneralParser(TaskInsurance taskInsurance, String html) {
        Document doc = Jsoup.parse(html);
        List<InsuranceHeFeiGeneral> list = new ArrayList<InsuranceHeFeiGeneral>();
        InsuranceHeFeiGeneral insuranceHeFeiGeneral = null;
        /*Elements tables = doc.getElementsByTag("table");
        for (Element table : tables) {
            Elements trs = table.getElementsByTag("tr");
        }*/

        Elements trs = doc.getElementsByTag("table").get(5).getElementsByTag("tr");
        if(trs.size() >= 0) {
            for (int i = 1; i < trs.size(); i++) {
                System.out.println("=======================================");
                log.info(trs.get(i).html());
                Elements tds = trs.get(i).getElementsByTag("td");
                if (tds.size() == 11) {
                    insuranceHeFeiGeneral = new InsuranceHeFeiGeneral();
                    insuranceHeFeiGeneral.setCompName(trs.get(i).getElementsByTag("td").get(0).text());
                    insuranceHeFeiGeneral.setPayDate(trs.get(i).getElementsByTag("td").get(1).text());
                    insuranceHeFeiGeneral.setInsurType(trs.get(i).getElementsByTag("td").get(2).text());
                    insuranceHeFeiGeneral.setPerPayRatio(trs.get(i).getElementsByTag("td").get(3).text());
                    insuranceHeFeiGeneral.setPerPayBasenum(trs.get(i).getElementsByTag("td").get(4).text());
                    insuranceHeFeiGeneral.setCompPayRatio(trs.get(i).getElementsByTag("td").get(5).text());
                    insuranceHeFeiGeneral.setCompPayAmount(trs.get(i).getElementsByTag("td").get(6).text());
                    insuranceHeFeiGeneral.setPerPayAmount(trs.get(i).getElementsByTag("td").get(7).text());
                    insuranceHeFeiGeneral.setPayAllMoney(trs.get(i).getElementsByTag("td").get(8).text());
                    insuranceHeFeiGeneral.setIsThismonthInaccount(trs.get(i).getElementsByTag("td").get(9).text());
                    insuranceHeFeiGeneral.setPay_type(trs.get(i).getElementsByTag("td").get(10).text());
                    insuranceHeFeiGeneral.setTaskid(taskInsurance.getTaskid());
                    list.add(insuranceHeFeiGeneral);
                }
            }
        }

        return list;
    }
}
