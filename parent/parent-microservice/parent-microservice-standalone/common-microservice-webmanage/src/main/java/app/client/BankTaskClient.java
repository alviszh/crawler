package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

//@FeignClient("bankTask")
@FeignClient(name = "BANK-TASK", url = "http://10.167.202.222:8000")
public interface BankTaskClient {

	/**
	 * 根据taskid获取task状态
	 * 
	 * @param taskid
	 * @return
	 */
	@GetMapping(path = "/bank/tasks/{taskid}/status")
	public TaskBank getTaskByTaskid(@PathVariable(name = "taskid") String taskid);

	@PostMapping(value = "/bank/tasks/getPages")
	public PageInfo<TaskBank> getTaskBankPages(@RequestParam(value = "currentPage") int currentPage,
			@RequestParam(value = "pageSize") int pageSize, @RequestParam(value = "taskid") String taskid);

	/**
	 * 获取加载图表的数据（线性）
	 * 
	 * @return
	 */
	@GetMapping(value = "/bank/tasks/lineData")
	public @ResponseBody List lineData();

	/**
	 * 统计每个运营商的调用量（饼图）
	 * 
	 * @return
	 */
	@GetMapping(value = "/bank/tasks/pieData")
	public @ResponseBody List pieData();
}