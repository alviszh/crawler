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
import app.service.AsyncZhongshanGetAllDataService;
import app.service.InsuranceService;
import app.service.InsuranceZhongshanService;


@RestController
@Configuration
@RequestMapping("/insurance/zhongshan") 
public class InsuranceZhongShanController {

	@Autowired
	private InsuranceZhongshanService insuranceZhongshanService;
	@Autowired
	private AsyncZhongshanGetAllDataService asyncZhongshanGetAllDataService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired 
	private TracerLog tracer;
	 
	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskInsurance login(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceZhongshanController.login",insuranceRequestParameters.getTaskId());
		
		tracer.addTag("parser.login.taskid",insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = insuranceZhongshanService.changeStatus(insuranceRequestParameters);
		
		try {
			insuranceZhongshanService.login(insuranceRequestParameters,1);
		} catch (Exception e) {
			tracer.addTag("InsuranceZhongshanController.login:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return taskInsurance;
		
	}
	
	/**
	 * 获取数据
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/getAllData")
	public TaskInsurance crawler(@RequestBody InsuranceRequestParameters insuranceRequestParameters){
		
		tracer.addTag("InsuranceZhongshanController.crawler", insuranceRequestParameters.getTaskId());
		
		tracer.addTag("parser.crawler.taskid",insuranceRequestParameters.getTaskId());
		
		TaskInsurance taskInsurance = null;
		
		boolean isCrawler = insuranceService.isDoing(insuranceRequestParameters);
		
		if(isCrawler){
			tracer.addTag("正在进行上次未完成的爬取任务。。。",insuranceRequestParameters.toString());
		}else{
			taskInsurance = insuranceZhongshanService.updateTaskInsurance(insuranceRequestParameters);	
			asyncZhongshanGetAllDataService.getAllData(insuranceRequestParameters);
		}
		
	
		return taskInsurance;
		
	}
	
}
