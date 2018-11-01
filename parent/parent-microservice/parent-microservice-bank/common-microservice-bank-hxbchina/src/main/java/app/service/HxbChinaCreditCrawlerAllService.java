package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import app.service.aop.ICrawler;

@Component
public class HxbChinaCreditCrawlerAllService implements ICrawler {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private HxbChinaCreditCrawlerService creditCrawlerService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	public String creditAcType;
	public String billDay;

	// 爬取所有的数据
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		// 爬取用户基本信息
		try {
			creditCrawlerService.getUserInfo(bankJsonBean, taskBank);
		} catch (Exception e) {
			tracer.addTag("爬取用户信息和流水信息的过程中出现异常，详细内容为：", ExUtils.getEDetail(e));
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(),
					BankStatusCode.BANK_USERINFO_ERROR.getDescription(), taskBank.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(),
					BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
		}
		return taskBank;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
