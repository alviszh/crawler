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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.zhejiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.zhejiang")
public class TelecomZhejiangCrawlerService implements ICrawler{
	
	@Autowired
	private TelecomZhejiangService telecomZhejiangService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		//用户信息
		try {
			telecomZhejiangService.getUserInfo(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.zhejiang.userinfo.error", e.getMessage());
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "无【用户信息】！");	
			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "无套餐信息！");
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404,"无【账户信息】！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			e.printStackTrace();
		}
				
		//缴费信息
		try{
			telecomZhejiangService.getPayRec(messageLogin);
		} catch (Exception e) {
			tracer.addTag("crawler.telecom.zhejiang.getPayRec.error", e.getMessage());
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
				
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			for(int i=0;i<7;i++){			
				Thread.sleep(10000);
				c.setTime(new Date());
				c.add(Calendar.MONTH, -i);
				Date date = c.getTime();
				String mon = sdf.format(date);
				//获取通话记录
				telecomZhejiangService.getCallRec(messageLogin,mon);
				//获取短信记录
				telecomZhejiangService.getMsgRec(messageLogin,mon);
			}
		} catch(Exception e){
			tracer.addTag("crawler.telecom.zhejiang.getCallRec.error", e.getMessage());
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "【短信信息】采集超时！");			
			taskMobile = crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
