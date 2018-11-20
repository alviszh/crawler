package app.client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

import feign.Request;

import java.util.concurrent.TimeUnit;

public class MiddleClientConfig {
	
	public static final int TIMEOUT_SECONDS = 100000;

	@Bean
	public Request.Options options() {
		return new Request.Options(TIMEOUT_SECONDS, TIMEOUT_SECONDS);
	}


	//重试间隔为2000ms，最大重试时间为1s,重试次数为3次
	@Bean
	public Retryer feignRetryer() {
		System.out.println("===== Retryer =============================");
		return new Retryer.Default(2000L, TimeUnit.SECONDS.toMillis(1L), 3);
	}
}
