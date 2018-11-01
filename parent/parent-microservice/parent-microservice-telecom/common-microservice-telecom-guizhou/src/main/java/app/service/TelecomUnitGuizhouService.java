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

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouCallrecord;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouHtml;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouSmsrecord;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouAccountRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouCallrecordRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouPaymonthRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouPointRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouRechargesRepository;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouSmsrecordRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomGuizhouHtmlUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.guizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.guizhou")
public class TelecomUnitGuizhouService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private CrawlerStatusMobileService  crawlerStatusMobileService;
	@Autowired
	private TelecomGuizhouAccountRepository telecomGuizhouAccountRepository;
	@Autowired
	private TelecomGuizhouHtmlRepository   telecomGuizhouHtmlRepository;
	@Autowired
	private TelecomGuizhouRechargesRepository   telecomGuizhouRechargesRepository;
	@Autowired
	private TelecomGuizhouHtmlUnit telecomGuizhouHtmlUnit;
	@Autowired
	private TelecomGuizhouPaymonthRepository  telecomGuizhouPaymonthRepository;
	@Autowired
	private TelecomGuizhouCallrecordRepository  telecomGuizhouCallrecordRepository;
	@Autowired
	private TelecomGuizhouSmsrecordRepository  telecomGuizhouSmsrecordRepository;
	@Autowired
	private TelecomGuizhouPointRepository  telecomGuizhouPointRepository;
	@Autowired
	private TracerLog tracer; 
	// 抓取账户信息
	@Async
	public void getAccountInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取账户信息", messageLogin.getTask_id());
		tracer.output("telecomUnitGuizhouService.getAccountInfo", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int count=0;
			WebParam webParam = telecomGuizhouHtmlUnit.getAccountInfo(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomGuizhouHtml telecomGuizhouHtml = new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("accountInfo");
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);							
				}			
				// 保存用户信息
				if (null != webParam.getAccount()) {
					telecomGuizhouAccountRepository.save(webParam.getAccount());		
					tracer.addTag("parser.telecom.crawler.getAccountInfo","账户信息入库"+webParam.getAccount());
					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 200, "数据采集中，【用户信息】采集成功");
				    crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	             							
				} else {
					taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 201, "数据采集中，【用户信息】采集成功");					  
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
					tracer.addTag("中国电信贵州抓取账户信息为空", messageLogin.getTask_id());
				}
			} else {
				tracer.addTag("中国电信贵州抓取失败", messageLogin.getTask_id());
				taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 500, "数据采集中，【用户信息】采集成功");				  
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {
			taskMobileRepository.updateAccountMsgStatus(messageLogin.getTask_id(), 500, "数据采集中，【用户信息】采集成功");				  
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	   
			tracer.addTag("TelecomUnitGuizhouService.getAccountInfo.Exception", e.toString());
			e.printStackTrace();
		}	
		tracer.addTag("中国电信抓取客户贵州用户基本信息", messageLogin.getTask_id());
 }
	//抓取电信充值记录
	@Async
	public void getRechargeRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception  {
		tracer.addTag("抓取贵州电信充值记录", messageLogin.getTask_id());
		tracer.output("telecomUnitGuizhouServiceget.getRechargeRecord", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());			
		try {
			int count=0;			
			WebParam webParam = telecomGuizhouHtmlUnit.getRechargeRecord(messageLogin, taskMobile,count);
			if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					TelecomGuizhouHtml   telecomGuizhouHtml=new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("rechargeRecord");
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);
				}				
				if (null != webParam.getRecharges() && !webParam.getRecharges().isEmpty()) {
					telecomGuizhouRechargesRepository.saveAll(webParam.getRecharges());
					tracer.addTag("parser.telecom.crawler.getRechargeRecord","充值缴费信息信息入库"+webParam.getRecharges());
					taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 200, "数据采集中，【缴费信息】采集成功");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 	
				}else{
					tracer.addTag("parser.telecom.crawler.getRechargeRecord","充值缴费信息信息爬取结果为空");
					taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 201, "数据采集中，【缴费信息】采集成功");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
				}
			}else{
				tracer.addTag("parser.telecom.crawler.getRechargeRecord","充值缴费信息信息爬取失败");
				taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 500, "数据采集中，【缴费信息】采集成功");				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 500, "数据采集中，【缴费信息】采集成功");
		    crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 		
			tracer.addTag("parser.telecom.crawler.getRechargeRecord.Exception",e.toString());
			e.printStackTrace();
		}
		tracer.addTag("抓取贵州电信充值记录", messageLogin.getTask_id());
	}
	
	//抓取电信充值记录
	@Async
	public void getPoint(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception  {
		tracer.addTag("抓取贵州电信积分记录", messageLogin.getTask_id());
		tracer.output("telecomUnitGuizhouServiceget.getRechargeRecord", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());				
		try {
			int count=0;			
			WebParam webParam = telecomGuizhouHtmlUnit.getPoints(messageLogin, taskMobile, count);
			if (null !=webParam) {
				if (null !=webParam.getHtml()) {
					TelecomGuizhouHtml   telecomGuizhouHtml=new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("point");
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);
				}				
				if (null != webParam.getPoint()) {
					telecomGuizhouPointRepository.save(webParam.getPoint());					
					tracer.addTag("parser.telecom.crawler.getPoint","积分信息信息入库"+webParam.getPoint());
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 		
				}else{
					tracer.addTag("parser.telecom.crawler.getPoint","充值缴费信息信息爬取结果为空");
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
				}
			}else{
				tracer.addTag("parser.telecom.crawler.getRechargeRecord","充值缴费信息信息爬取失败");
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			tracer.addTag("parser.telecom.crawler.getRechargeRecord.Exception",e.toString());			
			e.printStackTrace();
		}
		tracer.addTag("抓取贵州电信积分记录", messageLogin.getTask_id());
	}
	
	//抓取贵州电信短信详情
	@Async
	public void getSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取贵州电信短信详情", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				String month = getDateBefore("yyyyMM", i);
				WebParam webParam = telecomGuizhouHtmlUnit.getSmsRecord(messageLogin, taskMobile, month);
				if (null != webParam.getHtml()) {
					TelecomGuizhouHtml telecomGuizhouHtml = new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("smsRecord" + month);
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);
				}
				List<TelecomGuizhouSmsrecord> smsRecords = webParam.getSmsrecords();
				if (null != smsRecords && !smsRecords.isEmpty()) {
					telecomGuizhouSmsrecordRepository.saveAll(smsRecords);
					temp++;
					tracer.addTag("parser.telecom.crawler.getSmsRecord" + month, "抓取短信明细信息入库" + smsRecords.toString());
				} else {
					tracer.addTag("parser.telecom.crawler.getSmsRecord" + month, "抓取短信明细信息为空");
				}
			}
			if (temp > 0) {
				tracer.addTag("parser.telecom.crawler.getSMSDetails.status" + temp, "短信详单记录获取成功");
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 200, "数据采集中，【短信信息】采集成功");				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			} else {
				tracer.addTag("parser.telecom.crawler.getSMSDetails.status" + temp, "短信详单记录为空");
				taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, "数据采集中，【短信信息】采集成功");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {
			taskMobileRepository.updateSMSRecordStatus(messageLogin.getTask_id(), 201, "数据采集中，【短信信息】采集成功");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			tracer.addTag("parser.telecom.crawler.getSMSDetails.Exception",e.toString());
			e.printStackTrace();
		}
		tracer.addTag("抓取贵州电信短信详情", messageLogin.getTask_id());
	}
	// 抓取贵州电信语言详单
	@Async
	public void getVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile){
		tracer.addTag("抓取贵州电信语言详单", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				String month = getDateBefore("yyyyMM", i);
				WebParam webParam = telecomGuizhouHtmlUnit.getVoiceRecord(messageLogin, taskMobile, month);
				if (null != webParam.getHtml()) {
					TelecomGuizhouHtml telecomGuizhouHtml = new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("callRecord" + month);
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);
				}
				List<TelecomGuizhouCallrecord> callrecords = webParam.getCallrecords();
				if (null != callrecords && !callrecords.isEmpty()) {
					telecomGuizhouCallrecordRepository.saveAll(callrecords);
					temp++;
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month,
							"语言详单细信息入库" + callrecords.toString());
				} else {
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month, "语言详单信息爬取为空");
				}
			}
			if (temp > 0) {
				tracer.addTag("parser.telecom.crawler.getVoiceRecord.status" + temp, "通话详单记录获取成功");
				taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 200, "数据采集中，【语音信息】采集成功");				  
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			} else {
				tracer.addTag("parser.telecom.crawler.getVoiceRecord.status" + temp, "通话详单记录为空");
				taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, "数据采集中，【语音信息】采集成功");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {			
			taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 500, "数据采集中，【语音信息】采集成功");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			tracer.addTag("parser.telecom.crawler.getVoiceRecord.getVoiceRecordException",e.toString());
			e.printStackTrace();
		}	
		tracer.addTag("抓取贵州电信语言详单", messageLogin.getTask_id());
	}
	// 抓取月账单信息
	@Async
	public void getPaymonths(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception{
		tracer.addTag("抓取贵州电信月账单", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			String phoneNum = taskMobile.getPhonenum();
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				String month = getDateBefore("yyyyMM", i);
				WebParam webParam = telecomGuizhouHtmlUnit.getPaymonths(messageLogin, taskMobile, phoneNum, month);
				if (null != webParam.getHtml()) {
					TelecomGuizhouHtml telecomGuizhouHtml = new TelecomGuizhouHtml();
					telecomGuizhouHtml.setPageCount(1);
					telecomGuizhouHtml.setHtml(webParam.getHtml());
					telecomGuizhouHtml.setType("paymonth" + month);
					telecomGuizhouHtml.setUrl(webParam.getUrl());
					telecomGuizhouHtml.setTaskid(taskMobile.getTaskid());
					telecomGuizhouHtmlRepository.save(telecomGuizhouHtml);
				}
				if (null != webParam.getPaymonths() && !webParam.getPaymonths().isEmpty()) {
					telecomGuizhouPaymonthRepository.saveAll(webParam.getPaymonths());
					temp++;
					tracer.addTag("parser.telecom.crawler.getPaymonths" + month, "月账单信息入库" + webParam.getPaymonths());
				} else {
					tracer.addTag("parser.telecom.crawler.getPaymonths" + month, "月账单信息结果为空");
				}
			}
			if (temp > 0) {
				tracer.addTag("parser.telecom.crawler.getBillData.status" + temp, "账单信息获取成功");
				taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 200, "数据采集中，【账单信息】采集成功");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			} else {
				tracer.addTag("parser.telecom.crawler.getBillData.status" + temp, "账单信息获取失败");
				taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 201, "数据采集中，【账单信息】采集成功");				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			}
		} catch (Exception e) {		
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 500, "数据采集中，【账单信息】采集成功");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	 
			tracer.addTag("parser.telecom.crawler.getBillData.getPaymonthsException",e.toString());
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
