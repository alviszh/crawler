package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankStatusCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardTimeDeposit;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaCreditCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaCreditCardMonthbillRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaCreditCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaCreditCardUserinfoRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaDebitCardTimeDepositRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaDebitCardUserinfoRepository;
import com.microservice.dao.repository.crawler.bank.icbcchina.IcbcChinaDebitcardHtmlRepository;

import app.common.AccNumList;
import app.common.DepositParam;
import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.IcbcChinaParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.icbcchina"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.icbcchina"})
public class GetDataService {
	
	@Autowired
	private IcbcChinaParser icbcChinaParser;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private IcbcChinaDebitcardHtmlRepository icbcChinaDebitcardHtmlRepository;
	@Autowired
	private IcbcChinaCreditCardHtmlRepository icbcChinaCreditCardHtmlRepository;
	@Autowired
	private IcbcChinaDebitCardUserinfoRepository icbcChinaDebitcardUserinfoRepository;
	@Autowired
	private IcbcChinaCreditCardUserinfoRepository icbcChinaCreditCardUserinfoRepository;
	@Autowired
	private IcbcChinaDebitCardTransFlowRepository icbcChinaDebitCardTransFlowRepository;
	@Autowired
	private IcbcChinaCreditCardMonthbillRepository icbcChinaCreditCardMonthbillRepository;
	@Autowired
	private IcbcChinaCreditCardTransFlowRepository icbcChinaCreditCardTransFlowRepository;
	@Autowired
	private IcbcChinaDebitCardTimeDepositRepository icbcChinaDebitCardTimeDepositRepository;
	@Autowired
	private TracerLog tracer;
	
	public void getUserInfo(TaskBank taskBank){
		tracer.addTag("crawler.bank.crawler.getUserInfo.taskid", taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getUserInfo.begin.taskid", taskBank.getTaskid());
		taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
		try {
			WebParam webParam = icbcChinaParser.getUserInfo(taskBank);
			if(null != webParam){
				if(null != webParam.getPage()){
					IcbcChinaDebitCardHtml html = new IcbcChinaDebitCardHtml();
					html.setUrl(webParam.getUrl());
					html.setPagenumber(1);
					html.setType("userinfo");
					html.setTaskid(taskBank.getTaskid());
					html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					icbcChinaDebitcardHtmlRepository.save(html);
					tracer.addTag("crawler.bank.crawler.getUserInfo.page.success", "用户信息页面已入库");
				}
				if(null != webParam.getList()){
					icbcChinaDebitcardUserinfoRepository.saveAll(webParam.getList());
					taskBankStatusService.updateTaskBankUserinfo(200, "个人信息爬取成功", taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getUserInfo.userinfo.success", "用户信息数据已入库");
				}else{
					taskBankStatusService.updateTaskBankUserinfo(201, "个人信息爬取完成", taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getUserInfo.userinfo.fail", "用户信息无数据");
				}
			}else{
				tracer.addTag("crawler.bank.crawler.getUserInfo.error", "webParam为null，个人信息获取异常");
				taskBankStatusService.updateTaskBankUserinfo(404, "个人信息爬取完成", taskBank.getTaskid());
			}
		}catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.crawler.getUserInfo.Exception", e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, "个人信息爬取完成", taskBank.getTaskid());
		}
	}

	public void getTimeDeposit(TaskBank taskBank) {
		tracer.addTag("crawler.bank.crawler.getTimeDeposit.taskid", taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getTimeDeposit.begin.taskid", taskBank.getTaskid());
		taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
		try {
			//先获取不同的定期资产
			List<DepositParam> depositParams = icbcChinaParser.getTimeDeposit(taskBank);
			//判断是否有定期存款
			if(null != depositParams && depositParams.size() > 0){
				List<IcbcChinaDebitCardTimeDeposit> timeDeposits = new ArrayList<IcbcChinaDebitCardTimeDeposit>();
				for (DepositParam depositParam : depositParams) {
					 WebParam<IcbcChinaDebitCardTimeDeposit> param = icbcChinaParser.getTimeDepositData(depositParam, taskBank);
					 timeDeposits.add(param.getList().get(0));
				}
				icbcChinaDebitCardTimeDepositRepository.saveAll(timeDeposits);
				tracer.addTag("crawler.bank.crawler.getTimeDeposit.success", "该用户定期存款 已入库");
			}else{
				tracer.addTag("crawler.bank.crawler.getTimeDeposit.fail", "该用户无定期存款");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.crawler.getTimeDeposit.Exception", e.toString());
		}
	}
	
	public void getTransflow(TaskBank taskBank){
		tracer.addTag("crawler.bank.crawler.getTransflow.taskid", taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getTransflow.begin.taskid", taskBank.getTaskid());
		taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
		try {
			int a = 0;//流水入库计数器
			//先获取 要爬取的卡号以及对应的编号
			List<AccNumList> numLists = icbcChinaParser.getAccNumList(taskBank);
			tracer.addTag("numLists----------------->", numLists.toString());
			if(null != numLists && numLists.size() > 0){
				for (AccNumList accNumList : numLists) {
					if(null != accNumList.getAcctNo0() && accNumList.getAcctNo0().length() > 0){
						WebParam webParam = icbcChinaParser.getTransflow(taskBank, accNumList);
						
						IcbcChinaDebitCardHtml debitCardHtml = new IcbcChinaDebitCardHtml();
						debitCardHtml.setUrl(webParam.getUrl());
						debitCardHtml.setPagenumber(1);
						debitCardHtml.setType("transflow");
						debitCardHtml.setHtml(webParam.getHtml());			//将银行流水的CSV文件作为页面源码入库
						debitCardHtml.setTaskid(taskBank.getTaskid());
						
						if(null != webParam.getList()){
							icbcChinaDebitCardTransFlowRepository.saveAll(webParam.getList());
							tracer.addTag("crawler.bank.crawler.getTransflow.success."+accNumList.getCardNum(), "卡号"+accNumList.getCardNum()+"的流水信息成功入库");
							a++;
						}
					}
				}
				if(a > 0){
					taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(), BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getTransflow.success", "流水信息成功入库");
				}else{
					taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getTransflow.fail", "流水信息入库入库失败");
				}
			}else{
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR2.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), taskBank.getTaskid());
				tracer.addTag("crawler.bank.crawler.getTransflow.fail2", "流水信息入库入库失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.crawler.getTransflow.service.Exception", e.toString());
			taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR2.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), taskBank.getTaskid());
			tracer.addTag("crawler.bank.crawler.getTransflow.fail3", "流水信息入库入库过程出现异常");
		}
		
	}
	
	public void getCreditUserInfo(TaskBank taskBank, AccNumList accNumList){
		tracer.addTag("crawler.bank.crawler.getCreditUserInfo.taskid."+accNumList.getCardNum(), taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getCreditUserInfo.begin.taskid."+accNumList.getCardNum(), taskBank.getTaskid());
		taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
		try {
			WebParam webParam = icbcChinaParser.getCreditUserInfo(taskBank, accNumList);
			IcbcChinaCreditCardHtml cardHtml = new IcbcChinaCreditCardHtml();
			cardHtml.setPagenumber(1);
			cardHtml.setType("creditUserInfo");
			cardHtml.setUrl(webParam.getUrl());
			cardHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			cardHtml.setTaskid(taskBank.getTaskid());
			icbcChinaCreditCardHtmlRepository.save(cardHtml);
			tracer.addTag("crawler.bank.crawler.getCreditUserInfo.page.success."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡信息页面入库");
			if(null != webParam.getList()){
				icbcChinaCreditCardUserinfoRepository.saveAll(webParam.getList());
				taskBankStatusService.updateTaskBankUserinfo(200, accNumList.getCardNum()+"信用卡信息爬取成功", taskBank.getTaskid());
				tracer.addTag("crawler.bank.crawler.getCreditUserInfo.userinfo.success."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡信息数据已入库");
			}else{
				tracer.addTag("crawler.bank.crawler.getCreditUserInfo.userinfo.fail."+accNumList.getCardNum(), "用户信息无数据");
				taskBankStatusService.updateTaskBankUserinfo(201, accNumList.getCardNum()+"信用卡信息爬取完成", taskBank.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.crawler.getCreditUserInfo.Exception."+accNumList.getCardNum(), e.toString());
			taskBankStatusService.updateTaskBankUserinfo(404, accNumList.getCardNum()+"信用卡信息爬取完成", taskBank.getTaskid());
		}
		
	}

	public void getMonthBill(TaskBank taskBank, AccNumList accNumList) {
		tracer.addTag("crawler.bank.crawler.getMonthBill.taskid."+accNumList.getCardNum(), taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getMonthBill.begin.taskid."+accNumList.getCardNum(), taskBank.getTaskid());
		for (int i = 0; i < 4; i++) {
			taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
			try {
				WebParam webParam = icbcChinaParser.getMonthBill(taskBank, accNumList, i);
				if(null != webParam.getList()){
					icbcChinaCreditCardMonthbillRepository.saveAll(webParam.getList());
//					taskBankStatusService.changeStatus(taskBank.getPhase(), taskBank.getPhase_status(), accNumList.getCardNum()+"信用卡信息爬取成功"+i, taskBank.getError_code(), false, taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getMonthBill.monthBill.success."+i+"."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡信息数据已入库");
				}else{
					tracer.addTag("crawler.bank.crawler.getMonthBill.monthBill.fail."+i+"."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡信息无数据");
				}
				if(null != webParam.getCreditCardTransFlows()){
					icbcChinaCreditCardTransFlowRepository.saveAll(webParam.getCreditCardTransFlows());
					taskBankStatusService.updateTaskBankTransflow(200, "卡号"+accNumList.getCardNum()+"的流水信息采集成功"+i, taskBank.getTaskid());
					tracer.addTag("crawler.bank.crawler.getMonthBill.trans.success."+i+"."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡流水信息已入库");
				}else{
					tracer.addTag("crawler.bank.crawler.getMonthBill.trans.fail."+i+"."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡流水信息无数据");
					if(null == taskBank.getTransflowStatus()){
						taskBankStatusService.updateTaskBankTransflow(201, "卡号"+accNumList.getCardNum()+"的流水信息采集完成"+i, taskBank.getTaskid());
					}
				}
				
				IcbcChinaCreditCardHtml cardHtml = new IcbcChinaCreditCardHtml();
				cardHtml.setPagenumber(1);
				cardHtml.setType("monthbill"+i);
				cardHtml.setUrl(webParam.getUrl());
				cardHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
				cardHtml.setTaskid(taskBank.getTaskid());
				icbcChinaCreditCardHtmlRepository.save(cardHtml);
				tracer.addTag("crawler.bank.crawler.getMonthBill.page.success."+i+"."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡信息页面入库");
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("crawler.bank.crawler.getMonthBill.Exception."+i+"."+accNumList.getCardNum(), e.toString());
				if(null == taskBank.getTransflowStatus()){
					taskBankStatusService.updateTaskBankTransflow(404, "卡号"+accNumList.getCardNum()+"的流水信息采集完成"+i, taskBank.getTaskid());
				}
			}
		}
	}

	public void getPayStages(TaskBank taskBank, AccNumList accNumList) {
		tracer.addTag("crawler.bank.crawler.getPayStages.taskid", taskBank.getTaskid());
		tracer.addTag("crawler.bank.crawler.getPayStages.begin.taskid", taskBank.getTaskid());
		taskBank = taskBankRepository.findByTaskid(taskBank.getTaskid());
		try {
			WebParam webParam = icbcChinaParser.getPayStages(taskBank, accNumList);
			IcbcChinaCreditCardHtml cardHtml = new IcbcChinaCreditCardHtml();
			cardHtml.setPagenumber(1);
			cardHtml.setType("payStages"+accNumList.getCardNum());
			cardHtml.setUrl(webParam.getUrl());
			cardHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			cardHtml.setTaskid(taskBank.getTaskid());
			icbcChinaCreditCardHtmlRepository.save(cardHtml);
			tracer.addTag("crawler.bank.crawler.getPayStages.page.success."+accNumList.getCardNum(), accNumList.getCardNum()+"信用卡分期信息页面入库");
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.bank.crawler.getPayStages.Exception."+accNumList.getCardNum(), e.toString());
		}
		
	}

}
