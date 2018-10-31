package com.microservice.dao.entity.crawler.soical.search;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.gargoylesoftware.htmlunit.javascript.host.file.Blob;
import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="searchindex_image")
public class SearchIndexImage extends IdEntitySocial implements Serializable{

	
	private String uuid;
	
	private byte[] image;
	
	private String timetype;
	
	private String keyword;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTimetype() {
		return timetype;
	}

	public void setTimetype(String timetype) {
		this.timetype = timetype;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	
	
	
}
