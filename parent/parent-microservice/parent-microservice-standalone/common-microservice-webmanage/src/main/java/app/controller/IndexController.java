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
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    
    /**
     * 主页
     * @return
     */
    @RequestMapping("/carrier")
    public String indexCarrier(){
        return "carrier/index";
    }

    /**
     * 权限不足错误页面
     * @return
     */
    @RequestMapping("/403")
    public String error(){
        return "error/403";
    }


    /**
     * 数据查询（ETL）
     * @return
     */
    @RequestMapping("/dataSearch")
    public String dataSearch(){
        return "datasearch";
    }
}
