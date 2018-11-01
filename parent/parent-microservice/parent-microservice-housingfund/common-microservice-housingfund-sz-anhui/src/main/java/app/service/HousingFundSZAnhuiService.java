package app.service;

import app.domain.WebParam;
import app.parser.HousingFundSZAnhuiParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiHtml;
import com.microservice.dao.entity.crawler.housing.sz.anhui.HousingSZAnhuiUserInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:  安徽省直公积金信息爬取
 * @author: zmy
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.sz.anhui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.sz.anhui"})
public class HousingFundSZAnhuiService extends HousingBasicService  implements ICrawlerLogin {

    @Autowired
    private HousingFundSZAnhuiParser housingFundSZAnhuiParser;
    @Autowired
    private HousingFundSZAnhuiGetAllDataService housingFundSZAnhuiGetAllDataService;

    private static int errorCount=0;

    @Async
    @Override
    public TaskHousing login(MessageLoginForHousing messageLoginForHousing){
        String taskId = messageLoginForHousing.getTask_id();
        TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
        JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
        tracer.qryKeyValue("HousingFundSZAnhuiService.login", jsonObject.toString());
        if(null != taskHousing){
            WebParam webParam = null;
            try {
                webParam = housingFundSZAnhuiParser.login(messageLoginForHousing);
            }catch (Exception e) {
                tracer.addTag("登陆异常", e.getMessage());
                taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
                taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
                taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
                taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                save(taskHousing);
                return taskHousing;
            }
            System.out.println(webParam);
            System.out.println(webParam.getCode());
            if (null == webParam) {
                tracer.addTag("HousingFundSZAnhuiService===>login", taskId + "登录页获取超时！");
                taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
                taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
                taskHousing.setDescription("连接超时！");
                taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
                taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                taskHousing.setLoginMessageJson(jsonObject.toString());
                save(taskHousing);
                return taskHousing;

            } else {
                String alertMsg = webParam.getAlertMsg();
                System.out.println("*********alertMsg=" + alertMsg);
                if (alertMsg.contains("登录成功")) {
                    tracer.addTag(taskId, "登陆成功");
                    taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
                    taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
                    taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

                    taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
                    taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
                    taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                    taskHousing.setCookies(webParam.getCookies());//保存cookies
                    taskHousing.setLoginMessageJson(jsonObject.toString());
                    save(taskHousing);
                    return taskHousing;

                } else if (alertMsg.contains("用户名或密码错误")) { //姓名、身份证号码不正确
                    taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
                    taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
                    taskHousing.setDescription("用户名或密码错误!");

                    taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR.getCode());
                    taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR.getMessage());
                    taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                    taskHousing.setLoginMessageJson(jsonObject.toString());
                    save(taskHousing);
                    return taskHousing;

                } else if (alertMsg.contains("请输入用户名和密码")) {
                    taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
                    taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
                    taskHousing.setDescription("请输入用户名和密码!");

                    taskHousing.setError_code(StatusCodeRec.MOBILE_LOGIN_ERROR.getCode());
                    taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_ERROR.getMessage());
                    taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                    taskHousing.setLoginMessageJson(jsonObject.toString());
                    save(taskHousing);
                    return taskHousing;

                } else {
                    tracer.addTag("HousingFundSZAnhuiService===>login", taskId + "登录页获取超时！");
                    taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
                    taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
                    taskHousing.setDescription("连接超时！");
                    taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
                    taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
                    taskHousing.setLoginMessageJson(jsonObject.toString());
                    save(taskHousing);
                    return taskHousing;
                }
            }
        }
        return null;
    }

    @Async
    @Override
    public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
        TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
        tracer.qryKeyValue("HousingFundSZAnhuiService.crawler", taskHousing.getTaskid());
        WebClient webClient = taskHousing.getClient(taskHousing.getCookies());

        try {
            //跳转到合肥登录成功页面
            String urlHefei = "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx";
            WebRequest webRequestHefei = new WebRequest(new URL(urlHefei), HttpMethod.GET);
            Page pageHefei = webClient.getPage(webRequestHefei);
            System.out.println("============跳转到合肥登录成功页面============");
            String htmlHefei = pageHefei.getWebResponse().getContentAsString();
            System.out.println(htmlHefei);

            int statusCode = pageHefei.getWebResponse().getStatusCode();
            if (200 == statusCode) {
                String html = pageHefei.getWebResponse().getContentAsString();
                System.out.println("查询页面***"+html);
            /*int i = html.indexOf("cx_fromsfzhm_sz.aspx?");
            int j = html.indexOf(",", i);
            String getPayUrl = "http://www.ahgjj.gov.cn/gjjcx/cx_fromsfzhm_sz.aspx?"+html.substring(i+21, j-1);*/

                //获取公积金查询结果
                String jieguoUrl =  "http://www.ahgjj.gov.cn";
                int statusCodePay = 0;
                HtmlPage jieguoPage = null;
                try {
                /*WebClient webClientPay = taskHousing.getClient(taskHousing.getCookies());
                WebRequest webRequestPay = new WebRequest(new URL(getPayUrl), HttpMethod.GET);
                pagePay = webClientPay.getPage(webRequestPay);
                statusCodePay = pagePay.getWebResponse().getStatusCode();*/

                    if (htmlHefei.contains("window.open('..")) {
                        jieguoUrl += splitData(htmlHefei, "window.open('..", "')");
                    }
                    System.out.println("获取公积金url：jieguoUrl=" + jieguoUrl);
//		String urlJieguo = "http://www.ahgjj.gov.cn/gjjcx/cx_fromsfzhm_sz.aspx?cx=20180426&sfzhm=F38wrqoLFj/znMCuz29AJsWsRFD%2B8Tx5";
                    WebRequest webRequestJieguo = new WebRequest(new URL(jieguoUrl), HttpMethod.GET);
                    webRequestJieguo.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,**/*//*;q=0.8");
                    webRequestJieguo.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
                    webRequestJieguo.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
                    webRequestJieguo.setAdditionalHeader("Cache-Control", "max-age=0");
                    webRequestJieguo.setAdditionalHeader("Connection", "keep-alive");
                    webRequestJieguo.setAdditionalHeader("Host", "www.ahgjj.gov.cn");
                    webRequestJieguo.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
                    webRequestJieguo.setAdditionalHeader("Referer", "http://www.ahgjj.gov.cn/SzCasLogin/SzLogin.aspx");
                    webRequestJieguo.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.104 Safari/537.36");
                    jieguoPage = webClient.getPage(webRequestJieguo);
                    statusCodePay = jieguoPage.getWebResponse().getStatusCode();
                    System.out.println("============hefei== jieguoPage ==========");
                    System.out.println(jieguoPage.getWebResponse().getContentAsString());

                } catch (Exception e) {
                    tracer.addTag("crawler.getUserinfo.exception", taskHousing.getTaskid() + "【个人基本信息】页获取超时！" + e.toString());
                    System.out.println("crawler.getUserinfo.exception:"+ e.toString());
                }
                if (200 == statusCodePay) {
                    String htmlPay = jieguoPage.getWebResponse().getContentAsString();
                    System.out.println("jieguo***" + htmlPay);
                    if (htmlPay.contains("没有登录")){
                        tracer.addTag("crawler.getUserInfo.html" + taskHousing.getTaskid(), "没有登录不能查询!");
                        System.out.println("没有登录不能查询");
                    } else {
                        System.out.println("页面获取成功");
                        tracer.addTag("crawler.getUserInfo.html" + taskHousing.getTaskid(), "<xmp>" + htmlPay + "</xmp>");
                        housingFundSZAnhuiGetAllDataService.getUserInfo(taskHousing, jieguoPage, jieguoUrl);
                        housingFundSZAnhuiGetAllDataService.getPayDetails(taskHousing, jieguoPage, jieguoUrl);
                        updateTaskHousing(taskHousing.getTaskid());
                    }
                }
            }

        } catch (Exception e) {
            taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
            taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
            taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
            taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
            tracer.addTag("爬取异常", e.getMessage());
            save(taskHousing);
            return taskHousing;
        }
        return taskHousing;
    }

    public static String splitData(String str, String strStart, String strEnd) {
        int i = str.indexOf(strStart);
        int j = str.indexOf(strEnd, i);
        String tempStr=str.substring(i+strStart.length(), j);
        return tempStr;
    }

    @Override
    public TaskHousing getAllDataDone(String taskId) {
        return null;
    }
}
