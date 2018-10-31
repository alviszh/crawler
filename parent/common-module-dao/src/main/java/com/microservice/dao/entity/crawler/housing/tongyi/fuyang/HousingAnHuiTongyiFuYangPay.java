package com.microservice.dao.entity.crawler.housing.tongyi.fuyang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name = "housing_anhui_tongyi_fuyang_pay",indexes = {@Index(name = "index_anhui_tongyi_fuyang_taskid", columnList = "taskid")})
public class HousingAnHuiTongyiFuYangPay extends IdEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String QRRQ;//确认日期
	private String RNUM;//排序
	private String CLLX;//业务类型
	private String DWMC;//单位名称
	private String ZC;//0 未知含义
	private String YE;//余额
	private String LX;//0 未知含义
	private String HJNY;//对应年月
	private String XINGMING;//姓名
	private String SR;//收入

	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setQRRQ(String QRRQ) {
		this.QRRQ = QRRQ;
	}

	public String getQRRQ() {
		return QRRQ;
	}

	public void setRNUM(String RNUM) {
		this.RNUM = RNUM;
	}

	public String getRNUM() {
		return RNUM;
	}

	public void setCLLX(String CLLX) {
		this.CLLX = CLLX;
	}

	public String getCLLX() {
		return CLLX;
	}

	public void setDWMC(String DWMC) {
		this.DWMC = DWMC;
	}

	public String getDWMC() {
		return DWMC;
	}

	public void setZC(String ZC) {
		this.ZC = ZC;
	}

	public String getZC() {
		return ZC;
	}

	public void setYE(String YE) {
		this.YE = YE;
	}

	public String getYE() {
		return YE;
	}

	public void setLX(String LX) {
		this.LX = LX;
	}

	public String getLX() {
		return LX;
	}

	public void setHJNY(String HJNY) {
		this.HJNY = HJNY;
	}

	public String getHJNY() {
		return HJNY;
	}

	public void setXINGMING(String XINGMING) {
		this.XINGMING = XINGMING;
	}

	public String getXINGMING() {
		return XINGMING;
	}

	public void setSR(String SR) {
		this.SR = SR;
	}

	public String getSR() {
		return SR;
	}

	@Override
	public String toString() {
		return "HousingAnHuiTongyiFuYangPay [QRRQ=" + QRRQ + ", RNUM=" + RNUM + ", CLLX=" + CLLX + ", DWMC=" + DWMC
				+ ", ZC=" + ZC + ", YE=" + YE + ", LX=" + LX + ", HJNY=" + HJNY + ", XINGMING=" + XINGMING + ", SR="
				+ SR + "]";
	}

}
