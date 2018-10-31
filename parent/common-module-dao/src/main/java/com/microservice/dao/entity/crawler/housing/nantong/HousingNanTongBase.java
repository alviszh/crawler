package com.microservice.dao.entity.crawler.housing.nantong;

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
@Table(name="housing_nantong_base")
public class HousingNanTongBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;	
	
	//单位账户
	@Column(name="dwzh")
	private String dwzh;	
	
	//姓名
	@Column(name="xm")
	private String xm;	
	
	//身份证号码
	@Column(name="sfzhm")
	private String sfzhm;	
	
	//个人账户
	@Column(name="grzh")
	private String grzh;	
	
	//缴存基数
	@Column(name="jcjs")
	private String jcjs;	
	
	//月汇缴额
	@Column(name="yhje")
	private String yhje;	
	
	//公积金余额
	@Column(name="gjjye")
	private String gjjye;	
	
	//补贴月缴存额
	@Column(name="btyjce")
	private String btyjce;	
	
	//补贴余额
	@Column(name="btye")
	private String btye;	
	
	//最新汇缴年月
	@Column(name="zxhjny")
	private String zxhjny;	
	
	//账户状态
	@Column(name="zhzt")
	private String zhzt;

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

	public String getDwzh() {
		return dwzh;
	}

	public void setDwzh(String dwzh) {
		this.dwzh = dwzh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getSfzhm() {
		return sfzhm;
	}

	public void setSfzhm(String sfzhm) {
		this.sfzhm = sfzhm;
	}

	public String getGrzh() {
		return grzh;
	}

	public void setGrzh(String grzh) {
		this.grzh = grzh;
	}

	public String getJcjs() {
		return jcjs;
	}

	public void setJcjs(String jcjs) {
		this.jcjs = jcjs;
	}

	public String getYhje() {
		return yhje;
	}

	public void setYhje(String yhje) {
		this.yhje = yhje;
	}

	public String getGjjye() {
		return gjjye;
	}

	public void setGjjye(String gjjye) {
		this.gjjye = gjjye;
	}

	public String getBtyjce() {
		return btyjce;
	}

	public void setBtyjce(String btyjce) {
		this.btyjce = btyjce;
	}

	public String getBtye() {
		return btye;
	}

	public void setBtye(String btye) {
		this.btye = btye;
	}

	public String getZxhjny() {
		return zxhjny;
	}

	public void setZxhjny(String zxhjny) {
		this.zxhjny = zxhjny;
	}

	public String getZhzt() {
		return zhzt;
	}

	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
	
	
}
