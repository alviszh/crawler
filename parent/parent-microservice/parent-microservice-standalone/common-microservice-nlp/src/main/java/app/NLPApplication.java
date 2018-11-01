package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
//@EnableScheduling
// @EnableScheduling 注解的作用是发现注解@Scheduled的任务并后台执行。
public class NLPApplication { 

	public static void main(String[] args) {
		SpringApplication.run(NLPApplication.class, args);
	}

}