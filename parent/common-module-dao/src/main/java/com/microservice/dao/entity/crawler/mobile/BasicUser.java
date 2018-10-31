package com.microservice.dao.entity.crawler.mobile;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="basic_user")
public class BasicUser  extends IdEntity  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133365472766002696L;

	private String name;		//姓名
	
	private String idnum;		//身份证号
	 
	@JsonBackReference
    private List<TaskMobile> taskMobiles;

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
	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="basicUser")
	public List<TaskMobile> getTaskMobiles() {
		return taskMobiles;
	}

	public void setTaskMobiles(List<TaskMobile> taskMobiles) {
		this.taskMobiles = taskMobiles;
	}

	@Override
	public String toString() {
		return "BasicUser [name=" + name + ", idnum=" + idnum + "]";
	}
	
	
	
	
	
	

}
