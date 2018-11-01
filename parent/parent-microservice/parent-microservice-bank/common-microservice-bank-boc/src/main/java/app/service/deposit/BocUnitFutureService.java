package app.service.deposit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserInfoBalance;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfSingleLimit;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfo;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfoOpendate;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaDebitCardUserInfoBalanceRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaDebitCardUserinfSingleLimitRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaDebitCardUserinfoOpendateRepository;
import com.microservice.dao.repository.crawler.bank.bocchina.BocchinaDebitCardUserinfoRepository;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.bean.Error;
import app.bean.JsonRootBean;
import app.bean.Response;
import app.bean.ResultForTranFlow;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.BocParse2;
import app.service.BocServiceLoginAndGet;
import app.service.TaskBankStatusService;
import app.service.credit.BocServiceCreditLoginAndGet;

/**
 * 
 * 项目名称：common-microservice-bank-boc 类名称：BocUnitService 类描述： 创建人：hyx
 * 创建时间：2017年11月1日 上午11:17:01
 * 
 * @version
 */

@Component
public class BocUnitFutureService extends AbstractChaoJiYingHandler {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private BocchinaDebitCardUserinfoRepository bocchinaDebitCardUserinfoRepository;

	@Autowired
	private BocchinaDebitCardUserinfoOpendateRepository bocchinaDebitCardUserinfoOpendateRepository;

	@Autowired
	private BocchinaDebitCardUserinfSingleLimitRepository bocchinaDebitCardUserinfSingleLimitRepository;

	@Autowired
	private BocchinaDebitCardTransFlowRepository bocchinaDebitCardTransFlowRepository;

	@Autowired
	private BocchinaDebitCardUserInfoBalanceRepository bocchinaDebitCardUserInfoBalanceRepository;

	@Autowired
	private BocServiceLoginAndGet bocServiceLoginAndGet;

	@Autowired
	private TaskBankStatusService taskBankStatusService;

	@Autowired
	private TaskBankRepository taskBankRepository;
	
	@Autowired
	private BocServiceCreditLoginAndGet bocServiceCreditLoginAndGet;

	private String url = "https://ebsnew.boc.cn/BII/PsnGetUserProfile.do?_locale=zh_CN";

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 void
	 * @return
	 * @return
	 */
	public JsonRootBean<BocchinaDebitCardUserinfo> getUserinfo(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		try {
			String html = bocServiceLoginAndGet.getUserInfo(url, webClient);
			if (html.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), "您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务",
						null, false, bankJsonBean.getTaskid());
				taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
						BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
				return null;
			}

			JsonRootBean<BocchinaDebitCardUserinfo> userinfo_root = BocParse2.userinfo_parse(html);
			List<Response<BocchinaDebitCardUserinfo>> userinfo_list = userinfo_root.getResponse();

			for (Response<BocchinaDebitCardUserinfo> result_res : userinfo_list) {
				Error erro = result_res.getError();
				BocchinaDebitCardUserinfo result = result_res.getResult();
				if (result != null) {
					result.setTaskid(bankJsonBean.getTaskid());
					bocchinaDebitCardUserinfoRepository.save(result);
					tracerLog.output("crawler.bank.crawler.userinfo", "用户基本信息存储成功");
				} else {
					tracerLog.output("crawler.bank.crawler.userinfo", "用户基本信息:" + erro.getMessage());
				}
			}
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
					BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());

			return userinfo_root;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
					BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
		}
		return null;

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 void
	 * @return
	 */
	public Object getUserInfoOpendate(BankJsonBean bankJsonBean, String accountSeq) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		try {
			String html = bocServiceLoginAndGet.getUserInfoOpendate(url, webClient, accountSeq);
			if (html.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), "您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务",
						null, false, bankJsonBean.getTaskid());
				return null;
			}

			JsonRootBean<BocchinaDebitCardUserinfoOpendate> userinfo_root = BocParse2.userInfoOpendate_parse(html);
			List<Response<BocchinaDebitCardUserinfoOpendate>> userinfo_list = userinfo_root.getResponse();

			for (Response<BocchinaDebitCardUserinfoOpendate> result_res : userinfo_list) {
				Error erro = result_res.getError();
				BocchinaDebitCardUserinfoOpendate result = result_res.getResult();
				if (result != null) {
					System.out.println("====BocchinaDebitCardUserinfoOpendate==" + result.toString());
					result.setTaskid(bankJsonBean.getTaskid());
					bocchinaDebitCardUserinfoOpendateRepository.save(result);
					tracerLog.output("crawler.bank.crawler.userinfoSingleLimit", "用户开户信息存储成功");
				} else {
					tracerLog.output("crawler.bank.crawler.userinfoSingleLimit", "用户开户信息:" + erro.getMessage());
				}
				List<BocchinaDebitCardUserInfoBalance> accountDetaiList = result.getAccountDetaiList();
				if (accountDetaiList != null) {
					for (BocchinaDebitCardUserInfoBalance balance : accountDetaiList) {
						System.out.println("====BocchinaDebitCardUserInfoBalance==" + balance.toString());
						balance.setTaskid(bankJsonBean.getTaskid());
						bocchinaDebitCardUserInfoBalanceRepository.save(balance);
					}

				}

			}
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
					BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
					BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
		}
		return null;

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 创建人：hyx
	 * 创建时间：2017年11月1日
	 * 
	 * @version 1 返回值 void
	 * @return
	 */
	public Object getUserInfoSingleLimit(BankJsonBean bankJsonBean, String accountSeq) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		try {
			String html = bocServiceLoginAndGet.getUserInfoSingleLimit(url, webClient, accountSeq);
			if (html.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
						BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
				taskBankStatusService.changeTaskBankFinish(bankJsonBean.getTaskid().trim());
				return null;
			}

			JsonRootBean<BocchinaDebitCardUserinfSingleLimit> userinfo_root = BocParse2.userinfoSingleLimit_parse(html);
			List<Response<BocchinaDebitCardUserinfSingleLimit>> userinfo_list = userinfo_root.getResponse();

			for (Response<BocchinaDebitCardUserinfSingleLimit> result_res : userinfo_list) {
				Error erro = result_res.getError();
				BocchinaDebitCardUserinfSingleLimit result = result_res.getResult();
				if (result != null) {
					result.setTaskid(bankJsonBean.getTaskid());
					System.out.println("====BocchinaDebitCardUserinfSingleLimit==" + result.toString());
					bocchinaDebitCardUserinfSingleLimitRepository.save(result);
					tracerLog.output("crawler.bank.crawler.userinfoOpendate", "用户上限信息存储成功");
				} else {
					tracerLog.output("crawler.bank.crawler.userinfoOpendate", "用户上限信息:" + erro.getMessage());
				}
			}

			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(),
					BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR2.getError_code(),
					BankStatusCode.BANK_USERINFO_ERROR2.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
		}
		return null;

	}

	/**
	 * 
	 * 项目名称：common-microservice-bank-boc 所属包名：app.service 类描述： 获取用户流水信息 创建人：hyx
	 * 创建时间：2017年11月2日
	 * 
	 * @version 1 返回值 Object
	 */
	public Object getTranFlow(BankJsonBean bankJsonBean,String accountSeq) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		WebClient webClient = taskBank.getClient(taskBank.getCookies());
		LocalDate today = LocalDate.now();
		String countid = null;
		try {
			countid = bocServiceCreditLoginAndGet.getCountid(url, webClient);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(countid == null){
			taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR2.getError_code(),
					BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), taskBank.getTaskid());
			taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
			
			return null;
		}

		List<Future<String>> list_future = new ArrayList<Future<String>>();
		List<String> list_html = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			LocalDate enddate = today.plusMonths(-(i * 6));
			LocalDate startdate = today.plusMonths(-((i + 1) * 6));

			String enddate_string = (enddate + "").replaceAll("-", "/");
			String startdate_string = (startdate + "").replaceAll("-", "/");
			try {
				Future<String> future = bocServiceLoginAndGet.getTranFlow(url, webClient, countid, accountSeq, startdate_string, enddate_string);
						
				list_future.add(future);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracerLog.output("crawler.bank.crawler.transflow。countid", e.getMessage());

			}
		}
		boolean istrue = true;
		while (istrue) {
			if (list_future.size() <= 0) {
				istrue = false;
			}
			for (Future<String> future : list_future) {

				if (future.isDone()) { // 判断是否执行完毕

					list_future.remove(future);

					try {
						list_html.add(future.get());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}

		};
		for (String html : list_html) {
			if (html.indexOf("您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务") != -1) {
				taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhase(),
						BankStatusCode.BANK_CRAWLER_ERROR_PAGENUM.getPhasestatus(), "您的网银会话已经超时，为了保证您的财产安全，已经自动退出网银服务",
						null, false, bankJsonBean.getTaskid());
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR2.getError_code(),
						BankStatusCode.BANK_TRANSFLOW_ERROR2.getDescription(), taskBank.getTaskid());
				taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
				return null;
			}
			JsonRootBean<ResultForTranFlow> transflow_root = BocParse2.transFlow_parse(html);
			List<Response<ResultForTranFlow>> transflow_list = transflow_root.getResponse();
			for (Response<ResultForTranFlow> result_res : transflow_list) {
				Error erro = result_res.getError();
				ResultForTranFlow result = result_res.getResult();
				if (result != null) {
					List<BocchinaDebitCardTransFlow> list = result.getList();
					for (BocchinaDebitCardTransFlow result_transflow : list) {
//						System.out.println("====BocchinaDebitCardUserinfSingleLimit==" + result.toString());
						result_transflow.setTaskid(bankJsonBean.getTaskid());
						result_transflow.setAccountNumber(bankJsonBean.getLoginName());
						bocchinaDebitCardTransFlowRepository.save(result_transflow);
//						tracerLog.output("crawler.bank.crawler.transflow", k+"用户流水信息存储成功" + i+"list:"+list.size());
					}

				} else {
					tracerLog.output("crawler.bank.crawler.transflow", "用户流水信息:" + erro.getMessage());
				}
			}
			
		}
		taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(),
				BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskBank.getTaskid());
		taskBankStatusService.changeTaskBankFinishByStatus(bankJsonBean.getTaskid().trim());
		return null;
	}

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();

		for (int i = 0; i < 4; i++) {
			LocalDate enddate = today.plusMonths(-(i * 6));
			LocalDate startdate = today.plusMonths(-((i + 1) * 6));

			String enddate_string = (enddate + "").replaceAll("-", "/");
			String startdate_string = (startdate + "").replaceAll("-", "/");
			System.out.println("====" + i + "========");
			System.out.println("=========" + enddate_string);
			System.out.println("=========" + startdate_string);
		}
	}
}
