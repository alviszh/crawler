package com.microservice.dao.entity.crawler.housing.changchun;

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
@Table(name = "housing_changchun_pay",indexes = {@Index(name = "index_housing_changchun_pay_taskid", columnList = "taskid")})
public class HousingChangChunPay extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	private String yearmonth;//时间

	private String accnum;//个人账户

	private String amt;//发生额

	private String ywtype;//缴费方式

	private String unitaccname;//缴费公司

	private String trandate;//缴费日期

	private String unitaccnum;//单位账号

	private String accname;//姓名

	private String bal;//余额
	
	private Integer userid;

	private String taskid;

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getYearmonth() {
		return this.yearmonth;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

	public String getAccnum() {
		return this.accnum;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getAmt() {
		return this.amt;
	}

	public void setYwtype(String ywtype) {
		this.ywtype = ywtype;
	}

	public String getYwtype() {
		return this.ywtype;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getUnitaccname() {
		return this.unitaccname;
	}

	public void setTrandate(String trandate) {
		this.trandate = trandate;
	}

	public String getTrandate() {
		return this.trandate;
	}

	public void setUnitaccnum(String unitaccnum) {
		this.unitaccnum = unitaccnum;
	}

	public String getUnitaccnum() {
		return this.unitaccnum;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getAccname() {
		return this.accname;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getBal() {
		return this.bal;
	}

	@Override
	public String toString() {
		return "HousingChangChunPay [yearmonth=" + yearmonth + ", accnum=" + accnum + ", amt=" + amt + ", ywtype="
				+ ywtype + ", unitaccname=" + unitaccname + ", trandate=" + trandate + ", unitaccnum=" + unitaccnum
				+ ", accname=" + accname + ", bal=" + bal + ", userid=" + userid + ", taskid=" + taskid + "]";
	}

}
