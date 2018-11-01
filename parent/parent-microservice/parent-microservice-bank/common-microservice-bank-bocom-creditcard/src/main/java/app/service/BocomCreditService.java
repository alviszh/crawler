package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

/**
 * 
 * 项目名称：common-microservice-bank-bocom-creditcard 类名称：BocomCreditcardService
 * 类描述： 创建人：hyx 创建时间：2017年11月24日 上午10:20:06
 * 
 * @version
 */
@Component
public class BocomCreditService extends AbstractChaoJiYingHandler  {

	@Autowired
	private BocomCreditcardLoginAndGetService bocomCreditcardLoginAndGetService;

	@Autowired
	private BocomCreditCrawlerService bocomCreditCrawlerService;

	public TaskBank login(BankJsonBean bankJsonBean) {

		TaskBank taskBank = bocomCreditCrawlerService.login(bankJsonBean);
		
		if(taskBank !=null){
			if (taskBank.getPhase().indexOf(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase()) != -1
					&& taskBank.getPhase_status()
							.indexOf(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus()) != -1) {
				bocomCreditCrawlerService.getAllData(bankJsonBean);
			}
		}
		

		return taskBank;
	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-bocom-creditcard 所属包名：app.service 类描述： 未启用
	 * 创建人：hyx 创建时间：2018年6月21日
	 * 
	 * @version 1 返回值 TaskBank
	 */
	// public TaskBank setCode(BankJsonBean bankJsonBean) throws Exception {
	// TaskBank taskBank =
	// taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
	// taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase());
	// taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus());
	// taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription());
	// taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_DONING.getError_code());
	//
	// taskBank = taskBankRepository.save(taskBank);
	// try {
	// WebDriver driver =
	// bocomCreditcardLoginAndGetService.setSMSCode(bankJsonBean);
	//
	// if (taskBank != null ) {
	//
	// crawlerForNoCode(driver, bankJsonBean);
	//
	// return taskBank;
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// taskBank.setPhase(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase());
	// taskBank.setPhase_status(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus());
	// taskBank.setDescription(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription());
	// taskBank.setError_message(e.getMessage());
	// taskBank.setError_code(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code());
	// taskBank = taskBankRepository.save(taskBank);
	//
	// return taskBank;
	// }
	// return taskBank;
	//
	// }
	

	public TaskBank quit(BankJsonBean bankJsonBean) {
		return bocomCreditcardLoginAndGetService.quit(bankJsonBean);
	}	
}
