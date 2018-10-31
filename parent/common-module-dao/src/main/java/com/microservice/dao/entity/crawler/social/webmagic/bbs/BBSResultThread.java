package com.microservice.dao.entity.crawler.social.webmagic.bbs;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

/**
 * crawler-webmagic-bbs-thread 论坛回贴数据表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_bbs_thread_result" ,indexes = {@Index(name = "index_webmagic_bbs_thread_result_mainId", columnList = "mainId")})
public class BBSResultThread extends IdEntitySocial{
	
	private String BBSType;					//规则所属论坛类型
	private String mainId;				//回帖的关联id
	private String content;					//回帖内容
	
	public String getBBSType() {
		return BBSType;
	}
	public void setBBSType(String bBSType) {
		BBSType = bBSType;
	}
	/**
	 * @return the mainId
	 */
	public String getMainId() {
		return mainId;
	}
	/**
	 * @param mainId the mainId to set
	 */
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	

}
