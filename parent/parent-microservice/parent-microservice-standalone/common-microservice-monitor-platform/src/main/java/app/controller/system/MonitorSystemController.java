/**
 * 
 */
package app.controller.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;

import app.entity.system.MonitorEurekaServerInfo;
import app.service.system.MonitorSystemService;

/**
 * @author sln
 * @Description: 系统层面-数据库操作接口
 */
@RestController
@RequestMapping(value="/system")
public class MonitorSystemController {
	@Autowired 
	private MonitorSystemService monitorSystemService;
    /**
     * eureka分页查询任务列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getEurekaPages", method= RequestMethod.POST)
	public @ResponseBody
	PageInfo<MonitorEurekaServerInfo> getMonitorEurekaServerInfoPages(@RequestParam(value = "currentPage") int currentPage,
											@RequestParam(value = "pageSize") int pageSize,
											@RequestParam(value = "appname", required = false) String appname,
											@RequestParam(value = "developer", required = false) String developer) {

		//根据条件查询
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appname",appname);
		paramMap.put("developer",developer);

		Page<MonitorEurekaServerInfo> tasksPage = monitorSystemService.getTaskByParams(paramMap, currentPage, pageSize);
		System.out.println("******getTaskPages:"+tasksPage);

		PageInfo<MonitorEurekaServerInfo> pageInfo = new PageInfo<MonitorEurekaServerInfo>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
    /**
     * eureka删除某个监控项目
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/removeItem", method = { RequestMethod.POST})
	public void removeItem(Long id) {
    	System.out.println("要删除的监控项的id是："+id);
		monitorSystemService.deleteItemById(id);
	}
}
