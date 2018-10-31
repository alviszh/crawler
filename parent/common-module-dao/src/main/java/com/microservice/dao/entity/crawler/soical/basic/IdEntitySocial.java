package com.microservice.dao.entity.crawler.soical.basic;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.microservice.dao.entity.IdEntity;


/**
 * 
 * 项目名称：common-module-dao 类名称：IdEntitySocial 
 * 类描述: 舆情字段的继承类
 * 创建人：hyx 创建时间：2018年3月27日
 * 上午9:38:40
 * 
 * @version
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class IdEntitySocial extends IdEntity {

	@LastModifiedDate
	@CreatedDate
	protected Date updatetime;
	
	protected Date etltime;
	
	protected String uuid;
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getEtltime() {
		return etltime;
	}

	public void setEtltime(Date etltime) {
		this.etltime = etltime;
	}

	@Override
	public String toString() {
		return "IdEntitySocial [updatetime=" + updatetime + ", etltime=" + etltime + ", uuid=" + uuid + "]";
	}
	
	
}
