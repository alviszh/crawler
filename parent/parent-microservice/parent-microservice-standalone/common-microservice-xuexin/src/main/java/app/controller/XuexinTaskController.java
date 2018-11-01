package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;

import app.commontracerlog.TracerLog;
import app.service.XuexinTaskService;
@RestController
@Configuration
@RequestMapping("/xuexin")
public class XuexinTaskController {

	public static final Logger log = LoggerFactory.getLogger(XuexinTaskController.class);
	
	@Autowired
	private TracerLog tracer;

	@Autowired
	private XuexinTaskService xuexinTaskService;

	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/login")
	public TaskStandalone login(@RequestBody PbccrcJsonBean pbccrcJsonBean){
		
		tracer.qryKeyValue("parser.login.taskid",pbccrcJsonBean.getMapping_id());
		tracer.addTag("parser.login.auth",pbccrcJsonBean.getUsername());
		
		try {
			xuexinTaskService.login(pbccrcJsonBean);
		} catch (Exception e) {
			tracer.addTag("XuexinTaskController.login:" , pbccrcJsonBean.getMapping_id()+"---ERROR:"+e);
			e.printStackTrace();
		}

		return null;
		
	}
	
	
	/**
	 * 获取数据
	 * @param insuranceRequestParameters
	 * @return
	 */
	@PostMapping(value="/getAllData")
	public TaskStandalone crawler(@RequestBody PbccrcJsonBean pbccrcJsonBean){
		
		tracer.qryKeyValue("parser.crawler.taskid",pbccrcJsonBean.getMapping_id());
		tracer.addTag("parser.crawler.auth",pbccrcJsonBean.getUsername());
		
		xuexinTaskService.getAllData(pbccrcJsonBean);
	
		return null;
		
	}
	
	

}
