package com.microservice.dao.entity.crawler.insurance.haerbin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 德州社保 失业信息
 * @author qizhongbin
 *
 */
@Entity
@Table(name="insurance_haerbin_shiye_info")
public class InsuranceHaerbinShiyeInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 缴费属期 */
	@Column(name="jfsq")
	private String jfsq;
	
	/** 对应缴费属期 */
	@Column(name="dyjfsq")
	private String dyjfsq;
	
	/** 单位应缴 */
	@Column(name="dwyj")
	private String dwyj;
	
	/** 个人应缴 */
	@Column(name="gryj")
	private String gryj;
	
	/** 应缴小计*/
	@Column(name="yjxj")
	private String yjxj;
	
	/** 公司名称 */
	@Column(name="gsmc")
	private String gsmc;
	
	/** 险种类型 */
	@Column(name="xzlx")
	private String xzlx;
	
	/** 缴费基数 */
	@Column(name="jfjs")
	private String jfjs;
	
	/** 缴费标识 */
	@Column(name="jfbs")
	private String jfbs;
	
	/** 缴费类型 */
	@Column(name="jflx")
	private String jflx;
	
	/** 划账日期 */
	@Column(name="hzrq")
	private String hzrq;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getJfsq() {
		return jfsq;
	}

	public void setJfsq(String jfsq) {
		this.jfsq = jfsq;
	}

	public String getDyjfsq() {
		return dyjfsq;
	}

	public void setDyjfsq(String dyjfsq) {
		this.dyjfsq = dyjfsq;
	}

	public String getDwyj() {
		return dwyj;
	}

	public void setDwyj(String dwyj) {
		this.dwyj = dwyj;
	}

	public String getGryj() {
		return gryj;
	}

	public void setGryj(String gryj) {
		this.gryj = gryj;
	}

	public String getYjxj() {
		return yjxj;
	}

	public void setYjxj(String yjxj) {
		this.yjxj = yjxj;
	}

	public String getGsmc() {
		return gsmc;
	}

	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	public String getJfjs() {
		return jfjs;
	}

	public void setJfjs(String jfjs) {
		this.jfjs = jfjs;
	}

	public String getJfbs() {
		return jfbs;
	}

	public void setJfbs(String jfbs) {
		this.jfbs = jfbs;
	}

	public String getJflx() {
		return jflx;
	}

	public void setJflx(String jflx) {
		this.jflx = jflx;
	}

	public String getHzrq() {
		return hzrq;
	}

	public void setHzrq(String hzrq) {
		this.hzrq = hzrq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
}