package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.microservice.dao.entity.crawler.car.insurance.CarInsuranceCompanyCode;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;

import app.commontracerlog.TracerLog;
import app.service.CarInsuranceService;
import app.service.TaskService;

@RestController
@RequestMapping("/car/insurance")  
public class TaskController {
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * 一秒一轮巡，查询taskid状态
	 * @param taskid
	 * @return
	 */
	@GetMapping(path = "/tasks/{taskid}/status")
	public TaskCarInsurance taskStatus(@PathVariable String taskid){		
		return carInsuranceService.getTaskCarInsurance(taskid);		
	}
	
	/**
	 * 获取所有车险公司
	 * @return
	 */
	@GetMapping(path = "/companys")
	public List<CarInsuranceCompanyCode> getCompanys(){		
		return taskService.getCompanys();		
	}
	
	/**
	 * 创建taskid
	 * @param carInsuranceRequestBean
	 * @return
	 */
	@PostMapping(path = "/create")
	public TaskCarInsurance create(@RequestBody CarInsuranceRequestBean carInsuranceRequestBean){
		return taskService.create(carInsuranceRequestBean);
		
	}
	
		

}
