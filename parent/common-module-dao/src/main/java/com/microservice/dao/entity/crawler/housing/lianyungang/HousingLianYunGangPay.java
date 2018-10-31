package com.microservice.dao.entity.crawler.housing.lianyungang;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_lianyungang_pay")
public class HousingLianYunGangPay  extends IdEntity implements Serializable {
		
	private String taskid;
	private String date;					//日期
	private String remark;					//摘要
	private String outMoney;				//支用金额
	private String getMoney;				//收入金额
	private String balance;					//余额
	private String mark;					//备注
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOutMoney() {
		return outMoney;
	}
	public void setOutMoney(String outMoney) {
		this.outMoney = outMoney;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	@Override
	public String toString() {
		return "HousingLianYunGangPay [taskid=" + taskid + ", date=" + date + ", remark=" + remark + ", outMoney="
				+ outMoney + ", getMoney=" + getMoney + ", balance=" + balance + ", mark=" + mark + "]";
	}
	
}
