package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardRegular;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardAccountRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardBillRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardCodeRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardUserInfoRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaDebitCardAccountRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaDebitCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaDebitCardRegularRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaDebitCardUserInfoRepository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.ChinaCiticBankParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic","com.microservice.dao.entity.crawler.bank.citicchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic","com.microservice.dao.repository.crawler.bank.citicchina" })
public class ChinaCiticBankService {

	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChinaCiticBankParser chinaCiticBankParser;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private CiticChinaDebitCardHtmlRepository citicChinaDebitCardHtmlRepository;
	@Autowired
	private CiticChinaDebitCardUserInfoRepository citicChinaDebitCardUserInfoRepository;
	@Autowired
	private CiticChinaDebitCardAccountRepository citicChinaDebitCardAccountRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CiticChinaCreditCardAccountRepository citicChinaCreditCardAccountRepository;
	@Autowired
	private CiticChinaCreditCardHtmlRepository citicChinaCreditCardHtmlRepository;
	@Autowired
	private CiticChinaCreditCardUserInfoRepository citicChinaCreditCardUserInfoRepository;
	@Autowired
	private CiticChinaCreditCardCodeRepository citicChinaCreditCardCodeRepository;
	@Autowired
	private CiticChinaDebitCardRegularRepository citicChinaDebitCardRegularRepository;
	@Autowired
	private CiticChinaCreditCardBillRepository citicChinaCreditCardBillRepository;
	
	
	// 登陆
	public void login(BankJsonBean bankJsonBean, TaskBank taskBank) {
		try {
			tracer.addTag("action.login.citicchinabank.start", bankJsonBean.getTaskid());
			WebParam webParam = chinaCiticBankParser.login(bankJsonBean, taskBank);   
			
			if(webParam.getHtml().contains("交易失败"))
			{
				tracer.addTag("action.login.citicchinabank。pwd.ERROR", taskBank.getTaskid());
				taskBank.setDescription(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getDescription());
				taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase());
				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus());
				taskBankRepository.save(taskBank);
			}
			else if(webParam.getHtml().contains("请输入登录密码"))
			{
				tracer.addTag("action.login.citicchinabank。pwd.ERROR", taskBank.getTaskid());
				taskBank.setDescription(BankStatusCode.BANK_LOGIN_PWD_ERROR.getDescription());
				taskBank.setPhase(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhase());
				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_PWD_ERROR.getPhasestatus());
				taskBankRepository.save(taskBank);
			}
			else if(webParam.getHtml().contains("验证码错误"))
			{
				tracer.addTag("action.login.citicchinabank。img.ERROR", taskBank.getTaskid());
				taskBank.setDescription("网络繁忙，请您稍后再试！");
				taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase());
				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus());
				taskBankRepository.save(taskBank);
			}
			else if(webParam.getCode()==200){
				tracer.addTag("action.login.citicchinabank.SUCCESS", taskBank.getTaskid());
				taskBank.setDescription(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getDescription());
				taskBank.setPhase(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhase());
				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus());
				String pageSource = webParam.getHtml();
				int indexOf = pageSource.indexOf("createSessionKey");
				String substring = pageSource.substring(indexOf);
				String substring2 = substring.substring(28);
				String[] split = substring2.split("checkVeriyUrl=");
				String string2 = split[0].toString();
				String substring3 = string2.substring(0, 40);
				
				
				taskBank.setCrawlerHost(bankJsonBean.getIp());
				taskBank.setCrawlerPort(bankJsonBean.getPort());
				taskBank.setTaskid(bankJsonBean.getTaskid());
				taskBank.setError_message(substring3);
				taskBank.setWebdriverHandle(webParam.getWebHandle());
				taskBankRepository.save(taskBank);  
			}
			else {
				tracer.addTag("action.login.citicchinabank.ERROR", taskBank.getTaskid());
				taskBank.setDescription(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getDescription());
				taskBank.setPhase(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhase());
				taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_LOGINNAME_ERROR.getPhasestatus());
				taskBankRepository.save(taskBank);
			}
		} catch (Exception e) {
			tracer.addTag("action.login.citicchinabank.TIMEOUT", taskBank.getTaskid());
			taskBank.setDescription(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription());
			taskBank.setPhase(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase());
			taskBank.setPhase_status(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus());
			taskBankRepository.save(taskBank);
			e.printStackTrace();
		}

	}

	// 个人信息
	public void UserInfocrawler(TaskBank taskBank, BankJsonBean bankJsonBean) {
		try {
			tracer.addTag("action.crawler.userinfo.start", taskBank.getTaskid());
			WebParam<CiticChinaDebitCardUserInfo> webParam = chinaCiticBankParser.userInfocrawler(taskBank,bankJsonBean);
			if (null != webParam.getCiticChinaDebitCardUserInfo()) {
				CiticChinaDebitCardHtml citicChinaDebitCardHtml = new CiticChinaDebitCardHtml();
				citicChinaDebitCardHtml.setHtml(webParam.getHtml());
				citicChinaDebitCardHtml.setPagenumber(1);
				citicChinaDebitCardHtml.setType("UserInfocrawler");
				citicChinaDebitCardHtml.setUrl(webParam.getUrl());
				citicChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
				citicChinaDebitCardHtmlRepository.save(citicChinaDebitCardHtml);
				citicChinaDebitCardUserInfoRepository.save(webParam.getCiticChinaDebitCardUserInfo());
				tracer.addTag("action.crawler.userinfo.SUCCESS", taskBank.getTaskid());
				taskBank.setUserinfoStatus(200);
				taskBankRepository.save(taskBank);
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【个人信息】已采集完成", 200, false,
						taskBank.getTaskid());
			} else {
				tracer.addTag("action.crawler.userinfo.ERROR1", taskBank.getTaskid());
				taskBank.setUserinfoStatus(201);
				taskBankRepository.save(taskBank);
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【个人信息】已采集完成", 201, false,
						taskBank.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.userinfo.TIMEOUT", taskBank.getTaskid());
			taskBank.setUserinfoStatus(201);
			taskBankRepository.save(taskBank);
			taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
					BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【个人信息】已采集完成", 201, false,
					taskBank.getTaskid());
			e.printStackTrace();
		}

	}

	//定期信息
		public void debitCardRegularCrawler(TaskBank taskBank, BankJsonBean bankJsonBean) {
			try {
				tracer.addTag("action.creditCardRegularCrawler.start", bankJsonBean.getTaskid());
				WebParam<CiticChinaDebitCardRegular> webParam = chinaCiticBankParser.debitcardRegular(taskBank,bankJsonBean);
				if(null != webParam)
				{
					if(null != webParam.getList())
					{
//						taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
//								BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(),
//								BankStatusCode.BANK_CRAWLER_SUCCESS.getDescription(), 200, true, taskBank.getTaskid());
						CiticChinaDebitCardHtml citicChinaDebitCardHtml = new CiticChinaDebitCardHtml();
						citicChinaDebitCardHtml.setHtml(webParam.getHtml());
						citicChinaDebitCardHtml.setPagenumber(1);
						citicChinaDebitCardHtml.setType("creditCardRegularCrawler");
						citicChinaDebitCardHtml.setUrl(webParam.getUrl());
						citicChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
						citicChinaDebitCardHtmlRepository.save(citicChinaDebitCardHtml);
						citicChinaDebitCardRegularRepository.saveAll(webParam.getList());
						tracer.addTag("action.crawler.userinfo.SUCCESS", taskBank.getTaskid());
//						taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
//								BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【定期信息】已采集完成", 200, false,
//								taskBank.getTaskid());
						taskBankRepository.save(taskBank);
//						taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
					}    
					else
					{
						tracer.addTag("action.crawler.userinfo.ERROR1", taskBank.getTaskid());
//						taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
//								BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【定期信息】已采集完成", 201, false,
//								taskBank.getTaskid());
						//taskBankRepository.save(taskBank);
						//taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
					}
				}
				else
				{
					tracer.addTag("action.crawler.userinfo.ERROR2", taskBank.getTaskid());
//					taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
//							BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【定期信息】已采集完成", 201, false,
//							taskBank.getTaskid());
					//taskBankRepository.save(taskBank);
					//taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
				}
			} catch (Exception e) {
				tracer.addTag("action.crawler.userinfo.ERROR3", taskBank.getTaskid());
//				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
//						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【定期信息】已采集完成", 201, false,
//						taskBank.getTaskid());
				//taskBankRepository.save(taskBank);
				//taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
				e.printStackTrace();
			}
			
		}
	
	
	// 流水
	public void Accountcrawler(TaskBank taskBank, BankJsonBean bankJsonBean) {
		try {
			tracer.addTag("action.crawler.account.start", taskBank.getTaskid());
			WebParam<CiticChinaDebitCardAccount> webParam = chinaCiticBankParser.accountcrawler(taskBank, bankJsonBean);

			if (null != webParam) {
				if (null != webParam.getList()) {
					tracer.addTag("action.crawler.Accountcrawler.SUCCESS", taskBank.getTaskid());
					
					CiticChinaDebitCardHtml citicChinaDebitCardHtml = new CiticChinaDebitCardHtml();
					citicChinaDebitCardHtml.setHtml(webParam.getHtml());
					citicChinaDebitCardHtml.setPagenumber(1);
					citicChinaDebitCardHtml.setType("Accountcrawler");
					citicChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
					citicChinaDebitCardHtml.setUrl(webParam.getUrl());
					citicChinaDebitCardHtmlRepository.save(citicChinaDebitCardHtml);
					citicChinaDebitCardAccountRepository.saveAll(webParam.getList());
					taskBank.setTransflowStatus(200);
					taskBankRepository.save(taskBank);
					taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
							BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【流水信息】已采集完成", 200, true,
							taskBank.getTaskid());
//					taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
				} else {
					tracer.addTag("action.crawler.Accountcrawler.ERROR1", taskBank.getTaskid());
					taskBank.setTransflowStatus(201);
					taskBankRepository.save(taskBank);
					taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
							BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【流水信息】已采集完成", 201, true,
							taskBank.getTaskid());
//					taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
				}
			} else {
				tracer.addTag("action.crawler.Accountcrawler.ERROR2", taskBank.getTaskid());
				taskBank.setTransflowStatus(201);
				taskBankRepository.save(taskBank);
				taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
						BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【流水信息】已采集完成", 201, true,
						taskBank.getTaskid());
//				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.Accountcrawler.TIMEOUT", taskBank.getTaskid());
			taskBank.setTransflowStatus(201);
			taskBankRepository.save(taskBank);
			taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_SUCCESS.getPhase(),
					BankStatusCode.BANK_CRAWLER_SUCCESS.getPhasestatus(), "数据采集中，【流水信息】已采集完成", 201, true,
					taskBank.getTaskid());
			e.printStackTrace();
		}
		taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
	}
}
