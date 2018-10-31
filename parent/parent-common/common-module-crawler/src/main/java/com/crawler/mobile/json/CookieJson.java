package com.crawler.mobile.json;

import java.io.Serializable;

public class CookieJson implements Serializable{
	
	private String domain;
	
	private String key;
	
	private String value;
	
	public CookieJson(String domain,String key,String value){
		this.domain = domain;
		this.key = key;
		this.value = value;
		
	}
	
	public CookieJson(){
		
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CookieJson [domain=" + domain + ", key=" + key + ", value=" + value + "]";
	}
	
	

}
