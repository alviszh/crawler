
package com.microservice.dao.entity.crawler.honesty.shixin;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.microservice.dao.entity.crawler.executor.IdEntityExecutor;


/**   
*    
* 项目名称：common-module-dao   
* 类名称：ShiXinBean   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2018年7月12日 下午3:31:57   
* @version        
*/
@Entity
@DynamicUpdate(true)
@DynamicInsert
@Table(name  ="executor_shixin",indexes = {@Index(name = "index_shixin_shixinid", columnList = "shixinid")})
public class ShiXinBean  extends IdEntityExecutor implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long shixinid; //失信被执行人id或被执行人id
    private String iname; //姓名或公司名
    private String caseCode;//案号
    private int age;//年龄
    private String sexy;//性别
    private String cardNum;//身份证号或组织机构代码
    private String courtName;//执行法院
    private String areaName;//省份
    private String partyTypeName; //未知含义
    private String gistId; //执行依据文号
    private String regDate;//立案时间
    private String gistUnit;//做出执行依据单位
    private String duty;//生效法律文书确定的义务
    private String performance;//被执行人的履行情况 
    private String performedPart;//执行情况
    private String unperformPart;//未执行情况
    private String disruptTypeName;//失信被执行人行为具体情形
    private String publishDate;//发布时间
    
    private String execMoney; //执行标的 被执行人特有

    private String caseState;//0 未知 被执行人特有

    
    public String getExecMoney() {
		return execMoney;
	}
	public void setExecMoney(String execMoney) {
		this.execMoney = execMoney;
	}
	public String getCaseState() {
		return caseState;
	}
	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}
	private String taskid;
    
    private String type;//失信被执行人 或被执行人
    
    
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public long getShixinid() {
		return shixinid;
	}
	public void setShixinid(long shixinid) {
		this.shixinid = shixinid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSexy() {
		return sexy;
	}
	public void setSexy(String sexy) {
		this.sexy = sexy;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getPartyTypeName() {
		return partyTypeName;
	}
	public void setPartyTypeName(String partyTypeName) {
		this.partyTypeName = partyTypeName;
	}
	public String getGistId() {
		return gistId;
	}
	public void setGistId(String gistId) {
		this.gistId = gistId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getGistUnit() {
		return gistUnit;
	}
	public void setGistUnit(String gistUnit) {
		this.gistUnit = gistUnit;
	}
	
 	@Column(columnDefinition="text")
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	public String getPerformedPart() {
		return performedPart;
	}
	public void setPerformedPart(String performedPart) {
		this.performedPart = performedPart;
	}
	public String getUnperformPart() {
		return unperformPart;
	}
	public void setUnperformPart(String unperformPart) {
		this.unperformPart = unperformPart;
	}
 	@Column(columnDefinition="text")
	public String getDisruptTypeName() {
		return disruptTypeName;
	}
	public void setDisruptTypeName(String disruptTypeName) {
		this.disruptTypeName = disruptTypeName;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ShiXinBean [shixinid=" + shixinid + ", iname=" + iname + ", caseCode=" + caseCode + ", age=" + age
				+ ", sexy=" + sexy + ", cardNum=" + cardNum + ", courtName=" + courtName + ", areaName=" + areaName
				+ ", partyTypeName=" + partyTypeName + ", gistId=" + gistId + ", regDate=" + regDate + ", gistUnit="
				+ gistUnit + ", duty=" + duty + ", performance=" + performance + ", performedPart=" + performedPart
				+ ", unperformPart=" + unperformPart + ", disruptTypeName=" + disruptTypeName + ", publishDate="
				+ publishDate + ", execMoney=" + execMoney + ", caseState=" + caseState + ", taskid=" + taskid
				+ ", type=" + type + "]";
	}
   
    

}