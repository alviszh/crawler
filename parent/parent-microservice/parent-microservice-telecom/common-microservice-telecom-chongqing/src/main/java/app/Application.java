package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 重庆电信
 * @author tz
 *
 */
@EnableEurekaClient
@SpringBootApplication
@EnableAsync
@EnableRetry
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
