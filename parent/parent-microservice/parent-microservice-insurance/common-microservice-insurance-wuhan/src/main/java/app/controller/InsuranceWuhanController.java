package app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.service.AsyncWuhanGetAllInfoService;
import app.service.InsuranceWuhanService;
import app.service.InsuranceService;

//调用爬虫接口的地方
//这是控制流程的地方！！

@RestController
@Configuration
@RequestMapping("/insurance/wuhan") //制定基础服务路径
public class InsuranceWuhanController {

	public static final Logger log = LoggerFactory.getLogger(InsuranceWuhanController.class);
	
	@Autowired
	private InsuranceWuhanService insuranceWuhanService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	
	@Autowired
	private AsyncWuhanGetAllInfoService  asyncWuhanGetAllInfoService;
	
	
	
	/**
	 * author lisxhixiong
	 * @param insuranceRequestParameters
	 * @return
	 * @date  2017/8/21
	 */
	@PostMapping(value="/login") 	//设置登录入口
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if(null != taskInsurance){
		//在数据控中更改状态-----正在登录
		taskInsurance  = insuranceService.changeLoginStatusDoing(taskInsurance);
		}
		try {
			//调用登录方法
			insuranceWuhanService.login(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;		
	}
	/**
	 * author  lishixiong
	 * @param insuranceRequestParameters
	 * @return
	 * @date  2017/9/19
	 */
	@PostMapping(value="/getAllData")	//设置爬取用户信息的入口
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		log.info("------------InsuranceWuhanController crawler-------------");
		//将任务状态设置为正在爬取
		TaskInsurance taskInsurance = insuranceWuhanService.updateTaskInsurance(insuranceRequestParameters); //更改数据库中的登录状态
		try {
			//调用爬取用户信息方法
			asyncWuhanGetAllInfoService.getAllInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
}
