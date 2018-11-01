package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.unit.GetBankCardUnit;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
public class BocCrawlerService extends AbstractChaoJiYingHandler implements ICrawlerLogin{

	@Autowired
	private TaskBankRepository taskBankRepository;
	
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	
	@Autowired
	private BocAysncService bocAysncService;
	
		
	
	@Autowired
	private TracerLog tracerLog;

	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);

		bocAysncService.getAllData(bankJsonBean);
		return taskBank;
	}

	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskBank login(BankJsonBean bankJsonBean) {
		// TODO Auto-generated method stub
		try {
			String banktype = GetBankCardUnit.getname(bankJsonBean.getLoginName().trim());
			if (banktype != null) {
				 bocAysncService.login(bankJsonBean);
			} else {
				 bocAysncService.loginChromeByUserName(bankJsonBean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				bocAysncService.loginChromeByUserName(bankJsonBean);

			} catch (Exception e1) {
				e1.printStackTrace();
				
				tracerLog.output("crawler.bank.login.cardnum.exception",
						e.getMessage());
				e.printStackTrace();
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid());
			}
		}
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());

		return taskBank;
	}
}
