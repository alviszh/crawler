package app.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomHenanParser;
import app.service.aop.ICrawler;
import app.service.aop.ISms;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.henan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.henan")
public class TelecomHenanService implements ICrawler, ISms{
	
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private AsyncGetDataService asyncGetDataService;
	@Autowired
	private TelecomHenanParser telecomHenanParser;
	@Autowired
	private TracerLog tracer;
	
	//发送短信验证码
	@Override
	public TaskMobile sendSms(MessageLogin messageLogin){
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.sendSms", taskMobile.getTaskid());
		WebParam webParam = telecomHenanParser.sendSms(taskMobile);
		if(null != webParam && null != webParam.getPage()){
			String xmlstr = webParam.getPage().getWebResponse().getContentAsString();
			try {
				Document document = DocumentHelper.parseText(xmlstr);
				Element rootElement = document.getRootElement();
				Element resultMessage = rootElement.element("resultMessage");
				String resultMessage2 = resultMessage.getText();
				Element flag = rootElement.element("flag");
				String flag2 = flag.getText();
				if(flag2.contains("0") && resultMessage2 == ""){
					String prodtype = webParam.getHtml();
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskMobile.setCookies(cookies);
					taskMobile.setNexturl(prodtype);//将每个用户的prodtype存放到库中，以备以后传参使用
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					telecomCommonService.save(taskMobile);
					tracer.addTag("parser.telecom.crawler.sendSms.status.success", "短信验证码发送成功");
				}else{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
					if(resultMessage2.contains("请等待30分钟后在发送")){
						resultMessage2 = "您的账户在10分钟内通过不同的IP登录，"+resultMessage2;
					}
					taskMobile.setDescription(resultMessage2);
					telecomCommonService.save(taskMobile);
					tracer.addTag("parser.telecom.crawler.sendSms.status.error", "短信验证码发送失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.sendSms.Exception", e.getMessage());
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
				telecomCommonService.save(taskMobile);
				tracer.addTag("parser.telecom.crawler.sendSms.status.error", "短信验证码发送失败");
			}
		}else{
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			telecomCommonService.save(taskMobile);
			tracer.addTag("parser.telecom.crawler.sendSms.status.error", "短信验证码发送失败");
		}
		return taskMobile;
	}
	
	//验证短信验证码
	@Override
	@Async
	public TaskMobile verifySms(MessageLogin messageLogin) {
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		tracer.addTag("parser.telecom.crawler.setSmscode", taskMobile.getTaskid());
		try {
			WebParam webParam = telecomHenanParser.setSmscode(taskMobile,messageLogin);
			if(null != webParam.getHtmlPage()){
				String string = webParam.getHtmlPage().asText().trim();
				if(!string.equals("") && !string.contains("点击获取详单查询短信验证码")){			//验证成功
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					String cookies = CommonUnit.transcookieToJson(webParam.getHtmlPage().getWebClient());
					taskMobile.setCookies(cookies);
					tracer.addTag("parser.telecom.crawler.setSmscode.succsess", "验证成功");
				}else{
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
					taskMobile.setDescription("您输入的查询验证码错误或过期，请重新核对或再次获取！");
					tracer.addTag("parser.telecom.crawler.setSmscode.error", "您输入的查询验证码错误或过期，请重新核对或再次获取！");
				}
			}else{
				taskMobile.setFinished(true);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
				taskMobile.setDescription("网络异常，身份认证未通过");
				tracer.addTag("parser.telecom.crawler.setSmscode.error", "网络异常，身份认证未通过");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.setSmscode.Exception", e.getMessage());
			taskMobile.setFinished(true);
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
			taskMobile.setDescription("网络异常，身份认证未通过");
			tracer.addTag("parser.telecom.crawler.setSmscode.error2", "网络异常，身份认证未通过");
		}
		telecomCommonService.save(taskMobile);
		return taskMobile;
	}
	
	
	//数据爬取总方法
	@Override
	@Async
	public TaskMobile getAllData(MessageLogin messageLogin){
		
		TaskMobile taskMobile = telecomCommonService.findtaskMobile(messageLogin.getTask_id());
		taskMobile.setFamilyMsgStatus(201);
		taskMobile.setIntegralMsgStatus(201);
		telecomCommonService.save(taskMobile);
		if(null != taskMobile){
			try {
				asyncGetDataService.getUserInfo(taskMobile);				//获取个人信息
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getUserInfo.error", e.getMessage());
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]用户信息爬取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			try {
				asyncGetDataService.getPaymentHistory(taskMobile);			//获取缴费信息
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getPaymentHistory.error", e.getMessage());
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]充值缴费记录获取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			try {
				asyncGetDataService.getBillData(taskMobile);				//获取账单信息
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getBillData.error", e.getMessage());
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]充值缴费记录获取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			try {
				asyncGetDataService.getServer(taskMobile);					//获取在用业务信息
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getServer.error", e.getMessage());
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]在用业务信息爬取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			try {
				asyncGetDataService.getCallDetails(taskMobile);				//获取通话详单
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getCallDetails.error", e.getMessage());
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "[数据采集中]通话详单记录获取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			try {
				asyncGetDataService.getSMSDetails(taskMobile);				//获取短信详单
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.telecom.crawler.getAllData.getSMSDetails.error", e.getMessage());
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "[数据采集中]短信详单记录获取完成");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
			
		}
		return taskMobile;
		
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
