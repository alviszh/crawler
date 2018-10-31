package com.microservice.dao.entity.crawler.cmcc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 中国移动-请求补救表   （用于获取数据时返回异常后进行二次请求）
 * @author zz
 *
 */
@Entity
@Table(name="cmcc_remedy_parameter")
public class CmccRemedyParameter extends IdEntity{
	 
	@Column(name="task_id")
	private String taskId;		
	
	private String url;				//补救的url
	private String type;			//请求类型
	private Integer count;			//入库总条数
	private String year;			//年月
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public String toString() {
		return "CmccRemedyParameter [taskId=" + taskId + ", url=" + url + ", type=" + type + ", count=" + count + "]";
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}


}
