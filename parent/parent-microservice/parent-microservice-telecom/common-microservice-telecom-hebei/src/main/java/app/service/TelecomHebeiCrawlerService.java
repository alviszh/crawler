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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hebei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hebei")
public class TelecomHebeiCrawlerService implements ICrawler{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomHebeiService telecomHebeiService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		//用户信息
		try {
			telecomHebeiService.getUserInfo(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.hebei.userinfo.error", e.getMessage());
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "【用户信息】采集超时！");	
			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
				
		//套餐详情
		try {
			telecomHebeiService.getPackage(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.hebei.package.error", e.getMessage());
			taskMobileRepository.updateBusinessMsgStatus(messageLogin.getTask_id(), 404, "【业务信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
				
		//缴费信息
		try {
			telecomHebeiService.getPayfee(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.hebei.payfee.error", e.getMessage());
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());	
		}
				
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			String startYear = telecomHebeiService.getCurrentYearStartTime();
			String endYear = telecomHebeiService.getCurrentYearEndTime();
			for(int i=0;i<7;i++){			
				c1.setTime(new Date());
				c1.add(Calendar.MONTH, -i);
				Date date1 = c1.getTime();
				String currentmon = sdf.format(date1);
				//账单信息
				telecomHebeiService.getAccount(messageLogin,currentmon);
			}
					
					
			//通话信息
			telecomHebeiService.getCallRec(messageLogin);
			//短信信息
			telecomHebeiService.getMsgRec(messageLogin,startYear,endYear);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.hebei.getAccount.error", e.getMessage());
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "【账单信息】采集超时！");	
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");	
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "【短信信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
