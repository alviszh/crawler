package app.config;

import javax.sql.DataSource;

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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	//@Autowired
	//RedisConnectionFactory redisConnectionFactory;
	 
	 
	//一个资源服务只有有唯一的一个 RESOURCE_ID
	private static final String RESOURCE_ID = "pbccrc";  

	@Autowired
	DataSource dataSource;
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) { 
		resources.resourceId(RESOURCE_ID).stateless(true);
		resources.tokenServices(defaultTokenServices());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception { 
		//http.authorizeRequests().antMatchers("/pbccrc/**").access("#oauth2.clientHasRole('AUTH_PBCCRC') and #oauth2.hasScope('read')");
		http.authorizeRequests().antMatchers("/pbccrc/**").authenticated();
	}
	
	/**
	 * 创建一个默认的资源服务token
	 * 
	 * @return
	 */
	@Bean
	public ResourceServerTokenServices defaultTokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(jdbcTokenStore());
		return defaultTokenServices;
	}
	
	/*@Bean
	public TokenStore tokenStore() {
		TokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
		return tokenStore;
	}*/
	
	/**
	 * jdbc token 配置
	 */
	@Bean
	public TokenStore jdbcTokenStore() { 
	    return new JdbcTokenStore(dataSource);
	}
	
	
}
