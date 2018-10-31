package com.microservice.dao.entity.crawler.housing.dongguan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存明细
 * @author tz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_dongguan_pay" ,indexes = {@Index(name = "index_housing_dongguan_pay_taskid", columnList = "taskid")})
public class HousingDongguanPay extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;

	/**
	 * 日期
	 */
	private String acctDate;

	/**
	 * 摘要
	 */
	private String absCodeName;

	/**
	 * 发生额（元）
	 */
	private String ttlAmt;

	/**
	 * 余额
	 */
	private String curBal;

	/**
	 * 汇缴年月
	 */
	private String payYmon;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAcctDate() {
		return acctDate;
	}

	public void setAcctDate(String acctDate) {
		this.acctDate = acctDate;
	}

	public String getAbsCodeName() {
		return absCodeName;
	}

	public void setAbsCodeName(String absCodeName) {
		this.absCodeName = absCodeName;
	}

	public String getTtlAmt() {
		return ttlAmt;
	}

	public void setTtlAmt(String ttlAmt) {
		this.ttlAmt = ttlAmt;
	}

	public String getCurBal() {
		return curBal;
	}

	public void setCurBal(String curBal) {
		this.curBal = curBal;
	}

	public String getPayYmon() {
		return payYmon;
	}

	public void setPayYmon(String payYmon) {
		this.payYmon = payYmon;
	}

	@Override
	public String toString() {
		return "HousingDongguanPay [taskid=" + taskid + ", acctDate=" + acctDate + ", absCodeName=" + absCodeName
				+ ", ttlAmt=" + ttlAmt + ", curBal=" + curBal + ", payYmon=" + payYmon + "]";
	}

	public HousingDongguanPay(String taskid, String acctDate, String absCodeName, String ttlAmt, String curBal,
			String payYmon) {
		super();
		this.taskid = taskid;
		this.acctDate = acctDate;
		this.absCodeName = absCodeName;
		this.ttlAmt = ttlAmt;
		this.curBal = curBal;
		this.payYmon = payYmon;
	}

	public HousingDongguanPay() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
