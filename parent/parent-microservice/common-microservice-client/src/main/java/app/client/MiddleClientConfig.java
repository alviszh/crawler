package app.client;

import org.springframework.context.annotation.Bean;

import feign.Request;

public class MiddleClientConfig {
	
	public static final int TIMEOUT_SECONDS = 100000;

	@Bean
	public Request.Options options() {
		return new Request.Options(TIMEOUT_SECONDS, TIMEOUT_SECONDS);
	}

}
