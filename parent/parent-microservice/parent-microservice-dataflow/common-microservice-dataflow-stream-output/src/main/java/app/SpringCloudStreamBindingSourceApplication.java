package app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;


@SpringBootApplication
@EnableBinding(Source.class)
public class SpringCloudStreamBindingSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStreamBindingSourceApplication.class, args);
	}

	
	
	@InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
	public String timerMessageSource() {
		String format = "SourcePubish<>"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		System.out.println("publish message :" + format);
		return format;
	}
}
