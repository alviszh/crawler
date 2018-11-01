package app.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;

@Component
public class AsyncTianshuiGetDataService implements ICrawler{

	@Autowired
	private HousingTianshuiFutureService housingTianshuiFutureService;

	@Autowired
	private HousingBasicService housingBasicService;
	
	@Autowired
	protected TaskHousingRepository taskHousingRepository;

	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());

		tracer.addTag("AsyncTianshuiGetDataService.crawler.getAllData", messageLoginForHousing.getTask_id());

		Map<String, Future<String>> listfuture = new HashMap<>();

		try {
			// 获取个人信息
			Future<String> futureUserInfo = housingTianshuiFutureService.getUserInfo(taskHousing);
			listfuture.put("getUserInfo", futureUserInfo);

			// 个人缴存账户
			Future<String> futureAccountInfo = housingTianshuiFutureService.getAccountInfo(taskHousing);
			listfuture.put("getAccountInfo", futureAccountInfo);

			for (int i = 0; i < 5; i++) {
				String data = getDateBefore("yyyy", -i, 0, 0);
				tracer.addTag(messageLoginForHousing.getTask_id() + "---getDateBefore", data);
				// 缴存记录查询
				Future<String> futurePayRecord = housingTianshuiFutureService.getPayRecord(taskHousing, data);
				listfuture.put("getPayRecord", futurePayRecord);

				// 帐户明细查询
				Future<String> futurePayDetailed = housingTianshuiFutureService.getPayDetailed(taskHousing, data);
				listfuture.put("getPayDetailed", futurePayDetailed);
			}

			try {
				while (true) {
					for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
						if (entry.getValue().isDone()) { // 判断是否执行完毕
							tracer.addTag(messageLoginForHousing.getTask_id() + entry.getKey() + "---get",
									entry.getValue().get());
							tracer.addTag(messageLoginForHousing.getTask_id() + entry.getKey() + "---isDone",
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
				tracer.addTag("listfuture--ERROR", messageLoginForHousing.getTask_id() + "---ERROR:" + e);
			}
			housingBasicService.updateTaskHousing(messageLoginForHousing.getTask_id());

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("AsyncTianshuiGetDataService--ERROR:", messageLoginForHousing.getTask_id() + "---ERROR:" + e);
		}
		return taskHousing;
	}

	/*
	 * @Des 获取时间
	 */
	public static String getDateBefore(String fmt, int yearCount, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, yearCount);
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}


	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
