package app.controller.docs;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文档中心
 * @return
 */
@Controller
@RequestMapping(value="/docs")
public class DocsController {
	
	/**
	 * 开发指南 - 平台简介
	 */
    @RequestMapping("/dev-guide/platform-intro")
    public String platformIntro(Model model){
    	model.addAttribute("first_level", "dev-guide");
		model.addAttribute("seccond_level ", "platform-intro");
		//model.addAttribute("third_level ", "");
		 
        return "docs/dev-guide/platform-intro";
    }
    
    /**
	 * 开发指南 - 平台入驻
	 */
    @RequestMapping("/dev-guide/platform-enter")
    public String platformEnter(){
        return "docs/dev-guide/platform-enter";
    }
    
    /**
   	 * 开发指南 - 创建应用
   	 */
    @RequestMapping("/dev-guide/create-app")
    public String createApp(){
       return "docs/dev-guide/create-app";
    }
    
 
    /**
	 * 接入指南 - 产品接入必读
	 */
    @RequestMapping("/dev-guide/platform-accessread")
    public String platformAccessread(){
    	System.out.println("accessread");
        return "docs/dev-guide/platform-accessread";
    }
    
	/**
	 * 接入指南 - ios快速接入
	 */
	@RequestMapping("/dev-guide/platform-accessios")
	public String platformAccessIos() {
		System.out.println("accessios");
		return "docs/dev-guide/platform-accessios";
	}

	/**
	 * 接入指南 - ios快速接入
	 */
	@RequestMapping("/dev-guide/platform-accessandroid")
	public String platformAccessAndroid() {
		System.out.println("accessAndroid");
		return "docs/dev-guide/platform-accessandroid";
	}

	/**
	 * 接入指南 - H5快速接入
	 */
	@RequestMapping("/dev-guide/platform-accessh5")
	public String platformAccessH5() {
		System.out.println("accessh5");
		return "docs/dev-guide/platform-accessh5";
	}
}
