package com.crawler.cmcc.domain.json;

import java.io.Serializable;

public class LoginAuthJson implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3691538051155987686L;

	private String artifact;
	
	private String assertAcceptURL;
	
	private String code;
	
	private String desc;
	
	private boolean islocal;
	
	private String result;
	
	private String uid;

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getAssertAcceptURL() {
		return assertAcceptURL;
	}

	public void setAssertAcceptURL(String assertAcceptURL) {
		this.assertAcceptURL = assertAcceptURL;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isIslocal() {
		return islocal;
	}

	public void setIslocal(boolean islocal) {
		this.islocal = islocal;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	

}
