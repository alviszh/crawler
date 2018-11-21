package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.bean.IsDoneBean;
import app.bean.SanWangJsonBean;
import app.commontracerlog.TracerLog;
import app.service.SearchCrawlerService;
import app.service.log.SysLog;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：aa   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月17日 上午11:29:24   
* @version        
*/
/**
 * 城市 Controller 实现 Restful HTTP 服务
 * <p>
 * Created by bysocket on 03/05/2017.
 */
@RestController
@Configuration
@RequestMapping("/api-service/sanwang/search")
@EnableJpaAuditing
public class NewsRestController {

	@Autowired
	private SearchCrawlerService searchCrawlerService;
	@Autowired
	private SearchTaskRepository searchTaskRepository;

	@Autowired
	private TracerLog tracerLog;
	
	@Autowired
	private SysLog sysLog;
	
	
	
	private Gson gs = new Gson();

	@PostMapping(path = "/task")
	public IsDoneBean creatTask(@RequestBody SanWangJsonBean sanWangJsonBean) {
		tracerLog.qryKeyValue("taskid", sanWangJsonBean.getTaskid());
		tracerLog.output("sanWangJsonBean", gs.toJson(sanWangJsonBean));
		if (null == sanWangJsonBean.getTaskid()) {
			tracerLog.output("taskid is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		}
		if (sanWangJsonBean.getKeys().size() < 0) {
			tracerLog.output("keys is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("keys is null !");
		}

		return searchCrawlerService.createTaskList(sanWangJsonBean);
	}


	@RequestMapping(value = "/gettask", method = RequestMethod.POST)
	public List<SearchTask> getTask() {
		
		return searchCrawlerService.getTask();
	}

	@GetMapping(path = "/statue/taskid")
	public IsDoneBean isdone(@RequestParam(value = "taskid") String taskid) {

		sysLog.output("taskid", taskid);
		List<SearchTask> list = searchTaskRepository.findByTaskid(taskid);
		int totalnum = list.size();
		int finishednum = 0;
		int crawleringnum = 0;
		int errornum = 0;

		IsDoneBean isDoneBean = new IsDoneBean();
		isDoneBean.setTaskid(taskid);
		for (SearchTask searchTask : list) {
			if (searchTask.getPhase().trim() != "0") {
				if (searchTask.getPhase().trim().indexOf("1") != -1) {
					crawleringnum++;
				}

				if (searchTask.getPhase().trim().indexOf("200") != -1
						|| searchTask.getPhase().trim().indexOf("404") != -1) {
					finishednum++;
					if (searchTask.getPhase().indexOf("404") != -1) {
						errornum++;
					}
				}
			}
		}

		isDoneBean.setTotalnum(totalnum);
		isDoneBean.setUnfinishednum(totalnum - finishednum);
		isDoneBean.setFinishednum(finishednum);
		isDoneBean.setCrawleringnum(crawleringnum);
		isDoneBean.setErrornum(errornum);

		return isDoneBean;
	}

	@Scheduled(cron = "0/2 * * * * ?")
	public void geterror() {
		searchCrawlerService.geterror();
	}

}
