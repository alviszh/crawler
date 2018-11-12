package com.microservice.dao.entity.crawler.search;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：KeyNews   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月17日 上午10:45:29   
* @version        
*/
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name="search_newslist",
indexes = {@Index(name = "list_taskid", columnList = "taskid")})
public class NewsListJson  extends IdEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * taskid 保存的流水号
     */
	private String taskid;
    /**
     * 标题
     */
	private String title;

	/**
	 * 文本信息
	 */
    private String abstractTxt;

    /**
     * 链接地址
     */
    private String linkUrl;
    
    /**
     * 搜索引擎类型
     */
    private String type;
    
    private String keyword;
    @JsonManagedReference
    private List<NewsContent> list;
    
    private String sensitivekey;
	
	@Column(columnDefinition="text")
	public String getSensitivekey() {
		return sensitivekey;
	}
	public void setSensitivekey(String sensitivekey) {
		this.sensitivekey = sensitivekey;
	}
    

	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy="newsListJson")
	public List<NewsContent> getList() {
		return list;
	}

	public void setList(List<NewsContent> list) {
		this.list = list;
	}



	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	@Column(columnDefinition="text")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(columnDefinition="text")
	public String getAbstractTxt() {
		return abstractTxt;
	}

	public void setAbstractTxt(String abstractTxt) {
		this.abstractTxt = abstractTxt;
	}

	@Column(columnDefinition="text")
	public String getLinkUrl() {
		return linkUrl;
	}

	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}


	@Override
	public String toString() {
		return "NewsListJson [taskid=" + taskid + ", title=" + title + ", abstractTxt=" + abstractTxt + ", linkUrl="
				+ linkUrl + ", type=" + type + ", keyword=" + keyword + ", list=" + list + ", sensitivekey="
				+ sensitivekey + "]";
	}
   
	
    
}
