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
@Table(name="housing_panzhihua_base")
public class HousingPanzhihuaBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	//单位名称
	@Column(name="dwmc")
	private String dwmc;		
	
	//单位账号
	@Column(name="dwzh")
	private String dwzh;		
	
	//缴存管理部
	@Column(name="jcglb")
	private String jcglb;		
	
	//缴存银行
	@Column(name="jcyh")
	private String jcyh;		
	
	//缴存基数
	@Column(name="jcjs")
	private String jcjs;		
	
	//缴存比例
	@Column(name="jcbl")
	private String jcbl;		
	
	//工资基数
	@Column(name="gzjs")
	private String gzjs;		
	
	//月汇缴额
	@Column(name="yhje")
	private String yhje;		
	
	//缴存余额
	@Column(name="jcye")
	private String jcye;		
	
	//账户状态
	@Column(name="zhzt")
	private String zhzt;		
	
	//开户日期
	@Column(name="khrq")
	private String khrq;		
	
	//缴至年月
	@Column(name="jzny")
	private String jzny;		
	
	//绑定银行
	@Column(name="bdyh")
	private String bdyh;		
	
	//绑定银行卡号
	@Column(name="bdyhkh")
	private String bdyhkh;

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

	public String getJcglb() {
		return jcglb;
	}

	public void setJcglb(String jcglb) {
		this.jcglb = jcglb;
	}

	public String getJcyh() {
		return jcyh;
	}

	public void setJcyh(String jcyh) {
		this.jcyh = jcyh;
	}

	public String getJcbl() {
		return jcbl;
	}

	public void setJcbl(String jcbl) {
		this.jcbl = jcbl;
	}

	public String getGzjs() {
		return gzjs;
	}

	public void setGzjs(String gzjs) {
		this.gzjs = gzjs;
	}

	public String getYhje() {
		return yhje;
	}

	public void setYhje(String yhje) {
		this.yhje = yhje;
	}

	public String getJcye() {
		return jcye;
	}

	public void setJcye(String jcye) {
		this.jcye = jcye;
	}

	public String getZhzt() {
		return zhzt;
	}

	public void setZhzt(String zhzt) {
		this.zhzt = zhzt;
	}

	public String getKhrq() {
		return khrq;
	}

	public void setKhrq(String khrq) {
		this.khrq = khrq;
	}

	public String getJzny() {
		return jzny;
	}

	public void setJzny(String jzny) {
		this.jzny = jzny;
	}

	public String getBdyh() {
		return bdyh;
	}

	public void setBdyh(String bdyh) {
		this.bdyh = bdyh;
	}

	public String getBdyhkh() {
		return bdyhkh;
	}

	public void setBdyhkh(String bdyhkh) {
		this.bdyhkh = bdyhkh;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getJcjs() {
		return jcjs;
	}

	public void setJcjs(String jcjs) {
		this.jcjs = jcjs;
	}	
	
}
