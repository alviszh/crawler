package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.bank.json.IdleInstance;
import com.crawler.mobile.json.StatusCodeLogin;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CibChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/cibchina/debitcard")
public class CibChinaController {
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	
	@Autowired
	private CibChinaService cibChinaService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TracerLog tracerLog;
	
	@Autowired
	private TaskBankRepository taskBankRepository;
	// 登录中间层
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		System.out.println("loginAgent登陆");
		tracerLog.qryKeyValue("兴业银行（储蓄卡）集群的调用...", bankJsonBean.getTaskid());
//		TaskBank taskBank = null;
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	
		try{
			taskBank =  agentService.postAgent(bankJsonBean, "/bank/cibchina/debitcard/login"); 
			
		}catch(RuntimeException e){
			
            System.out.println("CibChinaController.loginAgent.exception="+ e.getMessage());
            return taskBank;
		}

//		TaskBank taskBank =  agentService.postAgent(bankJsonBean, "/bank/cibchina/debitcard/login"); 
		return taskBank;
	}
	// 登录业务
	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		tracerLog.qryKeyValue("兴业银行（储蓄卡）业务登录的调用...", bankJsonBean.getTaskid());
		try {
			taskBank = cibChinaService.login(bankJsonBean);
		} catch (Exception e) {
			
			tracerLog.addTag("兴业银行（储蓄卡），打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
		
	}
	
	// 发送验证码中间层
	@PostMapping(path = "/sendSmsAgent")
	public TaskBank sendSmsAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		System.out.println("crawlerAgent发送短信验证");
		tracerLog.qryKeyValue("兴业银行（储蓄卡）发送验证码集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		try{
			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSms");  
			
		}catch(RuntimeException e){
			tracerLog.addTag("crawlerAgent", "runtimeexception");
			
		}
//		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
		return taskBank;
 
	}
	
	// 发送验证码
	@PostMapping(path = "/sendSms")
	public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("兴业银行（信用卡）发送验证码的业务调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			taskBank = cibChinaService.sendSms(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
	}	
		
	// 验证验证码中间层
	@PostMapping(path = "/verifySmsAgent")
	public TaskBank verifySmsAgent(@RequestBody BankJsonBean bankJsonBean){
		System.out.println("crawlerAgent短信验证");
		tracerLog.qryKeyValue("兴业银行（储蓄卡）验证验证码集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		try{
			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/verifySms");  
			
		}catch(RuntimeException e){
			tracerLog.addTag("crawlerAgent", "runtimeexception");
			
		}
//		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
		return taskBank;
	}
	
	// 验证验证码
	@PostMapping(path = "/verifySms")
	public TaskBank verifySms(@RequestBody BankJsonBean bankJsonBean) {

		tracerLog.qryKeyValue("农业银行（信用卡）验证验证码的业务调用...", bankJsonBean.getTaskid());

		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			taskBank = cibChinaService.verifySms(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
	}
	
	// 爬取和解析中间层
	@PostMapping(path = "/getAllDataAgent")
	public TaskBank getAllDataAgent(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {
		tracerLog.qryKeyValue("兴业银行（储蓄卡）爬取和解析集群的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		try{
			taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/getAllData");  
			
		}catch(RuntimeException e){
			tracerLog.addTag("crawlerAgent", "runtimeexception");
			
		}
//		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cibchina/debitcard/sendSmsCode"); 
		return taskBank;
	}	

	// 爬取和解析	
	@PostMapping(path = "/getAllData")
	public TaskBank getAllData(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("兴业银行（储蓄卡）爬取和解析的业务调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		try {
			taskBank = cibChinaService.getAllData(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			tracerLog.addTag("登录兴业银行，打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
		
	}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cibChinaService.quit(bankJsonBean);
		return taskBank;
	}
	
}
