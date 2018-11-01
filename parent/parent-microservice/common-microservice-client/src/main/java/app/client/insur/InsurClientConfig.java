package app.client.insur;

import feign.Request;
import org.springframework.context.annotation.Bean;

public class InsurClientConfig {
	
	public static final int FIVE_SECONDS = 250000;

	@Bean
	public Request.Options options() {
		return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
	}
	

}
