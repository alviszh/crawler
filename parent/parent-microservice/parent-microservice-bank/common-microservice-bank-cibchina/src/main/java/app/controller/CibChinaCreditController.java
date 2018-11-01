package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.mobile.json.StatusCodeLogin;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CibChinaCreditService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/cibchina/creditcard")
public class CibChinaCreditController {
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	
	@Autowired
	private CibChinaCreditService cibChinaCreditService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TracerLog tracerLog;
	
	// 登录中间层
		@PostMapping(path = "/loginAgent")
		public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
			System.out.println("loginAgent登陆");
			tracerLog.qryKeyValue("兴业银行（信用卡）集群的调用...", bankJsonBean.getTaskid());
//			TaskBank taskBank = null;
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
			try{
				taskBank =  agentService.postAgent(bankJsonBean, "/bank/cibchina/creditcard/login"); 
				
			}catch(RuntimeException e){
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
	                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
	                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
	                    BankStatusCode.BANK_AGENT_ERROR.getError_code(),true,bankJsonBean.getTaskid());
	            tracerLog.addTag("CibChinaController.loginAgent.exception", e.getMessage());
	            System.out.println("CibChinaController.loginAgent.exception="+ e.getMessage());
	            return taskBank;
			}

//			TaskBank taskBank =  agentService.postAgent(bankJsonBean, "/bank/cibchina/debitcard/login"); 
			return taskBank;
		}
		// 登录业务
		@PostMapping(path = "/login")
		public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
			tracerLog.qryKeyValue("兴业银行（信用卡）业务登录的调用...", bankJsonBean.getTaskid());
			try {
				taskBank = cibChinaCreditService.login(bankJsonBean);
			} catch (Exception e) {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						"系统繁忙，请稍后再试！", 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
				tracerLog.addTag("兴业银行（信用卡），打开网页获取网页异常",e.toString()); 
			} 
			return taskBank;
			
		}
		
		// 发送验证码中间层
		@PostMapping(path = "/sendSmsAgent")
		public TaskBank sendSmsAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
			System.out.println("crawlerAgent发送短信验证");
			tracerLog.qryKeyValue("兴业银行（信用卡）发送验证码集群的调用...", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
			
			bankJsonBean.setIp(taskBank.getCrawlerHost());
			bankJsonBean.setPort(taskBank.getCrawlerPort());
			bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
			try{
				taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/creditcard/sendSms");  
				
			}catch(RuntimeException e){
				tracerLog.addTag("crawlerAgent", "runtimeexception");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
	                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
	                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
	                    BankStatusCode.BANK_AGENT_ERROR.getError_code(),true,bankJsonBean.getTaskid());
			}
//			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
			return taskBank;
	 
		}
		
		// 发送验证码
		@PostMapping(path = "/sendSms")
		public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean) {
			tracerLog.qryKeyValue("兴业银行（信用卡）发送验证码的业务调用...", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
			try {
				taskBank = cibChinaCreditService.sendSms(bankJsonBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						"系统繁忙，请稍后再试！", 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
				tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
			} 
			return taskBank;
		}	
			
		// 验证验证码中间层
		@PostMapping(path = "/verifySmsAgent")
		public TaskBank verifySmsAgent(@RequestBody BankJsonBean bankJsonBean){
			System.out.println("crawlerAgent短信验证");
			tracerLog.qryKeyValue("兴业银行（信用卡）验证验证码集群的调用...", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
			
			bankJsonBean.setIp(taskBank.getCrawlerHost());
			bankJsonBean.setPort(taskBank.getCrawlerPort());
			bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
			try{
				taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/creditcard/verifySms");  
				
			}catch(RuntimeException e){
				tracerLog.addTag("crawlerAgent", "runtimeexception");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
	                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
	                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
	                    BankStatusCode.BANK_AGENT_ERROR.getError_code(),true,bankJsonBean.getTaskid());
			}
//			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
			return taskBank;
		}
		
		// 验证验证码
		@PostMapping(path = "/verifySms")
		public TaskBank verifySms(@RequestBody BankJsonBean bankJsonBean) {

			tracerLog.qryKeyValue("兴业银行（信用卡）验证验证码的业务调用...", bankJsonBean.getTaskid());

			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
			try {
				taskBank = cibChinaCreditService.verifySms(bankJsonBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						"系统繁忙，请稍后再试！", 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
				tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
			} 
			return taskBank;
		}
		
		// 爬取和解析中间层
		@PostMapping(path = "/getAllDataAgent")
		public TaskBank getAllDataAgent(@RequestBody BankJsonBean bankJsonBean)
				throws IllegalAccessException, NativeException, Exception {
			tracerLog.qryKeyValue("兴业银行（信用卡）爬取和解析集群的调用...", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
			
			bankJsonBean.setIp(taskBank.getCrawlerHost());
			bankJsonBean.setPort(taskBank.getCrawlerPort());
			bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
			try{
				taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/creditcard/getAllData");  
				
			}catch(RuntimeException e){
				tracerLog.addTag("crawlerAgent", "runtimeexception");
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_AGENT_ERROR.getPhase(),
	                    BankStatusCode.BANK_AGENT_ERROR.getPhasestatus(),
	                    BankStatusCode.BANK_AGENT_ERROR.getDescription(),
	                    BankStatusCode.BANK_AGENT_ERROR.getError_code(),true,bankJsonBean.getTaskid());
			}
//			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
			return taskBank;
		}	

		// 爬取和解析	
		@PostMapping(path = "/getAllData")
		public TaskBank getAllData(@RequestBody BankJsonBean bankJsonBean) {
			tracerLog.qryKeyValue("兴业银行（信用卡）爬取和解析的业务调用...", bankJsonBean.getTaskid());
			TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);
			try {
				taskBank = cibChinaCreditService.getAllData(bankJsonBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						"系统繁忙，请稍后再试！", 
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
				tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
			} 
			return taskBank;
			
		}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cibChinaCreditService.quit(bankJsonBean);
		return taskBank;
	}
}
