package app.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.crawler.TelecomHtmlSiChuan;

@Component
public class GetCommonAndData {

	public static String getConsumptionPoints(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			int i) {
		LocalDate today = LocalDate.now();
		//本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "-" + monthint;
		String month1 = stardate.getYear() + monthint;
		
		
		return TelecomHtmlSiChuan.getConsumptionPoints(webClient,messageLogin,taskMobile,stardate.getDayOfMonth()+"",
				enddate.getDayOfMonth()+"",month,0,month1);
	}

	public static String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {

		LocalDate today = LocalDate.now();
		//本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "-" + monthint;
		String month1 = stardate.getYear() + monthint;
		
		
		return TelecomHtmlSiChuan.getCallThrem(webClient,messageLogin,taskMobile,"0"+stardate.getDayOfMonth()+"",
				enddate.getDayOfMonth()+"",month,0,month1);
	}

	public static String getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		LocalDate today = LocalDate.now();
		//本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "-" + monthint;
		String month1 = stardate.getYear() + monthint;
		
		
		return TelecomHtmlSiChuan.getPayMent(webClient,messageLogin,taskMobile,"0"+stardate.getDayOfMonth()+"",
				"0"+enddate.getDayOfMonth()+"",month,0,month1);
	}

	public static Map<String,String> getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		
		LocalDate today = LocalDate.now();
		//本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "-" + monthint;
		String month1 = stardate.getYear() + monthint;
		
		
		return TelecomHtmlSiChuan.getPhoneBill(webClient,messageLogin,taskMobile,"0"+stardate.getDayOfMonth()+"",
				"0"+enddate.getDayOfMonth()+"",month,0,month1);
	}

	public static String getBillDetail(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) throws Exception {
		LocalDate today = LocalDate.now();
		//本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "-" + monthint;
		String month1 = stardate.getYear() + monthint;
		
		
		return TelecomHtmlSiChuan.getBillDetail(webClient,messageLogin,taskMobile,"0"+stardate.getDayOfMonth()+"",
				enddate.getDayOfMonth()+"",month,0,month1);
	}

}
