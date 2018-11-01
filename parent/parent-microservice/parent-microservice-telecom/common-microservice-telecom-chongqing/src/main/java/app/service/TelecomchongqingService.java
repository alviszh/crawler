package app.service;

import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBalance;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBill;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingBusiness;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingCallRecord;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingComboUse;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingFlow;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingHtml;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingIncrement;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingIntegral;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingMessage;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingPay;
import com.microservice.dao.entity.crawler.telecom.chongqing.TelecomChongqingStarlevel;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingBalanceRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingBillRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingComboUseRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingFlowRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingIncrementBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingIntegralRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingMessageRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingPayRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingStarlevelRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.telecom.htmlparse.TelecomchongqingParser;
import app.exceptiondetail.EUtils;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.chongqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.chongqing")
public class TelecomchongqingService implements ISms {

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomchongqingParser telecomchongqingParser;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TelecomChongqingStarlevelRepository telecomChongqingStarlevelRepository;

	@Autowired
	private TelecomChongqingHtmlRepository telecomChongqingHtmlRepository;

	@Autowired
	private TelecomChongqingBalanceRepository telecomChongqingBalanceRepository;

	@Autowired
	private TelecomChongqingBillRepository telecomChongqingBillRepository;

	@Autowired
	private TelecomChongqingIntegralRepository telecomChongqingIntegralRepository;

	@Autowired
	private TelecomChongqingComboUseRepository telecomChongqingComboUseRepository;

	@Autowired
	private TelecomChongqingBusinessRepository telecomChongqingBusinessRepository;

	@Autowired
	private TelecomChongqingPayRepository telecomChongqingPayRepository;

	@Autowired
	private TelecomChongqingCallRecordRepository telecomChongqingCallRecordRepository;

	@Autowired
	private TelecomChongqingMessageRepository telecomChongqingMessageRepository;

	@Autowired
	private TelecomChongqingFlowRepository telecomChongqingFlowRepository;
	
	@Autowired
	private TelecomChongqingIncrementBusinessRepository telecomChongqingIncrementBusinessRepository;
	
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;
	
	

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		if (null == taskMobile) {
			return true;
		}
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	public TaskMobile verification(MessageLogin messageLogin, TaskMobile taskMobile) {

//		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase());
//		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhasestatus());
//		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getDescription());
//		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getError_code());
//		save(taskMobile);
		try {
			Boolean verificationName = telecomchongqingParser.verificationName(taskMobile);
			if (!verificationName) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
				taskMobile.setDescription("客户姓名信息不正确，请仔细核对后再输入");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
				// 登录失败状态存储
				save(taskMobile);
				return taskMobile;
			}
			Boolean verificationIdcard = telecomchongqingParser.verificationIdcard(taskMobile);
			if (!verificationIdcard) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
				taskMobile.setDescription("证件号码信息不正确，请仔细核对后再输入");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
				// 登录失败状态存储
				save(taskMobile);
				return taskMobile;
			}
			if (verificationName && verificationIdcard) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getError_code());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				// 登录成功状态更新
				save(taskMobile);
				return taskMobile;
			}

		} catch (Exception e) {
			tracer.addTag("TelecomService.verification.ERROR:taskid"+taskMobile.getTaskid(), eutils.getEDetail(e));
			// 实名认证用二次验证枚举
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
			// 实名认证状态更新
			save(taskMobile);
		}
		return taskMobile;
	}

	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		taskMobile = verification(messageLogin, taskMobile);
		if (!StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase().equals(taskMobile.getPhase())
				&& !StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus().equals(taskMobile.getPhase_status())) {
			return taskMobile;
		}
		try {
			tracer.addTag("parser.crawler.taskid.getPhoneCode", taskMobile.getTaskid());

			String errorDescription = "";
			WebClient webClient = telecomchongqingParser.addcookie(taskMobile);

			String url3 = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02031273";
			Page html3 = telecomchongqingParser.getPage(webClient, taskMobile, url3, null, null, false);
			String json3 = html3.getWebResponse().getContentAsString();
			String url = "http://cq.189.cn/new-bill/bill_DXYZM";
			Page page = telecomchongqingParser.getPage(webClient, taskMobile, url, HttpMethod.POST, null, false);
			String json = page.getWebResponse().getContentAsString();
			tracer.addTag("TelecomShanxi1Service.getphonecode---验证码返回数据:" + taskMobile.getTaskid() + "---json:", json);
			JSONObject jsonObj = JSONObject.fromObject(json);
			if (jsonObj.has("errorCode")) {
				String errorCode = jsonObj.getString("errorCode");
				if (!"0".equals(errorCode)) {
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
					if (jsonObj.has("errorDescription")) {
						errorDescription = jsonObj.getString("errorDescription");
						if(StringUtils.isBlank(errorDescription)){
							errorDescription = StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription();
						}
						taskMobile.setDescription(errorDescription);
					} else {
						taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
					}
					save(taskMobile);
					webClient.close();
					return taskMobile;
				} else {
					String cookieString = CommonUnit.transcookieToJson(webClient);
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
					taskMobile.setCookies(cookieString);
					// 发送验证码状态更新
					save(taskMobile);
					webClient.close();
					return taskMobile;
				}
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				save(taskMobile);
				webClient.close();
				return taskMobile;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.getPhoneCode---Taskid--",
					taskMobile.getTaskid() + eutils.getEDetail(e));
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
			return taskMobile;
		}

	}

	// 手机短信验证码验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		
		try {
			String htmljosn = telecomchongqingParser.verifySms(taskMobile, messageLogin);
			tracer.addTag("TelecomShanxi1Service.verificationcode", taskMobile.getTaskid() + "---htmlpage:" + htmljosn);

			JSONObject jsonObj = JSONObject.fromObject(htmljosn);
			if (jsonObj.has("xm")) {
				String xm = jsonObj.getString("xm");
				if ("1".equals(xm)) {
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription("短信验证码验证失败,姓名不准确");
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
					// 手机验证码验证失败状态更新
					save(taskMobile);
					return taskMobile;
				}
			}
			else if (jsonObj.has("sfz")) {
				String sfz = jsonObj.getString("sfz");
				if ("2".equals(sfz)) {
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription("短信验证码验证失败,身份证号码不准确");
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
					// 手机验证码验证失败状态更新
					save(taskMobile);
					return taskMobile;
				}
			}
			else if (jsonObj.has("message")) {
				String message = jsonObj.getString("message");
				if ("2333".equals(message)) {
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
					taskMobile.setDescription("短信验证码验证失败,验证码有误");
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
					// 手机验证码验证失败状态更新
					save(taskMobile);
					return taskMobile;
				}else{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					save(taskMobile);
					return taskMobile;
				}
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				save(taskMobile);
				return taskMobile;
			}

		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.verificationcode---Taskid",
					taskMobile.getTaskid() + eutils.getEDetail(e));
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
		}
		return taskMobile;

	}

	/**
	 * 用户星级服务信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getStarlevel(TaskMobile taskMobile) {
		tracer.addTag("TelecomchongqingService.getStarlevel", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingStarlevel> webParam = telecomchongqingParser.getStarlevel(taskMobile);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingStarlevelRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getStarlevel---用户星级服务信息",
							"用户星级服务信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【账户信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getStarlevel---用户星级服务信息",
							"用户星级服务信息未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【账户信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_starlevel", "1", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getStarlevel---用户星级服务信息源码",
						"用户星级服务信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");
				tracer.addTag("TelecomchongqingService.getStarlevel.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");
			tracer.addTag("TelecomchongqingService.getStarlevel---ERROR-"+ taskMobile.getTaskid() , eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * 余额查询
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBalance(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getBalance", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingBalance> webParam = telecomchongqingParser.getBalance(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingBalanceRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getBalance---余额信息", "余额信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【用户信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getBalance---余额信息", "余额信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【用户信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_balance", "1", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBalance---余额信息源码", "余额信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
				tracer.addTag("TelecomchongqingService.getBalance.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
			tracer.addTag("TelecomchongqingService.getBalance---ERROR--"+taskMobile.getTaskid() , eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}

	/**
	 * 实时话费查询
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBalanceRealtime(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getBalanceRealtime", taskMobile.getTaskid());

		try {
			
			WebParam<TelecomChongqingBalance> webParam = telecomchongqingParser.getBalanceRealtime(taskMobile,
					messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingBalanceRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getBalanceRealtime---实时话费查询",
							"余额信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【用户信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getBalanceRealtime---实时话费查询",
							"余额信息未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【用户信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_balance", "2", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBalanceRealtime---实时话费查询源码",
						"实时话费查询源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
				tracer.addTag("TelecomchongqingService.getBalanceRealtime.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");

			tracer.addTag("TelecomchongqingService.getBalanceRealtime---ERROR---"+
					taskMobile.getTaskid() , eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}
	
	
	/**
	 * 账单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBillAll(TaskMobile taskMobile, MessageLogin messageLogin ) {
		try {
			taskMobile = telecomchongqingParser.transferBill(taskMobile);
			for (int i = 1; i < 7; i++) {
				String yearmonth = AsyncchongqingGetAllDataService.getDateBefore("yyyy-MM", i);
				tracer.addTag("inTelecomChongqingService.getBillAll-----yearmonth:" + yearmonth, taskMobile.getTaskid());
				// 账单查询
				getBill(taskMobile, messageLogin, yearmonth);
			}
		} catch (Exception e) {
			tracer.addTag("inTelecomChongqingService.getBillAll---ERROR---"+taskMobile.getTaskid() , eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");

	}

	/**
	 * 账单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getBill(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomchongqingService.getBill---" + yearmonth, taskMobile.getTaskid());

		try {
			
			WebParam<TelecomChongqingBill> webParam = telecomchongqingParser.getBill(taskMobile, messageLogin,
					yearmonth);

			if (null != webParam) {

				telecomChongqingBillRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomchongqingService.getBill---账单", "账单已入库!" + taskMobile.getTaskid());

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_bill", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBill---账单源码", "账单源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomchongqingService.getBill.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingService.getBill---ERROR---"+ taskMobile.getTaskid() , eutils.getEDetail(e));
		}

	}

	/**
	 * 积分查询
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getIntegral(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getIntegral", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingIntegral> webParam = telecomchongqingParser.getIntegral(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingIntegralRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getIntegral---积分查询", "积分信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【积分信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getIntegral---积分查询", "积分信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【积分信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_integral", "1", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getIntegral---积分查询源码", "积分查询源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");

				tracer.addTag("TelecomchongqingService.getIntegral.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");
			tracer.addTag("TelecomchongqingService.getIntegral---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}
	
	
	
	/**
	 * 套餐使用情况
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getComboUseAll(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getComboUseAll", taskMobile.getTaskid());

		try {
			taskMobile = telecomchongqingParser.transferComboUse(taskMobile);
			for (int i = 0; i < 6; i++) {
				String yearmonth = AsyncchongqingGetAllDataService.getDateBefore("yyyy-MM-01 00:00:00", i);
				tracer.addTag("inTelecomChongqingService.getComboUseAll-----yearmonth:" + yearmonth,
						taskMobile.getTaskid());
				// 套餐使用情况
				getComboUse(taskMobile, messageLogin, yearmonth);
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingService.getComboUseAll---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 套餐使用情况
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getComboUse(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomchongqingService.getComboUse", taskMobile.getTaskid());

		try {
			
			WebParam<TelecomChongqingComboUse> webParam = telecomchongqingParser.getComboUse(taskMobile, messageLogin,
					yearmonth);

			if (null != webParam) {

				telecomChongqingComboUseRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomchongqingService.getComboUse---套餐使用情况", "套餐使用情况已入库!" + taskMobile.getTaskid());

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_combouse", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getComboUse---套餐使用情况源码",
						"套餐使用情况源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomchongqingService.getComboUse.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingService.getComboUse---ERROR---"+ taskMobile.getTaskid(), eutils.getEDetail(e));
		}
	}

	/**
	 * 在用业务情况-----套餐信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBusinessT(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getBusinessT", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingBusiness> webParam = telecomchongqingParser.getBusinessT(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingBusinessRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getBusinessT---在用业务情况-----套餐信息",
							"在用业务情况-----套餐信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【业务信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getBusinessT---在用业务情况-----套餐信息",
							"在用业务情况-----套餐信息未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【业务信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_business", "1", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBusinessT---在用业务情况-----套餐信息源码",
						"在用业务情况-----套餐信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

				tracer.addTag("TelecomchongqingService.getBusinessT.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

			tracer.addTag("TelecomchongqingService.getBusinessT---ERROR---"+ taskMobile.getTaskid(), eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}

	/**
	 * 在用业务----已订购的增值业务
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBusinessZ(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getBusinessZ", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingBusiness> webParam = telecomchongqingParser.getBusinessZ(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingBusinessRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getBusinessZ---在用业务情况-----已订购的增值业务",
							"在用业务情况-----已订购的增值业务信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【业务信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getBusinessZ---在用业务情况-----已订购的增值业务",
							"在用业务情况-----已订购的增值业务信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【业务信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_business", "2", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBusinessZ---在用业务情况-----已订购的增值业务源码",
						"在用业务情况-----已订购的增值业务源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

				tracer.addTag("TelecomchongqingService.getBusinessZ.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

			tracer.addTag("TelecomchongqingService.getBusinessZ---ERROR---" + taskMobile.getTaskid(),
					eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}

	/**
	 * 在用业务----已订购的基础功能
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBusinessJ(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomchongqingService.getBusinessJ", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingBusiness> webParam = telecomchongqingParser.getBusinessJ(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingBusinessRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getBusinessJ---在用业务情况-----已订购的基础功能",
							"在用业务情况-----已订购的基础功能信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【业务信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getBusinessJ---在用业务情况-----已订购的基础功能",
							"在用业务情况-----已订购的基础功能信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【业务信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_business", "3", webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getBusinessJ---在用业务情况-----已订购的基础功能源码",
						"在用业务情况-----已订购的基础功能源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

				tracer.addTag("TelecomchongqingService.getBusinessJ.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

			tracer.addTag("TelecomchongqingService.getBusinessJ---ERROR---" + taskMobile.getTaskid(),
					eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}

	/**
	 * 充值缴费
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getPay(TaskMobile taskMobile, MessageLogin messageLogin, String starttime, String endtime) {
		tracer.addTag("TelecomchongqingService.getPay", taskMobile.getTaskid());

		try {

			WebParam<TelecomChongqingPay> webParam = telecomchongqingParser.getPay(taskMobile, messageLogin, starttime,
					endtime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingPayRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getPay---充值缴费", "充值缴费信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【缴费信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getPay---充值缴费", "充值缴费信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【缴费信息】采集完成");

				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_pay", starttime+"_"+endtime, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getPay---充值缴费源码", "充值缴费源码表入库!" + taskMobile.getTaskid());

			} else {

				crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");

				tracer.addTag("TelecomchongqingService.getPay.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");
			tracer.addTag("TelecomchongqingService.getPay---ERROR---" + taskMobile.getTaskid(), eutils.getEDetail(e));
		}
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");
	}

	/**
	 * 通话详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getCallRecord(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth, String beginTime,
			String endTime) {
		tracer.addTag("TelecomchongqingService.getCallRecord", taskMobile.getTaskid());

		try {
			
			WebParam<TelecomChongqingCallRecord> webParam = telecomchongqingParser.getCallRecord(taskMobile,
					messageLogin, yearmonth, beginTime, endTime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingCallRecordRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getCallRecord---通话详单",
							"通话详单信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【通讯信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getCallRecord---通话详单",
							"通话详单信息未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【通讯信息】采集完成");
				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_callrecord", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getCallRecord---通话详单源码", "通话详单源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

				tracer.addTag("TelecomchongqingService.getCallRecord.webParam is null", taskMobile.getTaskid());
			}
		}catch (RuntimeException e) {
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录",
					yearmonth, taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", "系统超时",
					1);
			mobileDataErrRecRepository.save(dataErrRec);
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");
			tracer.addTag("TelecomchongqingService.getCallRecord.MobileDataErrRec"+yearmonth, taskMobile.getTaskid());
			
		}catch (Exception e) {
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

			tracer.addTag("TelecomchongqingService.getCallRecord---ERROR---" + taskMobile.getTaskid(),
					eutils.getEDetail(e));
		}
	}

	
	/**
	 * 短信详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getMessage(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth, String beginTime,
			String endTime) {
		tracer.addTag("TelecomchongqingService.getMessage", taskMobile.getTaskid());

		try {
			WebParam<TelecomChongqingMessage> webParam = telecomchongqingParser.getMessage(taskMobile, messageLogin,
					yearmonth, beginTime, endTime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomChongqingMessageRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomchongqingService.getMessage---短信详单", "短信详单信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【短信信息】已采集完成");
				} else {
					tracer.addTag("TelecomchongqingService.getMessage---短信详单", "短信详单信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，【短信信息】采集完成");

				}

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_message", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getMessage---短信详单源码", "短信详单源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");
				tracer.addTag("TelecomchongqingService.getMessage.webParam is null", taskMobile.getTaskid());
			}
		} catch (RuntimeException e) {
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录", yearmonth,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", "系统超时", 1);
			mobileDataErrRecRepository.save(dataErrRec);

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");

			tracer.addTag("TelecomchongqingService.getMessage.MobileDataErrRec" + yearmonth, taskMobile.getTaskid());

		} catch (Exception e) {

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");

			tracer.addTag("TelecomchongqingService.getMessage---ERROR---" + taskMobile.getTaskid(),
					eutils.getEDetail(e));
		}
	}

	
	/**
	 * 上网详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getFlow(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth, String beginTime,
			String endTime) {
		tracer.addTag("TelecomchongqingService.getFlow", taskMobile.getTaskid());

		try {
			WebParam<TelecomChongqingFlow> webParam = telecomchongqingParser.getFlow(taskMobile, messageLogin,
					yearmonth, beginTime, endTime);

			if (null != webParam) {

				telecomChongqingFlowRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomchongqingService.getFlow---上网详单", "上网详单信息已入库!" + taskMobile.getTaskid());

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_flow", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getFlow---上网详单源码", "上网详单源码表入库!" + taskMobile.getTaskid());

			} else {

				tracer.addTag("TelecomchongqingService.getFlow.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingService.getFlow---ERROR---" + taskMobile.getTaskid(), eutils.getEDetail(e));
		}
	}
	
	
	/**
	 * 增值详单
	 * @param taskMobile
	 * @param messageLogin
	 * @param yearmonth
	 * @param beginTime
	 * @param endTime
	 */
	public void getIncrementBusiness(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth, String beginTime,
			String endTime) {
		tracer.addTag("TelecomchongqingService.getIncrementBusiness", taskMobile.getTaskid());

		try {
			WebParam<TelecomChongqingIncrement> webParam = telecomchongqingParser.getIncrementBusiness(taskMobile, messageLogin,
					yearmonth, beginTime, endTime);

			if (null != webParam) {

				telecomChongqingIncrementBusinessRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomchongqingService.getIncrementBusiness---增值详单", "增值详单信息已入库!" + taskMobile.getTaskid());

				TelecomChongqingHtml telecomChongqingHtml = new TelecomChongqingHtml(taskMobile.getTaskid(),
						"telecom_chongqing_incrementbusiness", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomChongqingHtmlRepository.save(telecomChongqingHtml);

				tracer.addTag("TelecomchongqingService.getIncrementBusiness---增值详单源码", "增值详单源码表入库!" + taskMobile.getTaskid());

			} else {

				tracer.addTag("TelecomchongqingService.getIncrementBusiness.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomchongqingService.getIncrementBusiness---ERROR---" + taskMobile.getTaskid(),
					eutils.getEDetail(e));
		}
	}


}