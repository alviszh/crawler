package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.telecom.shanghai","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.telecom.shanghai","com.microservice.dao.repository.crawler.mobile"})
public class TelecomShanghaiCrawlerService implements ICrawler{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomShanghaiService telecomShanghaiService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		//用户信息
		try {
			telecomShanghaiService.getUserInfo(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.shanghai.userinfo.error", e.getMessage());
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "【用户信息】采集超时！");	
			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 404, "无亲情套餐！");
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
						
		//缴费信息
		try {
			telecomShanghaiService.getPayfee(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.shanghai.payfee.error", e.getMessage());
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
						
		//账户信息
		try {
			telecomShanghaiService.getAccount(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.shanghai.account.error", e.getMessage());
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "【账户信息】采集超时！");		
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "【套餐信息】采集超时！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
					
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			Calendar c = Calendar.getInstance();
			for(int i=0;i<7;i++){			
				c.setTime(new Date());
				c.add(Calendar.MONTH, -i);
				Date date = c.getTime();
				String mon = sdf.format(date);	
				//通话信息
				telecomShanghaiService.getCallRec(messageLogin,mon);
				//短信信息
				telecomShanghaiService.getMsgRec(messageLogin,mon);
			}
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.shanghai.callAndMsg.error", e.getMessage());
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "【短信信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		return taskMobile;
	}
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		return null;
		
	}


}
