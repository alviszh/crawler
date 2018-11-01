package app.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.crawler.TelecomHtmlGuangDong;

@Component
public class GetCommonAndData {

	@Autowired
	private TelecomHtmlGuangDong telecomHtmlGuangDong;
	public String getPayMent(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month1 = stardate.getYear() + monthint;
		return telecomHtmlGuangDong.getPayMent(webClient,messageLogin,taskMobile,stardate.getDayOfMonth()+"",
				enddate.getDayOfMonth()+"",0,month1);
	}

	public String getCallThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		LocalDate today = LocalDate.now();
		String today1 = today.toString();


		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String stardate1 = stardate.toString();
		String[] split2 = stardate1.split("-");
		String kai = "";
		for (String string : split2) {
			kai+=string;
		}

		String enddate1 = enddate.toString();
		String[] split3 = enddate1.split("-");
		String end="";
		for (String string : split3) {
			end+=string;
		}

		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month1 = stardate.getYear() + monthint;
		return telecomHtmlGuangDong.getCallThrem(webClient,messageLogin,taskMobile,kai,
				end,0,month1);
	}

	public  String getSMSThrem(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		LocalDate today = LocalDate.now();
		String today1 = today.toString();

		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if(i == 0) {
			enddate = today ;
		}
		String stardate1 = stardate.toString();
		String[] split2 = stardate1.split("-");
		String kai = "";
		for (String string : split2) {
			kai+=string;
		}

		String enddate1 = enddate.toString();
		String[] split3 = enddate1.split("-");
		String end="";
		for (String string : split3) {
			end+=string;
		}

		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month1 = stardate.getYear() + monthint;
		return telecomHtmlGuangDong.getSMSThrem(webClient,messageLogin,taskMobile, kai,
				end,0,month1);
	}

}
