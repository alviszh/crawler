package com.microservice.dao.entity.crawler.housing.tianshui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 个人缴存账户
 * @author tz
 *
 */
@Entity
@Table(name = "housing_tianshui_accountinfo" ,indexes = {@Index(name = "index_housing_tianshui_accountinfo_taskid", columnList = "taskid")})
public class HousingTianshuiAccountInfo extends IdEntity {

	/**
	 * taskid uuid 前端通过uuid访问状态结果
	 */
	private String taskid;
	
	/**
	 * 单位编号
	 */
	private String corpcode;

	/**
	 * 单位名称
	 */
	private String corpname;

	/**
	 * 缴存基数
	 */
	private String bmny;

	/**
	 * 单位月缴额
	 */
	private String corpdepmny;

	/**
	 * 个人月缴额
	 */
	private String perdepmny;

	/**
	 * 合计月缴额
	 */
	private String depmny;
	
	/**
	 * 缴止月份
	 */
	private String payendmnh;
	
	/**
	 * 缴存状态
	 * {"id":"01","name":"正常"},
	 * {"id":"02","name":"封存"},
	 * {"id":"03","name":"托管"},
	 * {"id":"04","name":"销户"},
	 * {"id":"09","name":"待生效"}
	 */
	private String depstate;

	/**
	 * 账户余额
	 */
	private String accbal;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getBmny() {
		return bmny;
	}

	public void setBmny(String bmny) {
		this.bmny = bmny;
	}

	public String getCorpdepmny() {
		return corpdepmny;
	}

	public void setCorpdepmny(String corpdepmny) {
		this.corpdepmny = corpdepmny;
	}

	public String getPerdepmny() {
		return perdepmny;
	}

	public void setPerdepmny(String perdepmny) {
		this.perdepmny = perdepmny;
	}

	public String getDepmny() {
		return depmny;
	}

	public void setDepmny(String depmny) {
		this.depmny = depmny;
	}

	public String getPayendmnh() {
		return payendmnh;
	}

	public void setPayendmnh(String payendmnh) {
		this.payendmnh = payendmnh;
	}

	public String getDepstate() {
		return depstate;
	}

	public void setDepstate(String depstate) {
		this.depstate = depstate;
	}

	public String getAccbal() {
		return accbal;
	}

	public void setAccbal(String accbal) {
		this.accbal = accbal;
	}

	public HousingTianshuiAccountInfo(String taskid, String corpcode, String corpname, String bmny, String corpdepmny,
			String perdepmny, String depmny, String payendmnh, String depstate, String accbal) {
		super();
		this.taskid = taskid;
		this.corpcode = corpcode;
		this.corpname = corpname;
		this.bmny = bmny;
		this.corpdepmny = corpdepmny;
		this.perdepmny = perdepmny;
		this.depmny = depmny;
		this.payendmnh = payendmnh;
		this.depstate = depstate;
		this.accbal = accbal;
	}

	public HousingTianshuiAccountInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
