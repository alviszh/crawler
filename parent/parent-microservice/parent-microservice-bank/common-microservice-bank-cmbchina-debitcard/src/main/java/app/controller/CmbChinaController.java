package app.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xvolks.jnative.exceptions.NativeException;

import com.crawler.bank.json.BankJsonBean;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.CmbChinaService;
import app.service.TaskBankStatusService;

@RestController
@Configuration
@RequestMapping("/bank/cmbchina/debitcard")
public class CmbChinaController extends AgentService{ 

	@Autowired
	CmbChinaService cmbChinaService;
 
	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private AgentService agentService;
	
	@Autowired
	private TaskBankRepository taskBankRepository;

	@Autowired
	private TracerLog tracerLog;

	static String GenIndex = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenIndex.aspx";

	static String genLoginVerifyM2 = "https://pbsz.ebank.cmbchina.com/CmbBank_GenShell/UI/GenShellPC/Login/GenLoginVerifyM2.aspx";

	/**
	 * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/loginAgent")
	public TaskBank loginAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		tracerLog.qryKeyValue("crawler.bank.login", bankJsonBean.getTaskid());   
		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	 
		try {
			taskBank = agentService.postAgent(bankJsonBean, "/bank/cmbchina/debitcard/login"); 
        } catch (Exception e) {
            tracerLog.addTag("CzbChinaController.loginAgent.exception", e.getMessage());
        }
		return taskBank;
	}

	/**
	 * @Des POST 登录接口
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/login")
	public TaskBank logincmbchina(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.login", bankJsonBean.toString()); 
//		TaskBank taskBank = taskBankStatusService.changeStatusLoginDoing(bankJsonBean);	 
		// 打开招行页面输入用户名密码登录 (异步)
//		TaskBank taskBank = 
		cmbChinaService.login(bankJsonBean); 
		return null;
	} 
	
	/**
	 * @Des POST 数据采集代理接口，将采集请求按照taskid 转发和登录步骤的那台机器上运行（为了复用 webdriver）
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/crawlerAgent")
	public TaskBank crawlerAgent(@RequestBody BankJsonBean bankJsonBean) throws  Exception { 
		tracerLog.qryKeyValue("crawler.bank.crawler.agent", bankJsonBean.getTaskid());   
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(), 
//				BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(), 
//				BankStatusCode.BANK_CRAWLER_DOING.getDescription(), 
//				null, false, bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		
		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cmbchina/debitcard/crawler"); 
		return taskBank;
 
	}
	
	/**
	 * @Des POST 数据采集接口
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/crawler")
	public TaskBank crawler(@RequestBody BankJsonBean bankJsonBean) throws Exception{ 
		tracerLog.qryKeyValue("crawler.bank.crawler", bankJsonBean.getTaskid());     
		//无需短信，直接开始爬取（异步）
		TaskBank taskBank = cmbChinaService.getAllData(bankJsonBean);  //这个方法会抛出未捕获的异常 待处理  todo
		return taskBank;
	}
	
	/**
	 * @Des POST 发送短信代理接口 ，将发送短信请求按照taskid 转发和登录步骤的那台机器上运行（为了复用 webdriver）
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/sendSmsCodeAgent")
	public TaskBank sendSmsCodeAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.sendcode.agent", bankJsonBean.toString());  
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_DONING.getPhase(), 
//				BankStatusCode.BANK_SEND_CODE_DONING.getPhasestatus(), 
//				BankStatusCode.BANK_SEND_CODE_DONING.getDescription(), 
//				null, false, bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		  
		// 发送短信 (同步)
		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cmbchina/debitcard/sendSmsCode");  
		return taskBank;
	} 
	
	/**
	 * @Des POST 发送短信验证接口
	 * @param bankJsonBean
	 */ 
	@PostMapping(path = "/sendSmsCode")
	public TaskBank sendSmsCode(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.sendcode", bankJsonBean.toString());   
		// 发送短信(同步)
		TaskBank taskBank = cmbChinaService.sendSms(bankJsonBean); 
		return taskBank;
	} 
 
	/**
	 * @Des POST 短信验证代理接口 ，将短信验证请求按照taskid 转发和登录步骤的那台机器上运行（为了复用 webdriver）
	 * @param bankJsonBean
	 * @param 请求路径
	 */ 
	@PostMapping(path = "/smsverfiyAgent")
	public TaskBank smsverfiyAgent(@RequestBody BankJsonBean bankJsonBean) throws Exception {
		tracerLog.qryKeyValue("crawler.bank.smsverfiy.agent", bankJsonBean.toString());  
//		TaskBank taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhase(), 
//				BankStatusCode.BANK_VALIDATE_CODE_DONING.getPhasestatus(), 
//				BankStatusCode.BANK_VALIDATE_CODE_DONING.getDescription(), 
//				null, false, bankJsonBean.getTaskid());
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		bankJsonBean.setIp(taskBank.getCrawlerHost());
		bankJsonBean.setPort(taskBank.getCrawlerPort());
		bankJsonBean.setWebdriverHandle(taskBank.getWebdriverHandle());
		  
		// 验证短信(同步)
		taskBank =  agentService.postAgentCombo(bankJsonBean, "/bank/cmbchina/debitcard/smsverfiy");  
		return taskBank; 
	}

	/**
	 * @Des POST 短信验证接口 
	 * @param bankJsonBean 
	 */ 
	@PostMapping(path = "/smsverfiy")
	public TaskBank smsverfiy(@RequestBody BankJsonBean bankJsonBean)
			throws IllegalAccessException, NativeException, Exception {
		tracerLog.qryKeyValue("crawler.bank.smsverfiy", bankJsonBean.toString());  
		TaskBank taskBank = cmbChinaService.verifySms(bankJsonBean); 
		return taskBank;
	}
	
	@PostMapping(path = "/quit")
	public TaskBank quit(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = cmbChinaService.quit(bankJsonBean);
		return taskBank;
	}
	
	

	//内部测试接口
	@GetMapping(path = "/getHandles")
	public Set<String> getHandles(){
		return cmbChinaService.getHandles();
	}
	
	//内部测试接口
	@GetMapping(path = "/getCurrentHandle")
	public String getCurrentHandle(){
		return cmbChinaService.getCurrentHandle();
	}
	
	

}
