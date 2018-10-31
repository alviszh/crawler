package com.microservice.dao.entity.crawler.soical.weibo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="social_weiboinfo_keyword_top_article")
public class SoicalWeiboInfo_Keyword_Top_Article  extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	/** 爬取批次号 */
	@Column(name="uuid")
	private String uuid;
	
	/**etltime */
	@Column(name="etltime")
	private String etltime;
	

	/** 关键字*/
	@Column(name="keyword")
	private String keyword;
	
	/** 微博标题  */
	@Column(name="title")
	private String title;
	
	/** 文章地址  */
	@Column(name="articleUrl")
	private String articleUrl;
	
	/** 图片  */
	@Column(name="image")
	private String image;
	
	/** 微博内容  */
	@Column(name="text")
	private String text;
	
	/** 微博创建时间 */
	@Column(name="created_at")
	private String created_at;
	
	/** 微博来源  */
	@Column(name="source")
	private String source;
	
	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	@Column(columnDefinition="text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEtltime() {
		return etltime;
	}

	public void setEtltime(String etltime) {
		this.etltime = etltime;
	}
	
}
