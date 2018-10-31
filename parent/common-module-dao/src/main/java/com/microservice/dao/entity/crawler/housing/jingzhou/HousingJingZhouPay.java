package com.microservice.dao.entity.crawler.housing.jingzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_jingzhou_pay",indexes = {@Index(name = "index_housing_jingzhou_pay_taskid", columnList = "taskid")})
public class HousingJingZhouPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4047708540377930537L;

	private String taskid;

	private String num;

	private String rdate;

	private String tq_money;

	private String cr_money;

	private String yu;

	private String zai;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getRdate() {
		return rdate;
	}

	public void setRdate(String rdate) {
		this.rdate = rdate;
	}

	public String getTq_money() {
		return tq_money;
	}

	public void setTq_money(String tq_money) {
		this.tq_money = tq_money;
	}

	public String getCr_money() {
		return cr_money;
	}

	public void setCr_money(String cr_money) {
		this.cr_money = cr_money;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

	public String getZai() {
		return zai;
	}

	public void setZai(String zai) {
		this.zai = zai;
	}

	public HousingJingZhouPay(String taskid, String num, String rdate, String tq_money, String cr_money, String yu,
			String zai) {
		super();
		this.taskid = taskid;
		this.num = num;
		this.rdate = rdate;
		this.tq_money = tq_money;
		this.cr_money = cr_money;
		this.yu = yu;
		this.zai = zai;
	}

	public HousingJingZhouPay() {
		super();
	}

	@Override
	public String toString() {
		return "HousingSiPingPay [taskid=" + taskid + ", num=" + num + ", rdate=" + rdate + ", tq_money=" + tq_money
				+ ", cr_money=" + cr_money + ", yu=" + yu + ", zai=" + zai + "]";
	}

}
