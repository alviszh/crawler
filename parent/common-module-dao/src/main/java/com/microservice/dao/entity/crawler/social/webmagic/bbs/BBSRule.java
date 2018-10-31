package com.microservice.dao.entity.crawler.social.webmagic.bbs;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

/**
 * crawler-webmagic-bbs 通用爬虫爬取模板(爬取论坛)
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_bbs_rule")
public class BBSRule extends IdEntity implements Serializable{
	
	@Override
	public String toString() {
		return "BBSRule [secondUrlCss=" + secondUrlCss + ", secondUrlRegex=" + secondUrlRegex + ", mainTitleRule="
				+ mainTitleRule + ", mainTimeCss=" + mainTimeCss + ", mainTimeRegex=" + mainTimeRegex
				+ ", mainAuthorRule=" + mainAuthorRule + ", threadElementRule=" + threadElementRule
				+ ", threadContentRule=" + threadContentRule + ", BBSType=" + BBSType + "]";
	}

	private static final long serialVersionUID = 1L;
	
	/*爬取所需规则*/
	private String secondUrlCss;				//第二级链接（css）
	private String secondUrlRegex;				//第二级链接（正则）
	private String mainTitleRule;				//主贴标题规则
	private String mainTimeCss;					//主贴发布时间规则（css）
	private String mainTimeRegex;				//主贴发布时间规则（正则）	
	private String mainAuthorRule;				//主贴发布者规则
	private String threadElementRule;			//获取每个回帖包含所有信息所在的element
	private String threadContentRule;			//回帖内容
	
	/*关联字段*/
	private String BBSType;						//规则所属论坛类型

	public String getSecondUrlCss() {
		return secondUrlCss;
	}

	public void setSecondUrlCss(String secondUrlCss) {
		this.secondUrlCss = secondUrlCss;
	}

	public String getSecondUrlRegex() {
		return secondUrlRegex;
	}

	public void setSecondUrlRegex(String secondUrlRegex) {
		this.secondUrlRegex = secondUrlRegex;
	}

	public String getMainTitleRule() {
		return mainTitleRule;
	}

	public void setMainTitleRule(String mainTitleRule) {
		this.mainTitleRule = mainTitleRule;
	}

	public String getMainTimeCss() {
		return mainTimeCss;
	}

	public void setMainTimeCss(String mainTimeCss) {
		this.mainTimeCss = mainTimeCss;
	}

	public String getMainTimeRegex() {
		return mainTimeRegex;
	}

	public void setMainTimeRegex(String mainTimeRegex) {
		this.mainTimeRegex = mainTimeRegex;
	}

	public String getMainAuthorRule() {
		return mainAuthorRule;
	}

	public void setMainAuthorRule(String mainAuthorRule) {
		this.mainAuthorRule = mainAuthorRule;
	}

	public String getThreadElementRule() {
		return threadElementRule;
	}

	public void setThreadElementRule(String threadElementRule) {
		this.threadElementRule = threadElementRule;
	}

	public String getThreadContentRule() {
		return threadContentRule;
	}

	public void setThreadContentRule(String threadContentRule) {
		this.threadContentRule = threadContentRule;
	}

	public String getBBSType() {
		return BBSType;
	}

	public void setBBSType(String bBSType) {
		BBSType = bBSType;
	}
	
	

}
