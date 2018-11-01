package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
@EnableAsync
public class MidGetDataService implements ICrawler{
	
	@Autowired
	private IcbcChinaService icbcChinaService;
	@Autowired
	private TracerLog tracer;

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		tracer.addTag("crawler.bank.login.midService.getAllData.taskid", bankJsonBean.getTaskid());
		if(bankJsonBean.getCardType().contains("DEBIT_CARD")){
			//开始爬取
			icbcChinaService.getData(bankJsonBean);
		}else if(bankJsonBean.getCardType().contains("CREDIT_CARD")){
			icbcChinaService.getCreditData(bankJsonBean);
		}
		return null;
	}
	
	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
