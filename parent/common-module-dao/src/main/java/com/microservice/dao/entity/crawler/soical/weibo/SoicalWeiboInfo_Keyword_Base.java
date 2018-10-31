package com.microservice.dao.entity.crawler.soical.weibo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="social_weiboinfo_keyword_base")
public class SoicalWeiboInfo_Keyword_Base  extends IdEntity implements Serializable {
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
	
	/** 头像*/
	@Column(name="profile_image_url")
	private String profile_image_url;
	
	/** 昵称*/
	@Column(name="name")
	private String name;
	
	/** 微博内容  */
	@Column(name="text")
	private String text;
	
	/** 图片 */
	@Column(name="image")
	private String image;
	
	/** 微博时间 */
	@Column(name="created_at")
	private String created_at;
	
	/** 微博来源  */
	@Column(name="source")
	private String source;
	
	/** 转发数  */
	@Column(name="zfs")
	private String zfs;
	
	/** 点赞数  */
	@Column(name="dzs")
	private String dzs;
	
	
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

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getZfs() {
		return zfs;
	}

	public void setZfs(String zfs) {
		this.zfs = zfs;
	}

	public String getDzs() {
		return dzs;
	}

	public void setDzs(String dzs) {
		this.dzs = dzs;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
