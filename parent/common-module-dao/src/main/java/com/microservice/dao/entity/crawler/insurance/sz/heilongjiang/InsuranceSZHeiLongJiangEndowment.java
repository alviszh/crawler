package com.microservice.dao.entity.crawler.insurance.sz.heilongjiang;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_sz_heilongjiang_endowment",indexes = {@Index(name = "index_insurance_sz_heilongjiang_endowment_taskid", columnList = "taskid")}) 
public class InsuranceSZHeiLongJiangEndowment extends IdEntity{

	private String taskid;

	@Override
	public String toString() {
		return "InsuranceSZHeiLongJiangEndowment [taskid=" + taskid + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
