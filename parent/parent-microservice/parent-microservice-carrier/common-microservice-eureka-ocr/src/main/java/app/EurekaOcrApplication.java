package app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 超级鹰验证码服务
 * @author zzhen
 * @date 2017年6月21日 上午10:03:40
 */
@EnableEurekaClient
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class EurekaOcrApplication {
	
    public static void main( String[] args ){
    	new SpringApplicationBuilder(EurekaOcrApplication.class).web(true).run(args);
    }
}
