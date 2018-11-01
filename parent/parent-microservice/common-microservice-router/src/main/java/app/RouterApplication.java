package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

import app.filter.SimpleFilter;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class RouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouterApplication.class, args);
	}

	//'pbccrc-v2' is mapped to route '/v2/pbccrc/**'
	/*
	@Bean
	public PatternServiceRouteMapper serviceRouteMapper() {
		return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)","${name}-${version}"); 
	}
	*/ 

	@Bean
	public SimpleFilter simpleFilter() {
		return new SimpleFilter();
	}

}
