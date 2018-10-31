package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_mobile_pay_info",indexes = {@Index(name = "index_pro_mobile_pay_info_taskid", columnList = "taskId")})
public class ProMobilePayInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String payfee;
	private String paytime;
	private String paymonth;
	private String payway;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getPayfee() {
		return payfee;
	}
	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getPaymonth() {
		return paymonth;
	}
	public void setPaymonth(String paymonth) {
		this.paymonth = paymonth;
	}
	public String getPayway() {
		return payway;
	}
	public void setPayway(String payway) {
		this.payway = payway;
	}
	@Override
	public String toString() {
		return "ProMobilePayInfo [taskId=" + taskId + ", resource=" + resource + ", payfee=" + payfee + ", paytime="
				+ paytime + ", paymonth=" + paymonth + ", payway=" + payway + "]";
	}
	
}
