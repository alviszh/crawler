package app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
//import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;

import app.bean.RequestParam;
import app.bean.WebDataBankReport;
import app.bean.WebDataCreditcard;
import app.bean.WebDataDebitcard;
import app.commontracerlog.TracerLog;
import app.service.BankETLService;
import app.service.BankReportService;
import app.service.CreditBankETLService;

@RestController
@RequestMapping("/etl/bank")
public class BankETLController {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private BankETLService bankETLService;
	@Autowired
	private CreditBankETLService creditBankETLService;
	@Autowired
	private BankReportService bankReportService;

	@PostMapping(path = "/tasks/debitcard/detail")
	public WebDataDebitcard getAllData(@RequestBody RequestParam requestParam) {
		tracer.addTag("action.crawler.bank-debitcard.getAllData", requestParam.toString());
		return bankETLService.getAllData(requestParam);
	}
	@GetMapping(path = "/tasks/debitcard/detail")
	public WebDataDebitcard getData(String taskid, String loginname) {
		System.out.println("taskid-----" + taskid);
		System.out.println("loginname-----" + loginname);
		RequestParam rp = new RequestParam();
		tracer.addTag("action.crawler.bank-debitcard.getAllData", rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		WebDataDebitcard allData = bankETLService.getAllData(rp);
		return allData;
	}

	@GetMapping(path = "/tasks/report")
	public WebDataBankReport getDataReport(String taskid, String loginname) {
		RequestParam rp = new RequestParam();
		tracer.addTag("action.crawler.bank-debitcard.getAllData", rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		return bankReportService.getAllData(rp);
	}

	@PostMapping(path = "/tasks/creditcard/detail")
	public WebDataCreditcard getAllDataCredit(@RequestBody RequestParam requestParam) {
		tracer.addTag("action.crawler.bank-creditcard.getAllData", requestParam.toString());
		return creditBankETLService.getAllData(requestParam);
	}

	@GetMapping(path = "/tasks/creditcard/detail")
	public WebDataCreditcard getDataCredit(String taskid, String loginname) {
		RequestParam rp = new RequestParam();
		tracer.addTag("action.crawler.bank-creditcard.getAllData", rp.toString());
		rp.setTaskid(taskid);
		rp.setLoginName(loginname);
		return creditBankETLService.getAllData(rp);
	}

	@RequestMapping(value = "/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskBank> findAll(
			@org.springframework.web.bind.annotation.RequestParam(value = "currentPage") int currentPage,
			@org.springframework.web.bind.annotation.RequestParam(value = "pageSize") int pageSize,
			@org.springframework.web.bind.annotation.RequestParam(value = "owner") String owner,
			@org.springframework.web.bind.annotation.RequestParam(value = "environmentId") String environmentId,
			@org.springframework.web.bind.annotation.RequestParam(value = "beginTime") String beginTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "endTime") String endTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "taskid") String taskid,
			@org.springframework.web.bind.annotation.RequestParam(value = "loginName") String loginName,
			@org.springframework.web.bind.annotation.RequestParam(value = "userId") String userId
			) {

		System.out.println("currentPage-----"+currentPage);
		System.out.println("pageSize-----"+pageSize);
		System.out.println("应用-----"+owner);
		System.out.println("环境-----"+environmentId);
		System.out.println("开始时间-----"+beginTime);
		System.out.println("结束时间-----"+endTime);
		System.out.println("任务id-----"+taskid);
		System.out.println("登录账号-----"+loginName);
		System.out.println("用户id-----"+userId);
		
		// 根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("owner", owner);
		paramMap.put("environmentId", environmentId);
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("taskid", taskid);
		paramMap.put("loginName", loginName);
		paramMap.put("userId", userId);

		Page<TaskBank> tasksPage = bankETLService.getBankTaskByParams(paramMap,currentPage, pageSize);
		
		System.out.println("网银获取到的数据-----"+tasksPage.getContent().size());
		
		PageInfo<TaskBank> pageInfo = new PageInfo<TaskBank>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
}
