package com.crawler.search.bean;

/**   
*    
* 项目名称：common-microservice-client   
* 类名称：aa   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年3月7日 下午6:40:20   
* @version        
*/
public class SearchTaskBean {

	private Long id;

	private String taskid;

	private String keyword;

	private String phase;// 当前步骤

	private String description;
	
	private String linkurl;
	
	private String type;//网站类型 百度，搜狗，好搜
	
	private int pagenum;//页数
	
	private int renum;//重试次数
	
	private Integer prioritynum; //优先级
	
	private String ipaddress;
	
	private String ipport;
		
	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getIpport() {
		return ipport;
	}

	public void setIpport(String ipport) {
		this.ipport = ipport;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPagenum() {
		return pagenum;
	}

	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	public int getRenum() {
		return renum;
	}

	public void setRenum(int renum) {
		this.renum = renum;
	}

	public Integer getPrioritynum() {
		return prioritynum;
	}

	public void setPrioritynum(Integer prioritynum) {
		this.prioritynum = prioritynum;
	}

	@Override
	public String toString() {
		return "SearchTaskBean [id=" + id + ", taskid=" + taskid + ", keyword=" + keyword + ", phase=" + phase
				+ ", description=" + description + ", linkurl=" + linkurl + ", type=" + type + ", pagenum=" + pagenum
				+ ", renum=" + renum + ", prioritynum=" + prioritynum + ", ipaddress=" + ipaddress + ", ipport="
				+ ipport + "]";
	}
	

	
	
}

