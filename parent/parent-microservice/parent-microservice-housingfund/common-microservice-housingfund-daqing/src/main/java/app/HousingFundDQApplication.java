package app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**   
*    
* 项目名称：common-microservice-housingfund-daqing   
* 类名称：HousingFundDQApplication   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月7日 下午12:16:38   
* @version        
*/
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
public class HousingFundDQApplication { 

	public static void main(String[] args) {
		SpringApplication.run(HousingFundDQApplication.class, args);
	}

}