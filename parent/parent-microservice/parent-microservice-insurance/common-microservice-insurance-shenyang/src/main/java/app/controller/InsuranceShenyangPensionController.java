package app.controller;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;
import app.service.InsuranceShenyangPensionService;
import app.service.InsuranceShenyangUnemployeedService;
import app.service.aop.InsuranceLogin;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Mu on 2017/9/19.
 */
@SuppressWarnings("all")
@RestController
@Configuration
@RequestMapping("/insurance/shenyang")
public class InsuranceShenyangPensionController {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceLogin insuranceLogin;

    /**
     * 登陆
     * @param insuranceRequestParameters
     * @param request
     * @return
     */
    @PostMapping("/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
        tracer.qryKeyValue("username", insuranceRequestParameters.getUsername());
        TaskInsurance taskInsurance = insuranceLogin.login(insuranceRequestParameters);
        return taskInsurance;
    }

    @PostMapping("/getData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{
        tracer.qryKeyValue("taskid",insuranceRequestParameters.getTaskId());
        tracer.qryKeyValue("username",insuranceRequestParameters.getUsername());
        TaskInsurance taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
        return taskInsurance;
    }
}
