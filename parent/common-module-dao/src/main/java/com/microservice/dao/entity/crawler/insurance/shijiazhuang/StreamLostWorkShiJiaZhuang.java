package com.microservice.dao.entity.crawler.insurance.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name = "insurance_shijiazhuang_losework",indexes = {@Index(name = "index_insurance_shijiazhuang_losework_taskid", columnList = "taskid")})
public class StreamLostWorkShiJiaZhuang extends IdEntity implements Serializable{
	private static final long serialVersionUID = 4117131556025595883L;
	private String taskid;
	private String streamlostwork_insur_name; //  保险名称
	private String streamlostwork_insur_pay_base;	 //	缴纳基数
	private String streamlostwork_insur_pay_per; //	个人缴费金额
	private String streamlostwork_insur_pay_comp; //	单位缴费金额
	private String streamlostwork_insur_pay_date; //	缴费时间(结算时间)
	private String streamlostwork_insur_belong_date;  //费款所属期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStreamlostwork_insur_name() {
		return streamlostwork_insur_name;
	}
	public void setStreamlostwork_insur_name(String streamlostwork_insur_name) {
		this.streamlostwork_insur_name = streamlostwork_insur_name;
	}
	public String getStreamlostwork_insur_pay_base() {
		return streamlostwork_insur_pay_base;
	}
	public void setStreamlostwork_insur_pay_base(String streamlostwork_insur_pay_base) {
		this.streamlostwork_insur_pay_base = streamlostwork_insur_pay_base;
	}
	public String getStreamlostwork_insur_pay_per() {
		return streamlostwork_insur_pay_per;
	}
	public void setStreamlostwork_insur_pay_per(String streamlostwork_insur_pay_per) {
		this.streamlostwork_insur_pay_per = streamlostwork_insur_pay_per;
	}
	public String getStreamlostwork_insur_pay_comp() {
		return streamlostwork_insur_pay_comp;
	}
	public void setStreamlostwork_insur_pay_comp(String streamlostwork_insur_pay_comp) {
		this.streamlostwork_insur_pay_comp = streamlostwork_insur_pay_comp;
	}
	public String getStreamlostwork_insur_pay_date() {
		return streamlostwork_insur_pay_date;
	}
	public void setStreamlostwork_insur_pay_date(String streamlostwork_insur_pay_date) {
		this.streamlostwork_insur_pay_date = streamlostwork_insur_pay_date;
	}
	public String getStreamlostwork_insur_belong_date() {
		return streamlostwork_insur_belong_date;
	}
	public void setStreamlostwork_insur_belong_date(String streamlostwork_insur_belong_date) {
		this.streamlostwork_insur_belong_date = streamlostwork_insur_belong_date;
	}
	public StreamLostWorkShiJiaZhuang() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StreamLostWorkShiJiaZhuang(String taskid, String streamlostwork_insur_name,
			String streamlostwork_insur_pay_base, String streamlostwork_insur_pay_per,
			String streamlostwork_insur_pay_comp, String streamlostwork_insur_pay_date,
			String streamlostwork_insur_belong_date) {
		super();
		this.taskid = taskid;
		this.streamlostwork_insur_name = streamlostwork_insur_name;
		this.streamlostwork_insur_pay_base = streamlostwork_insur_pay_base;
		this.streamlostwork_insur_pay_per = streamlostwork_insur_pay_per;
		this.streamlostwork_insur_pay_comp = streamlostwork_insur_pay_comp;
		this.streamlostwork_insur_pay_date = streamlostwork_insur_pay_date;
		this.streamlostwork_insur_belong_date = streamlostwork_insur_belong_date;
	}
	
}
