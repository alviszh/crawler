package com.microservice.dao.entity.crawler.telecom.tianjin;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description:   首页我的现状信息
 * @author sln
 * @date 2017年9月11日
 */
@Entity
@Table(name = "telecom_tianjin_currentsituation",indexes = {@Index(name = "index_telecom_tianjin_currentsituation_taskid", columnList = "taskid")})
public class TelecomTianjinCurrentSituation extends IdEntity implements Serializable {
	private static final long serialVersionUID = -3365916506759020527L;
	private String taskid;
//	剩余流量
	private String remainflow;
//	剩余语音
	private String remainvoice;
//	剩余短信
	private String remainsms;
//	本月话费
	private String thismonthcharge;
//	积分
	private String totalintegra;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getRemainflow() {
		return remainflow;
	}
	public void setRemainflow(String remainflow) {
		this.remainflow = remainflow;
	}
	public String getRemainvoice() {
		return remainvoice;
	}
	public void setRemainvoice(String remainvoice) {
		this.remainvoice = remainvoice;
	}
	public String getRemainsms() {
		return remainsms;
	}
	public void setRemainsms(String remainsms) {
		this.remainsms = remainsms;
	}
	public String getThismonthcharge() {
		return thismonthcharge;
	}
	public void setThismonthcharge(String thismonthcharge) {
		this.thismonthcharge = thismonthcharge;
	}
	public String getTotalintegra() {
		return totalintegra;
	}
	public void setTotalintegra(String totalintegra) {
		this.totalintegra = totalintegra;
	}
}	
