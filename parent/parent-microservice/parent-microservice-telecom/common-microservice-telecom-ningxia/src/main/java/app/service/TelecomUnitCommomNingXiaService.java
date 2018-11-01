package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.ningxia")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.ningxia")
public class TelecomUnitCommomNingXiaService implements ICrawler{
	@Autowired
	private TelecomCommonNingXiaService telecomCommonNingXiaService;
	@Autowired
	private TelecomCallMsgService telecomCallMsgService;
	public static final Logger log = LoggerFactory.getLogger(TelecomUnitCommomNingXiaService.class);

	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = telecomCommonNingXiaService.findtaskMobile(messageLogin.getTask_id().trim());

		// 对应的实体 TelecomNingxiaUserInfo
		// 爬取采集用户基本信息业务 我的资料组
		telecomCommonNingXiaService.crawlerWdzl(messageLogin);
		// 爬取采集用户基本信息业务 我的余额组
		telecomCommonNingXiaService.crawlerRemainMoney(messageLogin);

		// 对应的实体 TelecomNingxiaBusinessMsg
		// 爬取采集业务信息业务 增值业务组
		telecomCommonNingXiaService.crawlerBusinessMsg(messageLogin);

		// 对应的实体 TelecomNingxiaPayMsg
		// 爬取采集缴费信息业务 充值缴费记录查询
		telecomCommonNingXiaService.crawlerPaymsg(messageLogin);

		// 对应的实体 TelecomNingxiaMonthBillHistory
		// 爬取采集月账单记录信息业务
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
			telecomCommonNingXiaService.crawlerMonthBill(messageLogin, year);
		}

		// 通话记录信息的爬取和采集
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		List<String> list = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list.add(dateFormat.format(calendar.getTime()));
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		}
		System.out.println(list.toString());

		for (int k = 0; k < list.size(); k++) {
			String firstMonthdate = telecomCommonNingXiaService.getFirstMonthdate(k);
			String lastMonthdate = telecomCommonNingXiaService.getLastMonthdate(k);
			telecomCallMsgService.crawlerCallMessageHistory(messageLogin, firstMonthdate, lastMonthdate);
		}
		// 短信记录信息的爬取和采集
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMM");
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		List<String> list1 = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list1.add(dateFormat1.format(calendar1.getTime()));
			calendar1.set(Calendar.MONTH, calendar1.get(Calendar.MONTH) - 1);
		}
		System.out.println(list1.toString());
		
		for (int k = 0; k < list1.size(); k++) {
			String firstMonthdate = telecomCommonNingXiaService.getFirstMonthdate(k);
			String lastMonthdate = telecomCommonNingXiaService.getLastMonthdate(k);
			telecomCallMsgService.crawlerSmsMessageHistory(messageLogin, firstMonthdate, lastMonthdate);
		}
		return taskMobile;
	}

	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}