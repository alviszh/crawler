package app.service;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.search.NewsContent;
import com.microservice.dao.entity.crawler.search.NewsListJson;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.NewsContentRepository;
import com.microservice.dao.repository.crawler.search.NewsListJsonRepository;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParm;
import app.service.unit.SanWangUnitService;

/**
 * 
 * 项目名称：common-microservice-search 类名称：SearchCrawlerService 类描述： 创建人：hyx
 * 创建时间：2018年1月18日 下午3:12:48
 * 
 * @version
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.search")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.search")
public class SearchCrawlerService {

	@Autowired
	private SearchFutureService searchFutureService;

	@Autowired
	private SearchTaskRepository searchTaskRepository;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private NewsListJsonRepository newsListJsonRepository;

	@Autowired
	private NewsContentRepository newsContentRepository;
	
	@Autowired
	private SanWangUnitService sanWangUnitService;

	@Async("ExecutorForSearch") //配置类中的方法名
	public Future<String> Crawler(SearchTask searchTask, String linkurl) {
		long  start = System.currentTimeMillis();
		tracerLog.System("开始异步执行", linkurl);
		WebParm webParm = new WebParm();
		
		if (searchTask == null) {
			tracerLog.System("search Crawler", "未获取search task");
			return  new AsyncResult<String>("404");
		}
		tracerLog.System("异步触发search Crawler"+linkurl, searchTask.toString());
		// 修改状态改为 爬取中 1

		tracerLog.System("taskid", searchTask.getTaskid() + ";" + linkurl);
		if (searchTask.getType().indexOf("baidu") != -1) {
			tracerLog.System("search Crawler 爬baidu中", "修改状态改为 爬取中 1");
			webParm = searchFutureService.baiduCrawler(searchTask);
		} else if (searchTask.getType().indexOf("haosou") != -1) {
			tracerLog.System("search Crawler 爬haosou中", "修改状态改为 爬取中 1");
			webParm = searchFutureService.haosouCrawler(searchTask);

		} else if (searchTask.getType().indexOf("sougou") != -1) {
			tracerLog.System("search Crawler 爬sougou中", "修改状态改为 爬取中 1");
			webParm = searchFutureService.sougouCrawler(searchTask);
		} else {
			searchTask.setPhase("404");
		}

		try {
			List<NewsListJson> newsListJsonvalue = webParm.getList();
			if (newsListJsonvalue == null || newsListJsonvalue.size() < 0) {
				if (searchTask.getRenum() + 1 < 4) {
					searchTask.setRenum(searchTask.getRenum() + 1);
					searchTask.setPhase("404");
				} else {
					searchTask.setPhase("404");
				}
				searchTaskRepository.save(searchTask);
				return  new AsyncResult<String>("200");
			}

			tracerLog.System("future isdone", "开始保存到数据库");
			for (NewsListJson newsListJson : newsListJsonvalue) {
				newsListJson.setLinkUrl(sanWangUnitService.toUtf8String(newsListJson.getLinkUrl()));
				newsListJson = newsListJsonRepository.save(newsListJson);

				List<NewsContent> newsContentvalue = newsListJson.getList();

				try {
					newsContentvalue = newsContentRepository.saveAll(newsContentvalue);
				} catch (Exception e) {
					tracerLog.System("sql error", e.getMessage());
				}

			}

			searchTask.setPhase("200");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			searchTask.setPhase("404");
			e.printStackTrace();
		}
		long  end = System.currentTimeMillis();
		searchTaskRepository.save(searchTask);
		long  end2 = System.currentTimeMillis();
		tracerLog.System("执行结束"+linkurl+"       当前详细页连接耗时ms：", (end2 - start) + "ms");
		tracerLog.System("执行结束2"+linkurl+"       当前详细页存储耗时ms：", (end - start) + "ms");
		return  new AsyncResult<String>("200");

	}

}
