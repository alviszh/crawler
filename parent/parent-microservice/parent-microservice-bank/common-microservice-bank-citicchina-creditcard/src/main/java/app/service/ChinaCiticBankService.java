package app.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardCode;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardUserInfo;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardAccountRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardBillRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardCodeRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.citicchina.CiticChinaCreditCardUserInfoRepository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.ChinaCiticCreditCardParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic",
		"com.microservice.dao.entity.crawler.bank.citicchina" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic",
		"com.microservice.dao.repository.crawler.bank.citicchina" })
public class ChinaCiticBankService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChinaCiticCreditCardParser chinaCiticCreditBankParser;
	@Autowired
	private TaskBankRepository taskBankRepository;
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
	private CiticChinaCreditCardBillRepository citicChinaCreditCardBillRepository;

	// 登陆信用卡htmlunit
	public void loginCreditCardHtmlunit(BankJsonBean bankJsonBean, TaskBank taskBank, String fileSavePath) {
		try {
			tracer.addTag("action.loginCreditCard.start", taskBank.getTaskid());
			WebParam webParam = chinaCiticCreditBankParser.loginCreditCardHtmlunit(bankJsonBean, taskBank,
					fileSavePath);
			if (null != webParam) {
				if (webParam.getHtml().contains("手机格式错误，请重新输入")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getDescription(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), false,
							bankJsonBean.getTaskid(), webParam.getWebHandle());
					tracer.addTag("action.loginCreditCard.ERROR。time", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("密码由6-15位数字和字母组成")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getDescription(),
							BankStatusCode.BANK_LOGIN_LOGINNAME_PWD_ERROR.getError_code(), false,
							bankJsonBean.getTaskid(), webParam.getWebHandle());
					tracer.addTag("action.loginCreditCard.ERROR.pwd", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("为了更好地保护您的账户安全，请通过短信验证")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
//					taskBank.setWebdriverHandle(bankJsonBean.getWebdriverHandle());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("短信验证码错误，请重新输入")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), "短信验证码错误，请重新输入",
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					tracer.addTag("action.login.code.ERROR", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("图形验证码输入错误")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), "网络繁忙,请稍后再试",
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					tracer.addTag("action.login.img", taskBank.getTaskid());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					taskBankRepository.save(taskBank);
				} 
				else if (webParam.getHtml().contains("用户名或密码错误")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), "用户名或密码错误，若错误超过5次则账户将被限制",
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					tracer.addTag("action.login.name.pwd.ERROR", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				}
				
				else if (webParam.getHtml().contains("请求过于频繁")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), "请求过于频繁,请休息会吧",
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					tracer.addTag("action.login.times.more", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("短信发送频率过大")) {
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskBank.setCookies(cookieString);
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(), "短信发送频率过大，请在10分钟后重试",
							BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					tracer.addTag("action.login.getsms.more", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else if (webParam.getHtml().contains("短信发送成功")) {
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskBank.setCookies(cookieString);
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					Thread.sleep(2000);
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setDescription("短信验证码已发送到您的手机："+webParam.getWebHandle()+"，请注意查收");
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					taskBank.setBankType(bankJsonBean.getBankType());
					tracer.addTag("action.login.getsms.success", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				}

				else if (webParam.getHtml().contains("首页-我的账户")) {
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhase(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getDescription(),
							BankStatusCode.BANK_LOGIN_SUCCESS_NEEDSMS.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					Thread.sleep(2000);
					taskBankStatusService.changeStatusbyWebdriverHandle(
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getDescription(),
							BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid(),
							webParam.getWebHandle());
					taskBank.setDescription("短信验证码已发送到您的手机："+webParam.getWebHandle()+"，请注意查收");
					taskBank.setCrawlerHost(bankJsonBean.getIp());
					taskBank.setCrawlerPort(bankJsonBean.getPort());
					taskBank.setBankType(bankJsonBean.getBankType());
					taskBank.setTaskid(bankJsonBean.getTaskid());
					tracer.addTag("action.login.SUCCESS", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				}
			} else {
				taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
						BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 201, false, taskBank.getTaskid());
				tracer.addTag("action.login.TIMEOUT1", taskBank.getTaskid());
				taskBankRepository.save(taskBank);
			}
		} catch (Exception e) {
			taskBankStatusService.changeStatus(BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhase(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getPhasestatus(),
					BankStatusCode.BANK_LOGIN_TIMEOUT_ERROR.getDescription(), 201, false, taskBank.getTaskid());
			tracer.addTag("action.login.TIMEOUT2", taskBank.getTaskid());
			taskBankRepository.save(taskBank);
			e.printStackTrace();
		}

	}

	// 发送验证码htmlunit
	public void creditcardSaveCodeHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) {
		try {
			tracer.addTag("action.creditcard.SaveCode", taskBank.getTaskid());
			CiticChinaCreditCardCode c = new CiticChinaCreditCardCode();
			c.setCode(bankJsonBean.getVerification());
			c.setTaskid(bankJsonBean.getTaskid());
			citicChinaCreditCardCodeRepository.save(c);
			WebParam webParam = chinaCiticCreditBankParser.creditcardSaveCodeHtmlunit(taskBank, bankJsonBean);
			if (null != webParam) {
				if (webParam.getHtml().contains("首页-我的账户")) {
					Document doc = Jsoup.parse(webParam.getHtml());
					String elementsByClass = doc.getElementsByClass("title").get(0).getElementsByTag("p").text()
							.substring(3);
					// System.out.println(elementsByClass);
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskBank.setCookies(cookieString);
					taskBank.setParam(elementsByClass);
					taskBankRepository.save(taskBank);
					tracer.addTag("action.creditcard.SaveCode。SUCCESS", taskBank.getTaskid());
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getError_code(), false, bankJsonBean.getTaskid());
				} else if (webParam.getHtml().contains("重新获取")) {
					System.out.println("验证码失效，重新获取");
					tracer.addTag("action.creditcard.SaveCode。TIMEOUT", taskBank.getTaskid());
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_SUCCESS.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(), "验证码失效,登陆失败",
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
					taskBank.setParam(webParam.getHtml());
					taskBankRepository.save(taskBank);
				} else {
					tracer.addTag("action.creditcard.ERROR", taskBank.getTaskid());
					taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhase(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getPhasestatus(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getDescription(),
							BankStatusCode.BANK_VALIDATE_CODE_ERROR.getError_code(), false, bankJsonBean.getTaskid());
					taskBank.setParam(webParam.getHtml());
					taskBankRepository.save(taskBank);
				}
			} else {
				// 验证码为空
				tracer.addTag("action.creditcard.SaveCode。NULL", taskBank.getTaskid());
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getPhase(),
						BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getPhasestatus(), "验证码未输入，登陆失败",
						BankStatusCode.BANK_VALIDATE_CODE_ERROR3.getError_code(), false, bankJsonBean.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.creditcard.SaveCode。TIMEOUT", taskBank.getTaskid());
			taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhase(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getPhasestatus(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getDescription(),
					BankStatusCode.BANK_VALIDATE_CODE_ERROR1.getError_code(), false, bankJsonBean.getTaskid());
			e.printStackTrace();
		}

	}

	// 个人信息htmlunit
	public void creditCardUserInfoCrawlerHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) {
		try {
			WebParam<CiticChinaCreditCardUserInfo> webParam = chinaCiticCreditBankParser
					.creditCardUserInfoCawlerHtmlunit(taskBank, bankJsonBean);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("UserInfo.SUCCESS");
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);
					citicChinaCreditCardUserInfoRepository.save(webParam.getCiticChinaCreditCardUserInfo());
					taskBankStatusService.updateTaskBankUserinfo(200, "【个人信息】爬取完成", taskBank.getTaskid());
					tracer.addTag("action.crawler.Accountcrawler.SUCCESS", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				} else {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("UserInfo.ERROR");
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);

					taskBankStatusService.updateTaskBankUserinfo(201, "【个人信息】爬取完成", taskBank.getTaskid());
					tracer.addTag("action.crawler.Accountcrawler.ERROR", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
				}
			} else {
				taskBankStatusService.updateTaskBankUserinfo(201, "【个人信息】爬取完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.Accountcrawler.TIMEOUT1", taskBank.getTaskid());
				taskBankRepository.save(taskBank);
			}
		} catch (Exception e) {
			taskBankStatusService.updateTaskBankUserinfo(201, "【个人信息】爬取完成", taskBank.getTaskid());
			tracer.addTag("action.crawler.Accountcrawler.TIMEOUT2", taskBank.getTaskid());
			taskBankRepository.save(taskBank);
			e.printStackTrace();
		}

	}

	// 流水htmlunit
	public void creditCardAccountCrawlerHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) {
		int time = 0;
		WebParam<CiticChinaCreditCardAccount> webParam = null;
		try {
			for (int i = 0; i < 7; i++) {
				webParam = chinaCiticCreditBankParser.creditCardAccountCrawlerHtmlunit(taskBank, bankJsonBean, i);
				if (null != webParam.getList()) {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("AccountCawler.SUCCESS" + i);
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);
					citicChinaCreditCardAccountRepository.saveAll(webParam.getList());
					// citicChinaCreditCardBillRepository.saveAll(webParam.getList());
					tracer.addTag("action.crawler.Accountcrawler.SUCCESS"+i, taskBank.getTaskid());
					taskBankRepository.save(taskBank);
					time++;
				} else {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("AccountCawler.ERROR" + i);
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);
					tracer.addTag("action.crawler.Accountcrawler.ERROR"+i, taskBank.getTaskid());
				}
			}
			if (time > 0) {
				tracer.addTag("action.crawler.Accountcrawler.SUCCESS", taskBank.getTaskid());
				taskBankStatusService.updateTaskBankTransflow(200, "【账单明细】爬取完成", taskBank.getTaskid());
				taskBankRepository.save(taskBank);
				// taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			} else {
				tracer.addTag("action.crawler.Accountcrawler.ERROR", taskBank.getTaskid());
				taskBankStatusService.updateTaskBankTransflow(201, "【账单明细】爬取完成", taskBank.getTaskid());
				taskBankRepository.save(taskBank);
				// taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.Accountcrawler.TimeOut2", taskBank.getTaskid());
			taskBankStatusService.updateTaskBankTransflow(201, "【账单明细】爬取完成", taskBank.getTaskid());
			taskBankRepository.save(taskBank);
			// taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			e.printStackTrace();
		}
	}

	public void creditCardBillCrawlerHtmlunit(TaskBank taskBank, BankJsonBean bankJsonBean) {
		int time = 0;
		WebParam<CiticChinaCreditCardBill> webParam = null;
		try {
			for (int j = 1; j < 12; j++) {
				webParam = chinaCiticCreditBankParser.creditCardBillCrawlerHtmlunit(taskBank, bankJsonBean, j);
				if (null != webParam.getList()) {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("BillCawler.SUCCESS" + j);
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);
					citicChinaCreditCardBillRepository.saveAll(webParam.getList());
					tracer.addTag("action.crawler.BillCawler.SUCCESS", taskBank.getTaskid());
					taskBankRepository.save(taskBank);
					time++;
				} else {
					CiticChinaCreditCardHtml c = new CiticChinaCreditCardHtml();
					c.setHtml(webParam.getHtml());
					c.setPagenumber(1);
					c.setType("BillCawler.ERROR" + j);
					c.setTaskid(taskBank.getTaskid());
					c.setUrl(webParam.getUrl());
					citicChinaCreditCardHtmlRepository.save(c);
					tracer.addTag("action.crawler.BillCawler.ERROR", taskBank.getTaskid());
				}
			}
			if (time > 0) {
				tracer.addTag("action.crawler.BillCawler.SUCCESS", taskBank.getTaskid());
			} else {
				tracer.addTag("action.crawler.BillCawler.ERROR", taskBank.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.crawler.BillCawler.TimeOut", taskBank.getTaskid());
		}
		taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
	}

}
