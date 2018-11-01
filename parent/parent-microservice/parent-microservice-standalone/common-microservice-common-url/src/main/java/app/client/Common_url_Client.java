package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import app.bean.PageBean;



//@FeignClient(value = "common-microservice-common-url",configuration = Common_url_ClientConfig.class,fallback=Common_url_ClientFallback.class)
@FeignClient(value = "common-microservice-common-url",configuration = Common_url_ClientConfig.class)
public interface Common_url_Client {
	
	@PostMapping(path = "/common/url")
	public PageBean crawler(@RequestBody PageBean page);
	
}
