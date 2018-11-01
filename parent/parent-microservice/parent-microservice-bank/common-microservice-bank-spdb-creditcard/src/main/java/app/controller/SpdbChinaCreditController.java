package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.SpdbChinaCreditService;
import app.service.TaskBankStatusService;;

@RestController
@Configuration
@RequestMapping("/bank/spdbchina/creditcard")
public class SpdbChinaCreditController {
	@Autowired
	private TaskBankStatusService taskBankStatusService; 
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private SpdbChinaCreditService spdbChinaCreditService;
	
	//登陆接口
	@PostMapping(path = "/login")
	public TaskBank login(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("浦发银行（信用卡）业务登录的调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = null;
		
		try {
			taskBank = spdbChinaCreditService.login(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					"系统繁忙，请稍后再试！", 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
			tracerLog.addTag("登录浦发银行（信用卡），打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;	
	}
	
	//发送短信接口
	@PostMapping(path = "/sendSms")
	public TaskBank sendSms(@RequestBody BankJsonBean bankJsonBean) {
		tracerLog.qryKeyValue("浦发银行（信用卡）发送验证码的业务调用...", bankJsonBean.getTaskid());
		TaskBank taskBank = null;
		
		try {
			taskBank = spdbChinaCreditService.sendSms(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					"系统繁忙，请稍后再试！", 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
			tracerLog.addTag("登录浦发银行（信用卡），打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;	
	}
	//验证短信接口
	@PostMapping(path = "/verifySms")
	public TaskBank verifySms(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = null;	
		tracerLog.qryKeyValue("浦发银行（信用卡）验证验证码的业务调用...", bankJsonBean.getTaskid());
		
		try {
			taskBank = spdbChinaCreditService.verifySms(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					"系统繁忙，请稍后再试！", 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
			tracerLog.addTag("登录浦发银行（信用卡），打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
	}
	
	//爬取和解析
	@PostMapping(path = "/getAllData")
	public TaskBank getAllData(@RequestBody BankJsonBean bankJsonBean){
		TaskBank taskBank = null;	
		tracerLog.qryKeyValue("浦发银行（信用卡）爬取和解析的业务调用...", bankJsonBean.getTaskid());
		try {
			taskBank = spdbChinaCreditService.getAllData(bankJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(), 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					"系统繁忙，请稍后再试！", 
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(),true, bankJsonBean.getTaskid());
			tracerLog.addTag("登录浦发银行（信用卡），打开网页获取网页异常",e.toString()); 
		} 
		return taskBank;
	}	
}
