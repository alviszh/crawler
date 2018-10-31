/**
  * Copyright 2018 bejson.com 
  */
package com.microservice.dao.entity.crawler.housing.changzhou;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * Auto-generated: 2018-02-26 17:40:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_changzhou_basic",indexes = {@Index(name = "index_housing_changzhou_basic_taskid", columnList = "taskid")})
public class HousingChangZhouBasicRows  extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String SPTEL2; //联系电话
	private String SJH; //手机号码
	private String BTHJZTNAME; //"账户未设立" //"账户未设立" 

	private String QYSJH;
	private String SPMFACT;//月缴金额
	private String SNCODE;
	private String SNNAME; //单位名称
	private String SPJYM;////汇缴年月
	private String SPHJSZD;
	private String BTYE;//BT当前余额btye
	private String HJZTNAME;
	private String SPIDNO; //证件号码
	private String SPSINGL;///缴存比例
	private String BTHJNY;//bt汇缴年月 bthjny
	private String SPTEL;
	private String BTHJSTATUS;
	private String BTYJE; //BT月缴金额
	private String SPLXDZ;//联系地址
	private String SPNAME; //职工姓名
	private String SPMEND; //当前余额
	private String SPJCBL;
	private String SPADDR;
	private String SNCODE1;
	private String SPHJXZ;
	private String HJSTATUS;
	private String SPCODE; //职工账号
	private String BTBL;//bt缴存比例
	private String SPGZ;//工资基数
	
	private Integer userid;

	private String taskid;
	
	

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setSPTEL2(String SPTEL2) {
		this.SPTEL2 = SPTEL2;
	}

	public String getSPTEL2() {
		return SPTEL2;
	}

	public void setSJH(String SJH) {
		this.SJH = SJH;
	}

	public String getSJH() {
		return SJH;
	}

	public void setBTHJZTNAME(String BTHJZTNAME) {
		this.BTHJZTNAME = BTHJZTNAME;
	}

	public String getBTHJZTNAME() {
		return BTHJZTNAME;
	}

	public void setQYSJH(String QYSJH) {
		this.QYSJH = QYSJH;
	}

	public String getQYSJH() {
		return QYSJH;
	}

	public void setSPMFACT(String SPMFACT) {
		this.SPMFACT = SPMFACT;
	}

	public String getSPMFACT() {
		return SPMFACT;
	}

	public void setSNCODE(String SNCODE) {
		this.SNCODE = SNCODE;
	}

	public String getSNCODE() {
		return SNCODE;
	}

	public void setSNNAME(String SNNAME) {
		this.SNNAME = SNNAME;
	}

	public String getSNNAME() {
		return SNNAME;
	}

	public void setSPJYM(String SPJYM) {
		this.SPJYM = SPJYM;
	}

	public String getSPJYM() {
		return SPJYM;
	}

	public void setSPHJSZD(String SPHJSZD) {
		this.SPHJSZD = SPHJSZD;
	}

	public String getSPHJSZD() {
		return SPHJSZD;
	}

	public void setBTYE(String BTYE) {
		this.BTYE = BTYE;
	}

	public String getBTYE() {
		return BTYE;
	}

	public void setHJZTNAME(String HJZTNAME) {
		this.HJZTNAME = HJZTNAME;
	}

	public String getHJZTNAME() {
		return HJZTNAME;
	}

	public void setSPIDNO(String SPIDNO) {
		this.SPIDNO = SPIDNO;
	}

	public String getSPIDNO() {
		return SPIDNO;
	}

	public void setSPSINGL(String SPSINGL) {
		this.SPSINGL = SPSINGL;
	}

	public String getSPSINGL() {
		return SPSINGL;
	}

	public void setBTHJNY(String BTHJNY) {
		this.BTHJNY = BTHJNY;
	}

	public String getBTHJNY() {
		return BTHJNY;
	}

	public void setSPTEL(String SPTEL) {
		this.SPTEL = SPTEL;
	}

	public String getSPTEL() {
		return SPTEL;
	}

	public void setBTHJSTATUS(String BTHJSTATUS) {
		this.BTHJSTATUS = BTHJSTATUS;
	}

	public String getBTHJSTATUS() {
		return BTHJSTATUS;
	}

	public void setBTYJE(String BTYJE) {
		this.BTYJE = BTYJE;
	}

	public String getBTYJE() {
		return BTYJE;
	}

	public void setSPLXDZ(String SPLXDZ) {
		this.SPLXDZ = SPLXDZ;
	}

	public String getSPLXDZ() {
		return SPLXDZ;
	}

	public void setSPNAME(String SPNAME) {
		this.SPNAME = SPNAME;
	}

	public String getSPNAME() {
		return SPNAME;
	}

	public void setSPMEND(String SPMEND) {
		this.SPMEND = SPMEND;
	}

	public String getSPMEND() {
		return SPMEND;
	}

	public void setSPJCBL(String SPJCBL) {
		this.SPJCBL = SPJCBL;
	}

	public String getSPJCBL() {
		return SPJCBL;
	}

	public void setSPADDR(String SPADDR) {
		this.SPADDR = SPADDR;
	}

	public String getSPADDR() {
		return SPADDR;
	}

	public void setSNCODE1(String SNCODE1) {
		this.SNCODE1 = SNCODE1;
	}

	public String getSNCODE1() {
		return SNCODE1;
	}

	public void setSPHJXZ(String SPHJXZ) {
		this.SPHJXZ = SPHJXZ;
	}

	public String getSPHJXZ() {
		return SPHJXZ;
	}

	public void setHJSTATUS(String HJSTATUS) {
		this.HJSTATUS = HJSTATUS;
	}

	public String getHJSTATUS() {
		return HJSTATUS;
	}

	public void setSPCODE(String SPCODE) {
		this.SPCODE = SPCODE;
	}

	public String getSPCODE() {
		return SPCODE;
	}

	public void setBTBL(String BTBL) {
		this.BTBL = BTBL;
	}

	public String getBTBL() {
		return BTBL;
	}

	public void setSPGZ(String SPGZ) {
		this.SPGZ = SPGZ;
	}

	public String getSPGZ() {
		return SPGZ;
	}

	@Override
	public String toString() {
		return "HousingChangZhouBasicRows [SPTEL2=" + SPTEL2 + ", SJH=" + SJH + ", BTHJZTNAME=" + BTHJZTNAME
				+ ", QYSJH=" + QYSJH + ", SPMFACT=" + SPMFACT + ", SNCODE=" + SNCODE + ", SNNAME=" + SNNAME + ", SPJYM="
				+ SPJYM + ", SPHJSZD=" + SPHJSZD + ", BTYE=" + BTYE + ", HJZTNAME=" + HJZTNAME + ", SPIDNO=" + SPIDNO
				+ ", SPSINGL=" + SPSINGL + ", BTHJNY=" + BTHJNY + ", SPTEL=" + SPTEL + ", BTHJSTATUS=" + BTHJSTATUS
				+ ", BTYJE=" + BTYJE + ", SPLXDZ=" + SPLXDZ + ", SPNAME=" + SPNAME + ", SPMEND=" + SPMEND + ", SPJCBL="
				+ SPJCBL + ", SPADDR=" + SPADDR + ", SNCODE1=" + SNCODE1 + ", SPHJXZ=" + SPHJXZ + ", HJSTATUS="
				+ HJSTATUS + ", SPCODE=" + SPCODE + ", BTBL=" + BTBL + ", SPGZ=" + SPGZ + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

	
}