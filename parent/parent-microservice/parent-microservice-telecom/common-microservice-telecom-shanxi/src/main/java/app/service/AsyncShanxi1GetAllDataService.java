package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawler;

@Component
public class AsyncShanxi1GetAllDataService implements ICrawler{

	@Autowired
	private TelecomShanxi1Service inTelecomShanxi1Service;

	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 爬取总方法
	 * @param insuranceRequestParameters
	 */
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		
		tracer.addTag("AsyncShanxi1GetAllDataService.crawler.getAllData", messageLogin.getTask_id());

		TaskMobile taskMobile = inTelecomShanxi1Service.findtaskMobile(messageLogin.getTask_id());
		
		tracer.addTag("AsyncShanxi1GetAllDataService.crawler.getAllData", taskMobile.getTaskid());

		try {
			// 获取个人信息
			inTelecomShanxi1Service.getUserInfo(taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getUserInfo---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			String endDate = dateFormat.format(calendar.getTime());
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
			String startDate = dateFormat.format(calendar.getTime());
			tracer.addTag("TelecomShanxi1Service.getDecade",
					taskMobile.getTaskid() + "---startDate:" + startDate + "------endDate:" + endDate);
			// 获取缴费信息
			inTelecomShanxi1Service.getPayInfo(taskMobile, startDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getUserInfo---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 获取我的产品信息
			inTelecomShanxi1Service.getProduct(taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getUserInfo---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			// 获取我的账户信息
			inTelecomShanxi1Service.getAccount(taskMobile, messageLogin);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getAccount---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String endtime = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6);
		String starttime = dateFormat.format(calendar.getTime());
		try {
			// 获取我的短信详单
			inTelecomShanxi1Service.getTelecomShanxi1Message(taskMobile, messageLogin, starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Message---ERROR",
					taskMobile.getTaskid() + "---ERROR:" + e);
		}

		/**
		 * 注释----不需要
		 */
//		try {
//			// 获取我的流量详单
//			inTelecomShanxi1Service.getTelecomShanxi1Flow(taskMobile, messageLogin, starttime, endtime);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Flow---ERROR",
//					taskMobile.getTaskid() + "---ERROR:" + e);
//		}

		try {
			// 获取我的通话记录详单
			inTelecomShanxi1Service.getTelecomShanxi1CallRecord(taskMobile, messageLogin, starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getTelecomShanxi1Record---ERROR",
					taskMobile.getTaskid() + "---ERROR:" + e);
		}

		try {
			inTelecomShanxi1Service.getBillAll(taskMobile);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomShanxi1Service.getBillAll---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}

//		try {
//			// 获取我的星级服务信息
//			inTelecomShanxi1Service.getStarlevel(taskMobile);
//		} catch (Exception e) {
//			e.printStackTrace();
//			tracer.addTag("TelecomShanxi1Service.getStarlevel---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
//		}
		return taskMobile;

	}

	/**
	 * 获取六个月内的年月字符串
	 * 
	 * @return
	 */
	public static List<String> getDecade() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= 6; i++) {
			list.add(dateFormat.format(calendar.getTime()));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		}
		return list;
	}


	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
