package app.config;
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Autowired
	RedisConnectionFactory redisConnectionFactory;
	 
	//一个资源服务只有有唯一的一个 RESOURCE_ID
	private static final String RESOURCE_ID = "carrier";  

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) { 
		resources.resourceId(RESOURCE_ID).stateless(true);
		resources.tokenServices(defaultTokenServices());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception { 
		//此配置加上后，"/h5/carrier/**" 路径的请求都需要进行oauth2.0认证，认证服务位于common-microservice-router 项目
		//http.authorizeRequests().antMatchers("/h5/carrier/**").authenticated();
	}
	
	*//**
	 * 创建一个默认的资源服务token
	 * 
	 * @return
	 *//*
	@Bean
	public ResourceServerTokenServices defaultTokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}
	
	@Bean
	public TokenStore tokenStore() {
		TokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		return tokenStore;
	}
	
	
}
*/