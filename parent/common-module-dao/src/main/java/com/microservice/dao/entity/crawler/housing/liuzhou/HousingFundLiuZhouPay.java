package com.microservice.dao.entity.crawler.housing.liuzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "housing_liuzhou_pay",indexes = {@Index(name = "index_housing_liuzhou_pay_taskid", columnList = "taskid")})
public class HousingFundLiuZhouPay extends IdEntity implements Serializable{

	private String taskid;
	private String name;
	private String idcard;
	private String ywdate;//业务日期
	private String serialnum;//流水号
	private String type;//业务类型
	private String paydate;//存缴年月
	private String increase;//增加额
	private String reduce;//减少额
	private String balance;//余额
	private String dw_num;//单位帐号
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getYwdate() {
		return ywdate;
	}
	public void setYwdate(String ywdate) {
		this.ywdate = ywdate;
	}
	public String getSerialnum() {
		return serialnum;
	}
	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getReduce() {
		return reduce;
	}
	public void setReduce(String reduce) {
		this.reduce = reduce;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getDw_num() {
		return dw_num;
	}
	public void setDw_num(String dw_num) {
		this.dw_num = dw_num;
	}
	public HousingFundLiuZhouPay(String taskid, String name, String idcard, String ywdate, String serialnum,
			String type, String paydate, String increase, String reduce, String balance, String dw_num) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.ywdate = ywdate;
		this.serialnum = serialnum;
		this.type = type;
		this.paydate = paydate;
		this.increase = increase;
		this.reduce = reduce;
		this.balance = balance;
		this.dw_num = dw_num;
	}
	public HousingFundLiuZhouPay() {
		super();
	}
	@Override
	public String toString() {
		return "HousingFundLiuZhouPay [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard + ", ywdate="
				+ ywdate + ", serialnum=" + serialnum + ", type=" + type + ", paydate=" + paydate + ", increase="
				+ increase + ", reduce=" + reduce + ", balance=" + balance + ", dw_num=" + dw_num + "]";
	}
	
	
}
