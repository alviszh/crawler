package com.microservice.dao.entity.crawler.search;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.microservice.dao.entity.IdEntity;

/**
 * 
 * 项目名称：common-module-dao 类名称：SearchTask 类描述： 创建人：hyx 创建时间：2018年1月18日 上午11:44:00
 * 
 * @version
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "task_search")
@EntityListeners(AuditingEntityListener.class)
public class SearchTask extends IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String keyword;

	private String phase;// 当前步骤

	private String description;
	
	private String linkurl;
	
	private String type;//网站类型 百度，搜狗，好搜
	
	private int pagenum;//页数
	
	private int renum;//重试次数
	
	private Integer prioritynum; //优先级
	
	private String ipaddress;
	
	private String ipport;
	

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getIpport() {
		return ipport;
	}

	public void setIpport(String ipport) {
		this.ipport = ipport;
	}

	public Integer getPrioritynum() {
		return prioritynum;
	}

	public void setPrioritynum(Integer prioritynum) {
		this.prioritynum = prioritynum;
	}

	@LastModifiedDate
	@CreatedDate
    private Date updateTime;
	
	public int getRenum() {
		return renum;
	}

	public void setRenum(int renum) {
		this.renum = renum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(columnDefinition="text")
	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	@Column(columnDefinition="text")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "SearchTask [taskid=" + taskid + ", keyword=" + keyword + ", phase=" + phase + ", description="
				+ description + ", linkurl=" + linkurl + ", type=" + type + ", pagenum=" + pagenum + ", renum=" + renum
				+ ", prioritynum=" + prioritynum + ", ipaddress=" + ipaddress + ", ipport=" + ipport + ", updateTime="
				+ updateTime + "]";
	}

}
