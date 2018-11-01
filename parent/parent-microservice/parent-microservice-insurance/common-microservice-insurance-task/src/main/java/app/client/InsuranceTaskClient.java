package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.domain.json.IdAuthBean;

@FeignClient("api-3rd")
public interface InsuranceTaskClient {
	
	@GetMapping(value="/3rd/api/idauth")
	public IdAuthBean getAllData(@RequestParam("idnum") String idnum,@RequestParam("name") String name,@RequestParam("token") String token);

}
