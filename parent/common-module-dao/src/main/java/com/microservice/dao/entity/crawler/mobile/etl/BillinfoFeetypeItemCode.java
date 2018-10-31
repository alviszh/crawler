package com.microservice.dao.entity.crawler.mobile.etl;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="billinfo_feetype_item_code")
public class BillinfoFeetypeItemCode extends IdEntity implements Serializable{

	private static final long serialVersionUID = 5133365472766002696L;
	
	private String item_code;
	private String item_name;
	private String source_name;
	private String item_type;
	
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getSource_name() {
		return source_name;
	}
	public void setSource_name(String source_name) {
		this.source_name = source_name;
	}
	public String getItem_type() {
		return item_type;
	}
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
	@Override
	public String toString() {
		return "BillinfoFeetypeItemCode [item_code=" + item_code + ", item_name=" + item_name + ", source_name="
				+ source_name + ", item_type=" + item_type + "]";
	}
		
}
