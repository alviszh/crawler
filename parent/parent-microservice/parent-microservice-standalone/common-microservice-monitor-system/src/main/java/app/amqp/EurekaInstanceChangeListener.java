package app.amqp;


import java.text.SimpleDateFormat;
import java.util.Date;

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
		//将时间戳转成指定格式的时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//加八个小时
	    Date date = new Date(eurekaInstanceBean.getTimestamp()+8 * 60 * 60 * 1000);
	    String eventTime = simpleDateFormat.format(date);
	    
		if(eventType.contains("CanceledEvent")){  //服务断线事件
			microEventType="断线事件";
			eurekaInstanceChangeMailService.sendResultMail(eurekaInstanceBean,microEventType,eventTime);
		}else{  //服务注册  RegisteredEvent //服务上线事件
			microEventType="注册事件";
			eurekaInstanceChangeMailService.sendResultMail(eurekaInstanceBean, microEventType,eventTime);
		}
	}
}
