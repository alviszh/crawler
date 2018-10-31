package com.microservice.es.entity.crawler.search;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.springframework.data.elasticsearch.annotations.Document;

import com.microservice.es.entity.IdEntity;

/**   
*    
* 项目名称：common-module-dao   
* 类名称：NewsContent   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月24日 上午10:56:34   
* @version        
*/

@Document(indexName = "newscontenttest", type = "newscontenttest")
public class NewsContentES extends IdEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url  ;
	
	//@Field(index=FieldIndex.analyzed)
	private String title  ;
	private String content  ;
	private String time  ;
	private String taskid;
	private String newsListJsonEsId;
	
	
	
	public String getNewsListJsonEsId() {
		return newsListJsonEsId;
	}
	public void setNewsListJsonEsId(String newsListJsonEsId) {
		this.newsListJsonEsId = newsListJsonEsId;
	}
	@JsonManagedReference
	private NewsListJsonES newsListJsonEs;
	
	@JoinColumn(name="newsListJsonEs_id")
	public NewsListJsonES getNewsListJsonEs() {
		return newsListJsonEs;
	}
	public void setNewsListJsonEs(NewsListJsonES newsListJsonEs) {
		this.newsListJsonEs = newsListJsonEs;
	}
	
	public String getTaskid() {
		return taskid;
	}
	
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="text")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(columnDefinition="text")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "NewsContentES [url=" + url + ", title=" + title + ", content=" + content + ", time=" + time
				+ ", taskid=" + taskid + ", newsListJsonEsId=" + newsListJsonEsId + ", newsListJsonEs=" + newsListJsonEs
				+ "]";
	}
}
