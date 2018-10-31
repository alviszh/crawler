package com.microservice.dao.entity.crawler.housing.basic;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "basic_user_housing")
public class BasicUserHousing extends IdEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133365472766002696L;

	private String name; // 姓名

	private String idnum; // 身份证号

	@JsonBackReference
	private List<TaskHousing> taskHousing;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	@JsonBackReference
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "basicUserHousing")
	public List<TaskHousing> getTaskHousing() {
		return taskHousing;
	}

	public void setTaskHousing(List<TaskHousing> taskHousing) {
		this.taskHousing = taskHousing;
	}

}
