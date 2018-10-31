package com.microservice.dao.entity.crawler.telecom.yunnan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_yunnan_bill_html")
public class TelecomYunNanBillHtmlResult extends IdEntity {

	private String date;//月份
		
	private String num;//金额
		
	private Integer userid;

	private String taskid;
	
	private String html;
	
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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
		return "TelecomPhoneBillResult [date=" + date + ", num=" + num + ", userid=" + userid + ", taskid=" + taskid
				+ "]";
	}	
}