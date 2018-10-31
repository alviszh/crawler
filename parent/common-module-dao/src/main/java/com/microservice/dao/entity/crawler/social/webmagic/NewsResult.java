package com.microservice.dao.entity.crawler.social.webmagic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;


/**
 * crawler-webmagic 新闻结果表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_news_result")
public class NewsResult extends IdEntitySocial{
	
	private String title;				//标题
	private String url;					//链接
	private String sources;				//来源
	private String publishTime;			//发布时间
	private String content;				//正文
	private String label;				//新闻类型
	private String site;				//爬取站点
	private String ruleId;				//规则表关联
	private String text;				//正文纯文本
	
	@Column(columnDefinition="text")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	@Column(columnDefinition="text")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
