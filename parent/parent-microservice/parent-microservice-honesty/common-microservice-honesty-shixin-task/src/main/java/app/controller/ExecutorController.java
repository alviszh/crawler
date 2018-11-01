package app.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.dao.entity.crawler.executor.ExecutorCounter;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.repository.crawler.executor.ExecutorCounterRepository;
import com.microservice.dao.repository.crawler.honesty.shixin.HonestyTaskRepository;

import app.bean.HonestyJsonBean;
import app.bean.HonestyShiXinBean;
import app.bean.Honestybean;
import app.bean.IsDoneBean;
import app.commontracerlog.TracerLog;
import app.service.ExecutorCodeService;
import app.service.ExecutorService;
import app.service.ExecutorSqlService;
import app.service.create.ExecutorCreateService;

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
@RequestMapping("/api-service/honesty/shixin")
public class ExecutorController {

	@Value("${webdriver.proxy}")
	int proxy;

	@Value("${webdriver.iscrawler:1}")
	boolean iscrawler;

	@Autowired
	private ExecutorCreateService executorCreateService;

	@Autowired
	private HonestyTaskRepository honestyTaskRepository;

	@Autowired
	private ExecutorCounterRepository executorCounterRepository;

	@Autowired
	private ExecutorCodeService executorCodeService;

	@Autowired
	private ExecutorService executorService;

	@Autowired
	private ExecutorSqlService executorSqlService;

	private static Queue<HonestyTask> queue = new ConcurrentLinkedQueue<HonestyTask>();

	private int shixinid = 0;

	private String captchaId = null;

	@Autowired
	private TracerLog tracerLog;

	@Scheduled(cron = "0/1 * * * * ?") // 每10分钟执行一次
	public String findSearch() {

		if (!iscrawler) {
			tracerLog.System("-----------不开始循环抓取------------", "=======================");
			return null;

		}

		tracerLog.System("-----------系统定时开始------------", "=======================");

		if (shixinid == 0) {
			List<ExecutorCounter> bean = executorCounterRepository.findAll();
			if (bean != null && bean.size() > 0) {
				shixinid = bean.get(0).getShixinId();
			}

		}

		try {
			if (captchaId == null) {
				captchaId = executorCodeService.getcaptchaId(null);

			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			tracerLog.System("findSearch captchaId", captchaId);
			shixinid = executorService.crawler(captchaId, shixinid, null);

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
		List<ExecutorCounter> bean = executorCounterRepository.findAll();
		ExecutorCounter executorCounter = bean.get(0);
		executorCounter.setShixinId(shixinid);
		executorCounterRepository.save(executorCounter);
		return null;
	}

	@RequestMapping(value = "/gettask", method = RequestMethod.POST)
	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.SERIALIZABLE)
	public List<HonestyTask> getTask() {
		System.out.println("======进入serchtask 查询====");

		if (queue.size() <= 0) {
			return new ArrayList<>();
		}
		tracerLog.output("queue", queue.size() + "");
		List<HonestyTask> listreturn = new ArrayList<>();
		HonestyTask honestyTask = queue.poll();
		tracerLog.output("queue 分发出的数据数据", honestyTask.toString());

		listreturn.add(honestyTask);
		return listreturn;
	}

	@PostMapping(path = "/task")
	// @RequestMapping(value = "/task", method = RequestMethod.POST)
	public IsDoneBean creatTask(@RequestBody HonestyJsonBean honestyJsonBean) {
		tracerLog.output("taskid", honestyJsonBean.getTaskid());
		tracerLog.output("HonestyJsonBean", honestyJsonBean.toString());
		if (null == honestyJsonBean.getTaskid()) {
			tracerLog.output("taskid is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		}
		if (honestyJsonBean.getKeys().size() < 0) {
			tracerLog.output("keys is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("keys is null !");
		}

		return executorCreateService.createTaskList(honestyJsonBean);
	}

	@Scheduled(cron = "0/2 * * * * ?")
	public void setQueue() {
		// System.out.println("queue===" +queue.size());
		if (queue.size() < 20) {
			queue = executorCreateService.getHonestyTask(queue);
		}
		Timestamp time = Timestamp.valueOf(LocalDateTime.now().plusMinutes(-1));

		List<HonestyTask> list = honestyTaskRepository.findTopNumByPhaseAndUpdatetime("1", time);
		List<HonestyTask> list2 = new ArrayList<>();
		for (HonestyTask HonestyTask : list) {
			HonestyTask.setPhase("404");
			System.out.println(HonestyTask.toString());

			list2.add(HonestyTask);
		}

		honestyTaskRepository.saveAll(list2);
	}

	@GetMapping(path = "/statue/taskid")
	public IsDoneBean isdone(@RequestParam(value = "taskid") String taskid) {

		tracerLog.output("taskid", taskid);
		List<HonestyTask> list = honestyTaskRepository.findByTaskid(taskid);
		int totalnum = list.size();
		int finishednum = 0;
		int crawleringnum = 0;
		int errornum = 0;

		IsDoneBean isDoneBean = new IsDoneBean();
		isDoneBean.setTaskid(taskid);
		for (HonestyTask honestyTask : list) {
			if (honestyTask.getPhase().trim() != "0") {
				if (honestyTask.getPhase().trim().indexOf("1") != -1) {
					crawleringnum++;
				}

				if (honestyTask.getPhase().trim().indexOf("200") != -1
						|| honestyTask.getPhase().trim().indexOf("404") != -1
						|| honestyTask.getPhase().trim().indexOf("2") != -1) {
					finishednum++;
					if (honestyTask.getPhase().indexOf("404") != -1) {
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

	@PostMapping(path = "/search")
	public HonestyJsonBean search(@RequestBody HonestyJsonBean honestyJsonBean) {
		tracerLog.output("taskid", honestyJsonBean.getTaskid());
		tracerLog.output("HonestyJsonBean", honestyJsonBean.toString());
		List<Honestybean> keys = honestyJsonBean.getKeys();

		List<Future<HonestyShiXinBean>> future_list = new ArrayList<>();
		for (Honestybean honestybean : keys) {
			honestybean.setTaskid(honestyJsonBean.getTaskid());
			tracerLog.output("honestybean", honestybean.toString());

			if (honestybean.getPagesize() == null) {
				honestybean.setPagesize(honestyJsonBean.getPagesize());
			}

			if (honestybean.getPagenum() == null) {
				honestybean.setPagenum(honestyJsonBean.getPagenum());
			}

			Future<HonestyShiXinBean> future = executorSqlService.findShiXinBeanForPage(honestybean);
			future_list.add(future);

		}

		List<HonestyShiXinBean> result = new ArrayList<>();
		boolean isdone = true;
		tracerLog.System("future_list start", future_list.size() + "");
		while (isdone) {
			for (Future<HonestyShiXinBean> future : future_list) {

				if (future.isDone()) { // 判断是否执行完毕
					future_list.remove(future);

					try {
						HonestyShiXinBean honestyShiXinBean = future.get();
						result.add(honestyShiXinBean);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tracerLog.System("future_list", future_list.size() + "");

					break;
				}

			}
			if (future_list.size() <= 0) {
				isdone = false;
			}

		}
		honestyJsonBean.setResult(result);
		return honestyJsonBean;
	}
}
