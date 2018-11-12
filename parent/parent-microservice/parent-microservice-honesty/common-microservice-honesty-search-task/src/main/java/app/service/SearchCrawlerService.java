package app.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.bean.IsDoneBean;
import app.bean.SanWangJsonBean;
import app.client.aws.AwsApiClient;
import app.service.log.SysLog;

/**
 * 
 * 项目名称：common-microservice-search 类名称：SearchCrawlerService 类描述： 创建人：hyx
 * 创建时间：2018年1月18日 下午3:12:48
 * 
 * @version
 */

@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.search")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.search")
public class SearchCrawlerService {

	@Autowired
	private SearchFutureService searchFutureService;

	@Autowired
	private SearchTaskRepository searchTaskRepository;

	@Autowired
	private SysLog sysLog;

	@Autowired
	private AwsApiClient awsApiClient;

	@Value("${jobs.istrueip}")
	boolean istrueip;

	public IsDoneBean createTaskList(SanWangJsonBean sanWangJsonBean) {
		return searchFutureService.createTaskList(sanWangJsonBean);
	}

	public Queue<SearchTask> getSearchTask(Queue<SearchTask> queue) {

		List<SearchTask> list2 = searchTaskRepository.findTop40ByPhase("0");
		HttpProxyRes httpProxyRes = null;
		List<HttpProxyBean> httpProxyBeanSet = null;
		if (istrueip) {
			httpProxyRes = awsApiClient.getResProxy(40);
			sysLog.output("httpProxyBean", httpProxyRes.toString());

			// System.out.println(httpProxyBean.getIp()+"=========="+httpProxyBean.getPort());
			// searchTask.setIpaddress(httpProxyBean.getIp());
			// searchTask.setIpport(httpProxyBean.getPort());

			// searchTask = searchTaskRepository.save(searchTask);
			// sysLog.output("istrueip searchTask", searchTask.toString());
		}
		if (httpProxyRes != null) {
			httpProxyBeanSet = httpProxyRes.getHttpProxyBeanSet();
		}
		int i = 0;
		for (SearchTask searchTask : list2) {
			searchTask.setPhase("1");
			searchTask.setRenum(searchTask.getRenum() + 1);

			if (istrueip) {
				if (httpProxyBeanSet != null && httpProxyBeanSet.size() > 0) {
					searchTask.setIpaddress(httpProxyBeanSet.get(i).getIp());
					searchTask.setIpport(httpProxyBeanSet.get(i).getPort());
				}
			}

			try {
				searchTask = searchTaskRepository.save(searchTask);
				sysLog.output("queue 取出数据", searchTask.toString());
			} catch (Exception e) {
				sysLog.output("存储 数据", e.getMessage());

			}

		}

		if (list2.size() > 0) {
			try {
				queue.addAll(list2);

			} catch (Exception e) {
				sysLog.output("加入数据queue报错", e.getMessage());

				queue = new LinkedList<SearchTask>();
				queue.addAll(list2);

			}
		} else {
			sysLog.output("加入数据queue", "无爬取数据");
		}

		return queue;
	}
}
