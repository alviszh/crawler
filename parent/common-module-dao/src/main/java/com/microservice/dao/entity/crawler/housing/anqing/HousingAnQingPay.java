package com.microservice.dao.entity.crawler.housing.anqing;

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
@Table(name  ="housing_anqing_pay",indexes = {@Index(name = "index_housing_anqing_pay_taskid", columnList = "taskid")})
public class HousingAnQingPay  extends IdEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String xuhao;
	private String abstracttxt ;//摘要
	private String startdate;//发生日期
	private String debitenum;//借方金额
	private String creditnum;//贷方金额
	private String balance;//余额(元)
	
	private Integer userid;

	private String taskid;

	public String getXuhao() {
		return xuhao;
	}

	public void setXuhao(String xuhao) {
		this.xuhao = xuhao;
	}

	public String getAbstracttxt() {
		return abstracttxt;
	}

	public void setAbstracttxt(String abstracttxt) {
		this.abstracttxt = abstracttxt;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getDebitenum() {
		return debitenum;
	}

	public void setDebitenum(String debitenum) {
		this.debitenum = debitenum;
	}

	public String getCreditnum() {
		return creditnum;
	}

	public void setCreditnum(String creditnum) {
		this.creditnum = creditnum;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
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
		return "HousingAnQingPay [xuhao=" + xuhao + ", abstracttxt=" + abstracttxt + ", startdate=" + startdate
				+ ", debitenum=" + debitenum + ", creditnum=" + creditnum + ", balance=" + balance + ", userid="
				+ userid + ", taskid=" + taskid + "]";
	}

}
