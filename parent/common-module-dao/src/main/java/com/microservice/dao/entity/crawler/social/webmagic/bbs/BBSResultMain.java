package com.microservice.dao.entity.crawler.social.webmagic.bbs;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.crawler.soical.basic.IdEntitySocial;

/**
 * crawler-webmagic-bbs-main 论坛主贴数据表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_bbs_main_result" ,indexes = {@Index(name = "index_webmagic_bbs_main_result_uniqMsg", columnList = "uniqMsg")})
public class BBSResultMain extends IdEntitySocial{
	
	private String BBSType;					//规则所属论坛类型
	private Long seedLinkId;				//种子链接的关联id
	private String title;					//标题
	private String author;					//作者
	private String time;					//发布时间
	private String uniqMsg;					//去重信息
	
	public String getUniqMsg() {
		return uniqMsg;
	}
	public void setUniqMsg(String uniqMsg) {
		this.uniqMsg = uniqMsg;
	}
	@Override
	public String toString() {
		return "BBSResultMain [BBSType=" + BBSType + ", seedLinkId=" + seedLinkId + ", title=" + title + ", author="
				+ author + ", time=" + time + "]";
	}
	public String getBBSType() {
		return BBSType;
	}
	public void setBBSType(String bBSType) {
		BBSType = bBSType;
	}
	public Long getSeedLinkId() {
		return seedLinkId;
	}
	public void setSeedLinkId(Long seedLinkId) {
		this.seedLinkId = seedLinkId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	

}
