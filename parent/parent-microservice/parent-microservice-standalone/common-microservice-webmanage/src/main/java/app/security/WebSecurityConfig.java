package app.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import app.config.CustomUserDetailsService;

/**
 * Spring Security 过滤配置
 * @author rongshengxu
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	String contextPath = "webmanage";
        //允许所有用户访问"/"和"/home"
        http.authorizeRequests()
        		//.antMatchers(new String[]{"**/js/**","**/css/**","**/images/**","**/bookstrap/**","**/font-awesome/**","**/carrier/apitest/cmcc/result/**","**/carrier/apitest/unicom/result/**","**/carrier/apitest/telecom/result/**","**/favicon.ico"}).permitAll()
        		.antMatchers("/**/*.js").permitAll()
        		.antMatchers("/**/*.css").permitAll()
        		.antMatchers("/**/*.png").permitAll()
        		.antMatchers("/**/*.jpg").permitAll()
        		.antMatchers("/favicon.ico").permitAll()
        		//其他地址的访问均需验证权限
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //指定登录页是"/login"
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/")//登录成功后默认跳转到"/"
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")//退出登录后的默认url是"/login"
                .permitAll()
                .and()
                .csrf().disable();//关闭csrf功能

    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth
            .userDetailsService(customUserDetailsService())
            .passwordEncoder(passwordEncoder());

    }

    /**
     * 设置用户密码的加密方式为MD5加密
     * @return
     */
    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();

    }
    
    public static void main(String[] args) {
    	Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    	System.out.println("password:"+md5.encodePassword("123456", null));
	}

    /**
     * 自定义UserDetailsService，从数据库中读取用户信息
     * @return
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService(){
        return new CustomUserDetailsService();
    }

}