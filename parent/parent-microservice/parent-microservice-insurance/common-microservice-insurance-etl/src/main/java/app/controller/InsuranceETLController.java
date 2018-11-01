package app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.bean.RequestParam;
import app.bean.WebData;
import app.commontracerlog.TracerLog;
import app.service.InsuranceETLService;

@RestController
@RequestMapping("/etl/insurance")
public class InsuranceETLController {
	
	@Autowired 
	private TracerLog tracer;
	@Autowired 
	private InsuranceETLService insuranceETLService;
	
	
	@PostMapping(path = "/detail")
	public WebData getAllData(@RequestBody RequestParam requestParam){
		
		tracer.addTag("parser.crawler.getAllData",requestParam.toString());
		
		return insuranceETLService.getAllData(requestParam);
		
	}
	
	@GetMapping(path = "/detail")
	public WebData getData(String taskid,String idnum){
		System.out.println(taskid+"=====----===="+idnum);
		RequestParam rp = new RequestParam();
		rp.setTaskid(taskid);
		rp.setIdnum(idnum);
		tracer.addTag("parser.crawler.getAllData",rp.toString());
		WebData allData = insuranceETLService.getAllData(rp);
		return allData;
		
	}
	
	@RequestMapping(value = "/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskInsurance> findAll(
			@org.springframework.web.bind.annotation.RequestParam(value = "currentPage") int currentPage,
			@org.springframework.web.bind.annotation.RequestParam(value = "pageSize") int pageSize,
			@org.springframework.web.bind.annotation.RequestParam(value = "owner") String owner,
			@org.springframework.web.bind.annotation.RequestParam(value = "environmentId") String environmentId,
			@org.springframework.web.bind.annotation.RequestParam(value = "beginTime") String beginTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "endTime") String endTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "taskid") String taskid,
			@org.springframework.web.bind.annotation.RequestParam(value = "loginName") String loginName,
			@org.springframework.web.bind.annotation.RequestParam(value = "userId") String userId
			) {

		System.out.println("currentPage-----"+currentPage);
		System.out.println("pageSize-----"+pageSize);
		System.out.println("应用-----"+owner);
		System.out.println("环境-----"+environmentId);
		System.out.println("开始时间-----"+beginTime);
		System.out.println("结束时间-----"+endTime);
		System.out.println("任务id-----"+taskid);
		System.out.println("登录账号-----"+loginName);
		System.out.println("用户id-----"+userId);
		
		// 根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("owner", owner);
		paramMap.put("environmentId", environmentId);
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("taskid", taskid);
		paramMap.put("loginName", loginName);
		paramMap.put("userId", userId);

		Page<TaskInsurance> tasksPage = insuranceETLService.getBankTaskByParams(paramMap,currentPage, pageSize);
		
		System.out.println("社保获取到的数据-----"+tasksPage.getContent().size());
		
		PageInfo<TaskInsurance> pageInfo = new PageInfo<TaskInsurance>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
	
	
}
