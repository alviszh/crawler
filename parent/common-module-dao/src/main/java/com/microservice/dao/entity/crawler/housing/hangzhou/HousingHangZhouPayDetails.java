package com.microservice.dao.entity.crawler.housing.hangzhou;

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
@Table(name  ="housing_hangzhou_paydetails",indexes = {@Index(name = "index_housing_hangzhou_paydetails_taskid", columnList = "taskid")})
public class HousingHangZhouPayDetails extends IdEntity implements Serializable{
	private String date;        //业务年月
	private String type;        //缴存类型
	private String company;         //月缴存额单位
	private String personal;        //月缴存额个人
	private String total;           //月缴存额合计
	private String money;        //补缴金额
	private String accounted;    //是否入账
	private Integer userid;

	private String taskid;
	@Override
	public String toString() {
		return "HousingHangZhouPayDetails [date=" + date + ",type=" + type
				+ ", company=" + company + ", personal=" + personal + ", total="
				+ total+ ", money=" + money + ", accounted=" + accounted
				+ ", userid=" + userid + ", taskid=" + taskid + "]";
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPersonal() {
		return personal;
	}
	public void setPersonal(String personal) {
		this.personal = personal;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAccounted() {
		return accounted;
	}
	public void setAccounted(String accounted) {
		this.accounted = accounted;
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
	
	
	

}
