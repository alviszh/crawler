package com.microservice.dao.entity.crawler.soical.news;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

/**
 * 
 * 项目名称：common-module-dao 类名称：SearchTask 类描述： 创建人：hyx 创建时间：2018年1月18日 上午11:44:00
 * 
 * @version
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "news_url")
public class SocialNewsCrawlerUrl extends IdEntitySocial implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String keyword;

	private String phase;// 当前步骤

	private String description;

	private String linkurl;

	private String type;// 网站类型 百度，搜狗，好搜

	private int pagenum;// 页数

	private int renum;// 重试次数

	private Integer prioritynum; // 优先级

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

	@JsonManagedReference
	private List<SocialNewsUrlList> list;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "socialNewsCrawlerUrl")
	public List<SocialNewsUrlList> getList() {
		return list;
	}

	public void setList(List<SocialNewsUrlList> list) {
		this.list = list;
	}

	public Integer getPrioritynum() {
		return prioritynum;
	}

	public void setPrioritynum(Integer prioritynum) {
		this.prioritynum = prioritynum;
	}

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

	@Column(columnDefinition = "text")
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

	@Column(columnDefinition = "text")
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

}
