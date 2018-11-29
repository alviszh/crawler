/**
 * 
 */
package app.controller.monitor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sln
 * @date 2018年10月29日下午4:38:21
 * @Description: 监控任务-用于所有页面加载
 */
@Controller
@RequestMapping(value="/tasker")
public class MonitorController {
	/**
	 * eureka——监控信息
	 */
    @GetMapping(value = "/eureka-info")
	public String eurekaHtml(Model model) {
    	model.addAttribute("isMicroService", 10);
		return "eureka/eureka_list";
	}
    /**
     * carrier——运营商监控信息
     */
    @GetMapping(value = "/carrier-info")
    public String carrierHtml(Model model) {
    	model.addAttribute("isCarrier", 10);
    	return "carrier/carrier_list";
    }
}
