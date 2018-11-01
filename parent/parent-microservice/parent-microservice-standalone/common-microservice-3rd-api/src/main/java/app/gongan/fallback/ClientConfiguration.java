package app.gongan.fallback;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;
import feign.Logger;

@Configuration
public class ClientConfiguration {

    /**
     * Spring has created their own Feign Contract to allow you to use Spring's @RequestMapping annotations instead of Feigns. 
     * java.lang.IllegalStateException: Method getLinksForTrack not annotated with HTTP method type (ex. GET, POST)
     * */  
    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
       return Logger.Level.FULL;
    }
    
}
