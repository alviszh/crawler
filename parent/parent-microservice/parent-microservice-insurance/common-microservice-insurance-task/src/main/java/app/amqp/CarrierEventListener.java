package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.amqp.inbound.Sink;
 

@EnableBinding(Sink.class)
public class CarrierEventListener {
	
	@Autowired
	private TracerLog tracer;

	@StreamListener(Sink.INPUT)
	public void messageSink(TaskInsurance taskInsurance) {
		tracer.addTag("Insurance Task Received", taskInsurance.toString()); 
		System.out.println("Insurance Task Received"+taskInsurance.toString());
	}

}
