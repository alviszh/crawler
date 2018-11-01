package app.controller;

import app.commontracerlog.TracerLog;
import app.service.InsuranceXiamenService;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kaixu on 2017/9/19.
 */
@RestController
@Configuration
@RequestMapping("/insurance/xiamen")
public class InsuranceXiamenController {

    @Autowired
    private InsuranceXiamenService insuranceXiamenService;

    @Autowired
    private TracerLog tracer;

    /**
     * 登录
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value="/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.addTag("InsuranceXiamenController.login",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.login.auth",insuranceRequestParameters.getUsername());
        TaskInsurance taskInsurance = insuranceXiamenService.changeStatus(insuranceRequestParameters);
        insuranceXiamenService.login(insuranceRequestParameters);
        return taskInsurance;
    }

    @PostMapping(value = "/getAllData")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters parameter){
        tracer.addTag("InsuranceXiamenController.crawler:检测Task",parameter.toString());
        TaskInsurance taskInsurance = insuranceXiamenService.getTaskInsurance(parameter);
        if(null == taskInsurance){
            return insuranceXiamenService.initNotExistTaskInsurance();
        }
        
        tracer.addTag("InsuranceZhengzhouController.crawler:开始爬取",parameter.toString());
        //更新task表 为 爬取 进行中
        insuranceXiamenService.getAllData(parameter);
        return taskInsurance;
    }
}
