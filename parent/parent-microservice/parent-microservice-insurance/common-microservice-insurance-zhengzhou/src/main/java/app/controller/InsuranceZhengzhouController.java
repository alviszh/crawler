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
import app.service.InsuranceZhengzhouService;

/**
 * Created by kaixu on 2017/9/19.
 */
@RestController
@Configuration
@RequestMapping("/insurance/zhengzhou")
public class InsuranceZhengzhouController {

    @Autowired
    private InsuranceZhengzhouService insuranceZhengzhouService;

    @Autowired
    private TracerLog tracer;

    /**
     * 登录
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value="/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.addTag("InsuranceZhengzhouController.login",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
//        TaskInsurance taskInsurance = insuranceZhengzhouService.changeStatus(insuranceRequestParameters);
        TaskInsurance taskInsurance = insuranceZhengzhouService.login(insuranceRequestParameters);
        return taskInsurance;
    }

    @PostMapping(value = "/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter){
        tracer.addTag("InsuranceZhengzhouController.crawler:检测Task",parameter.toString());
        TaskInsurance taskInsurance = insuranceZhengzhouService.getTaskInsurance(parameter);
//        if(null == taskInsurance){
//            return insuranceZhengzhouService.initNotExistTaskInsurance();
//        }
//        if("CRAWLER".equals(taskInsurance.getPhase()) && "DOING".equals(taskInsurance.getPhase_status())){
//            tracer.addTag("正在进行上次未完成的爬取任务。。。",taskInsurance.toString());
//            return taskInsurance;
//        }
        tracer.addTag("InsuranceZhengzhouController.crawler:开始爬取",parameter.toString());
        //更新task表 为 爬取 进行中
//        taskInsurance = insuranceService.changeCrawlerStatusDoing(parameter);
        insuranceZhengzhouService.crawler(parameter,taskInsurance);
        return taskInsurance;
    }
}
