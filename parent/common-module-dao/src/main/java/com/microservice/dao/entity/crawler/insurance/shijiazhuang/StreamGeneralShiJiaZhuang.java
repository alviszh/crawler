package com.microservice.dao.entity.crawler.insurance.shijiazhuang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;
/**
 * 
 * @Description: 银行流水信息实体，为减少数据冗余，将四种保险的公有字段提取出来
 * @author sln
 * @date 2017年7月27日
 */
@Entity
@Table(name = "insurance_shijiazhuang_streamgeneral",indexes = {@Index(name = "index_insurance_shijiazhuang_streamgeneral_taskid", columnList = "taskid")})
public class StreamGeneralShiJiaZhuang extends IdEntity implements Serializable{
	private static final long serialVersionUID = -9221902182701368979L;
	private String taskid;
	private String streamgeneral_insur_type; //	保险类型(名称)
	private String streamgeneral_company_num; //	公司编号
	private String streamgeneral_company_name; //	公司名称	
	//社保个人编号：(石家庄有四险，每个都有自己的独有编号)
	private String streamgeneral_insur＿type_num;   //保险类型对应的个人编号
	private String streamgeneral_per_status; //	人员状态	
	private String streamgeneral_insur_status; //	社保状态
	private String streamgeneral_insur_start_date;  //	参保缴费起始日期
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getStreamgeneral_insur_type() {
		return streamgeneral_insur_type;
	}
	public void setStreamgeneral_insur_type(String streamgeneral_insur_type) {
		this.streamgeneral_insur_type = streamgeneral_insur_type;
	}
	public String getStreamgeneral_company_num() {
		return streamgeneral_company_num;
	}
	public void setStreamgeneral_company_num(String streamgeneral_company_num) {
		this.streamgeneral_company_num = streamgeneral_company_num;
	}
	public String getStreamgeneral_company_name() {
		return streamgeneral_company_name;
	}
	public void setStreamgeneral_company_name(String streamgeneral_company_name) {
		this.streamgeneral_company_name = streamgeneral_company_name;
	}
	public String getStreamgeneral_insur_status() {
		return streamgeneral_insur_status;
	}
	public void setStreamgeneral_insur_status(String streamgeneral_insur_status) {
		this.streamgeneral_insur_status = streamgeneral_insur_status;
	}
	public String getStreamgeneral_insur_start_date() {
		return streamgeneral_insur_start_date;
	}
	public void setStreamgeneral_insur_start_date(String streamgeneral_insur_start_date) {
		this.streamgeneral_insur_start_date = streamgeneral_insur_start_date;
	}
	public String getStreamgeneral_insur＿type_num() {
		return streamgeneral_insur＿type_num;
	}
	public void setStreamgeneral_insur＿type_num(String streamgeneral_insur＿type_num) {
		this.streamgeneral_insur＿type_num = streamgeneral_insur＿type_num;
	}
	public String getStreamgeneral_per_status() {
		return streamgeneral_per_status;
	}
	public void setStreamgeneral_per_status(String streamgeneral_per_status) {
		this.streamgeneral_per_status = streamgeneral_per_status;
	}
	@Override
	public String toString() {
		return "StreamGeneralShiJiaZhuang [taskid=" + taskid + ", streamgeneral_insur_type=" + streamgeneral_insur_type
				+ ", streamgeneral_company_num=" + streamgeneral_company_num + ", streamgeneral_company_name="
				+ streamgeneral_company_name + ", streamgeneral_insur＿type_num=" + streamgeneral_insur＿type_num
				+ ", streamgeneral_per_status=" + streamgeneral_per_status + ", streamgeneral_insur_status="
				+ streamgeneral_insur_status + ", streamgeneral_insur_start_date=" + streamgeneral_insur_start_date
				+ "]";
	}
	
	public StreamGeneralShiJiaZhuang(String taskid, String streamgeneral_insur_type, String streamgeneral_company_num,
			String streamgeneral_company_name, String streamgeneral_insur＿type_num, String streamgeneral_per_status,
			String streamgeneral_insur_status, String streamgeneral_insur_start_date) {
		super();
		this.taskid = taskid;
		this.streamgeneral_insur_type = streamgeneral_insur_type;
		this.streamgeneral_company_num = streamgeneral_company_num;
		this.streamgeneral_company_name = streamgeneral_company_name;
		this.streamgeneral_insur＿type_num = streamgeneral_insur＿type_num;
		this.streamgeneral_per_status = streamgeneral_per_status;
		this.streamgeneral_insur_status = streamgeneral_insur_status;
		this.streamgeneral_insur_start_date = streamgeneral_insur_start_date;
	}
	public StreamGeneralShiJiaZhuang() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
