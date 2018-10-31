package com.microservice.dao.entity.crawler.soical.weibo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="social_weibo_info")
public class SoicalWeiboInfo  extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	/**微博图片 */
	@Column(name="pic_urls")
	private String pic_urls;
	
	/** 爬取批次号 */
	@Column(name="uuid")
	private String uuid;
	
	/**etltime */
	@Column(name="etltime")
	private String etltime;
	
	/** 微博创建时间 */
	@Column(name="created_at")
	private String created_at;
	
	/** 微博ID字符串类型  */
	@Column(name="idstr")
	private String idstr;
	
	/** 微博内容  */
	@Column(name="text")
	private String text;
	
	/** 微博来源  */
	@Column(name="source")
	private String source;
	
	/** 转发数*/
	@Column(name="reposts_count")
	private String reposts_count;
	
	/** 评论数*/
	@Column(name="comments_count")
	private String comments_count;
	
	/** 昵称*/
	@Column(name="name")
	private String name;
	
	/** 头像*/
	@Column(name="profile_image_url")
	private String profile_image_url;
	
	/** 粉丝数*/
	@Column(name="followers_count")
	private String followers_count;
	
	/** 微博数*/
	@Column(name="statuses_count")
	private String statuses_count;

	

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

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
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

	public String getPic_urls() {
		return pic_urls;
	}

	public void setPic_urls(String pic_urls) {
		this.pic_urls = pic_urls;
	}

	public String getReposts_count() {
		return reposts_count;
	}

	public void setReposts_count(String reposts_count) {
		this.reposts_count = reposts_count;
	}

	public String getComments_count() {
		return comments_count;
	}

	public void setComments_count(String comments_count) {
		this.comments_count = comments_count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}

	public String getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(String statuses_count) {
		this.statuses_count = statuses_count;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}
	
	
}
