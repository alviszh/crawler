package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dandong.InsuranceDandongUserInfo;

import app.commontracerlog.TracerLog;
import app.service.InsuranceDandongService;
import app.service.InsuranceService;

@RestController
@Configuration
@RequestMapping("/insurance/dandong")
public class InsuranceDandongController {


    @Autowired
    private TracerLog tracer;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    InsuranceDandongService insuranceDandongService;


    /**
     * 登录
     *
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value = "/getdata")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

        tracer.addTag("InsuranceDandongController.login", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.login.taskid", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth", insuranceRequestParameters.getUsername());

        TaskInsurance taskInsurance = insuranceDandongService.getTaskInsurance(insuranceRequestParameters);

        if (taskInsurance != null) {
            insuranceDandongService.changeLoginStatusDoing(taskInsurance);
            try {
                boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
                if (!isCrawler) {
                    HtmlPage htmlPage = insuranceDandongService.login(insuranceRequestParameters, 1);
                    tracer.addTag("InsuranceDandongController.crawler", insuranceRequestParameters.getTaskId());
                    tracer.addTag("InsuranceDandongController.crawler.html.page", htmlPage.asXml());
                    insuranceDandongService.changeCrawlerStatusDoing(insuranceRequestParameters);
                    InsuranceDandongUserInfo userInfo = insuranceDandongService.getData(htmlPage);
                    if (userInfo != null) {
                        userInfo.setTaskid(taskInsurance.getTaskid());
                        insuranceDandongService.saveUserInfo(userInfo);
                        insuranceDandongService.saveTaskInsurance(taskInsurance);
                        insuranceDandongService.changeCrawlerStatusSuccess(taskInsurance);
                    } else {
                        throw new RuntimeException("Parser Dandong Insurance UserInfo Error!");
                    }

                } else {
                    tracer.addTag("正在进行上次未完成的爬取任务。。。", insuranceRequestParameters.toString());
                }
            } catch (Exception e) {
                tracer.addTag("InsuranceDandongController.crawler:", taskInsurance.getTaskid() + "---ERROR:" + e);
//                if (true) {
//                    insuranceDandongService.changeLoginStatusException(taskInsurance);
//                } else {
//                    insuranceDandongService.err(taskInsurance);
//                }

                e.printStackTrace();
            }
        }
        return taskInsurance;
    }
}
