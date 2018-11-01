package app.gongan.controller;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.crawler.domain.json.IdAuthBean;
import com.crawler.domain.json.IdAuthRequest;

import app.gongan.bean.reponse.TokenStatus;
import app.commontracerlog.TracerLog;
import app.gongan.service.GongAnClient;

@RestController
@RequestMapping("/3rd/api") 
public class GongAnController {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private GongAnClient gongAnClient;
	
	@GetMapping(value="/idauth")
	public IdAuthBean getAllData(String idnum,String name,String token){
		tracer.addTag("idnum",idnum);
		System.out.println("idnum:"+idnum);
		System.out.println("name:"+name);
		System.out.println("token:"+token); 
		IdAuthRequest idAuthRequest = new IdAuthRequest(); 
		idAuthRequest.setToken(token);
		idAuthRequest.setCustomerCretNum(idnum);
		idAuthRequest.setCustomerName(name); 
		return gongAnClient.getIdAuthBean(idAuthRequest);  
	}
	
	@GetMapping(value="/token/status")
	public TokenStatus getTokenStatus(String token,String appKey){
		System.out.println("token:"+token);
		System.out.println("appKey:"+appKey);
		return gongAnClient.getTokenStatus(token, appKey);  
	}
	
	

}
