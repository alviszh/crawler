package app.amqp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.crawler.monitor.json.EurekaInstanceBean;

import app.commontracerlog.TracerLog;
 
@EnableBinding(Sink.class)
public class EurekaInstanceChangeListener {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private EurekaInstanceChangeMailService eurekaInstanceChangeMailService;
	
	@StreamListener(Sink.INPUT)
	public void messageSink(EurekaInstanceBean eurekaInstanceBean) {
		String microEventType="";
		tracer.addTag("monitor-system Received", eurekaInstanceBean.toString()); 
		String eventType = eurekaInstanceBean.getEventType().trim();
		if(eventType.contains("CanceledEvent")){  //服务断线事件
			microEventType="断线事件";
			eurekaInstanceChangeMailService.sendResultMail(eurekaInstanceBean, microEventType);
		}else{  //服务注册  RegisteredEvent //服务上线事件
			microEventType="注册事件";
			eurekaInstanceChangeMailService.sendResultMail(eurekaInstanceBean, microEventType);
		}
	}
}
