package app.service.common.amqp.outbound;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
	
	
	String OUTPUT = "output";
	
	// 注解默认通道名字为方法名 ，当然也可以自定义channel名字，@Output("carrier_output")
	@Output(Source.OUTPUT)
	MessageChannel output();

}
