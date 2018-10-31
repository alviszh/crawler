package com.microservice.dao.entity.crawler.housing.changchun;

import java.io.Serializable;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Index;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_changchun_userinfo",indexes = {@Index(name = "index_housing_changchun_userinfo_taskid", columnList = "taskid")})
public class HousingChangChunUserInfo extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String lasttransdate;//最后交易日期

	private String certinum;//身份证号

	private String opnaccdate;//开户日期

	private String unitpayamt;//单位缴存金额

	private String accnum;//个人公积金账号

	private String accname1;//账户状态

	private String unitprop;//单位缴存比例

	private String unitaccname;//单位名称

	private String unitaccnum;//单位账号

	private String indipayamt;//个人缴存

	private String accname;//姓名

	private String indiprop;//个人缴存比例

	private String indipaysum;//月缴存金额

	private String bal;//总余额

	private String instname;//所辖机构

	private String lpaym;//交至年月

	private String basenum;//缴存基数
	
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

	private Integer userid;

	private String taskid;

	public void setLasttransdate(String lasttransdate) {
		this.lasttransdate = lasttransdate;
	}

	public String getLasttransdate() {
		return this.lasttransdate;
	}

	public void setCertinum(String certinum) {
		this.certinum = certinum;
	}

	public String getCertinum() {
		return this.certinum;
	}

	public void setOpnaccdate(String opnaccdate) {
		this.opnaccdate = opnaccdate;
	}

	public String getOpnaccdate() {
		return this.opnaccdate;
	}

	public void setUnitpayamt(String unitpayamt) {
		this.unitpayamt = unitpayamt;
	}

	public String getUnitpayamt() {
		return this.unitpayamt;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getAccnum() {
		return this.accnum;
	}

	public void setAccname1(String accname1) {
		this.accname1 = accname1;
	}

	public String getAccname1() {
		return this.accname1;
	}

	public void setUnitprop(String unitprop) {
		this.unitprop = unitprop;
	}

	public String getUnitprop() {
		return this.unitprop;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getUnitaccname() {
		return this.unitaccname;
	}

	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}

	public String getUnitaccnum() {
		return this.unitaccnum;
	}

	public void setIndipayamt(String indipayamt) {
		this.indipayamt = indipayamt;
	}

	public String getIndipayamt() {
		return this.indipayamt;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getAccname() {
		return this.accname;
	}

	public void setIndiprop(String indiprop) {
		this.indiprop = indiprop;
	}

	public String getIndiprop() {
		return this.indiprop;
	}

	public void setIndipaysum(String indipaysum) {
		this.indipaysum = indipaysum;
	}

	public String getIndipaysum() {
		return this.indipaysum;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getBal() {
		return this.bal;
	}

	public void setInstname(String instname) {
		this.instname = instname;
	}

	public String getInstname() {
		return this.instname;
	}

	public void setLpaym(String lpaym) {
		this.lpaym = lpaym;
	}

	public String getLpaym() {
		return this.lpaym;
	}

	public void setBasenum(String basenum) {
		this.basenum = basenum;
	}

	public String getBasenum() {
		return this.basenum;
	}

	@Override
	public String toString() {
		return "HousingChangChunUserInfo [lasttransdate=" + lasttransdate + ", certinum=" + certinum + ", opnaccdate="
				+ opnaccdate + ", unitpayamt=" + unitpayamt + ", accnum=" + accnum + ", accname1=" + accname1
				+ ", unitprop=" + unitprop + ", unitaccname=" + unitaccname + ", unitaccnum=" + unitaccnum
				+ ", indipayamt=" + indipayamt + ", accname=" + accname + ", indiprop=" + indiprop + ", indipaysum="
				+ indipaysum + ", bal=" + bal + ", instname=" + instname + ", lpaym=" + lpaym + ", basenum=" + basenum
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
}
