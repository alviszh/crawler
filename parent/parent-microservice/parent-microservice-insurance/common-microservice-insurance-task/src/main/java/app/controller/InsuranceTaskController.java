package app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;
import com.crawler.insurance.json.InsuranceJsonBean;
import com.microservice.dao.entity.crawler.insurance.basic.AreaCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.InsuranceTaskService;

@RestController
@RequestMapping("/insurance") 
public class InsuranceTaskController {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceTaskController.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceTaskService insuranceTaskService;
	
	@PostMapping(path = "/check")
	public TaskInsurance createTask(@RequestBody InsuranceJsonBean insuranceJsonBean){
		
		tracer.addTag("InsuranceJsonBean =======>>",insuranceJsonBean.toString());
		
		return insuranceTaskService.createTask(insuranceJsonBean);			
	
	}
	
	
	@GetMapping(path="/tasks/{taskid}/status")
	public TaskInsurance taskStatus(@PathVariable String taskid){
		
		TaskInsurance taskInsurance = insuranceTaskService.getTaskInsurance(taskid);
		tracer.addTag("Request task status", "taskid:"+taskid); 
		return taskInsurance;
		
	}
	
	
	/**
	 * @Description: 获取所有社保已开发完成的城市
	 * 
	 * 	0 :内测中  1：开发完成  2：维护中
	 * @return
	 */
	@GetMapping(path = "/citys")
	public List<AreaCode> getCitys(){
		
		tracer.addTag("getCitys ==>","start");
		
		return insuranceTaskService.getCitys();
		
	}
	
	
	@PostMapping(path = "/tasks/getInsurancePages")
	public @ResponseBody
	PageInfo<TaskInsurance> getTaskInsurancePages(@RequestParam(value = "currentPage") int currentPage,
											@RequestParam(value = "pageSize") int pageSize,
											@RequestParam(value = "taskid", required = false) String taskid) {

		//根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("taskid",taskid);
//		paramMap.put("idnum",idnum);

		Page<TaskInsurance> tasksPage = insuranceTaskService.getTaskInsuranceTaskByParams(paramMap, currentPage, pageSize);
		System.out.println("******getTaskPages:"+tasksPage);

		PageInfo<TaskInsurance> pageInfo = new PageInfo<TaskInsurance>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
	
	
	/**
	 * 根据创建时间统计社保的调用量（线性图表）
	 * @return
	 */
	@RequestMapping(value = "/tasks/lineData" , method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	List lineData(){
		List result = insuranceTaskService.getInsuranceTaskStatistics();
		return result;
	}

	/**
	 * 统计每个社保的调用量
	 * @return
	 */
	@RequestMapping(value = "/tasks/pieData" , method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	List pieData(){
		List result = insuranceTaskService.getGroupByInsurance();
		return result;
	}
	

}
		
