package com.microservice.dao.entity.crawler.insurance.liangshanyizu;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_liangshanyizu_baseinfo")
public class InsuranceliangshanyizuBaseInfo extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = -7225639204374657354L;
	

	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 姓名 */
	@Column(name="name")
	private String name;
	
	/** 证件类型 */
	@Column(name="zjlx")
	private String zjlx;
	
	/** 身份证号 */
	@Column(name="sfzh")
	private String sfzh;
	
	/** 性别 */
	@Column(name="sex")
	private String sex;
	
	/** 民族 */
	@Column(name="mz")
	private String mz;
	
	/** 年龄*/
	@Column(name="age")
	private String age;
	
	/** 出生日期*/
	@Column(name="birth")
	private String birth;
	
	/** 参工时间*/
	@Column(name="cgsj")
	private String cgsj;
	
	/** 养老退休状态*/
	@Column(name="yangtxzt")
	private String yangtxzt;
	
	/** 医疗退休状态*/
	@Column(name="yiltxzt")
	private String yiltxzt;
	
	/** 生存状态*/
	@Column(name="sczt")
	private String sczt;
	
	/** 户口性质*/
	@Column(name="hkxz")
	private String hkxz;
	
	/** 地址*/
	@Column(name="addr")
	private String addr;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getZjlx() {
		return zjlx;
	}


	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}


	public String getSfzh() {
		return sfzh;
	}


	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getMz() {
		return mz;
	}


	public void setMz(String mz) {
		this.mz = mz;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
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


	public String getYangtxzt() {
		return yangtxzt;
	}


	public void setYangtxzt(String yangtxzt) {
		this.yangtxzt = yangtxzt;
	}


	public String getYiltxzt() {
		return yiltxzt;
	}


	public void setYiltxzt(String yiltxzt) {
		this.yiltxzt = yiltxzt;
	}


	public String getSczt() {
		return sczt;
	}


	public void setSczt(String sczt) {
		this.sczt = sczt;
	}


	public String getHkxz() {
		return hkxz;
	}


	public void setHkxz(String hkxz) {
		this.hkxz = hkxz;
	}


	public String getAddr() {
		return addr;
	}


	public void setAddr(String addr) {
		this.addr = addr;
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