package app.entity.crawler;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 银行定时爬取任务
 * 					
 * @author: sln 
 */
@Entity
@Table(name = "monitor_bank_tasker")
public class MonitorBankTasker extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4731512033347864152L;
	private String webtype;    //XX银行XX卡
	private String developer;   //负责人
	private String paramsjson;    //参数json串(其中已经包含了银行类型、卡类型、登陆类型)
	private Integer isneedmonitor; //	是否需要监控（1——需要，0——暂时不需要）
	//============================
	//如下用于创建taskid时使用
	public String username;
	public String idnum;
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
	public String getParamsjson() {
		return paramsjson;
	}
	public void setParamsjson(String paramsjson) {
		this.paramsjson = paramsjson;
	}
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	@Override
	public String toString() {
		return "MonitorBankTasker [webtype=" + webtype + ", developer=" + developer + ", paramsjson=" + paramsjson
				+ ", isneedmonitor=" + isneedmonitor + ", username=" + username + ", idnum=" + idnum + "]";
	}
}
