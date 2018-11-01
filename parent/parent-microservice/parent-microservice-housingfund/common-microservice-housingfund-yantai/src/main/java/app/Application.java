package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 烟台社保
 * @author qzb
 * @date 2017年10月12日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class Application { 

	public static void main(String[] args) {
//		System.setProperty("java.awt.headless", "false");  
		SpringApplication.run(Application.class, args);
	}

}
