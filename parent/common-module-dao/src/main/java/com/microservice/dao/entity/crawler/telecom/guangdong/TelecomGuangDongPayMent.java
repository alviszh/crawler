package com.microservice.dao.entity.crawler.telecom.guangdong;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_guangdong_payment",indexes = {@Index(name = "index_telecom_guangdong_payment_taskid", columnList = "taskid")})
public class TelecomGuangDongPayMent extends IdEntity{

	private String pretime;//时间
	
	private String money;//金额
	
	private String type;//类型
	
	private String channels;//缴费渠道
	
	private String paymode;//缴费方式
	
	private String usamode;//使用方式
	
	private String taskid;
	
	private Integer userid;

	public String getPretime() {
		return pretime;
	}

	public void setPretime(String pretime) {
		this.pretime = pretime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getPaymode() {
		return paymode;
	}

	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}

	public String getUsamode() {
		return usamode;
	}

	public void setUsamode(String usamode) {
		this.usamode = usamode;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "TelecomGuangDongPayMent [pretime=" + pretime + ", money=" + money + ", type=" + type + ", channels="
				+ channels + ", paymode=" + paymode + ", usamode=" + usamode + ", taskid=" + taskid + ", userid="
				+ userid + "]";
	}
	
	
}
