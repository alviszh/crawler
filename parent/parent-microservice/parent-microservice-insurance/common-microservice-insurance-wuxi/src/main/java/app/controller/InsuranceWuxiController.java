package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.AsyncWuxiGetAllDataService;
import app.service.InsuranceService;
import app.service.InsuranceWuxiService;

/**
 * @author zhangyongjie
 * @create 2017-09-22 15:28
 * @Desc 无锡社保
 */
@RestController
@Configuration
@RequestMapping("/insurance/wuxi")
public class InsuranceWuxiController {

    @Autowired
    private InsuranceWuxiService insuranceWuxiService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TracerLog tracer;

    @Autowired
    private AsyncWuxiGetAllDataService asyncWuxiGetAllDataService;

    /**
     * 登录
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value="/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.addTag("InsuranceWuxiController.login",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
        TaskInsurance taskInsurance = insuranceWuxiService.changeStatus(insuranceRequestParameters);
        try {
            insuranceWuxiService.login(insuranceRequestParameters,1);
        } catch (Exception e) {
            tracer.addTag("InsuranceWuxiController.login:" , taskInsurance.getTaskid()+"---ERROR:"+e);
            e.printStackTrace();
        }
        return taskInsurance;
    }

    @PostMapping(value="/getAllData")
    public TaskInsurance getAllData(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
        tracer.addTag("InsuranceChengduController.getAllData", insuranceRequestParameters.getTaskId());
        boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
        TaskInsurance taskInsurance = null;
        if(isCrawler){
            tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
        }else{
            taskInsurance = insuranceWuxiService.updateTaskInsurance(insuranceRequestParameters);
            asyncWuxiGetAllDataService.getAllData(insuranceRequestParameters);
        }
        return taskInsurance;
    }
}
