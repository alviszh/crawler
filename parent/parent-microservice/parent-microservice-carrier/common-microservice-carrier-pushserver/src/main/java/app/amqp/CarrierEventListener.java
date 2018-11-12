package app.amqp;

import app.commontracerlog.TracerLog;
import app.service.MobileSendService;
import app.service.amqp.inbound.Sink;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


@EnableBinding(Sink.class)
public class CarrierEventListener {
	
	@Autowired
	private TracerLog tracer;
	
    @Autowired
    private MobileSendService mobileSendService;

	@StreamListener(Sink.INPUT)
	public void messageSink(TaskMobile taskMobile) {
		if (taskMobile != null ) {
			tracer.addTag("Pushserver Received", taskMobile.toString());

			//推送状态
			mobileSendService.sendMessageResult(taskMobile);
		}
		
	}

}
