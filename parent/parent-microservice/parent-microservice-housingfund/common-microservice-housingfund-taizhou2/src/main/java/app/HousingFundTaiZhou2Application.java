package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@SpringBootApplication   
@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundTaiZhou2Application {

	public static void main(String[] args) {
		SpringApplication.run(HousingFundTaiZhou2Application.class, args);
	}
}
