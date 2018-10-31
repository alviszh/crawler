package com.microservice.dao.entity.crawler.housing.luoyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="housing_luoyang_pay",indexes = {@Index(name = "index_housing_luoyang_pay_taskid", columnList = "taskid")})
public class HousingLuoYangPay extends IdEntity implements Serializable{

	private String years;            //缴存年月
	private String time;             //入账时间
    private String companyName;      //单位名称
    private String type;             //业务类型
	private String personal;         //个人月缴存额(元)
	private String company;          //单位月缴存额(元)
	private String total;              //合计月缴存额(元)
	
	private String taskid;
	
	@Override
	public String toString() {
		return "HousingLuoYangPay [years=" + years + ", time=" + time + ", companyName=" + companyName
				+ ", type=" + type + ", personal=" + personal + ", company=" + company
				+ ", total=" + total + ", taskid=" + taskid + "]";
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
