package com.microservice.dao.entity.crawler.insurance.nanyang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_nanyang_endowment",indexes = {@Index(name = "index_insurance_nanyang_endowment_taskid", columnList = "taskid")}) 
public class InsuranceNanYangEndowment extends IdEntity{

private String taskid;
	
	private String year;
	
	private String base;

	@Override
	public String toString() {
		return "InsurancePingDingShanEndowment [taskid=" + taskid + ", year=" + year + ", base=" + base + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
}
