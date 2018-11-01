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

import com.alibaba.fastjson.JSON;
import com.crawler.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.RequestParam;
import app.bean.WebData;
import app.bean.WebDataByID;
import app.bean.WebDataByName;
import app.bean.WebDataReport;
import app.bean.WebDataReportV2;
import app.bean.WebRawDataReportV2;
import app.commontracerlog.TracerLog;
import app.service.MobileETLByIdnumService;
import app.service.MobileETLByNameService;
import app.service.MobileETLRawDataReportV2Service;
import app.service.MobileETLReportService;
import app.service.MobileETLReportV2Service;
import app.service.MobileETLService;

@RestController
//@RequestMapping("/carrier")
public class MobileETLController {
	
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private MobileETLService mobileETLService;
	@Autowired
	private MobileETLByIdnumService mobileETLByIdnumService;
	@Autowired
	private MobileETLByNameService mobileETLByNameService;
	@Autowired
	private MobileETLReportService mobileETLReportService;
	@Autowired
	private MobileETLReportV2Service mobileETLReportV2Service;	
	@Autowired
	private MobileETLRawDataReportV2Service mobileETLRawDataReportV2Service;

	@PostMapping(path = "/carrier/tasks/mobileinfo/detail")
	public WebData getAllData(@RequestBody RequestParam requestParam){
		System.out.println("param: "+requestParam.toString());
		tracer.addTag("parser.crawler.getAllData",requestParam.toString());
		return mobileETLService.getAllData(requestParam);
		
	}
	
	@GetMapping(path = "/carrier/tasks/mobileinfo/detail")
	public WebData getData(String taskid,String mobilenum){
		System.out.println("taskid: "+taskid);
		System.out.println("mobilenum: "+mobilenum);		
		RequestParam rp = new RequestParam();
		tracer.addTag("parser.crawler.getAllData",rp.toString());
		tracer.addTag("taskid",taskid);
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		WebData allData = mobileETLService.getAllData(rp);
		String jsonString = JSON.toJSONString(allData);
		tracer.addTag("result",jsonString);
		return allData;		
	}
	
	@GetMapping(path = "/etl/carrier/tasks/mobileinfo/report")
	public WebDataReport getDataReport(String taskid,String mobilenum){
		System.out.println("taskid: "+taskid);
		System.out.println("mobilenum: "+mobilenum);		
		RequestParam rp = new RequestParam();
		tracer.addTag("parser.crawler.getAllData",rp.toString());
		tracer.addTag("taskid",taskid);
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		WebDataReport allData = mobileETLReportService.getAllData(rp);
		String jsonString = JSON.toJSONString(allData);
		tracer.addTag("result",jsonString);
		return allData;		
	}

	@GetMapping(path = "/etl/carrier/tasks/mobileinfo/detailStr")
	public String getDataStr(String taskid,String mobilenum){
		System.out.println("taskid: "+taskid);
		System.out.println("mobilenum: "+mobilenum);
		RequestParam rp = new RequestParam();
		tracer.addTag("parser.crawler.getAllData",rp.toString());
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		WebData webData = mobileETLService.getAllData(rp);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(webData);
	}
	
	@GetMapping(path = "/etl/carrier/tasks/getphonenum")
	public WebDataByID getAllDataByIdnum(String idnum){
		System.out.println("idnum: "+idnum);
		tracer.addTag("parser.crawler.getAllDataByIdnum",idnum);
		return mobileETLByIdnumService.getAllData(idnum);
		
	}
	
	@GetMapping(path = "/etl/carrier/tasks/getidnum")
	public WebDataByName getDataByname(String name){
		System.out.println("name: "+name);
		tracer.addTag("parser.crawler.getAllDataByName",name);
		return mobileETLByNameService.getAllData(name);
		
	}

	@RequestMapping(path = "/etl/carrier/v2/reportdata",method = {RequestMethod.GET,RequestMethod.POST})
	public WebDataReportV2 getCarrerDataV2(String taskid,String mobilenum){
		System.out.println("taskid: "+taskid);
		System.out.println("mobilenum: "+mobilenum);
		tracer.addTag("parser.crawler.getAllData",mobilenum);
		RequestParam rp = new RequestParam();	
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		return mobileETLReportV2Service.getAllData(rp);
	}
	
	@RequestMapping(path = "/etl/carrier/v2/reportrawdata",method = {RequestMethod.GET,RequestMethod.POST})
	public WebRawDataReportV2 getRawCarrerDataV2(String taskid,String mobilenum){
		System.out.println("taskid: "+taskid);
		System.out.println("mobilenum: "+mobilenum);
		tracer.addTag("parser.crawler.getRawCarrerDataV2",mobilenum);
		RequestParam rp = new RequestParam();	
		rp.setTaskid(taskid);
		rp.setMobileNum(mobilenum);
		return mobileETLRawDataReportV2Service.getAllData(rp);
	}
	
	
	@RequestMapping(value = "/etl/carrier/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskMobile> findAll(
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

		Page<TaskMobile> tasksPage = mobileETLService.getMobileTaskByParams(paramMap,currentPage, pageSize);
		
		System.out.println("运营商获取到的数据-----"+tasksPage.getContent().size());
		
		PageInfo<TaskMobile> pageInfo = new PageInfo<TaskMobile>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
	
	
	
}
