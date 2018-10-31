package com.microservice.dao.entity.crawler.telecom.hainan;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_hainan_pay")
public class TelecomHaiNanPayResult extends IdEntity {
	private String userrage;//指定号码和业务

	private String liushui;//980425860

	private String feeway;//现金

	private String ruzhangtime;//2017-09-20 14:46:20

	private String channel;

	private String ruzhangmon;
	
	private Integer userid;

	private String taskid;

	public void setUserrage(String userrage) {
		this.userrage = userrage;
	}

	public String getUserrage() {
		return this.userrage;
	}

	public void setLiushui(String liushui) {
		this.liushui = liushui;
	}

	public String getLiushui() {
		return this.liushui;
	}

	public void setFeeway(String feeway) {
		this.feeway = feeway;
	}

	public String getFeeway() {
		return this.feeway;
	}

	public void setRuzhangtime(String ruzhangtime) {
		this.ruzhangtime = ruzhangtime;
	}

	public String getRuzhangtime() {
		return this.ruzhangtime;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setRuzhangmon(String ruzhangmon) {
		this.ruzhangmon = ruzhangmon;
	}

	public String getRuzhangmon() {
		return this.ruzhangmon;
	}

	
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomHaiNanPayResult [userrage=" + userrage + ", liushui=" + liushui + ", feeway=" + feeway
				+ ", ruzhangtime=" + ruzhangtime + ", channel=" + channel + ", ruzhangmon=" + ruzhangmon + ", userid="
				+ userid + ", taskid=" + taskid + "]";
	}

}