package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class AsyncYichangGetAllDataService implements InsuranceCrawler {

	@Autowired
	private InsuranceYichangService insuranceYichangService;

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {

		tracer.addTag("parser.crawler.taskid", parameter.getTaskId());
		tracer.addTag("parser.crawler.auth", parameter.getUsername());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());

		try {
			Map<String, Future<String>> listfuture = new HashMap<>();

			// 公积金用户信息 201
			// 公积金明细 202
			// 社保用户信息 101
			// 社保流水 102
			
			// 社保用户信息 101
			Future<String> insuranceUserInfo = insuranceYichangService.getInsuranceUserinfo(taskInsurance,"101");
			listfuture.put("getinsuranceUserInfo", insuranceUserInfo);

			// 社保流水 102
			Future<String> insuranceGeneral = insuranceYichangService.getInsuranceGeneral(taskInsurance,"102");
			listfuture.put("getInsuranceGeneral", insuranceGeneral);

			try {
				while (true) {
					for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
						if (entry.getValue().isDone()) { // 判断是否执行完毕
							tracer.addTag(parameter.getTaskId() + entry.getKey() + "---get", entry.getValue().get());
							tracer.addTag(parameter.getTaskId() + entry.getKey() + "---isDone", entry.getValue().get());
							listfuture.remove(entry.getKey());
							break;
						}
					}
					if (listfuture.size() == 0) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("listfuture--ERROR", parameter.getTaskId() + "---ERROR:" + e);
			}
			insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("AsyncYichangGetAllDataService---ERROR:",
					parameter.getTaskId() + "---ERROR:" + e);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
