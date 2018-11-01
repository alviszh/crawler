package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	/**
	 * 登录页
	 * @return
	 */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    
    /**
     * 主页
     * @return
     */
    @RequestMapping("/index")
    public String index(){
    	System.out.println("-index-");
        return "index";
    }
    
    /**
     * 主页
     * @return
     */
    @RequestMapping("/")
    public String home(){
    	System.out.println("-home-");
        return "index";
    }

}
