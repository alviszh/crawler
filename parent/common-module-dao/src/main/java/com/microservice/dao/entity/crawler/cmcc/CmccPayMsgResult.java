package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmcc_paymsg_result")
public class CmccPayMsgResult extends IdEntity{
	
	private String payChannel;			//缴费频道
	private String payDate;				//缴费日期
	private String payFee;				//缴费金额
	private String payType;				//缴费类型
	private String payTypeName;			//缴费类型名称
	@Column(name="task_id")
	private String taskId;
	
	@Override
	public String toString() {
		return "CmccPayMsgResult [payChannel=" + payChannel + ", payDate=" + payDate + ", payFee=" + payFee
				+ ", payType=" + payType + ", payTypeName=" + payTypeName + ", taskId=" + taskId + "]";
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	

}
