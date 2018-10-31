package com.microservice.dao.entity.crawler.housing.suzhou2;

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
@Table(name="housing_suzhou2_base")
public class HousingsuzhouBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	//个人姓名
	@Column(name="grxm")
	private String grxm;		
	//身份证号码
	@Column(name="sfzhm")
	private String sfzhm;		
	//缴存基数
	@Column(name="jcjs")
	private String jcjs;		
	//月缴存额
	@Column(name="yjce")
	private String yjce;		
	//公积金余额
	@Column(name="gjjye")
	private String gjjye;		
	//单位账号
	@Column(name="dwzh")
	private String dwzh;		
	//个人账号
	@Column(name="grzh")
	private String grzh;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getGrxm() {
		return grxm;
	}
	public void setGrxm(String grxm) {
		this.grxm = grxm;
	}
	public String getSfzhm() {
		return sfzhm;
	}
	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}
	public String getJcjs() {
		return jcjs;
	}
	public void setJcjs(String jcjs) {
		this.jcjs = jcjs;
	}
	public String getYjce() {
		return yjce;
	}
	public void setYjce(String yjce) {
		this.yjce = yjce;
	}
	public String getGjjye() {
		return gjjye;
	}
	public void setGjjye(String gjjye) {
		this.gjjye = gjjye;
	}
	public String getDwzh() {
		return dwzh;
	}
	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}
	public String getGrzh() {
		return grzh;
	}
	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}		
	
}
