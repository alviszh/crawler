package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.search.SearchTaskRepository;

import app.bean.IsDoneBean;
import app.bean.SanWangJsonBean;
import app.commontracerlog.TracerLog;

/**
 * 
 * 项目名称：common-microservice-search 类名称：SearchFutureService 类描述： 创建人：hyx
 * 创建时间：2018年1月18日 上午10:53:02
 * 
 * @version
 */

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.search")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.search")
public class SearchFutureService {

	@Autowired
	private SearchTaskRepository searchTaskRepository;

	@Autowired
	private TracerLog tracerLog;

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 百度创建待爬取url队列
	 * 创建人：hyx 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 List<SearchTask>
	 */
	public List<SearchTask> geturlByPageBaidu(String taskid, String keyword, int pagenum, int priority) {
		String url = "https://www.baidu.com/s?wd=" + keyword;

		List<SearchTask> list = new ArrayList<>();
		for (int i = 0; i < pagenum; i++) {
			SearchTask searchTask = new SearchTask();
			String urlbaidu = url + "&pn=" + i * 10;
			searchTask.setLinkurl(urlbaidu);
			searchTask.setType("baidu");
			searchTask.setTaskid(taskid);
			searchTask.setKeyword(keyword);
			searchTask.setPagenum(pagenum);
			searchTask.setPhase("0");

			list.add(searchTask);
		}

		return list;
	}

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 好搜创建待爬取url队列
	 * 创建人：hyx 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 List<SearchTask>
	 */
	public List<SearchTask> geturlByPageHaoSou(String taskid, String keyword, int pagenum, int priority) {
		String url = "https://www.so.com/s?q=" + keyword;

		if (pagenum == 0) {
			pagenum = 1;
		}
		List<SearchTask> list = new ArrayList<>();
		for (int i = 1; i < pagenum + 1; i++) {
			SearchTask searchTask = new SearchTask();
			String urlbaidu = url + "&pn=" + i;
			searchTask.setLinkurl(urlbaidu);
			searchTask.setType("haosou");
			searchTask.setTaskid(taskid);
			searchTask.setKeyword(keyword);
			searchTask.setPagenum(pagenum);
			searchTask.setPhase("0");
			list.add(searchTask);
		}

		return list;
	}

	/**
	 * 
	 * 项目名称：common-microservice-search 所属包名：app.service 类描述： 搜狗创建待爬取url队列
	 * 创建人：hyx 创建时间：2018年1月24日
	 * 
	 * @version 1 返回值 List<SearchTask>
	 */
	public List<SearchTask> geturlByPageSouGou(String taskid, String keyword, int pagenum, int priority) {
		String url = "https://www.sogou.com/web?query=" + keyword;

		if (pagenum == 0) {
			pagenum = 1;
		}
		List<SearchTask> list = new ArrayList<>();
		for (int i = 1; i < pagenum + 1; i++) {
			SearchTask searchTask = new SearchTask();
			String urlbaidu = url + "&page=" + i;
			searchTask.setLinkurl(urlbaidu);
			searchTask.setType("sougou");

			searchTask.setTaskid(taskid);
			searchTask.setKeyword(keyword);
			searchTask.setPagenum(pagenum);
			searchTask.setPhase("0");
			list.add(searchTask);
		}

		return list;
	}

	/**
	 * @Description: 创建taskid及生成taskID ()
	 * @param insuranceJsonBean
	 * @return TaskInsurance
	 */
	public IsDoneBean createTask(SanWangJsonBean SanWangJsonBean) {

		String taskid = SanWangJsonBean.getTaskid();
		int pagenum = SanWangJsonBean.getPagenum();
		String keyword = SanWangJsonBean.getKeys().get(0);

		Integer priority = SanWangJsonBean.getPriority(); // 优先级
		if (priority == 0 || priority == null) {
			priority = 20;
		}
		if (null == taskid) {
			tracerLog.output("error", "taskid is null !");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		} else {
			List<SearchTask> listTask = searchTaskRepository.findByTaskid(taskid);

			if (null == listTask || listTask.size() <= 0) {
				listTask.addAll(geturlByPageBaidu(taskid, keyword, pagenum, priority));
				listTask.addAll(geturlByPageHaoSou(taskid, keyword, pagenum, priority));
				listTask.addAll(geturlByPageSouGou(taskid, keyword, pagenum, priority));
				searchTaskRepository.saveAll(listTask);

				int totalnum = listTask.size();
				int unfinishednum = totalnum;
				int finishednum = 0;
				int crawleringnum = 0;
				int errornum = 0;

				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setTaskid(taskid);
				isDoneBean.setTotalnum(totalnum);
				isDoneBean.setUnfinishednum(unfinishednum);
				isDoneBean.setFinishednum(finishednum);
				isDoneBean.setCrawleringnum(crawleringnum);
				isDoneBean.setErrornum(errornum);
				tracerLog.output("taskid--不存在   :", taskid);
				tracerLog.output("SearchTask 生成  :", taskid);
				return isDoneBean;
			} else {
				tracerLog.output("taskid存在   :", taskid);
				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setErrormessage("taskid存在   :" + taskid + ",请创建新的taskid");
				return isDoneBean;
			}
		}
	}

	/**
	 * @Description: 创建taskid及生成taskID ()
	 * @param insuranceJsonBean
	 * @return TaskInsurance
	 */
	public IsDoneBean createTaskList(SanWangJsonBean sanWangJsonBean) {
		String taskid = sanWangJsonBean.getTaskid();
		int pagenum = sanWangJsonBean.getPagenum();
		List<String> keywords = sanWangJsonBean.getKeys();

		Integer priority = sanWangJsonBean.getPriority(); // 优先级

		if (priority == 0 || priority == null) {
			priority = 20;
		}
		if (null == taskid) {
			tracerLog.output("error", "taskid is null !");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		} else {
			List<SearchTask> listTask = searchTaskRepository.findByTaskid(taskid);

			if (null == listTask || listTask.size() <= 0) {
				int i = 0;
				for (String keyword : keywords) {
					tracerLog.output("第" + i + "个", keyword);
					if (keyword == null || keyword.isEmpty()) {
						i++;
						continue;
					}
					listTask.addAll(geturlByPageBaidu(taskid, keyword, pagenum, priority));
					listTask.addAll(geturlByPageHaoSou(taskid, keyword, pagenum, priority));
					listTask.addAll(geturlByPageSouGou(taskid, keyword, pagenum, priority));
					i++;
				}

				searchTaskRepository.saveAll(listTask);

				int totalnum = listTask.size();
				int unfinishednum = totalnum;
				int finishednum = 0;
				int crawleringnum = 0;
				int errornum = 0;

				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setTaskid(taskid);
				isDoneBean.setTotalnum(totalnum);
				isDoneBean.setUnfinishednum(unfinishednum);
				isDoneBean.setFinishednum(finishednum);
				isDoneBean.setCrawleringnum(crawleringnum);
				isDoneBean.setErrornum(errornum);
				tracerLog.output("taskid不存在   :", taskid);
				tracerLog.output("SearchTask 生成  :", taskid);
				return isDoneBean;
			} else {
				tracerLog.output("taskid存在   :", taskid);
				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setErrormessage("taskid存在   :" + taskid + ",请创建新的taskid");
				return isDoneBean;
			}
		}
	}
}
