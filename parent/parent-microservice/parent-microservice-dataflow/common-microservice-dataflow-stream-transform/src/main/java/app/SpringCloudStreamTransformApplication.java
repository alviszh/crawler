package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.ServiceActivator;

@SpringBootApplication
@EnableBinding(Processor.class)
public class SpringCloudStreamTransformApplication {

	private static String name = "logging";

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStreamTransformApplication.class, args);
	}

	@ServiceActivator(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object transform(Object payload) {
		System.out.println("Transformed by " + this.name + "  payload: " + payload);
		return payload;
	}
}
