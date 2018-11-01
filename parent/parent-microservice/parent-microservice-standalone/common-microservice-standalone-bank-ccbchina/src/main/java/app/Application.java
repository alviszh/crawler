package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Des 中国建设银行爬取
 * @author tz
 * @date 2017年8月25日
 */

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class Application {
	
	@Autowired 
	private RestTemplateBuilder builder;
	
	// 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例 
	@Bean 
	public RestTemplate restTemplate() { 
		return builder.build(); 
	} 
	
    public static void main( String[] args ){
    	SpringApplication.run(Application.class, args);
    }
    
}
