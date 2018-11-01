package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.MobileSendService;
import app.service.amqp.inbound.Sink; 
 

@EnableBinding(Sink.class)
public class CarrierEventListener {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
    @Autowired
    private MobileSendService mobileSendService;

	@StreamListener(Sink.INPUT)
	public void messageSink(TaskMobile taskMobile) {
		tracer.addTag("Task Received", taskMobile.toString()); 
		
		if (taskMobile.getFinished() !=null && taskMobile.getFinished() && taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhase())
                && taskMobile.getPhase_status().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhasestatus())) {
			tracer.qryKeyValue("Mobile数据采集完成 推送前置规则",taskMobile.getTaskid());
			TaskMobile taskMobilen = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
            tracer.qryKeyValue("sendPrecedingRule start", "开始前置规则的推送");
            mobileSendService.sendPrecedingRule(taskMobilen);
            tracer.qryKeyValue("sendPrecedingRule end", "结束前置规则的推送");
        }
		
		
	}

}
