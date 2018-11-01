package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
public class BocService extends AbstractChaoJiYingHandler {

	@Autowired
	private BocCrawlerService bocCrawlerService;

	@Async
	public TaskBank crawler(BankJsonBean bankJsonBean) {

		TaskBank taskBank = bocCrawlerService.login(bankJsonBean);

		if (taskBank != null) {
			if (taskBank.getPhase().indexOf(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase()) != -1 && taskBank
					.getPhase_status().indexOf(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus()) != -1) {
				bocCrawlerService.getAllData(bankJsonBean);
			}
		}

		return taskBank;
	}

}
