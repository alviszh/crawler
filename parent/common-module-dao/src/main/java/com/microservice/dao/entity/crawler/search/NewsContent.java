package com.microservice.dao.entity.crawler.search;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：NewsContent   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月24日 上午10:56:34   
* @version        
*/
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="search_content",
indexes = {@Index(name = "content_taskid", columnList = "taskid")})
public class NewsContent extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url  ;
	private String title  ;
	private String content  ;
	private String time  ;
	private String taskid;
	private String linkurl;
	
	private String sensitivekey;
			
	@Column(columnDefinition="text")
	public String getSensitivekey() {
		return sensitivekey;
	}
	public void setSensitivekey(String sensitivekey) {
		this.sensitivekey = sensitivekey;
	}
	@Column(columnDefinition="text")
	public String getLinkurl() {
		return linkurl;
	}
	
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	@JsonManagedReference
	private NewsListJson newsListJson;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade = CascadeType.REFRESH)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="newsListJson_id")
	public NewsListJson getNewsListJson() {
		return newsListJson;
	}
	public void setNewsListJson(NewsListJson newsListJson) {
		this.newsListJson = newsListJson;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(columnDefinition="longtext")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "NewsContent [url=" + url + ", title=" + title + ", content=" + content + ", time=" + time + ", taskid="
				+ taskid + ", linkurl=" + linkurl + ", sensitivekey=" + sensitivekey + ", newsListJson=" + newsListJson + "]";
	}
}
