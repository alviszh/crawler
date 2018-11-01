package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

import app.commontracerlog.TracerLog;
import app.service.YgbxService;

@RestController
@RequestMapping("/car/insurance/ygbx")  
public class YgbxController {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private YgbxService ygbxService;
	
	/**
	 * 阳光保险请求保险单校验接口
	 * @param carInsuranceRequestBean  身份证号、保险单号
	 * @return
	 */
	@PostMapping(path = "/verify")
	public TaskCarInsurance verify(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("ygbx.verify.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = ygbxService.verify(carInsuranceRequestBean);		
		return taskCarInsurance;	
	}
	
	
	/**
	 * 数据爬取接口
	 * @param carInsuranceRequestBean
	 * @return
	 */
	@PostMapping(path = "/crawler")
	public TaskCarInsurance crawler(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		tracer.addTag("ygbx.crawler.taskid", carInsuranceRequestBean.getTaskid());
		return ygbxService.getAllData(carInsuranceRequestBean);
	}

}
