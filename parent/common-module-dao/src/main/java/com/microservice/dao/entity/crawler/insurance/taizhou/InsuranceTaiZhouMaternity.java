package com.microservice.dao.entity.crawler.insurance.taizhou;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;
@Entity
@Table(name="insurance_taizhou_maternity",indexes = {@Index(name = "index_insurance_taizhou_maternity_taskid", columnList = "taskid")})
public class InsuranceTaiZhouMaternity extends IdEntity{

	private String companyNum;//单位编号
	private String compnay;//单位名称
	private String type;//生育费用类别
	private String hospitalName;//定点名称
	private String inDate;//入院时间
	private String outDate;//出院时间
	private String time;//结算时间
	private String babyBirth;//新生儿出生日期
	private String status;//生养方式
	private String liuChan;//流产类别
	private String shouShu;//计生手术类别
	private String sum;//总费用
	private String baoXiao;//报销金额
	private String jintie;//生育津贴
	private String jiSheng;//计生津贴
	private String liuJinTie;//流产津贴
	private String buZhu;//一次性营养补助
	private String name;//姓名
	private String IDNum;//身份证
	private String personalNum;//个人编号
	private String taskid;
	@Override
	public String toString() {
		return "InsuranceTaiZhouMaternity [companyNum=" + companyNum + ", compnay=" + compnay + ", type=" + type
				+ ", hospitalName=" + hospitalName + ", inDate=" + inDate + ", outDate=" + outDate + ", time=" + time
				+ ", babyBirth=" + babyBirth + ", status=" + status + ", liuChan=" + liuChan + ", shouShu=" + shouShu
				+ ", sum=" + sum + ", baoXiao=" + baoXiao + ", jintie=" + jintie + ", jiSheng=" + jiSheng
				+ ", liuJinTie=" + liuJinTie + ", buZhu=" + buZhu + ", name=" + name + ", IDNum=" + IDNum
				+ ", personalNum=" + personalNum + ", taskid=" + taskid + "]";
	}
	public String getCompanyNum() {
		return companyNum;
	}
	public void setCompanyNum(String companyNum) {
		this.companyNum = companyNum;
	}
	public String getCompnay() {
		return compnay;
	}
	public void setCompnay(String compnay) {
		this.compnay = compnay;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getInDate() {
		return inDate;
	}
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBabyBirth() {
		return babyBirth;
	}
	public void setBabyBirth(String babyBirth) {
		this.babyBirth = babyBirth;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLiuChan() {
		return liuChan;
	}
	public void setLiuChan(String liuChan) {
		this.liuChan = liuChan;
	}
	public String getShouShu() {
		return shouShu;
	}
	public void setShouShu(String shouShu) {
		this.shouShu = shouShu;
	}
	public String getSum() {
		return sum;
	}
	public void setSum(String sum) {
		this.sum = sum;
	}
	public String getBaoXiao() {
		return baoXiao;
	}
	public void setBaoXiao(String baoXiao) {
		this.baoXiao = baoXiao;
	}
	public String getJintie() {
		return jintie;
	}
	public void setJintie(String jintie) {
		this.jintie = jintie;
	}
	public String getJiSheng() {
		return jiSheng;
	}
	public void setJiSheng(String jiSheng) {
		this.jiSheng = jiSheng;
	}
	public String getLiuJinTie() {
		return liuJinTie;
	}
	public void setLiuJinTie(String liuJinTie) {
		this.liuJinTie = liuJinTie;
	}
	public String getBuZhu() {
		return buZhu;
	}
	public void setBuZhu(String buZhu) {
		this.buZhu = buZhu;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getPersonalNum() {
		return personalNum;
	}
	public void setPersonalNum(String personalNum) {
		this.personalNum = personalNum;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
}
