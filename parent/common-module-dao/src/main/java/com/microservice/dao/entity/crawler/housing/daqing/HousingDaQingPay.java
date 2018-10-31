package com.microservice.dao.entity.crawler.housing.daqing;

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
@Table(name  ="housing_daqing_pay",indexes = {@Index(name = "index_housing_daqing_pay_taskid", columnList = "taskid")})
public class HousingDaQingPay  extends IdEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String xuhao;
	private String startdate;//发生日期
	private String abstracttxt ;//摘要
	
	private String drawnum;//支取额
	private String incomenum;//收入额
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
	
	public String getDrawnum() {
		return drawnum;
	}

	public void setDrawnum(String drawnum) {
		this.drawnum = drawnum;
	}

	public String getIncomenum() {
		return incomenum;
	}

	public void setIncomenum(String incomenum) {
		this.incomenum = incomenum;
	}

	@Override
	public String toString() {
		return "HousingDaQingPay [xuhao=" + xuhao + ", startdate=" + startdate + ", abstracttxt=" + abstracttxt
				+ ", drawnum=" + drawnum + ", incomenum=" + incomenum + ", balance=" + balance + ", userid=" + userid
				+ ", taskid=" + taskid + "]";
	}

	

}
