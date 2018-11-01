package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableEurekaClient
@SpringBootApplication
@EnableAsync
public class EurekaChinaTelecomBJApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaChinaTelecomBJApplication.class, args);
	}
}
