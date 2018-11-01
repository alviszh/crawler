package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceCrawler;

@Component
public class AsyncDongguanGetAllDataService implements InsuranceCrawler{

	@Autowired
	private InsuranceDongguanService insuranceDongguanService;
	
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
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("AsyncDongguanGetAllDataService.crawler.getAllData", insuranceRequestParameters.getTaskId());

		tracer.addTag("parser.crawler.taskid", insuranceRequestParameters.getTaskId());
		tracer.addTag("parser.crawler.auth", insuranceRequestParameters.getUsername());
		
		Map<String, Future<String>> listfuture = new HashMap<>();
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			
			// 获取Menv
			String htmlUrl = insuranceDongguanService.getMenu(insuranceRequestParameters,taskInsurance);
			if (null != htmlUrl && !"".equals(htmlUrl)) {
				try {
					// 获取个人信息
					Future<String> future = insuranceDongguanService.getUserInfo(insuranceRequestParameters, htmlUrl,
							taskInsurance);
					listfuture.put("getUserInfo", future);
					
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("AsyncDongguanGetAllDataService.insuranceDongguanService.getUserInfo.ERROR",
							insuranceRequestParameters.getTaskId() + e);
				}

				try {
					// 获取东莞社保参保记录
					Future<String> future = insuranceDongguanService.getInsuranceRecord(insuranceRequestParameters,
							htmlUrl, taskInsurance);
					listfuture.put("getInsuranceRecord", future);
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("AsyncDongguanGetAllDataService.insuranceDongguanService.getInsuranceRecord.ERROR",
							insuranceRequestParameters.getTaskId() + e);
				}
				try {
					List<String> decade = getDecade();
					tracer.addTag("parser.crawler.Insurance", insuranceRequestParameters.getTaskId());
					// 获取东莞社保月缴费情况查询
					for (String string : decade) {
						Future<String> future = insuranceDongguanService.getGeneral(insuranceRequestParameters, htmlUrl,
								string, taskInsurance);
						listfuture.put("getGeneral" + string, future);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					tracer.addTag("AsyncDongguanGetAllDataService.insuranceDongguanService.getGeneral.ERROR",
							insuranceRequestParameters.getTaskId() + e);
				}
				try {
					while (true) {
						for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
							if (entry.getValue().isDone()) { // 判断是否执行完毕
								tracer.addTag(insuranceRequestParameters.getTaskId() + entry.getKey() + "---get", entry.getValue().get());
								tracer.addTag(insuranceRequestParameters.getTaskId() + entry.getKey() + "---isDone", entry.getValue().get());
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
				insuranceDongguanService.update(insuranceRequestParameters.getTaskId(),200);
			}else{
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, taskInsurance);
				insuranceDongguanService.update(insuranceRequestParameters.getTaskId(),500);
			}
			insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("AsyncDongguanGetAllDataService.insuranceDongguanService.getMenu---ERROR:",
					insuranceRequestParameters.getTaskId() + "---ERROR:" + e);
		}
		return null;
	}

	/**
	 * 获取十年内的年月字符串
	 * 
	 * @return
	 */
	public List<String> getDecade() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 120; i++) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
			list.add(dateFormat.format(calendar.getTime()));
		}
		return list;
	}

	
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
