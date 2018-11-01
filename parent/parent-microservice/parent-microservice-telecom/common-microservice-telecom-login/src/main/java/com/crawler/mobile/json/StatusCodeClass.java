package com.crawler.mobile.json;

import java.time.LocalDate;

public class StatusCodeClass {

	private String description;
	
    private String phase;//当前步骤
	
	private String phasestatus;//步骤状态
	
	private Integer error_code;
	
	
	public static StatusCodeClass StatusCodeClass_USER_MSG() {
		StatusCodeClass classunicom= new StatusCodeClass();
		classunicom.setDescription("数据采集中，用户信息已采集完成");
		classunicom.setPhase("CRAWLER_USER_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	public static StatusCodeClass StatusCodeClass_SMS_MSG(int i) {
		StatusCodeClass classunicom= new StatusCodeClass();
		int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();

		classunicom.setDescription("数据采集中，短信信息"+nowmonth+"月已采集完成");
		classunicom.setPhase("CRAWLER_USER_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	public static StatusCodeClass StatusCodeClass_CALL_MSG(int i) {
		StatusCodeClass classunicom= new StatusCodeClass();
		int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();
		classunicom.setDescription("数据采集中，通讯信息"+nowmonth+"月已采集完成");
		classunicom.setPhase("CRAWLER_CALL_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	
	public static StatusCodeClass StatusCodeClass_PAY_MSG(int i) {
		StatusCodeClass classunicom= new StatusCodeClass();
		int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();

		classunicom.setDescription("数据采集中，缴费信息"+nowmonth+"月已采集完成");
		classunicom.setPhase("CRAWLER_PAY_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	public static StatusCodeClass StatusCodeClass_Buss_MSG() {
		StatusCodeClass classunicom= new StatusCodeClass();

		classunicom.setDescription("数据采集中，套餐信息已采集完成");
		classunicom.setPhase("CRAWLER_Buss_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	public static StatusCodeClass StatusCodeClass_CHECK_MSG(int i) {
		int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();

		StatusCodeClass classunicom= new StatusCodeClass();
		classunicom.setDescription("数据采集中，账单信息"+nowmonth+"月已采集完成");
		classunicom.setPhase("CRAWLER_CHECK_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	public static StatusCodeClass StatusCodeClass_INTEGRA_MSG(int i) {
		int nowmonth = LocalDate.now().plusMonths(-i).getMonthValue();

		StatusCodeClass classunicom= new StatusCodeClass();
		classunicom.setDescription("数据采集中，积分信息"+nowmonth+"月已采集完成");
		classunicom.setPhase("CRAWLER_CHECK_MSG");
		classunicom.setPhasestatus("SUCCESS");
		classunicom.setError_code(0);
		return classunicom;
	}
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}



	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getPhasestatus() {
		return phasestatus;
	}

	public void setPhasestatus(String phasestatus) {
		this.phasestatus = phasestatus;
	}

	public Integer getError_code() {
		return error_code;
	}

	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
		
}
