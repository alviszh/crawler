package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.E_CommerceTaskStatusService;
import app.service.JDLoginAndGetService;
import app.service.JDService;
import app.service.JDfenlei;

@RestController
@Configuration
@RequestMapping("/e_commerce/jd")
public class JDController {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private JDService jDService;

	@Autowired
	private JDLoginAndGetService jDLoginAndGetService;

	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;

	@Autowired
	private E_CommerceTaskStatusService e_CommerceTaskStatusService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private JDfenlei jdfenlei;

	@PostMapping(path = "/loginAgent")
	public E_CommerceTask loginAgent(@RequestBody E_CommerceJsonBean e_CommerceJsonBean)
			throws IllegalAccessException, NativeException, Exception {

		tracerLog.output("京东集群的调用", e_CommerceJsonBean.getTaskid());
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());

		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		/*
		 * e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.
		 * E_COMMERCE_LOGIN_DOING.getPhase(),
		 * E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getPhasestatus(),
		 * E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getDescription(),
		 * E_ComerceStatusCode.E_COMMERCE_LOGIN_DOING.getError_code(), false,
		 * e_CommerceJsonBean.getTaskid());
		 */

		try {
			e_CommerceTask = agentService.postAgent(e_CommerceJsonBean, "/e_commerce/jd/login", 300000L);

		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * e_CommerceTask.setError_message(e.getMessage());
			 * e_CommerceTask.setTesthtml(e_CommerceJsonBean.toString());
			 * e_CommerceTask = e_commerceTaskRepository.save(e_CommerceTask);
			 * e_CommerceTask = e_CommerceTaskStatusService.changeStatus(
			 * E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
			 * E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhasestatus(),
			 * E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
			 * E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true,
			 * e_CommerceJsonBean.getTaskid());
			 */
			return e_CommerceTask;
		}

		return e_CommerceTask;
	}

	@PostMapping(path = "/login")
	public String login(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());

		try {
			jDService.loginCrawler(e_CommerceJsonBean);
		} catch (Exception e) {
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), true, e_CommerceJsonBean.getTaskid());
		}

		return null;
	}

	@PostMapping(path = "/base64ImageAgent")
	public E_CommerceTask loginAgentByQRCode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean)
			throws IllegalAccessException, NativeException, Exception {

		tracerLog.output("京东集群的调用  base64ImageAgent", e_CommerceJsonBean.getTaskid());
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());

		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		try {
			e_CommerceTask =  agentService.postAgent(e_CommerceJsonBean, "/e_commerce/jd/base64Image", 300000L);
			
			e_CommerceTask = e_commerceTaskRepository.save(e_CommerceTask);
			System.out.println("e_CommerceTask========="+e_CommerceTask.toString());
			tracerLog.output("e_CommerceTask",(new Gson()).toJson(e_CommerceTask));


		} catch (Exception e) {
			 e.printStackTrace();
			 e_CommerceTask.setError_message(e.getMessage());
			 e_CommerceTask.setTesthtml(e_CommerceJsonBean.toString());
			 e_CommerceTask = e_commerceTaskRepository.save(e_CommerceTask);
			 e_CommerceTask = e_CommerceTaskStatusService.changeStatus(
			 E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhase(),
			 E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getPhasestatus(),
			 E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getDescription(),
			 E_ComerceStatusCode.E_COMMERCE_AGENT_ERROR.getError_code(), true,
			 e_CommerceJsonBean.getTaskid());
			return e_CommerceTask;
		}

		return e_CommerceTask;
	}

	@PostMapping(path = "/base64Image")
	public E_CommerceTask getQRcode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());
		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		try {
			e_CommerceTask =  jDService.getQRcode(e_CommerceJsonBean);
		} catch (Exception e) {
			e_CommerceTask = e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getError_code(), true,
					e_CommerceJsonBean.getTaskid());
		}

		return e_CommerceTask;
	}

	@PostMapping(path = "/checkQRcodeAgent")
	public E_CommerceTask checkAliQRcodeAgent(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) {
		tracerLog.addTag("crawler.taobao.QRcodeAgent.taskid", e_CommerceJsonBean.getTaskid());

		System.out.println("crawler.taobao.QRcodeAgent +++++" + e_CommerceJsonBean.getTaskid());
		
		tracerLog.output("京东集群的调用", e_CommerceJsonBean.getTaskid());
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());

		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		
		e_CommerceJsonBean.setIp(e_CommerceTask.getCrawlerHost());
		e_CommerceJsonBean.setPort(e_CommerceTask.getCrawlerPort());
		
		e_CommerceTask = agentService.postAgentCombo(e_CommerceJsonBean, "/e_commerce/jd/checkQRcode");
		return e_CommerceTask;
	}

	@PostMapping(path = "/checkQRcode")
	public E_CommerceTask checkQRcode(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) {
		tracerLog.output("crawler.taobao.checkAliQRcode", e_CommerceJsonBean.getTaskid());
		System.out.println("crawler.taobao.checkAliQRcode +++++" + e_CommerceJsonBean.getTaskid());
		
		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		
		try {
			e_CommerceTask = jDService.checkJDQRcode(e_CommerceJsonBean);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.taobao.checkAliQRcode.exception", e.getMessage());
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), true, e_CommerceJsonBean.getTaskid());
		}
		return e_CommerceTask;
	}

	@PostMapping(path = "/sendSmsCodeAgent")
	public E_CommerceTask sendSmsCodeAgent(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {
		tracerLog.output("crawler.jd.sendcode.agent", e_CommerceJsonBean.toString());
		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());
		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());
		e_CommerceTask.setVerificationPhone(e_CommerceJsonBean.getVerfiySMS().trim());
		e_CommerceTask = e_commerceTaskRepository.save(e_CommerceTask);
		e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getPhase(),
				E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getPhasestatus(),
				E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getDescription(),
				E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_DONING.getError_code(), false,
				e_CommerceJsonBean.getTaskid());

		e_CommerceJsonBean.setIp(e_CommerceTask.getCrawlerHost());
		e_CommerceJsonBean.setPort(e_CommerceTask.getCrawlerPort());
		e_CommerceJsonBean.setWebdriverHandle(e_CommerceTask.getWebdriverHandle());

		// 验证短信
		e_CommerceTask = agentService.postAgentCombo(e_CommerceJsonBean, "/ds/jd/setSmsCode");
		return e_CommerceTask;
	}

	@PostMapping(path = "/setSmsCode")
	public E_CommerceTask setSMS(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) throws Exception {

		tracerLog.output("taskid", e_CommerceJsonBean.getTaskid());

		tracerLog.output("crawler.e_commerce.setSmsCode", e_CommerceJsonBean.toString());
		E_CommerceTask e_CommerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		try {
			e_CommerceTask = jDService.verifySmsCrawler(e_CommerceJsonBean);
		} catch (Exception e) {
			e_CommerceTask = e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getPhasestatus(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getError_code(), true,
					e_CommerceJsonBean.getTaskid());
		}

		return e_CommerceTask;
	}

	@PostMapping(path = "/quit")
	public E_CommerceTask quit(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask ecommerceTask = jDLoginAndGetService.quit(e_CommerceJsonBean);
		return ecommerceTask;
	}

	@PostMapping(path = "/fenlei")
	public E_CommerceTask fenlei(@RequestBody E_CommerceJsonBean e_CommerceJsonBean) {
		try {
			jdfenlei.fenlei();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
