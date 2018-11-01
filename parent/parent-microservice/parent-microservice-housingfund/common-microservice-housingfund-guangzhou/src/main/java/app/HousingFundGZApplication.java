package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 广州公积金
 * @author: sln 
 * @date: 2017年9月28日 下午3:36:31 
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundGZApplication {
	@Autowired 
	private RestTemplateBuilder builder; 

	// 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例 
	@Bean 
	public RestTemplate restTemplate() { 
	return builder.build(); 
	}
	public static void main(String[] args) {
		SpringApplication.run(HousingFundGZApplication.class, args);
	}
}
