package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.InsuranceDaQingService;
import app.service.InsuranceService;

/**
 	后来大庆社保改成了只有一个接口，登陆成功之后响应的页面中就是要爬取的信息,故决定直接就是爬取接口
 	该网站调研的时候发现，输入错误的登陆信息，或者是输入不存在的信息，响应回来的页面也是“无记录”
 	sln
 	2018-08-16
 	
 	
 	故决定调用两个接口，用中间表来存储登陆成功之后响应的页面，然后爬取的时候再取出来
 */
@RestController
@Configuration
@SuppressWarnings("all")
@RequestMapping("/insurance/daqing")
public class InsuranceDaQingController {

    @Autowired
    private InsuranceDaQingService insuranceDaQingService;
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceDaQingService daQingService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;

    @PostMapping(value="/login")
    public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
    	tracer.addTag("登录的用户名和密码分别是：",insuranceRequestParameters.getUsername()+"和"+insuranceRequestParameters.getPassword());
    	TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        taskInsurance = daQingService.login(insuranceRequestParameters);
        return taskInsurance;
    }

    @PostMapping(value="/getAllData")
    public void crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
    	daQingService.getAllData(insuranceRequestParameters);
    }
}
