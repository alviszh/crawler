package com.microservice.dao.entity.crawler.soical.basic;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;
import com.microservice.dao.entity.crawler.soical.basic.SocialCommonKeyWord;


/**   
*    
* 项目名称：common-module-dao   
* 类名称：SocialNewsKey   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年4月4日 下午4:49:28   
* @version        
*/
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "news_keyword")
public class SocialNewsKey extends IdEntitySocial implements Serializable{

	private static final long serialVersionUID = 1L;

	private String taskid;

	private String keyword;

	private String phase;// 当前步骤

	private String description;
						
	private int prioritynum; //优先级
	
	private int pagenum;//页数
	
	private int isNews;
	
	private int isSearch;
	


	public int getIsNews() {
		return isNews;
	}

	public void setIsNews(int isNews) {
		this.isNews = isNews;
	}

	public int getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(int isSearch) {
		this.isSearch = isSearch;
	}

	public void setPrioritynum(int prioritynum) {
		this.prioritynum = prioritynum;
	}

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
	private SocialCommonKeyWord socialCommonKeyWord;

//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "socialCommonKeyWord")
//	@JoinColumn(name="common_keyword_id")
	@OneToOne(fetch=FetchType.EAGER,cascade = CascadeType.ALL)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="common_keyword_id")
	public SocialCommonKeyWord getSocialCommonKeyWord() {
		return socialCommonKeyWord;
	}

	public void setSocialCommonKeyWord(SocialCommonKeyWord socialCommonKeyWord) {
		this.socialCommonKeyWord = socialCommonKeyWord;
	}
	
	public int getPagenum() {
		return pagenum;
	}

	

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getPrioritynum() {
		return prioritynum;
	}

	public void setPrioritynum(Integer prioritynum) {
		this.prioritynum = prioritynum;
	}

	

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

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
	
	@Override
	public String toString() {
		return "SocialNewsKey [taskid=" + taskid + ", keyword=" + keyword + ", phase=" + phase + ", description="
				+ description + ", prioritynum=" + prioritynum + ", pagenum=" + pagenum + ", isNews=" + isNews
				+ ", isSearch=" + isSearch + ", ipaddress=" + ipaddress + ", ipport=" + ipport + "]";
	}

}
