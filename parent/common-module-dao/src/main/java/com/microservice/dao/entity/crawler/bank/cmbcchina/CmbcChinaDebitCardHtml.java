package com.microservice.dao.entity.crawler.bank.cmbcchina;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="cmbcchina_debitcard_html",indexes = {@Index(name = "index_cmbcchina_debitcard_html_taskid", columnList = "taskid")})
public class CmbcChinaDebitCardHtml extends IdEntity implements Serializable{
	private static final long serialVersionUID = -7598784970840963059L;
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
