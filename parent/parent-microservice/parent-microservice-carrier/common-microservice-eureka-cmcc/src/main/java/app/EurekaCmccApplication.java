package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description: 移动通话记录记录爬取微服务
 * @author zzhen
 * @date 2017年6月26日 下午4:27:03
 */
@EnableEurekaClient
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableFeignClients
@EnableRetry
public class EurekaCmccApplication {
	
    public static void main( String[] args ){
    	SpringApplication.run(EurekaCmccApplication.class, args); 
    }
}
