package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**   
*    
* 项目名称：common-microservice-webmanage   
* 类名称：SanWangController   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年2月5日 上午11:09:59   
* @version        
*/
@Controller
@RequestMapping("/doc/honesty")
public class HonestyController {
	public static final Logger log = LoggerFactory.getLogger(HonestyController.class);
	
	@RequestMapping("/executorGet")
	public String executorGet(Model model) {
		model.addAttribute("module", CbConfModule.HONESTY.getCode());
		model.addAttribute("isApi", true);
		return "doc/honesty/executorGet";
	}
	
	@RequestMapping("/executorStatueGet")
	public String executorStatueGet(Model model) {
		model.addAttribute("module", CbConfModule.HONESTY.getCode());
		model.addAttribute("isApi", true);
		return "doc/honesty/executorStatueGet";
	}
	
	@RequestMapping("/executorSearchGet")
	public String statueGet(Model model) {
		model.addAttribute("module", CbConfModule.HONESTY.getCode());
		model.addAttribute("isApi", true);
		return "doc/honesty/executorSearchGet";
	}

}
