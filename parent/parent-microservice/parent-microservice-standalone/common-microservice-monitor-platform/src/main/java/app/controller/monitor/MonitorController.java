/**
 * 
 */
package app.controller.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.system.MonitorEurekaServerInfo;
import app.service.MonitorService;

/**
 * @author sln
 * @date 2018年10月29日下午4:38:21
 * @Description: 定时监控任务
 */
@Controller
@RequestMapping(value="/tasker")
public class MonitorController {
	@Autowired
	private MonitorService monitorService;
	/**
	 * eureka——监控信息
	 */
    @GetMapping(value = "/eureka-info")
	public String eurekaList(Model model) {
    	List<MonitorEurekaServerInfo> eurekaList = monitorService.getEurekaList();
    	model.addAttribute("isMicroService", 10);
		model.addAttribute("eurekaList", eurekaList);
		return "eureka/eureka_list";
	}
}
