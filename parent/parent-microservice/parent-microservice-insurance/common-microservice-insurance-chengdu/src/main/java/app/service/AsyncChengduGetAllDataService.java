package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class AsyncChengduGetAllDataService implements InsuranceCrawler {

	@Autowired
	private InsuranceChengduService insuranceChengduService;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceService insuranceService;

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("AsyncChengduGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());

		Map<String, Future<String>> listfuture = new HashMap<>();

		try {
			// 获取社保总信息
			Future<String> future = insuranceChengduService.getSummary(insuranceRequestParameters);
			listfuture.put("getSummary", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 大病补充医疗保险缴费明细
			Future<String> future = insuranceChengduService.getSeriousIllnessMedical(insuranceRequestParameters);
			listfuture.put("getSeriousIllnessMedical", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 医疗账户消费明细
			Future<String> future = insuranceChengduService.getMedicalconsumption(insuranceRequestParameters);
			listfuture.put("getMedicalconsumption", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 养老保险个人账户明细信息
			Future<String> future = insuranceChengduService.getPensionAccount(insuranceRequestParameters);
			listfuture.put("getPensionAccount", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取个人信息
			Future<String> future = insuranceChengduService.getUserInfo(insuranceRequestParameters);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取养老缴费明细
			Future<String> future = insuranceChengduService.getPension(insuranceRequestParameters);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取生育保险缴费明细
			Future<String> future = insuranceChengduService.getBear(insuranceRequestParameters);
			listfuture.put("getBear", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取工伤保险缴费明细
			Future<String> future = insuranceChengduService.getInjury(insuranceRequestParameters);
			listfuture.put("getInjury", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取医疗保险缴费明细
			Future<String> future = insuranceChengduService.getMedical(insuranceRequestParameters);
			listfuture.put("getMedical", future);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// 获取失业保险缴费明细
			Future<String> future = insuranceChengduService.getUnemployment(insuranceRequestParameters);
			listfuture.put("getUnemployment", future);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(insuranceRequestParameters.getTaskId() + entry.getKey() + "---get",
								entry.getValue().get());
						tracer.addTag(insuranceRequestParameters.getTaskId() + entry.getKey() + "---isDone",
								entry.getValue().get());
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
			tracer.addTag("listfuture--ERROR", insuranceRequestParameters.getTaskId() + "---ERROR:" + e);
		}

		TaskInsurance taskInsurance = insuranceService
				.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());

		return taskInsurance;

	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
