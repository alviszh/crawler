package com.crawler.taxation.json;

import java.io.Serializable;

public class TaxationJsonBean implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -5115154845415106526L;

	public Long id;
	public String username; // 用户名
	public String idnum; // 身份证号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	@Override
	public String toString() {
		return "InsuranceJsonBean [id=" + id + ", username=" + username + ", idnum=" + idnum;
	}
}
