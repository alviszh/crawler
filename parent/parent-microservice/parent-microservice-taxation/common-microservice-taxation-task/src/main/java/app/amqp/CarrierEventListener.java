package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;

import app.commontracerlog.TracerLog;
import app.service.amqp.inbound.Sink;
 

@EnableBinding(Sink.class)
public class CarrierEventListener {
	
	@Autowired
	private TracerLog tracer;

	@StreamListener(Sink.INPUT)
	public void messageSink(TaskTaxation taskTaxation) {
		tracer.addTag("Taxation Task Received", taskTaxation.toString()); 
		System.out.println("Taxation Task Received"+taskTaxation.toString());
	}

}
