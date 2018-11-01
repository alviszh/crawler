package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;

@Component
@EnableAsync
public class MidService{
	
	@Autowired
	private IcbcChinaService icbcChinaService;
	@Autowired
	private MidGetDataService midGetDataService;
	@Autowired
	private TracerLog tracer;

	@Async
	public TaskBank login(BankJsonBean bankJsonBean) {
		tracer.addTag("crawler.bank.login.midService.taskid", bankJsonBean.getTaskid());
		TaskBank taskBank = icbcChinaService.login(bankJsonBean);
		if(null != taskBank){
			if(taskBank.getPhase_status().contains("SUCCESS_NEEDSMS")){
				tracer.addTag("crawler.bank.login.midService.loginStatus", "needSMS");
				icbcChinaService.sendSms(bankJsonBean);
			}else if(taskBank.getPhase_status().contains("SUCCESS_NEXTSTEP")){
				tracer.addTag("crawler.bank.login.midService.loginStatus", "loginSuccess_"+bankJsonBean.getCardType());
				midGetDataService.getAllData(bankJsonBean);
			}
		}else{
			tracer.addTag("crawler.bank.login.midService.loginStatus", "登陆失败。");
		}
		return null;
	}

}
