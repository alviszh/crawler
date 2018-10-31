package com.microservice.dao.entity.crawler.soical.web.set_up;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "frontpage_sceneInfo")
public class SceneInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue()
	private String setupid;
	
	private String scenename;//场景名称
	
	private String describe;//描述
	
	private String creator;//创建人
	
	private String creationtime;//创建时间
	
	private String glid;


	public String getSetupid() {
		return setupid;
	}

	public void setSetupid(String setupid) {
		this.setupid = setupid;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	public String getScenename() {
		return scenename;
	}

	public void setScenename(String scenename) {
		this.scenename = scenename;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	@Override
	public String toString() {
		return "SceneInfo [id=" + id + ", scenename=" + scenename + ", describe=" + describe + ", creator=" + creator
				+ ", creationtime=" + creationtime + ", glid=" + glid + "]";
	}
	
	
}
