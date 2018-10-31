package com.microservice.dao.entity.crawler.telecom.chongqing;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 充值缴费
 * @author tz
 *
 */
@Entity
@Table(name = "telecom_chongqing_pay" ,indexes = {@Index(name = "index_telecom_chongqing_pay_taskid", columnList = "taskid")})
public class TelecomChongqingPay extends IdEntity {
	
	/**
	 * taskid
	 */
	private String taskid;
	
	/**
	 * 交易时间
	 */
	private String time;
	
	/**
	 * 交易编号
	 */
	private String orderNo;
	
	/**
	 * 交易状态
	 */
	private String state;
	
	/**
	 * 充值/交费金额 
	 */
	private String money;
	
	/**
	 * 充值/交费渠道
	 */
	private String channel;
	
	/**
	 * 充值/交费方式
	 */
	private String type;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TelecomChongqingPay [taskid=" + taskid + ", time=" + time + ", orderNo=" + orderNo + ", state=" + state
				+ ", money=" + money + ", channel=" + channel + ", type=" + type + "]";
	}

	public TelecomChongqingPay(String taskid, String time, String orderNo, String state, String money, String channel,
			String type) {
		super();
		this.taskid = taskid;
		this.time = time;
		this.orderNo = orderNo;
		this.state = state;
		this.money = money;
		this.channel = channel;
		this.type = type;
	}

	public TelecomChongqingPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}