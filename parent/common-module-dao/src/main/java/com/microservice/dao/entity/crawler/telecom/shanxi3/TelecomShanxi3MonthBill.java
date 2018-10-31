package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  月账单
 * @author sln
 * @date 2017年9月6日 下午8:18:19
 */
@Entity
@Table(name = "telecom_shanxi3_monthbill",indexes = {@Index(name = "index_telecom_shanxi3_monthbill_taskid", columnList = "taskid")})
public class TelecomShanxi3MonthBill extends IdEntity implements Serializable {
	private static final long serialVersionUID = 2053571341410503484L;
	//计费月
	private String countmonth;
	//打印日期
	private String printdate;
//	手机号
	private String phonenum;
//	费用名称
	private String expensename;
//	优惠前（元）
	private String beforediscount;
//	优惠费用（元）
	private String discount;
//	费用小计（元）
	private String totalcost; 
	private String taskid;
	public String getCountmonth() {
		return countmonth;
	}
	public void setCountmonth(String countmonth) {
		this.countmonth = countmonth;
	}
	public String getPrintdate() {
		return printdate;
	}
	public void setPrintdate(String printdate) {
		this.printdate = printdate;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getExpensename() {
		return expensename;
	}
	public void setExpensename(String expensename) {
		this.expensename = expensename;
	}
	public String getBeforediscount() {
		return beforediscount;
	}
	public void setBeforediscount(String beforediscount) {
		this.beforediscount = beforediscount;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getTotalcost() {
		return totalcost;
	}
	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TelecomShanxi3MonthBill() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3MonthBill(String countmonth, String printdate, String phonenum, String expensename,
			String beforediscount, String discount, String totalcost, String taskid) {
		super();
		this.countmonth = countmonth;
		this.printdate = printdate;
		this.phonenum = phonenum;
		this.expensename = expensename;
		this.beforediscount = beforediscount;
		this.discount = discount;
		this.totalcost = totalcost;
		this.taskid = taskid;
	}
	
}

