package com.microservice.dao.entity.crawler.insurance.zhengzhou;

import com.microservice.dao.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 郑州社保个人信息
 * @author kaixu
 *
 */
@Entity
@Table(name="insurance_zhengzhou_userinfo")
public class InsuranceZhengzhouUserInfo extends IdEntity  implements Serializable {
	private static final long serialVersionUID = -1646403919031277347L;
	/** 爬取批次号 */
	@Column(name="task_id")
	private String taskId;

	@Column(name = "cjbh")
	private String cjbh;

	/**身份证号码*/
	@Column(name = "id_no")
	private String idNo;

	/**姓名*/
	@Column(name = "name")
	private String name;

	/**个人编号*/
	@Column(name = "personel_no")
	private String personelNo;

	/**社保卡号*/
	@Column(name = "card_no")
	private String cardNo;

	@Column(name = "area_name")
	private String areaName;

	@Column(name = "area_no")
	private String areaNo;

	/**单位编号*/
	@Column(name = "company_id")
	private String companyId;

	/**单位名称*/
	@Column(name = "company_name")
	private String companyName;

	/**卡 余 额*/
	@Column(name = "balance")
	private String balance;

	/**缴费基数*/
	@Column(name = "pay_base")
	private String payBase;
	/* 参保状态 1、正常参保，2、已停保3、已退保 */
	@Column(name = "insure_type")
	private Integer insureType;
	/* 旧卡标识 */
	@Column(name = "old_card_flag")
	private Integer oldCardFlag;
	/* 卡状态-办理状态，
		0：初始值
		1：已打印
		2：已缴费
		3：发卡在途
		4：回卡成功
		5：回卡失败
		6：持卡正常
		7：挂失
		8：补卡
	*/
	@Column(name = "step_flag")
	private Integer stepFlag;

	@Column(name = "person_type")
	private Integer personType;




	public InsuranceZhengzhouUserInfo() {
	}

	public InsuranceZhengzhouUserInfo(String cjbh, String idNo, String name, String personelNo, String cardNo, String areaName, String areaNo, String companyId, String companyName, String balance, String payBase, Integer insureType, Integer oldCardFlag, Integer stepFlag, Integer personFype) {
		this.cjbh = cjbh;
		this.idNo = idNo;
		this.name = name;
		this.personelNo = personelNo;
		this.cardNo = cardNo;
		this.areaName = areaName;
		this.areaNo = areaNo;
		this.companyId = companyId;
		this.companyName = companyName;
		this.balance = balance;
		this.payBase = payBase;
		this.insureType = insureType;
		this.oldCardFlag = oldCardFlag;
		this.stepFlag = stepFlag;
		this.personType = personType;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCjbh() {
		return cjbh;
	}

	public void setCjbh(String cjbh) {
		this.cjbh = cjbh;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonelNo() {
		return personelNo;
	}

	public void setPersonelNo(String personelNo) {
		this.personelNo = personelNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getPayBase() {
		return payBase;
	}

	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}

	public Integer getInsureType() {
		return insureType;
	}

	public void setInsureType(Integer insureType) {
		this.insureType = insureType;
	}

	public Integer getOldCardFlag() {
		return oldCardFlag;
	}

	public void setOldCardFlag(Integer oldCardFlag) {
		this.oldCardFlag = oldCardFlag;
	}

	public Integer getStepFlag() {
		return stepFlag;
	}

	public void setStepFlag(Integer stepFlag) {
		this.stepFlag = stepFlag;
	}

	public Integer getPersonType() {
		return personType;
	}

	public void setPersonType(Integer personFype) {
		this.personType = personFype;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "InsuranceZhengzhouUserInfo{" +
				"cjbh='" + cjbh + '\'' +
				", idNo='" + idNo + '\'' +
				", name='" + name + '\'' +
				", personelNo='" + personelNo + '\'' +
				", cardNo='" + cardNo + '\'' +
				", areaName='" + areaName + '\'' +
				", areaNo='" + areaNo + '\'' +
				", companyId='" + companyId + '\'' +
				", companyName='" + companyName + '\'' +
				", balance='" + balance + '\'' +
				", payBase='" + payBase + '\'' +
				", insureType=" + insureType +
				", oldCardFlag=" + oldCardFlag +
				", stepFlag=" + stepFlag +
				", personFype=" + personType +
				'}';
	}
}
