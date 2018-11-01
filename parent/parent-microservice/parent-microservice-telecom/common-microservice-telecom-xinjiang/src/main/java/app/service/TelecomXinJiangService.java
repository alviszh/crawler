package app.service;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.xinjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.xinjiang")
public class TelecomXinJiangService {
	@Autowired
	private TelecomUnitXinJiangService telecomUnitXinJiangService;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer; 
	@Async
	public void getUserInfo(MessageLogin messageLogin) {
	try {
			telecomUnitXinJiangService.getUserInfo(messageLogin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAccountInfo(MessageLogin messageLogin) {
	try {
			telecomUnitXinJiangService.getAccountInfo(messageLogin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Async
	public void getPoint(MessageLogin messageLogin) {
			try {
				telecomUnitXinJiangService.getPoint(messageLogin);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@Async
	public void getRechargeRecord(MessageLogin messageLogin)  {
		try {
		 telecomUnitXinJiangService.getRechargeRecord(messageLogin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Async
	public void getPaymonths(MessageLogin messageLogin)  {
		try {
		   telecomUnitXinJiangService.getPaymonths(messageLogin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Async
	public void getVoiceRecordData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());	
		try {			
			Future<String>  data1=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 0);	
			Thread.sleep(3000);
			Future<String>  data2=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 1);
			Thread.sleep(3000);
			Future<String>  data3=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 2);
			Thread.sleep(3000);
			Future<String>  data4=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 3);
			Thread.sleep(3000);
			Future<String>  data5=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 4);
			Thread.sleep(3000);
			Future<String>  data6=telecomUnitXinJiangService.getVoiceRecordByMonth(messageLogin,taskMobile, 5);	
			Thread.sleep(3000);
			while(true){
				if (data1.isDone() && data2.isDone() && data3.isDone() && data4.isDone() && data5.isDone()
						&& data6.isDone()) {
					if (data1.get().contains("数据采集成功") || data2.get().contains("数据采集成功")
							|| data3.get().contains("数据采集成功") || data4.get().contains("数据采集成功")
							|| data5.get().contains("数据采集成功") || data6.get().contains("数据采集成功")) {						
						tracer.addTag("getVoiceRecordData数据采集成功","getVoiceRecordData数据采集成功");	
						taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 200, "数据采集中，【语音信息】采集成功");
						crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					} else {
						tracer.addTag("getVoiceRecordData数据采集完成,通话记录为空","getVoiceRecordData数据采集完成,通话记录为空");	
						taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 201, "数据采集中，【语音信息】采集成功");
						crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
					}
					break;
				}				
				Thread.sleep(2500);
			}
		} catch (Exception e) {
			tracer.addTag("getVoiceRecord.Exception", e.getMessage());
			taskMobileRepository.updateCallRecordStatus(messageLogin.getTask_id(), 500, "数据采集中，【语音信息】采集成功");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			e.printStackTrace();
		}		
	}
	@Async
	public void getSmsRecord(MessageLogin messageLogin) {
		try {
			telecomUnitXinJiangService.getSmsRecord(messageLogin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Async
	public void getRealtimefee(MessageLogin messageLogin)  {
		try {
			telecomUnitXinJiangService.getRealtimefee(messageLogin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Async
	public void getAddvalueItems(MessageLogin messageLogin)  {
		try {
			telecomUnitXinJiangService.getAddvalueItems(messageLogin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
