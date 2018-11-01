package app.client;


import feign.Request;
import feign.Retryer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class StandaloneClientConfig {
    private static Logger log = LoggerFactory.getLogger(StandaloneClientConfig.class);

    public static final int FIVE_SECONDS = 60000;

    @Bean
    public Request.Options options() {
        return new Request.Options(FIVE_SECONDS, FIVE_SECONDS);
    }

    //重试间隔为5000ms，最大重试时间为1s,重试次数为3次
    @Bean
    public Retryer feignRetryer() {
        System.out.println("===== Retryer =============================");
        return new Retryer.Default(5000L, TimeUnit.SECONDS.toMillis(1L), 3);
    }

}
