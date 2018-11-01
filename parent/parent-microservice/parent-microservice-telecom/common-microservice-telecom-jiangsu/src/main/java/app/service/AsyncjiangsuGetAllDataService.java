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

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
public class AsyncjiangsuGetAllDataService implements ICrawler{

	@Autowired
	private TelecomjiangsuService inTelecomjiangsuService;

	@Autowired
	private TracerLog tracer;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		
		tracer.addTag("AsyncChongqing1GetAllDataService.crawler.getAllData", messageLogin.getTask_id());

		TaskMobile taskMobile = inTelecomjiangsuService.findtaskMobile(messageLogin.getTask_id());
		
		tracer.addTag("AsyncChongqing1GetAllDataService.crawler.getAllData", taskMobile.getTaskid());
		

		Map<String, Future<String>> listfuture = new HashMap<>();
		
		
		
		/**
		 * 亲情号信息，并没有找到所以直接修改状态201
		 */
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【亲情号信息】已采集完成");

		
//		try {
//			// 在用业务情况Two
//			Future<String> future = inTelecomjiangsuService.getBusinessTwo(taskMobile, messageLogin);
//			listfuture.put("getBusinessTwo", future);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("inTelecomjiangsuService.getBusiness---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
//		}
		
		
		try {
			// 在用业务情况
			Future<String> future = inTelecomjiangsuService.getBusiness(taskMobile, messageLogin);
			listfuture.put("getBusiness", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService.getBusiness---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		
		
		try {
			// 用户信息
			Future<String> future = inTelecomjiangsuService.getUserInfo(taskMobile, messageLogin);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService.getUserInfo---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		
		try {
			for (int i = 1; i < 7; i++) {
				String yearmonth = AsyncjiangsuGetAllDataService.getDateBefore("yyyy-MM", 0, -i, 0);
				tracer.addTag("inTelecomjiangsuService.getBill-----yearmonth:" + yearmonth, taskMobile.getTaskid());
				// 账单查询
				Future<String> future = inTelecomjiangsuService.getBillSum(taskMobile, messageLogin,yearmonth);
				listfuture.put(yearmonth+"getBillSum", future);
				// 账单明细
				Future<String> future1 = inTelecomjiangsuService.getBill(taskMobile, messageLogin, yearmonth);
				listfuture.put(yearmonth+"getBill", future1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService.getBill---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		
		try {
			for (int i = 0; i < 6; i++) {
				String yearmonth = getDateBefore("yyyy-MM",0, -i,0);
				String yearmonthPay = getDateBefore("yyyyMM",0, -i,0);
				tracer.addTag("inTelecomjiangsuService.getDecadeCZJF---yearmonth:"+yearmonth+"yearmonthPay:"+yearmonthPay,
						taskMobile.getTaskid());
				// 充值缴费
				Future<String> future = inTelecomjiangsuService.getPay(taskMobile, messageLogin, yearmonthPay);
				listfuture.put(yearmonth+"getPay", future);
				//余额变动明细
				Future<String> future1 = inTelecomjiangsuService.getBalanceRecord(taskMobile, messageLogin, yearmonth);
				listfuture.put(yearmonth+"getBalanceRecord", future1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService.getPay---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			String beginTime = getDateBefore("yyyy-MM-dd", 0, -6, 0);
			String endTime = getDateBefore("yyyy-MM-dd", 0, 0, 0);
			
			tracer.addTag("inTelecomjiangsuService"+taskMobile.getTaskid(),
					 "---beginTime:" + beginTime + "-----endTime:" + endTime);
			// 通话详单
			Future<String> future = inTelecomjiangsuService.getCallRecord(taskMobile, messageLogin,beginTime, endTime);
			listfuture.put(beginTime+"---"+endTime+"getCallRecord", future);
			// 短信详单
			Future<String> future1 = inTelecomjiangsuService.getMessage(taskMobile, messageLogin, beginTime, endTime);
			listfuture.put(beginTime+"---"+endTime+"getMessage", future1);

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
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
			tracer.addTag("inTelecomjiangsuService-listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return taskMobile;
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
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
