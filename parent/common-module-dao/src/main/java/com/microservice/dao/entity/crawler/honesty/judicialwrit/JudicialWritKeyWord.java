package com.microservice.dao.entity.crawler.honesty.judicialwrit;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.executor.IdEntityExecutor;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "judicial_writ_keyword")
public class JudicialWritKeyWord extends IdEntityExecutor implements Serializable {

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
