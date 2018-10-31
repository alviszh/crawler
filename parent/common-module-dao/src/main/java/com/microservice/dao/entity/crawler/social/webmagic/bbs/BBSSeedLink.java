package com.microservice.dao.entity.crawler.social.webmagic.bbs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

/**
 * 论坛种子表
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_bbs_seed_link")
public class BBSSeedLink extends IdEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Date lastCrawlTime;				//最后爬取时间
	private String url;							//爬取链接
	private String ruleId;						//对应的匹配的规则
	private String BBSType;						//规则所属论坛类型
	private String BBSName;						//论坛名字
	private Integer crawlerStatus;				//爬取开关
	
	@Override
	public String toString() {
		return "BBSSeedLink [lastCrawlTime=" + lastCrawlTime + ", url=" + url + ", ruleId=" + ruleId 
				 + ", BBSType=" + BBSType + ", BBSName=" + BBSName + ", crawlerStatus=" + crawlerStatus
				+ "]";
	}
	public Date getLastCrawlTime() {
		return lastCrawlTime;
	}
	public void setLastCrawlTime(Date lastCrawlTime) {
		this.lastCrawlTime = lastCrawlTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getBBSType() {
		return BBSType;
	}
	public void setBBSType(String bBSType) {
		BBSType = bBSType;
	}
	public String getBBSName() {
		return BBSName;
	}
	public void setBBSName(String bBSName) {
		BBSName = bBSName;
	}
	public Integer getCrawlerStatus() {
		return crawlerStatus;
	}
	public void setCrawlerStatus(Integer crawlerStatus) {
		this.crawlerStatus = crawlerStatus;
	}

}
