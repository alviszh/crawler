package com.microservice.dao.entity.crawler.housing.lijiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_lijiang_account",indexes = {@Index(name = "index_housing_lijiang_account_taskid", columnList = "taskid")})
public class HousingFundLiJiangAccount extends IdEntity{

	private String datea;//日期
	
	private String taskid;
	
	private String descr;//摘要
	
	private String jf;//借方
	
	private String df;//贷方
	
	private String fee;//余额

	@Override
	public String toString() {
		return "HousingFundLiJiangAccount [datea=" + datea + ", taskid=" + taskid + ", descr=" + descr + ", jf=" + jf
				+ ", df=" + df + ", fee=" + fee + "]";
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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
	
	
}
