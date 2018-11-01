package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 滁州公积金
 * @author zh
 * @date 2017年7月24日
 */
@EnableEurekaClient
@SpringBootApplication
//@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundCZApplication {
	public static void main(String[] args) {
		SpringApplication.run(HousingFundCZApplication.class, args);
	}
}
