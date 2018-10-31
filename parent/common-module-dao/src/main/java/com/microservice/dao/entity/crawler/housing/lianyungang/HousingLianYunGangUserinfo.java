package com.microservice.dao.entity.crawler.housing.lianyungang;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import com.microservice.dao.entity.IdEntity;

@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="housing_lianyungang_userinfo")
public class HousingLianYunGangUserinfo  extends IdEntity implements Serializable {
		
	private String taskid;
	private String personalNum;					//个人账号
	private String companyNum;					//单位账号
	private String companyName;					//缴存单位名称
	private String mark;						//备注
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	@Override
	public String toString() {
		return "HousingLianYunGangUserinfo [taskid=" + taskid + ", personalNum=" + personalNum + ", companyNum="
				+ companyNum + ", companyName=" + companyName + ", mark=" + mark + "]";
	}
	
}
