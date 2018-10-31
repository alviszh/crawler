package com.microservice.dao.entity.crawler.insurance.sz.guangxi;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
/**
 * 广西基本保险类型
 * @author zcx
 *
 */
@Entity
@Table(name="insurance_sz_guangxi_basictypes")
public class InsuranceSZGuangXibasictypes extends IdEntity {
	
	private String companyname;//单位名称
	private String type;//险种类型
	private String state;//险种状态
	private String beginmonth;//起始年月
	private String endmonth;//截止年月
	private String taskid;//
	
	
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBeginmonth() {
		return beginmonth;
	}
	public void setBeginmonth(String beginmonth) {
		this.beginmonth = beginmonth;
	}
	public String getEndmonth() {
		return endmonth;
	}
	public void setEndmonth(String endmonth) {
		this.endmonth = endmonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public InsuranceSZGuangXibasictypes(String companyname, String type, String state, String beginmonth,
			String endmonth, String taskid) {
		super();
		this.companyname = companyname;
		this.type = type;
		this.state = state;
		this.beginmonth = beginmonth;
		this.endmonth = endmonth;
		this.taskid = taskid;
	}
	public InsuranceSZGuangXibasictypes() {
		super();
	}
}
