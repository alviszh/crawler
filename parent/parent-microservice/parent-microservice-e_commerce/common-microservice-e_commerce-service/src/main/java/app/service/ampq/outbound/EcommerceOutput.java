package app.service.ampq.outbound;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EcommerceOutput {
	
	
	String OUTPUT = "e_commerce_output";
	
	// 注解默认通道名字为方法名 ，当然也可以自定义channel名字，@Output("carrier_output")
	@Output(EcommerceOutput.OUTPUT)
	MessageChannel e_commerce_output();

}
