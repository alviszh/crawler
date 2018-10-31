package com.microservice.dao.entity.crawler.housing.panzhihua;

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
@Table(name="housing_panzhihua_pay")
public class HousingPanzhihuaPay extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	
	//缴存年月
	@Column(name="month")
	private String month;		
	
	//入账时间
	@Column(name="rzsj")
	private String rzsj;		
	
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	
	//业务类型
	@Column(name="ywlx")
	private String ywlx;		
	
	//个人月缴存额
	@Column(name="gryjce")
	private String gryjce;		
	
	//单位月缴存额
	@Column(name="dwyjce")
	private String dwyjce;		
	
	//合计月缴存额
	@Column(name="hjyjce")
	private String hjyjce;
	
	//备注
	@Column(name="bz")
	private String bz;

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRzsj() {
		return rzsj;
	}

	public void setRzsj(String rzsj) {
		this.rzsj = rzsj;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getGryjce() {
		return gryjce;
	}

	public void setGryjce(String gryjce) {
		this.gryjce = gryjce;
	}

	public String getDwyjce() {
		return dwyjce;
	}

	public void setDwyjce(String dwyjce) {
		this.dwyjce = dwyjce;
	}

	public String getHjyjce() {
		return hjyjce;
	}

	public void setHjyjce(String hjyjce) {
		this.hjyjce = hjyjce;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
}
