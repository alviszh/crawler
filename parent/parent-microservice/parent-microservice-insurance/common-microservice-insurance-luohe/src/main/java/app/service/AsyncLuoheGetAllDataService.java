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
public class AsyncLuoheGetAllDataService implements InsuranceCrawler{

	@Autowired
	private InsuranceLuoheService insuranceLuoheService;

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
		tracer.addTag("AsyncLuoheGetAllDataService.crawler.getAllData", parameter.getTaskId());

		tracer.addTag("parser.crawler.taskid", parameter.getTaskId());
		tracer.addTag("parser.crawler.auth", parameter.getUsername());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());

		try {
			Map<String, Future<String>> listfuture = new HashMap<>();

			// 基本信息
			Future<String> userInfo = insuranceLuoheService.getuserinfo(taskInsurance);
			listfuture.put("getUserInfo", userInfo);

			// 养老保险个人缴费流水
			Future<String> getPension = insuranceLuoheService.getPension(taskInsurance);
			listfuture.put("getPension", getPension);

			// 医疗保险个人缴费流水
			Future<String> getMedical = insuranceLuoheService.getMedical(taskInsurance);
			listfuture.put("getMedical", getMedical);

			// 工伤保险个人缴费流水
			Future<String> getInjury = insuranceLuoheService.getInjury(taskInsurance);
			listfuture.put("getInjury", getInjury);

			// 失业保险个人缴费流水
			Future<String> getUnemployment = insuranceLuoheService.getUnemployment(taskInsurance);
			listfuture.put("getUnemployment", getUnemployment);

			// 生育保险个人缴费流水
			Future<String> getBear = insuranceLuoheService.getBear(taskInsurance);
			listfuture.put("getBear", getBear);

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
			tracer.addTag("AsyncLuoheGetAllDataService---ERROR:",
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
