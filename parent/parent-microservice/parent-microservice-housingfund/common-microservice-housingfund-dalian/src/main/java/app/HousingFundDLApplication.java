package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @Description: 大连公积金
 * @author zh
 * @date 2017年7月24日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
@EnableRetry
public class HousingFundDLApplication{
	public static void main(String[] args) {
		SpringApplication.run(HousingFundDLApplication.class, args);
	}
}
