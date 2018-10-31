package com.microservice.dao.entity.crawler.bank.basic;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.microservice.dao.entity.IdEntity;

/**
 * 银行用户表
 * @author zz
 *
 */
@Entity
@Table(name="basic_user_bank")
public class BasicUserBank extends IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5133365472766002696L;
	
	private String name;		//姓名
	
	private String idnum;		//身份证号
	
	private Integer auth;		//认证  
	
//	@JsonManagedReference
	@JsonBackReference
    private List<TaskBank> taskBank;

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

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

	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="basicUserBank")
	public List<TaskBank> getTaskBank() {
		return taskBank;
	}

	public void setTaskBank(List<TaskBank> taskBank) {
		this.taskBank = taskBank;
	}

	@Override
	public String toString() {
		return "BasicUserBank [name=" + name + ", idnum=" + idnum + ", auth=" + auth + "]";
	}

}
