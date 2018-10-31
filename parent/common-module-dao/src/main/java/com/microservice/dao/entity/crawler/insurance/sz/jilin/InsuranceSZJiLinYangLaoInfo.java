package com.microservice.dao.entity.crawler.insurance.sz.jilin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_sz_jilin_yanglaoinfo",indexes = {@Index(name = "index_insurance_sz_jilin_yanglaoinfo_taskid", columnList = "taskid")})
public class InsuranceSZJiLinYangLaoInfo extends IdEntity{

	private String taskid;
	
	private String name;
	
	private String idcard;
	
	private String feeperiod;//费款所属期
	
	private String insurancetype;//险种类型
	
	private String paytype;//缴费类型
	
	private String paybase;//缴费基数
	
	private String perpaymoney;//个人缴费金额
	
	private String perpaysign;//个人缴费标志
	
	private String perpaydatetime;//个人缴费到账日期
	
	private String dwnummoney;//单位缴费划账户金额
	
	private String dwcrossmoney;//单位缴费划统筹金额
	
	private String dwpaysnig;//单位缴费标志
	
	private String dwdatetime;//单位缴费到账日期

	private String month;//缴费月数
	
	private String dwnum;//缴费单位编号
	
	private String dwname;//缴费单位名称

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getFeeperiod() {
		return feeperiod;
	}

	public void setFeeperiod(String feeperiod) {
		this.feeperiod = feeperiod;
	}

	public String getInsurancetype() {
		return insurancetype;
	}

	public void setInsurancetype(String insurancetype) {
		this.insurancetype = insurancetype;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getPaybase() {
		return paybase;
	}

	public void setPaybase(String paybase) {
		this.paybase = paybase;
	}

	public String getPerpaymoney() {
		return perpaymoney;
	}

	public void setPerpaymoney(String perpaymoney) {
		this.perpaymoney = perpaymoney;
	}

	public String getPerpaysign() {
		return perpaysign;
	}

	public void setPerpaysign(String perpaysign) {
		this.perpaysign = perpaysign;
	}

	public String getPerpaydatetime() {
		return perpaydatetime;
	}

	public void setPerpaydatetime(String perpaydatetime) {
		this.perpaydatetime = perpaydatetime;
	}

	public String getDwnummoney() {
		return dwnummoney;
	}

	public void setDwnummoney(String dwnummoney) {
		this.dwnummoney = dwnummoney;
	}

	public String getDwcrossmoney() {
		return dwcrossmoney;
	}

	public void setDwcrossmoney(String dwcrossmoney) {
		this.dwcrossmoney = dwcrossmoney;
	}

	public String getDwpaysnig() {
		return dwpaysnig;
	}

	public void setDwpaysnig(String dwpaysnig) {
		this.dwpaysnig = dwpaysnig;
	}

	public String getDwdatetime() {
		return dwdatetime;
	}

	public void setDwdatetime(String dwdatetime) {
		this.dwdatetime = dwdatetime;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDwnum() {
		return dwnum;
	}

	public void setDwnum(String dwnum) {
		this.dwnum = dwnum;
	}

	public String getDwname() {
		return dwname;
	}

	public void setDwname(String dwname) {
		this.dwname = dwname;
	}

	public InsuranceSZJiLinYangLaoInfo(String taskid, String name, String idcard, String feeperiod,
			String insurancetype, String paytype, String paybase, String perpaymoney, String perpaysign,
			String perpaydatetime, String dwnummoney, String dwcrossmoney, String dwpaysnig, String dwdatetime,
			String month, String dwnum, String dwname) {
		super();
		this.taskid = taskid;
		this.name = name;
		this.idcard = idcard;
		this.feeperiod = feeperiod;
		this.insurancetype = insurancetype;
		this.paytype = paytype;
		this.paybase = paybase;
		this.perpaymoney = perpaymoney;
		this.perpaysign = perpaysign;
		this.perpaydatetime = perpaydatetime;
		this.dwnummoney = dwnummoney;
		this.dwcrossmoney = dwcrossmoney;
		this.dwpaysnig = dwpaysnig;
		this.dwdatetime = dwdatetime;
		this.month = month;
		this.dwnum = dwnum;
		this.dwname = dwname;
	}

	public InsuranceSZJiLinYangLaoInfo() {
		super();
	}

	@Override
	public String toString() {
		return "InsuranceSZJiLinYangLaoInfo [taskid=" + taskid + ", name=" + name + ", idcard=" + idcard
				+ ", feeperiod=" + feeperiod + ", insurancetype=" + insurancetype + ", paytype=" + paytype
				+ ", paybase=" + paybase + ", perpaymoney=" + perpaymoney + ", perpaysign=" + perpaysign
				+ ", perpaydatetime=" + perpaydatetime + ", dwnummoney=" + dwnummoney + ", dwcrossmoney=" + dwcrossmoney
				+ ", dwpaysnig=" + dwpaysnig + ", dwdatetime=" + dwdatetime + ", month=" + month + ", dwnum=" + dwnum
				+ ", dwname=" + dwname + "]";
	}

	
}
