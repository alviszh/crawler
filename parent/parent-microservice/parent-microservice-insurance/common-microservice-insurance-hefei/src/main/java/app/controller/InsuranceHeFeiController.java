package app.controller;

import app.bean.MyProps;
import app.commontracerlog.TracerLog;
import app.service.AsyncHeFeiGetAllDataService;
import app.service.InsuranceHeFeiService;
import app.service.InsuranceService;
import app.service.aop.InsuranceLogin;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 合肥市社保
 * @author zmy
 * @date 2017年9月26日
 */
@RestController
@Configuration
@RequestMapping("/insurance/hefei")
public class InsuranceHeFeiController {
    public static final Logger log = LoggerFactory.getLogger(InsuranceHeFeiController.class);

    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceLogin insuranceLogin;

    /**
     * 登录接口
     * @param insuranceRequestParameters
     * @return
     */
    @PostMapping(value="/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
        tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = null;
        taskInsurance = insuranceLogin.login(insuranceRequestParameters);
        return taskInsurance;
}

    @PostMapping(value="/crawler")
    public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters) throws Exception{

        tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
//        boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
        TaskInsurance taskInsurance = null;
//        if(isCrawler){
            tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
        taskInsurance = insuranceLogin.getAllData(insuranceRequestParameters);
       /* }else{
            taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
            //爬取个人基本信息
            asyncHeFeiGetAllDataService.getUserInfo(taskInsurance, myProps.getIps());
            //爬取五险
            asyncHeFeiGetAllDataService.getAllInsurData(taskInsurance, myProps.getIps());
        }*/

        return taskInsurance;

    }
}
