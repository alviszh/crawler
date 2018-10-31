package com.microservice.dao.entity.crawler.telecom.jiangsu;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 短信详单
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_jiangsu_message" ,indexes = {@Index(name = "index_telecom_jiangsu_message_taskid", columnList = "taskid")})
public class TelecomJiangsuMessage extends IdEntity {

	/**
	 * taskid
	 */
	private String taskid;

	/**
	 * 对方号码
	 */
	private String accNbr;

	/**
	 * 业务类型
	 */
	private String ticketType;

	/**
	 * 发送日期（年月日）
	 */
	private String startDateNew;

	/**
	 * 发送开始时间（时分秒）
	 */
	private String startTimeNew;

	/**
	 * 金额（元）
	*/
	private String ticketChargeCh;
	
	/**
	 * 产品名称
	 */
	private String productName;

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

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getStartDateNew() {
		return startDateNew;
	}

	public void setStartDateNew(String startDateNew) {
		this.startDateNew = startDateNew;
	}

	public String getStartTimeNew() {
		return startTimeNew;
	}

	public void setStartTimeNew(String startTimeNew) {
		this.startTimeNew = startTimeNew;
	}

	public String getTicketChargeCh() {
		return ticketChargeCh;
	}

	public void setTicketChargeCh(String ticketChargeCh) {
		this.ticketChargeCh = ticketChargeCh;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "TelecomJiangsuMessage [taskid=" + taskid + ", accNbr=" + accNbr + ", ticketType=" + ticketType
				+ ", startDateNew=" + startDateNew + ", startTimeNew=" + startTimeNew + ", ticketChargeCh="
				+ ticketChargeCh + ", productName=" + productName + "]";
	}

	public TelecomJiangsuMessage(String taskid, String accNbr, String ticketType, String startDateNew,
			String startTimeNew, String ticketChargeCh, String productName) {
		super();
		this.taskid = taskid;
		this.accNbr = accNbr;
		this.ticketType = ticketType;
		this.startDateNew = startDateNew;
		this.startTimeNew = startTimeNew;
		this.ticketChargeCh = ticketChargeCh;
		this.productName = productName;
	}

	public TelecomJiangsuMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}