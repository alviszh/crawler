package app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.search.bean.SearchTaskBean;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.client.SearchTaskClient;
import app.commontracerlog.TracerLog;
import app.service.SearchCrawlerService;

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
@RequestMapping("/sanwang") 
@RestController
@Configuration
public class NewsRestController {

  /*  @Autowired
    private NewsESServiceImpl newsESServiceImpl;*/
    @Autowired
    private SearchCrawlerService searchCrawlerService;
    @Autowired
    private TracerLog tracerLog;

	@Autowired
	private SearchTaskRepository searchTaskRepository;
	
	@Autowired
	private SearchTaskClient searchTaskClient;
	    
    @Scheduled(cron = "${jobs.cron}")
//	@Scheduled(fixedDelay=50000)  //上一次执行完毕时间点后2秒再次执行；
	public SearchTask crawlerClient() {
    	long startTime = System.currentTimeMillis();
    	tracerLog.System("-----------系统定时开始------------",""+startTime);
		
		List<SearchTaskBean> list = searchTaskClient.getTask();
		
		if (list.size() <= 0) {
			long endTime = System.currentTimeMillis();
			tracerLog.System("未发现未抓取程序        当前共计程序耗时ms：", (endTime - startTime) + "ms");
			tracerLog.System("未发现未抓取程序        当前共计程序耗时s：", (endTime - startTime) / 1000 + "s");
			return null;
		}
		List<Future<String>> future_list = new ArrayList<>();
		long s2 = System.currentTimeMillis();		
						
		tracerLog.System("获取列表数量:"+list.size(), (s2 - startTime) + "ms");

		for(SearchTaskBean searchTaskBean :  list){
			tracerLog.System("taskid", searchTaskBean.getId()+"");
			long s3 = System.currentTimeMillis();
			SearchTask searchTask = searchTaskRepository.findTop1ById(searchTaskBean.getId());
			long s4 = System.currentTimeMillis();
			
			tracerLog.System("searchTask"+";"+searchTaskBean.getId()+" 耗时："+(s4 - s3) + "ms", searchTask.toString());
			Future<String> future = searchCrawlerService.Crawler(searchTask,searchTask.getLinkurl());
			future_list.add(future);
		}
		
		boolean isdone = true;
		tracerLog.System("future_list start", future_list.size() + "");
		long startTime222 = System.currentTimeMillis();
		while (isdone) {
			for (Future<String> future : future_list) {

				if (future.isDone()) { // 判断是否执行完毕
					future_list.remove(future);
					tracerLog.System("future_list", future_list.size() + "");

					break;
				}

			}
			if (future_list.size() <= 0) {
				isdone = false;
			}

		}
		
		long endTime = System.currentTimeMillis();
		tracerLog.System("future_list.size()---"+future_list.size()+"当前共计程序耗时ms：", (endTime - startTime) + "ms");
		tracerLog.System("future_list.size()---"+future_list.size()+"当前共计程序耗时s：", (endTime - startTime222) + "ms");
		return null;
	}
  
}
