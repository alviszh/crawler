package com.microservice.dao.entity.crawler.housing.bengbu;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_bengbu_account",indexes = {@Index(name = "index_housing_bengbu_account_taskid", columnList = "taskid")})
public class HousingFundBengBuAccount extends IdEntity implements Serializable{

	private String datea;//日期
	private String descr;//摘要
	private String jf;//借方金额
	private String df;//贷方金额
	private String fee;//余额
	private String taskid;
	@Override
	public String toString() {
		return "HousingFundBengBuAccount [datea=" + datea + ", descr=" + descr + ", jf=" + jf + ", df=" + df + ", fee="
				+ fee + ", taskid=" + taskid + "]";
	}
	public String getDatea() {
		return datea;
	}
	public void setDatea(String datea) {
		this.datea = datea;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getJf() {
		return jf;
	}
	public void setJf(String jf) {
		this.jf = jf;
	}
	public String getDf() {
		return df;
	}
	public void setDf(String df) {
		this.df = df;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
