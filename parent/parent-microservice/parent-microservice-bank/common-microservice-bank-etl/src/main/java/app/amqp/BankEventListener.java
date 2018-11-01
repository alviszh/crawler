package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.amqp.inbound.Sink; 
 

@EnableBinding(Sink.class)
public class BankEventListener {
	
	@Autowired
	private TracerLog tracer;
	
	@StreamListener(Sink.INPUT)
	public void messageSink(TaskBank taskBank) {
		System.out.println("银行-ETL-----接收到消息队列的信息："+taskBank.toString());
		tracer.addTag("ETL Received", taskBank.toString()); 
	}

}
