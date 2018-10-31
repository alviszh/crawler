package com.microservice.dao.entity.crawler.insurance.qingdao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * @Description: 青岛医疗现就职公司信息实体
 * @author sln
 * @date 2017年8月9日
 */
@Entity
@Table(name = "insurance_qingdao_compinfo",indexes = {@Index(name = "index_insurance_qingdao_compinfo_taskid", columnList = "taskid")})
public class InsuranceQingdaoCompInfo extends IdEntity implements Serializable{
	private static final long serialVersionUID = 3267057792368956200L;
	private String taskid;
	//险种类型
	private String insurtype;
	//个人缴费状态
	private String paystatus;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getInsurtype() {
		return insurtype;
	}
	public void setInsurtype(String insurtype) {
		this.insurtype = insurtype;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public InsuranceQingdaoCompInfo() {
		super();
	}
	public InsuranceQingdaoCompInfo(String taskid, String insurtype, String paystatus) {
		super();
		this.taskid = taskid;
		this.insurtype = insurtype;
		this.paystatus = paystatus;
	}
}
