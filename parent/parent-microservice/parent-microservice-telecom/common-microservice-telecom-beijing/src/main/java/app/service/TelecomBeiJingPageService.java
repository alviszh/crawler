package app.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.beijing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.beijing")
public class TelecomBeiJingPageService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TelecomUnitBeiJingService telecomUnitBeiJingService;
	
	@Autowired
	private TracerLog tracerLog;
	

	public String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) {

		telecomUnitBeiJingService.getUserInfo(messageLogin, taskMobile, 0);

		return null;
	}

	@Async
	public Future<String> getcallbypage(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile, int i,String smsCode) {
		int pagenums = 1;
		List<Future<String>> listfuture = new ArrayList<Future<String>>();

		for (int pagenum = 0; pagenum <= pagenums ; pagenum++) {
			tracerLog.output("循环页码"+pagenums,"第"+pagenum+"页");
			
			tracerLog.output("循环页码月份"+i,"第"+pagenum+"页");


			LocalDate today = LocalDate.now();
			// 本月的第一天
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
			LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
			if (i == 0) {
				enddate = today;
			}
			String monthint = stardate.getMonthValue() + "";
			if (monthint.length() < 2) {
				monthint = "0" + monthint;
			}
			String month = stardate.getYear() + "年" + monthint + "月";

			if (pagenum != 1) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (pagenum == 0) {
					String pagenumsString = telecomUnitBeiJingService.getCallThrem(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth()+"", enddate.getDayOfMonth()+"", month, pagenum);
					if (pagenumsString != null) {
						if (Integer.parseInt(pagenumsString) != 0) {
							pagenums = Integer.parseInt(pagenumsString);
						}

					}
				} else {
					Future<String> future = telecomUnitBeiJingService.getCallThremByAsync(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth()+"", enddate.getDayOfMonth()+"", month, pagenum,smsCode);
				
					listfuture.add(future);
				
				}

			}

			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<String> future : listfuture) {

					if (future.isDone()) { // 判断是否执行完毕
						listfuture.remove(future);
						break;
					}
					
				}
				if (listfuture.size() <= 0) {
					istrue = false;
				}
			}
		}
		return new AsyncResult<String>("error");	
	}


	public void getSMSThrembypage(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		int pagenums = 2;

		for (int pagenum = 0; pagenum < pagenums + 1; pagenum++) {
			if (pagenum != 1) {

				String pagenumsString = telecomUnitBeiJingService.getSMSThrem(webClientCookies, messageLogin,
						taskMobile, i, pagenum);
				if (pagenumsString != null) {
					if (Integer.parseInt(pagenumsString) != 0) {
						pagenums = Integer.parseInt(pagenumsString);
					}

				}
			}

		}
	}

	

	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
}