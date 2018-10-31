package com.microservice.dao.entity.crawler.insurance.mianyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_mianyang_baseinfo")
public class InsurancemianyangBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 身份证号 */
	@Column(name="sfzh")
	private String sfzh;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 出生年月 */
	@Column(name="birth")
	private String birth;
	
	/** 参工时间 */
	@Column(name="cgsj")
	private String cgsj;


	public String getGrbh() {
		return grbh;
	}


	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}


	public String getSfzh() {
		return sfzh;
	}


	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getBirth() {
		return birth;
	}


	public void setBirth(String birth) {
		this.birth = birth;
	}


	public String getCgsj() {
		return cgsj;
	}


	public void setCgsj(String cgsj) {
		this.cgsj = cgsj;
	}


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}