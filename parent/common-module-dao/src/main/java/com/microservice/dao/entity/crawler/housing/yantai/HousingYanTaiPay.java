package com.microservice.dao.entity.crawler.housing.yantai;

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
@Table(name = "housing_yantai_pay")
public class HousingYanTaiPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name = "taskid")
	private String taskid;
	//个人账号
	@Column(name = "person_number")
	private String person_number;
	
	//记账时间
	@Column(name = "date")
	private String date;
	
	//收支类型
	@Column(name = "type")
	private String type;
	
	//月份
	@Column(name = "month")
	private String month;
	
	//发生额
	@Column(name = "open_money")
	private String open_money;
	
	//余额
	@Column(name = "yue")
	private String yue;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getPerson_number() {
		return person_number;
	}

	public void setPerson_number(String person_number) {
		this.person_number = person_number;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getOpen_money() {
		return open_money;
	}

	public void setOpen_money(String open_money) {
		this.open_money = open_money;
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
