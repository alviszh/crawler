package app.client;

import org.springframework.context.annotation.Bean;

import feign.Request;

public class Common_url_ClientConfig {
	
	public static final int FIVE_SECONDS = 100000;

	@Bean
	public Request.Options options() {
		return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
	}
	

}
