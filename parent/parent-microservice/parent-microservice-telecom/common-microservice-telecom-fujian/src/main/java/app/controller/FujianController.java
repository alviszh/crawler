package app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.TelecomCommon;
import app.service.aop.ICrawler;

@RestController
@RequestMapping("/carrier")
public class FujianController implements ICrawler {
	@Autowired
	private TelecomCommon telecomCommonService1;
//	@Autowired
//	private TelecomCommonLoginService telecomCommonService;

//	@RequestMapping(value = "/login", method = { RequestMethod.POST })
//	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
//
//		tracer.addTag("Telecomchongqing1Controller.login", messageLogin.getTask_id());
//
//		tracer.addTag("parser.login.taskid", messageLogin.getTask_id());
//		tracer.addTag("parser.login.auth", messageLogin.getName());
//
//		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
//
//		TaskMobile taskMobile = telecomCommonService.login(messageLogin);
//
//		result.setData(taskMobile);
//		return result;
//
//	}

	@Autowired
	private TracerLog tracer;

	@RequestMapping(value = "/getphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> getYzm(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService1.findtaskMobile(messageLogin.getTask_id().trim());
		// 调用发送验证码业务方法
		telecomCommonService1.sendSms(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/setphonecode", method = { RequestMethod.POST })
	public ResultData<TaskMobile> setYzm(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService1.findtaskMobile(messageLogin.getTask_id().trim());
		// 调用验证验证码业务方法
		telecomCommonService1.verifySms(messageLogin);

		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/crawler", method = { RequestMethod.POST })
	public TaskMobile getAllData(@RequestBody MessageLogin messageLogin) {
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getName());
		TaskMobile taskMobile = telecomCommonService1.findtaskMobile(messageLogin.getTask_id().trim());
		// 准备开始
		taskMobile.setFamilyMsgStatus(200);
		telecomCommonService1.save(taskMobile);

		// 历史月账单信息
		SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
		Calendar calendar22 = Calendar.getInstance();
		calendar22.setTime(new Date());
		List<String> list22 = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list22.add(dateFormat22.format(calendar22.getTime()));
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list22.size(); i++) {
			String monthdate = list22.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService1.crawlerMonthBillHistory(messageLogin, year, i);
		}
		
		// 我的余额
		telecomCommonService1.crawlerWdyue(messageLogin);
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
