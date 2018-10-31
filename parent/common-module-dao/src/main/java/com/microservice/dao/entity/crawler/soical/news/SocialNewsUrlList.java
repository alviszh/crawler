package com.microservice.dao.entity.crawler.soical.news;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：KeyNews   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月17日 上午10:45:29   
* @version        
*/
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="news_url_list")
public class SocialNewsUrlList  extends IdEntitySocial implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * taskid 保存的流水号
     */
	private String taskid;
    /**
     * 标题
     */
	private String title;

	/**
	 * 文本信息
	 */
    private String abstractTxt;

    /**
     * 链接地址
     */
    private String linkUrl;
    
    private Integer priorityNum;
    
    
    
    public Integer getPriorityNum() {
		return priorityNum;
	}

	public void setPriorityNum(Integer priorityNum) {
		this.priorityNum = priorityNum;
	}

	@Override
	public String toString() {
		return "SocialNewsUrlList [taskid=" + taskid + ", title=" + title + ", abstractTxt=" + abstractTxt
				+ ", linkUrl=" + linkUrl + ", priorityNum=" + priorityNum + ", type=" + type + ", key=" + key
				+ ", pubdate=" + pubdate + ", source=" + source + ", author=" + author + ", source_url=" + source_url
				+ ", site=" + site + ", webType=" + webType + ", socialNewsCrawlerUrl=" + socialNewsCrawlerUrl + "]";
	}

	/**
     * 搜索引擎类型
     */
    private String type;
    
    private String key;
    /*
     * 发布时间
     */
    private String pubdate;
    
    /**
     * 来源
     */
    private String source;
    
    /**
     * 作者
     */
    private String author;
    
    private String source_url;
    
    private String site;
    
    private int webType;
    
    
        
    public int getWebType() {
		return webType;
	}

	public void setWebType(int webType) {
		this.webType = webType;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Column(columnDefinition="text")
	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@JsonManagedReference
	private SocialNewsCrawlerUrl socialNewsCrawlerUrl;
    
    
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="socialurl_id")
	public SocialNewsCrawlerUrl getSocialNewsCrawlerUrl() {
		return socialNewsCrawlerUrl;
	}

	public void setSocialNewsCrawlerUrl(SocialNewsCrawlerUrl socialNewsCrawlerUrl) {
		this.socialNewsCrawlerUrl = socialNewsCrawlerUrl;
	}

	
        
	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(columnDefinition="text")
	public String getAbstractTxt() {
		return abstractTxt;
	}

	public void setAbstractTxt(String abstractTxt) {
		this.abstractTxt = abstractTxt;
	}

	@Column(columnDefinition="text")
	public String getLinkUrl() {
		return linkUrl;
	}

	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}   
	
    
}
