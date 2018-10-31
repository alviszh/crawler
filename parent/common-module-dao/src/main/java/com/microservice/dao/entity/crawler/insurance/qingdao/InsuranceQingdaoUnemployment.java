package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 青岛失业保险信息实体
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_unemployment",indexes = {@Index(name = "index_insurance_qingdao_unemployment_taskid", columnList = "taskid")})
public class InsuranceQingdaoUnemployment extends IdEntity implements Serializable{
	private static final long serialVersionUID = -3379357909228512343L;
	private String taskid;
//	缴费年月
	private String paydate;
//	应属年月
	private String belongdate;
//	单位名称
	private String compname;
//	缴费类别
	private String paytype;
//	缴费基数
	private String paybasenum;
//	个人缴费
	private String perpay;
//	缴费标志
	private String paystatus;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPaydate() {
		return paydate;
	}
	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}
	public String getBelongdate() {
		return belongdate;
	}
	public void setBelongdate(String belongdate) {
		this.belongdate = belongdate;
	}
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPaybasenum() {
		return paybasenum;
	}
	public void setPaybasenum(String paybasenum) {
		this.paybasenum = paybasenum;
	}
	public String getPerpay() {
		return perpay;
	}
	public void setPerpay(String perpay) {
		this.perpay = perpay;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public InsuranceQingdaoUnemployment() {
		super();
	}
	public InsuranceQingdaoUnemployment(String taskid, String paydate, String belongdate, String compname,
			String paytype, String paybasenum, String perpay, String paystatus) {
		super();
		this.taskid = taskid;
		this.paydate = paydate;
		this.belongdate = belongdate;
		this.compname = compname;
		this.paytype = paytype;
		this.paybasenum = paybasenum;
		this.perpay = perpay;
		this.paystatus = paystatus;
	}
}
