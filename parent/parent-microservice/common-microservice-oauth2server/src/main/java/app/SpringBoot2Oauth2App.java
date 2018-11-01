package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author meidi Date 2018-08-13
 */
@SpringBootApplication
public class SpringBoot2Oauth2App {
	
	//password 模式
	//http://localhost:2348/oauth/token?username=user_1&password=123456&grant_type=password&scope=select&client_id=client_2&client_secret=123456
	
	//client 模式
	//http://localhost:2348/oauth/token?grant_type=client_credentials&scope=select&client_id=client_1&client_secret=123456
	 
	
	public static void main(String[] args) { 
		SpringApplication.run(SpringBoot2Oauth2App.class, args);
	}
	 

}
