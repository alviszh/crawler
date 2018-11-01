package app.gongan.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import com.crawler.domain.json.IdAuthBean;
import com.crawler.domain.json.IdAuthRequest;
import app.gongan.bean.reponse.TokenStatus;
import app.gongan.fallback.GongAnClientFallback;
import feign.RequestLine;


//@FeignClient(name = "gongan",url = "http://10.150.33.112:9003",fallback = GongAnClientFallback.class,configuration=ClientConfiguration.class)
//@FeignClient(name = "gongan",fallback = GongAnClientFallback.class,configuration=ClientConfiguration.class)
@FeignClient(name="gongan",url="${api.gongan.url}",fallback=GongAnClientFallback.class)
public interface GongAnClient extends BaseApi<TokenStatus> {

	//User findByUsername(@RequestParam("userName") final String userName, @RequestParam("address") final String address); 
	//@RequestLine("POST /security/gongAn/scySimple?token=2ba16f69-4b9a-4623-9c4e-2ca453989d39")
	//@Headers("Accept: " + MediaType.APPLICATION_JSON_VALUE)
	//@RequestMapping(method = RequestMethod.POST, value = "/security/gongAn/scySimple?token=2ba16f69-4b9a-4623-9c4e-2ca453989d39") 
	// @PostMapping(path = "/security/gongAn/scySimple")
	//@RequestMapping(method = RequestMethod.POST, value = "/security/gongAn/scySimple")
	//IdAuthBean getIdAuthBean(@RequestBody IdAuthRequest idAuthRequest); 
	//@RequestMapping(method = RequestMethod.GET, value = "/{trackid}/links")
	//@RequestMapping(value = "/uaa/oauth/check_token?token={token}&appKey={appKey}",method = RequestMethod.GET) 
	//TokenStatus getToken(@RequestParam("token") String token,@RequestParam("appKey") String appKey);
	
	@RequestLine("POST /security/gongAn/scySimple?token=2ba16f69-4b9a-4623-9c4e-2ca453989d39")
	IdAuthBean getIdAuthBean(@RequestBody IdAuthRequest idAuthRequest); 
	


	
}
