package app.service.ampq.inbound;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface EcommerceInput {
  
	String INPUT = "e_commerce_input";
	
	// 注解默认通道名字为方法名 ，当然也可以自定义channel名字，@Output("carrier_channel")
	@Input(EcommerceInput.INPUT)
	SubscribableChannel e_commerce_input();

}
