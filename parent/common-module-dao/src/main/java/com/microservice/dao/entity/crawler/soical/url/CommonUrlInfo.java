package com.microservice.dao.entity.crawler.soical.url;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="common_url_info")
public class CommonUrlInfo  extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1221206979457794498L;
	/** 爬取批次号 */
	@Column(name="taskid")
	private String taskid;
	
	/** 正文 */
	private String text;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Lob  
	@Column(columnDefinition="TEXT")  
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
