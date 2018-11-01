package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

import app.commontracerlog.TracerLog;
import app.service.PingAnService;

@RestController
@RequestMapping("/car/insurance/pingan")
public class PingAnController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private PingAnService pingAnService;
	@PostMapping(path = "/verify")
	public TaskCarInsurance verify(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("pingan.verify.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = pingAnService.verify(carInsuranceRequestBean);
		return taskCarInsurance;	
	}
	
	@PostMapping(path = "/sendSms")
	public TaskCarInsurance sendSms(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("pingan.sendSms.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = pingAnService.sendSms(carInsuranceRequestBean);
		return taskCarInsurance;
	}
	
	@PostMapping(path = "/verifySms")
	public TaskCarInsurance verifySms(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("pingan.verifySms.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = pingAnService.verifySms(carInsuranceRequestBean);
		return taskCarInsurance;
	}
	
	@PostMapping(path = "/getAllData")
	public TaskCarInsurance getAllData(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("pingan.getAllData.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = pingAnService.getAllData(carInsuranceRequestBean);
		return taskCarInsurance;
	}
	
}
