package com.microservice.dao.entity.crawler.insurance.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "insurance_shijiazhuang_medical",indexes = {@Index(name = "index_insurance_shijiazhuang_medical_taskid", columnList = "taskid")})
public class StreamMedicalShiJiaZhuang extends IdEntity implements Serializable{
	private static final long serialVersionUID = 6244680388715832073L;
	private String taskid;
	private String streammedical_insur_name; //  缴费类型（保险名称）
	private String streammedical_insur_pay_base;	 //	缴纳基数
	private String streammedical_insur_pay_per; //	个人缴费金额
	private String streammedical_insur_pay_comp; //	单位缴费金额
	private String streammedical_insur_pay_date; //	缴费时间(结算期)
	private String streammedical_insur_belong_date;  //费款所属期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStreammedical_insur_name() {
		return streammedical_insur_name;
	}
	public void setStreammedical_insur_name(String streammedical_insur_name) {
		this.streammedical_insur_name = streammedical_insur_name;
	}
	public String getStreammedical_insur_pay_base() {
		return streammedical_insur_pay_base;
	}
	public void setStreammedical_insur_pay_base(String streammedical_insur_pay_base) {
		this.streammedical_insur_pay_base = streammedical_insur_pay_base;
	}
	public String getStreammedical_insur_pay_per() {
		return streammedical_insur_pay_per;
	}
	public void setStreammedical_insur_pay_per(String streammedical_insur_pay_per) {
		this.streammedical_insur_pay_per = streammedical_insur_pay_per;
	}
	public String getStreammedical_insur_pay_comp() {
		return streammedical_insur_pay_comp;
	}
	public void setStreammedical_insur_pay_comp(String streammedical_insur_pay_comp) {
		this.streammedical_insur_pay_comp = streammedical_insur_pay_comp;
	}
	public String getStreammedical_insur_pay_date() {
		return streammedical_insur_pay_date;
	}
	public void setStreammedical_insur_pay_date(String streammedical_insur_pay_date) {
		this.streammedical_insur_pay_date = streammedical_insur_pay_date;
	}
	public String getStreammedical_insur_belong_date() {
		return streammedical_insur_belong_date;
	}
	public void setStreammedical_insur_belong_date(String streammedical_insur_belong_date) {
		this.streammedical_insur_belong_date = streammedical_insur_belong_date;
	}
	public StreamMedicalShiJiaZhuang() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StreamMedicalShiJiaZhuang(String taskid, String streammedical_insur_name,
			String streammedical_insur_pay_base, String streammedical_insur_pay_per,
			String streammedical_insur_pay_comp, String streammedical_insur_pay_date,
			String streammedical_insur_belong_date) {
		super();
		this.taskid = taskid;
		this.streammedical_insur_name = streammedical_insur_name;
		this.streammedical_insur_pay_base = streammedical_insur_pay_base;
		this.streammedical_insur_pay_per = streammedical_insur_pay_per;
		this.streammedical_insur_pay_comp = streammedical_insur_pay_comp;
		this.streammedical_insur_pay_date = streammedical_insur_pay_date;
		this.streammedical_insur_belong_date = streammedical_insur_belong_date;
	}
}
