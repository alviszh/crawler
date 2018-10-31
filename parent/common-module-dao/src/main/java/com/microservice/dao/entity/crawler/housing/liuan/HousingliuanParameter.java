package com.microservice.dao.entity.crawler.housing.liuan;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_liuan_parameter")
public class HousingliuanParameter extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	
	@Column(name="taskid")
	private String taskid;		

	@Column(name="parameter")
	private String parameter;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
