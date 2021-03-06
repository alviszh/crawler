package app.endpoint;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
		@SuppressWarnings("unused")
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		/*OAuth2AuthenticationDetails OAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)authentication.getDetails();
		String tokenValue = OAuth2AuthenticationDetails.getTokenValue();
		System.out.println("tokenValue------------"+tokenValue);
		boolean isAuthenticated = authentication.isAuthenticated();
		System.out.println("isAuthenticated------------"+isAuthenticated);*/
		return "mobile id : " + id;
	}
	
	
	@GetMapping("/pbccrc/{id}")
	public String getPbccrc(@PathVariable String id) {
		// for debug
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return "pbccrc id : " + id;
	}

}
