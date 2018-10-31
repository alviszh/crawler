package com.microservice.dao.entity.crawler.housing.yvlin;

import java.io.Serializable;

/**   
*    
* 项目名称：common-microservice-housingfund-yvlin   
* 类名称：aa   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月12日 下午3:26:51   
* @version        
*/

/**
 * Copyright 2018 bejson.com 
 */
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_yvlin_pay")
public class HousingBasicPayResult extends IdEntity implements Serializable{

	private String unitaccname; //汇缴双边
	private String amt1; //发生额 个人转出等
	private String amt2; //发生额  汇缴入账
	private String amt4; //发生额 比较准确
	private String begindatec; //开始年月
	private String instancenum;
	private Date transdate; //交易日期
	private String freeuse1;
	private String peoplenum;//未知
	private String freeuse4; //摘要
	private String basenumber; //余额
	private String enddatec; //截止年月
	private String freeuse6;
	
	private String taskid;
	
	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setUnitaccname(String unitaccname) {
		this.unitaccname = unitaccname;
	}

	public String getUnitaccname() {
		return unitaccname;
	}

	public void setAmt1(String amt1) {
		this.amt1 = amt1;
	}

	public String getAmt1() {
		return amt1;
	}

	public void setAmt2(String amt2) {
		this.amt2 = amt2;
	}

	public String getAmt2() {
		return amt2;
	}

	public void setAmt4(String amt4) {
		this.amt4 = amt4;
	}

	public String getAmt4() {
		return amt4;
	}

	public void setBegindatec(String begindatec) {
		this.begindatec = begindatec;
	}

	public String getBegindatec() {
		return begindatec;
	}

	public void setInstancenum(String instancenum) {
		this.instancenum = instancenum;
	}

	public String getInstancenum() {
		return instancenum;
	}

	public void setTransdate(Date transdate) {
		this.transdate = transdate;
	}

	public Date getTransdate() {
		return transdate;
	}

	public void setFreeuse1(String freeuse1) {
		this.freeuse1 = freeuse1;
	}

	public String getFreeuse1() {
		return freeuse1;
	}

	public void setPeoplenum(String peoplenum) {
		this.peoplenum = peoplenum;
	}

	public String getPeoplenum() {
		return peoplenum;
	}

	public void setFreeuse4(String freeuse4) {
		this.freeuse4 = freeuse4;
	}

	public String getFreeuse4() {
		return freeuse4;
	}

	public void setBasenumber(String basenumber) {
		this.basenumber = basenumber;
	}

	public String getBasenumber() {
		return basenumber;
	}

	public void setEnddatec(String enddatec) {
		this.enddatec = enddatec;
	}

	public String getEnddatec() {
		return enddatec;
	}

	public void setFreeuse6(String freeuse6) {
		this.freeuse6 = freeuse6;
	}

	public String getFreeuse6() {
		return freeuse6;
	}

	@Override
	public String toString() {
		return "BasicPayResult [unitaccname=" + unitaccname + ", amt1=" + amt1 + ", amt2=" + amt2 + ", amt4=" + amt4
				+ ", begindatec=" + begindatec + ", instancenum=" + instancenum + ", transdate=" + transdate
				+ ", freeuse1=" + freeuse1 + ", peoplenum=" + peoplenum + ", freeuse4=" + freeuse4 + ", basenumber="
				+ basenumber + ", enddatec=" + enddatec + ", freeuse6=" + freeuse6 + "]";
	}

}