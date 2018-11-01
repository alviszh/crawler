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
import app.service.InsuranceService;
import app.service.InsuranceTaiyuanService;

/**
 * Created by Mu on 2017/9/19.
 */
@SuppressWarnings("all")
@RestController
@Configuration
@RequestMapping("/insurance/taiyuan")
public class InsuranceTaiyuanController {
    @Autowired
    private InsuranceTaiyuanService insuranceTaiyuanService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;

    /**
     * 登陆
     * @param insuranceRequestParameters
     * @param request
     * @return
     */
    @PostMapping("/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
        TaskInsurance taskInsurance = insuranceTaiyuanService.changeStatus(insuranceRequestParameters);

        try {
            taskInsurance= insuranceTaiyuanService.login(insuranceRequestParameters,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskInsurance;
    }

    @PostMapping("/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
        tracer.addTag("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());
        boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);

        TaskInsurance taskInsurance = null;
        if(isCrawler){
            tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
        }else{
            //更改状态完正在爬取
            taskInsurance = insuranceTaiyuanService.updateTaskInsurance(insuranceRequestParameters);
            insuranceTaiyuanService.getUserInfo(insuranceRequestParameters);
            insuranceTaiyuanService.getJuminFirst(insuranceRequestParameters);
            insuranceTaiyuanService.getJuminPayInfo(insuranceRequestParameters);
            insuranceTaiyuanService.getStaffFirst(insuranceRequestParameters);
            insuranceTaiyuanService.getStaffPayInfo(insuranceRequestParameters);
        }
        return taskInsurance;
    }
}
