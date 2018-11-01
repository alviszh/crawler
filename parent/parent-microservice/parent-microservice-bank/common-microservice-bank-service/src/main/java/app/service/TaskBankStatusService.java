package app.service;

import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.impl.CrawlerImpl;

/**
 * @Des 更改task_bank表状态
 * @author zz
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic"})
public class TaskBankStatusService {
	
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private CrawlerImpl iCrawler;
	/**
	 * @Des 改变task_bank状态
	 * @author meidi
	 * @param phase
	 * @param phasestatus
	 * @param description
	 * @param error_code
	 * @param taskid
	 * @return
	 */
	public TaskBank changeStatusbyWebdriverHandle(String phase, String phasestatus, String description, Integer error_code,
			boolean finished, String taskid ,String webdriverHandle) {
		tracerLog.addTag("change task status", description);
		
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		taskBank.setPhase(phase);
		taskBank.setPhase_status(phasestatus);
		taskBank.setDescription(description);
		taskBank.setError_code(error_code);
		taskBank.setWebdriverHandle(webdriverHandle);
		if(finished){
			taskBank.setFinished(finished);
		}
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
	/**
	 * @Des 改变task_bank状态
	 * @author zz
	 * @param phase
	 * @param phasestatus
	 * @param description
	 * @param error_code
	 * @param taskid
	 * @return
	 */
	public TaskBank systemClose(boolean finished, String taskid) {
		tracerLog.addTag("systemClose", taskid); 
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid); 
		taskBank.setError_code(BankStatusCode.SYSTEM_QUIT.getError_code());
		taskBank.setError_message(BankStatusCode.SYSTEM_QUIT.getDescription());
		if(finished){
			taskBank.setFinished(finished);
		}
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
 
	/**
	 * @Des 改变task_bank状态
	 * @author zz
	 * @param phase
	 * @param phasestatus
	 * @param description
	 * @param error_code
	 * @param taskid
	 * @return
	 */
	public TaskBank changeStatus(String phase, String phasestatus, String description, Integer error_code,
			boolean finished, String taskid ) {
		tracerLog.addTag("change task status", description);
		
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		taskBank.setPhase(phase);
		taskBank.setPhase_status(phasestatus);
		taskBank.setDescription(description);
		taskBank.setError_code(error_code);
		if(finished){
			taskBank.setFinished(finished);
		}
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
	public TaskBank changeStatus(String phase, String phasestatus, String description, Integer error_code,
			boolean finished, String taskid,String cookies) {
		tracerLog.addTag("change task status", description);
		
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		taskBank.setCookies(cookies);
		taskBank.setPhase(phase);
		taskBank.setPhase_status(phasestatus);
		taskBank.setDescription(description);
		taskBank.setError_code(error_code);
		if(finished){
			taskBank.setFinished(finished);
		}
		taskBankRepository.save(taskBank);
		return taskBank;
	}
	
	/**
	 * @Des 登录准备 ,此方法适用于 需要winio 的请看下，在task表中存储微服务实例的IP 和 端口
	 * @param bankJsonBean
	 * @return
	 */
	public TaskBank changeStatusLoginDoing(BankJsonBean bankJsonBean){
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(null == taskBank){
			throw new RuntimeException("Entity bean TaskBank is null ! taskid>>"+bankJsonBean.getTaskid()+"<<");
		}
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_DOING.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_DOING.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_DOING.getDescription());
		taskBank.setError_code(BankStatusCode.BANK_LOGIN_DOING.getError_code());
		Gson gson = new Gson();
		taskBank.setTesthtml(gson.toJson(bankJsonBean));
		taskBank.setBankType(bankJsonBean.getBankType());
		System.out.println("cardTyep ==============================>"+bankJsonBean.getCardType());
		taskBank.setCardType(bankJsonBean.getCardType());
		taskBank.setLoginType(bankJsonBean.getLoginType());
		System.out.println("loginName ==============================>"+bankJsonBean.getLoginName());
		taskBank.setLoginName(bankJsonBean.getLoginName());
		taskBank.setCrawlerHost(bankJsonBean.getIp());
		taskBank.setCrawlerPort(bankJsonBean.getPort());
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
	/**
	 * @Des 登录成功，cookie入库
	 * @author zz
	 * @param <T>
	 * @param driver
	 * @param domain
	 * @return
	 */
	public <T> void transforCookie(WebDriver driver, String domain, TaskBank taskBank, T params){
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();
		for(org.openqa.selenium.Cookie cookie : cookiesDriver){
			Cookie cookieWebClient = new Cookie(domain, cookie.getName(), cookie.getValue());
			webClient.getCookieManager().addCookie(cookieWebClient);
		}
		
		String cookieJson = CommonUnit.transcookieToJson(webClient);
		System.out.println("cookieJson============="+cookieJson);
		if(null != params){
			Gson gson=new Gson();
			String param = gson.toJson(params);
			taskBank.setParam(param);
		}
		taskBank.setCookies(cookieJson);
		taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription());
		taskBankRepository.save(taskBank);

	}

	/**
	 * @Des 判断是否正在爬取
	 * @author zz
	 * @param taskid
	 * @return
	 */
	public boolean isDoing(String taskid) {
		
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		
		if(taskBank.getPhase().equals("CRAWLER") && taskBank.getPhase_status().equals("DOING")){
			return true;
		}
		return false;
	}
	
	/**
	 * @Des 爬取完成
	 * @author wpy
	 * @param taskid
	 */
	public TaskBank changeTaskBankFinish(String taskid) {
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if(taskBank==null){
			throw new RuntimeException("RuntimeException function changeTaskBankFinish taskBank==null");
		} 
		taskBank.setPhase(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase());
		taskBank.setPhase_status(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus());
		taskBank.setDescription(BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription());
		taskBank.setFinished(true);
		taskBank = taskBankRepository.save(taskBank); 
		
		iCrawler.getAllDataDone(taskid);
		
		return taskBank;
	}
	
	
	/**   
	  *    
	  * 项目名称：common-microservice-bank-service  
	  * 所属包名：app.service
	  * 类描述：   根据两个爬取的状态码 判断是否爬取完成
	  * 创建人：hyx 
	  * 创建时间：2017年11月24日 
	  * @version 1  
	  * 返回值    TaskBank
	  */
	public TaskBank changeTaskBankFinishByStatus(String taskid) {
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if(taskBank==null){
			throw new RuntimeException("RuntimeException function changeTaskBankFinish taskBank==null");
		} 
		
		if(taskBank.getTransflowStatus()!=null && taskBank.getUserinfoStatus()!=null){
			taskBank.setPhase(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus());
			taskBank.setDescription(BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription());
			taskBank.setFinished(true);
			taskBank = taskBankRepository.save(taskBank); 
		}
		
		return taskBank;
	}
	
	/**   
	  *    
	  * 项目名称：common-microservice-bank-service  
	  * 所属包名：app.service
	  * 类描述：   根据两个爬取的状态码 判断是否爬取完成
	  * 创建人：hyx 
	  * 创建时间：2017年11月24日 
	  * @version 1  
	  * 返回值    TaskBank
	  */
	public TaskBank changeTaskBankFinishByStatusError(String taskid) {
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if(taskBank==null){
			throw new RuntimeException("RuntimeException function changeTaskBankFinish taskBank==null");
		} 
		
		if(taskBank.getTransflowStatus()!=null && taskBank.getUserinfoStatus()!=null){
			taskBank.setPhase(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus());
			taskBank.setDescription(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription());
			taskBank.setFinished(true);
			taskBank = taskBankRepository.save(taskBank); 
		}
		
		return taskBank;
	}
	
	//更改用户信息状态
	public TaskBank updateTaskBankUserinfo(Integer code, String description, String taskid){
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if(taskBank==null){
			throw new RuntimeException("RuntimeException function updateTaskBankUserinfo taskBank==null");
		}
		taskBank.setUserinfoStatus(code);
		taskBank.setDescription(description);
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
	//更改流水信息状态
	public TaskBank updateTaskBankTransflow(Integer code, String description, String taskid){ 
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if(taskBank==null){
			throw new RuntimeException("RuntimeException function updateTaskBankTransflow taskBank==null");
		} 
		taskBank.setTransflowStatus(code);
		taskBank.setDescription(description);
		taskBank = taskBankRepository.save(taskBank);
		return taskBank;
	}
	
//	// list转为string
//	public static String listToString(List<String> stringList) {
//		if (stringList == null) {
//			return null;
//		}
//		StringBuilder result = new StringBuilder();
//		boolean flag = false;
//		for (String string : stringList) {
//			if (flag) {
//				result.append(",");
//			} else {
//				flag = true;
//			}
//			result.append(string);
//		}
//		return result.toString();
//	}
}
