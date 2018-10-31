package com.microservice.dao.entity.crawler.bank.citicchina;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="citicchina_creditcard_code",indexes = {@Index(name = "index_citicchina_creditcard_code_taskid", columnList = "taskid")}) 
public class CiticChinaCreditCardCode extends IdEntity implements Serializable{

	private String taskid;
	
	private String code;

	@Override
	public String toString() {
		return "CiticChinaCreditCardCode [taskid=" + taskid + ", code=" + code + "]";
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
