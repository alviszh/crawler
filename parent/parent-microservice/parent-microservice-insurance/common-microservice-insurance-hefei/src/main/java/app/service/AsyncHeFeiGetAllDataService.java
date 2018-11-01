package app.service;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHeFeiParser;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiGeneral;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiHtml;
import com.microservice.dao.entity.crawler.insurance.hefei.InsuranceHeFeiUserInfo;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfo;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingHtml;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingUserInfo;
import com.microservice.dao.repository.crawler.insurance.hefei.InsuranceHeFeiGeneralRepository;
import com.microservice.dao.repository.crawler.insurance.hefei.InsuranceHeFeiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.hefei.InsuranceHeFeiUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.hefei"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.hefei"})
public class AsyncHeFeiGetAllDataService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceHeFeiHtmlRepository insuranceHeFeiHtmlRepository;
    @Autowired
    private InsuranceHeFeiUserInfoRepository insuranceHeFeiUserInfoRepository;
    @Autowired
    private InsuranceHeFeiGeneralRepository insuranceHeFeiGeneralRepository;
    @Autowired
    private InsuranceHeFeiParser insuranceHeFeiParser;

    @Async
    public void getUserInfo(TaskInsurance taskInsurance, String [] ips) throws  Exception {
        tracer.qryKeyValue("AsyncHeFeiGetAllDataService.getUserInfo", taskInsurance.getTaskid());
        for (String ip : ips) {
            String url = "http://"+ ip +"/wssb/admin/000001/Grwxcb.jsp";
            int statusCode = 0;
            HtmlPage page = null;
            try {
                WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
                WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
                page = webClient.getPage(webRequest);
                statusCode = page.getWebResponse().getStatusCode();
            } catch (Exception e) {
                tracer.addTag("parser.crawler.getUserinfo", taskInsurance.getTaskid() + "【个人基本信息】页获取超时！" + e);
            }
            if (200 == statusCode) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("parser.crawler.getUserInfo.html" + taskInsurance.getTaskid(), "<xmp>" + html + "</xmp>");
                //存储用户信息源码页
                InsuranceHeFeiHtml insuranceHeFeiHtml = new InsuranceHeFeiHtml();
                insuranceHeFeiHtml.setTaskid(taskInsurance.getTaskid());
                insuranceHeFeiHtml.setType("userInfo用户信息源码页");
                insuranceHeFeiHtml.setPageCount(1);
                insuranceHeFeiHtml.setUrl(url);
                insuranceHeFeiHtml.setHtml(html);
                insuranceHeFeiHtmlRepository.save(insuranceHeFeiHtml);
                tracer.addTag("parser.crawler.getUserinfo.html", "个人信息源码页已入库" + taskInsurance.getTaskid());
                //获取用户信息解析返回值
                WebParam<InsuranceHeFeiUserInfo> webParam = insuranceHeFeiParser.userInfoParser(taskInsurance, html);
                if (null != webParam.getInsuranceHeFeiUserInfo()) {
                    insuranceHeFeiUserInfoRepository.save(webParam.getInsuranceHeFeiUserInfo());
                    tracer.qryKeyValue("parser.crawler.getUserinfo", "个人信息已入库" + taskInsurance.getTaskid());
                    insuranceService.changeCrawlerStatus("【个人基本信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                    insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
                } else {
                    tracer.qryKeyValue("parser.crawler.getUserinfo", "【个人基本信息】无可采集数据，或社保网站升级维护中，暂不提供服务" + taskInsurance.getTaskid());
                    insuranceService.changeCrawlerStatus("【个人基本信息】无可采集数据，或社保网站升级维护中，暂不提供服务", InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase(),
                            203, taskInsurance);
                    insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
                }
                break;
            }
        }
    }

    @Async
    public void getAllInsurData(TaskInsurance taskInsurance,String [] ips) throws Exception{
        tracer.addTag("AsyncHeFeiGetAllDataService.getAllInsurData", taskInsurance.getTaskid());

        for (String ip : ips) {
            String url = "http://"+ ip +"/wssb/admin/000001/Grzmmx.jsp?flag=T";
            int statusCode = 0;
            HtmlPage page = null;
            try {
                WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
                WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
                page = webClient.getPage(webRequest);
                statusCode = page.getWebResponse().getStatusCode();
            } catch (Exception e) {
                tracer.addTag("parser.crawler.getAllInsurData", taskInsurance.getTaskid() + "【社保-五险信息】页获取超时！" + e);
            }
//            if (null != page) {
//                int statusCode = page.getWebResponse().getStatusCode();
            if (200 == statusCode) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("parser.crawler.getMedical.html" + taskInsurance.getTaskid(), "<xmp>" + html + "</xmp>");
                InsuranceHeFeiHtml insuranceHeFeiHtml = new InsuranceHeFeiHtml();
                insuranceHeFeiHtml.setTaskid(taskInsurance.getTaskid());
                insuranceHeFeiHtml.setType("社保-五险信息源码页");
                insuranceHeFeiHtml.setPageCount(1);
                insuranceHeFeiHtml.setUrl(url);
                insuranceHeFeiHtml.setHtml(html);
                insuranceHeFeiHtmlRepository.save(insuranceHeFeiHtml);
                tracer.qryKeyValue("parser.crawler.getMedical.html", "社保-五险信息源码页已入库" + taskInsurance.getTaskid());
                List<InsuranceHeFeiGeneral> list = insuranceHeFeiParser.insurGeneralParser(taskInsurance, html);
                if (null != list && list.size() > 0) {
                    insuranceHeFeiGeneralRepository.saveAll(list);
                    tracer.qryKeyValue("parser.crawler.getMedical", "社保-五险信息已入库" + taskInsurance.getTaskid());
                    insuranceService.changeCrawlerStatus("【养老保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                    insuranceService.changeCrawlerStatus("【失业保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                    insuranceService.changeCrawlerStatus("【生育保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                    insuranceService.changeCrawlerStatus("【医疗保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                    insuranceService.changeCrawlerStatus("【工伤保险信息】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
                            200, taskInsurance);
                } else {
                    tracer.qryKeyValue("parser.crawler.getMedical", "【社保-五险信息】无可采集数据，或社保网站升级维护中，暂不提供服务" + taskInsurance.getTaskid());
                    insuranceService.changeCrawlerStatus("【社保-五险信息】无可采集数据，或社保网站升级维护中，暂不提供服务", InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase(),
                            203, taskInsurance);
                }
                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
                break;
            }
//            }
        }

    }
}