package app.service.common.amqp.inbound;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {
  
	String INPUT = "input";
	
	// 注解默认通道名字为方法名 ，当然也可以自定义channel名字，@Output("carrier_channel")
	@Input(Sink.INPUT)
	SubscribableChannel input();

}
