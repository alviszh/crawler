package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Account;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Bill;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1CallRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Flow;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Html;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Message;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Order;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1PayInfo;
import com.microservice.dao.entity.crawler.telecom.shanxi1.TelecomShanxi1Starlevel;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1AccountRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1BillRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1FlowRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1HtmlRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1MessageRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1OrderRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1PayInfoRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1RecordRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1StarlevelRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1UserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.telecom.htmlparse.TelecomShanxi1Parser;
import app.exceptiondetail.EUtils;
import app.service.aop.ISms;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shanxi1")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shanxi1")
public class TelecomShanxi1Service implements ISms{

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomUnitService telecomUnitService;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private TelecomShanxi1Parser telecomShanxiParser;

	@Autowired
	private TelecomShanxi1UserInfoRepository telecomShangxi1UserInfoRepository;

	@Autowired
	private TelecomShanxi1HtmlRepository telecomShanxi1HtmlRepository;

	@Autowired
	private TelecomShanxi1PayInfoRepository telecomShanxi1PayInfoRepository;

	@Autowired
	private TelecomShanxi1OrderRepository telecomShanxi1OrderRepository;

	@Autowired
	private TelecomShanxi1AccountRepository telecomShanxi1AccountRepository;

	@Autowired
	private TelecomShanxi1MessageRepository telecomShanxi1MessageRepository;

	@Autowired
	private TelecomShanxi1FlowRepository telecomShanxi1FlowRepository;

	@Autowired
	private TelecomShanxi1RecordRepository telecomShanxi1RecordRepository;

	@Autowired
	private TelecomShanxi1BillRepository telecomShanxi1BillRepository;

	@Autowired
	private TelecomShanxi1StarlevelRepository telecomShanxi1StarlevelRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	public TaskMobile verificationName(TaskMobile taskMobile) {

		try {
			HtmlPage htmlpage = telecomUnitService.verification(taskMobile);
			if (htmlpage == null) {
				
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
				// 验证失败状态存储
				save(taskMobile);
				return taskMobile;
				
			} else {
				HtmlSpan loginSpan = htmlpage.querySelector("#zmzqrmessSpan");
				if (null == loginSpan) {

					tracer.addTag("TelecomShanxi1Service.verificationName.loginSpan is null", taskMobile.getTaskid());

					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
					
					// 验证成功状态更新
					save(taskMobile);
					return taskMobile;

				} else {

					String asText = loginSpan.asText();

					tracer.addTag("TelecomShanxi1Service.verificationName.asText",
							asText + "------taskId:" + taskMobile.getTaskid());

					if (asText.contains("验证通过")) {

						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus());
						taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getDescription());
						taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getError_code());
						taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
						String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
						taskMobile.setCookies(cookieString);
						// 验证成功状态更新
						save(taskMobile);
						return taskMobile;

					} else {
						if (asText.contains("验证码")) {
							asText = "身份信息验证失败!";
							tracer.addTag("verificationName.asText------taskId:" + taskMobile.getTaskid(),
									"超级鹰解析验证码失败："+asText);
						}
						/**
						 * if (asText.contains("请填写正确的姓名") ||
						 * asText.contains("请填写正确的的证件号码") ||
						 * asText.contains("信息验证失败"))
						 */
						// 实名认证用二次验证枚举
						taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
						taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
						taskMobile.setDescription(asText);
						taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
						taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
						// 验证失败状态存储
						save(taskMobile);
						return taskMobile;
					}
				}

			}

		} catch (Exception e) {

			tracer.addTag("TelecomService.verificationName.ERROR:taskid---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			// 实名认证用二次验证枚举
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_QUERY_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());

			// 验证失败状态更新
			save(taskMobile);
		}
		return taskMobile;
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
	 * 山西用户个人信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getUserInfo(TaskMobile taskMobile) {
		tracer.addTag("TelecomShanxi1Service.getUserInfo", taskMobile.getTaskid());

		tracer.addTag("parser.crawler.getUserinfo", taskMobile.getTaskid());

		try {

			WebParam webParam = telecomShanxiParser.getUserInfo(taskMobile);

			if (null != webParam) {
				if (null != webParam.getTelecomShanxi1UserInfo()) {

					telecomShangxi1UserInfoRepository.save(webParam.getTelecomShanxi1UserInfo());

					tracer.addTag("TelecomShanxi1Service.getUserInfo 个人信息", "个人信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【用户信息】已采集完成");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【积分信息】已采集完成");
				} else {
					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500,
							"数据采集中，【积分信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_userinfo", "1", webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getUserInfo 个人信息源码", "个人信息源码表入库!" + taskMobile.getTaskid());

			} else {

				tracer.addTag("TelecomShanxi1Service.getUserInfo.webParam is null", taskMobile.getTaskid());
				crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
				crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");

			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");

			tracer.addTag("TelecomShanxi1Service.getUserInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

	}

	/**
	 * 山西用户缴费信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getPayInfo(TaskMobile taskMobile, String startDate, String endDate) {
		tracer.addTag("TelecomShanxi1Service.getPayInfo", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1PayInfo> webParam = telecomShanxiParser.getPayInfo(taskMobile, startDate, endDate);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1PayInfoRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getPayInfo 缴费信息", "缴费信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【缴费信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getPayInfo 缴费信息", "缴费信息未采集到数据!" + taskMobile.getTaskid());
					
					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【缴费信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_payinfo", startDate + "_" + endDate, webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getPayInfo 缴费信息源码", "缴费信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");
				tracer.addTag("TelecomShanxi1Service.getPayInfo.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");
			tracer.addTag("TelecomShanxi1Service.getPayInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * 山西用户产品信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getProduct(TaskMobile taskMobile) {
		tracer.addTag("TelecomShanxi1Service.getProduct", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Order> webParam = telecomShanxiParser.getProduct(taskMobile);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1OrderRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getProduct山西用户产品信息", "山西用户产品信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【业务信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getProduct山西用户产品信息", "山西用户产品信息未采集到数据!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【业务信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_order", "1", webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getProduct山西用户产品信息源码", "山西用户产品信息源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomShanxi1Service.getProduct is null", taskMobile.getTaskid());
				crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.getProduct---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【业务信息】采集完成");
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

	}

	/**
	 * 山西用户账户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getAccount(TaskMobile taskMobile, MessageLogin messageLogin) {
		tracer.addTag("TelecomShanxi1Service.getAccount", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Account> webParam = telecomShanxiParser.getAccount(taskMobile, messageLogin);

			if (null != webParam) {
				
				
				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1AccountRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getAccount山西用户账户信息", "山西用户账户信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【账户信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getAccount山西用户账户信息", "山西用户账户信息未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【账户信息】采集完成");
				}
				
				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_account", "1", webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getAccount山西用户账户信息源码", "山西用户账户信息源码表入库!" + taskMobile.getTaskid());

			} 
			else {
				crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");
				
				tracer.addTag("TelecomShanxi1Service.getAccount.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");
			tracer.addTag("TelecomShanxi1Service.getProduct---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * 获取我的短信详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getTelecomShanxi1Message(TaskMobile taskMobile, MessageLogin messageLogin, String starttime,
			String endtime) {
		tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Message> webParam = telecomShanxiParser.getTelecomShanxi1Message(taskMobile,
					messageLogin, starttime, endtime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1MessageRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message获取我的短信详单",
							"获取我的短信详单已入库!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【短信信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message获取我的短信详单",
							"获取我的短信详单未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，【短信信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_message", starttime + "_" + endtime, webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message获取我的短信详单源码",
						"获取我的短信详单源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message.webParam is null",
						taskMobile.getTaskid());
				crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【短信信息】采集完成");
			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * 获取我的流量详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getTelecomShanxi1Flow(TaskMobile taskMobile, MessageLogin messageLogin, String starttime,
			String endtime) {
		tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Flow> webParam = telecomShanxiParser.getTelecomShanxi1Flow(taskMobile, messageLogin,
					starttime, endtime);

			if (null != webParam) {

				telecomShanxi1FlowRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow获取我的流量详单",
						"获取我的流量详单已入库!" + taskMobile.getTaskid());

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_flow", starttime + "_" + endtime, webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow获取我的流量详单源码",
						"获取我的流量详单源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

	}

	/**
	 * 获取我的通话记录详单
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getTelecomShanxi1CallRecord(TaskMobile taskMobile, MessageLogin messageLogin, String starttime,
			String endtime) {
		tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1CallRecord> webParam = telecomShanxiParser.getTelecomShanxi1CallRecord(taskMobile,
					messageLogin, starttime, endtime);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1RecordRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord获取我的通话记录详单",
							"获取我的通话记录详单已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【通讯信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord获取我的通话记录详单",
							"获取我的通话记录详单未采集到数据!" + taskMobile.getTaskid());
					crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【通讯信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_callrecord", starttime + "_" + endtime, webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord获取我的通话记录详单源码",
						"获取我的通话记录详单源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

				tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord.webParam is null",
						taskMobile.getTaskid());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");

			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1CallRecord---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	/**
	 * 山西用户月账单消费
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getBillAll(TaskMobile taskMobile) {
		tracer.addTag("TelecomShanxi1Service.getBill", taskMobile.getTaskid());
		try {
			List<String> decade = AsyncShanxi1GetAllDataService.getDecade();
			for (String yearMonth : decade) {
				tracer.addTag("TelecomShanxi1Service.getDecade", taskMobile.getTaskid() + "---yearmonth" + yearMonth);
				getBill(taskMobile, yearMonth);
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.getBill---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		//没字段标识了，借用一下亲情号的字段，偷懒写法
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【亲情号信息】已采集完成");
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}
	
	/**
	 * 山西用户月账单消费
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getBill(TaskMobile taskMobile, String yearMonth) {
		tracer.addTag("TelecomShanxi1Service.getBill"+yearMonth, taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Bill> webParam = telecomShanxiParser.getBill(taskMobile, yearMonth);

			if (null != webParam) {

				telecomShanxi1BillRepository.saveAll(webParam.getList());

				tracer.addTag("TelecomShanxi1Service.getBill 月账单消费信息"+yearMonth, "月账单消费信息已入库!" + taskMobile.getTaskid());

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_bill", yearMonth, webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);

				tracer.addTag("TelecomShanxi1Service.getBill 月账单消费信息源码"+yearMonth, "月账单消费信息源码表入库!" + taskMobile.getTaskid());

			} else {
				tracer.addTag("TelecomShanxi1Service.getBill.webParam is null"+yearMonth, taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.getBill---ERROR---"+yearMonth+"---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

	}

	/**
	 * 山西用户星级服务信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getStarlevel(TaskMobile taskMobile) {
		tracer.addTag("TelecomShanxi1Service.getStarlevel", taskMobile.getTaskid());

		try {

			WebParam<TelecomShanxi1Starlevel> webParam = telecomShanxiParser.getStarlevel(taskMobile);

			if (null != webParam) {

				if (null != webParam.getList() && webParam.getList().size() > 0) {

					telecomShanxi1StarlevelRepository.saveAll(webParam.getList());

					tracer.addTag("TelecomShanxi1Service.getStarlevel---山西用户星级服务信息",
							"山西用户星级服务信息已入库!" + taskMobile.getTaskid());

					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), webParam.getCode(),
							"数据采集中，【账户信息】已采集完成");
				} else {
					tracer.addTag("TelecomShanxi1Service.getStarlevel---山西用户星级服务信息",
							"山西用户星级服务信息未采集到数据!" + taskMobile.getTaskid());
					
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200,
							"数据采集中，【账户信息】采集完成");
				}

				TelecomShanxi1Html telecomShanxi1Html = new TelecomShanxi1Html(taskMobile.getTaskid(),
						"telecom_shanxi1_starlevel", "1", webParam.getUrl(), webParam.getHtml());
				telecomShanxi1HtmlRepository.save(telecomShanxi1Html);
				

				tracer.addTag("TelecomShanxi1Service.getStarlevel---山西用户星级服务信息源码",
						"山西用户星级服务信息源码表入库!" + taskMobile.getTaskid());

			} else {
				crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");
				tracer.addTag("TelecomShanxi1Service.getStarlevel.webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【账户信息】采集完成");

			tracer.addTag("TelecomShanxi1Service.getStarlevel---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

	// 获取手机验证码
	@Async
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile = findtaskMobile(messageLogin.getTask_id());
		
		taskMobile = verificationName(taskMobile);
		if (!StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhase().equals(taskMobile.getPhase())
				&& !StatusCodeEnum.TASKMOBILE_READY_CODE_DONING.getPhasestatus().equals(taskMobile.getPhase_status())) {
			return taskMobile;
		}
		try {
			tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
			WebClient webClient = telecomShanxiParser.addcookie(taskMobile);
			String urlData = "http://www.189.cn/bss/sms/sendcode.do?flag=0&callback=jQuery111204100412069723336_1539072059786&_=1539072059789";

//			String urlData = "http://www.189.cn/bss/sms/sendcode.do?phoneNum=" + messageLogin.getName()
//					+ "&callback=jQuery111208221578666113154_1503569715993&_=1503569715997";

			Page page = telecomShanxiParser.getPage(webClient, taskMobile, urlData, null);

			String html = page.getWebResponse().getContentAsString();

			tracer.addTag("TelecomShanxiParser.getphonecode---验证码返回数据:", taskMobile.getTaskid() + "---html:" + html);

			if(!html.startsWith("{")){
				html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));
			}
			JSONObject jsonObj = JSONObject.fromObject(html);
			String code = jsonObj.getString("code");
			String errorDescription = jsonObj.getString("errorDescription");
			
//			HtmlPage htmlpage = telecomShanxiParser.getphonecode(messageLogin, taskMobile);
			
			if ("0".equals(code)) {

				tracer.addTag("电信获取验证码---taskId:" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				String cookieString = CommonUnit.transcookieToJson(webClient);

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				taskMobile.setCookies(cookieString);
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}
			else if (StringUtils.isNotBlank(errorDescription) && !"null".equals(errorDescription)) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(errorDescription);
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			} else {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
				return taskMobile;
			}

		} catch (Exception e) {
			tracer.addTag("TelecomShanxi1Service.getPhoneCode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
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
	public TaskMobile verifySms(MessageLogin mssageLogin) {
		TaskMobile taskMobile = findtaskMobile(mssageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskMobile.getTaskid());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String endtime = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
		String starttime = dateFormat.format(calendar.getTime());
		String htmlpage = "";
		try {
			tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord", taskMobile.getTaskid());
			String urlData = "http://www.189.cn/bss/billing/provincebillrecord.do?&callback=jQuery1112005879917732313844_1503539936060&accounttype=1&account="
					+ mssageLogin.getName() + "&type=1&needRandomCodeFlag=1&randomCode=" + mssageLogin.getSms_code()
					+ "&starttime=" + starttime + "&endtime=" + endtime + "&_=1503539936068";
			WebClient webClient = telecomShanxiParser.addcookie(taskMobile);
			try {
				Page page = telecomShanxiParser.getPage(webClient, taskMobile, urlData, null);
				if (null != page) {
					String html = page.getWebResponse().getContentAsString();
					tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord 验证验证码---通话记录详单" + taskMobile.getTaskid(),
							"<xmp>" + html + "</xmp>");
					html = html.substring(html.lastIndexOf("(") + 1, html.lastIndexOf(")"));

					JSONObject jsonObj = JSONObject.fromObject(html);

					htmlpage = jsonObj.getString("errorDescription");
				}
			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.getTelecomShanxi1CallRecord---ERROR---"+
						taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			
			try {
				String userInfourl = "http://www.189.cn//dqmh/ssoLink.do?method=linkTo&platNo=10007&toStUrl=http://sx.189.cn/service/jf/integralSearch.action";
				Page page = telecomShanxiParser.getPage(webClient, taskMobile, userInfourl, null);
				if (null != page) {
					String html = page.getWebResponse().getContentAsString();
					tracer.addTag("TelecomShanxiParser.userInfourl" + taskMobile.getTaskid(),
							"<xmp>" + html + "</xmp>");
				}
			} catch (Exception e) {
				tracer.addTag("TelecomShanxiParser.userInfourl---ERROR---"+
						taskMobile.getTaskid() ,eutils.getEDetail(e));
			}
			
			
//			String htmlpage = telecomShanxiParser.verificationcode(taskMobile, mssageLogin, starttime, endtime);
			tracer.addTag("TelecomShanxi1Service.verificationcode", taskMobile.getTaskid() + "---htmlpage:" + htmlpage);
			if (StringUtils.isBlank(htmlpage)) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}else if ("null".equals(htmlpage)) {
				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskMobile.setCookies(cookieString);
				// 手机验证码验证成功状态更新
				save(taskMobile);
				return taskMobile;
			} else if ( htmlpage.contains("用户未登录") || htmlpage.contains("系统忙")) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription("系统繁忙,请稍后重试");
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}else{
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(htmlpage);
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 手机验证码验证失败状态更新
				save(taskMobile);
				return taskMobile;
			}
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
			tracer.addTag("TelecomShanxi1Service.verificationcode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
			return taskMobile;
		}

	}

}