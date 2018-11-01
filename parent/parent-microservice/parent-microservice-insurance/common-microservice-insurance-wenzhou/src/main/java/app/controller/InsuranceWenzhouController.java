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
import app.service.InsuranceWenzhouCommonService;
import app.service.InsuranceWenzhouService;

/**
 * Created by Mu on 2017/9/19.
 */
@SuppressWarnings("all")
@RestController
@Configuration
@RequestMapping("/insurance/wenzhou")
public class InsuranceWenzhouController {
	@Autowired
	private InsuranceWenzhouCommonService insuranceWenzhouCommonService;
    @Autowired
    private InsuranceWenzhouService insuranceWenzhouService;
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
        TaskInsurance taskInsurance = insuranceWenzhouService.changeStatus(insuranceRequestParameters);

        taskInsurance= insuranceWenzhouCommonService.login(insuranceRequestParameters);
        
        return taskInsurance;
    }

    @PostMapping("/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
        tracer.addTag("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());

        TaskInsurance taskInsurance = insuranceWenzhouCommonService.getAllData(insuranceRequestParameters);

        return taskInsurance;
    }
}
