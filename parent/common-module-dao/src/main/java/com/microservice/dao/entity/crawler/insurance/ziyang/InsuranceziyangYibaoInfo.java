package com.microservice.dao.entity.crawler.insurance.ziyang;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_ziyang_yibao_info")
public class InsuranceziyangYibaoInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 单位名称*/
	@Column(name="dwmc")
	private String dwmc;
	
	/** 缴费年月 */
	@Column(name="month")
	private String month;
	
	/** 费款所属年月 */
	@Column(name="fkssny")
	private String fkssny;
	
	/** 缴费基数 */
	@Column(name="jfjs")
	private String jfjs;
	
	/** 单位缴费 */
	@Column(name="dwjf")
	private String dwjf;
	
	/** 个人缴费 */
	@Column(name="grjf")
	private String grjf;
	
	/** 缴费状态 */
	@Column(name="jfzt")
	private String jfzt;
	
	/** 参保地 */
	@Column(name="cbd")
	private String cbd;
	
	/** 划入个账金额 */
	@Column(name="hrgzje")
	private String hrgzje;
	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getDwmc() {
		return dwmc;
	}


	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getFkssny() {
		return fkssny;
	}


	public void setFkssny(String fkssny) {
		this.fkssny = fkssny;
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


	public String getJfzt() {
		return jfzt;
	}


	public void setJfzt(String jfzt) {
		this.jfzt = jfzt;
	}


	public String getCbd() {
		return cbd;
	}


	public void setCbd(String cbd) {
		this.cbd = cbd;
	}


	public String getHrgzje() {
		return hrgzje;
	}


	public void setHrgzje(String hrgzje) {
		this.hrgzje = hrgzje;
	}
	
}