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
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

import app.bean.RequestParam;
import app.bean.WebDataJD;
import app.bean.WebDataSuning;
import app.bean.WebDataTaoBao;
import app.commontracerlog.TracerLog;
import app.service.JDETLservice;
import app.service.SuningETLservice;
import app.service.TaoBaoETLservice;

@RestController
@RequestMapping("/ecommerce") 
public class EcommerceETLController {

	@Autowired
	private TracerLog tracerLog;
	@Autowired 
	JDETLservice jDETLservice;
	@Autowired 
	TaoBaoETLservice taoBaoETLservice;
	@Autowired 
	SuningETLservice suningETLservice;
	
	@PostMapping(path = "/tasks/jd/detail")
	public WebDataJD getAllData(@RequestBody RequestParam requestParam){
		tracerLog.addTag("action.crawler.ecommerce-jd.getAllData",requestParam.toString());
		return jDETLservice.getAllData(requestParam);
	}
	
	@GetMapping(path = "/tasks/jd/detail")
	public WebDataJD getData(String taskid,String loginname){
		RequestParam rp = new RequestParam();
		tracerLog.addTag("action.crawler.ecommerce-jd.getAllData",rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		return jDETLservice.getAllData(rp);
	}
	
	@PostMapping(path = "/tasks/taobao/detail")
	public WebDataTaoBao getAllDataTaobao(@RequestBody RequestParam requestParam){
		tracerLog.addTag("action.crawler.ecommerce-taobao.getAllData",requestParam.toString());
		return taoBaoETLservice.getAllData(requestParam);
	}
	
	@GetMapping(path = "/tasks/taobao/detail")
	public WebDataTaoBao getDataTaobao(String taskid,String loginname){
		RequestParam rp = new RequestParam();
		tracerLog.addTag("action.crawler.ecommerce-taobao.getAllData",rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		return taoBaoETLservice.getAllData(rp);
	}
	
	@PostMapping(path = "/tasks/suning/detail")
	public WebDataSuning getAllDataSuning(@RequestBody RequestParam requestParam){
		tracerLog.addTag("action.crawler.ecommerce-suning.getAllData",requestParam.toString());
		return suningETLservice.getAllData(requestParam);
	}
	
	@GetMapping(path = "/tasks/suning/detail")
	public WebDataSuning getDataSuning(String taskid,String loginname){
		RequestParam rp = new RequestParam();
		tracerLog.addTag("action.crawler.ecommerce-taobao.getAllData",rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		return suningETLservice.getAllData(rp);
	}
	
	@RequestMapping(value = "/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<E_CommerceTask> findAll(
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

		Page<E_CommerceTask> tasksPage = suningETLservice.getBankTaskByParams(paramMap,currentPage, pageSize);
		
		System.out.println("电商获取到的数据-----"+tasksPage.getContent().size());
		
		PageInfo<E_CommerceTask> pageInfo = new PageInfo<E_CommerceTask>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
	
	
}
