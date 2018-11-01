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

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月31日 下午5:51:46 
 */
@Component
public class CmbcChinaCrawlerAllService implements ICrawler{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CmbcChinaCrawlerService cmbcChinaCrawlerService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			//爬取用户信息
			cmbcChinaCrawlerService.getUserInfo(taskBank);
			//调用流水爬取方法
			cmbcChinaCrawlerService.getTransflow(taskBank);
		} catch (Exception e) {
			tracer.addTag("爬取用户信息和流水信息的过程中出现异常，详细内容为：",ExUtils.getEDetail(e));
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(), BankStatusCode.BANK_USERINFO_ERROR.getDescription(),taskBank.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
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
