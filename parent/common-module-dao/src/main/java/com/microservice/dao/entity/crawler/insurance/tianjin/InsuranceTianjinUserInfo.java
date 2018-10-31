package com.microservice.dao.entity.crawler.insurance.tianjin;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name="insurance_tianjin_userinfo",indexes = {@Index(name = "index_insurance_tianjin_userinfo_taskid", columnList = "taskid")}) 
public class InsuranceTianjinUserInfo extends IdEntity{

	private String taskid;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	private String name;   //姓名name
	private String nation;  //民族nation
	private String cardType; //证件类型
    private String cardNum;  //社保卡号idso
    private String cardBank; //社保卡开卡银行
    private String country;  //国家
    private String workType; //离退休状态
    private String residence; //户口性质
    private String sex;      //性别
    private String birthday; //出生日期
    private String zjNum;    //证件号码idnum
    private String phone;    //手机号码 
    private String cardStatus;  //社保卡激活状态
    private String joinWork;   //参加工作日期
    private String postalcode;  //邮政编码
    private String addr;      //户口所在地详址
    private String outWork;   //离退休日期
    private String registerNum;  //户口本号码
    private String fixedNum;    //固定电话
    private String livingAddress;  //居住地址
    private String activeStatus;  //激活状态
	public InsuranceTianjinUserInfo(String name, String nation, String cardType, String cardNum, String cardBank,
			String country, String workType, String residence, String sex, String birthday, String zjNum, String phone,
			String cardStatus, String joinWork, String postalcode, String addr, String outWork, String registerNum,
			String fixedNum, String livingAddress, String activeStatus) {
		super();
		this.name = name;
		this.nation = nation;
		this.cardType = cardType;
		this.cardNum = cardNum;
		this.cardBank = cardBank;
		this.country = country;
		this.workType = workType;
		this.residence = residence;
		this.sex = sex;
		this.birthday = birthday;
		this.zjNum = zjNum;
		this.phone = phone;
		this.cardStatus = cardStatus;
		this.joinWork = joinWork;
		this.postalcode = postalcode;
		this.addr = addr;
		this.outWork = outWork;
		this.registerNum = registerNum;
		this.fixedNum = fixedNum;
		this.livingAddress = livingAddress;
		this.activeStatus = activeStatus;
	}
	public InsuranceTianjinUserInfo() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardBank() {
		return cardBank;
	}
	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getZjNum() {
		return zjNum;
	}
	public void setZjNum(String zjNum) {
		this.zjNum = zjNum;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getJoinWork() {
		return joinWork;
	}
	public void setJoinWork(String joinWork) {
		this.joinWork = joinWork;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getOutWork() {
		return outWork;
	}
	public void setOutWork(String outWork) {
		this.outWork = outWork;
	}
	public String getRegisterNum() {
		return registerNum;
	}
	public void setRegisterNum(String registerNum) {
		this.registerNum = registerNum;
	}
	public String getFixedNum() {
		return fixedNum;
	}
	public void setFixedNum(String fixedNum) {
		this.fixedNum = fixedNum;
	}
	public String getLivingAddress() {
		return livingAddress;
	}
	public void setLivingAddress(String livingAddress) {
		this.livingAddress = livingAddress;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Override
	public String toString() {
		return "InsuranceTianjinUserInfo [name=" + name + ", nation=" + nation + ", cardType=" + cardType + ", cardNum="
				+ cardNum + ", cardBank=" + cardBank + ", country=" + country + ", workType=" + workType
				+ ", residence=" + residence + ", sex=" + sex + ", birthday=" + birthday + ", zjNum=" + zjNum
				+ ", phone=" + phone + ", cardStatus=" + cardStatus + ", joinWork=" + joinWork + ", postalcode="
				+ postalcode + ", addr=" + addr + ", outWork=" + outWork + ", registerNum=" + registerNum
				+ ", fixedNum=" + fixedNum + ", livingAddress=" + livingAddress + ", activeStatus=" + activeStatus
				+ "]";
	}
	
    
    
}
