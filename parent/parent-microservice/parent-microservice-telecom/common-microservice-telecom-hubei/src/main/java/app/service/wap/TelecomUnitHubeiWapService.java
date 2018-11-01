package app.service.wap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapHtml;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapCallrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapPaymonthsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapPointrecordRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapRechargesRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.wap.TelecomHubeiWapUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WapParam;
import app.htmlunit.wap.TelecomHubeiWapHtmlUnit;
import app.service.CrawlerStatusMobileService;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomUnitHubeiWapService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiWapUserinfoRepository telecomHubeiWapUserinfoRepository;
	@Autowired
	private TelecomHubeiWapRechargesRepository telecomHubeiWapRechargesRepository;
	@Autowired
	private TelecomHubeiWapPointrecordRepository telecomHubeiWapPointrecordRepository;
	@Autowired
	private TelecomHubeiWapPaymonthsRepository telecomHubeiWapPaymonthsRepository;
	@Autowired
	private TelecomHubeiWapHtmlRepository telecomHubeiWapHtmlRepository;
	@Autowired
	private TelecomHubeiWapCallrecordsRepository telecomHubeiWapCallrecordsRepository;
	@Autowired
	private TelecomHubeiWapHtmlUnit telecomHubeiWapHtmlUnit;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer; 
	// 抓取用户信息
	@Async
	public void getUserInfo(MessageLogin messageLogin){
		tracer.addTag("TelecomUnitHubeiWapService.getUserInfo", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {		
			int count = 0;
			WapParam wapParam = telecomHubeiWapHtmlUnit.getUserInfo(messageLogin, taskMobile, count);
			if (null != wapParam) {
				if (null != wapParam.getHtml()) {
					TelecomHubeiWapHtml telecomHubeiWapHtml = new TelecomHubeiWapHtml();
					telecomHubeiWapHtml.setPageCount(1);
					telecomHubeiWapHtml.setHtml(wapParam.getHtml());
					telecomHubeiWapHtml.setType("userInfo");
					telecomHubeiWapHtml.setUrl(wapParam.getUrl());
					telecomHubeiWapHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiWapHtmlRepository.save(telecomHubeiWapHtml);		
				}
				// 保存用户信息
				if (null != wapParam.getUserinfo()) {
					telecomHubeiWapUserinfoRepository.save(wapParam.getUserinfo());
					tracer.addTag("parser.telecom.crawler.getUserInfo", "用户信息入库" +wapParam.getUserinfo());
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【用户信息】采集成功");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());					
				}else{
					tracer.addTag("中国电信湖北抓取用户信息为空", messageLogin.getTask_id());
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【用户信息】采集完成");											
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());					  
				}
			} else {
				tracer.addTag("中国电信湖北用户信息抓取失败", messageLogin.getTask_id());	
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("中国电信湖北抓取用户信息出错", messageLogin.getTask_id());
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【用户信息】采集完成");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}
		tracer.addTag("中国电信抓取客户湖北用户基本信息", messageLogin.getTask_id());	
	}
	
	//抓取电信充值记录
	@Async
	public void getRechargeRecord(MessageLogin messageLogin){
		tracer.addTag("TelecomUnitHubeiWapService.getRechargeRecord", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		try {
			int temp = 0;
			for (int i = 0; i < 6; i++) {
				int count = 0;
				String month=getDateBefore("yyyyMM",  i);
				WapParam wapParam = telecomHubeiWapHtmlUnit.getRechargeRecord(messageLogin, taskMobile, month, count);
				if (null != wapParam.getHtml()) {
					TelecomHubeiWapHtml telecomHubeiWapHtml = new TelecomHubeiWapHtml();
					telecomHubeiWapHtml.setPageCount(1);
					telecomHubeiWapHtml.setHtml(wapParam.getHtml());
					telecomHubeiWapHtml.setType("rechargeRecord"+month);
					telecomHubeiWapHtml.setUrl(wapParam.getUrl());
					telecomHubeiWapHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiWapHtmlRepository.save(telecomHubeiWapHtml);					
				}
				if (null != wapParam.getRecharges() && !wapParam.getRecharges().isEmpty()) {
					telecomHubeiWapRechargesRepository.saveAll(wapParam.getRecharges());
					temp++;
					tracer.addTag("parser.telecom.crawler.getRechargeRecord",
							month + "月充值缴费信息信息入库" + wapParam.getRecharges());
				} else {
					tracer.addTag("parser.telecom.crawler.getRechargeRecord", month + "月充值缴费信息信息爬取结果为空");
				}				
			}
			if (temp > 0) {
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【充值信息】已采集完成");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			} else {
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【充值信息】采集完成");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【充值信息】采集完成");	
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
    		tracer.addTag("getRechargeRecord.Exception", e.toString());
			e.printStackTrace();
		}
		tracer.addTag("抓取湖北电信充值记录", messageLogin.getTask_id());
	}	
	// 抓取湖北电信语言详单
	@Async
	public void getVoiceRecord(MessageLogin messageLogin){
		tracer.addTag("TelecomUnitHubeiWapService.getRechargeRecord", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());	
		try {
			int temp = 0;		
			for (int i = 0; i < 6; i++) {
				int count = 0;
				String month = getDateBefore("yyyyMM", i);
				WapParam wapParam = telecomHubeiWapHtmlUnit.getVoiceRecord(messageLogin, taskMobile, month,count);
				if (null != wapParam.getHtml()) {
					TelecomHubeiWapHtml telecomHubeiWapHtml = new TelecomHubeiWapHtml();
					telecomHubeiWapHtml.setPageCount(1);
					telecomHubeiWapHtml.setHtml(wapParam.getHtml());
					telecomHubeiWapHtml.setType("callRecord" + month);
					telecomHubeiWapHtml.setUrl(wapParam.getUrl());
					telecomHubeiWapHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiWapHtmlRepository.save(telecomHubeiWapHtml);
				}
				if (null != wapParam.getCallrecords() && !wapParam.getCallrecords().isEmpty()) {
					telecomHubeiWapCallrecordsRepository.saveAll(wapParam.getCallrecords());
					temp++;
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month,
							"语言详单细信息入库" + wapParam.getCallrecords());
				} else {
					tracer.addTag("parser.telecom.crawler.getVoiceRecord" + month, "语言详单信息爬取为空");
				}
			}		
    		if (temp > 0) {
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，【通讯信息】采集完成");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			} else {
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 201, "数据采集中，【通讯信息】采集完成");
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());	
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 500, "数据采集中，【通讯信息】采集完成");		
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.telecom.crawler.getVoiceRecord.Exception",e.toString());
			e.printStackTrace();
		}	
		tracer.addTag("抓取湖北电信语言详单", messageLogin.getTask_id());
	}	
	// 抓取月账单信息
	@Async
	public void getPaymonths(MessageLogin messageLogin){
		tracer.addTag("getPaymonths", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid( messageLogin.getTask_id());
		try {		
			int temp=0;
			for (int i = 1; i <=6; i++) {
				String month= getDateBefore("yyyyMM",  i);
				int count=0;
			    WapParam wapParam = telecomHubeiWapHtmlUnit.getPaymonths(messageLogin, taskMobile,month,count);
				if (null != wapParam.getHtml()) {
					TelecomHubeiWapHtml telecomHubeiWapHtml = new TelecomHubeiWapHtml();
					telecomHubeiWapHtml.setPageCount(1);
					telecomHubeiWapHtml.setHtml(wapParam.getHtml());
					telecomHubeiWapHtml.setType("paymonth"+month);
					telecomHubeiWapHtml.setUrl(wapParam.getUrl());
					telecomHubeiWapHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiWapHtmlRepository.save(telecomHubeiWapHtml);           	
				}
				if (null != wapParam.getPaymonths() && !wapParam.getPaymonths().isEmpty()) {
					telecomHubeiWapPaymonthsRepository.saveAll(wapParam.getPaymonths());
					temp++;				
					tracer.addTag("parser.telecom.crawler.getPaymonths"+month, "月账单信息入库"+wapParam.getPaymonths());					
				} else {				
					tracer.addTag("parser.telecom.crawler.getPaymonths"+month, "月账单信息结果为空");
				}
				if (temp>0) {
					taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());	
					taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【缴费信息】已采集完成");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}else{
					taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【缴费信息】已采集完成");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}
			}
		} catch (Exception e) {		
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【缴费信息】采集完成");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("parser.telecom.crawler.getPaymonths.Exception", e.toString());
			e.printStackTrace();
		}
	}
	// 抓取积分信息
	@Async
	public void getPointrecords(MessageLogin messageLogin) {
		tracer.addTag("getPointrecords", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			int count=0;		
			 WapParam wapParam = telecomHubeiWapHtmlUnit.getPointRecord(messageLogin, taskMobile, count);
				if (null != wapParam.getHtml()) {
					TelecomHubeiWapHtml telecomHubeiWapHtml = new TelecomHubeiWapHtml();
					telecomHubeiWapHtml.setPageCount(1);
					telecomHubeiWapHtml.setHtml(wapParam.getHtml());
					telecomHubeiWapHtml.setType("point");
					telecomHubeiWapHtml.setUrl(wapParam.getUrl());
					telecomHubeiWapHtml.setTaskid(taskMobile.getTaskid());
					telecomHubeiWapHtmlRepository.save(telecomHubeiWapHtml);		
				}
				if (null != wapParam.getPointrecord()) {
					telecomHubeiWapPointrecordRepository.save(wapParam.getPointrecord());
					tracer.addTag("parser.telecom.crawler.getPointrecords",wapParam.getPointrecord().toString());
					taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，【积分信息】已采集完成");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				} else {						
					tracer.addTag("parser.telecom.crawler.getPointrecords", "积分信息结果为空");
					taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【积分信息】已采集完成");
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}			
		} catch (Exception e) {
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 500, "数据采集中，【积分信息】采集完成");
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