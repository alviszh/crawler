package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.maimai.json.MaimaiJsonBean;
import com.crawler.pbccrc.json.PbccrcJsonBean;

import app.commontracerlog.TracerLog;
import app.service.MaimiaService;
//import app.service.Phone;

@RestController
@Configuration
@RequestMapping("/maimai")
public class MaimaiController {
	@Autowired
    private TracerLog tracerLog;
	@Autowired 
    private MaimiaService maimiaService;
	
//	@Autowired
//    private Phone phone;
	/**
     * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
     * @param maimaiJsonBean
     */
    @PostMapping(path = "/login")
    public String getLogin(@RequestBody PbccrcJsonBean pbccrcJsonBean){
    	maimiaService.login(pbccrcJsonBean);
//    	try {
//			phone.getPhone();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
    	
    }
    
    
    @PostMapping(path = "/crawler")
    public String getCrawler(@RequestBody PbccrcJsonBean pbccrcJsonBean){
    	maimiaService.getAllData(pbccrcJsonBean);
		return null;
    	
    }
}
