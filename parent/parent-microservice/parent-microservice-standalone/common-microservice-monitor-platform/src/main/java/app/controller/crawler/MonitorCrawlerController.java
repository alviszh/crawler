/**
 * 
 */
package app.controller.crawler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;

import app.entity.crawler.MonitorTelecomTasker;
import app.service.crawler.MonitorCrawlerService;

/**
 * @author sln
 * @Description: 爬虫层面-数据库操作接口
 */
@Controller
@RequestMapping(value="/crawler")
public class MonitorCrawlerController {
	@Autowired
	private MonitorCrawlerService monitorCrawlerService;
	/**
     * carrier分页查询任务列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getCarrierPages", method= RequestMethod.POST)
	public @ResponseBody
	PageInfo<MonitorTelecomTasker> getMonitorEurekaServerInfoPages(@RequestParam(value = "currentPage") int currentPage,
											@RequestParam(value = "pageSize") int pageSize,
											@RequestParam(value = "province", required = false) String province,
											@RequestParam(value = "developer", required = false) String developer) {

		//根据条件查询
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("province",province);
		paramMap.put("developer",developer);

		Page<MonitorTelecomTasker> tasksPage = monitorCrawlerService.getCarrierTaskByParams(paramMap, currentPage, pageSize);
		System.out.println("******getTaskPages:"+tasksPage);

		PageInfo<MonitorTelecomTasker> pageInfo = new PageInfo<MonitorTelecomTasker>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
    /**
     * 删除某个监控项目
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/removeCarrierItem", method = { RequestMethod.POST})
	public void removeItem(Long id) {
    	monitorCrawlerService.deleteItemById(id);
	}
    /**
     * 添加某个监控项目
     */
	@RequestMapping(value = "/addCarrierItem", method = { RequestMethod.POST})
	public MonitorTelecomTasker addBusinessConsumer(MonitorTelecomTasker monitorTelecomTasker) {
		monitorCrawlerService.saveCarrierItem(monitorTelecomTasker);
		return monitorTelecomTasker;
	}
}
