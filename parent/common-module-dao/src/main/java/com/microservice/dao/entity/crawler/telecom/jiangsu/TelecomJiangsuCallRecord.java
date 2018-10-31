package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 语音详单
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_callrecord" ,indexes = {@Index(name = "index_telecom_jiangsu_callrecord_taskid", columnList = "taskid")})
public class TelecomJiangsuCallRecord extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 对方号码
	 */
	private String accNbr;

	/**
	 * 日期
	 */
	private String startDateNew;

	/**
	 * 呼叫类型
	 */
	private String ticketTypeNew;

	/**
	 * 通话类型
	 */
	private String durationType;

	/**
	 * 通话地区
	*/
	private String areaCode;
	
	/**
	 * 通话开始时间（时分秒）
	 */
	private String startTimeNew;
	
	/**
	 * 通话时长（时分秒）
	 */
	private String durationCh;
	
	/**
	 * 通话时间（时分秒）
	 */
	private String startTime;
	
	/**
	 * 金额（元）
	 */
	private String ticketChargeCh  ;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccNbr() {
		return accNbr;
	}

	public void setAccNbr(String accNbr) {
		this.accNbr = accNbr;
	}

	public String getStartDateNew() {
		return startDateNew;
	}

	public void setStartDateNew(String startDateNew) {
		this.startDateNew = startDateNew;
	}

	public String getTicketTypeNew() {
		return ticketTypeNew;
	}

	public void setTicketTypeNew(String ticketTypeNew) {
		this.ticketTypeNew = ticketTypeNew;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getStartTimeNew() {
		return startTimeNew;
	}

	public void setStartTimeNew(String startTimeNew) {
		this.startTimeNew = startTimeNew;
	}

	public String getDurationCh() {
		return durationCh;
	}

	public void setDurationCh(String durationCh) {
		this.durationCh = durationCh;
	}

	public String getTicketChargeCh() {
		return ticketChargeCh;
	}

	public void setTicketChargeCh(String ticketChargeCh) {
		this.ticketChargeCh = ticketChargeCh;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public TelecomJiangsuCallRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TelecomJiangsuCallRecord(String taskid, String accNbr, String startDateNew, String ticketTypeNew,
			String durationType, String areaCode, String startTimeNew, String durationCh, String startTime,
			String ticketChargeCh) {
		super();
		this.taskid = taskid;
		this.accNbr = accNbr;
		this.startDateNew = startDateNew;
		this.ticketTypeNew = ticketTypeNew;
		this.durationType = durationType;
		this.areaCode = areaCode;
		this.startTimeNew = startTimeNew;
		this.durationCh = durationCh;
		this.startTime = startTime;
		this.ticketChargeCh = ticketChargeCh;
	}

}