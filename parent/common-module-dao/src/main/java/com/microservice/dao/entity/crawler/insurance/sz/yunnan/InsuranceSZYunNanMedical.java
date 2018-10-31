package com.microservice.dao.entity.crawler.insurance.sz.yunnan;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_sz_yunnan_medical",indexes = {@Index(name = "index_insurance_sz_yunnan_medical_taskid", columnList = "taskid")}) 
public class InsuranceSZYunNanMedical extends IdEntity{
	private String taskid;							//uuid 前端通过uuid访问状态结果
	
	private String datea;//对应期号
	
	private String dateaIn;//费款所属期号
	
	private String base;//缴费基数
	
	private String personal;//个人缴费划入
	
	private String company;//单位缴费划入
	
	private String sum;//划入合计
	
	private String lastDatea;//到账时间

	@Override
	public String toString() {
		return "InsuranceSZYunNanMedical [taskid=" + taskid + ", datea=" + datea + ", dateaIn=" + dateaIn + ", base="
				+ base + ", personal=" + personal + ", company=" + company + ", sum=" + sum + ", lastDatea=" + lastDatea
				+ "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getDatea() {
		return datea;
	}

	public void setDatea(String datea) {
		this.datea = datea;
	}

	public String getDateaIn() {
		return dateaIn;
	}

	public void setDateaIn(String dateaIn) {
		this.dateaIn = dateaIn;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getSum() {
		return sum;
	}

	public void setSum(String sum) {
		this.sum = sum;
	}

	public String getLastDatea() {
		return lastDatea;
	}

	public void setLastDatea(String lastDatea) {
		this.lastDatea = lastDatea;
	}
	
	

}
