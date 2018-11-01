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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.neimenggu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.neimenggu")
public class TelecomService implements ICrawler {

	@Autowired
	private TelecomCommonService telecomCommonService;

	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = null;
		// 基本信息-其他信息
		telecomCommonService.crawlerWdother(messageLogin);
		// 当月账单信息
		telecomCommonService.crawlerMonthBill(messageLogin);
		// 历史月账单信息
		SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
		Calendar calendar22 = Calendar.getInstance();
		calendar22.setTime(new Date());
		List<String> list22 = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list22.add(dateFormat22.format(calendar22.getTime()));
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list22.size(); i++) {
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
		for (int j = 0; j <= 5; j++) {
			list221.add(dateFormat221.format(calendar221.getTime()));
			calendar221.set(Calendar.MONTH, calendar221.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list221.size(); i++) {
			String monthdate = list221.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService.crawlerPaymsg(messageLogin, year + "");
		}
		// 语音信息
		SimpleDateFormat dateFormat2211 = new SimpleDateFormat("yyyyMM");
		Calendar calendar2211 = Calendar.getInstance();
		calendar2211.setTime(new Date());
		List<String> list2211 = new ArrayList<String>();
		for (int j = 0; j <= 6; j++) {
			list2211.add(dateFormat2211.format(calendar2211.getTime()));
			calendar2211.set(Calendar.MONTH, calendar2211.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list2211.size(); i++) {
			String monthdate = list2211.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService.crawlerCall(messageLogin, year + "");
		}
		// 短信信息
		SimpleDateFormat dateFormat22111 = new SimpleDateFormat("yyyyMM");
		Calendar calendar22111 = Calendar.getInstance();
		calendar22111.setTime(new Date());
		List<String> list22111 = new ArrayList<String>();
		for (int j = 0; j <= 6; j++) {
			list22111.add(dateFormat22111.format(calendar22111.getTime()));
			calendar22111.set(Calendar.MONTH, calendar22111.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list22111.size(); i++) {
			String monthdate = list22111.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			telecomCommonService.crawlerMessage(messageLogin, year + "");
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
