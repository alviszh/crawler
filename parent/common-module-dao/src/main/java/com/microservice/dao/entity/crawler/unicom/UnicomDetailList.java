/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.unicom;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_historyresult",indexes = {@Index(name = "index_unicom_historyresult_taskid", columnList = "taskid")})
public class UnicomDetailList extends IdEntity {

	private String name;// 费用类型
	private String value;// 费用值

	private String adjustafter;//
	private String adjustbefore;
	private String balance;
	private String discnt;
	private String fee;//费用值2
	private String integrateitem;//费用类型2
	private String integrateitemcode;//费用类型code2
	private String parentitemcode;
	private String usedvalue;
	
	private String month;

	private Integer userid;

	private String taskid;

	@JsonBackReference
	private List<UnicomDetailList> detailList;

	@JsonBackReference
	private UnicomDetailList detailList2;
	
	public String getAdjustafter() {
		return adjustafter;
	}

	public void setAdjustafter(String adjustafter) {
		this.adjustafter = adjustafter;
	}

	public String getAdjustbefore() {
		return adjustbefore;
	}

	public void setAdjustbefore(String adjustbefore) {
		this.adjustbefore = adjustbefore;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getDiscnt() {
		return discnt;
	}

	public void setDiscnt(String discnt) {
		this.discnt = discnt;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getIntegrateitem() {
		return integrateitem;
	}

	public void setIntegrateitem(String integrateitem) {
		this.integrateitem = integrateitem;
	}

	public String getIntegrateitemcode() {
		return integrateitemcode;
	}

	public void setIntegrateitemcode(String integrateitemcode) {
		this.integrateitemcode = integrateitemcode;
	}

	public String getParentitemcode() {
		return parentitemcode;
	}

	public void setParentitemcode(String parentitemcode) {
		this.parentitemcode = parentitemcode;
	}

	public String getUsedvalue() {
		return usedvalue;
	}

	public void setUsedvalue(String usedvalue) {
		this.usedvalue = usedvalue;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unicomdetaillist_id")
	@JsonIgnore
	public UnicomDetailList getDetailList2() {
		return detailList2;
	}

	public void setDetailList2(UnicomDetailList detailList2) {
		this.detailList2 = detailList2;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "detailList2")
	public List<UnicomDetailList> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<UnicomDetailList> detailList) {
		this.detailList = detailList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	

}