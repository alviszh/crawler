package com.microservice.dao.entity.crawler.bank.hxbchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月17日 上午10:22:19 
 */
@Entity
@Table(name="hxbchina_creditcard_html",indexes = {@Index(name = "index_hxbchina_creditcard_html_taskid", columnList = "taskid")})
public class HxbChinaCreditCardHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4805184524982367340L;
	private String taskid;							
	private String type;
	private Integer pagenumber;	
	private String url;	
	private String html;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPagenumber() {
		return pagenumber;
	}
	public void setPagenumber(Integer pagenumber) {
		this.pagenumber = pagenumber;
	}
	@Column(columnDefinition="text")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	@Override
	public String toString() {
		return "CiticChinaDebitCardHtml [taskid=" + taskid + ", type=" + type + ", pagenumber=" + pagenumber + ", url="
				+ url + ", html=" + html + "]";
	}
}
