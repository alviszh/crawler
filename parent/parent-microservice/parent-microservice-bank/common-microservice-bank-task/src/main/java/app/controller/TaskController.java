package app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;
import com.crawler.bank.json.BankUserBean;
import com.microservice.dao.entity.crawler.bank.basic.BankCode;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.commontracerlog.TracerLog;
import app.service.BankTaskService;

/**
 * @author zz
 *
 */
@RestController
@RequestMapping("/bank")
public class TaskController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private BankTaskService bankTaskService;

	@PostMapping(path = "/check")
	public TaskBank createTask(@RequestBody BankUserBean bankUserBean) {

		tracer.addTag("bank.crawler.check", bankUserBean.toString());

		return bankTaskService.createTask(bankUserBean);

	}

	@GetMapping(path = "/tasks/{taskid}/status")
	public TaskBank taskStatus(@PathVariable String taskid) {

		TaskBank taskBank = bankTaskService.getTaskBank(taskid);
		tracer.addTag("bank.crawler.status", "taskid:" + taskid);
		return taskBank;

	}

	@RequestMapping(value = "/tasks/getPages", method = RequestMethod.POST)
	public @ResponseBody PageInfo<TaskBank> getTaskBankPages(@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize,
			@RequestParam(value = "taskid", required = false) String taskid) {

		System.out.println("currentPage-----" + currentPage);
		System.out.println("pageSize-----" + pageSize);
		System.out.println("taskid-----" + taskid);
		System.out.println("bank-task获取数据的接口进来了！");

		// 根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("taskid", taskid);

		Page<TaskBank> tasksPage = bankTaskService.getBankTaskByParams(paramMap, currentPage, pageSize);
		System.out.println("******getTaskPages:" + tasksPage);

		PageInfo<TaskBank> pageInfo = new PageInfo<TaskBank>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}

	/**
	 * 根据创建时间统计网银的调用量（线性图表）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tasks/lineData", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List lineData() {
		System.out.println("Task服务中---线性图进来了！");
		List result = bankTaskService.getMobileTaskStatistics();
		return result;
	}

	/**
	 * 统计每个网银的调用量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/tasks/pieData", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List pieData() {
		System.out.println("Task服务中---饼图进来了！");
		List result = bankTaskService.getGroupByCarrier();
		return result;
	}

	/**
	 * @author zz
	 * @Des 获取所有银行及开发状态
	 * @return
	 */
	@GetMapping(path = "/getBank")
	public List<BankCode> getBank() {

		tracer.addTag("bank.crawler.getBank", "start");

		return bankTaskService.getBank();

	}
}
