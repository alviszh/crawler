package com.microservice.dao.entity.crawler.housing.beijing;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_beijing_userinfo",indexes = {@Index(name = "index_housing_beijing_userinfo_taskid", columnList = "taskid")})
public class HousingBeiJingUserinfo  extends IdEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name  ;//姓名
	private String personnum   ;//个人登记号
	private String idtype  ;//证件类型
	private String idnum  ;//证件号
	private String companynum  ;//单位登记号
	private String companyname  ;//单位名称
	private String managementnum  ;//所属管理部编号
	private String managementname  ;//所属管理部名称
	private String balance  ;//当前余额
	private String accountstatus  ;//帐户状态
	private String payyear  ;//当年缴存金额
	private String fetchyear  ;//当年提取金额
	private String payoldyear  ;//上年结转余额
	private String enddate;// ;//最后业务日期
	private String transfer ;//转出金额
	
	private Integer userid;

	private String taskid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonnum() {
		return personnum;
	}

	public void setPersonnum(String personnum) {
		this.personnum = personnum;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getCompanynum() {
		return companynum;
	}

	public void setCompanynum(String companynum) {
		this.companynum = companynum;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getManagementnum() {
		return managementnum;
	}

	public void setManagementnum(String managementnum) {
		this.managementnum = managementnum;
	}

	public String getManagementname() {
		return managementname;
	}

	public void setManagementname(String managementname) {
		this.managementname = managementname;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(String accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getPayyear() {
		return payyear;
	}

	public void setPayyear(String payyear) {
		this.payyear = payyear;
	}

	public String getFetchyear() {
		return fetchyear;
	}

	public void setFetchyear(String fetchyear) {
		this.fetchyear = fetchyear;
	}

	public String getPayoldyear() {
		return payoldyear;
	}

	public void setPayoldyear(String payoldyear) {
		this.payoldyear = payoldyear;
	}


	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}

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

	@Override
	public String toString() {
		return "HousingBeiJingUserinfo [name=" + name + ", personnum=" + personnum + ", idtype=" + idtype + ", idnum="
				+ idnum + ", companynum=" + companynum + ", companyname=" + companyname + ", managementnum="
				+ managementnum + ", managementname=" + managementname + ", balance=" + balance + ", accountstatus="
				+ accountstatus + ", payyear=" + payyear + ", fetchyear=" + fetchyear + ", payoldyear=" + payoldyear
				+ ", enddate=" + enddate + ", transfer=" + transfer + ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	
	

}
