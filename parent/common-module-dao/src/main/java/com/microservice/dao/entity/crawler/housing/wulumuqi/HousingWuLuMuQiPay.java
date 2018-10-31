package com.microservice.dao.entity.crawler.housing.wulumuqi;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_wulumuqi_pay")
public class HousingWuLuMuQiPay  extends IdEntity implements Serializable {
		
	private String taskid;
	private String payDate;						//记账日期
	private String payType;						//归集和提取业务类型
	private String fee;							//发生额
	private String interest;					//发生利息额
	private String getReason;					//提取原因
	private String getType;						//提取方式
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getGetReason() {
		return getReason;
	}
	public void setGetReason(String getReason) {
		this.getReason = getReason;
	}
	public String getGetType() {
		return getType;
	}
	public void setGetType(String getType) {
		this.getType = getType;
	}
	@Override
	public String toString() {
		return "HousingZhuHaiPay [taskid=" + taskid + ", payDate=" + payDate + ", payType=" + payType + ", fee=" + fee
				+ ", interest=" + interest + ", getReason=" + getReason + ", getType=" + getType + "]";
	}
}
