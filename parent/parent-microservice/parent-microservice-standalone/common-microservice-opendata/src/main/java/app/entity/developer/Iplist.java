package app.entity.developer;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * IP
 * 
 * @author tz
 *
 */
@Entity
@Table(name = "opendata_iplist")
public class Iplist extends IdEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4961348868422315732L;

	/**
	 * 应用
	 */
	private App app;
	
	/**
	 * iP
	 */
	private String ip;
	/**
	 * 端口
	 */
	private String port;
	/**
	 * ip描述
	 */
	private String describe;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "app_id")
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}


}