package com.microservice.dao.entity.crawler.housing.tianshui;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 缴存记录查询
 * @author tz
 *
 */
@Entity
@Table(name = "housing_tianshui_payrecord" ,indexes = {@Index(name = "index_housing_tianshui_payrecord_taskid", columnList = "taskid")})
public class HousingTianshuiPayRecord extends IdEntity {

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
	 * 业务类型
	 * {"id":"1","name":"汇缴登记"},
	 * {"id":"2","name":"补缴登记"},
	 * {"id":"3","name":"暂收登记"}
	 */
	private String paybustype;

	/**
	 * 缴存类型
	 * {"id":"01","name":"主缴存帐户"},
	 * {"id":"02","name":"补充公积金"}
	 */
	private String deptype;

	/**
	 * 缴款月份起
	 */
	private String starmnh;

	/**
	 * 缴款月份止
	 */
	private String endmnh;
	
	/**
	 * 单位缴存额
	 */
	private String corpdepmny;
	
	/**
	 * 个人缴存额
	 */
	private String perdepmny;

	/**
	 * 合计缴存额
	 */
	private String depmny;
	
	/**
	 * 业务受理时间
	 */
	private String dotime;
	
	/**
	 * 到账时间时间---json里没有
	 */
	private String suertime;

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

	public String getPaybustype() {
		return paybustype;
	}

	public void setPaybustype(String paybustype) {
		this.paybustype = paybustype;
	}

	public String getDeptype() {
		return deptype;
	}

	public void setDeptype(String deptype) {
		this.deptype = deptype;
	}

	public String getStarmnh() {
		return starmnh;
	}

	public void setStarmnh(String starmnh) {
		this.starmnh = starmnh;
	}

	public String getEndmnh() {
		return endmnh;
	}

	public void setEndmnh(String endmnh) {
		this.endmnh = endmnh;
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

	public String getDotime() {
		return dotime;
	}

	public void setDotime(String dotime) {
		this.dotime = dotime;
	}

	public String getSuertime() {
		return suertime;
	}

	public void setSuertime(String suertime) {
		this.suertime = suertime;
	}

	public HousingTianshuiPayRecord(String taskid, String corpcode, String corpname, String paybustype, String deptype,
			String starmnh, String endmnh, String corpdepmny, String perdepmny, String depmny, String dotime,
			String suertime) {
		super();
		this.taskid = taskid;
		this.corpcode = corpcode;
		this.corpname = corpname;
		this.paybustype = paybustype;
		this.deptype = deptype;
		this.starmnh = starmnh;
		this.endmnh = endmnh;
		this.corpdepmny = corpdepmny;
		this.perdepmny = perdepmny;
		this.depmny = depmny;
		this.dotime = dotime;
		this.suertime = suertime;
	}

	public HousingTianshuiPayRecord() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
