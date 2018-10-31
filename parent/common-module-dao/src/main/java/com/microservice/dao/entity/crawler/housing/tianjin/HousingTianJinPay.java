package com.microservice.dao.entity.crawler.housing.tianjin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_tianjin_pay")
public class HousingTianJinPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//日期
	@Column(name="date")
	private String date;
	//月份
	@Column(name="month")
	private String month;
	//业务摘要
	@Column(name="memo")
	private String memo;
	//发生金额
	@Column(name="happen_money")
	private String happen_money;
	
	//账户余额
	@Column(name="yue")
	private String yue;
	
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
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getHappen_money() {
		return happen_money;
	}
	public void setHappen_money(String happen_money) {
		this.happen_money = happen_money;
	}
	public String getYue() {
		return yue;
	}
	public void setYue(String yue) {
		this.yue = yue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
