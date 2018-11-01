package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.service.common.amqp.inbound.Sink; 
 

@EnableBinding(Sink.class)
public class HousingEventListener {
	
	@Autowired
	private TracerLog tracer;

	@StreamListener(Sink.INPUT)
	public void messageSink(TaskHousing taskHousing) {
		tracer.addTag("Task Received", taskHousing.toString()); 
		System.out.println("Housing TASK Received"+taskHousing.toString());
	}

}
