package app.controller.developer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.EnvironmentDef;

import app.entity.developer.App;
import app.entity.developer.Product;
import app.entity.security.SUser;
import app.service.AppService;
import app.service.SUserService;
import app.utils.QrCodeUtil;

/**
 * 开发者中心
 * 
 * @author tz
 *
 */
@Controller
@RequestMapping(value = "/developer/app")
public class AppController {

	@Autowired
	private SUserService suserService;
	
	@Autowired
	private AppService appService;

	@Value("${pbccrc.prod.url.domain}")
	String prodDomain;
	
	@Value("${opendata.pbccrc.h5url}")
	String pbccrch5url;
	
	
	/**
	 * 开发者中心 - 应用管理
	 */
    @RequestMapping(value = "/app-manage" , method = {RequestMethod.POST,RequestMethod.GET})
	public String platformIntro(Model model,String appName) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		List<App> applist = appService.queryApp(suser, appName);
		model.addAttribute("appName", appName);
		model.addAttribute("datas", applist);
		return "developer/applist";
	}
	
	
    /**
     * 添加应用
     * @param model
     * @param appid
     * @return
     */
    @RequestMapping(value = "/editapp")
    public String editApp(Model model){
    	List<Product> listProduct = appService.queryProduct();
    	model.addAttribute("listproduct", listProduct);
    	return "developer/editapp";
    }
    
    /**
     * 添加应用
     * @param model
     * @param app
     * @param positions
     * @return
     */
    @RequestMapping(value = "/saveapp" , method = RequestMethod.POST)
    public String  saveapp(Model model,@ModelAttribute("app") App app,@RequestParam(value = "position[]", required = false) String[] positions){
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		appService.addApp(suser,app,positions);
		
		return "redirect:/developer/app/app-manage";
    }
    
    /**
	 * 开发者中心 - 应用详情
	 */
    @RequestMapping(value = "/showapp/{appId}" , method = {RequestMethod.POST,RequestMethod.GET})
    public String showApp(@PathVariable String appId,Model model){
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = appService.queryAppdetails(suser, appId,EnvironmentDef.SANDBOX);
		
		model.addAttribute("app", app);
		List<Product> differenceSet = appService.differenceSet(app);
    	model.addAttribute("differenceSet", differenceSet);
    	return "developer/appshow";
    }
    
	/**
	 * 添加产品
	 */
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	public String addProduct(String appId, Model model,
			@RequestParam(value = "addposition[]", required = false) String[] addposition) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = appService.queryAppdetails(suser, appId,EnvironmentDef.SANDBOX);
		app = appService.addProduct(app, addposition);
		return "redirect:/developer/app/showapp/" + appId;
	}
	
	/**
	 * 申请上线
	 */
	@RequestMapping(value = "/applyonline", method = RequestMethod.POST)
	public String applyOnline(String appId, Model model,
			@RequestParam(value = "position[]", required = false) String[] position) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = appService.queryAppdetails(suser, appId,EnvironmentDef.SANDBOX);
		appService.applyOnline(app, position);
		return "redirect:/developer/app/showapp/" + appId;
	}
    
    
    /**
	 * 开发者中心 - 应用详情
	 */
    @RequestMapping(value = "/sandbox/{appId}/{mode}")
    public String sandbox(Model model,@PathVariable String appId,@PathVariable String mode){
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	SUser suser = suserService.findUserByLoginName(userDetails.getUsername());
		App app = appService.queryAppdetails(suser, appId,mode);
		model.addAttribute("app", app);
		String title = "沙箱测试";
		String token = app.getTest_token();
		String client_id = app.getTest_clientId();
		String client_secret = app.getTest_client_secret();
		if(mode.equals(EnvironmentDef.PROD)){
			title = "生产环境";
			token = app.getProd_token();
			client_id = app.getProd_clientId();
			client_secret = app.getProd_client_secret();
		}
//		OauthClientDetails oauthClientDetails = appService.findOauthClientDetails(app,mode);
		model.addAttribute("title", title);
		model.addAttribute("client_id", client_id);
		model.addAttribute("client_secret", client_secret);
		model.addAttribute("token", token);
//		model.addAttribute("oauthClientDetails", oauthClientDetails);
    	return "developer/sandbox";
    }
    
    /**
	 * 开发者中心 - 应用详情
	 */
    @RequestMapping(value = "/apitest")
    public String apitest(Model model,Long appid){
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	return "developer/apitest";
    }


	/**
	 * API测试 - 人行征信
	 */
	@RequestMapping(value = "/apitest/pbccrc")
	public String apitestPbccrc(Model model,Long appid){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("prodDomain", prodDomain);
		String createQrCode = QrCodeUtil.createQrCode(pbccrch5url);
		model.addAttribute("qrcode", "data:image/jpg;base64,"+createQrCode);
		
		return "developer/apitestpbccrc";
	}

}
