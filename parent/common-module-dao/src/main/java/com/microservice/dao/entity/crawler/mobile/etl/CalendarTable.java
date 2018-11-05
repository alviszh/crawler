package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="calendar_table")
public class CalendarTable  extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5223553236512118239L;
	
	
	private String calDate;
	
	private String dayOfWeek;
	
	private String weekStatus;
	
	private String isWork;

	public String getCalDate() {
		return calDate;
	}

	public void setCalDate(String calDate) {
		this.calDate = calDate;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getWeekStatus() {
		return weekStatus;
	}

	public void setWeekStatus(String weekStatus) {
		this.weekStatus = weekStatus;
	}

	public String getIsWork() {
		return isWork;
	}

	public void setIsWork(String isWork) {
		this.isWork = isWork;
	}

	@Override
	public String toString() {
		return "CalendarTable [calDate=" + calDate + ", dayOfWeek=" + dayOfWeek + ", weekStatus=" + weekStatus
				+ ", isWork=" + isWork + "]";
	}
	
	

}
