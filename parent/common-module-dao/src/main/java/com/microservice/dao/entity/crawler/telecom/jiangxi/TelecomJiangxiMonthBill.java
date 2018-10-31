package com.microservice.dao.entity.crawler.telecom.jiangxi;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * @Description  月账单
 * @author sln
 * @date 2017年9月16日
 */
@Entity
@Table(name = "telecom_jiangxi_monthbill",indexes = {@Index(name = "index_telecom_jiangxi_monthbill_taskid", columnList = "taskid")})
public class TelecomJiangxiMonthBill extends IdEntity implements Serializable {
	private static final long serialVersionUID = 6286496636357566920L;
//	所属账期
	private String belongmonth;
//  接入号码
	private String accessnum;
//	打印时间
	private String printtime;
//	费用类型
	private String feetype;
//	费用金额
	private String fee;
//	本期费用总额
	private float monthtotalcost;
	private String taskid;
	public String getBelongmonth() {
		return belongmonth;
	}
	public void setBelongmonth(String belongmonth) {
		this.belongmonth = belongmonth;
	}
	public String getAccessnum() {
		return accessnum;
	}
	public void setAccessnum(String accessnum) {
		this.accessnum = accessnum;
	}
	public String getPrinttime() {
		return printtime;
	}
	public void setPrinttime(String printtime) {
		this.printtime = printtime;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	
	public float getMonthtotalcost() {
		return monthtotalcost;
	}
	public void setMonthtotalcost(float monthtotalcost) {
		this.monthtotalcost = monthtotalcost;
	}
	public TelecomJiangxiMonthBill() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TelecomJiangxiMonthBill(String belongmonth, String accessnum, String printtime, String feetype, String fee,
			float monthtotalcost, String taskid) {
		super();
		this.belongmonth = belongmonth;
		this.accessnum = accessnum;
		this.printtime = printtime;
		this.feetype = feetype;
		this.fee = fee;
		this.monthtotalcost = monthtotalcost;
		this.taskid = taskid;
	}
	
	
}

