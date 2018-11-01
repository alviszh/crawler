package app.entity.crawler;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 与url表通过url建立表关系
 * @author sln
 *
 */
@Entity
@Table(name = "monitor_aftertreat_js")
public class MonitorJsAfterTreat extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7299032560004801388L;
	private String url;
	private String jspath;
	private String webtype;
	private String treatmethod;  //处理方法（个别网站的js中的方法名是网站后台随生成的，或者前后两次执行用的js内容的同样的部分，有注释和没被注释的区别）
	private String strstart;   //截取字符串的起始位置
	private String strend;    //截取字符串的终止位置
	private String md5orlength;  //比对长度还是加密的结果      length    md5  （比对长度的原因在于部分js代码中的函数是后台生成的，存在变化）
	private String developer;    //网站负责人
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTreatmethod() {
		return treatmethod;
	}
	public String getJspath() {
		return jspath;
	}
	public void setJspath(String jspath) {
		this.jspath = jspath;
	}
	public void setTreatmethod(String treatmethod) {
		this.treatmethod = treatmethod;
	}
	public String getStrstart() {
		return strstart;
	}
	public void setStrstart(String strstart) {
		this.strstart = strstart;
	}
	public String getStrend() {
		return strend;
	}
	public void setStrend(String strend) {
		this.strend = strend;
	}
	public String getMd5orlength() {
		return md5orlength;
	}
	public void setMd5orlength(String md5orlength) {
		this.md5orlength = md5orlength;
	}
	public String getWebtype() {
		return webtype;
	}
	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	
}
