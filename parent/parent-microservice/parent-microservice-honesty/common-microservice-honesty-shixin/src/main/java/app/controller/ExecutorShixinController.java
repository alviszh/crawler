package app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.honesty.bean.HonestyTaskBean;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.entity.crawler.search.SearchTask;
import com.microservice.dao.repository.crawler.honesty.shixin.HonestyTaskRepository;

import app.client.honesty.HonestyTaskClient;
import app.commontracerlog.TracerLog;
import app.service.executor.find.ExecutorExecutorService;
import app.service.shixin.find.ExecutorShiXinService;

/**
 * 
 * 项目名称：common-microservice-executor 类名称：ExecutorController 类描述：
 * 创建人：Administrator 创建时间：2018年7月12日 下午3:58:42
 * 
 * @version
 */
@RestController
@Configuration
@EnableScheduling
@RequestMapping("/honesty")
public class ExecutorShixinController {

	@Autowired
	private HonestyTaskClient honestyTaskClient;

	 @Autowired
	 private HonestyTaskRepository honestyTaskRepository;

	@Autowired
	private ExecutorShiXinService executorShiXinService;
	
	@Autowired
	private ExecutorExecutorService executorExecutorService;
	

	@Autowired
	private TracerLog tracerLog;

	 @Scheduled(cron = "${jobs.cron}")
	//// @Scheduled(fixedDelay=50000) //上一次执行完毕时间点后2秒再次执行；
//	@PostMapping(path = "/shixin/crawler")
	public SearchTask crawlerClient() {
		long startTime = System.currentTimeMillis();
		tracerLog.System("-----------系统定时开始------------", "" + startTime);

		 List<HonestyTaskBean> list = honestyTaskClient.getTask();

//		List<HonestyTask> list = honestyTaskRepository.findByTaskid("0001");

		if (list.size() <= 0) {
			long endTime = System.currentTimeMillis();
			tracerLog.System("未发现未抓取程序        当前共计程序耗时ms：", (endTime - startTime) + "ms");
			tracerLog.System("未发现未抓取程序        当前共计程序耗时s：", (endTime - startTime) / 1000 + "s");
			return null;
		}
		List<Future<String>> future_list = new ArrayList<>();
		long s2 = System.currentTimeMillis();
		tracerLog.System("获取列表数量:" + list.size(), (s2 - startTime) + "ms");

		for (HonestyTaskBean honestyTaskBean : list) {
			tracerLog.addTag("taskid", honestyTaskBean.getId() + "");
	

			// tracerLog.System("searchTask"+";"+honestyTask.getId()+" 耗时："+(s4
			// - s3) + "ms", honestyTask.toString());
			Future<String> future = executorShiXinService.clawler(honestyTaskBean);
			Future<String> future2 = executorExecutorService.clawler(honestyTaskBean);
			future_list.add(future);
			future_list.add(future2);
		}

		boolean isdone = true;
		tracerLog.System("future_list start", future_list.size() + "");
		long startTime222 = System.currentTimeMillis();
		while (isdone) {
			for (Future<String> future : future_list) {

				if (future.isDone()) { // 判断是否执行完毕
					future_list.remove(future);
					tracerLog.System("future_list isdone", future_list.size() + "");

					break;
				}

			}
			if (future_list.size() <= 0) {
				isdone = false;
			}

		}

		long endTime = System.currentTimeMillis();
		
		for (HonestyTaskBean honestyTaskBean : list) {
			HonestyTask honestyTask = honestyTaskRepository.findTop1ById(honestyTaskBean.getId());
			honestyTask.setPhase("2");
			honestyTaskRepository.save(honestyTask);
		}
		
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时ms：", (endTime - startTime) + "ms");
		tracerLog.System("future_list.size()---" + future_list.size() + "当前共计程序耗时s：", (endTime - startTime222) + "ms");
		return null;
	}

}
