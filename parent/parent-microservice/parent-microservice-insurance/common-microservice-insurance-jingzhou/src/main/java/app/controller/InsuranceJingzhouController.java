package app.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.InsuranceJingzhouService;
import app.service.InsuranceService;


@RestController
@Configuration
@RequestMapping("/insurance/jingzhou")
public class InsuranceJingzhouController {


    @Autowired
    private TracerLog tracer;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    InsuranceJingzhouService insuranceJingzhouService;


    /**
     * 登录
     *
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value = "/getdata")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters) {

        tracer.addTag("InsuranceZhoushanController.login", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.login.taskid", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth", insuranceRequestParameters.getUsername());

        TaskInsurance taskInsurance = insuranceService.getTaskInsurance(insuranceRequestParameters);

        if (taskInsurance != null) {
            insuranceService.changeLoginStatusDoing(taskInsurance);

            boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
            if (!isCrawler) {
                String cookies = null;

                try {
                    String[] result = insuranceJingzhouService.login(insuranceRequestParameters);
                    cookies = result[0];
                    insuranceRequestParameters.setUsername(result[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                    insuranceService.changeLoginStatusTimeOut(taskInsurance);
                }
                if (StringUtils.isNotBlank(cookies)) {
                    tracer.addTag("InsuranceDandongController.crawlder", insuranceRequestParameters.getTaskId());
                    tracer.addTag("InsuranceDandongController.crawler.cookies", cookies);
                    insuranceJingzhouService.changeCrawlerStatusDoing(insuranceRequestParameters);
                    taskInsurance = insuranceJingzhouService.getData(taskInsurance, insuranceRequestParameters, cookies);
                } else {
                    insuranceService.changeLoginStatusTimeOut(taskInsurance);
                }
            } else {
                tracer.addTag("正在进行上次未完成的爬取任务。", insuranceRequestParameters.toString());
            }
            return taskInsurance;
        } else {
            return null;
        }
    }
}