package com.microservice.dao.entity.crawler.telecom.shanxi3;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  缴费信息实体(该实体的信息需要从余额变动中查询，故顺带着将余额信息也存储了)
 * @author sln
 * @date 2017年8月24日 上午10:42:36
 */
@Entity
@Table(name = "telecom_shanxi3_chargeinfo",indexes = {@Index(name = "index_telecom_shanxi3_chargeinfo_taskid", columnList = "taskid")})
public class TelecomShanxi3ChargeInfo extends IdEntity implements Serializable {
	private static final long serialVersionUID = 3025307308277816675L;
//	流水号
	private String flownum;
//	入账时间
	private String chargetime;
//	入账金额
	private String chargemoney;
//	交费渠道
	private String chargechanal;
//	交费方式
	private String chargeway;
//	使用范围
	private String usescope;
//	查询周期
	private String querycycle;
	private String taskid;
	public String getFlownum() {
		return flownum;
	}
	public void setFlownum(String flownum) {
		this.flownum = flownum;
	}
	public String getChargetime() {
		return chargetime;
	}
	public void setChargetime(String chargetime) {
		this.chargetime = chargetime;
	}
	public String getChargemoney() {
		return chargemoney;
	}
	public void setChargemoney(String chargemoney) {
		this.chargemoney = chargemoney;
	}
	public String getChargeway() {
		return chargeway;
	}
	public void setChargeway(String chargeway) {
		this.chargeway = chargeway;
	}
	public String getUsescope() {
		return usescope;
	}
	public void setUsescope(String usescope) {
		this.usescope = usescope;
	}
	public String getQuerycycle() {
		return querycycle;
	}
	public void setQuerycycle(String querycycle) {
		this.querycycle = querycycle;
	}
	public TelecomShanxi3ChargeInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getChargechanal() {
		return chargechanal;
	}
	public void setChargechanal(String chargechanal) {
		this.chargechanal = chargechanal;
	}
	public TelecomShanxi3ChargeInfo(String flownum, String chargetime, String chargemoney, String chargechanal,
			String chargeway, String usescope, String querycycle, String taskid) {
		super();
		this.flownum = flownum;
		this.chargetime = chargetime;
		this.chargemoney = chargemoney;
		this.chargechanal = chargechanal;
		this.chargeway = chargeway;
		this.usescope = usescope;
		this.querycycle = querycycle;
		this.taskid = taskid;
	}
	
	
}

