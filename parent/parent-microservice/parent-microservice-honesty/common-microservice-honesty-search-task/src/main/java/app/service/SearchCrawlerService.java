package app.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.bean.IsDoneBean;
import app.bean.SanWangJsonBean;
import app.client.aws.AwsApiClient;
import app.service.log.SysLog;
import app.unit.SanWangUnit;

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
	
	@Value("${jobs.num}")
	int num;
	
	private static Queue<SearchTask> queue = new ConcurrentLinkedQueue<SearchTask>();

	
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.SERIALIZABLE)
	public List<SearchTask> getTask() {
		System.out.println("======进入serchtask 查询====");
		
		if (queue.size() <= 0) {
			return new ArrayList<>();
		}
		
		if(num<=0){
			return new ArrayList<>();
		}
		sysLog.output("queue", queue.size()+"");
		List<SearchTask> listreturn = new ArrayList<>();
		for(int i=0;i<num;i++){
			SearchTask searchTask = queue.poll();
			try{
				sysLog.output("queue 分发出的数据数据",searchTask.toString());
				
				listreturn.add(searchTask);
			}catch(Exception e){
				sysLog.output("queue 分发出的数据数据","null");
			}
			
		}		
		return listreturn;
	}

	public void geterror() {
		System.out.println("queue===" +queue.size());
		if (queue.size() < 20) {
			queue = getSearchTask(queue);
		}
		Timestamp time = Timestamp.valueOf(LocalDateTime.now().plusMinutes(-1));

		List<SearchTask> list = searchTaskRepository.findByPhaseAndUpdateTimeOrderByIdDescPrioritynumDesc("1", time);
		List<SearchTask> list2 = new ArrayList<>();
		for (SearchTask searchTask : list) {
			searchTask.setPhase("404");
			System.out.println(searchTask.toString());

			list2.add(searchTask);
		}

		searchTaskRepository.saveAll(list2);
	}

	public IsDoneBean createTaskList(SanWangJsonBean sanWangJsonBean) {
		return searchFutureService.createTaskList(sanWangJsonBean);
	}

	public Queue<SearchTask> getSearchTask(Queue<SearchTask> queue) {

		List<SearchTask> list_searchtask = searchTaskRepository.findTop40ByPhase("0");
		if(list_searchtask ==null || list_searchtask.size() <=0){
			return queue;

		}
		HttpProxyRes httpProxyRes = null;
		List<HttpProxyBean> httpProxyBeanSet = null;
		if (istrueip) {
			httpProxyRes = awsApiClient.getProxy(4);
			sysLog.output("httpProxyBean", httpProxyRes.toString());

		}
		if (httpProxyRes != null) {
			httpProxyBeanSet = httpProxyRes.getHttpProxyBeanSet();
		}
		int i = 0;
		for (SearchTask searchTask : list_searchtask) {
			searchTask.setPhase("1");
			searchTask.setRenum(searchTask.getRenum() + 1);

			if (istrueip) {
				if (httpProxyBeanSet != null && httpProxyBeanSet.size() > 0) {
					
					try{
						String ip = SanWangUnit.getIpByDNS(httpProxyBeanSet.get(i/10).getName());
						searchTask.setIpaddress(ip);
						searchTask.setIpport(httpProxyBeanSet.get(i/10).getPort());

					}catch(Exception e){
						e.printStackTrace();
						sysLog.output("解析dns错误", httpProxyBeanSet.get(i/10).toString());
						
					}
				}
			}

			try {
				searchTask = searchTaskRepository.save(searchTask);
				sysLog.output("queue 取出数据", searchTask.toString());
			} catch (Exception e) {
				sysLog.output("存储 数据", e.getMessage());

			}
			i++;

		}

		if (list_searchtask.size() > 0) {
			try {
				queue.addAll(list_searchtask);

			} catch (Exception e) {
				sysLog.output("加入数据queue报错", e.getMessage());

				queue = new LinkedList<SearchTask>();
				queue.addAll(list_searchtask);

			}
		} else {
			sysLog.output("加入数据queue", "无爬取数据");
		}

		return queue;
	}
	
	public static void main(String[] args) {
		System.out.println(32/10);
	}
}
