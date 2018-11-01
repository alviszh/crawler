package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;
import app.service.aop.ISms;
import app.service.common.LoginAndGetSiChuan;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.sichuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.sichuan")
public class TelecomSiChuanService implements ISms,ICrawler{
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomUnitSiChuanService telecomUnitSiChuanService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	Map<String, Future<String>> listfuture = new HashMap<>();
	public TaskMobile findtaskMobile(String taskid) {

		return taskMobileRepository.findByTaskid(taskid);
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	

	//获取手机验证码
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		TaskMobile taskMobile =taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		try {
			WebParamTelecom webParamTelecom = LoginAndGetSiChuan.getphonecode(messageLogin,taskMobile);
			if (webParamTelecom == null) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
				// 发送验证码状态更新
				save(taskMobile);
			}
			tracer.addTag("电信 四川获取验证码", webParamTelecom.getHtml());
			String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());

			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
			taskMobile.setCookies(cookieString);
			// 发送验证码状态更新
			save(taskMobile);
		} catch (Exception e) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 发送验证码状态更新
			save(taskMobile);
			tracer.addTag("parser.crawler.auth", e.toString());
		}
		return taskMobile;
	}


	//手机短信验证码验证
	@Async
	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile =taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		tracer.addTag("parser.crawler.taskid",taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_DONING.getDescription());
		// 手机验证码验证状态更新
		save(taskMobile);
		WebParamTelecom  webParamTelecom = LoginAndGetSiChuan.setphonecode(messageLogin, taskMobile);
		
		if (webParamTelecom == null) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			// 手机验证码验证失败状态更新
			save(taskMobile);
			return taskMobile;
		} else if (webParamTelecom.getHtml().indexOf("证件号码输入有误！") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_ID_Verific_ERROR.getPhase());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_ID_Verific_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_ID_Verific_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_ID_Verific_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			System.out.println("验证码验证失败");
			return taskMobile;
		}  else if (webParamTelecom.getHtml().indexOf("短信随机码不正确！") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			System.out.println("验证码验证失败");
			return taskMobile;
		} else if (webParamTelecom.getHtml().indexOf("短信验证码失效！") != -1) {
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_THREE.getMessage());
			// 手机验证码验证失败对不起，系统忙，请稍后再试状态更新
			save(taskMobile);
			return taskMobile;
		} else {
			String html = webParamTelecom.getHtml();
			System.out.println(html);
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
			String cookieString = CommonUnit.transcookieToJson(webParamTelecom.getWebClient());
			taskMobile.setCookies(cookieString);
			// 手机验证码验证成功状态更新
			save(taskMobile);
		}				
		return taskMobile;
	}
	
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		TaskMobile taskMobile =taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		webClient = telecomUnitSiChuanService.getInitMy189homeWebClient(messageLogin, taskMobile);
		/***
		 * 功能
		 */
		//个人信息 
		try {
			Future<String> userInfo = telecomUnitSiChuanService.getUserInfo(messageLogin,taskMobile);//成功
			listfuture.put("getUserInfo", userInfo);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getUserInfo" + e.toString());
		}
		
		//月账单
		try {
			for(int i = 0 ; i < 6; i++){
				try {
					Future<String> phoneBill = telecomUnitSiChuanService.getPhoneBill(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getPhoneBill", phoneBill);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
					continue;
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
				}
			}//成功
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getPhoneBill" + e.toString());
		}
		
		//缴费信息
		try {
			for(int i = 0 ; i < 6; i++){
				try {
					Future<String> payMent = telecomUnitSiChuanService.getPayMent(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getPayMent", payMent);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
					continue;
				}
				
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
				}
				
			}	
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getPayMent" + e.toString());
		}
		//消费积分
		try {
			for(int i = 0 ; i < 6; i++){
				try {
					Future<String> consumptionPoints = telecomUnitSiChuanService.getConsumptionPoints(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getConsumptionPoints", consumptionPoints);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
					continue;
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
				}
			}//成功
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getConsumptionPoints" + e.toString());
		}
		
		//我的业务
		try {
			Future<String> businessMessage = telecomUnitSiChuanService.getBusinessMessage(messageLogin,taskMobile);//成功
			listfuture.put("getBusinessMessage", businessMessage);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getBusinessMessage" + e.toString());
		}
		
		//通话详单
		try {
			for(int i = 0 ; i < 6; i++){
				try {
					Future<String> callThrem = telecomUnitSiChuanService.getCallThrem(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getCallThrem", callThrem);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
					continue;
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
				}

			}//成功
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getCallThrem" + e.toString());
		}
		
		//短信详单
		try {
			for(int i = 0 ; i < 6; i++){
				try {
					Future<String> billDetail = telecomUnitSiChuanService.getBillDetail(webClient,messageLogin,taskMobile,i);
					listfuture.put(i+"getBillDetail", billDetail);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
					continue;
				}
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					tracer.addTag("parser.crawler.auth", e.toString());
				}

			}//成功
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("parser.crawler.auth", "getBillDetail" + e.toString());
		}
		
		System.out.println("11111111111");
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			taskMobile = crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			return taskMobile;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService-listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
			return taskMobile;
		}
		
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
