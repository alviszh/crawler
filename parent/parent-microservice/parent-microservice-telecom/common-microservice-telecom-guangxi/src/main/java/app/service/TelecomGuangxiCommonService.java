package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;
import app.service.aop.ISmsTwice;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guangxi")
public class TelecomGuangxiCommonService implements ICrawlerLogin,ISms,ISmsTwice{

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomGuangxiService telecomGuangxiService;
	

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);

	}

	
	// 登陆
	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		telecomGuangxiService.login(messageLogin);
		return taskMobile;
	}
	
	// 2获取验证码
	@Async
	@Override
	public TaskMobile sendSmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile = telecomGuangxiService.sendSmsTwice(messageLogin);
		return taskMobile;
	}
	
	// 验证验证码2
	@Async
	@Override
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile = telecomGuangxiService.verifySmsTwice(messageLogin);
		return taskMobile;
	}

	//第一次获取验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile = telecomGuangxiService.sendSms(messageLogin);
		return taskMobile;
	}

	// 第1次验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		taskMobile = telecomGuangxiService.verifySms(messageLogin);
		return taskMobile;
	}
	

	

	// 第二次爬取
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		//亲情号码
		telecomGuangxiService.getfamily1(messageLogin, taskMobile);
		
		// 积分
		try {
			telecomGuangxiService.getScore(messageLogin, taskMobile);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getintegraResult" + e.toString());
		}
		// 业务
		try {
			telecomGuangxiService.getBusiness(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getPointsAndCharges" + e.toString());
		}
		// 电话详单
		try {
				 telecomGuangxiService.getAllCall(messageLogin, taskMobile);
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
		}

		// 短信详单
		try {
				telecomGuangxiService.getSMS(messageLogin, taskMobile);// 成功
		} catch (Exception e) {
			tracer.addTag("parser.crawler.auth", "getSMSThrem" + e.toString());
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
