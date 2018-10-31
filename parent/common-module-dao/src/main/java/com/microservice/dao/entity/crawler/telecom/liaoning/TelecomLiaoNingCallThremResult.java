package com.microservice.dao.entity.crawler.telecom.liaoning;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "telecom_liaoning_callresult",indexes = {@Index(name = "index_telecom_liaoning_callresult_taskid", columnList = "taskid")})
public class TelecomLiaoNingCallThremResult extends IdEntity  {

	private String total;//总计费用

	private String duration;//  时长(秒)

	private String feeName;// 费用类型

	private String baseFee;// 基本费

	private String indbDate;// null

	private String otherFee;// 其他费用

	private String roamType;// null

	private String tollType;// null

	private String callDate;// 呼叫时间

	private String callType;// 呼叫类型
	
	private String cellId;//null
	
	private String cityName;//null
	
	private String counterAreaCode;//对方区号
	
	private String counterNumber;//对方号码
	
	private String favour;//优惠费
	
	private String queryMonth;//201708
	
	private String tollAdd;//长途费
	
	private String transUseAmont;//0
	
	private Integer userid;

	private String taskid;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(String baseFee) {
		this.baseFee = baseFee;
	}

	public String getIndbDate() {
		return indbDate;
	}

	public void setIndbDate(String indbDate) {
		this.indbDate = indbDate;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public String getRoamType() {
		return roamType;
	}

	public void setRoamType(String roamType) {
		this.roamType = roamType;
	}

	public String getTollType() {
		return tollType;
	}

	public void setTollType(String tollType) {
		this.tollType = tollType;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCounterAreaCode() {
		return counterAreaCode;
	}

	public void setCounterAreaCode(String counterAreaCode) {
		this.counterAreaCode = counterAreaCode;
	}

	public String getCounterNumber() {
		return counterNumber;
	}

	public void setCounterNumber(String counterNumber) {
		this.counterNumber = counterNumber;
	}

	public String getFavour() {
		return favour;
	}

	public void setFavour(String favour) {
		this.favour = favour;
	}

	public String getQueryMonth() {
		return queryMonth;
	}

	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}

	public String getTollAdd() {
		return tollAdd;
	}

	public void setTollAdd(String tollAdd) {
		this.tollAdd = tollAdd;
	}

	public String getTransUseAmont() {
		return transUseAmont;
	}

	public void setTransUseAmont(String transUseAmont) {
		this.transUseAmont = transUseAmont;
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

	public TelecomLiaoNingCallThremResult() {
		super();
	}

	public TelecomLiaoNingCallThremResult(String total, String duration, String feeName, String baseFee,
			String indbDate, String otherFee, String roamType, String tollType, String callDate, String callType,
			String cellId, String cityName, String counterAreaCode, String counterNumber, String favour,
			String queryMonth, String tollAdd, String transUseAmont, Integer userid, String taskid) {
		super();
		this.total = total;
		this.duration = duration;
		this.feeName = feeName;
		this.baseFee = baseFee;
		this.indbDate = indbDate;
		this.otherFee = otherFee;
		this.roamType = roamType;
		this.tollType = tollType;
		this.callDate = callDate;
		this.callType = callType;
		this.cellId = cellId;
		this.cityName = cityName;
		this.counterAreaCode = counterAreaCode;
		this.counterNumber = counterNumber;
		this.favour = favour;
		this.queryMonth = queryMonth;
		this.tollAdd = tollAdd;
		this.transUseAmont = transUseAmont;
		this.userid = userid;
		this.taskid = taskid;
	}

	@Override
	public String toString() {
		return "TelecomLiaoNingCallThremResult [total=" + total + ", duration=" + duration + ", feeName=" + feeName
				+ ", baseFee=" + baseFee + ", indbDate=" + indbDate + ", otherFee=" + otherFee + ", roamType="
				+ roamType + ", tollType=" + tollType + ", callDate=" + callDate + ", callType=" + callType
				+ ", cellId=" + cellId + ", cityName=" + cityName + ", counterAreaCode=" + counterAreaCode
				+ ", counterNumber=" + counterNumber + ", favour=" + favour + ", queryMonth=" + queryMonth
				+ ", tollAdd=" + tollAdd + ", transUseAmont=" + transUseAmont + ", userid=" + userid + ", taskid="
				+ taskid + "]";
	}

}
