package com.microservice.dao.entity.crawler.e_commerce.etl.jd;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="financeinfo_jd") //交易明细
public class FinanceInfoJD extends IdEntity implements Serializable{

	private static final long serialVersionUID = -3207075026659390860L;

	@Column(name="task_id")
	private String taskId; //唯一标识
	private String financeAmount;
	private String yesterdayProfit;
	
	@JsonBackReference
	private String resource; //溯源字段

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFinanceAmount() {
		return financeAmount;
	}

	public void setFinanceAmount(String financeAmount) {
		this.financeAmount = financeAmount;
	}

	public String getYesterdayProfit() {
		return yesterdayProfit;
	}

	public void setYesterdayProfit(String yesterdayProfit) {
		this.yesterdayProfit = yesterdayProfit;
	}
	
	@Column(columnDefinition="text")
	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "FinanceInfoJD [taskId=" + taskId + ", financeAmount=" + financeAmount + ", yesterdayProfit="
				+ yesterdayProfit + ", resource=" + resource + "]";
	}
	
	
}
