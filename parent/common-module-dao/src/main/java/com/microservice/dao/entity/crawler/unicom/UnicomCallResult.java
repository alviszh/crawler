package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_callresult",indexes = {@Index(name = "index_unicom_callresult_taskid", columnList = "taskid")})
public class UnicomCallResult extends IdEntity {

	private String usernumber;//用户名
	
	private Integer userid;
	
	private String taskid;

	private String businesstype;//未知

	private String longtype;

	private String thtype;

	private String calledhome;//他人号码

	private String cellid;

	private String thtypeName;//语音电话

	private String twoplusfee;

	private String totalfee;//共消费

	private String calllonghour;//时长通话

	private String calldate;//通话日期

	private String calltime;//通话时间

	private String calltype;//通话类型 1 主叫 2 被叫

	private String othernum;

	private String otherarea;

	private String romatype;

	private String homearea;

	private String homenum;

	private String calltypeName;

	private String landtype;//国内 或国外

	private String romatypeName;// 国内长途  VPN呼叫

	private String homeareaName;

	private String otherareaName;

	private String nativefee;

	private String landfee;

	private String roamfee;

	private String deratefee;//收费

	private String otherfee;//附加费
	

	public String getUsernumber() {
		return usernumber;
	}


	public void setUsernumber(String usernumber) {
		this.usernumber = usernumber;
	}


	public Integer getUserid() {
		return userid;
	}


	public void setUserid(Integer userid) {
		this.userid = userid;
	}


	public String getTaskid() {
		return taskid;
	}


	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}


	public String getBusinesstype() {
		return businesstype;
	}


	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}


	public String getLongtype() {
		return longtype;
	}


	public void setLongtype(String longtype) {
		this.longtype = longtype;
	}


	public String getThtype() {
		return thtype;
	}


	public void setThtype(String thtype) {
		this.thtype = thtype;
	}


	public String getCalledhome() {
		return calledhome;
	}


	public void setCalledhome(String calledhome) {
		this.calledhome = calledhome;
	}


	public String getCellid() {
		return cellid;
	}


	public void setCellid(String cellid) {
		this.cellid = cellid;
	}


	public String getThtypeName() {
		return thtypeName;
	}


	public void setThtypeName(String thtypeName) {
		this.thtypeName = thtypeName;
	}


	public String getTwoplusfee() {
		return twoplusfee;
	}


	public void setTwoplusfee(String twoplusfee) {
		this.twoplusfee = twoplusfee;
	}


	public String getTotalfee() {
		return totalfee;
	}


	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}


	public String getCalllonghour() {
		return calllonghour;
	}


	public void setCalllonghour(String calllonghour) {
		this.calllonghour = calllonghour;
	}


	public String getCalldate() {
		return calldate;
	}


	public void setCalldate(String calldate) {
		this.calldate = calldate;
	}


	public String getCalltime() {
		return calltime;
	}


	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}


	public String getCalltype() {
		return calltype;
	}


	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}


	public String getOthernum() {
		return othernum;
	}


	public void setOthernum(String othernum) {
		this.othernum = othernum;
	}


	public String getOtherarea() {
		return otherarea;
	}


	public void setOtherarea(String otherarea) {
		this.otherarea = otherarea;
	}


	public String getRomatype() {
		return romatype;
	}


	public void setRomatype(String romatype) {
		this.romatype = romatype;
	}


	public String getHomearea() {
		return homearea;
	}


	public void setHomearea(String homearea) {
		this.homearea = homearea;
	}


	public String getHomenum() {
		return homenum;
	}


	public void setHomenum(String homenum) {
		this.homenum = homenum;
	}


	public String getCalltypeName() {
		return calltypeName;
	}


	public void setCalltypeName(String calltypeName) {
		this.calltypeName = calltypeName;
	}


	public String getLandtype() {
		return landtype;
	}


	public void setLandtype(String landtype) {
		this.landtype = landtype;
	}


	public String getRomatypeName() {
		return romatypeName;
	}


	public void setRomatypeName(String romatypeName) {
		this.romatypeName = romatypeName;
	}


	public String getHomeareaName() {
		return homeareaName;
	}


	public void setHomeareaName(String homeareaName) {
		this.homeareaName = homeareaName;
	}


	public String getOtherareaName() {
		return otherareaName;
	}


	public void setOtherareaName(String otherareaName) {
		this.otherareaName = otherareaName;
	}


	public String getNativefee() {
		return nativefee;
	}


	public void setNativefee(String nativefee) {
		this.nativefee = nativefee;
	}


	public String getLandfee() {
		return landfee;
	}


	public void setLandfee(String landfee) {
		this.landfee = landfee;
	}


	public String getRoamfee() {
		return roamfee;
	}


	public void setRoamfee(String roamfee) {
		this.roamfee = roamfee;
	}


	public String getDeratefee() {
		return deratefee;
	}


	public void setDeratefee(String deratefee) {
		this.deratefee = deratefee;
	}


	public String getOtherfee() {
		return otherfee;
	}


	public void setOtherfee(String otherfee) {
		this.otherfee = otherfee;
	}

	public String toStringmd5() {
		return "UnicomResult [usernumber=" + usernumber + ", calldate=" + calldate + ", calltime=" + calltime
				+ ", othernum=" + othernum + "]";
	}

	@Override
	public String toString() {
		return "UnicomCallResult [usernumber=" + usernumber + ", userid=" + userid + ", taskid=" + taskid
				+ ", businesstype=" + businesstype + ", longtype=" + longtype + ", thtype=" + thtype + ", calledhome="
				+ calledhome + ", cellid=" + cellid + ", thtypeName=" + thtypeName + ", twoplusfee=" + twoplusfee
				+ ", totalfee=" + totalfee + ", calllonghour=" + calllonghour + ", calldate=" + calldate + ", calltime="
				+ calltime + ", calltype=" + calltype + ", othernum=" + othernum + ", otherarea=" + otherarea
				+ ", romatype=" + romatype + ", homearea=" + homearea + ", homenum=" + homenum + ", calltypeName="
				+ calltypeName + ", landtype=" + landtype + ", romatypeName=" + romatypeName + ", homeareaName="
				+ homeareaName + ", otherareaName=" + otherareaName + ", nativefee=" + nativefee + ", landfee="
				+ landfee + ", roamfee=" + roamfee + ", deratefee=" + deratefee + ", otherfee=" + otherfee + "]";
	}
	
	

}
