package app.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * 由于采用资源服务和认证服务分离的方案， common-microservice-router 只负责认证服务，以下rest 接口为测试使用
 */

@RestController
public class TestEndpoints {

	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable String id) {
		// for debug
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "product id : " + id;
	}

	@GetMapping("/mobile/{id}")
	public String getOrder(@PathVariable String id) {
		// for debug
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		/*OAuth2AuthenticationDetails OAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)authentication.getDetails();
		String tokenValue = OAuth2AuthenticationDetails.getTokenValue();
		System.out.println("tokenValue------------"+tokenValue);
		boolean isAuthenticated = authentication.isAuthenticated();
		System.out.println("isAuthenticated------------"+isAuthenticated);*/
		return "mobile-h5 id : " + id;
	}
	
	
	@GetMapping("/pbccrc/{id}")
	public String getPbccrc(@PathVariable String id) {
		// for debug
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "pbccrc-h5 id : " + id;
	}

}
