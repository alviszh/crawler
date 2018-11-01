package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISmsTwice;

@RestController
@Configuration
@RequestMapping("/carrier")
public class EurekaCmccController {

	@Autowired
	private ISmsTwice crawlerSmsTwiceTracer;

	@Autowired
	private ICrawlerLogin crawlerLoginTracer;

	@Autowired
	private TracerLog tracer;

	/**
	 * @Description: 发送登录随机短信接口
	 * @param messageLogin
	 * @return
	 */
	@PostMapping(value = "/cmccsendSMS")
	public TaskMobile sendSMS(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.login.sendSMS", messageLogin.getTask_id());
		tracer.addTag("parser.login.sendSMS.auth", messageLogin.getName());
		return crawlerSmsTwiceTracer.sendSms(messageLogin);
	}

	/**
	 * @Description: 登录接口
	 * @param messageLogin
	 * @return
	 */
	@PostMapping(value = "/cmcclogin")
	public TaskMobile login(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		crawlerLoginTracer.login(messageLogin);
		return new TaskMobile();
	}

	/**
	 * @Description: 第二次验证接口
	 * @param messageLogin
	 * @return
	 */
	@PostMapping(value = "/cmccsecondAttestation")
	public TaskMobile secondAttestation(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.login.attestation", messageLogin.getTask_id());
		tracer.addTag("parser.login.attestation.auth", messageLogin.getName());
		crawlerSmsTwiceTracer.verifySmsTwice(messageLogin);
		return new TaskMobile();
	}

	/**
	 * @Description: 发送第二次验证随机短信接口
	 * @param messageLogin
	 * @return
	 */
	@PostMapping(value = "/cmccsendVerifySMS")
	public TaskMobile sendVerifySMS(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.login.sendVerifySMS", messageLogin.getTask_id());
		tracer.addTag("parser.login.sendVerifySMS.auth", messageLogin.getName());
		crawlerSmsTwiceTracer.sendSmsTwice(messageLogin);
		return new TaskMobile();

	}

	/**
	 * @Description: 爬取总接口
	 * @param messageLogin
	 * @return
	 */
	@PostMapping(value = "/cmccGetAllData")
	public String getAllData(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		crawlerLoginTracer.getAllData(messageLogin);
		return "SUCCESS";

	}

}
