package com.microservice.dao.entity.crawler.soical.basic;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * 
 * 项目名称：common-module-dao 类名称：SocialKeyWord 类描述： 创建人：hyx 创建时间：2018年4月11日
 * 下午2:49:39
 * 
 * @version
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "common_keyword")
public class SocialCommonKeyWord extends IdEntitySocial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskid;

	private String keyword;
	
	private int isNews;

	private int isWeibo;

	private int isTeiba;

	private int isZhihu;

	private int isSearch;
	
	private SocialNewsKey socialNewsKey;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "socialCommonKeyWord")
	@JoinColumn(name="socialNewsKey_id",referencedColumnName = "socialNewsKey_id")
	public SocialNewsKey getSocialNewsKey() {
		return socialNewsKey;
	}

	public void setSocialNewsKey(SocialNewsKey socialNewsKey) {
		this.socialNewsKey = socialNewsKey;
	}
	
//	@OneToOne(mappedBy = "idcard", cascade = { CascadeType.PERSIST,
//			CascadeType.MERGE, CascadeType.REFRESH }, optional = false)

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

	public int getIsNews() {
		return isNews;
	}

	public void setIsNews(int isNews) {
		this.isNews = isNews;
	}

	public int getIsWeibo() {
		return isWeibo;
	}

	public void setIsWeibo(int isWeibo) {
		this.isWeibo = isWeibo;
	}

	public int getIsTeiba() {
		return isTeiba;
	}

	public void setIsTeiba(int isTeiba) {
		this.isTeiba = isTeiba;
	}

	public int getIsZhihu() {
		return isZhihu;
	}

	public void setIsZhihu(int isZhihu) {
		this.isZhihu = isZhihu;
	}

	public int getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(int isSearch) {
		this.isSearch = isSearch;
	}

	@Override
	public String toString() {
		return "SocialCommonKeyWord [taskid=" + taskid + ", keyword=" + keyword + ", isNews=" + isNews + ", isWeibo="
				+ isWeibo + ", isTeiba=" + isTeiba + ", isZhihu=" + isZhihu + ", isSearch=" + isSearch
				+ ", socialNewsKey=" + socialNewsKey + "]";
	}

	
	

}
