package app.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.service.crawler.telecom.htmlunit.LognAndGetBeijingService;

@Component
public class TelecomHtmlService {
	
	@Autowired
	private LognAndGetBeijingService lognAndGetBeijingService;
	
	// 获取北京用户基本信息
	public  String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		return lognAndGetBeijingService.getUserInfo(messageLogin, taskMobile);
	}

	// 获取北京用户往月账单
	public  String getphoneBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile) {
		return lognAndGetBeijingService.getphoneBill( webClient,messageLogin, taskMobile);
	}

	// 获取北京用户缴费信息
	public  String getpayResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, int i, int k) {

		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		// 本月的最后一天

		String html = lognAndGetBeijingService.getpayResult( webClient,messageLogin, taskMobile, stardate.toString(), enddate.toString());
		if (html.indexOf("尊敬的客户，出了一点点问题，请您稍候再试或立即反馈我们处理，给您带来不便敬请谅解") != -1) {
			k++;
			if (k < 3) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return html = getpayResult( webClient,messageLogin, taskMobile, i, k);
			} else {
				return html;
			}

		}

		return html;
	}

	// 获取北京用户 积分增长
	public  String getintegraResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		return lognAndGetBeijingService.getintegraResult( webClient,messageLogin, taskMobile);
	}

	// 获取北京用户话费余额
	public  String getChargesResult(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		return lognAndGetBeijingService.getChargesResult( webClient,messageLogin, taskMobile);
	}

//	// 获取北京用户 通话详单
//	public  String getCallThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
//			int i, int pagnum) {
//		LocalDate today = LocalDate.now();
//		// 本月的第一天
//		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
//		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
//		if (i == 0) {
//			enddate = today;
//		}
//		String monthint = stardate.getMonthValue() + "";
//		if (monthint.length() < 2) {
//			monthint = "0" + monthint;
//		}
//		String month = stardate.getYear() + "年" + monthint + "月";
//
//		// 本月的最后一天
//		return LognAndGetBeijing.getCallThrem(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth() + "",
//				enddate.getDayOfMonth() + "", month, pagnum + "",0);
//	}
	
	// 获取北京用户 通话详单
		public  String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
				String stardate, String enddate, String month, int pagenum) {
		
			// 本月的最后一天
				return  lognAndGetBeijingService.getCallThrem(webClient, messageLogin, taskMobile, stardate + "",
						enddate, month, pagenum + "");
			
			
		}

	// 获取北京用户 短信详单
	public  String getSMSThrem(WebClient webClientCookies, MessageLogin messageLogin, TaskMobile taskMobile,
			int i, int pagnum) {
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
		// 本月的最后一天
		return lognAndGetBeijingService.getSMSThrem(webClientCookies, messageLogin, taskMobile, stardate.getDayOfMonth() + "",
				enddate.getDayOfMonth() + "", month, pagnum + "");
	}

//	public  void main(String[] args) {
//		LocalDate today = LocalDate.now();
//		// 本月的第一天
//		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-1);
//		String monthint = stardate.getMonthValue() + "";
//		if (monthint.length() < 2) {
//			monthint = "0" + monthint;
//		}
//		String month = stardate.getYear() + "年" + monthint + "月";
//		System.out.println(month);
//
//	}

}
