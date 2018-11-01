package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.domain.json.Result;
import com.crawler.mobile.json.ResultData;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.ReportData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.qq.TaskQQ;
import com.microservice.dao.repository.crawler.qq.TaskQQRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.QQService;
import app.service.TaskQQStatusService;

@RestController
@Configuration
@RequestMapping("/qq")
public class QQController {

	@Autowired
	private TracerLog tracer;
	@Autowired
    private AgentService agentService;
	@Autowired
	private QQService qQService;
	@Autowired
	private TaskQQRepository taskQQRepository;
	@Autowired
	private TaskQQStatusService taskQQStatusService;
	@Autowired
	private TracerLog tracerLog;

	@PostMapping(path = "/loginAgent")
	public TaskQQ loginAgent(@RequestBody PbccrcJsonBean pbccrcJsonBean)throws Exception {
	        String mapping_id = pbccrcJsonBean.getMapping_id();
	        tracerLog.qryKeyValue("mappingId", mapping_id);
	        tracerLog.addTag("crawler.pbccrc.getCreditAgent", pbccrcJsonBean.getMapping_id());
	        TaskQQ taskQQ = taskQQStatusService.changeStatusLoginDoing(pbccrcJsonBean);
	        try {
	             taskQQ = agentService.postAgent(pbccrcJsonBean, "/qq/login");
	            tracerLog.addTag("getCreditAgent.result", taskQQ.toString());
	            System.out.println("getCreditAgent.result="+ taskQQ.toString());

	        }  catch (RuntimeException e) {
	            tracerLog.qryKeyValue("RuntimeException", "没有闲置的实例");
	           /* reportResult = new Result<ReportData>();
	            ReportData reportData = new ReportData("3", "系统繁忙，请稍后再试", null, null);
	            reportResult.setData(reportData);*/
	            tracerLog.addTag("PbccrcController.getCreditAgent.exception", e.getMessage());
	            System.out.println("PbccrcController.getCreditAgent.exception="+ e.getMessage());
//	            return gson.toJson(reportResult);
	        }
	        return taskQQ;
	}
	
	@PostMapping(path="/login")
	public TaskQQ login(@RequestBody PbccrcJsonBean pbccrcJsonBean){
		//ResultData<TaskQQ> resultData = new ResultData<TaskQQ>();
		tracer.qryKeyValue("parser.login.taskid",pbccrcJsonBean.getMapping_id());
		tracer.addTag("parser.login.auth",pbccrcJsonBean.getUsername());
		//String taskid=pbccrcJsonBean.getMapping_id();
		TaskQQ taskqq = qQService.login(pbccrcJsonBean);
		return taskqq;
		
	}
	
	
	@PostMapping(path = "/crawlerAgent")
	public TaskQQ crawlerAgent(@RequestBody PbccrcJsonBean pbccrcJsonBean) throws  Exception {
		TaskQQ taskqq = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		pbccrcJsonBean.setIp(taskqq.getCrawlerHost());
		pbccrcJsonBean.setPort(taskqq.getCrawlerPort());
		taskqq = agentService.postAgentCombo(pbccrcJsonBean, "/qq/crawler");
		return taskqq;
	}
	
	@PostMapping(path="/crawler")
	public TaskQQ crawler(@RequestBody PbccrcJsonBean pbccrcJsonBean){
		
		tracer.qryKeyValue("parser.login.taskid",pbccrcJsonBean.getMapping_id());
		tracer.addTag("parser.login.auth",pbccrcJsonBean.getUsername());
		TaskQQ taskQQ = qQService.getAllData(pbccrcJsonBean);
		return taskQQ;
		
	}
	
	 /**
     * 系统退出，释放资源
     * @param pbccrcJsonBean
     */
    @PostMapping(path = "/quit")
    public void quit(@RequestBody PbccrcJsonBean pbccrcJsonBean){
    	qQService.quit(pbccrcJsonBean);
    }
}
