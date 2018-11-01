package app.entity.crawler;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description:  该表用于具体说明每一种处理方式下，具体的处理切入点是什么，比如指明具体的切割字符串的起止位置
 * @author: sln 
 * @date: 2018年3月26日 上午10:32:51 
 */
/**
 * 	对html源码的处理方法：
 * 
 *  removeByElementLocate;  //根据元素定位并且移除
	onlyRemainByElementLocate;  //根据元素定位，只留取这部分
	setEmptyByElementLocate;  //根据元素定位，根据属性置空变化的部分
	remainSourceCodeByAdd;    //根据所指定的字符串的起止位置截取该部分，最后进行累计（加法思路）
	replaceAllNoteSymbol;   //去除注释符号
	setEmptyBySplitAndReplace  //随记变化的内容多处出现，且相同，无法通过元素定位，如青岛社保，变化的内容还有时间信息
 *
 */
@Entity
@Table(name = "monitor_aftertreat_html")
public class MonitorHtmlAfterTreat extends IdEntity implements Serializable {
	private static final long serialVersionUID = -7299032560004801388L;
	private String url;
	private String webtype;
	private String treatmethod;  //处理方法	
	private String strstart;   //截取字符串的起始位置
	private String strend;    //截取字符串的终止位置
	private String elementselect;  //写成key=value形式,如name=userForm
	private String attribute;   //定位元素之后，将会变化的属性值置空
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
	public String getElementselect() {
		return elementselect;
	}
	public void setElementselect(String elementselect) {
		this.elementselect = elementselect;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
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
