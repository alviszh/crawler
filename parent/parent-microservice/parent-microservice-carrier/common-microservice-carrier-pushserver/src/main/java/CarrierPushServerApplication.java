import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: 爬虫任务微服务接口，用于推送服务
 * @author zmy
 * @date 2018年11月2日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class CarrierPushServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrierPushServerApplication.class, args);
	}


 
}
