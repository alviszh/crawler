package app.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.search.NewsListJson;

/**   
*    
* 项目名称：common-microservice-search   
* 类名称：WebParm   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月24日 上午10:34:19   
* @version        
*/
public class WebParm {

	private String linkurl;

	private String keyword;
		
	private String code;
	
	private List<NewsListJson> list;
	
	

	public List<NewsListJson> getList() {
		return list;
	}

	public void setList(List<NewsListJson> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "WebParm [linkurl=" + linkurl + ", keyword=" + keyword + ", code=" + code + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	
}
