package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Description: 沈阳社保
 * @author Mu
 * @date 2017年9月18日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
public class Application { 

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
