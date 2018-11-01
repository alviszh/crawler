package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author DougeChow
 * @Description: 爬虫任务微服务接口，用于创建任务、查询任务状态
 * @date 2017年11月30日
 */
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
public class E_CommerceTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(E_CommerceTaskApplication.class, args);
    }
}
