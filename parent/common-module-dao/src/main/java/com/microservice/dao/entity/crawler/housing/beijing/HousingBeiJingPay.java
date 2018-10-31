package com.microservice.dao.entity.crawler.housing.beijing;

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
@Table(name  ="housing_beijing_pay",indexes = {@Index(name = "index_housing_beijing_pay_taskid", columnList = "taskid")})
public class HousingBeiJingPay  extends IdEntity implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String receiveddate ;//到账日期
	private String settlementdate;//汇补缴年月
	private String businesstype;//业务类型
	private String addnum;//增加额(元)
	private String lessennum;//减少额(元)
	private String balance;//余额(元)
	
	private Integer userid;

	private String taskid;

	public String getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(String receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getSettlementdate() {
		return settlementdate;
	}

	public void setSettlementdate(String settlementdate) {
		this.settlementdate = settlementdate;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getAddnum() {
		return addnum;
	}

	public void setAddnum(String addnum) {
		this.addnum = addnum;
	}

	public String getLessennum() {
		return lessennum;
	}

	public void setLessennum(String lessennum) {
		this.lessennum = lessennum;
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
		return "HousingBeiJingPay [receiveddate=" + receiveddate + ", settlementdate=" + settlementdate
				+ ", businesstype=" + businesstype + ", addnum=" + addnum + ", lessennum=" + lessennum + ", balance="
				+ balance + ", userid=" + userid + ", taskid=" + taskid + "]";
	}	
}
