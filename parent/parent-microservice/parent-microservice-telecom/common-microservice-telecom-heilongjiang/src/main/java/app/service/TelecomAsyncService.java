package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.heilongjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.heilongjiang")
public class TelecomAsyncService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(TelecomAsyncService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomUnitService telecomUnitService;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());

		
		getCallThemHtml(messageLogin, taskMobile);
		getPayMsgStatusThem(messageLogin, taskMobile);
		getCustomThem(messageLogin, taskMobile);
		
		return taskMobile;
	}
	
	
	public String getCustomThem(MessageLogin messageLogin, TaskMobile taskMobile) {
		log.info("====================爬取结果集===========================");
		for (int i = 1; i <= 6; i++) {
			try {
				telecomUnitService.getCustomThem(messageLogin, taskMobile, i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**   
	  *    
	  * 项目名称：common-microservice-telecom-heilongjiang  
	  * 所属包名：app.service
	  * 类描述：   
	  * 短信 seledType=5
	  * 上网数据 seledType=4
	  * 语音通话详单 seledType=9
	  * 创建人：hyx 
	  * 创建时间：2018年10月25日 
	  * @version 1  
	  * 返回值    String
	  */
	@Async
	public String getCallThemHtml(MessageLogin messageLogin, TaskMobile taskMobile) { 
		int seledType = 0;

		List<Future<Integer>> listfuture = new ArrayList<>();

		
		//注意 seledType 与selectype区别
		for (int selectype = 1; selectype <= 3; selectype++) {
			if(selectype == 1){
				seledType = 5;
			}
			
			if(selectype == 2){
				seledType = 4;
			}
			
			if(selectype == 3){
				seledType = 9;
			}
		
//		int selectype = 1,seledType=9;
				
			for (int i = 1; i <= 6; i++) {
				Integer totalnum = 1;
				
				
				totalnum = telecomUnitService.getCallThemHtml(messageLogin, taskMobile, i, seledType,selectype);
				
				double pagenum = Math.ceil(totalnum/30);
				
				tracerLog.addTag("typenum",selectype+"");

				tracerLog.addTag("pagenum",pagenum+"");
				for(int k = 1 ;k<pagenum;k++){
					try {
					
						Future<Integer>  future =  telecomUnitService.getCallThemHtml(messageLogin, taskMobile, i, seledType,selectype,k);

						listfuture.add(future);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			if(listfuture.size()<=0){
				istrue = false;
			}
			for (Future<Integer> future : listfuture) {
				if (future.isDone()) { // 判断是否执行完毕
					listfuture.remove(future);
					tracerLog.addTag("Future", future.isDone()+"");
					break;
				}

			}
		}

		
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		taskMobile.setError_message(StatusCodeRec.MESSAGE_SUCESS_TWO.getMessage()); // 爬取成功状态更新
		taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;
	}

	@Async
	public String getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		telecomUnitService.getPayMsgStatusThem(messageLogin, taskMobile);

		return null;
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.addTag("正在进行上次未完成的爬取任务。。。", taskMobile.toString());
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	
}