package app.entity.system;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;


/**
 * @author sln
 * @date 2018年8月27日下午5:16:23
 * @Description:弃用之前监控rancher的表，用此表，直接指定rancher不通环境的基础地址
 */
@Entity
@Table(name = "monitor_rancher_info")
public class MonitorRancherInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 5712823021092325790L;
	private String baseaddr;  //基础地址
	private String envirtype;  //环境类型
	private Integer ismonitor;    //是否需要监控
	public String getBaseaddr() {
		return baseaddr;
	}
	public void setBaseaddr(String baseaddr) {
		this.baseaddr = baseaddr;
	}
	public String getEnvirtype() {
		return envirtype;
	}
	public void setEnvirtype(String envirtype) {
		this.envirtype = envirtype;
	}
	public Integer getIsmonitor() {
		return ismonitor;
	}
	public void setIsmonitor(Integer ismonitor) {
		this.ismonitor = ismonitor;
	}
	
}
