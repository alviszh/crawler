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
public class AsyncchongqingGetAllDataService implements ICrawler{

	@Autowired
	private TelecomchongqingService inTelecomChongqingService;

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

		TaskMobile taskMobile = inTelecomChongqingService.findtaskMobile(messageLogin.getTask_id());
		
		Map<String, Future<String>> listfuture = new HashMap<>();

		/**
		 * 亲情号信息，并没有找到所以直接修改状态201
		 */
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【亲情号信息】已采集完成");

		crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，【账户信息】已采集完成");

		// try {
		// /**
		// * 不需要爬取了，统一登陆后爬取
		// */
		// // 获取我的星级服务信息
		// // inTelecomChongqingService.getStarlevel(taskMobile);
		// } catch (Exception e) {
		// e.printStackTrace();
		// tracer.addTag("inTelecomChongqingService.getStarlevel---ERROR",
		// taskMobile.getTaskid() + "---ERROR:" + e);
		// }

		try {
			// 余额查询
			Future<String> future = inTelecomChongqingService.getBalance(taskMobile, messageLogin);
			listfuture.put("getBalance", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getBalance---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		
		/**
		 * 网站改版找不到实时话费了
		 */
//		try {
//			Thread.sleep(1000);
//			// 实时话费查询
//			Future<String> future = inTelecomChongqingService.getBalanceRealtime(taskMobile, messageLogin);
//			listfuture.put("getBalanceRealtime", future);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("inTelecomChongqingService.getBalanceRealtime---ERROR",
//					taskMobile.getTaskid() + "---ERROR:" + e);
//		}

		try {
			// 账单查询
			Future<String> future = inTelecomChongqingService.getBillAll(taskMobile, messageLogin);
			listfuture.put("getBillAll", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getBillAll---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 积分查询
			Future<String> future = inTelecomChongqingService.getIntegral(taskMobile, messageLogin);
			listfuture.put("getIntegral", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getIntegral---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 套餐使用情况
			Future<String> future = inTelecomChongqingService.getComboUseAll(taskMobile, messageLogin);
			listfuture.put("getComboUseAll", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getComboUse---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 在用业务情况-----套餐信息
			Future<String> future = inTelecomChongqingService.getBusinessT(taskMobile, messageLogin);
			listfuture.put("getBusinessT", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getBusinessT---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 在用业务----已订购的增值业务
			Future<String> future = inTelecomChongqingService.getBusinessZ(taskMobile, messageLogin);
			listfuture.put("getBusinessZ", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getBusinessZ---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 在用业务----已订购的基础功能
			Future<String> future = inTelecomChongqingService.getBusinessJ(taskMobile, messageLogin);
			listfuture.put("getBusinessJ", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getBusinessJ---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			String endtime = dateFormat.format(calendar.getTime());
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
			String starttime = dateFormat.format(calendar.getTime());
			tracer.addTag("inTelecomChongqingService.getDecadeCZJF",
					taskMobile.getTaskid() + "---starttime" + starttime + "-----endtime:" + endtime);
			// 充值缴费
			Future<String> future = inTelecomChongqingService.getPay(taskMobile, messageLogin, starttime, endtime);
			listfuture.put(starttime + "-" + endtime + "getPay", future);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getPay---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {

			// for (int i = 0; i < 7; i++) {
			for (int i = 6; i > -1; i--) {
				String yearmonth = getDateBefore("yyyy-MM", i);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(StrToDate(yearmonth));
				calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
				String beginTime = format.format(calendar.getTime());
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				String endTime = format.format(calendar.getTime());
				tracer.addTag("inTelecomChongqingService.getCallRecord-----" + yearmonth,
						taskMobile.getTaskid() + "---beginTime:" + beginTime + "-----endTime:" + endTime);
				// 通话详单
				inTelecomChongqingService.getCallRecord(taskMobile, messageLogin, yearmonth, beginTime, endTime);
			}

			for (int i = 6; i > -1; i--) {
				String yearmonth = getDateBefore("yyyy-MM", i);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(StrToDate(yearmonth));
				calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
				String beginTime = format.format(calendar.getTime());
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				String endTime = format.format(calendar.getTime());
				tracer.addTag("inTelecomChongqingService.getMessage-----" + yearmonth,
						taskMobile.getTaskid() + "---beginTime:" + beginTime + "-----endTime:" + endTime);
				// 短信详单
				inTelecomChongqingService.getMessage(taskMobile, messageLogin, yearmonth, beginTime, endTime);
			}
			/**
			 * 暂时不需要还是同步的费时间
			 */
			// for (int i = 6; i > -1; i--) {
			// String yearmonth = getDateBefore("yyyy-MM", i);
			// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// Calendar calendar = Calendar.getInstance();
			// calendar.setTime(StrToDate(yearmonth));
			// calendar.set(Calendar.DATE,
			// calendar.getActualMinimum(Calendar.DATE));
			// String beginTime = format.format(calendar.getTime());
			// calendar.set(Calendar.DATE,
			// calendar.getActualMaximum(Calendar.DATE));
			// String endTime = format.format(calendar.getTime());
			// tracer.addTag("inTelecomChongqingService.getFlow-----" +
			// yearmonth,
			// taskMobile.getTaskid() + "---beginTime:" + beginTime +
			// "-----endTime:" + endTime);
			// // 上网详单
			// inTelecomChongqingService.getFlow(taskMobile, messageLogin,
			// yearmonth, beginTime, endTime);
			// }
			for (int i = 6; i > -1; i--) {
				String yearmonth = getDateBefore("yyyy-MM", i);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(StrToDate(yearmonth));
				calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
				String beginTime = format.format(calendar.getTime());
				calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
				String endTime = format.format(calendar.getTime());
				tracer.addTag("inTelecomChongqingService.getIncrementBusiness-----" + yearmonth,
						taskMobile.getTaskid() + "---beginTime:" + beginTime + "-----endTime:" + endTime);
				// 增值详单
				inTelecomChongqingService.getIncrementBusiness(taskMobile, messageLogin, yearmonth, beginTime, endTime);

			}
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
			taskMobile = crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomChongqingService.getDecade3---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return taskMobile;

	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}


	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
