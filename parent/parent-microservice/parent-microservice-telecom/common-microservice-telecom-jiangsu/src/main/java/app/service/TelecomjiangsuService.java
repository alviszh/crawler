package app.service;

import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBalance;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBill;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBillSum;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuCallRecord;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuHtml;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuMessage;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuPay;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuBalanceRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuBillRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuBillSumRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuMessageRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuPayRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.telecom.htmlparse.TelecomjiangsuParser;
import app.exceptiondetail.EUtils;
import app.service.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jiangsu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jiangsu")
public class TelecomjiangsuService implements ICrawlerLogin{

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomjiangsuParser telecomjiangsuParser;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TelecomJiangsuHtmlRepository telecomJiangsuHtmlRepository;

	@Autowired
	private TelecomJiangsuUserInfoRepository telecomJiangsuUserInfoRepository;

	@Autowired
	private TelecomJiangsuBusinessRepository telecomJiangsuBusinessRepository;

	@Autowired
	private TelecomJiangsuPayRepository telecomJiangsuPayRepository;

	@Autowired
	private TelecomJiangsuBalanceRepository telecomJiangsuBalanceRepository;

	@Autowired
	private TelecomJiangsuCallRecordRepository telecomJiangsuCallRecordRepository;

	@Autowired
	private TelecomJiangsuMessageRepository telecomJiangsuMessageRepository;

	@Autowired
	private TelecomJiangsuBillRepository telecomJiangsuBillRepository;

	@Autowired
	private TelecomJiangsuBillSumRepository telecomJiangsuBillSumRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

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
	/**
	 * 再次登录
	 * @param taskMobile
	 * @param messageLogin
	 * @throws Exception
	 */
	public void loginAgain(TaskMobile taskMobile ,  MessageLogin messageLogin,int i) throws Exception {

		tracer.addTag("parser.crawler.loginAgain", taskMobile.getTaskid());
//			WebClient webClient = telecomjiangsuParser.addcookie(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
//			String url = "http://js.189.cn/nservice/login/toLogin?favurl=http://js.189.cn/index";
			String url = "http://js.189.cn/nservice/login/toLogin";
			HtmlPage html = telecomjiangsuParser.getHtml(url, webClient, taskMobile);
			
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='cellphone']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/div/input");
			username.setText(messageLogin.getName());
			passwordInput.setText(messageLogin.getPassword());
			HtmlImage valiCodeImg = html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/p[2]/span/img");
			
			String code = "1902";
			try {
				code = chaoJiYingOcrService.getVerifycode(valiCodeImg, "1902");
			} catch (Exception e) {
				tracer.addTag("TelecomjiangsuService.loginAgain---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			
			HtmlTextInput valicodeStrinput = (HtmlTextInput) html.getFirstByXPath("/html/body/div[2]/div[2]/div/div[2]/form/p[2]/input");
			valicodeStrinput.reset();
			valicodeStrinput.setText(code);
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='login_byPhone']");
			HtmlPage htmlPage = button.click();
			String contentAsString = htmlPage.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(contentAsString);
			Element showTimeMsgPupup = doc.getElementById("showTimeMsgPupup");
			
			if (showTimeMsgPupup != null) {
				String text = showTimeMsgPupup.text();
				if (text.contains("验证码错误") && i < 4) {
					loginAgain(taskMobile, messageLogin, ++i);
					return;
				}
				if (text != null && !text.equals("") && !text.contains("验证码错误")) {
					taskMobile.setDescription(text);
				}else{
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
				}
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskMobile.setError_message(text);
				taskMobileRepository.save(taskMobile);
			} else {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
				
//				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhase());
//				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getPhasestatus());
//				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getDescription());
//				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_LOGINTWO_SUCCESS.getError_code());
				String cookieString = CommonUnit.transcookieToJson(html.getWebClient());
				taskMobile.setCookies(cookieString);
				taskMobileRepository.save(taskMobile);
			}
			
		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuService.transfer---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Nine.getMessage());
			
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
//			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskMobileRepository.save(taskMobile);
		}
		webClient.close();

	}

	// 获取手机验证码
	@Async
	public void getPhoneCode(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			tracer.addTag("parser.crawler.taskid.getPhoneCode", taskMobile.getTaskid());

			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_DONING.getDescription());
			// 发送验证码状态更新
			save(taskMobile);
			String errorDescription = telecomjiangsuParser.getphonecode(messageLogin, taskMobile);
			if ("SUCCESS".equals(errorDescription)) {

			} else {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
			}

		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuService.getPhoneCode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
		}

	}

	// 手机短信验证码验证
	@Async
	public void verificationcode(MessageLogin mssageLogin, TaskMobile taskMobile) {
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		// 手机验证码验证状态更新
		save(taskMobile);
		try {
			String msg = telecomjiangsuParser.verificationcode(taskMobile, mssageLogin);
			tracer.addTag("TelecomjiangsuService.verificationcode", taskMobile.getTaskid() + "---msg:" + msg);
			if ("SUCCESS".equals(msg)) {
				
			} else {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("短信验证码验证失败!");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuService.verificationcode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
		}

	}


	/**
	 * 账单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public  Future<String> getBillSum(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomjiangsuService.getBill---" + yearmonth, taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuBillSum> webParam = telecomjiangsuParser.getBillSum(taskMobile, messageLogin, yearmonth);

			if (null != webParam) {

				telecomJiangsuBillSumRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomjiangsuService.getBill---账单", "账单已入库!" + taskMobile.getTaskid());

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_billsum", yearmonth,
						webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);
				tracer.addTag("TelecomjiangsuService.getBill---账单源码", "账单源码表入库!" + taskMobile.getTaskid());
			} else {
				tracer.addTag("TelecomjiangsuService.getBill.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuService.getBill---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}


	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getUserInfo(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomjiangsuService.getUserInfo", taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuUserInfo> webParam = telecomjiangsuParser.getUserInfo(taskMobile);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuUserInfoRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getUserInfo---用户信息", "用户信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【用户信息】已采集完成");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【积分信息】已采集完成");
				} else {
					tracer.addTag("TelecomjiangsuService.getUserInfo---用户信息", "用户信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【用户信息】采集完成");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【积分信息】采集完成");
				}

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_userinfo", "1", webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
				crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getUserInfo.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");

			tracer.addTag("TelecomjiangsuService.getUserInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 账户明细
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBill(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomjiangsuService.getBill" + yearmonth, taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuBill> webParam = telecomjiangsuParser.getBill(taskMobile, yearmonth);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuBillRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getBill---账单明细" + yearmonth,
							"账单明细已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【账户信息】已采集完成");
				} else {
					tracer.addTag("TelecomjiangsuService.getBill---账单明细" + yearmonth,
							"账单明细未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【账户信息】采集完成");
				}

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_bill", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getBill---账单明细源码" + yearmonth,
						"账单明细源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getBill.webParam is null" + yearmonth, taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");

			tracer.addTag("TelecomjiangsuService.getBill---ERROR" + yearmonth+"---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 在用业务情况
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBusiness(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomjiangsuService.getBusiness", taskMobile.getTaskid());
		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuBusiness> webParam = telecomjiangsuParser.getBusiness(taskMobile, messageLogin);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuBusinessRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getBusiness---在用业务情况",
							"在用业务情况-----套餐信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【业务信息】已采集完成");
				} else {
					tracer.addTag("TelecomjiangsuService.getBusiness---在用业务情况",
							"在用业务情况-----套餐信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【业务信息】采集完成");
				}

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_business", "1", webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getBusiness---在用业务情况-----套餐信息源码",
						"在用业务情况-----套餐信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getBusiness.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");

			tracer.addTag("TelecomjiangsuService.getBusiness---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 在用业务情况Two
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBusinessTwo(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomjiangsuService.getBusinessTwo", taskMobile.getTaskid());
		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuBusiness> webParam = telecomjiangsuParser.getBusinessTwo(taskMobile, messageLogin);

			if (null != webParam) {

				telecomJiangsuBusinessRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomjiangsuService.getBusinessTwo---在用业务情况", "在用业务情况已入库!" + taskMobile.getTaskid());

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_business", "2", webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getBusinessTwo---在用业务情况源码",
						"在用业务情况源码表入库!" + taskMobile.getTaskid());

			} else {

				tracer.addTag("TelecomjiangsuService.getBusinessTwo.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			tracer.addTag("TelecomjiangsuService.getBusinessTwo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

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
	public Future<String> getPay(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomjiangsuService.getPay" + yearmonth, taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuPay> webParam = telecomjiangsuParser.getPay(taskMobile, messageLogin, yearmonth);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuPayRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getPay---充值缴费" + yearmonth,
							"充值缴费信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【缴费信息】已采集完成");
				} else {

					tracer.addTag("TelecomjiangsuService.getPay---充值缴费" + yearmonth,
							"充值缴费信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【缴费信息】采集完成");

				}

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_pay", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getPay---充值缴费源码" + yearmonth,
						"充值缴费源码表入库!" + taskMobile.getTaskid());

			} else {

				crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getPay.webParam is null" + yearmonth, taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");
			tracer.addTag("TelecomjiangsuService.getPay---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 余额变动明细
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getBalanceRecord(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth) {
		tracer.addTag("TelecomjiangsuService.getBalanceRecord" + yearmonth, taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuBalance> webParam = telecomjiangsuParser.getBalanceRecord(taskMobile, yearmonth);

			if (null != webParam) {

				telecomJiangsuBalanceRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomjiangsuService.getBalanceRecord---余额变动明细" + yearmonth,
						"余额变动明细已入库!" + taskMobile.getTaskid());

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_balance", yearmonth, webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getBalanceRecord---余额变动明细源码" + yearmonth,
						"余额变动明细源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomjiangsuService.getBalanceRecord.webParam is null" + yearmonth,
						taskMobile.getTaskid());
			}
		} catch (Exception e) {

			tracer.addTag("TelecomjiangsuService.getBalanceRecord---ERROR" + yearmonth+"---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 通话详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getCallRecord(TaskMobile taskMobile, MessageLogin messageLogin, String beginTime,
			String endTime) {
		tracer.addTag("TelecomjiangsuService.getCallRecord", taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuCallRecord> webParam = telecomjiangsuParser.getCallRecord(taskMobile, beginTime,
					endTime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuCallRecordRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getCallRecord---通话详单", "通话详单信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【通讯信息】已采集完成");
				} else {
					tracer.addTag("TelecomjiangsuService.getCallRecord---通话详单",
							"通话详单信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，【通讯信息】采集完成");
				}
				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_callrecord", beginTime + "-" + endTime, webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getCallRecord---通话详单源码", "通话详单源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getCallRecord.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

			tracer.addTag("TelecomjiangsuService.getCallRecord---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	/**
	 * 短信详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getMessage(TaskMobile taskMobile, MessageLogin messageLogin, String beginTime,
			String endTime) {
		tracer.addTag("TelecomjiangsuService.getMessage", taskMobile.getTaskid());

		try {
			taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
			WebParam<TelecomJiangsuMessage> webParam = telecomjiangsuParser.getMessage(taskMobile, beginTime, endTime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomJiangsuMessageRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomjiangsuService.getMessage---短信详单", "短信详单信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【短信信息】已采集完成");
				} else {
					tracer.addTag("TelecomjiangsuService.getMessage---短信详单", "短信详单信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，【短信信息】采集完成");

				}

				TelecomJiangsuHtml telecomjiangsuHtml = new TelecomJiangsuHtml(taskMobile.getTaskid(),
						"telecom_jiangsu_message", beginTime + "-" + endTime, webParam.getUrl(), webParam.getHtml());
				telecomJiangsuHtmlRepository.save(telecomjiangsuHtml);

				tracer.addTag("TelecomjiangsuService.getMessage---短信详单源码", "短信详单源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");

				tracer.addTag("TelecomjiangsuService.getMessage.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");

			tracer.addTag("TelecomjiangsuService.getMessage---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return new AsyncResult<String>("200");
	}

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Async
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		try {
			loginAgain(taskMobile, messageLogin, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

}