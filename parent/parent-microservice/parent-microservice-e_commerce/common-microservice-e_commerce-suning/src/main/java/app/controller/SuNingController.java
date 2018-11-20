package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.TaskStatusCode;
import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.E_CommerceTaskStatusService;
import app.service.SuNingService;

@RestController
@Configuration
@RequestMapping("/ds/suning")
public class SuNingController {

	@Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	@Autowired
	private SuNingService suNingService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private TracerLog tracerLog;
	
	@PostMapping(path = "/sendSMS")
	public E_CommerceTask sendSMS(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {

		tracerLog.output("crawler.e_commerce.SuNingController.sendSMS.taskid", e_CommerceJsonBean.getTaskid());
		E_CommerceTask ecommerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_SEND_CODE_DONING.getPhase(), E_ComerceStatusCode.E_COMMERCE_SEND_CODE_DONING.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_SEND_CODE_DONING.getDescription(),
				E_ComerceStatusCode.E_COMMERCE_SEND_CODE_DONING.getError_code(), false, e_CommerceJsonBean.getTaskid());
		ecommerceTask.setWebsiteType("sn");
		e_CommerceTaskRepository.save(ecommerceTask);
		ecommerceTask = suNingService.sendSMS(e_CommerceJsonBean, ecommerceTask);

		return ecommerceTask;
	}
	
	@PostMapping(path = "/loginAgent")
	public E_CommerceTask loginAgent(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws IllegalAccessException, NativeException, Exception {
		tracerLog.output("crawler.e_commerce.login", e_CommerceJsonBean.getTaskid());   
		E_CommerceTask ecommerceTask = new E_CommerceTask();
		try {
			ecommerceTask =  agentService.postAgent(e_CommerceJsonBean, "/ds/suning/login"); 
		} catch (RuntimeException e) {
			tracerLog.output("SuNingController.loginAgent.exception", e.getMessage());
			ecommerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true, e_CommerceJsonBean.getTaskid());
		}
		return ecommerceTask;
	}
	
	@PostMapping(path = "/login")
	public E_CommerceTask login(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {
		tracerLog.output("crawler.e_commerce.login", e_CommerceJsonBean.getTaskid());
		E_CommerceTask ecommerceTask = e_commerceTaskStatusService.changeStatusLoginDoing(e_CommerceJsonBean);
		ecommerceTask.setWebsiteType("sn");
		e_CommerceTaskRepository.save(ecommerceTask);
		ecommerceTask = suNingService.login(e_CommerceJsonBean, ecommerceTask);
		return ecommerceTask;
	}
	
	@PostMapping(path = "/quit")
	public E_CommerceTask quit(@RequestBody E_CommerceJsonBean e_CommerceJsonBean){
		E_CommerceTask ecommerceTask = suNingService.quitDriver(e_CommerceJsonBean);
		return ecommerceTask;
	}

}
