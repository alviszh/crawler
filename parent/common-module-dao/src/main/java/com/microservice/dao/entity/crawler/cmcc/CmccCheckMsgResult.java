package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * @author Administrator
 *	移动月账单
 */
@Entity
@Table(name="cmcc_checkmsg_result")
public class CmccCheckMsgResult extends IdEntity{
	
	@Column(name="task_id")
	private String taskId;
	private String billMonth;			//所属月份
	private String billStartDate;		//开始日期
	private String billEndDate;			//结束日期
	private String billFee;				//当月总消费金额
	
	@Override
	public String toString() {
		return "CmccCheckMsgResult [taskId=" + taskId + ", billMonth=" + billMonth
				+ ", billStartDate=" + billStartDate + ", billEndDate=" + billEndDate + ", billFee=" + billFee + "]";
	}
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBillMonth() {
		return billMonth;
	}
	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	public String getBillStartDate() {
		return billStartDate;
	}
	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}
	public String getBillEndDate() {
		return billEndDate;
	}
	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}
	public String getBillFee() {
		return billFee;
	}
	public void setBillFee(String billFee) {
		this.billFee = billFee;
	}

}
