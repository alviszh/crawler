package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @Des 人行征信
 * @author zmy
 * @date 2018年3月27日
 */

@EnableEurekaClient
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCircuitBreaker
@EnableFeignClients
@EnableAsync
public class PbccrcApplication {

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
   /*
    
    @Autowired
    private RestTemplateBuilder builder;
    
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }*/

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {
        return restTemplateBuilder
                .setConnectTimeout(100000)
                .setReadTimeout(100000)
                .build();
    }


    public static void main( String[] args ){
        SpringApplication.run(PbccrcApplication.class, args);
    }

}
