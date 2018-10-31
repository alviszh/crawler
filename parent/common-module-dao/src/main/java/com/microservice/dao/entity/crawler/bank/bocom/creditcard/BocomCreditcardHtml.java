package com.microservice.dao.entity.crawler.bank.bocom.creditcard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：BocomCreditcardHtml   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月21日 上午11:25:20   
* @version        
*/
@Entity
@Table(name="bocom_creditcard_html",indexes = {@Index(name = "index_bocom_creditcard_html_taskid", columnList = "taskid")})
public class BocomCreditcardHtml extends IdEntity{
	
	private String taskid;
	@Column(columnDefinition="text")
	private String html;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	
	
	
}
