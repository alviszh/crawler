package com.microservice.dao.entity.crawler.social.webmagic;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

/**
 * crawler-webmagic 通用爬虫爬取模板
 * @author zz
 *
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="webmagic_news_rule")
public class NewsRule extends IdEntity implements Serializable{
	
	
	@Override
	public String toString() {
		return "NewsRule [crawler_source=" + crawler_source + ", url=" + url + ", content_rule=" + content_rule
				+ ", source_rule=" + source_rule + ", label=" + label + ", second_url_regex=" + second_url_regex
				+ ", host=" + host +"]";
	}

	private static final long serialVersionUID = 1L;
	
	private String crawler_source;					//爬取来源
	
	private String url;								//链接
	
	private String content_rule;					//正文规则
	
	private String source_rule;						//来源规则
	
	private String label;							//标签
	
	private String second_url_regex;				//第二级链接（正则）
	
	private String host;							//域名
	
	private String errorMes;						//模板配置错误信息
	
	private String second_url_css;					//第二级链接规则（css）    需要具体到链接所在的标签
	
	private Date lastCrawlerTime;		//最后一次爬取的时间
	
	private Integer crawlerState;					//爬取状态			0为开启  1为停止
	
	public Integer getCrawlerState() {
		return crawlerState;
	}
	
	public void setCrawlerState(Integer crawlerState) {
		this.crawlerState = crawlerState;
	}
	
	public Date getLastCrawlerTime() {
		return lastCrawlerTime;
	}

	public void setLastCrawlerTime(Date lastCrawlerTime) {
		this.lastCrawlerTime = lastCrawlerTime;
	}

	public String getSecond_url_css() {
		return second_url_css;
	}

	public void setSecond_url_css(String second_url_css) {
		this.second_url_css = second_url_css;
	}

	public String getErrorMes() {
		return errorMes;
	}

	public void setErrorMes(String errorMes) {
		this.errorMes = errorMes;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getCrawler_source() {
		return crawler_source;
	}

	public void setCrawler_source(String crawler_source) {
		this.crawler_source = crawler_source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getContent_rule() {
		return content_rule;
	}

	public void setContent_rule(String content_rule) {
		this.content_rule = content_rule;
	}


	public String getSource_rule() {
		return source_rule;
	}

	public void setSource_rule(String source_rule) {
		this.source_rule = source_rule;
	}

	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSecond_url_regex() {
		return second_url_regex;
	}

	public void setSecond_url_regex(String second_url_regex) {
		this.second_url_regex = second_url_regex;
	}

	

}
