package com.microservice.es.entity.crawler.search;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.springframework.data.elasticsearch.annotations.Document;

import com.microservice.es.entity.IdEntity;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：KeyNews   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月17日 上午10:45:29   
* @version        
*/

@Document(indexName = "newslist", type = "newslist")
public class NewsListJsonES  extends IdEntity implements Serializable {
    private static final long serialVersionUID = -1L;
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
    
    private String key;
    @JsonManagedReference
    private List<NewsContentES> list;
        

	@OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="newsListJsonEs")
	public List<NewsContentES> getList() {
		return list;
	}

	public void setList(List<NewsContentES> list) {
		this.list = list;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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


	public String getLinkUrl() {
		return linkUrl;
	}

	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "NewsListJson [taskid=" + taskid + ", title=" + title + ", abstractTxt=" + abstractTxt + ", linkUrl="
				+ linkUrl + ", type=" + type + ", key=" + key + ", list=" + list + "]";
	}
   
	
    
}
