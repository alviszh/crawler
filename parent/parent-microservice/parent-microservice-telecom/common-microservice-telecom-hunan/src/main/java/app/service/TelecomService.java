package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.aop.ICrawler;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hunan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hunan")
public class TelecomService implements ICrawler{

	@Autowired
	private TelecomCommon telecomCommonService;

	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = null;
		// 短信
		SimpleDateFormat dateFormatmsg = new SimpleDateFormat("yyyy-MM");
		Calendar calendarMsg = Calendar.getInstance();
		calendarMsg.setTime(new Date());
		List<String> listmsg = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			listmsg.add(dateFormatmsg.format(calendarMsg.getTime()));
			calendarMsg.set(Calendar.MONTH, calendarMsg.get(Calendar.MONTH) - 1);
		}
		System.out.println(listmsg.toString());
		for (int k = 0; k < listmsg.size(); k++) {
			String lastMonthdate1 = telecomCommonService.getLastMonthdate(k);
			String[] splitlast1 = lastMonthdate1.split("-");
			System.out.println(listmsg.get(k));
			System.out.println(splitlast1[1]);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 短信信息
			telecomCommonService.crawlerMessage(messageLogin, listmsg.get(k), splitlast1[1]);
		}

		// 语音
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list.add(dateFormat.format(calendar.getTime()));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		}
		System.out.println(list.toString());

		for (int k = 0; k < list.size(); k++) {
			String lastMonthdate = telecomCommonService.getLastMonthdate(k);
			String[] splitlast = lastMonthdate.split("-");
			System.out.println(list.get(k));
			System.out.println(splitlast[1]);
			// 语音信息
			telecomCommonService.crawlerCall(messageLogin, list.get(k), splitlast[1]);

		}
		
		// 基本信息-我的账户状态
		telecomCommonService.crawlerWdstatus(messageLogin);
		
		// 历史月账单信息
		SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
		Calendar calendar22 = Calendar.getInstance();
		calendar22.setTime(new Date());
		List<String> list22 = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list22.add(dateFormat22.format(calendar22.getTime()));
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH) - 1);
		}
		for (int i = 1; i < list22.size(); i++) {
			String monthdate = list22.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService.crawlerMonthBillHistory(messageLogin, year);
		}

		
		// 缴费信息
		SimpleDateFormat dateFormat221 = new SimpleDateFormat("yyyyMM");
		Calendar calendar221 = Calendar.getInstance();
		calendar221.setTime(new Date());
		List<String> list221 = new ArrayList<String>();
		for (int j = 0; j <= 4; j++) {
			list221.add(dateFormat221.format(calendar221.getTime()));
			calendar221.set(Calendar.MONTH, calendar221.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list221.size(); i++) {
			String monthdate = list221.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService.crawlerPaymsg(messageLogin, year, i+1);
		}

		// 业务信息-程控业务信息-增值业务信息-套餐业务信息
		telecomCommonService.crawlerWdbuss(messageLogin);
		
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
