package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description 亲情号信息实体
 * @author sln
 * @date 2017年8月24日 上午9:53:54
 */
@Entity
@Table(name = "telecom_shanxi3_familycall",indexes = {@Index(name = "index_telecom_shanxi3_familycall_taskid", columnList = "taskid")})
public class TelecomShanxi3FamilyCall extends IdEntity implements Serializable {
	private static final long serialVersionUID = -8796232170889424041L;
	private String taskid;
	// 本机号码
	private String ownnum;
	// 亲情号码
	private String familynum;
	// 主叫时长（秒）
	private String originatingcalltime;
	// 被叫时长（秒）
	private String terminatingcalltime;
	// 合计时长（秒）
	private String totalcalltime;
	//所属时间段(亲情号通话时间段网页上并没有显示，所以决定用所属时间段来区分是哪个月的)
	private String belongmonth;
	
	public String getBelongmonth() {
		return belongmonth;
	}
	public void setBelongmonth(String belongmonth) {
		this.belongmonth = belongmonth;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOwnnum() {
		return ownnum;
	}
	public void setOwnnum(String ownnum) {
		this.ownnum = ownnum;
	}
	public String getFamilynum() {
		return familynum;
	}
	public void setFamilynum(String familynum) {
		this.familynum = familynum;
	}
	public String getOriginatingcalltime() {
		return originatingcalltime;
	}
	public void setOriginatingcalltime(String originatingcalltime) {
		this.originatingcalltime = originatingcalltime;
	}
	public String getTerminatingcalltime() {
		return terminatingcalltime;
	}
	public void setTerminatingcalltime(String terminatingcalltime) {
		this.terminatingcalltime = terminatingcalltime;
	}
	public String getTotalcalltime() {
		return totalcalltime;
	}
	public void setTotalcalltime(String totalcalltime) {
		this.totalcalltime = totalcalltime;
	}
	public TelecomShanxi3FamilyCall() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomShanxi3FamilyCall(String taskid, String ownnum, String familynum, String originatingcalltime,
			String terminatingcalltime, String totalcalltime, String belongmonth) {
		super();
		this.taskid = taskid;
		this.ownnum = ownnum;
		this.familynum = familynum;
		this.originatingcalltime = originatingcalltime;
		this.terminatingcalltime = terminatingcalltime;
		this.totalcalltime = totalcalltime;
		this.belongmonth = belongmonth;
	}
	
}
