package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:石家庄公积金服务启动
 * @author: sln 
 * @date: 2017年10月19日 下午2:24:53 
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundSJZApplication {
	public static void main(String[] args) {
		SpringApplication.run(HousingFundSJZApplication.class, args);
	}
}
