package com.microservice.dao.entity.crawler.insurance.sz.yunnan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_yunnan_endowment",indexes = {@Index(name = "index_insurance_sz_yunnan_endowment_taskid", columnList = "taskid")}) 
public class InsuranceSZYunNanEndowment extends IdEntity{
	private String taskid;							//uuid 前端通过uuid访问状态结果
	
	private String dateaIn;//费款所属期号
	private String datea;//缴费月份
	
	private String type;//缴费类型
	
	private String base;//缴费基数
	
	private String personal;//个人缴费金额
	private String personalDate;//个人缴费到账日期
	
	private String company;//单位名称

	@Override
	public String toString() {
		return "InsuranceSZYunNanEndowment [taskid=" + taskid + ", dateaIn=" + dateaIn + ", datea=" + datea + ", type="
				+ type + ", base=" + base + ", personal=" + personal + ", personalDate=" + personalDate + ", company="
				+ company + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDateaIn() {
		return dateaIn;
	}

	public void setDateaIn(String dateaIn) {
		this.dateaIn = dateaIn;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public String getPersonalDate() {
		return personalDate;
	}

	public void setPersonalDate(String personalDate) {
		this.personalDate = personalDate;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	

}
