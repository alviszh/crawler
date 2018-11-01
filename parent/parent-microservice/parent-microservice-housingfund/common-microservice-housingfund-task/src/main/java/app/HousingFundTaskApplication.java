package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 爬虫任务微服务接口，用于创建任务、查询任务状态(公积金)
 * @date 2017年7月24日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundTaskApplication { 

	public static void main(String[] args) {
		SpringApplication.run(HousingFundTaskApplication.class, args);
	}
}
