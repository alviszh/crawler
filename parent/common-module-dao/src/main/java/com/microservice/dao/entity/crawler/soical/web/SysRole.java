package com.microservice.dao.entity.crawler.soical.web;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/*
 * 角色表
 */
@Entity
@Table(name = "soical_sys_role")
public class SysRole extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	//姓名
	@Column(name="name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
