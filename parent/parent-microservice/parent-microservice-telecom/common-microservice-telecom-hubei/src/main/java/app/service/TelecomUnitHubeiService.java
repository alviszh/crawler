package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiHtml;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiAccountRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiCallrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiPaymonthsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiPointrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiRechargesRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiServicesRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiSmsrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomHubeiHtmlUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomUnitHubeiService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiUserinfoRepository telecomHubeiUserinfoRepository;
	@Autowired
	private TelecomHubeiHtmlRepository telecomHubeiHtmlRepository;
	@Autowired
	private TelecomHubeiAccountRepository telecomHubeiAccountRepository;
	@Autowired
	private TelecomHubeiPaymonthsRepository telecomHubeiPaymonthsRepository;
	@Autowired
	private TelecomHubeiPointrecordsRepository telecomHubeiPointrecordsRepository;
	@Autowired
	private TelecomHubeiRechargesRepository telecomHubeiRechargesRepository;
	@Autowired
	private TelecomHubeiSmsrecordsRepository telecomHubeiSmsrecordsRepository;
	@Autowired
	private TelecomHubeiServicesRepository telecomHubeiServicesRepository;
	@Autowired
	private TelecomOtherPageUnitHubeiService  telecomOtherPageUnitHubeiService;
	@Autowired
	private TelecomHubeiCallrecordsRepository telecomHubeiCallrecordsRepository;
	@Autowired
	private TelecomHubeiHtmlUnit  telecomHubeiHtmlUnit;
	@Autowired
	private CrawlerStatusMobileService  crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer; 
	// 抓取用户信息
	public Boolean getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile){
		try {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			int count = 0;
			WebParam webParam = telecomHubeiHtmlUnit.getUserInfo(messageLogin, taskMobile, count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("userInfo");
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);		
				}
				// 保存用户信息
				if (null != webParam.getUserinfo()) {
					telecomHubeiUserinfoRepository.save(webParam.getUserinfo());
					tracer.addTag("parser.telecom.crawler.getUserInfo", "用户信息入库" +webParam.getUserinfo());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
					taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
		            taskMobile.setCookies(cookies);     
		            taskMobileRepository.save(taskMobile);
		            return true;
				}else{
					tracer.addTag("中国电信湖北抓取用户信息为空", messageLogin.getTask_id());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
				    return false;
				}
			} else {
				tracer.addTag("中国电信湖北用户信息抓取失败", messageLogin.getTask_id());	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
				return false;
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
			tracer.addTag("中国电信湖北抓取用户信息出错", messageLogin.getTask_id());
			e.printStackTrace();
		}
		tracer.addTag("中国电信抓取客户湖北用户基本信息", messageLogin.getTask_id());
		return false;
	}
	// 抓取账户信息
	@Async
	public void getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		try {					
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			int count=0;
			WebParam webParam = telecomHubeiHtmlUnit.getAccountInfo(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("accountInfo");
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);							
				}			
				// 保存账户信息
				if (null != webParam.getAccountinfo()) {
					telecomHubeiAccountRepository.save(webParam.getAccountinfo());
					tracer.addTag("parser.telecom.crawler.getAccountInfo", "账户信息入库" + webParam.getAccountinfo());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				} else {				
					tracer.addTag("中国电信湖北抓取账户信息为空", messageLogin.getTask_id());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}
			} else {
				tracer.addTag("中国电信湖北抓取账户信息为空", messageLogin.getTask_id());
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
    		crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
    		tracer.addTag("getAccountInfo.Exception", e.toString());
			e.printStackTrace();
		}	
		tracer.addTag("中国电信抓取客户湖北账户信息", messageLogin.getTask_id());
 }
	//抓取电信充值记录
	@Async
	public void getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取湖北电信充值记录", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());			
		try {
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				int count = 0;
				String month=getDateBefore("yyyyMM",  i);
				WebParam webParam = telecomHubeiHtmlUnit.getRechargeRecord(messageLogin, taskMobile, month, count);
				if (null != webParam.getHtml()) {
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("rechargeRecord"+month);
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);
				}
				if (null != webParam.getRecharges() && !webParam.getRecharges().isEmpty()) {
					telecomHubeiRechargesRepository.saveAll(webParam.getRecharges());
					temp++;
					tracer.addTag("parser.telecom.crawler.getRechargeRecord",
							month + "月充值缴费信息信息入库" + webParam.getRecharges());
				} else {
					tracer.addTag("parser.telecom.crawler.getRechargeRecord", month + "月充值缴费信息信息爬取结果为空");
				}				
			}
			if (temp > 0) {
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());	
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			} else {
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
    		tracer.addTag("getRechargeRecord.Exception", e.toString());
			e.printStackTrace();
		}
		tracer.addTag("抓取湖北电信充值记录", messageLogin.getTask_id());
	}
	//抓取湖北电信短信详情
	@Async
	public void getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取湖北电信短信详情", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());	
		try {
		    int temp=0;
			for (int i = 0; i < 6; i++) {				
			int count=0;
			String month=getDateBefore("yyyyMM",  i);
			WebParam webParam = telecomHubeiHtmlUnit.getSmsRecord(messageLogin, taskMobile,month,count);
				if (null !=webParam.getHtml()) {
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("smsRecord"+month);
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);		
				}				
				List<TelecomHubeiSmsrecords>  smsRecords=webParam.getSmsrecords();
				if (null != smsRecords && !smsRecords.isEmpty()) {
					telecomHubeiSmsrecordsRepository.saveAll(smsRecords);
					temp++;					
					tracer.addTag("parser.telecom.crawler.getSmsRecord"+month,"月抓取短信明细信息入库"+smsRecords);
				}else{
					tracer.addTag("parser.telecom.crawler.getSmsRecord"+month,month+"月抓取短信明细信息为空");
				}					
				if (null !=webParam.getNumber()) {
					tracer.addTag("短信详单总条数" + month, webParam.getNumber());
					int pageNumber = Integer.parseInt(webParam.getNumber()) / 20+ 1;
					tracer.addTag("短信详单总页数" + month, pageNumber + "");
					if (pageNumber>=2) {
						for (int j = 2; j <= pageNumber; j++) {
						    tracer.addTag(month+"短信详单第" + pageNumber+"页", pageNumber + "");
							telecomOtherPageUnitHubeiService.getOtherPageSmsRecord(messageLogin, taskMobile, month, j);
						}
					}				
				}				
			}
			if(temp>0){
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			}else{
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			}
		} catch (Exception e) {	
		    tracer.addTag("parser.telecom.crawler.getSmsRecord.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			e.printStackTrace();
		}
		tracer.addTag("抓取湖北电信短信详情", messageLogin.getTask_id());
	}
	// 抓取湖北电信语言详单
	@Async
	public void getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("抓取湖北电信语言详单", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				int count = 0;
				String month = getDateBefore("yyyyMM", i);
				WebParam webParam = telecomHubeiHtmlUnit.getVoiceRecord(messageLogin, taskMobile, month, count);
				if (null != webParam.getHtml()) {
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("callRecord"+month);
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);
				}
				if (null != webParam.getCallrecords() && !webParam.getCallrecords().isEmpty()) {
					telecomHubeiCallrecordsRepository.saveAll(webParam.getCallrecords());
					temp++;
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month,
							"语言详单细信息入库" + webParam.getCallrecords());
				} else {
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month, "语言详单信息爬取为空");
				}
				if (null != webParam.getNumber()) {
					tracer.addTag("通话详单总条数" + month, webParam.getNumber());
					int pageNumber = Integer.parseInt(webParam.getNumber()) / 20 + 1;
					tracer.addTag("通话详单总页数" + month, pageNumber + "");
					if (pageNumber >= 2) {
						for (int j = 2; j <= pageNumber; j++) {
							telecomOtherPageUnitHubeiService.getOtherPageVoiceRecord(messageLogin, taskMobile, month,j);
						}
					}
				}
			}
    		if (temp > 0) {
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			} else {
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getVoiceRecord.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());					
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}	
		tracer.addTag("抓取湖北电信语言详单", messageLogin.getTask_id());
	}
	// 抓取湖北套餐信息
	@Async
	public void getServiceInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		tracer.addTag("抓取套餐信息", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());	
		try {
			int count = 0;
			WebParam webParam = telecomHubeiHtmlUnit.getServiceInfo(messageLogin, taskMobile, count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {					
					TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("serviceInfo");
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);		
				}
				if (null != webParam.getServices() && !webParam.getServices().isEmpty()) {
					telecomHubeiServicesRepository.saveAll(webParam.getServices());
					tracer.addTag("parser.telecom.crawler.getServiceInfo", "套餐信息入库"+webParam.getServices());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}else{
					tracer.addTag("parser.telecom.crawler.getServiceInfo", "套餐信息结果为空");
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());					
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
					
				}			
			}else{
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());	
				tracer.addTag("parser.telecom.crawler.getServiceInfo", "套餐信息失败");
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
			tracer.addTag("parser.telecom.crawler.getServiceInfo.Exception", e.toString());
			e.printStackTrace();
		}	
	}
	// 抓取月账单信息
	@Async
	public void getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取湖北电信抓取月账单信息", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {		
			int temp=0;
			for (int i = 1; i <=6; i++) {
				String month= getDateBefore("yyyyMM",  i);
				int count=0;
			    WebParam webParam = telecomHubeiHtmlUnit.getPaymonths(messageLogin, taskMobile,month,count);
				if (null != webParam.getHtml()) {
            		TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("paymonth"+month);
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);		
				}
				if (null != webParam.getPaymonths() && !webParam.getPaymonths().isEmpty()) {
					telecomHubeiPaymonthsRepository.saveAll(webParam.getPaymonths());
					temp++;				
					tracer.addTag("parser.telecom.crawler.getPaymonths"+month, "月账单信息入库"+webParam.getPaymonths());					
				} else {				
					tracer.addTag("parser.telecom.crawler.getPaymonths"+month, "月账单信息结果为空");
				}
				if (temp>0) {
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());	
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}else{
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());	
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}
			}
		} catch (Exception e) {		
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());	
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.telecom.crawler.getPaymonths.Exception", e.toString());
			e.printStackTrace();
		}
	}
	// 抓取月积分信息
	@Async
	public void getPointrecords(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取湖北电信月积分信息", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int temp=0;
			for (int i = 0; i < 6; i++) {
			int count=0;
			String month= getDateBefore("yyyyMM",  i);
			WebParam webParam = telecomHubeiHtmlUnit.getPointRecord(messageLogin, taskMobile, month, count);
				if (null != webParam.getHtml()) {
            		TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
					telecomHubeiHtml.setPageCount(1);
					telecomHubeiHtml.setHtml(webParam.getHtml());
					telecomHubeiHtml.setType("points"+month);
					telecomHubeiHtml.setUrl(webParam.getUrl());
					telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiHtmlRepository.save(telecomHubeiHtml);		
				}
				if (null != webParam.getPointrecords() && !webParam.getPointrecords().isEmpty()) {
					telecomHubeiPointrecordsRepository.saveAll(webParam.getPointrecords());
					temp++;					
					tracer.addTag("parser.telecom.crawler.getPointrecords"+month, "月积分信息入库"+webParam.getPointrecords());
				} else {						
					tracer.addTag("parser.telecom.crawler.getPointrecords"+month, "月积分信息结果为空");
				}			
			}
			if(temp>0){
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}else{
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.telecom.crawler.getPointrecords", "月积分信息结果失败");
			e.printStackTrace();
		}

	}
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
}