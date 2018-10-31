package com.microservice.dao.entity.crawler.housing.tianjin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @description: 
 * @author: qzb
 * @date: 2017年9月29日 上午9:58:45 
 */
@Entity
@Table(name="housing_tianjin_base")
public class HousingTianJinBase extends IdEntity implements Serializable {
	private static final long serialVersionUID = -281963208057393328L;
	@Column(name="taskid")
	private String taskid;		
	
	//职工姓名
	@Column(name="name")
	private String name;
	
	//单位名称
	@Column(name="company")
	private String company;
	
	//开户管理部
	@Column(name="management")
	private String management;
	
	//个人代码
	@Column(name="person_code")
	private String person_code;
	
	//单位代码
	@Column(name="company_code")
	private String company_code;
	
	//开户网点代码
	@Column(name="kaihuwangdian_code")
	private String kaihuwangdian_code;
	
	//身份证号
	@Column(name="card")
	private String card;
	
	//龙卡卡号
	@Column(name="longka_card")
	private String longka_card;
	
	//状态
	@Column(name="status")
	private String status;
	
	//开户时间
	@Column(name="open_date")
	private String open_date;
	
	//单位比例
	@Column(name="company_proportion")
	private String company_proportion;
	
	//个人比例
	@Column(name="person_proportion")
	private String person_proportion;
	
	//缴存基数 
	@Column(name="pay_cardinal")
	private String pay_cardinal;
	
	
	
	
	
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCompany_proportion() {
		return company_proportion;
	}
	public void setCompany_proportion(String company_proportion) {
		this.company_proportion = company_proportion;
	}
	public String getPerson_proportion() {
		return person_proportion;
	}
	public void setPerson_proportion(String person_proportion) {
		this.person_proportion = person_proportion;
	}
	public String getPay_cardinal() {
		return pay_cardinal;
	}
	public void setPay_cardinal(String pay_cardinal) {
		this.pay_cardinal = pay_cardinal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	public String getPerson_code() {
		return person_code;
	}
	public void setPerson_code(String person_code) {
		this.person_code = person_code;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getKaihuwangdian_code() {
		return kaihuwangdian_code;
	}
	public void setKaihuwangdian_code(String kaihuwangdian_code) {
		this.kaihuwangdian_code = kaihuwangdian_code;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getLongka_card() {
		return longka_card;
	}
	public void setLongka_card(String longka_card) {
		this.longka_card = longka_card;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOpen_date() {
		return open_date;
	}
	public void setOpen_date(String open_date) {
		this.open_date = open_date;
	}
	
}
