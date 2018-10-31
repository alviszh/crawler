package com.microservice.dao.entity.crawler.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "s_user")
public class SUser extends IdEntity implements Serializable {

	private static final long serialVersionUID = 5709688177071433888L;

	/** 登录账号 */
	@Column(name = "login_name")
	private String loginName;

	/** 用户姓名 */
	@Column(name = "name")
	private String name;

	/** 登录密码 */
	@Column(name = "password")
	private String password;

	/** 角色列表 */
	private Set<SRole> SRoles = new HashSet<SRole>(0);

	public SUser() {

	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "SUser")
	public Set<SRole> getSRoles() {
		return this.SRoles;
	}

	public void setSRoles(Set<SRole> SRoles) {
		this.SRoles = SRoles;
	}

}
