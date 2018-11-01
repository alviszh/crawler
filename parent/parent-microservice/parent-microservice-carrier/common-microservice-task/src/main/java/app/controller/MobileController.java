package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MobileJsonBean;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.BasicUser;

import app.commontracerlog.TracerLog;
import app.service.MobileService;

@RestController 
@RequestMapping("/carrier")  
public class MobileController {
	
	public static final Logger log = LoggerFactory.getLogger(MobileController.class);
	
	@Autowired
	private MobileService mobileService;
	
	@Autowired
	private TracerLog tracer;
	
	/*
	@RequestMapping(value = "/mobile/{num}", method = RequestMethod.GET)  
	public ResultData<MobileJsonBean> index(@PathVariable("num") String num){
		log.info("-----------MobileController:  num="+num+"---------------"); 
		ResultData<MobileJsonBean> data = mobileService.judgeOperatorByMobile(num);
		return data;	
	}*/ 
	
	@PostMapping("/check")
	public ResultData<MobileJsonBean> post(@RequestBody MobileJsonBean mobileJsonBean) { 
		//判断用户是否在基本表中已经存在
		tracer.addTag("check接口", "-----MobileJsonBean----"+mobileJsonBean.toString());
		mobileJsonBean = mobileService.checkUser(mobileJsonBean); 
		ResultData<MobileJsonBean> bean = mobileService.judgeOperatorByMobile(mobileJsonBean); 
		
		/*try {
			mobileService.testAsync("meidi");
		} catch (Exception e) {
			tracer.addTag("mobileService Exception", "Exception:"+e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	    return bean;
	}
	
	@RequestMapping(value="/admin")
	public String info(){
		return "hello! common-microservice-eureka-mobile";
		
	}
}
