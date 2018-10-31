package com.microservice.dao.entity.crawler.insurance.kaifeng;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_kaifeng_yibao_info")
public class InsurancekaifengYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 计划月份 */
	@Column(name="jhyf")
	private String jhyf;
	
	/** 征缴月份 */
	@Column(name="zjyf")
	private String zjyf;
	
	/** 缴费基数 */
	@Column(name="jfjs")
	private String jfjs;
	
	/** 单位缴费 */
	@Column(name="dwjf")
	private String dwjf;
	
	/** 个人缴费 */
	@Column(name="grjf")
	private String grjf;
	
	/** 合计 */
	@Column(name="hj")
	private String hj;
	
	/** 记入个人部分 */
	@Column(name="jrgrbf")
	private String jrgrbf;
	
	/** 缴费标志 */
	@Column(name="jfbz")
	private String jfbz;
	
	/** 到账日期 */
	@Column(name="dzrq")
	private String dzrq;
	
	/** 单位名称 */
	@Column(name="dwmc")
	private String dwmc;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getJhyf() {
		return jhyf;
	}

	public void setJhyf(String jhyf) {
		this.jhyf = jhyf;
	}

	public String getZjyf() {
		return zjyf;
	}

	public void setZjyf(String zjyf) {
		this.zjyf = zjyf;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public String getDwjf() {
		return dwjf;
	}

	public void setDwjf(String dwjf) {
		this.dwjf = dwjf;
	}

	public String getGrjf() {
		return grjf;
	}

	public void setGrjf(String grjf) {
		this.grjf = grjf;
	}

	public String getHj() {
		return hj;
	}

	public void setHj(String hj) {
		this.hj = hj;
	}

	public String getJrgrbf() {
		return jrgrbf;
	}

	public void setJrgrbf(String jrgrbf) {
		this.jrgrbf = jrgrbf;
	}

	public String getJfbz() {
		return jfbz;
	}

	public void setJfbz(String jfbz) {
		this.jfbz = jfbz;
	}

	public String getDzrq() {
		return dzrq;
	}

	public void setDzrq(String dzrq) {
		this.dzrq = dzrq;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}