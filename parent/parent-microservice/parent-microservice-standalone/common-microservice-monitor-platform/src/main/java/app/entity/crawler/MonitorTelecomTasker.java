package app.entity.crawler;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;


import com.microservice.dao.entity.IdEntity;

/**
 * @description: 监测可以跳过短信验证码的电信，传参数执行（即电信爬取的可执行）
 * 					
 * @author: sln 
 */
@Entity
@Table(name = "monitor_telecom_tasker")
public class MonitorTelecomTasker extends IdEntity implements Serializable {
	private static final long serialVersionUID = 4918169247990622081L;
//	姓名
	private String name;
//	身份证号
	private String idnum;
//	手机号
	private String phonenum;
//	服务密码
	private String servicepwd;
//	所属省份
	private String province;
//	所属城市
	private String city;  
//	负责人
	private String developer;
//	是否需要监控（1——需要，0——暂时不需要）
	private Integer isneedmonitor;
	private String oncesmskey;  //一次短信验证码中包含的关键字
	private String twicesmskey;   //二次短信验证码中包含的关键字
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getServicepwd() {
		return servicepwd;
	}
	public void setServicepwd(String servicepwd) {
		this.servicepwd = servicepwd;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public Integer getIsneedmonitor() {
		return isneedmonitor;
	}
	public void setIsneedmonitor(Integer isneedmonitor) {
		this.isneedmonitor = isneedmonitor;
	}
	public String getOncesmskey() {
		return oncesmskey;
	}
	public void setOncesmskey(String oncesmskey) {
		this.oncesmskey = oncesmskey;
	}
	public String getTwicesmskey() {
		return twicesmskey;
	}
	public void setTwicesmskey(String twicesmskey) {
		this.twicesmskey = twicesmskey;
	}
	@Override
	public String toString() {
		return "MonitorTelecomTasker [name=" + name + ", idnum=" + idnum + ", phonenum=" + phonenum + ", servicepwd="
				+ servicepwd + ", province=" + province + ", city=" + city + ", developer=" + developer
				+ ", isneedmonitor=" + isneedmonitor + ", oncesmskey=" + oncesmskey + ", twicesmskey=" + twicesmskey
				+ "]";
	}
	
}
