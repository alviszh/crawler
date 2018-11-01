package app.controller.developer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.dao.developer.AppProductListRepository;
import app.entity.developer.App;
import app.entity.developer.AppProductList;
import app.entity.security.SUser;
import app.service.AppService;
import app.service.SUserService;

/**
 * 开发者中心
 * 
 * @author tz
 *
 */
@RestController
@RequestMapping(value = "/developer/appdata")
public class AppRestController {

	@Autowired
	private SUserService suserService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private AppProductListRepository appProductListRepository;
	
	
	@RequestMapping(value = "/appproductlistdetails", method = RequestMethod.POST)
	public AppProductList getAppproductlistdetails(String  appId,Long appProductListId) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = appService.queryAppdetails(suser, appId,"all");
		AppProductList appProductList = appProductListRepository.findById(appProductListId).get();
		return appProductList;
	}
	
	@RequestMapping(value = "/modifyappproductlistdetails", method = RequestMethod.POST)
	public AppProductList modifyAppproductlistdetails(String appId,Long appProductListId,  String task_notice_url, String login_notice_url,
			String crawler_notice_url, String report_notice_url,String param) {
		AppProductList appProductList = appService.modifyAppproductlistdetails(appId, appProductListId, task_notice_url, login_notice_url, crawler_notice_url,
				report_notice_url,param);
		return appProductList;
	}
	
	
	@RequestMapping(value = "/requestnoticeurl", method = RequestMethod.POST)
	public Map<String, Object> requestnoticeurl(String url,String appId,Long appProductListId,String request_body_param,String param) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		appService.requestnoticeurl(url, appId, appProductListId, request_body_param, param);
		
		
		return result;
	}
	

}
