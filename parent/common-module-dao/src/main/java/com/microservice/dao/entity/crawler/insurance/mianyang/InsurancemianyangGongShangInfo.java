package com.microservice.dao.entity.crawler.insurance.mianyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_mianyang_gongshang_info")
public class InsurancemianyangGongShangInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 个人编号 */
	@Column(name="grbh")
	private String grbh;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 单位编号*/
	@Column(name="dwbh")
	private String dwbh;
	
	/** 单位名称*/
	@Column(name="dwmc")
	private String dwmc;
	
	/** 所属期号*/
	@Column(name="ssqh")
	private String ssqh;
	
	/** 个人应缴*/
	@Column(name="gryj")
	private String gryj;
	
	/** 单位应缴*/
	@Column(name="dwyj")
	private String dwyj;
	
	/** 个人实缴*/
	@Column(name="grsj")
	private String grsj;
	
	/** 单位实缴*/
	@Column(name="dwsj")
	private String dwsj;
	
	/** 应缴合计*/
	@Column(name="yjhj")
	private String yjhj;
	
	/** 实缴合计*/
	@Column(name="sjhj")
	private String sjhj;

	public String getGrbh() {
		return grbh;
	}


	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDwbh() {
		return dwbh;
	}


	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}


	public String getDwmc() {
		return dwmc;
	}


	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}


	public String getSsqh() {
		return ssqh;
	}


	public void setSsqh(String ssqh) {
		this.ssqh = ssqh;
	}


	public String getGryj() {
		return gryj;
	}


	public void setGryj(String gryj) {
		this.gryj = gryj;
	}


	public String getDwyj() {
		return dwyj;
	}


	public void setDwyj(String dwyj) {
		this.dwyj = dwyj;
	}


	public String getGrsj() {
		return grsj;
	}


	public void setGrsj(String grsj) {
		this.grsj = grsj;
	}


	public String getDwsj() {
		return dwsj;
	}


	public void setDwsj(String dwsj) {
		this.dwsj = dwsj;
	}


	public String getYjhj() {
		return yjhj;
	}


	public void setYjhj(String yjhj) {
		this.yjhj = yjhj;
	}


	public String getSjhj() {
		return sjhj;
	}


	public void setSjhj(String sjhj) {
		this.sjhj = sjhj;
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