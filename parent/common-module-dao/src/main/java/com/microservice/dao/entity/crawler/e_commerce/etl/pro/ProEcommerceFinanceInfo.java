package com.microservice.dao.entity.crawler.e_commerce.etl.pro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="pro_ecommerce_finance_info",indexes = {@Index(name = "index_pro_ecommerce_finance_info_taskid", columnList = "taskId")})
public class ProEcommerceFinanceInfo extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	@Column(name="task_id")
	private String taskId;	//唯一标识
	private String resource;
	private String financeName;
	private String avialableBalance;
	private String accumulatedIncome;
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
	public String getFinanceName() {
		return financeName;
	}
	public void setFinanceName(String financeName) {
		this.financeName = financeName;
	}
	public String getAvialableBalance() {
		return avialableBalance;
	}
	public void setAvialableBalance(String avialableBalance) {
		this.avialableBalance = avialableBalance;
	}
	public String getAccumulatedIncome() {
		return accumulatedIncome;
	}
	public void setAccumulatedIncome(String accumulatedIncome) {
		this.accumulatedIncome = accumulatedIncome;
	}
	@Override
	public String toString() {
		return "ProEcommerceFinanceInfo [taskId=" + taskId + ", resource=" + resource + ", financeName=" + financeName
				+ ", avialableBalance=" + avialableBalance + ", accumulatedIncome=" + accumulatedIncome + "]";
	}
	
		
}	
