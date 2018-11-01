package app.entity.crawler;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:  将登录页面的源代码进行加密，该表的id主键为js表的外键（一对多）
 * @author: sln 
 * @date: 2018年1月30日 下午4:18:37 
 */
@Entity
@Table(name = "monitor_loginpage_html")
public class MonitorLoginPageHtml extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4918169247990622081L;
	private String taskid;
	private String url;      //登录url地址
	private String htmlmd5;  //网站登录页源代码加密出来的md5码
	private Boolean changeflag;   //标识与旧的md5码相比，是否有变动
	private long urlid;      //外键，用于和url表建立关联
	private String webtype;    //网站类型名称	
	private Integer jscount;    //该登录页面所对应的js数量
	private String htmlcode;//   存储源码
	private String aftertreathtmlcode;  //经过处理后的html内容
	private String developer;    //网站负责人
	private String htmlmodified; //网页上一次修改时间，如果提供，就存储，否则就用字符串"null"显示
	private String comparetaskid;  //参照taskid
	//如下字段为js数量变化提示内容：
	private Boolean jscountchangeflag;
	private String jscountchangedetail;
//	@JsonManagedReference
//	private List<MonitorLoginPageJs> jsList;
//
//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "loginPage")
//	public List<MonitorLoginPageJs> getJsList() {
//	    return jsList;
//	}
//	
//	public void setJsList(List<MonitorLoginPageJs> jsList) {
//		this.jsList = jsList;
//	}
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
	public String getHtmlmd5() {
		return htmlmd5;
	}
	public void setHtmlmd5(String htmlmd5) {
		this.htmlmd5 = htmlmd5;
	}
	public Boolean getChangeflag() {
		return changeflag;
	}
	public void setChangeflag(Boolean changeflag) {
		this.changeflag = changeflag;
	}
	public long getUrlid() {
		return urlid;
	}
	public void setUrlid(long urlid) {
		this.urlid = urlid;
	}
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public Integer getJscount() {
		return jscount;
	}
	public void setJscount(Integer jscount) {
		this.jscount = jscount;
	}
	public String getHtmlcode() {
		return htmlcode;
	}
	public void setHtmlcode(String htmlcode) {
		this.htmlcode = htmlcode;
	}
	public String getAftertreathtmlcode() {
		return aftertreathtmlcode;
	}
	public void setAftertreathtmlcode(String aftertreathtmlcode) {
		this.aftertreathtmlcode = aftertreathtmlcode;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getHtmlmodified() {
		return htmlmodified;
	}
	public void setHtmlmodified(String htmlmodified) {
		this.htmlmodified = htmlmodified;
	}
	public String getComparetaskid() {
		return comparetaskid;
	}
	public void setComparetaskid(String comparetaskid) {
		this.comparetaskid = comparetaskid;
	}
	public Boolean getJscountchangeflag() {
		return jscountchangeflag;
	}
	public void setJscountchangeflag(Boolean jscountchangeflag) {
		this.jscountchangeflag = jscountchangeflag;
	}
	public String getJscountchangedetail() {
		return jscountchangedetail;
	}
	public void setJscountchangedetail(String jscountchangedetail) {
		this.jscountchangedetail = jscountchangedetail;
	}
	public MonitorLoginPageHtml() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MonitorLoginPageHtml(String taskid, String url, String htmlmd5, Boolean changeflag, long urlid,
			String webtype, Integer jscount, String htmlcode, String aftertreathtmlcode, String developer,
			String htmlmodified, String comparetaskid, Boolean jscountchangeflag, String jscountchangedetail) {
		super();
		this.taskid = taskid;
		this.url = url;
		this.htmlmd5 = htmlmd5;
		this.changeflag = changeflag;
		this.urlid = urlid;
		this.webtype = webtype;
		this.jscount = jscount;
		this.htmlcode = htmlcode;
		this.aftertreathtmlcode = aftertreathtmlcode;
		this.developer = developer;
		this.htmlmodified = htmlmodified;
		this.comparetaskid = comparetaskid;
		this.jscountchangeflag = jscountchangeflag;
		this.jscountchangedetail = jscountchangedetail;
	}
}
